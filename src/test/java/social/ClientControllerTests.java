package social;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import social.customers.Customer;
import social.utils.CustomerAssertions;
import social.utils.CustomerUtilService;
import social.utils.RequestService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RooxTestApplication.class)
@WebIntegrationTest
public class ClientControllerTests
{
    @Autowired
    private CustomerUtilService customerUtilService;

    @Test
    public void clientCanSeeHisData() {
        Customer user = customerUtilService.createTestUser("user");

        ResponseEntity<Customer> responseEntity = get("/customers/" + user.id, "user", "user");

        CustomerAssertions.assertThatCustomerObjectsAreSame(user, responseEntity.getBody());
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
