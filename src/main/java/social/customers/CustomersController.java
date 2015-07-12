package social.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import social.auth.CurrentUserDetailsService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class CustomersController {
    @Autowired
    CustomersRepository repository;

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    @ResponseBody
    public List<Customer> index() {
        return repository.findAll();
    }

    @PreAuthorize("@currentUserAccessByIdAndStateService.canAccessUser(principal, #id)")
    @RequestMapping(value = "/customers/{id:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public Customer show(@PathVariable("id") Long id) {
        return repository.findOne(id);
    }

    @RequestMapping(value = {"/customers/@me{additional-data}", "/customers/@me/{additional-data:.*}"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public void folowMe(@PathVariable("additional-data") String additionalData, @AuthenticationPrincipal CurrentUserDetailsService.CurrentUser principal, HttpServletResponse response) throws IOException {
        if (!additionalData.isEmpty())
            additionalData = "/" + additionalData;

        response.sendRedirect("/customers/" + principal.getId() + additionalData);
    }

    @RequestMapping(value = "/create-test-customer/{username}/{password}", method = RequestMethod.GET)
    @ResponseBody
    public void createTestCustomer(@PathVariable("username") String username, @PathVariable("password") String password) {
        Customer customer = new Customer();
        customer.username = username;
        customer.passwordHash = new BCryptPasswordEncoder().encode(password);
        customer.state = Customer.ActiveState.ACTIVE;

        repository.save(customer);
    }
}
