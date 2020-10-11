package se.kry.codetest;

import io.vertx.core.Future;

import java.util.List;
import java.util.Map;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import se.kry.codetest.DBConnector;

public class BackgroundPoller {

  public Future<List<String>> pollServices(Map<String, String> services, Vertx vertx, DBConnector connector) {

    services.forEach((url, status) -> {
      WebClient.create(vertx).getAbs(url).send(result -> {
        System.out.println("POLLER RESULT :"+ result);
        if (result.result() == null) {
          services.put(url, "FAIL");
          //TODO update line in the database
          return;
        }

        if (result.result().statusCode() == 200) {
          services.put(url, "OK");
          //TODO update line in the database
        } else {
          services.put(url, "FAIL");
          //TODO update line in the database
        }
      });
    });
    return Future.succeededFuture();
  }
  }

