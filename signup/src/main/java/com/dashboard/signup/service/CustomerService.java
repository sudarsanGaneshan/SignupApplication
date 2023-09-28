package com.dashboard.signup.service;

import com.dashboard.signup.persistance.entity.Customer;
import com.dashboard.signup.persistance.repository.CustomerRepository;
import com.dashboard.signup.utils.userAccessEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    public Customer createUser(Customer signup){
       signup.setId(sequenceGeneratorService.generateSequence(signup.SEQUENCE_NAME));
        signup.setUserAccess(userAccessEnum.PERMITTED.name());
        return customerRepository.save(signup);
    }


    public void updateUser(Customer user, userAccessEnum userAccess){
        user.setUserAccess(userAccess.name());
        customerRepository.save(user);
    }

    public Optional<Customer> findUserById(Long userId){
        String userIdString = Long.toString(userId);
        Optional<Customer> user = customerRepository.findById(userId);
        return user;
    }

    public List<Customer> fetchAllUsers(){
        return customerRepository.findAll();
    }

    public Boolean isSignupRequestValid(Customer signup){
        System.out.println(signup.toString());
        if((signup == null) || (signup.getFirstName() == null ||
           signup.getLastName() == null ||
           signup.getEmail() == null ||
           signup.getUserName() == null ||
           signup.getPassword() == null)) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean isUserValidforRegistration(String userName){
        return customerRepository.findByuserName(userName) == null;
    }

    public Boolean isUserSigninValid(String userName, String password){
        System.out.println("Inside valid user");
        return ((userName.isEmpty()) && (password.isEmpty())) ? false : true;
    }

    public  HttpStatus authenticate (String userName, String password){
        System.out.println("authenticate");
        Customer user =  customerRepository.findByuserName(userName);
        if(user != null){
            return (user.getPassword().equals(password) && user.getUserAccess().equals(userAccessEnum.PERMITTED.name())) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }
}
