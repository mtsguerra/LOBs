package lob.guis;

import lob.gaming.GameAnimation;

import javax.swing.*;

/**
 * A class providing a method for game initializations
 *
 * @author Jos&eacute; Paulo Leal {@code jpleal@fc.up.pt}
 */
public class GenericGameGUI extends JFrame {

    WorldViewer viewer = new WorldViewer();

    /**
     * Perform game initializations using a game based on {@link GameAnimation}
     *
     * @param animation providing basic configurations.
     */
    protected void init(GameAnimation animation) {
        setTitle(animation.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize((int) animation.getWidth(), (int) animation.getHeight());
        setResizable(false);
        setVisible(true);

        add(viewer);

        animation.setMessageShower( viewer::showMessage );
        animation.setFrameShower( viewer::showFrame );

        animation.resetGame();

        viewer.showMessage( animation.getInstructions() );
    }
}
