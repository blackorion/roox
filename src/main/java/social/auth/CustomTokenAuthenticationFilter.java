package social.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String AUTH_HEADER = "Authorization";

    public CustomTokenAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        return authenticationFromToken(request);
    }

    private Authentication authenticationFromToken(HttpServletRequest request) {
        String[] split = parseCredentialsFromToken(request);

        if (split.length != 2)
            return null;

        String username = split[0];
        String password = split[1];

        return new UsernamePasswordAuthenticationToken(username, password);
    }

    private String[] parseCredentialsFromToken(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER);
        token = token.replaceFirst("Bearer ", "");

        return token.split(":");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, final FilterChain chain) throws IOException, ServletException {
        //This filter only applies if the header is present
        if (StringUtils.isEmpty(((HttpServletRequest) req).getHeader(AUTH_HEADER))) {
            chain.doFilter(req, res);
            return;
        }

        //On success keep going on the chain
        this.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                chain.doFilter(request, response);
            }
        });

        super.doFilter(req, res, chain);
    }
}
