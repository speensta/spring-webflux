package com.springwebflux.controller;

import com.springwebflux.domain.Customer;
import com.springwebflux.repository.CustomerRepository;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
public class CustomerController {

    private CustomerRepository customerRepository;
    private Sinks.Many<Customer> slink;//stream Flux.merge

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.slink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping("/flux")
    public Flux<Integer> flux() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> fluxstream() {
        return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/customer", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Customer> findAll() {
        return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/customer/{id}")
    public Mono<Customer> findById(@PathVariable Long id) {
        return customerRepository.findById(id).log();
    }

    @GetMapping(value = "/customer/sse")
    public Flux<ServerSentEvent<Customer>> findAllSSE() {
        return slink.asFlux().map(c -> ServerSentEvent.builder(c).build())
                .doOnCancel(() -> slink.asFlux().blockLast());
    }

    @PostMapping(value = "/customer")
    public Mono<Customer> save() {
        return customerRepository.save(new Customer("test1","test2")).doOnNext(c -> slink.tryEmitNext(c));
    }

}
