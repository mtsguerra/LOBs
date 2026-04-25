package lob.gaming.games;

import lob.LotsOfBallsException;
import lob.gaming.GameAnimation;
import lob.gaming.Player;

/**
 * MockGame is an implementation of the GameAnimation class that simulates a simple game.
 * It provides specific details such as game name, instructions, and dimensions.
 * The class also tracks the number of times the game has been reset.
 */
public class MockGame extends GameAnimation {
    public static final String GAME_NAME = "Mock Game";
    public static final String GAME_INSTRUCTIONS = "Game Instructions";
    public static final int GAME_WIDTH = 300;
    public static final int GAME_HEIGHT = 200;

    /**
     * Constructs a new instance of the GameAnimation class.
     * This class is responsible for managing and orchestrating the animations
     * within a game, including the timing, sequencing, and transitions of
     * visual elements.
     * <p>
     * The GameAnimation class provides functionality to create dynamic and
     * engaging visual effects, ensuring that animations run smoothly and
     * are synchronized with the game's state or events.
     *
     */
    public MockGame() throws LotsOfBallsException {
        super();
    }

    @Override
    public String getName() {
        return GAME_NAME;
    }

    @Override
    public String getInstructions() {
        return GAME_INSTRUCTIONS;
    }

    @Override
    public double getWidth() {
        return GAME_WIDTH;
    }

    @Override
    public double getHeight() {
        return GAME_HEIGHT;
    }

    private int resetCounter = 0;

    /**
     * Retrieves the current value of the reset counter.
     *
     * @return the number of times the reset method has been invoked.
     */
    public int getResetCounter() {
        return resetCounter;
    };

    @Override
    public void resetGame() {
        resetCounter++;
    }
}
