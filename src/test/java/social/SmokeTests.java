package social;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import social.customers.Customer;
import social.utils.CustomerUtilService;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RooxTestApplication.class)
@WebIntegrationTest
public class SmokeTests {
    @Autowired
    private CustomerUtilService customerUtilService;

    @Test
    public void ttt() {
        Customer user = customerUtilService.createTestUser("user");

        RestTemplate template = new TestRestTemplate();
        String url = "http://localhost:8080/customers/" + user.id;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer user:user");
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Object> exchange = template.exchange(url, HttpMethod.GET, request, Object.class);

        assertTrue(exchange.getStatusCode().equals(HttpStatus.OK));
    }

    @After
    public void tearDown() {
        customerUtilService.clearDatabase();
    }
}
