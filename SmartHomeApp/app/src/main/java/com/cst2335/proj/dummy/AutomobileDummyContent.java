package com.cst2335.proj.dummy;

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
    public static final String FULE = "Fule";
    public static final String ODOMETER = "Odometer";
    public static final String RADIO = "Radio";
    public static final String GPS = "GPS";
    public static final String TEMPERATURE = "Temperature";
    public static final String LIGHT = "Light";

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 7;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            switch (i) {
                case 1:
                    addItem(new DummyItem(i + "", SPEED, SPEED));
                    break;
                case 2:
                    addItem(new DummyItem(i + "", FULE, FULE));
                    break;
                case 3:
                    addItem(new DummyItem(i + "", ODOMETER, ODOMETER));
                    break;
                case 4:
                    addItem(new DummyItem(i + "", RADIO, RADIO));
                    break;
                case 5:
                    addItem(new DummyItem(i + "", GPS, GPS));
                    break;
                case 6:
                    addItem(new DummyItem(i + "", TEMPERATURE, TEMPERATURE));
                    break;
                case 7:
                    addItem(new DummyItem(i + "", LIGHT, LIGHT));
                    break;
                default:
                    addItem(createDummyItem(i));
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
