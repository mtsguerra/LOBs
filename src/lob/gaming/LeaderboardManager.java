package lob.gaming;

import java.io.File;

public class LeaderboardManager {

    private static LeaderboardManager instance = null;
    private static File leaderboardFile;

    private LeaderboardManager() {
    }

    public static LeaderboardManager getInstance() {
        if (instance == null) {
            instance = new LeaderboardManager();
        }
        return instance;
    }

    public static void setLeaderboardFile(File file) {
        leaderboardFile = file;
    }

    // --- ADICIONA ISTO AQUI ---
    // A classe interna que o teste está a pedir
    public static class GameResult {
        // Por agora, deixamos a classe vazia só para o compilador não reclamar.
        // Mais tarde, se o teste exigir, podemos adicionar coisas como o nome do jogador, o jogo e os pontos.
    }
    // --------------------------
}