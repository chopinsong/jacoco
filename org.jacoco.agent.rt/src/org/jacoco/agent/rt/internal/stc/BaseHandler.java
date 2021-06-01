package org.jacoco.agent.rt.internal.stc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

/**
 * 请求处理基类
 *
 * @author songyanc
 */
public abstract class BaseHandler implements HttpHandler {
    /**
     * 处理请求
     *
     * @param exchange 请求数据
     * @param body     body
     * @return 影响休
     */
    public abstract JsonObject onHandle(HttpExchange exchange, JsonObject body);

    @Override
    public void handle(HttpExchange exchange) {
        try {
            JsonObject body = getBody(exchange);
            JsonObject baseResponse = onHandle(exchange, body);
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(new Gson().toJson(baseResponse).getBytes(getCharset()));
            os.close();
        } catch (Exception e) {
            printStack(e);
            throw new FpxException(e);
        }
    }

    protected JsonObject getBody(HttpExchange exchange) {
        InputStream is = null;
        try{
            is = exchange.getRequestBody();
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            return JsonParser.parseString(new String(bytes)).getAsJsonObject();
        } catch (Exception e) {
            printStack(e);
            return new JsonObject();
        }finally {
            if (null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                    printStack(e);
                }
            }
        }
    }

    JsonObject successResponse( ) {
        JsonObject res = new JsonObject();
        res.addProperty("code", 0);
        res.addProperty("msg", "success");
        return res;
    }
    JsonObject successResponse(Object data) {
        JsonObject res = successResponse();
        res.add("data", JsonParser.parseString(new Gson().toJson(data)));
        return res;
    }

    JsonObject failResponse(String msg) {
        JsonObject res = new JsonObject();
        res.addProperty("code", 1);
        res.addProperty("msg", msg);
        return res;
    }

    private String getCharset() {
        return isWindows() ? "GBK" : "utf-8";
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().contains("WINDOWS");
    }

     void printStack(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        System.out.println(stringWriter);
    }

    static class FpxException extends RuntimeException {
        public FpxException(String message) {
            super(message);
        }

        public FpxException(Exception e) {
            super(e);
        }
    }

}
