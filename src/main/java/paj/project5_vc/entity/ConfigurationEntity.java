package paj.project5_vc.entity;

import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "configuration")
@NamedQuery(name = "Configuration.findLatestConfiguration", query = "SELECT c FROM ConfigurationEntity c WHERE c.configId = (SELECT MAX(cc.configId) FROM ConfigurationEntity cc)")


public class ConfigurationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id", nullable = false, unique = true, updatable = false)
    private int configId;

    @Column(name = "token_timer")
    private int tokenTimer;

    public ConfigurationEntity() {
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public int getTokenTimer() {
        return tokenTimer;
    }

    public void setTokenTimer(int tokenTimer) {
        this.tokenTimer = tokenTimer;
    }
}
