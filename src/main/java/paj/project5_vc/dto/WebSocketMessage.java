package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

@XmlRootElement
public class WebSocketMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement
    private String action;
    @XmlElement
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

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "action='" + action + '\'' +
                ", object=" + object +
                '}';
    }
}
