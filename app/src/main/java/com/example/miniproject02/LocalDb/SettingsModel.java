package com.example.miniproject02.LocalDb;

public class SettingsModel {
    public static class colorModel{
        public static String TABLE_NAME = "color";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_CODE= "code";
    }

    public static class InnerSettingsModel{
        public static String TABLE_NAME = "settings";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_VALUE= "value";
    }
}
