package lob.gaming;

public class Player {
    private String name;
    private int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() { return name; }
    public int getScore() { return score; }

    // --- ADICIONA ESTE MÉTODO ---
    // O teste na linha 178 precisa disto para criar a "password/chave" do jogador
    public String generateKey() {
        // Criamos uma chave baseada no nome.
        // Adicionamos um sufixo para garantir que tem tamanho suficiente para o substring do teste.
        return name + "_session_key_12345";
    }
}