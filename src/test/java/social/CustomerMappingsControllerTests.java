package social;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import social.customers.Customer;
import social.customers.partnermapping.PartnerMapping;
import social.utils.CustomerUtilService;
import social.utils.PartnerMappingsUtilService;
import social.utils.RequestService;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RooxTestApplication.class)
@WebIntegrationTest
public class CustomerMappingsControllerTests {
    @Autowired
    private CustomerUtilService customerUtilService;
    @Autowired
    private PartnerMappingsUtilService partnerMappingsUtilService;

    @Test
    public void clientCanFetchAllHisMappings() {
        Customer user = customerUtilService.createTestUser("user");
        partnerMappingsUtilService.createMapping(user);
        partnerMappingsUtilService.createMapping(user);
        partnerMappingsUtilService.createMapping(user);

        ResponseEntity<List> responseEntity = RequestService.get("/customers/" + user.id + "/partner-mappings", "user", "user", List.class);

        assertThat(responseEntity.getBody().size(), is(3));
    }

    @Test
    public void clientCanCreateNewMapping() {
        Customer user = customerUtilService.createTestUser("user");
        PartnerMapping stubMapping = new PartnerMapping();
        stubMapping.customer = user;

        ResponseEntity<PartnerMapping> post = RequestService.post("/customers/" + user.id + "/partner-mappings", "user", "user", PartnerMapping.class, stubMapping);

        List<?> mappings = customerUtilService.fetchCustomerMappings(user.id);
        assertThat(mappings.size(), is(1));
    }

    @Test
    public void clientCanModifyHisMappings() throws URISyntaxException {
        Customer user = customerUtilService.createTestUser("user");
        PartnerMapping mapping = partnerMappingsUtilService.createMapping(user);
        mapping.accId = 222L;

        RequestService.put("/customers/" + user.id + "/partner-mappings/" + mapping.id, "user", "user", PartnerMapping.class, mapping);

        PartnerMapping result = partnerMappingsUtilService.find(mapping.id);

        assertThat(result.accId, is(222L));
    }

    @After
    public void tearDown() {
        partnerMappingsUtilService.clearDatabase();
        customerUtilService.clearDatabase();
    }
}
