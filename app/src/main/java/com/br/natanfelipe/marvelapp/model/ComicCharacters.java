package com.br.natanfelipe.marvelapp.model;

public class ComicCharacters {

    int id;
    String name;
    String description;
    Thumbnail thumbnail;
    Comics comics;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Comics getComics() {
        return comics;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }
}
