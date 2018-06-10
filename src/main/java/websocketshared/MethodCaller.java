package websocketshared;

import tankgamegui.enums.BlockType;
import tankgamegui.enums.Methods;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MethodCaller {

    public void reflectiveMethodCaller(Message message, Object controller) {
        reflectionInternal(message.getMethodName(), message.getParameters(), controller);
    }

    private void reflectionInternal(String methodName, Object[] params, Object controller) {
        java.lang.reflect.Method method;
        Class[] paramClasses = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramClasses[i] = params[i].getClass();
            convertToInt(params, paramClasses, i);
            Object isEnum = enumMap.get(params[i]);
            if (isEnum != null) {
                params[i] = isEnum;
                paramClasses[i] = params[i].getClass();
            }
        }
        try {
            method = controller.getClass().getDeclaredMethod(methodName, paramClasses);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                method.invoke(controller, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void convertToInt(Object[] params, Class[] argClasses, int i) {
        if (primitiveMap.containsKey(argClasses[i])) {
            argClasses[i] = primitiveMap.get(argClasses[i]);
            if (argClasses[i].getSimpleName().equalsIgnoreCase("int")) {
                params[i] = ((Double) params[i]).intValue();
            }
        }
    }

    private final static Map<Class<?>, Class<?>> primitiveMap = new HashMap<>();

    static {
        primitiveMap.put(Boolean.class, boolean.class);
        primitiveMap.put(Byte.class, byte.class);
        primitiveMap.put(Short.class, short.class);
        primitiveMap.put(Character.class, char.class);
        primitiveMap.put(Integer.class, int.class);
        primitiveMap.put(Long.class, long.class);
        primitiveMap.put(Float.class, float.class);
        primitiveMap.put(Double.class, int.class);
    }

    private final static Map<String, Enum<?>> enumMap = new HashMap<>();

    static {
        enumMap.put("WATER", BlockType.WATER);
        enumMap.put("JUMP", Methods.JUMP);
    }
}