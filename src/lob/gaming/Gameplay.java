package lob.gaming;

public interface Gameplay {
    String getName();
    String getInstructions();
    double getWidth();  // Mudado de int para double
    double getHeight(); // Mudado de int para double
    void resetGame();   // Mudado de reset() para resetGame()
    void step();        // Vamos manter sem parâmetros para simplificar
}