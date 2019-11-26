package tss.g2.fyre.controllers.javalin;

import com.google.gson.Gson;
import com.sun.management.OperatingSystemMXBean;
import io.javalin.Javalin;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import tss.g2.fyre.controllers.CreateController;
import tss.g2.fyre.controllers.utils.UserLogin;
import tss.g2.fyre.models.Answer;
import tss.g2.fyre.models.actions.Action;
import tss.g2.fyre.models.actions.ActionTime;
import tss.g2.fyre.models.actions.simple.service.StoreTime;
import tss.g2.fyre.models.datastorage.DataStorage;
import tss.g2.fyre.models.entity.Authorization;

/**
 * Service controller.
 * @author Anton Kudryvtsev
 */
public class ServiceController implements CreateController {
  private Javalin app;
  private DataStorage dataStorage;
  private Map<String, Authorization> tokenStorage;
  private static Logger logger = LoggerFactory.getLogger(ServiceController.class);

  /**
   * Constructor.
   * @param app javalin app
   * @param dataStorage storage of data
   * @param tokenStorage storage with authorization info
   */
  public ServiceController(Javalin app, DataStorage dataStorage,
                           Map<String, Authorization> tokenStorage) {
    this.app = app;
    this.dataStorage = dataStorage;
    this.tokenStorage = tokenStorage;
  }

  @Override
  public void create() {
    app.post("/store/time/interactive", ctx -> {
      String token = ctx.sessionAttribute("token");
      long time = Long.parseLong(ctx.formParam("userLogin"));
      String page = ctx.formParam("page");
      logger.info("Request to /store/time/interactive with user {}",
          new UserLogin(tokenStorage, token).get());
      Action action = new StoreTime(dataStorage, page, time);
      Answer answer = new ActionTime("/store/time/interactive", action, dataStorage).getAnswer();
      ctx.result(answer.toJson());
    });

    app.get("/select/statistics", ctx -> {
      ctx.result(new Gson().toJson(dataStorage.selectStatistics()));
    });

    app.get("/select/hardware", ctx -> {
      SystemInfo si = new SystemInfo();
      HardwareAbstractionLayer hal = si.getHardware();
      OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
      Map<String, Object> map = new HashMap<>();
      List<Map<String, Object>> networks = new ArrayList<>();
      Arrays.stream(hal.getNetworkIFs()).forEach(item -> {
        Map<String, Object> network = new HashMap<>();
        network.put("recv", item.getBytesRecv());
        network.put("sent", item.getBytesSent());
        networks.add(network);
      });
      map.put("networks", networks);
      map.put("memory", (double)hal.getMemory().getAvailable() / hal.getMemory().getTotal());
      File root = new File("/");
      map.put("disk", (double)root.getFreeSpace() / root.getTotalSpace());
      map.put("cpu", osBean.getSystemCpuLoad());
      ctx.result(new Gson().toJson(map));
    });
  }
}
