//===============================================================================
// (c) 2015 eWorkplace Apps.  All rights reserved.
// Original Author: Dheeraj Nagar
// Original Date: 30 Nov 2015
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


        //Return the ChatMessageType enum on the basis of passed string
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
         *
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
