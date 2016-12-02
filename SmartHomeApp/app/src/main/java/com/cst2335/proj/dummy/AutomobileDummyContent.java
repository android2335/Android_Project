package com.cst2335.proj.dummy;

import com.cst2335.proj.AutomobileDatabaseOperate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class AutomobileDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    public static final String SPEED = "Speed";
    public static final String FUEL = "Fuel";
    public static final String ODOMETER = "Odometer";
    public static final String RADIO = "Radio";
    public static final String GPS = "GPS";
    public static final String TEMPERATURE = "Temperature";
    public static final String LIGHT = "Light";

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public static void createItem() {
        //clear
        ITEMS.clear();
        ITEM_MAP.clear();

        // Add some sample items.
        int k = 1;
        ArrayList<Integer> itemNo = AutomobileDatabaseOperate.getItemNo();
        for (int i = 1; i <= AutomobileDatabaseOperate.ITEM_NUM; i++)
            for (int j = 0; j < AutomobileDatabaseOperate.ITEM_NUM; j++) {
                if (itemNo.get(j) == i) {
                    addItem(new DummyItem(k++ + "", AutomobileDatabaseOperate.itemName.get(j), AutomobileDatabaseOperate.itemName.get(j)));
                    break;
            }
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
