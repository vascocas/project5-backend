package paj.project5_vc.dto;

public class WebSocketMessage {

    private String action;
    private Object object;

    public WebSocketMessage(String action, Object object) {
        this.action = action;
        this.object = object;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
