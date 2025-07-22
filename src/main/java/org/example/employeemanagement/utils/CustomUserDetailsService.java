package org.example.employeemanagement.utils;

import org.example.employeemanagement.domain.Permission;
import org.example.employeemanagement.domain.Person;
import org.example.employeemanagement.domain.Role;
import org.example.employeemanagement.repositories.UserRepository;
import org.example.employeemanagement.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException("Email cannot be empty.");
        }
        System.out.println(email);
        System.out.println("logging in");


        User user = userRepository.findByEmailWithDetails(email)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
        System.out.println("logged in");
        Collection<? extends GrantedAuthority> authorities = getAuthorities(user.getPerson());
        System.out.println("authorities: " + authorities);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                authorities
        );
    }


    private Collection<? extends GrantedAuthority> getAuthorities(Person person) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        Set<Role> roles = person.getRoles();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        for (Permission permission : person.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }
        return authorities;
    }
}