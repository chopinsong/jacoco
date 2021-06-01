package org.jacoco.agent.rt.internal.stc;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import org.jacoco.agent.rt.internal.Agent;

import java.lang.ref.WeakReference;

/**
 * @author songyanc
 */
public class ResetHandler extends BaseHandler {
    private WeakReference<Agent> instance;

    public ResetHandler(WeakReference<Agent> instance) {
        this.instance = instance;
    }

    @Override
    public JsonObject onHandle(HttpExchange exchange, JsonObject body) {
        if (null==instance||null==instance.get()){
            throw new FpxException("agent为空");
        }
        try {
            instance.get().reset();
        } catch (Exception e) {
            System.out.println("重置失败");
            printStack(e);
            return failResponse("重置失败");
        }
        return successResponse();
    }
}
