package lob.gaming;

import lob.physics.shapes.AppearanceFactory;

public abstract class GameAnimation implements Gameplay {
    private AppearanceFactory appearanceFactory;
    private int fps;

    // Getters e Setters para a Fábrica de Aparências
    public AppearanceFactory getAppearanceFactory() {
        return appearanceFactory;
    }

    public void setAppearanceFactory(AppearanceFactory appearanceFactory) {
        this.appearanceFactory = appearanceFactory;
    }

    // Getters e Setters para os FPS (Frames por segundo)
    public int getFPS() {
        return fps;
    }

    public void setFPS(int fps) {
        this.fps = fps;
    }

    // O "Step" é o tempo que passa entre cada frame (em segundos)
    public double getStep() {
        return fps > 0 ? 1.0 / fps : 0.0;
    }
}