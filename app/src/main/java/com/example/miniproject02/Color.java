package com.example.miniproject02;

public class Color {
    private String Name;
    private String Code;

    //region getters and setters
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
    //endregion

    public Color(String name, String code) {
        Name = name;
        Code = code;
    }
}
