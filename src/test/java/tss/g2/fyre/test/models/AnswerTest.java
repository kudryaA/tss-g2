package tss.g2.fyre.test.models;


import org.junit.Assert;
import org.junit.Test;
import tss.g2.fyre.models.Answer;

public class AnswerTest {
  @Test
  public void createAnswer() {
    Answer answer1 = new Answer(false);
    Assert.assertEquals(answer1.isStatus(), false);
    Answer answer2 = new Answer(true);
    Assert.assertEquals(answer2.isStatus(), true);
  }

  @Test
  public void toJSON() {
    Answer answer1 = new Answer(true);
    Assert.assertEquals(answer1.toJson(), "{\"status\":true}");
    Answer answer2 = new Answer(true, "sfjvsfvsfhsf");
    Assert.assertEquals(answer2.toJson(), "{\"status\":true,\"obj\":\"sfjvsfvsfhsf\"}");
  }
}
