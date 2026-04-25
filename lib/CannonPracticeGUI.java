package lob.guis;

import lob.gaming.games.CannonPractice;
import lob.physics.Vector2D;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * A simple GUI for Cannon Practice based on {@link GenericGameGUI}
 *
 * @author Jos&eacute; Paulo Leal {@code jpleal@fc.up.pt}
 */
public class CannonPracticeGUI extends GenericGameGUI {

    private static final Color DARK_SLATE_GREY = new Color(0x2f4f4f);
    private static final Color ORANGE = new Color(0xFFA500);
    private static final Color BRICK_COLOR = new Color(0xCC8E69);


    static {
        CannonPractice.setAppearanceFactory(new WorldViewer.ColloredAppearanceFactory(
                Map.of("cannonball",DARK_SLATE_GREY,"target" ,ORANGE,"wall",BRICK_COLOR)));
        WorldViewer.setShowVelocity(true);
    }

    /**
     * Create a GUI instance by performing standard initialization,
     * and bind mouse events to {@link CannonPractice#fire(Vector2D)}.
     */
    CannonPracticeGUI() {
        super();

        CannonPractice cannonPractice = new CannonPractice();

        init(cannonPractice);

        viewer.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                var x = (double) event.getX();
                var y = (double) event.getY();

                cannonPractice.fire( new Vector2D(x, y) );
            }
        });
    }

    /**
     * Start this GUI of Cannon Practice
     * @param args ignored
     */
    public static void main(String[] args) {
        new CannonPracticeGUI();
    }

}
