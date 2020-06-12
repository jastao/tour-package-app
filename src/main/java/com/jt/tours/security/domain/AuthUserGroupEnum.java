package com.jt.tours.security.domain;

/**
 * Enumeration for the authority user group.
 *
 * Created by Jason Tao on 6/3/2020
 */
public enum AuthUserGroupEnum {

    CSR_ADMIN("CSR_ADMIN"), CSR_USER("CSR_USER"), VIEWER("VIEWER");

    private String label;

    AuthUserGroupEnum(String label) {
        this.label = label;
    }

    public static AuthUserGroupEnum findByLabel(String label) {

        for(AuthUserGroupEnum authGroup : AuthUserGroupEnum.values()) {
            if(authGroup.label.equalsIgnoreCase(label)) {
                return authGroup;
            }
        }
        return null;
    }

    public String getLabel() { return this.label; }
}
