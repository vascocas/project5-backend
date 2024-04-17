package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.time.LocalDate;


@XmlRootElement
public class DayCount implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement
    private LocalDate date;
    @XmlElement
    private int value;

    public DayCount(LocalDate date, int value) {
        this.date = date;
        this.value = value;
    }

    // Getters and setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DayCount{" +
                "date=" + date +
                ", value=" + value +
                '}';
    }
}
