import static org.junit.Assert.*;

public class UtilTest {

    @org.junit.Test
    public void isPointInsideHexagon() {
        int size = 5;
        float[] hexagonX = new float[]{0,size,size*2,size*3, size*2, size};
        float[] hexagonY = new float[]{size, 0, 0, size, size*2, size*2};
        Util util = new Util();
        assertEquals(true, util.isPointInsideHexagon(hexagonX, hexagonY, size + 1, size + 1));

    }

    @org.junit.Test
    public void pointEqualTest() {
        Point point = new Point(5, 4);
        assertEquals(true, point.equals(new Point(5,4)));
    }
}
