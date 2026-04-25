package lob.gaming;

import lob.LotsOfBallsException;
import lob.gaming.games.MockGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@code ReflectGameFactory} class, which provides methods for dynamically
 * loading, creating, and managing game classes that extend {@code GameAnimation}.
 *
 * This test class includes multiple test cases to validate the behavior and implementation
 * of various functionalities of {@code ReflectGameFactory}.
 *
 * @param <T> The type parameter, extending {@code GameAnimation}.
 */
class ReflectGameFactoryTest <T extends GameAnimation> {
    static final String MOCK_GAME_BINARY_NAME = "lob.gaming.games.MockGame";

    ReflectGameFactory<T> factory;

    @BeforeEach
    void setUp() throws LotsOfBallsException {
        factory = new ReflectGameFactory<>();
    }

    /**
     * Unit test for the `getAvailableGameNames` method in the `ReflectGameFactory` class.
     *
     * This test verifies that the method correctly retrieves a set of game names
     * available for instantiation by the factory. The expected set of game names
     * is compared against the actual result returned by the method.
     *
     * The test ensures consistency between the game's available names in the factory's
     * configuration and the output of the method.
     */
    @Test
    void getAvailableGameNames() {
        var expected = Set.of("Arkanoid Knockoff","Mock Game","Dribbling Master","Cannon Practice","Micro Golf");

        assertEquals( expected ,factory.getAvailableGameNames() );

    }

    /**
     * Unit test for the `getGame` method in the `ReflectGameFactory` class.
     *
     * This test verifies that the `getGame` method correctly retrieves an instance
     * of the specified game by its name. The test ensures that the created instance
     * matches the expected game type.
     **/
    @Test
    void getGame() throws LotsOfBallsException {
         GameAnimation game = factory.getGame(MockGame.GAME_NAME);

         assertTrue( game instanceof MockGame, "mock game expected");
    }

    @Test
    void getGameNull() throws LotsOfBallsException {
        assertThrows(LotsOfBallsException.class, () -> factory.getGame( "non exiting game nane"));
    }

    /**
     * Unit test for the `getGame` method in the `ReflectGameFactory` class.
     *
     * This test ensures that the `getGame` method throws a {@code LotsOfBallsException}
     * when attempting to retrieve a game using an invalid or non-existent game name.
     *
     * The test verifies that:
     * - A {@code LotsOfBallsException} is thrown when the provided game name does not exist in the game class map.
     * - The exception is correctly triggered by the factory's `getGame` method.
     */
    @Test
    void getGameThrowsException() {

        assertThrows(LotsOfBallsException.class, () -> factory.getGame("some.random.binary.name"));
    }

    /**
     * Unit test for the `collectGameClassesFromPackage` method in the `ReflectGameFactory` class.
     *
     * This test ensures that the `collectGameClassesFromPackage` method throws a {@code LotsOfBallsException}
     * when attempting to scan a package that does not exist or cannot be found.
     */
    @Test
    void collectGameClassesFromPackage() {

        assertThrows( LotsOfBallsException.class, () ->
            factory.collectGameClassesFromPackage("some.random.package")
        );
    }

    /**
     * Unit test for the `getClassName` method in the `ReflectGameFactory` class.
     *
     * This test verifies that the `getClassName` method correctly retrieves the binary
     * class name from a given pathname in package format. The test uses various pathnames
     * and their corresponding expected binary class names to ensure that the method works
     * as intended.
     *
     * @param pathname the file path in package format (e.g., "lob/games/DribblingMaster").
     * @param binaryName the expected binary class name (e.g., "lob.games.DribblingMaster").
     * @throws LotsOfBallsException if an error occurs while getting the class name.
     */
    @ParameterizedTest
    @CsvSource({
            "lob/gaming/games/DribblingMaster, lob.games.DribblingMaster",
            "lob/gaming/games/ArkanoidKnockoff, lob.games.ArkanoidKnockoff",
            "lob/gaming/games/SomeOtherGame, lob.games.SomeOtherGame"
    })
    void getClassName(String pathname,String binaryName) throws LotsOfBallsException {
        Path path = Paths.get(pathname);

        factory.getClassName( path );
    }

    @Test
    void loadGameClass() {
        Class clazz = factory.loadGameClass(MOCK_GAME_BINARY_NAME);

        assertEquals( MOCK_GAME_BINARY_NAME, clazz.getName() );
    }

    /**
     * Unit test for the `loadGameClass` method in the `ReflectGameFactory` class.
     *
     * This test ensures that the `loadGameClass` method behaves correctly when provided with an
     * invalid or non-existent binary name. Specifically, the method should return {@code null} in such cases.
     *
     * The test verifies the following:
     * - The factory's `loadGameClass` method is invoked with an invalid binary name.
     * - The method returns {@code null} for the invalid input as expected.
     * - A null result is confirmed to indicate that no valid game class could be loaded.
     */
    @Test
    void loadGameClassThrowsException() {
        assertNull( factory.loadGameClass("some.random.binary.name"),
                "null expected for invalid binary name");
    }

    /**
     * Unit test for the `getGameName` method in the `ReflectGameFactory` class.
     *
     * This test verifies that the `getGameName` method correctly retrieves the name
     * of the game from a valid game class. The test ensures that:
     * - The factory loads the appropriate game class using its binary name.
     * - The name retrieved by the `getGameName` method matches the expected value.
     *
     * Assertions are used to validate that the retrieved game name meets the expected outcome.
     */
    void getGameName() {
        Class<T> clazz = factory.loadGameClass(MOCK_GAME_BINARY_NAME);

        assertEquals(MockGame.GAME_NAME, factory.getGameName( clazz ),
                "unexpected mock game name");
    }

    /**
     * Unit test for the `loadGameClass` method in the `ReflectGameFactory` class.
     *
     * This test verifies that the `loadGameClass` method correctly returns {@code null}
     * when provided with an invalid or non-existent binary name.
    */
    @Test
    void getGameNameNull() {
        assertNull( factory.loadGameClass("some.random.binary.name"),
                "null expected with invalid binary name");
    }

    /**
     * Verifies the creation of an instance of the specified game class using the factory's `createInstance` method.
     * This test ensures that the loaded class is not null and instantiation is successful.
     */
    @Test
    void createInstance() throws LotsOfBallsException {
        Class<T> clazz = factory.loadGameClass(MOCK_GAME_BINARY_NAME);

        assertNotNull( factory.createInstance( clazz) );

    }
}