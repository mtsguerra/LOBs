package lob.physics;

import lob.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for `Vector2D` implementation, verifying functionality
 * and constants related to 2D vector operations.
 *
 * Extends `TestData` and uses parameterized and standard unit tests
 * to ensure the correctness of the `Vector2D` class methods and constants.
 */
class
Vector2DTest extends TestData {

    /**
     * Test that Vector2D is a record
     */
    @Test
    void isRecord() {
        assertTrue(Vector2D.class.isRecord(),"Vector2D is not a record");
    }


    /**
     * Test vector multiplication by a scalar
     * @param x coordinate of vector
     * @param y coordinate of vector
     * @param k scalar
     */
    @ParameterizedTest
    @MethodSource("doubles3")
    void multiply(double x, double y, double k) {
        assertEquals(new Vector2D(x * k, y * k), new Vector2D(x,y).multiply(k));
    }

    /**
     * Verifies the length (magnitude) of a 2D vector computed using the Euclidean distance formula.
     * The test compares the calculated length of the vector with the expected value.
     *
     * @param x the x-component of the vector
     * @param y the y-component of the vector
     * @param l the expected length of the vector
     */
    @ParameterizedTest
    @CsvSource({
            "1, 0, 1",
            "0, 1, 1",
            "1, 1, 1.414213562",
            "2, 2, 2.828427125",
            "3, 4, 5",
            "4, 3, 5",
            "5, 5, 7.071067812",
            "5,12,13",
            "8,15,17"
    } )
    void length(double x, double y, double l) {
        assertEquals(l,new Vector2D(x,y).length(),EPSILON);
    }

    /**
     * Tests the normalization of a vector represented by its x and y components.
     * The `normalize` method should return a unit vector (length of 1) in the
     * direction of the original vector. The test asserts that the length of
     * the normalized vector is equal to 1 within a small margin of error.
     *
     * @param x the x-component of the vector to be normalized
     * @param y the y-component of the vector to be normalized
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void normalize(double x, double y) {
        Vector2D v = new Vector2D(x,y);

        assertEquals(1,v.normalize().length(),EPSILON);
    }

    /**
     * Tests the `normalize` method of the `Vector2D` class.
     *
     * The `normalize` method computes a unit vector in the same direction
     * as the original vector, such that the vector's length becomes 1. This test
     * case ensures that the normalized vector of a zero vector (0,0) returns
     * a length of 0.
     *
     * The assertion verifies that the length of the normalized zero vector
     * is equal to 0 within a given small margin of error (EPSILON).
     */
    @Test
    void normalize() {
        Vector2D v = new Vector2D(0,0);
        assertEquals(0,v.normalize().length(),EPSILON);
    }


    /**
     * Tests the squared length calculation of a 2D vector.
     * The squared length is computed as the sum of the squares of the x and y components of the vector.
     *
     * @param x the x-component of the vector
     * @param y the y-component of the vector
     * @param l the expected squared length of the vector
     */
    @ParameterizedTest
    @CsvSource({
            "1, 0,  1",
            "0, 1,  1",
            "1, 1,  2",
            "2, 2,  8",
            "3, 4, 25",
            "4, 3, 25",
            "5, 5, 50",
            "5,12,169",
            "8,15,289",
    } )
    void lengthSquared(double x, double y, double l) {
        assertEquals(l,new Vector2D(x,y).lengthSquared(),EPSILON);
    }


    /**
     * Tests the addition of two 2D vectors using the add method of the Vector2D class.
     *
     * @param x1 the x-coordinate of the first vector
     * @param y1 the y-coordinate of the first vector
     * @param x2 the x-coordinate of the second vector
     * @param y2 the y-coordinate of the second vector
     */
    @ParameterizedTest
    @MethodSource("doubles4")
    void add(double x1, double y1, double x2, double y2) {
        assertEquals( new Vector2D(x1 + x2, y1 + y2), new Vector2D(x1,y1).add(new Vector2D(x2,y2)) );
    }

    /**
     * Tests the subtraction of two 2D vectors.
     * The method calculates the difference of the components of the first vector and the second vector,
     * asserting the result matches the expected output.
     *
     * @param x1 the x-component of the first vector
     * @param y1 the y-component of the first vector
     * @param x2 the x-component of the second vector
     * @param y2 the y-component of the second vector
     */
    @ParameterizedTest
    @MethodSource("doubles4")
    void subtract(double x1, double y1, double x2, double y2) {
        assertEquals( new Vector2D(x1 - x2, y1 - y2), new Vector2D(x1,y1).subtract(new Vector2D(x2,y2)) );
    }


    /**
     * Tests the inner product (dot product) calculation between two 2D vectors.
     * The inner product is calculated as the sum of the products of their respective components.
     *
     * @param x1 the x-component of the first vector
     * @param y1 the y-component of the first vector
     * @param x2 the x-component of the second vector
     * @param y2 the y-component of the second vector
     */
    @ParameterizedTest
    @MethodSource("doubles4")
    void innerProduct(double x1, double y1, double x2, double y2) {
        assertEquals( x1*x2 + y1*y2, new Vector2D(x1,y1).innerProduct(new Vector2D(x2,y2)) );
    }

    /**
     * Test vector x coordinates
     * @param x coordinate of vector
     * @param y coordinate of vector
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void x(double x, double y) {
        assertEquals(x, new Vector2D(x,y).x());
    }

    /**
     * Test vector y coordinates
     * @param x coordinate of vector
     * @param y coordinate of vector
     */
    @ParameterizedTest
    @MethodSource("doubles2")
    void y(double x, double y) {
        assertEquals(y, new Vector2D(x,y).y());
    }

    /**
     * Tests the `NULL_VECTOR` constant in the `Vector2D` class.
     *
     * This constant represents a vector with both components set to zero (0, 0).
     * The test verifies that the `NULL_VECTOR` is correctly defined and
     * equals a new instance of `Vector2D` initialized to (0, 0).
     */
    @Test
    void nullVector() {
        assertEquals(new Vector2D(0,0),Vector2D.NULL_VECTOR);
    }

    /**
     * Tests the `NORTH` constant in the `Vector2D` class.
     *
     * This constant represents a vector pointing to the north direction
     * with components (0, -1). The test verifies that the `NORTH` constant
     * is correctly defined and matches a newly instantiated `Vector2D` object
     * with the same components.
     */
    @Test
    void north() {
        assertEquals(new Vector2D(0,-1),Vector2D.NORTH);
    }

    /**
     * Tests the `SOUTH` constant in the `Vector2D` class.
     *
     * This constant represents a vector pointing to the south direction
     * with components (0, 1). The test verifies that the `SOUTH` constant
     * is correctly defined and matches a newly instantiated `Vector2D`
     * object with the same components.
     */
    @Test
    void south() {
        assertEquals(new Vector2D(0,1),Vector2D.SOUTH);
    }

    /**
     * Tests the `WEST` constant in the `Vector2D` class.
     *
     * This constant represents a vector pointing to the west direction
     * with components (0, -1). The test verifies that the `WEST` constant
     * is correctly defined and equals a newly instantiated `Vector2D` object
     * with the same components.
     */
    @Test
    void west() {
        assertEquals(new Vector2D(-1,0),Vector2D.WEST);
    }

    /**
     * Tests the `EAST` constant in the `Vector2D` class.
     *
     * This constant represents a vector pointing in the eastward direction
     * with components (0, 1). The test verifies that the `EAST` constant
     * is correctly defined and matches a newly instantiated `Vector2D` object
     * with the same components.
     */
    @Test
    void east() {
        assertEquals(new Vector2D(1,0),Vector2D.EAST);
    }

    /**
     * Tests the `NORTH_WEST` constant in the `Vector2D` class.
     *
     * This constant represents a vector pointing to the northwest direction
     * with components (-1, -1). The test verifies that the `NORTH_WEST` constant
     * is correctly defined and matches a newly instantiated `Vector2D` object
     * with the same components.
     */
    @Test
    void northWest() {
        assertEquals(new Vector2D(-1,-1),Vector2D.NORTH_WEST);
    }

    /**
     * Tests the `SOUTH_WEST` constant in the `Vector2D` class.
     *
     * This constant represents a vector pointing to the southwest direction
     * with components (-1, 1). The test verifies that the `SOUTH_WEST` constant
     * is correctly defined and matches a newly instantiated `Vector2D` object
     * with the same components.
     */
    @Test
    void southWest() {
        assertEquals(new Vector2D(-1,1),Vector2D.SOUTH_WEST);
    }

    /**
     * Tests the `NORTH_EAST` constant in the `Vector2D` class.
     *
     * This constant represents a vector pointing to the northeast direction
     * with components (1, -1). The test verifies that the `NORTH_EAST` constant
     * is correctly defined and matches a newly instantiated `Vector2D` object
     * with the same components.
     */
    @Test
    void northEast() {
        assertEquals(new Vector2D(1,-1),Vector2D.NORTH_EAST);
    }

    /**
     * Tests the `SOUTH_EAST` constant in the `Vector2D` class.
     *
     * This constant represents a vector pointing to the southeast direction
     * with components (1, 1). The test verifies that the `SOUTH_EAST` constant
     * is correctly defined and matches a newly instantiated `Vector2D` object
     * with the same components.
     */
    @Test
    void southEast() {
        assertEquals(new Vector2D(1,1),Vector2D.SOUTH_EAST);
    }
}