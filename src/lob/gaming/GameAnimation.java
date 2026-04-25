package lob.gaming;

import lob.physics.engine.PhysicsWorld;
import lob.physics.shapes.Appearance;
import lob.physics.shapes.AppearanceFactory;
import lob.physics.shapes.FrameShower;
import java.util.function.Consumer;

public abstract class GameAnimation implements Gameplay {

    // --- CAMPOS ESTÁTICOS (Configurações Globais) ---
    private static AppearanceFactory appearanceFactory;
    private static int fps = 60;

    // --- CAMPOS DE INSTÂNCIA (Lógica do Jogo) ---
    private Consumer<String> messageShower;
    private FrameShower frameShower;

    // Controle de estado da animação
    private boolean running = false;

    /**
     * CORREÇÃO: O PhysicsWorld agora exige largura e altura no construtor.
     * Usamos 800 e 600 como valores padrão para resolver o erro de compilação.
     */
    protected PhysicsWorld world = new PhysicsWorld(800, 600);

    public GameAnimation() {
        // Construtor base
    }

    // --- CONTROLE DA ANIMAÇÃO ---

    public boolean isRunning() {
        return running;
    }

    public void start() {
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

    // --- GESTÃO DE MENSAGENS (UI) ---

    public Consumer<String> getMessageShower() {
        return messageShower;
    }

    public void setMessageShower(Consumer<String> messageShower) {
        this.messageShower = messageShower;
    }

    public void showMessage(String message) {
        if (messageShower != null) {
            messageShower.accept(message);
        }
    }

    // --- GESTÃO DE FRAMES (Desenho) ---

    public FrameShower getFrameShower() {
        return frameShower;
    }

    public void setFrameShower(FrameShower frameShower) {
        this.frameShower = frameShower;
    }

    public void showFrame() {
        if (frameShower != null) {
            frameShower.show(world);
        }
    }

    // --- MÉTODOS ESTÁTICOS ---

    public static int getFPS() {
        return fps;
    }

    public static void setFPS(int newFps) {
        fps = newFps;
    }

    public static double getStep() {
        return fps > 0 ? 1.0 / fps : 0.0;
    }

    public static AppearanceFactory getAppearanceFactory() {
        return appearanceFactory;
    }

    public static void setAppearanceFactory(AppearanceFactory factory) {
        appearanceFactory = factory;
    }

    public static Appearance getAppearance(String name) {
        return (appearanceFactory != null) ? appearanceFactory.getAppearance(name) : null;
    }

    // --- MÉTODOS ABSTRATOS (Interface Gameplay) ---

    @Override public abstract String getName();
    @Override public abstract String getInstructions();
    @Override public abstract double getWidth();
    @Override public abstract double getHeight();
    @Override public abstract void resetGame();
    @Override public abstract void step();
}