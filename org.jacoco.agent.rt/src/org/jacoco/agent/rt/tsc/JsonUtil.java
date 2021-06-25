package org.jacoco.agent.rt.tsc;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * json工具
 *
 * @author songyanc
 */
public class JsonUtil {
    /**
     * json转对象
     *
     * @param json json字符串
     * @return Map对象
     */
    public static Map<String, Object> parse(String json) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        try {
            engine.eval("function parse(json){return JSON.parse(json);};");
        } catch (ScriptException e) {
            return new HashMap<String, Object>(0);
        }
        IJson iJson = ((Invocable) engine).getInterface(IJson.class);
        return iJson.parse(json);
    }

    public static <T> T parse(String json, Class<T> t) {
        Map<String, Object> targetData = parse(json);
        return setField(t, targetData);
    }

    private static <T> T setField(Class<T> t, Map<String, Object> targetData) {
        T instance;
        try {
            instance = t.newInstance();
        } catch (Exception e) {
            return null;
        }
        for (Map.Entry<String, Object> entry : targetData.entrySet()) {
            String key = entry.getKey();
            Field field;
            try {
                field = t.getDeclaredField(key);
            } catch (Exception e) {
                continue;
            }
            Object value = entry.getValue();
            if (value instanceof ScriptObjectMirror) {
                field.setAccessible(true);
                try {
                    field.set(instance, setField(field.getType(), (Map<String, Object>) value));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                field.setAccessible(true);
                try {
                    field.set(instance, value);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
        return instance;
    }

    public interface IJson {
        /**
         * json转对象
         *
         * @param json json字符串
         * @return Map对象
         */
        Map<String, Object> parse(String json);
    }

}
