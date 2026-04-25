package lob.gaming;

public class Player {
    // Classe que representa as informações de um jogador individual
    private String name;
    private int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    // Podes precisar de getters mais tarde, deixo já o básico:
    public String getName() { return name; }
    public int getScore() { return score; }
}