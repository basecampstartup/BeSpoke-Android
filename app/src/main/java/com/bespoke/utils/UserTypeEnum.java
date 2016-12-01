package com.bespoke.utils;

/**
 * Created by admin on 11/28/2016.
 */

public enum UserTypeEnum {

    ADMIN(1),
    SUPERADMIN(2),
    NORMALUSER(3);

    private int id;

    UserTypeEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        switch (this) {
            case ADMIN:
                return "ADMIN";
            case SUPERADMIN:
                return "SUPERADMIN";
            case NORMALUSER:
                return "NORMALUSER";
            default:
                return "SYSTEM_ERROR";
        }
    }
}

