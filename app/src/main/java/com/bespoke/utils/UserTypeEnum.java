//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 28/11/2016.
//===============================================================================
package com.bespoke.utils;

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

