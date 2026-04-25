package lob.physics.forces;

import lob.TestData;
import lob.physics.Vector2D;
import lob.physics.shapes.Circle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;


class GravityStrategyTest extends TestData {

    final static Vector2D GRAVITY = new Vector2D(0, 9.8);

    GravityStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new GravityStrategy();
    }

    /**
     * Tests the behavior of the {@code getAcceleration method.
     *
     * This test verifies that the {@code getAcceleration} method correctly returns
     * the predefined gravitational acceleration vector when both the shape and the list
     * of shapes are {@code null}. It ensures that the strategy consistently adheres
     * to the assumption of applying a constant gravitational force irrespective of
     * the input parameters in this scenario.
     */
    @RepeatedTest(10)
    void getAccelerationNullNull() {
        assertEquals(GRAVITY, strategy.getAcceleration(null));
    }


    /**
     * Tests whether the acceleration of a given ball (circle-shaped object) is correctly calculated
     * based on predefined gravity.
     *
     * @param x the x-coordinate of the ball's position
     * @param y the y-coordinate of the ball's position
     * @param vx the x-component of the ball's velocity
     * @param vy the y-component of the ball's velocity
     * @param radius the radius of the ball, which must be a non-negative number
     */
    @ParameterizedTest
    @MethodSource("doubles5")
    void getAcceleration(double x, double y,double vx, double vy,double radius) {
        Circle ball =  new Circle(new Vector2D(x, y), new Vector2D(vx,vy), radius, null);

        assertEquals(GRAVITY, strategy.getAcceleration(ball));
    }

}