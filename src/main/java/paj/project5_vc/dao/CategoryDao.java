package paj.project5_vc.dao;

import paj.project5_vc.entity.CategoryEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;

@Stateless
public class CategoryDao extends AbstractDao<CategoryEntity> {

    private static final long serialVersionUID = 1L;

    public CategoryDao() {
        super(CategoryEntity.class);
    }


    public ArrayList<CategoryEntity> findAllCategories() {
        try {
            ArrayList<CategoryEntity> ctgEntityEntities = (ArrayList<CategoryEntity>) em.createNamedQuery("Category.findAllCategories").getResultList();
            return ctgEntityEntities;
        } catch (Exception e) {
            return null;
        }
    }

    public CategoryEntity findCategoryById(int id) {
        try {
            return (CategoryEntity) em.createNamedQuery("Category.findCategoryById").setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public CategoryEntity findCategoryByName(String name) {
        try {
            return (CategoryEntity) em.createNamedQuery("Category.findCategoryByName").setParameter("name", name)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public ArrayList<CategoryEntity> findCategoriesByTaskCount() {
        try {
            return (ArrayList<CategoryEntity>) em.createNamedQuery("Category.findCategoriesByTaskCount")
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}