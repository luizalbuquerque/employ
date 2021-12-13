package com.paguemob.enums;

public enum IndustryEnum {

    AGRICULTURE("Agriculture"),
    INDUSTRY("Industry"),
    BANKING("Banking"),
    TELECOM("Telecom"),
    IT("IT"),
    CONSTRUCTION("Construction"),
    MINING("Mining"),
    ENERGY("Energy"),
    TOURISM("Tourism"),
    EDUCATION("Education"),
    SERVICES("Services");

    private final String description;

    IndustryEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
