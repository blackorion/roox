package social.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import social.customers.Customer;
import social.customers.CustomersRepository;

@Service
public class CurrentUserDetailsService implements UserDetailsService {
    @Autowired
    private CustomersRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username=%s was not found", username)));

        return new CurrentUser(customer);
    }

    public static class CurrentUser extends User {
        private Customer customer;

        public CurrentUser(Customer customer) {
            super(customer.username, customer.passwordHash, AuthorityUtils.createAuthorityList("USER"));

            this.customer = customer;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Long getId() {
            return customer.id;
        }
    }
}
