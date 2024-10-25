//package cs4337.group_8.AuthenticationMicroservice.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class OAuthTokenFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String accessToken = authHeader.substring(7);
//
//            if (isTokenExpired(accessToken)) {
//                OAuth2AccessTokenResponse accessTokenResponse = refreshAccessToken("refreshToken");
//                storeAccessToken(accessTokenResponse);
//                storeRefreshToken(accessTokenResponse);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private boolean isTokenExpired(String accessToken) {
//        return false;
//    }
//
//    private OAuth2AccessTokenResponse refreshAccessToken(String refreshToken) {
//        return null;
//    }
//
//    private void storeAccessToken(OAuth2AccessTokenResponse accessTokenResponse) {
//
//    }
//
//    private void storeRefreshToken(OAuth2AccessTokenResponse accessTokenResponse) {
//
//    }
//}
