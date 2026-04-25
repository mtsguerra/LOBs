package lob.gaming;

public class Players {
    private static Players instance = null;

    private Players() { }

    public static Players getInstance() {
        if (instance == null) instance = new Players();
        return instance;
    }

    void reset() { instance = null; }
}