package social.utils;

import social.customers.Customer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CustomerAssertions
{
    public static void assertThatCustomerObjectsAreSame(Customer expected, Customer actual) {
        assertThat(expected.id, equalTo(actual.id));
        assertThat(expected.username, equalTo(actual.username));
        assertThat(expected.mappingList.size(), equalTo(actual.mappingList.size()));
    }
}
