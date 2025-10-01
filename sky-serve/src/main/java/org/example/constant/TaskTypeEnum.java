package org.example.constant;

public enum TaskTypeEnum {
    OUT("A"),
    INNER("B"),
    SKY("C");
    private String value;
    private TaskTypeEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public static TaskTypeEnum getEnum(String value) {
        for (TaskTypeEnum e : TaskTypeEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
