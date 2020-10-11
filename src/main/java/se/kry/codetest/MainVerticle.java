package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainVerticle extends AbstractVerticle {

  private HashMap<String, String> services = new HashMap<>();
  private DBConnector connector;
  private BackgroundPoller poller = new BackgroundPoller();

  @Override
  public void start(Future<Void> startFuture) {
    connector = new DBConnector(vertx);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    // read list from database
    Future<ResultSet> servicesList = connector.query("SELECT * FROM service");
    ResultSet rs = servicesList.result();

    //temporary test data in a variable
    services.put("https://www.kry.se", "UNKNOWN");
    services.put("http://www.my.test", "OK");
    services.put("https://www.another.test", "FAIL");

    vertx.setPeriodic(1000 * 20, timerId -> poller.pollServices(services, vertx, connector));
    setRoutes(router);
    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(8080, result -> {
          if (result.succeeded()) {
            System.out.println("KRY code test service started");
            startFuture.complete();
          } else {
            startFuture.fail(result.cause());
          }
        });
  }

  private void setRoutes(Router router){
    router.route("/*").handler(StaticHandler.create());
    router.get("/service").handler(req -> {
      List<JsonObject> jsonServices = services
          .entrySet()
          .stream()
          .map(service ->
              new JsonObject()
                  .put("name", service.getKey())
                  .put("status", service.getValue()))
          .collect(Collectors.toList());
      req.response()
          .putHeader("content-type", "application/json")
          .putHeader("Access-Control-Allow-Origin", "*")
          .end(new JsonArray(jsonServices).encode());
    });
    router.post("/service").handler(req -> {
      JsonObject jsonBody = req.getBodyAsJson();
      services.put(jsonBody.getString("url"), "UNKNOWN");
      req.response()
          .putHeader("content-type", "text/plain")
          .putHeader("Access-Control-Allow-Origin", "*")
          .end("OK");
    });
    router.post("/delete").handler(req -> {
      JsonObject jsonBody = req.getBodyAsJson();
      services.remove(jsonBody.getString("url"));
      req.response()
              .putHeader("content-type", "text/plain")
              .putHeader("Access-Control-Allow-Origin", "*")
              .end("REMOVED");
    });
  }

}



