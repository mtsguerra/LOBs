package lob.guis;

import lob.gaming.games.MicroGolf;
import lob.physics.Vector2D;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * A simple GUI for MicroGolfGUI based on {@link GenericGameGUI},
 *
 * @author Jos&eacute; Paulo Leal {@code jpleal@fc.up.pt}
 */
public class MicroGolfGUI extends GenericGameGUI {

    private static final Color DARK_SLATE_GREY = new Color(0x2f4f4f);
    private static final Color ORANGE = new Color(0xFFA500);
    private static final Color ELECTRIC_BLUE = new Color(0x59CBE8);

    static {
        MicroGolf.setAppearanceFactory(new WorldViewer.ColloredAppearanceFactory(
                Map.of("wall", DARK_SLATE_GREY,
                        "target", ORANGE,
                        "ball", ELECTRIC_BLUE)));
        WorldViewer.setShowVelocity(true);
    }

    /**
     * Create a GUI instance by performing standard initialization,
     * and bind mouse events to {@link MicroGolf#strikeBall(Vector2D)}.
     */
    MicroGolfGUI() {
        super();

        MicroGolf microGolf = new MicroGolf();

        init(microGolf);

        viewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {

                var x = (double) event.getX();
                var y = (double) event.getY();

                microGolf.strikeBall( new Vector2D(x, y) );
            }
        });

    }

    /**
     * Start this GUI of Micro Golf
     * @param args ignored
     */
    public static void main(String[] args) {
        new MicroGolfGUI();
    }
}