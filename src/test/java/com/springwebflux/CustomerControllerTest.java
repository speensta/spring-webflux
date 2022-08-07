package com.springwebflux;

import com.springwebflux.domain.Customer;
import com.springwebflux.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

//@SpringBootTest
@WebFluxTest
public class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void test1() {

        when(customerRepository.findById(1L)).thenReturn(Mono.just(new Customer("Jack","Bauer")));

        webTestClient.get().uri("/customer/{id}", 1L)
                .exchange()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("Jack")
                .jsonPath("$.lastName").isEqualTo("Bauer");


    }

}
