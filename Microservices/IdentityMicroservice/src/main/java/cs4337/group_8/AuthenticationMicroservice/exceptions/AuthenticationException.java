package cs4337.group_8.AuthenticationMicroservice.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationException extends CustomException {
    public AuthenticationException(String message) {
        super(message);
    }
}
