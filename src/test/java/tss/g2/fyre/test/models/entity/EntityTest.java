package tss.g2.fyre.test.models.entity;

import org.junit.Assert;
import org.junit.Test;
import tss.g2.fyre.models.entity.*;
import tss.g2.fyre.models.entity.recipe.Recipe;

import java.util.Date;

public class EntityTest {
    @Test
    public void testAuthorization() {
        Authorization auth = new Authorization("123", "user");
        Assert.assertEquals("123", auth.getLogin());
        Assert.assertEquals("user", auth.getRole());
        Assert.assertEquals(new Authorization("123", "user"), auth);
    }

    @Test
    public void testComment() {
        Comment comm = new Comment("login", "firstComment");
        Assert.assertEquals("login", comm.getUserLogin());
        Assert.assertEquals("firstComment", comm.getCommentText());
        Assert.assertEquals(new Comment("login", "firstComment"), comm);
    }

    @Test
    public void testModerator() {
        Moderator moder = new Moderator("moder", "login", "123");
        Assert.assertEquals("moder", moder.getName());
        Assert.assertEquals("login", moder.getLogin());
        Assert.assertEquals("123", moder.getPassword());
        Assert.assertEquals(new Moderator("moder", "login", "123"), moder);
    }

    @Test
    public void testPerson() {
        Person pers = new Person("login", "name", "surname", false, "123@gmail.com", "role");
        Assert.assertEquals("login", pers.getLogin());
        Assert.assertEquals("name", pers.getName());
        Assert.assertEquals("surname", pers.getSurname());
        Assert.assertEquals(false, pers.getBannedStatus());
        Assert.assertEquals("123@gmail.com", pers.getEmail());
        Assert.assertEquals("role", pers.getRole());
        Assert.assertEquals(new Person("login", "name", "surname", false, "123@gmail.com", "role"), pers);
    }

    @Test
    public void testType() {
        Type t = new Type("type1", "descr", "12345");
        Assert.assertEquals("type1", t.getTypeName());
        Assert.assertEquals("descr", t.getDescription());
        Assert.assertEquals("12345", t.getImage());
        Assert.assertEquals(new Type("type1", "descr", "12345"), t);
    }

    @Test
    public void recipeTest() {
        Date d = new Date();
        Recipe recipe = new Recipe("13", "rec", "qwe", "qwe", d, "123", "qwe", 12);
        Assert.assertEquals("13", recipe.getId());
        Assert.assertEquals("rec", recipe.getName());
        Assert.assertEquals("qwe", recipe.getComposition());
        Assert.assertEquals("qwe", recipe.getCookingSteps());
        Assert.assertEquals(d, recipe.getPublicationDate());
        Assert.assertEquals("123", recipe.getImage());
        Assert.assertEquals("qwe", recipe.getCreator());
        Assert.assertEquals(12, recipe.getRating());
    }
}
