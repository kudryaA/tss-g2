package tss.g2.fyre.test.controllers.utils;

import org.junit.Assert;
import org.junit.Test;
import tss.g2.fyre.controllers.utils.UserLogin;
import tss.g2.fyre.models.entity.Authorization;
import tss.g2.fyre.models.entity.Roles;
import tss.g2.fyre.utils.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UserLoginTest {
    private static Properties properties = new Configuration("config/configuration.yml").getProperties();

    @Test
    public void testUserLogin() {
        Map<String, Authorization> tokenStorage = new HashMap<>();
        Authorization authorization = new Authorization("login", Roles.user.toString());
        tokenStorage.put("some_token", authorization);
        UserLogin userLogin = new UserLogin(tokenStorage, "some_token");
        Assert.assertEquals("login", userLogin.get());
    }
}
