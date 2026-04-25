package lob.physics.forces;

import lob.TestData;
import lob.physics.Vector2D;
import lob.physics.shapes.Circle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


class NoForceStrategyTest extends TestData {

    NoForceStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new NoForceStrategy();
    }

    /**
     * Tests the behavior of the {@code getAcceleration} method.
     *
     * This test verifies that the {@code getAcceleration} method correctly returns
     * {@link Vector2D#NULL_VECTOR} when both the shape and list of shapes are {@code null}.
     * It ensures that the strategy adheres to its contract of providing zero acceleration
     * under circumstances where no force is applicable.
     */
    @RepeatedTest(10)
    void getAccelerationNullNull() {
        assertEquals(Vector2D.NULL_VECTOR, strategy.getAcceleration(null));
    }

    /**
     * Tests the behavior of the {@code getAcceleration} method.
     *
     * This test verifies that the method correctly returns {@link Vector2D#NULL_VECTOR}
     * when simulating a scenario where no force is applied to the given {@link Circle}.
     * The test ensures that the strategy adheres to the specification of providing
     * zero acceleration in force-free conditions.
     *
     * @param x the x-coordinate of the circle's position
     * @param y the y-coordinate of the circle's position
     * @param vx the x-component of the circle's velocity
     * @param vy the y-component of the circle's velocity
     * @param radius the radius of the circle, must be a non-negative value
     */
    @ParameterizedTest
    @MethodSource("doubles5")
    void getAcceleration(double x, double y,double vx, double vy,double radius) {
        Circle ball =  new Circle(new Vector2D(x, y), new Vector2D(vx,vy), radius, null);

        assertEquals(Vector2D.NULL_VECTOR, strategy.getAcceleration(ball));
    }

}