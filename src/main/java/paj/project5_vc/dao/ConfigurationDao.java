package paj.project5_vc.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import paj.project5_vc.entity.ConfigurationEntity;


    @Stateless
    public class ConfigurationDao extends AbstractDao<ConfigurationEntity> {

        private static final long serialVersionUID = 1L;

        public ConfigurationDao() {
            super(ConfigurationEntity.class);
        }

        public ConfigurationEntity findTokenTimer() {
            try {
                return (ConfigurationEntity) em.createNamedQuery("Configuration.findLatestConfiguration")
                        .getSingleResult();
            } catch (NoResultException e) {
                // Handle if no configuration is found
                ConfigurationEntity defaultConfig = new ConfigurationEntity();
                defaultConfig.setTokenTimer(10000); // Provide a default value
                persist(defaultConfig);
                return defaultConfig;
            }
        }

}
