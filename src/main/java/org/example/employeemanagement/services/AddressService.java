package org.example.employeemanagement.services;

import org.example.employeemanagement.domain.*;
import org.example.employeemanagement.dto.*;
import org.example.employeemanagement.repositories.AddressRepository;
import org.example.employeemanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }

    @Transactional
    public UserResponse deleteAddress(Long address_id, String email) {
        User user = getUserByEmail(email);
        Address address = addressRepository.findById(address_id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + address_id));
        if( user.getPerson().getAddresses() != null && user.getPerson().getAddresses().contains(address) ) {
            user.getPerson().getAddresses().remove(address);
            userRepository.save(user);
            addressRepository.deleteById(address_id);
            System.out.println("Deleted address with id: " + address_id);
        }
        return getDetails(email);
    }

    @Transactional
    public UserResponse addAddress(String email ,AddressRequest request) {
        User user = getUserByEmail(email);
        Person person = user.getPerson();
        Set<Address> addresses = person.getAddresses();
        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        addresses.add(address);
        person.setAddresses(addresses);
        try{
            userRepository.save(user);
        }catch (DataAccessException e){
            throw e;
        }
        return getDetails(email);
    }

    @Transactional
    public UserResponse editAddress(Long address_id, AddressRequest addressRequest, String email) {
        User user = getUserByEmail(email);
        Person person = user.getPerson();
        Set<Address> addresses = person.getAddresses();
        Address address = addressRepository.findById(address_id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + address_id));
        if(addresses != null && addresses.contains(address)) {
            if(addressRequest.getStreet() != null && !addressRequest.getStreet().isBlank())
                address.setStreet(addressRequest.getStreet());
            if(addressRequest.getCity() != null && !addressRequest.getCity().isBlank())
                address.setCity(addressRequest.getCity());
            if(addressRequest.getState() != null && !addressRequest.getState().isBlank())
                address.setState(addressRequest.getState());
            if(addressRequest.getPostalCode() != null && !addressRequest.getPostalCode().isBlank())
                address.setPostalCode(addressRequest.getPostalCode());
            if(addressRequest.getCountry() != null && !addressRequest.getCountry().isBlank())
                address.setCountry(addressRequest.getCountry());
        }
        try {
            addressRepository.save(address);
        }catch (DataAccessException e){
            throw e;
        }
        return getDetails(email);
    }

    @Transactional
    public UserResponse deleteAddress(Long user_id, Long address_id) {
        User user = getUserById(user_id);
        Address address = addressRepository.findById(address_id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + address_id));
        if( user.getPerson().getAddresses() != null && user.getPerson().getAddresses().contains(address) ) {
            user.getPerson().getAddresses().remove(address);
            try {
                userRepository.save(user);
                addressRepository.deleteById(address_id);
            }catch (DataAccessException e){
                throw e;
            }
        }
        return getDetails(user.getEmail());
    }

    @Transactional
    public UserResponse addAddress(Long user_id, AddressRequest request) {
        User user = getUserById(user_id);
        Person person = user.getPerson();
        Set<Address> addresses = person.getAddresses();
        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        addresses.add(address);
        person.setAddresses(addresses);
        try {
            userRepository.save(user);
        }catch (DataAccessException e){
            throw e;
        }
        return getDetails(user.getEmail());
    }

    @Transactional
    public UserResponse editAddress( Long user_id ,Long address_id, AddressRequest addressRequest) {
        User user = getUserById(user_id);
        Person person = user.getPerson();
        Set<Address> addresses = person.getAddresses();
        Address address = addressRepository.findById(address_id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + address_id));
        if(addresses != null && addresses.contains(address)) {
            if(addressRequest.getStreet() != null && !addressRequest.getStreet().isBlank())
                address.setStreet(addressRequest.getStreet());
            if(addressRequest.getCity() != null && !addressRequest.getCity().isBlank())
                address.setCity(addressRequest.getCity());
            if(addressRequest.getState() != null && !addressRequest.getState().isBlank())
                address.setState(addressRequest.getState());
            if(addressRequest.getPostalCode() != null && !addressRequest.getPostalCode().isBlank())
                address.setPostalCode(addressRequest.getPostalCode());
            if(addressRequest.getCountry() != null && !addressRequest.getCountry().isBlank())
                address.setCountry(addressRequest.getCountry());
        }
        try {
            addressRepository.save(address);
        }catch (DataAccessException e){
            throw e;
        }
        return getDetails(user.getEmail());
    }



    public UserResponse getDetails(String email) {

        User user = getUserByEmail(email);
        Person person = user.getPerson();

        UserResponse dto = new UserResponse();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        if(person.getDateOfBirth() != null) {
            dto.setDateOfBirth(person.getDateOfBirth().toString());
        }
        dto.setRole(person.getRole());
        dto.setPermissions(person.getPermissions());
        dto.setAddresses(person.getAddresses());
        return dto;
    }

}
