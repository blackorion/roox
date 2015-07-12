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
import social.customers.Customer;
import social.utils.CustomerUtilService;
import social.utils.RequestService;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RooxTestApplication.class)
@WebIntegrationTest
public class AuthorizationTests {
    @Autowired
    private CustomerUtilService customerUtilService;

    @Test
    public void notAuthorizedUser() {
        ResponseEntity<Customer> responseEntity = get("/customers", "non existing user", "any password");

        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void authorizedUserIsNotAbleToEnterOtherUserData() {
        Customer user = customerUtilService.createTestUser("user");
        ResponseEntity<Customer> responseEntity = get("/customers/1" + user.id, "user", "user");

        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.FORBIDDEN));
    }

    private ResponseEntity<Customer> get(String url, String username, String password) {

        return new TestRestTemplate()
                .exchange(RequestService.generateUrl(url), HttpMethod.GET, createAuthorizationHeader(username, password), Customer.class);
    }

    private HttpEntity<String> createAuthorizationHeader(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s:%s", username, password));

        return new HttpEntity<>(headers);
    }

    @After
    public void tearDown() {
        customerUtilService.clearDatabase();
    }
}