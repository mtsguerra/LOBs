package lob.physics.shapes;

import lob.TestData;
import lob.physics.Vector2D;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link Circle} class.
 */
class CircleTest extends TestData {

    static Circle circle =  new Circle(new Vector2D(0,0),new Vector2D(0,0),1,new MockAppearance());

    /**
     * Test that Circle is a record
     */
    @Test
    void isRecord() {
        assertTrue(Circle.class.isRecord(),"Circle is not a record");
    }

    /**
     * Tests the {@link Circle#position()} method to verify that the position
     * of the circle is correctly set and retrieved.
     *
     * @param x the x-coordinate of the circle's position
     * @param y the y-coordinate of the circle's position
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void position(double x, double y ) {
        Vector2D position = new Vector2D(x,y);
        Vector2D velocity = new Vector2D(0,0);
        double radius = 1;
        Circle circle = new Circle(position,velocity,radius,null);

        assertEquals(position,circle.position());
    }

    /**
     * Tests that a {@link NullPointerException} is thrown when a null position is passed to the constructor.
     */
    @Test
    void nullPosition() {
        Exception exception = assertThrows(NullPointerException.class,
                () -> new Circle(null,new Vector2D(0,0),1,null));
        assertNotNull(exception.getMessage(),"Exception message is null");
    }

    /**
     * Tests the {@link Circle#velocity()} method to verify that the velocity
     * of the circle is correctly set and retrieved.
     *
     * @param x the x-component of the circle's velocity
     * @param y the y-component of the circle's velocity
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void velocity(double x, double y) {
        Vector2D position = new Vector2D(0,0);
        Vector2D velocity = new Vector2D(x,y);
        double radius = 1;
        Circle circle = new Circle(position,velocity,radius,null);

        assertEquals(velocity,circle.velocity());
    }

    /**
     * Tests that a {@link NullPointerException} is thrown when a null velocity is passed to the constructor.
     */
    @Test
    void nullVelocity() {
        Exception exception = assertThrows(NullPointerException.class,
                () -> new Circle(new Vector2D(0,0),null,1,null));

        assertNotNull(exception.getMessage(),"Exception message is null");
    }

    @Nested
    class RadiusTest extends  TestData {
        /**
         * Tests the {@link Circle#radius()} method to verify that the radius of the circle
         * @param radius the radius of the circle
         */
        @ParameterizedTest
        @MethodSource("doubles1")
        void radius(double radius) {
            Vector2D position = new Vector2D(0,0);
            Vector2D velocity = new Vector2D(0,0);
            Circle circle = new Circle(position,velocity,radius,null);

            assertEquals(radius,circle.radius());
        }

        @Test
        void negativeRadius() {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> new Circle(new Vector2D(0,0),new Vector2D(0,0),-1,null));
            assertNotNull(exception.getMessage(),"Exception message is null");
        }
    }

    @Nested
    class AppearanceTest extends TestData {

        /**
         * Tests the behavior of a Circle when its appearance is set to null.
         *
         * This method verifies that the appearance property of a Circle is null
         * when explicitly initialized with a null value. It asserts that the
         * {@link Circle#appearance()} method returns null as expected.
         */
        void nullAppearance() {
            Vector2D position = new Vector2D(0,0);
            Vector2D velocity = new Vector2D(0,0);
            double radius = 1;
            Circle circle = new Circle(position,velocity,radius,null);

            assertNull(circle.appearance(),"Appearance should be null");
        }

        /**
         * Tests the behavior of a Circle when its appearance is defined by a specific color.
         *
         * This test ensures that a Circle correctly retains and provides access to its appearance,
         * which is defined by a color, through an instance of the `ColoredAppearance` class.
         * The test validates that the color provided matches the expected value and that
         * the appearance is of the correct type.
         *
         * @param colorName the name of the color to be tested, which determines the color used in the Circle's appearance
         */
        @ParameterizedTest
        @MethodSource("colorNames")
        void color(String colorName) {
            record ColoredAppearance(Color color) implements Appearance {}
            Vector2D position = new Vector2D(0,0);
            Vector2D velocity = new Vector2D(0,0);
            double radius = 1;
            Color color = getColorByName(colorName);
            ColoredAppearance coloredAppearance1 = new ColoredAppearance(color);
            Circle circle = new Circle(position,velocity,radius,coloredAppearance1);
            Appearance appearance = circle.appearance();

            if(appearance instanceof ColoredAppearance(Color color1)) {
                assertEquals(color, color1);
            } else
                fail("unexpected appearance type");

        }

    }

    /**
     * Tests the {@code getNormal} method of the {@code Circle} class to verify that it
     * correctly computes the normalized vector representing the outward-pointing normal
     * at a given point on the circle's boundary.
     *
     * @param r the angle in radians, scaled by 2π, to define the direction of the point in polar coordinates
     * @param l the radial distance from the circle's center to compute the point position
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void getNormal(double r, double l) {
        final var a = r * 2 * Math.PI;
        final var x = l * Math.cos(a);
        final var y = l * Math.sin(a);
        Vector2D point = new Vector2D(x, y);

        assertEquals(point.normalize(), circle.getNormal(point), "Normalization failed");
    }

    /**
     * Tests whether a point lies inside the circle by generating points
     * using polar coordinates derived from the input parameters and
     * comparing the results with the `circle.inside` method.
     *
     * @param r the angle in radians, scaled by 2π, to define the direction of the point in polar coordinates
     * @param s the radial distance from the origin used to compute the point position
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void inside(double r, double s) {
        final var a = r * 2 * Math.PI;
        final var l = 2 * s;
        final var x = l * Math.cos(a);
        final var y = l * Math.sin(a);
        Vector2D point = new Vector2D(x, y);

        assertEquals(l <= 1, circle.inside(point), "inside failed");
    }

    /**
     * Tests the {@code closestPoint} method of the {@code Circle} class.
     * This test verifies that the method correctly computes the closest point
     * on the circle's boundary to the given input point. The input point is converted
     * to a {@code Vector2D} object and its length is determined. If the length is within
     * the radius of the circle, the point itself is expected to be the result. Otherwise,
     * the point is scaled to lie on the circle's boundary.
     *
     * @param rx the normalized x-coordinate in the range [0, 1], which is transformed to
     *           represent the x-coordinate of the input point in the circle's space
     * @param ry the normalized y-coordinate in the range [0, 1], which is transformed to
     *           represent the y-coordinate of the input point in the circle's space
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void closestPoint(double rx, double ry) {
        var x = 2 * rx - 1;
        var y = 2 * ry - 1;
        var point = new Vector2D(x, y);
        var length = point.length();
        var expected = length <= 1 ? point : point.multiply(1/length);

        assertEquals(expected, circle.closestPoint(point) );
    }

    /**
     * Tests the {@code move} method of the {@code Circle} class.
     * This method verifies that the circle's position and velocity
     * are correctly updated based on the provided acceleration
     * and a fixed time step. The appearance of the circle is
     * expected to remain unchanged.
     *
     * @param vx the x-component of the acceleration to apply to the circle
     * @param vy the y-component of the acceleration to apply to the circle
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void move(double vx, double vy) {
        var acceleration = new Vector2D(vx,vy);
        var dt = 1/60;

        var moved = circle.move(acceleration,dt);

        assertEquals(acceleration.multiply(dt) , moved.velocity() );
        assertEquals(circle.position().add(moved.velocity().multiply(dt)),moved.position() );
        assertEquals(circle.appearance(),moved.appearance(),"should be unchanged");
    }

}