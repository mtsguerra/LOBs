package lob.physics.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PendingActionsTest {
    List<Integer> performedActions = new ArrayList<>();

    class MockActionOnShapes implements ActionOnShapes {
        int order;

        MockActionOnShapes(int order) {
            this.order = order;
        }

        public void execute() {
                performedActions.add(order);
        };
    }

    @BeforeEach
    void setUp() {
        performedActions.clear();
    }

    @Test
    void test() {
        var size = 10;
        List<Integer> expected = new ArrayList<>();

        PendingActions pendingActions = new PendingActions();

        IntStream.range(0, size).forEach(i -> {
            MockActionOnShapes action = new MockActionOnShapes(i);
            pendingActions.defer( action );

            expected.add(i);
        });

        assertEquals( 0, performedActions.size() , "no action performed yet" );

        pendingActions.executePendingActions();

        assertEquals( expected.size(), performedActions.size() , "wrong number of actions");
        assertEquals( expected , performedActions, "wrong order of actions");

        pendingActions.executePendingActions();

        assertEquals( expected.size(), performedActions.size() , "no more actions should be executed");


    }


}