package dk.illution.computer.info;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComputerList {

    /**
     * The computer class.
     *
     * @author Illution
     */
    public static class Computer {

        /**
         * The id of the computer
         */
        public String id;
        /**
         * The JSONObject of the computer containing all the information
         */
        public JSONObject computer;

        /**
         * Creates a Computer object
         *
         * @param id       The id of the computer
         * @param computer The JSONObject of the computer containing all the
         *                 information
         */
        public Computer(String id, JSONObject computer) {
            this.id = id;
            this.computer = computer;
        }
    }

    /**
     * The list of computers
     */
    public static List<Computer> ITEMS = new ArrayList<Computer>();

    /**
     * The map of the computers that consists of the id and the Computer obejct
     */
    public static Map<String, Computer> ITEM_MAP = new HashMap<String, Computer>();

    /**
     * Adds a Computer to the list
     *
     * @param item The Computer object
     */
    public static void addItem(Computer item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
