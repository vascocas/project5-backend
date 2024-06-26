package paj.project5_vc.bean;

import paj.project5_vc.dao.CategoryDao;
import paj.project5_vc.dao.TaskDao;
import paj.project5_vc.dao.UserDao;
import paj.project5_vc.dto.CategoryDto;
import paj.project5_vc.entity.CategoryEntity;
import paj.project5_vc.entity.TaskEntity;
import paj.project5_vc.entity.UserEntity;
import paj.project5_vc.enums.UserRole;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.io.Serializable;
import java.util.ArrayList;

@Stateless
public class CategoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    CategoryDao categoryDao;
    @EJB
    TaskDao taskDao;
    @EJB
    UserDao userDao;

    public CategoryBean() {
    }

    public ArrayList<CategoryDto> getAllCategories() {
        ArrayList<CategoryEntity> categories = categoryDao.findAllCategories();
        if (categories != null && !categories.isEmpty()) {
            ArrayList<CategoryDto> ctgDtos = convertCategoriesFromEntityListToDtoList(categories);
            return ctgDtos;
        } else {
            return new ArrayList<>();
        }
    }

    public CategoryDto getCategoryById(int id) {
        CategoryEntity category = categoryDao.findCategoryById(id);
        if (category != null) {
            CategoryDto ctgDto = convertCategoryFromEntityToDto(category);
            return ctgDto;
        } else {
            return new CategoryDto();
        }
    }

    public boolean addCategory(String token, CategoryDto category) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        UserRole userRole = user.getRole();
        // Check if the user is a PRODUCT_OWNER
        if (userRole == UserRole.PRODUCT_OWNER) {
            CategoryEntity c = categoryDao.findCategoryByName(category.getName());
            if (c == null) {
                CategoryEntity ctgEntity = new CategoryEntity();
                ctgEntity.setCategoryName(category.getName());
                categoryDao.persist(ctgEntity);
                return true;
            }
        }
        return false;
    }

    public boolean removeCategory(String token, int categoryId) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        UserRole userRole = user.getRole();
        // Check if the user is a PRODUCT_OWNER
        if (userRole == UserRole.PRODUCT_OWNER) {
            CategoryEntity c = categoryDao.findCategoryById(categoryId);
            if (c != null) {
                ArrayList<TaskEntity> tasks = taskDao.findTasksByCategoryId(categoryId);
                if (tasks == null || tasks.isEmpty()) {
                    categoryDao.remove(c);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean updateCategoryName(String token, CategoryDto ctg) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        UserRole userRole = user.getRole();
        // Check if the user is a PRODUCT_OWNER
        if (userRole == UserRole.PRODUCT_OWNER) {
            CategoryEntity c = categoryDao.findCategoryById(ctg.getId());
            if (c != null) {
                CategoryEntity c1 = categoryDao.findCategoryByName(ctg.getName());
                if (c1 == null || c1.getId() == c.getId()) {
                    c.setCategoryName(ctg.getName());
                    return true;
                }
            }
        }
        return false;
    }

    private CategoryEntity convertCategoryFromDtoToEntity(CategoryDto c) {
        CategoryEntity ctgEntity = new CategoryEntity();
        ctgEntity.setId(c.getId());
        ctgEntity.setCategoryName(c.getName());
        return ctgEntity;
    }

    private CategoryDto convertCategoryFromEntityToDto(CategoryEntity c) {
        CategoryDto ctgDto = new CategoryDto();
        ctgDto.setId(c.getId());
        ctgDto.setName(c.getCategoryName());
        return ctgDto;
    }

    private ArrayList<CategoryDto> convertCategoriesFromEntityListToDtoList
            (ArrayList<CategoryEntity> ctgEntityEntities) {
        ArrayList<CategoryDto> ctgDtos = new ArrayList<>();
        for (CategoryEntity c : ctgEntityEntities) {
            ctgDtos.add(convertCategoryFromEntityToDto(c));
        }
        return ctgDtos;
    }

}