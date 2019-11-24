package tss.g2.fyre.test.models;

import org.junit.Assert;
import org.junit.Test;
import tss.g2.fyre.models.AnswerWithComment;

public class AnswerWithCommentTest {
    @Test
    public void checkAnswer() {
        AnswerWithComment answer = new AnswerWithComment(true, "qwe");
        Assert.assertEquals("qwe", answer.getComment());
        Assert.assertEquals(new AnswerWithComment(true, "qwe"), answer);

    }
}
