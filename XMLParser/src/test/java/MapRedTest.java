import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MapRedTest {
    @Test
    public void evaluateStringAppend() {
        MapRed obj = new MapRed();
        boolean val = obj.appendStrToFile("teststring");
        assertEquals(true, val);
    }

}