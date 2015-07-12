package social.auth;

import org.springframework.stereotype.Service;
import social.auth.CurrentUserDetailsService.CurrentUser;
import social.customers.Customer;

@Service
public class CurrentUserAccessByIdAndStateService implements CurrentUserAccessService {
    @Override
    public boolean canAccessUser(CurrentUser user, Long userId) {
        return user != null
                && user.getId().equals(userId)
                && user.getCustomer().state.equals(Customer.ActiveState.ACTIVE);
    }
}
