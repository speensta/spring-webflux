package com.springwebflux.repository;

import com.springwebflux.domain.Customer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

    @Query("select * from customer WHERE last_name = :lastname")
    Flux<Customer> findByLastName(String lastName);
}
