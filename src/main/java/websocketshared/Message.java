package websocketshared;

public class Message {

    private String messageType;
    private Object[] parameters;
    private String methodName;
    private Object responds;

    public Object getResponds() {
        return responds;
    }

    public void setResponds(Object _responds){
        responds = _responds;
    }

    public Object[] getParameters(){
        return parameters;
    }

    public void setParameters(Object[] _parameters){
        parameters = _parameters;
    }

    public String getMethodName(){
        return methodName;
    }

    public void setMethodName(String _methodName){
        methodName = _methodName;
    }

    public String getMessageType(){
        return messageType;
    }

    public void setMessageType(String _messageType){
        messageType = _messageType;
    }
}