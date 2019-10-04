package g2.tss.fyre;

import g2.tss.fyre.controllers.JavalinController;
import io.javalin.Javalin;

public class Application {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        new JavalinController(app).create();
    }
}
