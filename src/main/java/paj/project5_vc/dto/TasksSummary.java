package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

@XmlRootElement
public class TasksSummary implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement
    private String field;
    @XmlElement
    private int sum;

    public TasksSummary() {
    }

    public TasksSummary(String field, int sum) {
        this.field = field;
        this.sum = sum;
    }

    // Getters and setters
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "field='" + field + '\'' +
                ", sum=" + sum;
    }
}
