package com.jt.tours.domain;

/**
 * Enumeration of the level of effort for the tour
 *
 * Created by Jason Tao on 5/29/2020
 */
public enum DifficultyEnum {
    Easy("Easy"), Medium("Medium"), Hard("Hard"), Varies("Varies");

    private String label;

    DifficultyEnum(String label) {
        this.label = label;
    }

    public static DifficultyEnum findByLabel(String label) {

        for(DifficultyEnum difficulty : DifficultyEnum.values()) {
            if(difficulty.label.equalsIgnoreCase(label)) {
                return difficulty;
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }
}
