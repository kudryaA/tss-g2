package g2.tss.fyre.controllers;


import g2.tss.fyre.models.Answer;
import io.javalin.Javalin;

/**
 * This interface initialize general javalin controller
 *
 * @author Anton Kudryavtsev
 */
public class JavalinController implements CreateController {

    private Javalin app;

    /**
     * Constructor
     *
     * @param app javalin object
     */
    public JavalinController(Javalin app) {
        this.app = app;
    }

    public void create() {
        app.get("/test", ctx -> ctx.result(new Answer<>(true).toJSON()));
    }
}
