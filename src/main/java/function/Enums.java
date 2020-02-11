package function;

public class Enums {
    public enum SelectionIndex {
        BAR_SERIAL_COMBO(0),
        BAR_SERIAL_TEXT(1),
        LOCATION_CODE(2),
        DEVICE_TYPE(3),
        TECH_NOTE(4),
        REQUESTING(5);

        private int value;

        SelectionIndex(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
