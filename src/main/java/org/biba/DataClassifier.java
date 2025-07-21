package org.biba;

public class DataClassifier {

    public static DataType classify(String value) {

        if (isInteger(value)) {
            return DataType.INTEGER;
        } else if (isFloat(value)) {
            return DataType.FLOAT;
        } else {
            return DataType.STRING;
        }
    }

    private static boolean isInteger(String value) {
        try {
            Long.parseLong(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isFloat(String value) {
        try {
            Float.parseFloat(value.trim());
            return value.contains(".") || value.contains("e");
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
