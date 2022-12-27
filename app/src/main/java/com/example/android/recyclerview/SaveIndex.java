package com.example.android.recyclerview;

public class SaveIndex {
    static int storedIndex = 0;
    public SaveIndex(int index) {
        storedIndex = index;

    }

    public static void StoreIndex(int index) {
        storedIndex = index;
    }

    public static int getStoredIndex() {
        return storedIndex;
    }
}
