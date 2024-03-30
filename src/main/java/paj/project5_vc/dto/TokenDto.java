package paj.project5_vc.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenDto {

    @XmlElement
    private int timer;

    public TokenDto() {
    }

    public TokenDto(int timer) {
        this.timer = timer;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

}
