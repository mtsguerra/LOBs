package lob.gaming;

import lob.physics.shapes.AppearanceFactory;
import lob.physics.shapes.Appearance;

public abstract class GameAnimation implements Gameplay {
    private static AppearanceFactory appearanceFactory;

    // 1. Torna o FPS estático e define o valor padrão (60)
    private static int fps = 60;

    public GameAnimation() { }

    // 2. Torna os métodos de FPS e Step estáticos
    // Isto resolve os erros "cannot be referenced from a static context"

    public static int getFPS() {
        return fps;
    }

    public static void setFPS(int newFps) {
        fps = newFps;
    }

    public static double getStep() {
        return fps > 0 ? 1.0 / fps : 0.0;
    }

    // --- MÉTODOS DA FÁBRICA (Já tínhamos feito) ---
    public static AppearanceFactory getAppearanceFactory() {
        return appearanceFactory;
    }

    public static void setAppearanceFactory(AppearanceFactory factory) {
        appearanceFactory = factory;
    }

    public static Appearance getAppearance(String name) {
        if (appearanceFactory == null) return null;
        return appearanceFactory.getAppearance(name);
    }

    // --- MÉTODOS DA INTERFACE Gameplay ---
    @Override public abstract String getName();
    @Override public abstract String getInstructions();
    @Override public abstract double getWidth();
    @Override public abstract double getHeight();
    @Override public abstract void resetGame();
    @Override public abstract void step();
}