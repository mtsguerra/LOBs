package lob.physics.shapes;

import lob.TestData;
import lob.physics.Vector2D;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link Rectangle} class.
 */
class RectangleTest extends TestData {

    Rectangle rectangle = new Rectangle(-1,-1,1,1, new MockAppearance());

    /**
     * Tests that the upper-left corner of a rectangle is correctly set and retrieved.
     *
     * @param xMin the minimum x-coordinate, representing the left boundary of the rectangle
     * @param yMin the minimum y-coordinate, representing the bottom boundary of the rectangle
     * @param xMax the maximum x-coordinate, representing the right boundary of the rectangle
     * @param yMax the maximum y-coordinate, representing the top boundary of the rectangle
     */
    @ParameterizedTest
    @MethodSource("rectangles")
    void upperLeft(double xMin, double yMin,double xMax, double yMax) {
        Vector2D upperLeftCorner = new Vector2D(xMin,yMin);
        Rectangle rectangle = new Rectangle(xMin, yMin, xMax, yMax, null);

        assertEquals(upperLeftCorner,rectangle.upperLeft());
    }

    /**
     * Tests that the lower-right corner of a rectangle is correctly set and retrieved.
     *
     * @param xMin the minimum x-coordinate, representing the left boundary of the rectangle
     * @param yMin the minimum y-coordinate, representing the bottom boundary of the rectangle
     * @param xMax the maximum x-coordinate, representing the right boundary of the rectangle
     * @param yMax the maximum y-coordinate, representing the top boundary of the rectangle
     */
    @ParameterizedTest
    @MethodSource("rectangles")
    void lowerRight(double xMin, double yMin,double xMax, double yMax) {
        Vector2D lowerRightCorner = new Vector2D(xMax,yMax);
        Rectangle rectangle = new Rectangle(xMin, yMin, xMax, yMax, null);

        assertEquals(lowerRightCorner,rectangle.lowerRight());
    }

    /**
     * Tests that the lower-left corner of a rectangle is correctly set and retrieved.
     *
     * @param xMin the minimum x-coordinate, representing the left boundary of the rectangle
     * @param yMin the minimum y-coordinate, representing the bottom boundary of the rectangle
     * @param xMax the maximum x-coordinate, representing the right boundary of the rectangle
     * @param yMax the maximum y-coordinate, representing the top boundary of the rectangle
     */
    @ParameterizedTest
    @MethodSource("rectangles")
    void lowerLeft(double xMin, double yMin,double xMax, double yMax) {
        Vector2D lowerLeftCorner = new Vector2D(xMin,yMax);
        Rectangle rectangle = new Rectangle(xMin,yMin,xMax,yMax, null);

        assertEquals(lowerLeftCorner,rectangle.lowerLeft());
    }

    /**
     * Tests that the upper-right corner of a rectangle is correctly set and retrieved.
     *
     * @param xMin the minimum x-coordinate, representing the left boundary of the rectangle
     * @param yMin the minimum y-coordinate, representing the bottom boundary of the rectangle
     * @param xMax the maximum x-coordinate, representing the right boundary of the rectangle
     * @param yMax the maximum y-coordinate, representing the top boundary of the rectangle
     */
    @ParameterizedTest
    @MethodSource("rectangles")
    void upperRight(double xMin, double yMin,double xMax, double yMax) {
        Vector2D upperRightCorner = new Vector2D(xMax,yMin);
        Rectangle rectangle = new Rectangle(xMin,yMin,xMax,yMax, null);

        assertEquals(upperRightCorner,rectangle.upperRight());
    }

    /**
     * Validates the creation of a rectangle when both its upper left and lower right corners
     * coincide as the same point. This test ensures proper handling when the rectangle degenerates
     * into a single point.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void pointRectangle(double x, double y) {

        Rectangle rectangle = new Rectangle(x,y,x,y, null);

        assertNotNull(rectangle);
    }

    /**
     * Tests that a {@link IllegalArgumentException} is thrown when the lower right corner is below the upper left corner.
     */
    @ParameterizedTest
    @CsvSource({
            "1, 1, 1, 0",
            "1, 0, 0, 0",
            "1, 1, 1, 0",
            "1, 1, 0, 0",
    })
    void invalidRectangle(double x1, double y1, double x2, double y2) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Rectangle(x1,y1,x2,y2,null));
        assertNotNull(exception.getMessage(),"Exception message is null");
    }

    /**
     * Class AppearanceTest encapsulates tests specifically designed to validate
     * the behavior of the appearance-related functionalities in the Rectangle class.
     * This includes verification of appearance initialization, proper handling of
     * null and non-default appearances, and behavior with custom-defined appearance types.
     *
     * AppearanceTest leverages the utilities and test data provided by its superclass
     * TestData for parameterized testing and data-driven validation. Each test is structured
     * to ensure accurate behavior of Rectangle's appearance property in a variety of contexts.
     */
    @Nested
    class AppearanceTest extends TestData {

        /**
         * Tests the behavior of the {@link Rectangle#appearance()} method when the rectangle is
         * created with a null appearance. The test ensures that the appearance property
         * is correctly set to null when specified as null during initialization.
         *
         * This test specifically verifies that the appearance of the rectangle remains null
         * and does not default to any unexpected or unintended value.
         *
         * Assertions:
         * - Confirms that {@link Rectangle#appearance()} returns null.
         */
        @Test
        void nullAppearance() {
            Rectangle rectangle = new Rectangle(0, 0, 0, 0, null);

            assertNull(rectangle.appearance(),"appearance should be null");
        }

        /**
         * Tests the behavior of the rectangle's appearance when associated with a specialized type and specific color.
         * Ensures that the appearance object correctly represents the color assigned to the rectangle.
         *
         * @param colorName the name of the color to be tested for the rectangle's appearance
         */
        @ParameterizedTest
        @MethodSource("colorNames")
        void appearanceWithColor(String colorName) {
            record ColoredAppearance(Color color) implements Appearance { }
            Color color = getColorByName(colorName);
            ColoredAppearance coloredAppearance1 = new ColoredAppearance(color);
            Rectangle rectangle = new Rectangle(0, 0, 0, 0, coloredAppearance1);
            Appearance appearance = rectangle.appearance();

            if (appearance instanceof ColoredAppearance(Color color1)) {
                assertEquals(color, color1,"appearance should have color "+colorName);
            } else
                fail("Unexpected appearance type");
        }
    }

    @Nested
    class closestPointTest extends TestData {
        Rectangle rectangle = new Rectangle(-10, -10, 10, 10, null);

        @ParameterizedTest
        @CsvSource({
                "    0, 20,  0, 10, bellow the rectangle",
                "    0, 10,  0, 10, bellow on the border",
                "    0,  5,  0,  5, bellow inside the rectangle (with penetration)",
                "   10, 20, 10, 10, bellow the right corner",
                "   10, 10, 10, 10, on the right bottom corner",
                "    0,-20,  0,-10, above the rectangle",
                "    0,-10,  0,-10, above on the border",
                "    0, -5,  0, -5, above inside the rectangle (with penetration)",
                "  -10,-10,-10,-10, on the upper left corner",
                "  -20,  0,-10,  0, left of the rectangle",
                "  -10,  0,-10,  0, left on the border",
                "   -5,  0, -5,  0, left inside the rectangle (with penetration)",
                "  -10, 10,-10, 10, left bottom corner",
                "   20,  0, 10,  0, right of the rectangle",
                "   10,  0, 10,  0, right on the border",
                "    5,  0,  5,  0, right inside the rectangle (with penetration",
                "   10,-10, 10,-10, on top right corner",
                "    0,  0,  0,  0, at the center of the rectangle"
        })
        void closestPoint(double x, double y, double closestX, double closestY, String message) {
            assertEquals( new Vector2D(closestX,closestY), rectangle.closestPoint( new Vector2D(x,y) ),
                    " unexpected closest point "+message);
        }
    }

    /**
     * Verifies that the position (center point) of the rectangle is correctly calculated.
     *
     * @param xMin the minimum x-coordinate, representing the left boundary of the rectangle
     * @param yMin the minimum y-coordinate, representing the bottom boundary of the rectangle
     * @param xMax the maximum x-coordinate, representing the right boundary of the rectangle
     * @param yMax the maximum y-coordinate, representing the top boundary of the rectangle
     */
    @ParameterizedTest
    @MethodSource("rectangles")
    void position(double xMin,double yMin, double xMax,  double yMax) {
        var rectangle = new Rectangle(xMin,yMin,xMax,yMax,null);

        assertEquals( new Vector2D((xMin+xMax)/2,(yMin+yMax)/2),rectangle.position() );
    }

    /**
     * Tests whether a given point is inside the rectangle or not. The point's
     * coordinates are derived from the parameterized input values, scaled and
     * translated to lie within a specific range.
     *
     * @param rx normalized x-coordinate, where the range is assumed to be [0, 1]
     * @param ry normalized y-coordinate, where the range is assumed to be [0, 1]
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void inside(double rx, double ry) {
        var x = 4 * rx - 2;
        var y = 4 * ry - 2;
        var point = new Vector2D(x, y);

        assertEquals(Math.abs(x) <= 1 && Math.abs(y) <= 1,  rectangle.inside(point) );
    }

    /**
     * Tests the movement of a rectangle when an acceleration is applied. Shouldn't move at all.
     *
     * @param ax the x-component of the acceleration vector applied to the rectangle
     * @param ay the y-component of the acceleration vector applied to the rectangle
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void move(double ax, double ay) {
        var acceleration = new Vector2D(ax,ay);
        var dt = 1/60;

        assertEquals(rectangle, rectangle.move(acceleration, dt), "shouldn't move");
    }

    /**
     * Test suite for the method {@code getNormal} of the {@code Rectangle} class.
     * This class performs parameterized tests to validate the behavior of
     * the {@code getNormal} method in different scenarios.
     * The tests check whether the method correctly computes the normal vector
     * on specific points along the rectangle's edges, corners, and center.
     *
     * The parameterized test cases use a {@code CsvSource} annotation to provide
     * various combinations of input points on the rectangle and their expected normal vectors.
     * Points include positions on the top, bottom, left, and right edges, as well
     * as the four corners and the center of the rectangle.
     *
     * Each test case asserts that the actual normal vector returned by the
     * {@code Rectangle#getNormal(Vector2D)} method matches the expected vector
     * for the corresponding input point.
     */
    @Nested
    class GetNormalTest extends TestData {
        Rectangle rectangle = new Rectangle(-10, -10, 10, 10, null);

        /**
         * Tests the {@code getNormal} method of the rectangle for various positions
         * to determine the normal vector at a specific point on the rectangle's surface.
         *
         * @param x the x-coordinate of the point being tested
         * @param y the y-coordinate of the point being tested
         * @param dx the expected x-component of the normal vector
         * @param dy the expected y-component of the normal vector
         * @param message a description or context for the current test case
         */
        @ParameterizedTest
        @CsvSource({
                "   0,  -10,  0, -1, on the top side go ↑",
                "   1,  -10,  0, -1, on the top side go ↑",
                "   2,  -10,  0, -1, on the top side go ↑",
                "  -1,  -10,  0, -1, on the top side go ↑",
                "  -1,  -10,  0, -1, on the top side go ↑",
                "  -1,  -10,  0, -1, on the top side go ↑",
                "  -2,  -10,  0, -1, on the top side go ↑",
                "   0,   -5,  0, -1, in the top go ↑",
                "  -1,   -5,  0, -1, in the top go ↑",
                "   1,   -5,  0, -1, in the top go ↑",

                "   0,   10,  0,  1, on the bottom side go ↓",
                "   1,   10,  0,  1, on the bottom side go ↓",
                "   2,   10,  0,  1, on the bottom side go ↓",
                "  -1,   10,  0,  1, on the bottom side go ↓",
                "  -2,   10,  0,  1, on the bottom side go ↓",
                "   0,    5,  0,  1, in the bottom go ↓",
                "  -1,    5,  0,  1, in the bottom go ↓",
                "   1,    5,  0,  1, in the bottom go ↓",

                "  10,    0,  1,  0, on the right side go →",
                "  10,    1,  1,  0, on the right side go →",
                "  10,    2,  1,  0, on the right side go →",
                "  10,   -1,  1,  0, on the right side go →",
                "  10,   -2,  1,  0, on the right side go →",
                "   5,    0,  1,  0, in the right go →",
                "   5,   -1,  1,  0, in the right go →",
                "   5,    1,  1,  0, in the right go →",

                " -10,    0, -1,  0, on the left side go ←",
                " -10,    1, -1,  0, on the left side go ←",
                " -10,    2, -1,  0, on the left side go ←",
                " -10,   -1, -1,  0, on the left side go ←",
                " -10,   -2, -1,  0, on the left side go ←",
                "  -5,    0, -1,  0, in the left go ←",
                "  -5,   -1, -1,  0, in the left go ←",
                "  -5,    1, -1,  0, in the left go ←",

                " -10,   -10, 0, -1, at the top left corner go ↑",
                "  10,   -10, 0, -1, at the top right corner go ↑",
                "  10,    10, 1,  0, at the bottom right corner go →",
                " -10,    10, 0,  1, at the bottom left corner go  ↓",
                "   0,     0, 0, -1, at the center go ↑"

        })
        void getNormal(double x, double y, double dx, double dy, String message) {
            assertEquals( new Vector2D(dx,dy) , rectangle.getNormal( new Vector2D(x,y) ), message);
        }

    }

}