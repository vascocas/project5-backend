package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

@XmlRootElement
public class CategoryTasksSummary implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement
    private String category;
    @XmlElement
    private int taskCountSum;

    public CategoryTasksSummary() {
    }

    public CategoryTasksSummary(String category, int taskCountSum) {
        this.category = category;
        this.taskCountSum = taskCountSum;
    }

    // Getters and setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTaskCountSum() {
        return taskCountSum;
    }

    public void setTaskCountSum(int taskCountSum) {
        this.taskCountSum = taskCountSum;
    }

    @Override
    public String toString() {
        return "CategoryTasksSummary{" +
                "category='" + category + '\'' +
                ", taskCountSum=" + taskCountSum +
                '}';
    }
}
