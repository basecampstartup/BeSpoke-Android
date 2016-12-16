//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 05/12/2016.
//===============================================================================
package com.bespoke.utils;

/**
 * Enum class to manage the formatting
 */
public enum TicketStatus {
        NONE(0),
        OPEN(1),
        INPROGRESS(2),
        CLOSED(3);

        private int id;

        TicketStatus(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            switch (this) {
                case OPEN:
                    return "Open";
                case CLOSED:
                    return "Closed";
                case INPROGRESS:
                    return "Inprogress";
                default:
                    return "";
            }
        }

    /**
     * Return the ChatMessageType enum on the basis of passed string
     * @param type
     * @return
     */
    public static TicketStatus getType(String type) {
            switch (type) {

                case "OPEN":
                    return TicketStatus.OPEN;
                case "INPROGRESS":
                    return TicketStatus.INPROGRESS;
                case "CLOSED":
                    return TicketStatus.CLOSED;
                default:
                    return TicketStatus.NONE;
            }
        }

        /**
         * Return the String value of enum
         * @return
         */
        public static TicketStatus keyToEnum(int key) {
            switch (key) {

                case 1:
                    return TicketStatus.OPEN;
                case 2:
                    return TicketStatus.INPROGRESS;
                case 3:
                    return TicketStatus.CLOSED;
                default:
                    return NONE;
            }
        }
}
