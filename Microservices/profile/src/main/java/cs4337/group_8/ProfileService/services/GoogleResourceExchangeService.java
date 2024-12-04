package cs4337.group_8.ProfileService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs4337.group_8.ProfileService.mappers.GoogleUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class GoogleResourceExchangeService {


    public GoogleResourceExchangeService() {}

    public GoogleUserDetails getGoogleProfileDetails(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            return objectMapper.treeToValue(jsonNode, GoogleUserDetails.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse provided user details");
        }
    }
}
