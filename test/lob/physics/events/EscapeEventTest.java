package lob.physics.events;

import lob.TestData;
import lob.physics.Vector2D;
import lob.physics.shapes.Circle;
import lob.physics.shapes.Rectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class EscapeEventTest extends TestData {

    /**
     * Tests if the {@code EscapeEvent} class is a record.
     *
     * This test ensures that the {@link EscapeEvent} class is implemented as a
     * Java record, verifying compliance with the intended design pattern and
     * data encapsulation strategy.
     *
     * The test passes if {@code EscapeEvent.class.isRecord()} returns {@code true},
     * confirming that the class is declared as a record.
     */
    @Test
    void isRecord() {
        assertTrue(EscapeEvent.class.isRecord(), "should be a record");
    }

    /**
     * Tests the behavior of the {@code EscapeEvent} class when constructed with null parameters.
     *
     * This test verifies that:
     * 1. The {@link EscapeEvent#escaped()} method returns {@code null} when the {@code escaped} parameter
     *    is initialized to {@code null}.
     * 2. The {@link EscapeEvent#boundary()} method returns {@code null} when the {@code boundary} parameter
     *    is initialized to {@code null}.
     *
     * The test passes if the above conditions hold true.
     */
    @Test
    void checkNulls() {
        var event = new EscapeEvent(null,null);

        assertAll(
                () -> assertNull(event.escaped()),
                () -> assertNull(event.boundary())
        );
    }

    /**
     * Verifies the boundary behavior of the {@code EscapeEvent} class.
     *
     * This method tests if the {@link EscapeEvent#boundary()} method accurately returns
     * the {@link Rectangle} that represents the boundary when an {@code EscapeEvent}
     * is constructed with the specified parameters.
     *
     * @param x the minimum x-coordinate of the rectangle
     * @param y the minimum y-coordinate of the rectangle
     * @param width the width of the rectangle, determining the maximum x-coordinate
     * @param height the height of the rectangle, determining the maximum y-coordinate
     */
    @ParameterizedTest
    @MethodSource("doubles4")
    void boundary(double x, double y, double width, double height) {
        var boundary = new Rectangle(x, y, x+width, y+height,null);
        var event = new EscapeEvent(boundary,null);

        assertEquals(boundary, event.boundary());
    }

    /**
     * Tests the functionality of the {@code escaped} method of the {@code EscapeEvent} class.
     *
     * This test ensures that the {@link EscapeEvent#escaped()} method correctly returns the
     * instance of {@link Circle} that represents the escaped object provided during object construction.
     *
     * @param x the x-coordinate of the center of the circle
     * @param y the y-coordinate of the center of the circle
     * @param radius the radius of the circle
     */
    @ParameterizedTest
    @MethodSource("doubles3")
    void escaped(double x, double y, double radius) {
        var escaped = new Circle(new Vector2D(x, y),Vector2D.NULL_VECTOR,radius,null);
        var event = new EscapeEvent(null,escaped);

        assertEquals(escaped, event.escaped());
    }
}