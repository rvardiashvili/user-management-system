package org.example.employeemanagement.controllers;


import org.example.employeemanagement.dto.*;
import org.example.employeemanagement.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.employeemanagement.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<UserResponse> addAddress(@Valid @RequestBody AddressRequest addressRequest, Authentication authentication) {
        return ResponseEntity.ok(addressService.addAddress(authentication.getName(), addressRequest));
    }
    @PutMapping("/address/{address_id}")
    public ResponseEntity<UserResponse> editAddress(@PathVariable Long address_id, @Valid @RequestBody AddressRequest addressRequest, Authentication authentication) {
        return ResponseEntity.ok(addressService.editAddress(address_id, addressRequest, authentication.getName()));
    }

    @DeleteMapping("/address/{address_id}")
    public ResponseEntity<UserResponse> deleteAddress(@PathVariable Long address_id, Authentication authentication) {
        return ResponseEntity.ok(addressService.deleteAddress(address_id, authentication.getName()));
    }

    @PostMapping("/address/{user_id}")
    @PreAuthorize("hasAuthority('edit-users')")
    public ResponseEntity<UserResponse> addAddress(@PathVariable Long user_id, @Valid @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(addressService.addAddress(user_id, addressRequest));
    }
    @PutMapping("/address/{user_id}/{address_id}")
    @PreAuthorize("hasAuthority('edit-users')")
    public ResponseEntity<UserResponse> editAddress(@PathVariable Long user_id, @PathVariable Long address_id, @Valid @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(addressService.editAddress(user_id, address_id, addressRequest));
    }

    @DeleteMapping("/address/{user_id}/{address_id}")
    public ResponseEntity<UserResponse> deleteAddress(@PathVariable Long user_id, @PathVariable Long address_id) {
        return ResponseEntity.ok(addressService.deleteAddress(user_id, address_id));
    }

}