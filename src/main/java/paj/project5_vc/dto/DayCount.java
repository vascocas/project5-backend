package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.time.LocalDate;


@XmlRootElement
public class DayUserCount implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlElement
    private LocalDate date;
    @XmlElement
    private int userCount;

    public DayUserCount(LocalDate date, int userCount) {
        this.date = date;
        this.userCount = userCount;
    }

    // Getters and setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    @Override
    public String toString() {
        return "DayUserCount{" +
                "date=" + date +
                ", userCount=" + userCount +
                '}';
    }
}
