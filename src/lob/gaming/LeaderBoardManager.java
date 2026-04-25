package lob.gaming;

public class LeaderBoardManager {
    private static LeaderBoardManager instance = null;

    private LeaderBoardManager() { }

    public static LeaderBoardManager getInstance() {
        if (instance == null) instance = new LeaderBoardManager();
        return instance;
    }

    void reset() { instance = null; }
}