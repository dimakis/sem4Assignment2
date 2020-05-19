//package graphTraversal;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import utils.GraphNodeAL;
//import utils.Landmark;
//
//import java.util.ArrayList;
//
//import static org.junit.Assert.assertEquals;
//
//public class BreadthFirstSearchTest {
//
//    BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch();
//    int[] test = new int[36];
//    GraphNodeAL start, end;
//
//    @Before
//    public void setUp() throws Exception {
//
////        start = new GraphNodeAL(new Landmark(2, 4, "start", false));
////        end = new GraphNodeAL(new Landmark(5, 4, "end", false));
//
//        for (int i = 0; i < 36; i++) {
//            test[i] = -1;
//        }
//        test[20] = 0;
//        test[21] = 0;
//        test[22] = 0;
//        test[23] = 0;
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        start = end = null;
//    }
//
//    @Test
//    public void bfs() {
//        ArrayList<Integer> newPath = new ArrayList<>();
//        newPath.add(23);
//        newPath.add(22);
//        newPath.add(21);
//        newPath.add(20);
//
//        assertEquals(breadthFirstSearch.bfs(start, end, 6, test), newPath);
//    }
//}
