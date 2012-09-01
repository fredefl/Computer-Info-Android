package dk.illution.computer.info.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class DummyContent {

	public static class DummyItem {

		public String id;
		public JSONObject computer;

		public DummyItem(String id, JSONObject computer) {
			this.id = id;
			this.computer = computer;
		}
	}

	public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();
	public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

	public static void addItem(DummyItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}
}
