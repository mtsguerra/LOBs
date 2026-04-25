package lob.gaming;

public interface Gameplay {
    void step(double dt); // O passo da simulação física
    void reset();
}