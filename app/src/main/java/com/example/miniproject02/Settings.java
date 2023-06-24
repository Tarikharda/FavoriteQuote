package com.example.miniproject02;

public class Settings{

    private String Name;
    private String Value;

    //region getters and setters
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
    //endregion

    public Settings(String name, String value) {
        this.Name = name;
        this.Value = value;
    }
}
