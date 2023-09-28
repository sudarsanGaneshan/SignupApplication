package com.dashboard.signup.persistance.repository;

import com.dashboard.signup.persistance.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, Long> {

    public Customer findByuserName(String userName);

}
