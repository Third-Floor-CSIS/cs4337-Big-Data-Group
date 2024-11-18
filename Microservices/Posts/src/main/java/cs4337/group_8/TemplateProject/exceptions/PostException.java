package cs4337.group_8.TemplateProject.exceptions;

public class PostException extends RuntimeException {

    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }
}
