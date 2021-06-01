package org.jacoco.agent.rt.internal.stc;

import com.sun.net.httpserver.HttpServer;
import org.jacoco.agent.rt.internal.Agent;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

/**
 * @author songyanc
 */
public class FpxWeb {
    private WeakReference<Agent> instance;
    private HttpServer server;

    public FpxWeb(Agent agent) {
        instance = new WeakReference<Agent>(agent);
    }

    public void run() {
        if (null==instance||null==instance.get()){
            System.out.println("agent实例为空");
            return;
        }
        try {
            InetSocketAddress address = new InetSocketAddress(8001);
            server = HttpServer.create(address, 0);
            server.createContext("/reset", new ResetHandler(instance));
            server.createContext("/getExecutionData", new ExecutionDataHandler(instance));
            server.start();
        } catch (IOException e) {
            System.out.println("启动失败"+e.getMessage());
        }
    }


    public void stop() {
        if (null!=server){
            server.stop(0);
        }
    }
}
