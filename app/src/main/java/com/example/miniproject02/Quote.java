package com.example.miniproject02;

public class Quote {
    private String id;
    private String quote;
    private String author;

    //region getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    //endregion

    public Quote(String id, String quote, String author) {
        this.id = id;
        this.quote = quote;
        this.author = author;
    }
}
