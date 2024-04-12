package paj.project5_vc.dao;

import paj.project5_vc.entity.TokenEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import paj.project5_vc.entity.UserEntity;

import java.util.ArrayList;

@Stateless
public class TokenDao extends AbstractDao<TokenEntity> {

    private static final long serialVersionUID = 1L;

    public TokenDao() {
        super(TokenEntity.class);
    }

    public TokenEntity findTokenByValue(String tokenValue) {
        try {
            return (TokenEntity) em.createNamedQuery("Token.findTokenByValue")
                    .setParameter("tokenValue", tokenValue)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ArrayList<TokenEntity> findAllTokensByUserId(int userId) {
        try {
            ArrayList<TokenEntity> tokensByUserId = (ArrayList<TokenEntity>) em.createNamedQuery("Token.findTokensByUserId")
                    .setParameter("userId", userId)
                    .getResultList();
            return tokensByUserId;
        } catch (Exception e) {
            return null;
        }
    }

}