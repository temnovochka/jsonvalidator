import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JSONValidator{
    private static Logger log = Logger.getLogger(JSONValidator.class.getName());

    private static final int RETURN_CODE = 250;

    /* server instance */
    private HttpServer server;
    /* JSON builder from String instance*/
    private Gson gsonBuilder;

    /* Main method*/
    public JSONValidator() throws IOException {
        /* Init objects*/
        server = HttpServer.create();
        gsonBuilder = new GsonBuilder().setPrettyPrinting().create();

        /* Bind server to specific port*/
        server.bind(new InetSocketAddress(80), 0);
        server.createContext("/", new HttpHandler() {
            /* Function to handle input from client */
            public void handle(HttpExchange exchange) throws IOException {
                /* Stream, that read from socket */
                InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody());
                String httpString = new BufferedReader(inputStreamReader).lines().collect(Collectors.joining());
                /* Log results*/
                log.info(httpString);
                String jsonString = null;

                try{
                    /* Trying to build json
                    * Incorrect input string will raise exception
                    * */
                    Object gsonObject = gsonBuilder.fromJson(httpString, Object.class);
                    jsonString = gsonBuilder.toJson(gsonObject);
                }
                catch(JsonSyntaxException ex){
                    /* Forming error object to return to client */
                    JsonObject error = new JsonObject();
                    error.addProperty("Code", ex.hashCode());
                    error.addProperty("Message", ex.getMessage());

                    /* Exception logging */
                    log.warning("Exception rised!");
                    log.warning(ex.getMessage());

                    jsonString = gsonBuilder.toJson(error);
                }

                /* Return message to client */
                exchange.sendResponseHeaders(RETURN_CODE, jsonString.length());
                exchange.getResponseBody().write(jsonString.getBytes());
                /* Close socket for http */
                exchange.close();
            }
        });
    }

    public void start(){
        this.server.start();
    }

    public void stop (){
        this.server.stop(0);
    }
}
