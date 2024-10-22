package cs4337.group_8.TemplateProject.enums;

public enum SampleEnumWithValues {
    SAMPLE_ENUM_1("Sample Enum 1"),
    SAMPLE_ENUM_2("Sample Enum 2"),
    SAMPLE_ENUM_3("Sample Enum 3");

    private final String value;

    SampleEnumWithValues(String value) { // Constructor
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
