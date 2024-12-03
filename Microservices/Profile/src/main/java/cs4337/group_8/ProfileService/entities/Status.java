package cs4337.group_8.ProfileService.entities;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE("active"),
    INACTIVE("inactive"),
    PENDING("pending");

    private final String string;

    Status(String string) {
        this.string = string;
    }

    public static Status fromString(String value) {
        for (Status status : Status.values()) {
            if (status.string.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
