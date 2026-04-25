package lob.guis;

import lob.gaming.games.DribblingMaster;
import lob.physics.Vector2D;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * A simple GUI for Dribbling Master based on {@link GenericGameGUI}
 *
 * @author Jos&eacute; Paulo Leal {@code jpleal@fc.up.pt}
 */
public class DribblingMasterGUI extends GenericGameGUI {

    private static final Color BROWN = new Color(0x964B00);
    private static final Color ORANGE = new Color(0xFFA500);

    static {
        DribblingMaster.setAppearanceFactory(
                new WorldViewer.ColloredAppearanceFactory(
                        Map.of("basketball",ORANGE,"target",Color.RED,"floor",BROWN)
        ));
    }

    /**
     * Create a GUI instance by performing standard initialization,
     * and bind mouse events to {@link DribblingMaster#strikeBall(Vector2D)}.
     */
    DribblingMasterGUI() {
        super();

        DribblingMaster dribblingMaster = new DribblingMaster();

        init(dribblingMaster);

        viewer.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                var x = (double) event.getX();
                var y = (double) event.getY();

                dribblingMaster.strikeBall( new Vector2D(x, y) );
            }
        });
    }

    /**
     * Start this GUI of Dribbling Master
     * @param args ignored
     */
    public static void main(String[] args) {
        new DribblingMasterGUI();
    }

}
