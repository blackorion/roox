package social.utils;

import social.customers.partnermapping.PartnerMapping;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PartnerMappingAssertions {
    public static void assertThatMappingObjectsAreSame(PartnerMapping expected, PartnerMapping actual) {
        assertThat(expected.id, equalTo(actual.id));
        assertThat(expected.accId, equalTo(actual.accId));
        assertThat(expected.partnerId, equalTo(actual.partnerId));

        if (expected.customer == null)
            assertThat(actual.customer, is(nullValue()));
        else {
            assertThat(actual.customer, is(not(nullValue())));
            assertThat(expected.customer.id, equalTo(actual.customer.id));
        }
    }
}
