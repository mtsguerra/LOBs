package lob.gaming;

import lob.LotsOfBallsException;
import java.io.File;
import java.util.ArrayList;
import java.util.Date; // Import necessário para as datas!
import java.util.List;

public class LeaderboardManager {

    private static LeaderboardManager instance = null;
    private static File leaderboardFile;

    // Lista para guardar os resultados em memória
    private final List<GameResult> results = new ArrayList<>();

    private LeaderboardManager() {
    }

    public static LeaderboardManager getInstance() {
        if (instance == null) {
            instance = new LeaderboardManager();
        }
        return instance;
    }

    // --- MÉTODOS DE FICHEIRO ---

    public static void setLeaderboardFile(File file) {
        leaderboardFile = file;
    }

    public static void setLeaderboardFile(String filePath) {
        leaderboardFile = new File(filePath);
    }

    public static File getLeaderboardFile() {
        return leaderboardFile;
    }

    // --- MÉTODOS DE GESTÃO DE PONTUAÇÕES ---

    public void reset() {
        results.clear();
    }

    /**
     * Regista um novo resultado.
     * Resolve o erro: exception LotsOfBallsException is never thrown...
     */
    public void logGameResult(String nick, String game, int points) throws LotsOfBallsException {
        // Ao registar, guardamos também o momento exato (new Date())
        results.add(new GameResult(nick, game, new Date(), points));
    }

    /**
     * Devolve a tabela de liderança para um jogo específico.
     */
    public List<GameResult> getLeaderboard(String game, int size) {
        List<GameResult> filtered = new ArrayList<>();

        // Filtra os resultados para devolver apenas os do jogo pedido
        for (GameResult r : results) {
            if (r.game().equals(game)) {
                filtered.add(r);
                if (filtered.size() == size) break; // Limita ao tamanho pedido
            }
        }
        return filtered;
    }


    // ==========================================
    // --- CLASSE INTERNA GAMERESULT (RECORD) ---
    // ==========================================

    /**
     * Adicionada a propriedade 'date' exigida pelos testes.
     * A ordem foi alinhada com as chamadas de logGameResult.
     */
    public record GameResult(String nick, String game, Date date, int points) {
    }
}