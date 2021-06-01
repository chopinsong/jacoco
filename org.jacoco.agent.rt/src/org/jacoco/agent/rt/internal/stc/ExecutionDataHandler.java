package org.jacoco.agent.rt.internal.stc;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import org.jacoco.agent.rt.internal.Agent;

import java.lang.ref.WeakReference;
import java.nio.charset.Charset;

/**
 * @author songyanc
 */
public class ExecutionDataHandler extends BaseHandler {
    private WeakReference<Agent> instance;

    public ExecutionDataHandler(WeakReference<Agent> instance) {
        this.instance = instance;
    }

    @Override
    public JsonObject onHandle(HttpExchange exchange, JsonObject body) {
        if (null == instance || null == instance.get()) {
            throw new FpxException("agent实例空");
        }
        byte[] executionData = new byte[0];
        try {
            executionData = instance.get().getExecutionData(false);
        } catch (Exception e) {
            System.out.println("获取数据失败");
            printStack(e);
            return failResponse("获取数据失败");
        }
        return successResponse(new String(executionData, Charset.forName("utf-8")));
    }

}
