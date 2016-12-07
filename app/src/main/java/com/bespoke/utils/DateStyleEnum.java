//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 30 Nov 2015
//===============================================================================
package com.bespoke.utils;

/**
 * Enum class to manage the formatting
 */
public class DateStyleEnum {
    public enum StyleType {

        SHORT(0),
        MEDIUM(1),
        LONG(2),
        DAY_MONTH(3);


        private int id;

        StyleType(int id) {
            this.id = id;
        }

        public static StyleType stringToEnum(String displayString) {
            switch (displayString) {
                case "SHORT":
                    return StyleType.SHORT;
                case "MEDIUM":
                    return StyleType.MEDIUM;
                case "LONG":
                    return StyleType.LONG;
                case "DAY_MONTH":
                    return StyleType.DAY_MONTH;

                default:
                    return null;
            }
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            switch (this) {
                case SHORT:
                    return "SHORT";
                case MEDIUM:
                    return "MEDIUM";
                case LONG:
                    return "LONG";
                case DAY_MONTH:
                    return "DAY_MONTH";
                default:
                    return "SYSTEM_ERROR";
            }
        }
    }
}
