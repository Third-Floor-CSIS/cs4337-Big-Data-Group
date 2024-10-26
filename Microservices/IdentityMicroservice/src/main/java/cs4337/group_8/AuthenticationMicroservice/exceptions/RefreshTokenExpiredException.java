package cs4337.group_8.AuthenticationMicroservice.exceptions;

public class RefreshTokenExpiredException extends CustomException {
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
