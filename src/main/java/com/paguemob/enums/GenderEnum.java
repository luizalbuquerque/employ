package com.paguemob.enums;

public enum GenderEnum {

    NOT_KNOWN("not known"),
    MALE("male"),
    FEMALE("female"),
    NOT_APPLICABLE("not applicable");

    private final String description;

    GenderEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
