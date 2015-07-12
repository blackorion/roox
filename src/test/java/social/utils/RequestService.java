package social.utils;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@Service
public class RequestService {
    public static String generateUrl(String url) {
        return "http://localhost:8080" + url;
    }

    public static <T> ResponseEntity<T> get(String url, String username, String password, Class<T> responseType) {
        return exchange(url, username, password, responseType, HttpMethod.GET);
    }

    public static <T> ResponseEntity<T> post(String url, String username, String password, Class<T> responseType, T body) {
        return exchange(url, username, password, responseType, HttpMethod.POST, body);
    }

    public static <T> ResponseEntity<T> put(String url, String username, String password, Class<T> responseType, T body) {
        return exchange(url, username, password, responseType, HttpMethod.PUT, body);
    }

    public static <T> ResponseEntity<T> exchange(String url, String username, String password, Class<T> responseType, HttpMethod method, T body) {
        try {
            RequestEntity<T> requestEntity = new RequestEntity<T>(body, method, new URI(url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return new TestRestTemplate()
                .exchange(RequestService.generateUrl(url), method, createAuthorizationHeader(username, password), responseType);
    }

    public static <T> ResponseEntity<T> exchange(String url, String username, String password, Class<T> responseType, HttpMethod method) {
        return exchange(url, username, password, responseType, null);
    }

    public static HttpEntity<String> createAuthorizationHeader(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", String.format("Bearer %s:%s", username, password));

        return new HttpEntity<>(headers);
    }
}
