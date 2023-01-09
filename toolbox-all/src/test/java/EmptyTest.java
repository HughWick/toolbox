//import com.github.hugh.util.EmptyUtils;
//import org.junit.jupiter.api.Test;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class EmptyTest {
//
//    @Test
//    void isEmpty() {
//        String[] arr = {};
////        List<String> list = new ArrayList<>();
//        assertTrue(EmptyUtils.isEmpty("  "));
//        assertTrue(EmptyUtils.isEmpty("null"));
//        assertTrue(EmptyUtils.isEmpty(new ArrayList<>()));
//        System.out.println("-8-->" + EmptyUtils.isEmpty(arr));
//        assertTrue(EmptyUtils.isEmpty(new HashMap<>()));
//        assertFalse(EmptyUtils.isEmpty(12));
//        assertFalse(EmptyUtils.isEmpty("[]"));
////        System.out.println("-9-->" + EmptyUtils.isEmpty(list));
//    }
//
//    @Test
//    void isNotEmpty() {
//        System.out.println("---1>" + EmptyUtils.isNotEmpty("b"));
//        System.out.println("2-->" + EmptyUtils.isNotEmpty(""));
//        System.out.println("--3->" + EmptyUtils.isNotEmpty("[]"));
////        System.out.println("--2->" + EmptyUtils.isNotEmpty("[]"));
//    }
//
//    @Test
//    void test03() {
//        String[] strArr = {"1"};
//        assertTrue(EmptyUtils.isNotEmpty(strArr));
//        assertFalse(EmptyUtils.isEmpty(strArr));
//        assertEquals(strArr.length, 1);
//    }
//}
