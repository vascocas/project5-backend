package paj.project5_vc.bean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paj.project5_vc.dao.CategoryDao;
import paj.project5_vc.dao.TaskDao;
import paj.project5_vc.dao.UserDao;
import paj.project5_vc.dto.DayCount;
import paj.project5_vc.dto.TasksSummary;
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
import java.time.temporal.ChronoUnit;
import java.util.*;


@Stateless
public class TaskBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(TaskBean.class);

    @EJB
    TaskDao taskDao;
    @EJB
    UserDao userDao;
    @EJB
    CategoryDao categoryDao;

    public TaskBean() {
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
                    logger.info("User: " + user.getUsername() + " has removed the task id: " + t.getId());
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
                    return convertTasksFromEntityListToDtoList(tasks);
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
                        return convertTasksFromEntityListToDtoList(tasks);
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
            // Add validation to prevent unnecessary updates
            if (currentState != newState) {
                // Check if the task is transitioning to the DONE state (set completion date)
                if (currentState == TaskState.DOING && newState == TaskState.DONE) {
                    t.setCompletedDate(LocalDate.now());
                    // Calculate and set duration in days
                    long duration = ChronoUnit.DAYS.between(t.getStartDate(), t.getCompletedDate());
                    t.setDuration((int) duration);
                }
                t.setState(newState); // Update the task state
                return true;
            }
        }
        return false;
    }

    public List<TasksSummary> countTasksByStatus(String token) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            UserRole userRole = user.getRole();
            // Check if the user is a PRODUCT_OWNER
            if (userRole == UserRole.PRODUCT_OWNER) {
                List<Object[]> taskStateCounts = taskDao.countTasksByStatus();
                List<TasksSummary> taskStateSummaries = new ArrayList<>();
                for (Object[] taskStateCount : taskStateCounts) {
                    Integer stateValue = (Integer) taskStateCount[0];
                    String state = TaskState.fromValue(stateValue).name();
                    int count = ((Number) taskStateCount[1]).intValue();
                    TasksSummary summary = new TasksSummary(state, count);
                    taskStateSummaries.add(summary);
                }
                return taskStateSummaries;
            }
        }
        return Collections.emptyList(); // Return an empty list if user is not authenticated or authorized
    }

    public List<TasksSummary> getCategoryTasksBySum(String token) {
        // Get user role by token
        UserEntity user = userDao.findUserByToken(token);
        if (user != null) {
            UserRole userRole = user.getRole();
            // Check if the user is a PRODUCT_OWNER
            if (userRole == UserRole.PRODUCT_OWNER) {
                List<Object[]> categoryTasks = taskDao.countTasksByCategoryOrderedByCount();
                List<TasksSummary> categoryTaskSummaries = new ArrayList<>();
                for (Object[] categoryTask : categoryTasks) {
                    Integer categoryId = (Integer) categoryTask[0]; // Category ID is Integer
                    String category = getCategoryNameById(categoryId); // Get category name by ID
                    int taskCountSum = ((Number) categoryTask[1]).intValue();
                    TasksSummary summary = new TasksSummary(category, taskCountSum);
                    categoryTaskSummaries.add(summary);
                }
                return categoryTaskSummaries;
            }
        }
        return Collections.emptyList(); // Return an empty list if user is not authenticated or authorized
    }

    // Helper method to get category name by ID
    private String getCategoryNameById(int categoryId) {
        CategoryEntity category = categoryDao.findCategoryById(categoryId);
        return category.getCategoryName();
    }

    public List<DayCount> getCompletedTasksCumulative() {
        List<DayCount> counts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int cumulativeCount = 0;
        for (int i = 6; i >= 0; i--) {
            LocalDate currentDate = today.minusDays(i);
            int taskCount = taskDao.countCompletedTasksByDate(currentDate);
            cumulativeCount += taskCount;
            counts.add(new DayCount(currentDate, cumulativeCount));
        }
        return counts;
    }

    public double getAverageTaskDuration() {
        return taskDao.findAverageTaskDuration();
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