package social.utils;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import social.customers.Customer;
import social.customers.CustomersRepository;
import social.customers.partnermapping.PartnerMapping;

import java.util.List;

@Service
public class CustomerUtilService
{
    @Autowired
    private CustomersRepository customersRepository;

    public Customer createTestUser(String username) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Customer customer = new Customer();
        customer.username = username;
        customer.passwordHash = encoder.encode(username);
        customer.state = Customer.ActiveState.ACTIVE;

        return customersRepository.save(customer);
    }

    public void clearDatabase() {
        customersRepository.deleteAll();
    }

    public Customer find(long id) {
        return customersRepository.findOne(id);
    }

    public List<PartnerMapping> fetchCustomerMappings(Long customerId) {
        Customer customer = customersRepository.findOne(customerId);
        Hibernate.initialize(customer.mappingList);
        List<PartnerMapping> list =  customer.mappingList;

        return list;
    }
}
