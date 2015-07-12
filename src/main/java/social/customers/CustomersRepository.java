package social.customers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomersRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String s);
}
