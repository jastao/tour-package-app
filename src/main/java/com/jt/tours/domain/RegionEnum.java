package com.jt.tours.domain;

/**
 * Enumeration of the region
 *
 * Created by Jason Tao on 5/29/2020
 */
public enum RegionEnum {

    Central_Coast("Central Coast"),
    East_Coast("East Coast"),
    West_Coast("West Coast");

    private String label;

    RegionEnum(String label) {
        this.label = label;
    }

    public static RegionEnum findByLabel(String label) {

        for(RegionEnum region : RegionEnum.values()) {
            if(region.label.equalsIgnoreCase(label)) {
                return region;
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }
}
