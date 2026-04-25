package lob.physics.forces;

import lob.TestData;
import lob.physics.Vector2D;
import lob.physics.shapes.Circle;
import lob.physics.shapes.Rectangle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class FrictionStrategyTest extends TestData {


    /**
     * Tests whether the acceleration of a given ball (circle-shaped object) is correctly calculated
     * based on predefined gravity.
     *
     * @param x the x-coordinate of the ball's position
     * @param y the y-coordinate of the ball's position
     * @param vx the x-component of the ball's velocity
     * @param vy the y-component of the ball's velocity
     * @param friction coefficient to use in this strategy
     */
    @ParameterizedTest
    @MethodSource("doubles5")
    void getAccelerationCircle(double x, double y,double vx, double vy,double friction) {
        var strategy = new FrictionStrategy(friction);
        var position = new Vector2D(x, y);
        var velocity = new Vector2D(vx, vy);
        var ball =  new Circle(position, velocity, 1, null);

        var expected = velocity.normalize().multiply(-friction);

        assertEquals(expected, strategy.getAcceleration(ball));
    }

    @ParameterizedTest
    @MethodSource("doubles5")
    void getAccelerationRectangle(double x, double y,double width, double height,double friction) {
        var strategy = new FrictionStrategy(friction);
        var rectangle =  new Rectangle(x,y,x+width,y+height, null);

        var expected = Vector2D.NULL_VECTOR;

        assertEquals(expected, strategy.getAcceleration(rectangle));
    }
}