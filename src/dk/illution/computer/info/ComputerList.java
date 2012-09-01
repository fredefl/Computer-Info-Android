package dk.illution.computer.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class ComputerList {

	public static class Computer {

		public String id;
		public JSONObject computer;

		public Computer (String id, JSONObject computer) {
			this.id = id;
			this.computer = computer;
		}
	}

	public static List<Computer> ITEMS = new ArrayList<Computer>();
	public static Map<String, Computer> ITEM_MAP = new HashMap<String, Computer>();

	public static void addItem(Computer item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}
}
