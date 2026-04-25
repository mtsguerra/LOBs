package lob.guis;

import lob.gaming.games.ArkanoidKnockoff;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple GUI for Arkanoid Knockoff based on {@link GenericGameGUI}
 *
 * @author Jos&eacute; Paulo Leal {@code jpleal@fc.up.pt}
 */
public class ArkanoidKnockoffGUI extends GenericGameGUI {

    private static final Color DARK_SLATE_GREY = new Color(0x2f4f4f);
    private static final Color ORANGE = new Color(0xFFA500);
    private static final Color ELECTRIC_BLUE = new Color(0x59CBE8);

    static {
        ArkanoidKnockoff.setAppearanceFactory(new WorldViewer.ColloredAppearanceFactory(
                Map.of("wall",              DARK_SLATE_GREY,
                        "red brick",        Color.RED,
                        "yellow brick",     Color.YELLOW,
                        "blue brick",       Color.BLUE,
                        "pink brick",       Color.PINK,
                        "orange brick",     ORANGE,
                        "paddle",           Color.GRAY,
                        "ball",             ELECTRIC_BLUE)));
        WorldViewer.setShowVelocity(false);
    }

    ArkanoidKnockoffGUI() {
        super();

        ArkanoidKnockoff arkanoidKnockoff = new ArkanoidKnockoff();

        init(arkanoidKnockoff);

        viewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                arkanoidKnockoff.startGame();
            }
        });

        viewer.addMouseMotionListener(new MouseAdapter() {
            record HorizontalPosition(int x, long time) {}

            List<HorizontalPosition> window = new ArrayList<>();
            final static long WINDOW_SIZE = 500;
            int previous = 0;

            /**
             * Handles the mouse movement event by tracking the horizontal position of the mouse over time,
             * applying smoothing through a sliding time window, and moving the paddle accordingly.
             *
             * @param event the mouse event containing details about the current mouse position and state
             */
            @Override
            public void mouseMoved(MouseEvent event) {
                var now = System.currentTimeMillis();
                var move = new HorizontalPosition(event.getX() , now);

                window.add(move);
                window = window.stream()
                        .filter( p -> now - p.time < WINDOW_SIZE)
                        .collect(Collectors.toList());

                int x = (int) (double) window.stream()
                        .collect(Collectors.averagingInt(HorizontalPosition::x));

                if(x != previous)
                    arkanoidKnockoff.movePaddle(x);

                previous = x;
            }
        });
    }

    /**
     * Start this GUI of Arkanoid Knockoff
     * @param args ignored
     */
    public static void main(String[] args) {
        new ArkanoidKnockoffGUI();
    }

}

