package paj.project5_vc.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;


@Entity
@Table(name = "token")
@NamedQuery(name = "Token.findTokenByValue", query = "SELECT t FROM TokenEntity t WHERE t.tokenValue = :tokenValue")
@NamedQuery(name = "Token.findTokensByUserId", query = "SELECT t FROM TokenEntity t WHERE t.user.id = :userId")

public class TokenEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false, unique = true, updatable = false)
    private int tokenId;

    @Column(name = "token_value", unique = true)
    private String tokenValue;

    @Column(name = "token_expiration")
    private Instant tokenExpiration;

    //Owning Side User - Token
    @ManyToOne
    private UserEntity user;

    public TokenEntity() {
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Instant getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Instant tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}