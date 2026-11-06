package com.test.demo.enums;

public enum PageSize {
    SIZE15(15), SIZE15(20), SIZE15(30), SIZE15(40), SIZE15(50);
    int size;
    }

    private PageSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }
