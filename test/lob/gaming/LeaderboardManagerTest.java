package lob.gaming;

import lob.LotsOfBallsException;
import lob.TestData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the LeaderboardManager class.
 * Contains tests to verify the functionality and reliability of leaderboard management,
 * including logging game results, handling backups, and ensuring integrity during multi-process interactions.
 * Extends the TestData superclass to leverage shared test utilities.
 */
class LeaderboardManagerTest extends TestData {
    static final long MAX_MILLISECONDS_DIFF = 1000;
    static final File LEADERBOARDS_FILE = new File("test_leaderboard.ser");
    static LeaderboardManager manager;

    @BeforeAll
    public static void setUpClass() throws LotsOfBallsException {
        LeaderboardManager.setLeaderboardFile(LEADERBOARDS_FILE);

        manager = LeaderboardManager.getInstance();
    }

    @AfterAll
    public static void tearDownClass() {
        if(LEADERBOARDS_FILE.exists()) LEADERBOARDS_FILE.delete();
    }

    @BeforeEach
    public void setUp() throws Exception {
        manager.reset();

        LeaderboardManager.setLeaderboardFile(LEADERBOARDS_FILE); // some tests change it
    }


    /**
     * Unit tests for verifying the behavior of the LeaderboardManager's file management functionality.
     * This nested test class extends TestData and contains test cases to validate getter
     * and setter methods for manipulating the leaderboard file in the LeaderboardManager.
     */
    @Nested
    class LeaderboardFileTests extends TestData {
        /**
         * Check static getter to retrieve a default leaderboard's file
         */
        @Test
        public void testGetLeaderboardManagerFile() {
            assertNotNull(LeaderboardManager.getLeaderboardFile());
        }

        /**
         * Check static getter and setter for the leaderboard's file
         */
        @ParameterizedTest
        @MethodSource("nameProvider")
        public void testSetLeaderboardManagerFile(String name) {
            var file = new File(name);

            LeaderboardManager.setLeaderboardFile(file);
            assertEquals(file, LeaderboardManager.getLeaderboardFile());
        }

        /**
         * Check static getter and setter for the leaderboard's file
         */
        @ParameterizedTest
        @MethodSource("nameProvider")
        public void testSetLeaderboardManagerFileWithPathname(String name) {
            var file = new File(name);

            LeaderboardManager.setLeaderboardFile (name);
            assertEquals(file, LeaderboardManager.getLeaderboardFile());
        }

    }

    /**
     * Nested class containing tests for the `getInstance` method and the singleton behavior of the `LeaderboardManager` class.
     */
    @Nested
    class GetInstanceTest {

        /**
         * Test if the instance was created in the fixture
         */
        @Test
        public void notNull() {
            assertNotNull(manager);
        }

        /**
         * Validates that the `LeaderboardManager` singleton instance remains consistent across multiple invocations.
         * The test is executed multiple times to ensure reliability under repeated use.
         *
         * @throws LotsOfBallsException if an I/O error occurs while accessing the singleton instance.
         */
        @RepeatedTest(10)
        public void alwaysTheSame() throws LotsOfBallsException {
            assertTrue(manager == LeaderboardManager.getInstance());
        }
    }


    @ParameterizedTest
    @MethodSource("nickGamePointsFullProvider")
    void logGameResult(String nick, String game, int points) throws LotsOfBallsException {
        var now = new Date().getTime();
        manager.logGameResult(nick, game, points);

        var size = 10;
        var leaderboard = manager.getLeaderboard(game,size);

        assertEquals(1, leaderboard.size() , "just one result expected");

        var row = leaderboard.get(0);

        assertEquals( nick , row.nick() , "unexpected nick");
        assertEquals( game, row.game() , "unexpected game");
        assertEquals( points, row.points() , "unexpected points");
        assertTrue( now - row.date().getTime() <= MAX_MILLISECONDS_DIFF , "unexpected date diff");
    }

    /**
     * A nested test class for verifying leaderboard functionality in the LeaderboardManager.
     */
    @Nested
    class GetLeaderboardTest extends TestData {
        static List<LeaderboardManager.GameResult> completeLeaderboard;
        static int maxSize;
        static List<String> games;

        @BeforeAll
        static void setUpClass() {
            completeLeaderboard = nickGamePointsFullProvider()
                    .map(arguments -> {
                        var values = arguments.get();
                        var nick = (String) values[0];
                        var game = (String) values[1];
                        var date = new Date();
                        var points = (Integer) values[2];

                        return new LeaderboardManager.GameResult(nick,game,date,points);
                    })
                    .collect(Collectors.toList());
            maxSize = completeLeaderboard.size();
            games = completeLeaderboard.stream()
                    .map(row -> row.game())
                    .collect(Collectors.toList());
        }

        @BeforeEach()
        void setUp() {
            completeLeaderboard.stream().
                    forEach(row -> {
                        try {
                            manager.logGameResult(row.nick(), row.game(), row.points());
                        } catch (LotsOfBallsException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        /**
         * Verifies the leaderboard retrieval functionality for a specific game.
         *
         * @param game the name of the game for which the leaderboard is being tested
         */
        @ParameterizedTest
        @MethodSource("gamesProvider")
        void getLeaderBoard(String game) {
            var now = new Date().getTime();
            var expected = completeLeaderboard.stream()
                    .filter(row -> row.game().equals(game))
                    .sorted((a,b) -> b.points() - a.points())
                    .collect(Collectors.toList());
            var obtained = manager.getLeaderboard(game,maxSize);

            // row creation dates in leaderboards make them incomparable
            // assertEquals(expected, manager.getLeaderboard(game,maxSize));

            assertTrue(obtained.size() <= maxSize, "invalid size, greater than "+maxSize);
            assertEquals(expected.size(),obtained.size(), "leaderboard size mismatch");

            for(var pos = 0; pos < expected.size(); pos++) {
                var expectedRow = expected.get(pos);
                var obtainedRow = obtained.get(pos);

                assertEquals(expectedRow.nick(), obtainedRow.nick(), "invalid nick in row "+pos);
                assertEquals(expectedRow.game(), obtainedRow.game(), "invalid game name in row "+pos);
                assertEquals(expectedRow.points(), obtainedRow.points(), "invalid points in row "+pos);
                assertTrue( now - obtainedRow.date().getTime() < MAX_MILLISECONDS_DIFF, "invalid date in row "+pos+
                        "\n(more than "+MAX_MILLISECONDS_DIFF+" milliseconds of difference)");
            }
        }

        /**
         * Validates that the leaderboard size for a specific game matches the expected size
         * when requesting a single result.
         *
         * @param game the name of the game for which the leaderboard size is being tested
         */
        @ParameterizedTest
        @MethodSource("gamesProvider")
        void getLeaderBoardSize(String game) {
            var leaderboard = manager.getLeaderboard(game,1);

            assertEquals( 1 , leaderboard.size() , "just one result expected" );
        }
    }

    /**
     * Class to execute tests from a different process.
     * A different process may inicialize players from a backup, if available.
     */
    private static class OtherTester {
        final static int OK = 0;
        final static int WRONG_NUMBER_OF_ARGUMENTS = 1;
        final static int INVALID_NUMBER_OF_ROWS = 2;
        final static int WRONG_NICK = 3;
        final static int WRONG_GAME = 4;
        final static int WRONG_POINTS = 5;
        /**
         * Retrieve a player from a different process to test backup.
         * Return information to the calling process using exit values.
         * @param args to retrieve player
         * @throws LotsOfBallsException on backup's I/O errors
         */
        public static void main(String[] args) throws LotsOfBallsException {
            LeaderboardManager.setLeaderboardFile(LEADERBOARDS_FILE);

            if (args.length != 3) System.exit(WRONG_NUMBER_OF_ARGUMENTS);

            LeaderboardManager manager = LeaderboardManager.getInstance();
            String nick = args[0];
            String game = args[1];
            int points = Integer.parseInt(args[2]);

            var leaderboard = manager.getLeaderboard(game,1);

            if (leaderboard.size() != 1) System.exit(INVALID_NUMBER_OF_ROWS);

            var row = leaderboard.get(0);

            if (!nick.equals(row.nick())) System.exit(WRONG_NICK);
            if (!game.equals(row.game())) System.exit(WRONG_GAME);
            if (points != row.points() ) System.exit(WRONG_POINTS);

            System.exit(OK);
        }

        /**
         * Execute the main in this class, for tests that must
         * be executed from a different process.
         *
         * @param nick to test
         * @param game to test
         * @param points to test
         * @return process exit value
         * @throws IOException on process execution
         * @throws InterruptedException on waiting for process termination
         */
        private static int execute(String nick, String game, int points) throws IOException, InterruptedException {
            var runtime = Runtime.getRuntime();
            var className = LeaderboardManagerTest.OtherTester.class.getName();
            var javaHome = System.getProperty("java.home");
            var classPath = System.getProperty("java.class.path");
            var separator = System.getProperty("file.separator");
            var javaPath = javaHome + separator + "bin" + separator + "java";
            var commandLine = new String[] {javaPath, "-cp", classPath, className, nick, game, String.valueOf(points) };
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
     * @param game played
     * @param points won
     * @throws LotsOfBallsException if register fails to load backup
     */
    @ParameterizedTest
    @MethodSource("nickGamePointsProvider")
    public void testBackup(String nick,String game, int points) throws LotsOfBallsException {
        manager.logGameResult(nick,game,points);

        assertAll(
                () -> assertEquals(LeaderboardManagerTest.OtherTester.OK, LeaderboardManagerTest.OtherTester.execute(nick,game,points) ,
                        "player should be on the backup"),
                () -> assertEquals(OtherTester.WRONG_NICK, LeaderboardManagerTest.OtherTester.execute(nick+"2",game,0),
                        "wrong nick, shouldn't be on the backup"),
                () -> assertEquals(OtherTester.INVALID_NUMBER_OF_ROWS, LeaderboardManagerTest.OtherTester.execute(nick,game+"2",0),
                        "wrong game, shouldn't be on the backup"),
                () -> assertEquals(OtherTester.WRONG_POINTS, LeaderboardManagerTest.OtherTester.execute(nick,game,points+1),
                        "wrong points, shouldn't be on the backup"),
                () -> {
                    LEADERBOARDS_FILE.delete();
                    assertEquals(OtherTester.INVALID_NUMBER_OF_ROWS, LeaderboardManagerTest.OtherTester.execute(nick,game,points) ,
                            "player should not be on backup after deleting file");
                }
        );
    }

}