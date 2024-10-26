package cs4337.group_8.AuthenticationMicroservice.exceptions;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
        log.error(message);
        this.printStackTrace();
    }
}
