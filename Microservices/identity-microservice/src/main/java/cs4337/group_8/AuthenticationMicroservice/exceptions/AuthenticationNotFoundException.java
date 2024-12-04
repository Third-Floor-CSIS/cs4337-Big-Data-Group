package cs4337.group_8.AuthenticationMicroservice.exceptions;

public class AuthenticationNotFoundException extends CustomException {
    public AuthenticationNotFoundException(String message) {
        super(message);
    }
}
