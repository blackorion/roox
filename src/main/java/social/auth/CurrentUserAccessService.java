package social.auth;

public interface CurrentUserAccessService {
    boolean canAccessUser(CurrentUserDetailsService.CurrentUser user, Long userId);
}
