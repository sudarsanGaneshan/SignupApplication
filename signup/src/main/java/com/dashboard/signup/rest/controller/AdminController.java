/*
AdminController is used to interact with the admin dashboard
* */
package com.dashboard.signup.rest.controller;

import com.dashboard.signup.persistance.entity.Customer;
import com.dashboard.signup.service.CustomerService;
import com.dashboard.signup.utils.userAccessEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    @Autowired
    private CustomerService signupService;


    @GetMapping("/fetch/users")
    public ResponseEntity<?> fetchAllUsers() {
        List<Customer> users = signupService.fetchAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users/{userId}/update")
    public ResponseEntity<?> updateUserAccess(@PathVariable(value = "userId") Long userId,
                                              @RequestParam("userAccess") userAccessEnum userAccess) {

        Customer user = signupService.findUserById(userId);
        if(user != null){
            signupService.updateUser(user, userAccess);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
