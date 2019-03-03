import org.junit.Test;
import static org.junit.Assert.*;


public class MapRedTest {

    @Test
    public void evaluateStringAppend() {
        MapRed obj = new MapRed();
        boolean val = obj.appendStrToFile("teststring");
        assertEquals(true, val);
    }

    @Test
    public void checkNotNullValues(){
        MapRed obj = new MapRed();
        assertNotNull(obj.appendStrToFile("teststring"));
    }

    @Test
    public void checkForEquality(){
        MapRed obj = new MapRed();
        assertSame(obj.appendStrToFile("key1"),obj.appendStrToFile("key2"));
    }

    @Test
    public void checkFalse(){
        MapRed obj = new MapRed();
            assertFalse(obj.appendStrToFile(null));
    }

    @Test
    public void checkNotSame(){
        MapRed obj = new MapRed();
        assertNotSame(obj.appendStrToFile("key1"), obj.appendStrToFile(null));
    }
}