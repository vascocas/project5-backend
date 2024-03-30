package paj.project5_vc.bean;

import paj.project5_vc.dao.CategoryDao;
import paj.project5_vc.dao.TaskDao;
import paj.project5_vc.dao.UserDao;
import paj.project5_vc.dto.TaskDto;
import paj.project5_vc.dto.TaskStateDto;
import paj.project5_vc.entity.CategoryEntity;
import paj.project5_vc.entity.TaskEntity;
import paj.project5_vc.entity.UserEntity;
import paj.project5_vc.enums.TaskState;
import paj.project5_vc.enums.UserRole;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;


@Stateless
public class TaskBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    TaskDao taskDao;
    @EJB
    UserDao userDao;
    @EJB
    CategoryDao categoryDao;

    public TaskBean() {
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public TaskDto addTask(String token, TaskDto t) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            TaskEntity taskEntity = convertTaskFromDtoToEntity(t);
            taskEntity.setCreator(userEntity);
            taskEntity.setState(TaskState.TODO);
            taskEntity.setDeleted(false);
            taskDao.persist(taskEntity);
            return convertTaskFromEntityToDto(taskEntity);
        }
        return null;
    }

    public boolean removeTask(String token, int id) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            UserRole userRole = user.getRole();
            // Check if the user is a SCRUM_MASTER or PRODUCT_OWNER
            if (userRole == UserRole.SCRUM_MASTER || userRole == UserRole.PRODUCT_OWNER) {
                TaskEntity t = taskDao.findTaskById(id);
                if (t != null) {
                    t.setDeleted(true);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeAllUserTasks(String token, int userId) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            UserRole userRole = user.getRole();
            // Check if the user is a PRODUCT_OWNER
            if (userRole == UserRole.PRODUCT_OWNER) {
                UserEntity userEntity = userDao.findUserById(userId);
                if (userEntity != null) {
                    ArrayList<TaskEntity> tasks = taskDao.findTasksByUser(userEntity);
                    if (tasks != null) {
                        for (TaskEntity t : tasks) {
                            t.setDeleted(true);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean restoreDeletedTask(String token, int id) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            UserRole userRole = user.getRole();
            // Check if the user is a PRODUCT_OWNER
            if (userRole == UserRole.PRODUCT_OWNER) {
                TaskEntity t = taskDao.findTaskById(id);
                if (t != null) {
                    t.setDeleted(false);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeTaskPermanently(String token, int id) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            UserRole userRole = user.getRole();
            // Check if the user is a PRODUCT_OWNER
            if (userRole == UserRole.PRODUCT_OWNER) {
                TaskEntity t = taskDao.findTaskById(id);
                if (t != null) {
                    taskDao.remove(t);
                    return true;
                }
            }
        }
        return false;
    }

    public TaskDto getTask(int id) {
        TaskEntity t = taskDao.findTaskById(id);
        if (t != null) {
            return convertTaskFromEntityToDto(t);
        } else return new TaskDto();
    }

    public ArrayList<TaskDto> getAllTasks() {
        ArrayList<TaskEntity> tasks = taskDao.findAllActiveTasks();
        if (tasks != null && !tasks.isEmpty()) {
            return convertTasksFromEntityListToDtoList(tasks);
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<TaskDto> getUserTasks(String token, int userId) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            UserRole userRole = user.getRole();
            // Check if the user is a SCRUM_MASTER or PRODUCT_OWNER
            if (userRole == UserRole.SCRUM_MASTER || userRole == UserRole.PRODUCT_OWNER) {
                UserEntity userTask = userDao.findUserById(userId);
                if (userTask != null) {
                    ArrayList<TaskEntity> tasks = taskDao.findTasksByUser(userTask);
                    if (tasks != null) {
                        return convertTasksFromEntityListToDtoList(tasks);
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<TaskDto> getDeletedTasks(String token) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            UserRole userRole = user.getRole();
            // Check if the user is a SCRUM_MASTER or PRODUCT_OWNER
            if (userRole == UserRole.SCRUM_MASTER || userRole == UserRole.PRODUCT_OWNER) {
                ArrayList<TaskEntity> tasks = taskDao.findTasksByDeleted();
                if (tasks != null) {
                    ArrayList<TaskDto> taskDtos = convertTasksFromEntityListToDtoList(tasks);
                    return taskDtos;
                }
            }
        }
        return null;
    }

    public ArrayList<TaskDto> getCategoryTasks(String token, int categoryId) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            UserRole userRole = user.getRole();
            // Check if the user is a SCRUM_MASTER or PRODUCT_OWNER
            if (userRole == UserRole.SCRUM_MASTER || userRole == UserRole.PRODUCT_OWNER) {
                CategoryEntity ctgEntity = categoryDao.findCategoryById(categoryId);
                if (ctgEntity != null) {
                    ArrayList<TaskEntity> tasks = taskDao.findTasksByCategoryId(ctgEntity.getId());
                    if (tasks != null) {
                        ArrayList<TaskDto> taskDtos = convertTasksFromEntityListToDtoList(tasks);
                        return taskDtos;
                    }
                }
            }
        }
        return null;
    }

    public TaskDto updateTask(String token, TaskDto taskDto, CategoryEntity taskCategory) {
        TaskEntity t;
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            UserRole userRole = user.getRole();
            // Check if the user is a DEVELOPER
            if (userRole == UserRole.DEVELOPER) {
                t = taskDao.findTaskByIdAndUser(taskDto.getId(), user.getId());
            } else {
                t = taskDao.findTaskById(taskDto.getId());
            }
            if (t != null) {
                t.setTitle(taskDto.getTitle());
                t.setDescription(taskDto.getDescription());
                t.setStartDate(taskDto.getStartDate());
                t.setEndDate(taskDto.getEndDate());
                t.setPriority(taskDto.getPriority());
                t.setDeleted(taskDto.isDeleted());
                t.setCategory(taskCategory);
                return convertTaskFromEntityToDto(t);
            }
        }
        return new TaskDto();
    }

    public boolean updateTaskStatus(TaskStateDto newStatus) {
        TaskEntity t = taskDao.findTaskById(newStatus.getId());
        if (t != null) {
            TaskState currentState = t.getState();
            TaskState newState = newStatus.getState();
            // Check if the task is transitioning to the DONE state
            if (currentState == TaskState.DOING && newState == TaskState.DONE) {
                t.setCompletedDate(LocalDate.now()); // Set the completion date
            }
            t.setState(newState); // Update the task state
            return true;
        }
        return false;
    }

    public long calculateTaskCompletionTime(TaskEntity task) {
        if (task.getStartDate() != null && task.getCompletedDate() != null) {
            return ChronoUnit.DAYS.between(task.getStartDate(), task.getCompletedDate());
        } else {
            // Handle case where timestamps are not set
            return -1; // or throw an exception
        }
    }

    private TaskDto convertTaskFromEntityToDto(TaskEntity t) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(t.getId());
        taskDto.setTitle(t.getTitle());
        taskDto.setDescription(t.getDescription());
        taskDto.setStartDate(t.getStartDate());
        taskDto.setEndDate(t.getEndDate());
        taskDto.setState(t.getState());
        taskDto.setPriority(t.getPriority());
        taskDto.setDeleted(t.isDeleted());
        taskDto.setCategory(t.getCategory().getCategoryName());
        if (t.getCreator() == null) {
            taskDto.setCreator(null);
        } else {
            taskDto.setCreator(t.getCreator().getUsername());
        }
        return taskDto;
    }

    private TaskEntity convertTaskFromDtoToEntity(TaskDto t) {
        CategoryEntity taskCategory = categoryDao.findCategoryByName(t.getCategory());
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(t.getTitle());
        taskEntity.setDescription(t.getDescription());
        taskEntity.setPriority(t.getPriority());
        taskEntity.setStartDate(t.getStartDate());
        taskEntity.setEndDate(t.getEndDate());
        taskEntity.setDeleted(t.isDeleted());
        taskEntity.setCategory(taskCategory);
        return taskEntity;
    }

    private ArrayList<TaskDto> convertTasksFromEntityListToDtoList(ArrayList<TaskEntity> taskEntityEntities) {
        ArrayList<TaskDto> taskDtos = new ArrayList<>();
        for (TaskEntity t : taskEntityEntities) {
            taskDtos.add(convertTaskFromEntityToDto(t));
        }
        return taskDtos;
    }
}