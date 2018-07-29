package com.br.natanfelipe.marvelapp.model;

import java.util.List;

public class DataComics {
    List<ComicCharacters> results;
    int total;
    int offset;

    public List<ComicCharacters> getResults() {
        return results;
    }

    public int getTotal() {
        return total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
