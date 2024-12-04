package cs4337.group_8.posts.exceptions;

public class PostException extends RuntimeException {

    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }
}
