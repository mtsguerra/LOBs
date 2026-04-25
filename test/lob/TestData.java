package lob;

import org.junit.jupiter.params.provider.Arguments;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestData {




    /*************\
     *   DOUBLES  *\
     \*************/

    protected double WIDTH = 100;
    protected double HEIGHT = 100;


    /**
     * A small positive constant used as a threshold for comparing floating-point numbers.
     * This value represents the maximum allowable difference between two doubles
     * to consider them effectively equal, accounting for the limitations of floating-point precision.
     */
    protected static final double EPSILON = 1e-9;


    static final protected int SIZE = 10;
    static final private Random random = new Random();

    static {
        random.setSeed(0); // make tests repeatable
    }

    static final private List<Integer> SIGNAL = List.of(-1,0,1);
    static final protected double DELTA = EPSILON / 10;

    /**
     * Generates a stream of arguments with small variations of double values.
     * Each argument consists of a double value that is the result of multiplying
     * elements in the SIGNAL stream by the DELTA constant.
     *
     * @return a stream of Arguments containing double values with small variations
     */
    static protected Stream<Arguments> smallVariations() {
        return SIGNAL.stream().map( d -> Arguments.of(d * DELTA));
    }

    /**
     * Generates a stream of arguments with small variations of double values.
     * Each argument consists of pairs of double values, including zero and
     * small positive and negative variations defined by DELTA, an order of magnitude smaller than EPSILON.
     *
     * @return a stream of Arguments consisting of double value pairs with small variations
     */
    @SuppressWarnings("unused")
    static protected Stream<Arguments> smallPairedVariations() {

        return SIGNAL.stream()
                .flatMap(sx -> SIGNAL.stream()
                .map(sy -> Arguments.of(sx * DELTA, sy * DELTA)));
    }

    /**
     * Generates a stream of arguments with several doubles
     *
     * @param count of doubles in each Arguments instance
     * @return a stream of Arguments
     */
    static private Stream<Arguments> doubles(int count) {
        return IntStream.range(0, SIZE).
                mapToObj(i ->
                        Arguments.of(random
                                .doubles(count)
                                .boxed()
                                .toList()
                                .toArray()));
    }

    /**
     * A stream arguments with a single double
     *
     * @return stream of Arguments
     */
    @SuppressWarnings("unused")
    static protected Stream<Arguments> doubles1() {
        return doubles(1);
    }

    /**
     * A stream arguments with a pair of doubles
     *
     * @return stream of Arguments
     */
    @SuppressWarnings("unused")
    protected static Stream<Arguments> doubles2() {
        return doubles(2);
    }

    /**
     * A stream arguments with a triple of doubles
     *
     * @return stream of Arguments
     */
    @SuppressWarnings("unused")
    static protected Stream<Arguments> doubles3() {
        return doubles(3);
    }

    /**
     * A stream arguments with a quadruple of doubles
     *
     * @return stream of Arguments
     */
    @SuppressWarnings("unused")
    static protected Stream<Arguments> doubles4() {
        return doubles(4);
    }

    /**
     * A stream arguments with a quintuple of doubles
     *
     * @return stream of Arguments
     */
    @SuppressWarnings("unused")
    static protected Stream<Arguments> doubles5() {
        return doubles(5);
    }

    static Stream<Arguments> rectangles() {
        return IntStream.range(0, SIZE).
                mapToObj(i -> {
                        double[] d = random.doubles(4).toArray();
                        return Arguments.of(d[0],d[1],d[0]+d[2],d[1]+d[3]);
                });
    }

    /**
     * Provides a stream of arguments representing directional movements.
     * Each argument consists of a direction name and its corresponding
     * x and y coordinate changes.
     *
     * @return a stream of Arguments where each argument represents
     *         a direction and its associated x and y offsets
     */
    @SuppressWarnings("unused")
    static Stream<Arguments> directions() {
        return Stream.of(
                Arguments.of("west",      -1,  0),
                Arguments.of("east",       1,  0),
                Arguments.of("north",      0, -1),
                Arguments.of("south",      0,  1) /*,
                Arguments.of("northwest", -1, -1),
                Arguments.of("southwest", -1,  1),
                Arguments.of("northeast",  1, -1),
                Arguments.of("southeast",  1,  1)*/);
    }

     /*************\
      *   INTS    *
     \*************/

     protected static int[] INTS = { 0, 1, 5, 10, 72, 100, 432, 1000};

     /*************\
      *   NICKS   *
     \*************/

    protected static final String INVALID_NICK = "User ZERO";

    protected static final String[] NICKS = { "U0", "U1", "U2" };
    protected static final String   NICK = NICKS[0];
    protected static Stream<String> nickProvider() {
        return Stream.of(NICKS);
    }

     /*************\
      *   NAMES   *
     \*************/

    protected static final String[] NAMES = { "User Zero", "User One", "User Two" };
    protected static final String  NAME = NAMES[0];
    @SuppressWarnings("unused")
    protected static Stream<String> nameProvider() {
        return Stream.of(NAMES);
    }

     /*************\
      *   GAMES   *
     \*************/

    protected static final String[] GAMES = { "Game", "Another Game", "What Game!?", "..." };
    protected static final String  GAME = GAMES[0];
    @SuppressWarnings("unused")
    protected static Stream<String> gamesProvider() {
        return Stream.of(GAMES);
    }


    @SuppressWarnings("unused")
    protected static Stream<Arguments> nickAndNameProvider() {
        return Stream.of(
                Arguments.of(NICKS[0],NAMES[0]),
                Arguments.of(NICKS[1],NAMES[1]),
                Arguments.of(NICKS[2],NAMES[2])
        );
    }

    protected static Stream<Arguments> nickGamePointsProvider() {
        return Stream.of(
                Arguments.of(NICKS[0],GAMES[0],INTS[0]),
                Arguments.of(NICKS[1],GAMES[1],INTS[1]),
                Arguments.of(NICKS[2],GAMES[2],INTS[2])
        );
    }

    @SuppressWarnings("unused")
    protected static Stream<Arguments> nickGamePointsFullProvider() {
        Stream.Builder<Arguments> builder = Stream.builder();

        for(String nick: NICKS)
            for(String names: GAMES)
                for(int points: INTS)
                    builder.add(Arguments.of(nick, names, points));

        return builder.build();
    }


     /*************\
     *   COLORS   *\
     \*************/

    /**
     * Returns a stream of color names
     * @return stream of color names
     */
    @SuppressWarnings("unused")
    static protected Stream<String> colorNames() {
        return Stream.of("BLACK","WHITE","RED","GREEN","BLUE");
    }

    /**
     * Returns a color by its name
     * @param name of the color
     * @return the color
     */
    static protected Color getColorByName(String name) {
        try {
            return (Color) Color.class.getField(name.toUpperCase()).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            // e.printStackTrace();
            return null;
        }
    }
}
