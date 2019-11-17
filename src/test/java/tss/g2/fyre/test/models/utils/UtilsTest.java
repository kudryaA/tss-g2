package tss.g2.fyre.test.models.utils;

import org.junit.Assert;
import org.junit.Test;
import tss.g2.fyre.utils.DateConverter;
import tss.g2.fyre.utils.ToHash;

public class UtilsTest {
    @Test
    public void testDate() {
        DateConverter dateConverter = new DateConverter("29/03/1999 22:17:43");
        Assert.assertEquals(new DateConverter("29/03/1999 22:17:43"), dateConverter);
    }

    @Test
    public void toHashTest() {
        ToHash toHash = new ToHash("asd");
        Assert.assertEquals(new ToHash("asd"), toHash);
    }
}
