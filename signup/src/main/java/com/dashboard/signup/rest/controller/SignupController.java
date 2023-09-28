/*
SignupController is used to interact with the Signup and Login modul
* */

package com.dashboard.signup.rest.controller;

import com.dashboard.signup.persistance.entity.Customer;
import com.dashboard.signup.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/signup")
public class SignupController {

    @Autowired
    private CustomerService customerService;


    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody Customer signup) {

        if(customerService.isSignupRequestValid(signup)){
            if(customerService.isUserValidforRegistration(signup.getUserName())){
                customerService.createUser(signup);
                return new ResponseEntity<>(HttpStatus.OK);
            } else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> createSession(@RequestParam("userName") String userName,
                                             @RequestParam("password") String password,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        if(customerService.isUserSigninValid(userName,password)){
            return new ResponseEntity<>(customerService.authenticate(userName,password));
        } else {
            System.out.println(String.format("Request from %s is invalid", request.getRemoteAddr()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
