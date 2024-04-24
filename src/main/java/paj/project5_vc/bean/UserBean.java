package paj.project5_vc.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paj.project5_vc.dao.ConfigurationDao;
import paj.project5_vc.dao.TaskDao;
import paj.project5_vc.dao.TokenDao;
import paj.project5_vc.dao.UserDao;
import paj.project5_vc.dto.*;
import paj.project5_vc.entity.ConfigurationEntity;
import paj.project5_vc.entity.TaskEntity;
import paj.project5_vc.entity.TokenEntity;
import paj.project5_vc.entity.UserEntity;
import paj.project5_vc.enums.TaskState;
import paj.project5_vc.enums.UserRole;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.io.Serializable;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Stateless
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(UserBean.class);

    @EJB
    UserDao userDao;
    @EJB
    TaskDao taskDao;
    @EJB
    TokenDao tokenDao;
    @EJB
    ConfigurationDao configDao;
    @EJB
    PassEncoder passEncoder;
    @EJB
    EmailBean email;


    public TokenDto getTokenTimer() {
        ConfigurationEntity timer = configDao.findTokenTimer();
        TokenDto tokenDto = new TokenDto();
        tokenDto.setTimer(timer.getTokenTimer());
        return tokenDto;
    }

    public boolean setTokenTimer(String token, TokenDto tokenTimer) {
        // Get user role by token
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check if the user is not a DEVELOPER or SCRUM_MASTER
            if (userRole != UserRole.DEVELOPER && userRole != UserRole.SCRUM_MASTER) {
                ConfigurationEntity timer = configDao.findTokenTimer();
                timer.setTokenTimer(tokenTimer.getTimer());
                return true;
            }
        }
        return false;
    }

    public LoginDto login(String username, String password) {
        LoginDto successLogin = new LoginDto();
        UserEntity userEntity = userDao.findUserByUsername(username);
        if (userEntity != null && !userEntity.isDeleted() && userEntity.isValidated()) {
            // Retrieve the hashed password associated with the user
            String hashedPassword = userEntity.getPassword();
            // Check if the provided password matches the hashed password
            if (passEncoder.matches(password, hashedPassword)) {
                String tokenValue = generateNewToken();
                TokenEntity tokenEntity = new TokenEntity();
                tokenEntity.setTokenValue(tokenValue);
                tokenEntity.setUser(userEntity);
                ConfigurationEntity timer = configDao.findTokenTimer();
                tokenEntity.setTokenExpiration(Instant.now().plus(timer.getTokenTimer(), ChronoUnit.MINUTES));
                tokenDao.persist(tokenEntity);
                successLogin.setToken(tokenValue);
                successLogin.setId(userEntity.getId());
                successLogin.setUsername(username);
                successLogin.setRole(userEntity.getRole());
                successLogin.setPhoto(userEntity.getPhoto());
                logger.warn("User " + username + " is successfully logged in");
                return successLogin;
            }
        }
        return null;
    }

    private String generateNewToken() {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    private boolean isTokenValid(TokenEntity t) {
        Instant expiration = t.getTokenExpiration();
        if (expiration != null && expiration != null && expiration.isAfter(Instant.now())) {
            return true;
        }
        return false;
    }

    public boolean register(UserDto user) {
        // Check if a user with the provided username and email already exists
        UserEntity userByUsername = userDao.findUserByUsername(user.getUsername());
        UserEntity userByEmail = userDao.findUserByEmail(user.getEmail());
        if ((userByUsername == null) && (userByEmail == null)) {
            UserEntity newUser = convertUserDtotoEntity(user);
            String validationToken = generateNewToken();
            newUser.setValidationToken(validationToken);
            newUser.setRole(UserRole.DEVELOPER);
            userDao.persist(newUser);
            email.sendConfirmationEmail("aor.scrum.board@gmail.com", validationToken);
            return true;
        }
        return false;
    }

    public boolean validateUser(String token) {
        UserEntity userEntity = userDao.findUserByValidationToken(token);
        if (userEntity != null) {
            if(userEntity.getValidationToken().equals(token)) {
                userEntity.setValidated(true);
                return true;
            }
        }
        return false;
    }

    public UserDto createUser(String token, UserDto user) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check if the user is a DEVELOPER or SCRUM_MASTER: cannot create user
            if (userRole != UserRole.DEVELOPER && userRole != UserRole.SCRUM_MASTER) {
                // Check if a user with the provided username and email already exists
                UserEntity userByUsername = userDao.findUserByUsername(user.getUsername());
                UserEntity userByEmail = userDao.findUserByEmail(user.getEmail());
                if ((userByUsername == null) && (userByEmail == null)) {
                    UserEntity newUser = convertUserDtotoEntity(user);
                    newUser.setRole(user.getRole());
                    userDao.persist(newUser);
                    return convertUserEntitytoUserDto(newUser);
                }
            }
        }
        return null;
    }

    public boolean logout(String token) {
        UserEntity u = userDao.findUserByToken(token);
        if (u != null) {
            TokenEntity t = tokenDao.findTokenByValue(token);
            tokenDao.remove(t);
            return true;
        }
        return false;
    }

    public boolean tokenExist(String token) {
        UserEntity u = userDao.findUserByToken(token);
        TokenEntity t = tokenDao.findTokenByValue(token);
        if (u != null && isTokenValid(t)) {
            ConfigurationEntity timer = configDao.findTokenTimer();
            t.setTokenExpiration(Instant.now().plus(timer.getTokenTimer(), ChronoUnit.MINUTES));
            return true;
        } else {
            return false;
        }
    }

    public UserDto userById(int id) {
        UserEntity u = userDao.findUserById(id);
        if (u != null) {
            UserDto dto = convertUserEntitytoUserDto(u);
            return dto;
        } else return new UserDto();
    }

    public UserDto getLoggedProfile(String token) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserDto userDto = convertUserEntitytoUserDto(userEntity);
            return userDto;
        }
        return new UserDto();
    }

    public UserDto getProfile(String token, int userId) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check if the user is a DEVELOPER or SCRUM_MASTER: can only edit own profile
            if (userRole != UserRole.DEVELOPER && userRole != UserRole.SCRUM_MASTER) {
                UserEntity userEdit = userDao.findUserById(userId);
                if (userEdit != null && userEdit.isValidated()) {
                    UserDto userDto = convertUserEntitytoUserDto(userEdit);
                    return userDto;
                }
            }
        }
        return new UserDto();
    }

    public PublicProfileDto getPublicProfile(String username) {
        UserEntity userEntity = userDao.findUserByUsername(username);
        if (userEntity != null) {
            PublicProfileDto profileDto = new PublicProfileDto();
            profileDto.setUserId(userEntity.getId());
            profileDto.setUsername(userEntity.getUsername());
            profileDto.setEmail(userEntity.getEmail());
            profileDto.setFirstName(userEntity.getFirstName());
            profileDto.setLastName(userEntity.getLastName());
            profileDto.setPhoto(userEntity.getPhoto());
            profileDto.setTotalTasks(taskDao.findTotalTasksByUser(username));
            profileDto.setTotalToDoTasks(taskDao.findTotalTasksByStateAndUser(username, TaskState.TODO));
            profileDto.setTotalDoingTasks(taskDao.findTotalTasksByStateAndUser(username, TaskState.DOING));
            profileDto.setTotalDoneTasks(taskDao.findTotalTasksByStateAndUser(username, TaskState.DONE));
            return profileDto;
        }
        return new PublicProfileDto();
    }

    public boolean editProfile(UserDto user, String token) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            userEntity.setEmail(user.getEmail());
            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());
            userEntity.setPhone(user.getPhone());
            userEntity.setPhoto(user.getPhoto());
            return true;
        }
        return false;
    }

    public boolean editUsersProfile(UserDto user, String token) {
        // Get user role by token
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check if the user is a DEVELOPER or SCRUM_MASTER: can only edit own profile
            if (userRole != UserRole.DEVELOPER && userRole != UserRole.SCRUM_MASTER) {
                UserEntity u = userDao.findUserById(user.getId());
                if (u != null) {
                    u.setEmail(user.getEmail());
                    u.setFirstName(user.getFirstName());
                    u.setLastName(user.getLastName());
                    u.setPhone(user.getPhone());
                    u.setPhoto(user.getPhoto());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean editUserPassword(String token, PasswordDto newPass) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            // Retrieve the hashed password associated with the user
            String hashedPassword = userEntity.getPassword();
            if ((passEncoder.matches(newPass.getPassword(), hashedPassword)) && (newPass.getNewPass().equals(newPass.getConfirmPass()))) {
                String encryptedPassword = passEncoder.encode(newPass.getNewPass());
                userEntity.setPassword(encryptedPassword);
                return true;
            }
        }
        return false;
    }

    public boolean editUsersPassword(String token, PasswordDto newPass) {
        // Get user role by token
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check if the user is a DEVELOPER or SCRUM_MASTER: can only edit own profile
            if (userRole != UserRole.DEVELOPER && userRole != UserRole.SCRUM_MASTER) {
                UserEntity u = userDao.findUserById(newPass.getId());
                if (u != null) {
                    String hashedPassword = u.getPassword();
                    if ((passEncoder.matches(newPass.getPassword(), hashedPassword)) && (newPass.getNewPass().equals(newPass.getConfirmPass()))) {
                        String encryptedPassword = passEncoder.encode(newPass.getNewPass());
                        u.setPassword(encryptedPassword);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean updateRole(RoleDto user, String token) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check if the user is not a DEVELOPER or SCRUM_MASTER: cannot change user role
            // if (userRole == UserRole.DEVELOPER || userRole == UserRole.SCRUM_MASTER) {
            //    return false;
            //}
            UserEntity u = userDao.findUserById(user.getId());
            if (u != null) {
                u.setRole(user.getRole());
                userDao.persist(u);
                return true;
            }
        }
        return false;
    }

    public RoleDto getRole(String token) {
        UserEntity u = userDao.findUserByToken(token);
        RoleDto dto = new RoleDto();
        if (u != null) {
            dto.setRole(u.getRole());
            return dto;
        } else return new RoleDto();
    }

    public ArrayList<RoleDto> getAllUsernames() {
        ArrayList<UserEntity> users = userDao.findAllActiveUsernames();
        if (users != null && !users.isEmpty()) {
            return convertUsersFromEntityListToRoleDtoList(users);
        } else {
            return new ArrayList<>();
        }
    }


    public UserTableDto getUsers(String token, UserRole role, String order, int page, int pageSize) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check if the user isn't a DEVELOPER or SCRUM_MASTER: cannot get all users list
            if (userRole != UserRole.DEVELOPER && userRole != UserRole.SCRUM_MASTER) {
                ArrayList<UserEntity> userList;
                int totalItems;
                // Calculate the offset based on the page number and page size
                int offset = (page - 1) * pageSize;
                // Check if there is a filter parameter defined for role
                if (role == UserRole.DEVELOPER || role == UserRole.SCRUM_MASTER || role == UserRole.PRODUCT_OWNER) {
                    // Apply pagination parameters to the query
                    userList = userDao.findUsersByRole(role, order, offset, pageSize);
                    // Get the total count of users
                    totalItems = userDao.findTotalUsersCountByRole(role);
                } else {
                    userList = userDao.findAllActiveUsers(order, offset, pageSize);
                    // Get the total count of users
                    totalItems = userDao.findTotalValidatedUsers();
                }
                if (userList != null) {
                    // Get the total number of pages based on the total items and page size
                    int totalPages = (int) Math.ceil((double) totalItems / pageSize);
                    if (page > totalPages) {
                        page = totalPages;
                    }
                    // Create and return the response object with users, total items, total pages, current page, and page size
                    return new UserTableDto(convertUsersFromEntityToUserManagmentDtoList(userList), totalItems, totalPages, page, pageSize);
                }
            }
        }
        return null;
    }

    public ArrayList<UserManagementDto> getDeletedUsers(String token) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check if the user is a DEVELOPER: cannot get deleted users list
            if (userRole == UserRole.DEVELOPER) {
                return null;
            }
            ArrayList<UserEntity> userDeletedList = userDao.findAllDeletedUsers();
            if (userDeletedList != null) {
                return convertUsersFromEntityToUserManagmentDtoList(userDeletedList);
            }
        }
        return null;
    }

    public boolean deleteUser(String token, int userId) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check user role: DEVELOPER or SCRUM_MASTER cannot delete users
            if (userRole != UserRole.DEVELOPER && userRole != UserRole.SCRUM_MASTER) {
                UserEntity u = userDao.findUserById(userId);
                if (u != null) {
                    u.setDeleted(true);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeUser(int id, String token) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check user role: DEVELOPER or SCRUM_MASTER cannot remove users
            if (userRole != UserRole.DEVELOPER && userRole != UserRole.SCRUM_MASTER) {
                UserEntity u = userDao.findUserById(id);
                if (u != null) {
                    for (TaskEntity task : u.getTasks()) {
                        task.setCreator(null);
                    }
                    userDao.remove(u);
                    return true;
                }
            }
        }
        return false;
    }

    public String getUsersCount(String token) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check user role: only PRODUCT_OWNER has access to the Dashboard
            if (userRole != UserRole.DEVELOPER && userRole != UserRole.SCRUM_MASTER) {
                int totalUsers = userDao.findTotalUserCount();
                int totalValidatedUsers = userDao.findTotalValidatedUsers();
                int totalNonValidatedUsers = userDao.findTotalNonValidatedUserCount();

                // Create a map to hold the information
                Map<String, Integer> userCounts = new HashMap<>();
                userCounts.put("totalUsers", totalUsers);
                userCounts.put("validatedUsers", totalValidatedUsers);
                userCounts.put("nonValidatedUsers", totalNonValidatedUsers);

                // Convert the map to JSON
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.writeValueAsString(userCounts);
                } catch (Exception e) {
                    // Handle the exception
                    e.printStackTrace();
                }
            }
        }
        // If user is not authenticated or authorized, return an empty JSON object
        return "{}";
    }

    public double getAverageTasksPerUser(String token) {
        UserEntity userEntity = userDao.findUserByToken(token);
        if (userEntity != null) {
            UserRole userRole = userEntity.getRole();
            // Check user role: PRODUCT_OWNER
            if (userRole == UserRole.PRODUCT_OWNER) {
                int totalUsers = userDao.findTotalValidatedUsers();
                int totalTasks = taskDao.countTotalTasks();
                if (totalUsers > 0) {
                    return (double) totalTasks / totalUsers;
                }
            }
        }
        return 0;
    }

    public List<DayCount> getRegisteredUsersCountForLastWeek() {
        List<DayCount> counts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate currentDate = today.minusDays(i);
            int userCount = userDao.findTotalUsersCountByDayRegisterAt(currentDate);
            counts.add(new DayCount(currentDate, userCount));
        }
        return counts;
    }

    private UserEntity convertUserDtotoEntity(UserDto user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        // Encrypt the password before storing
        String encryptedPassword = passEncoder.encode(user.getPassword());
        userEntity.setPassword(encryptedPassword);
        userEntity.setEmail(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone());
        if (user.getPhoto() == null || user.getPhoto().isEmpty()) {
            userEntity.setPhoto("https://cdn.pixabay.com/photo/2015/03/04/22/35/avatar-659651_640.png");
        } else {
            userEntity.setPhoto(user.getPhoto());
        }
        userEntity.setDeleted(false);
        userEntity.setValidated(false);
        return userEntity;
    }

    private UserDto convertUserEntitytoUserDto(UserEntity user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhone(user.getPhone());
        userDto.setPhoto(user.getPhoto());
        userDto.setDeleted(user.isDeleted());
        userDto.setValidated(user.isValidated());
        userDto.setRole(user.getRole());
        return userDto;
    }

    private ArrayList<UserDto> convertUsersFromEntityListToUserDtoList(ArrayList<UserEntity> userEntityEntities) {
        ArrayList<UserDto> userDtos = new ArrayList<>();
        for (UserEntity u : userEntityEntities) {
            userDtos.add(convertUserEntitytoUserDto(u));
        }
        return userDtos;
    }

    private LoginDto convertUserEntitytoLoginDto(UserEntity user) {
        LoginDto loginDto = new LoginDto();
        loginDto.setId(user.getId());
        loginDto.setUsername(user.getUsername());
        loginDto.setRole(user.getRole());
        loginDto.setPhoto(user.getPhoto());
        return loginDto;
    }

    private ArrayList<LoginDto> convertUsersFromEntityListToLoginDtoList
            (ArrayList<UserEntity> userEntityEntities) {
        ArrayList<LoginDto> loginDtos = new ArrayList<>();
        for (UserEntity u : userEntityEntities) {
            loginDtos.add(convertUserEntitytoLoginDto(u));
        }
        return loginDtos;
    }

    private ArrayList<RoleDto> convertUsersFromEntityListToRoleDtoList
            (ArrayList<UserEntity> userEntityEntities) {
        ArrayList<RoleDto> roleDtos = new ArrayList<>();
        for (UserEntity u : userEntityEntities) {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(u.getId());
            roleDto.setUsername(u.getUsername());
            roleDto.setRole(u.getRole());
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }

    private ArrayList<UserManagementDto> convertUsersFromEntityToUserManagmentDtoList
            (ArrayList<UserEntity> userEntityEntities) {
        ArrayList<UserManagementDto> usersDtos = new ArrayList<>();
        for (UserEntity u : userEntityEntities) {
            UserManagementDto userDto = new UserManagementDto();
            userDto.setId(u.getId());
            userDto.setUsername(u.getUsername());
            userDto.setRole(u.getRole());
            userDto.setEmail(u.getEmail());
            usersDtos.add(userDto);
        }
        return usersDtos;
    }

}