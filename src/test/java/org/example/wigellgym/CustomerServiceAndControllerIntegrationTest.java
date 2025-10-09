//package org.example.wigellgym;
//
//import org.example.wigellgym.entities.GymCustomer;
//import org.example.wigellgym.repositories.GymCustomerRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class CustomerServiceAndControllerIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private GymCustomerRepository gymCustomerRepository;
//
//    @BeforeEach
//    void setup() {
//        gymCustomerRepository.deleteAll();
//
//    }
//
//    @Test
//    void creatingCustomerShouldReturn201() {
//        GymCustomer customer = new GymCustomer(null, "Lisa", "Andersson",
//                "lisa");
//
//
//        ResponseEntity<String> response = restTemplate
//                .withBasicAuth("joey", "joey")
//                .postForEntity("/api/wigellgym/customers", customer, String.class);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertTrue(response.getBody().contains("Lisa"));
//    }
//
//    @Test
//    void creatingCustomerWithMissingUsernameShouldReturnBadRequest() {
//        GymCustomer customer = new GymCustomer(null, "Olle", "Karlsson",
//                null);
//
//        ResponseEntity<String> response = restTemplate
//                .withBasicAuth("joey", "joey")
//                .postForEntity("/api/wigellgym/customers", customer, String.class);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertTrue(response.getBody().contains("Customer needs a username."));
//
//    }
//
//    @Test
//    void getAllCustomersShouldReturnList() {
//        GymCustomer customer1 = new GymCustomer(null, "A", "B",
//                "a");
//        GymCustomer customer2= new GymCustomer(null, "C", "D",
//                "c");
//        gymCustomerRepository.save(customer1);
//        gymCustomerRepository.save(customer2);
//
//        ResponseEntity<String> response = restTemplate
//                .withBasicAuth("joey", "joey")
//                .getForEntity("/api/wigellgym/customers", String.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody().contains("A"));
//        assertTrue(response.getBody().contains("C"));
//    }
//}
