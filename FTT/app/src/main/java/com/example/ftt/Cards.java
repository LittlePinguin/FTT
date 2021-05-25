package com.example.ftt;

public class Cards {
    int type;
    int num;
    String synopsis;

    public Cards(int type, int num, String synopsis) {
        this.type = type;
        this.num = num;
        this.synopsis = synopsis;
    }

    public int getType() {
        return type;
    }

    public int getNum() {
        return num;
    }

    public String getSynopsis() {
        return synopsis;
    }
}
