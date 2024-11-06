package cs4337.group_8.ProfileService.exceptions;

public class SampleCustomException extends RuntimeException {
  public SampleCustomException(String message) {
    // You could potentially log the message here using @Slf4j
    super(message);
  }
}
