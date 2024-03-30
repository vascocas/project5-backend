package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import paj.project5_vc.enums.TaskState;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement
public class TaskStateDto {

    @XmlElement
    private int id;
    @XmlElement
    private TaskState state;

    public TaskStateDto() {
    }

    public TaskStateDto(int id, TaskState state) {
        this.id = id;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }
}