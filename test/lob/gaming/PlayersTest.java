package lob.gaming;

import lob.LotsOfBallsException;
import lob.TestData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PlayersTest  extends TestData {
        public static final File PLAYERS_FILE = new File("test_players.ser");
        static Players players;

        @BeforeAll
        public static void setUpClass() throws LotsOfBallsException {
                Players.setPlayersFile(PLAYERS_FILE);

                players = Players.getInstance();
        }

        @AfterAll
        public static void tearDownClass() {
                if(PLAYERS_FILE.exists()) PLAYERS_FILE.delete();
        }

        @BeforeEach
        public void setUp() throws Exception {
                players.reset();

                Players.setPlayersFile(PLAYERS_FILE); // some tests change it
        }


        @Nested
        class PlayerFileTests extends TestData {
            /**
             * Check static getter to retrieve a default players' file
             */
            @Test
            public void testGetPlayersFile() {
                assertNotNull(Players.getPlayersFile());
            }

            /**
             * Check static getter and setter for players' file
             */
            @ParameterizedTest
            @MethodSource("nameProvider")
            public void testSetPlayersFile(String name) {
                var file = new File(name);

                Players.setPlayersFile(file);
                assertEquals(file, Players.getPlayersFile());
            }

            /**
             * Check static getter and setter for players' file
             */
            @ParameterizedTest
            @MethodSource("nameProvider")
            public void testSetPlayersFileWithPathname(String name) {
                var file = new File(name);

                Players.setPlayersFile (name);
                assertEquals(file, Players.getPlayersFile());
            }

        }


    /**
     * Nested class containing tests for the `getInstance` method and the singleton behavior of the `Players` class.
     */
    @Nested
        class GetInstanceTest {

            /**
             * Test if the instance was created in the fixture
             */
            @Test
            public void notNull() {
                assertNotNull(players);
            }

            /**
             * Validates that the `Players` singleton instance remains consistent across multiple invocations.
             * The test is executed multiple times to ensure reliability under repeated use.
             *
             * @throws LotsOfBallsException if an I/O error occurs while accessing the singleton instance.
             */
            @RepeatedTest(10)
            public void alwaysTheSame() throws LotsOfBallsException {
                assertTrue(players == Players.getInstance());
            }
        }

        /**
         * Check player registration with invalid nicks, duplicate nicks, multiple
         * players
         */
        @Test
        public void testRegister()  {
                assertAll(
                                () -> assertNull(players.register(INVALID_NICK, NAME),"Invalid nick"),
                                () -> {
                                        assertNotNull(players.register(NICK, NAME), "Valid nick");
                                        assertNull(players.register(NICK, NAME), "Duplicate nick");
                                },
                                () -> {
                                        assertNotNull(players.register(NICKS[1], NAMES[1]), "Valid nick");
                                        assertNull(players.register(NICKS[1], NAMES[1]), "Duplicate nick");
                                }
                );
        }

        @Nested
        class GetPlayerTest extends TestData {

            /**
             * Check getting a Player by nick when it is unavailable.
             */
            @ParameterizedTest
            @MethodSource("nickProvider")
            public void unregisterdPlayer(String nick) {
                assertNull(players.getPlayer(nick), "Invalid nick");
            }

            /**
             * Check betting a Player by nick when it is available or not
             * @throws LotsOfBallsException on backup I/O errors
             */
            @ParameterizedTest
            @MethodSource("nickAndNameProvider")
            public void registeredPlayer(String nick, String name) throws LotsOfBallsException {
                players.register(nick,name);
                assertNotNull(players.getPlayer(nick), "Valid nick");
            }
        }

    /**
     * Tests the functionality of retrieving or creating a player with the given nick and name.
     * Ensures that the same player is returned for repeated calls with identical parameters.
     *
     * @param nick the nickname of the player
     * @param name the name of the player
     * @throws LotsOfBallsException if an error occurs during player retrieval or creation
     */
    @ParameterizedTest
    @MethodSource("nickAndNameProvider")
    void testGetOrCreate(String nick, String name) throws LotsOfBallsException {
            var first  = players.getOrCreatePlayer(nick,name);
            var second = players.getOrCreatePlayer(nick,name);

            assertNotNull(first,"some player expected");
            assertEquals(first,second, "same player expected");
        }

        /**
         * Check authorization with the generated key
         * @param nick of player
         * @param name of player
         */
        @ParameterizedTest
        @MethodSource("nickAndNameProvider")
        public void testAuthenticate(String nick, String name) throws LotsOfBallsException {
                var player = players.getOrCreatePlayer(nick,name);
                var key  = player.generateKey();
                var truncatedKey = key.substring(0, key.length()/2);

                assertAll(
                                () -> assertTrue(players.authenticate(nick,key),
                                                "should authenticate with key"),
                                () -> assertFalse(players.authenticate(nick,truncatedKey),
                                                "shouldn't authenticate with a truncated key"),
                                () -> assertFalse(players.authenticate(nick+"2",key),
                                                "shouldn't authenticate with a non-existing nick")

                );
        }

        /**
         * Class to execute tests from a different process.
         * A different process may inicialize players from a backup, if available.
         */
        private static class OtherTester {
                final static int OK = 0;
                final static int WRONG_NUMBER_OF_ARGUMENTS = 1;
                final static int PLAYER_WITH_NICK_NOT_FOUND = 2;
                final static int PLAYER_HAS_DIFFERENT_NAME = 3;
                /**
                 * Retrieve a player from a different process to test backup.
                 * Return information to the calling process using exit values.
                 * @param args to retrieve player
                 * @throws LotsOfBallsException on backup's I/O errors
                 */
                public static void main(String[] args) throws LotsOfBallsException {
                        Players.setPlayersFile(PLAYERS_FILE);

                        if (args.length != 2) System.exit(WRONG_NUMBER_OF_ARGUMENTS);

                        Players players = Players.getInstance();
                        String nick = args[0];
                        String name = args[1];
                        Player player = players.getPlayer(nick);

                        if (player == null) System.exit(PLAYER_WITH_NICK_NOT_FOUND);
                        if (!name.equals(player.getName())) System.exit(PLAYER_HAS_DIFFERENT_NAME);

                        System.exit(OK);

                }

                /**
                 * Execute the main in this class, for tests that must
                 * be executed from a different process.
                 *
                 * @param nick of player to test
                 * @param name of player to test
                 * @return process exit value
                 * @throws IOException on process execution
                 * @throws InterruptedException on waiting for process termination
                 */
                private static int execute(String nick, String name) throws IOException, InterruptedException {
                        var runtime = Runtime.getRuntime();
                        var className = OtherTester.class.getName();
                        var javaHome = System.getProperty("java.home");
                        var classPath = System.getProperty("java.class.path");
                        var separator = System.getProperty("file.separator");
                        var javaPath = javaHome + separator + "bin" + separator + "java";
                        var commandLine = new String[] {javaPath, "-cp", classPath, className, nick, name};
                        var process = runtime.exec(commandLine);

                        show(process.errorReader());
                        show(process.inputReader());

                        return process.waitFor();
                }

                /**
                 * Show process readers (for debugging)
                 * @param reader providing characters to read
                 * @throws IOException reading stream
                 */
                private static void show(Reader reader) throws IOException {
                        try(var bufferedReader = new BufferedReader(reader)) {
                                bufferedReader.lines().forEach(System.out::println);
                        }
                }
        }

        /**
         * Checks a player on the singleton backup.
         * Registers a player and launches a different process to check if it was correctly recorded.
         *
         * @param nick of player
         * @param name of player
         * @throws LotsOfBallsException if register fails to load backup
         */
        @ParameterizedTest
        @MethodSource("nickAndNameProvider")
        public void testBackup(String nick,String name) throws LotsOfBallsException {
                players.register(nick,name);

                assertAll(
                                () -> assertEquals(OtherTester.OK,OtherTester.execute(nick,name) ,
                                                "player should be on the backup"),
                                () -> assertEquals(OtherTester.PLAYER_WITH_NICK_NOT_FOUND,OtherTester.execute(nick+"2",""),
                                                "wrong nick, shouldn't be on the backup"),
                                () -> assertEquals(OtherTester.PLAYER_HAS_DIFFERENT_NAME,OtherTester.execute(nick,name+"2"),
                                                "wrong name, shouldn't be on the backup"),
                                () -> {
                                        PLAYERS_FILE.delete();
                                        assertEquals(OtherTester.PLAYER_WITH_NICK_NOT_FOUND,OtherTester.execute(nick,name) ,
                                "player should not be on backup after deleting file");
                                }
                );
        }

}