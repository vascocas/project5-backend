package paj.project5_vc.service;

import jakarta.ejb.EJB;
import paj.project5_vc.bean.UserBean;
import paj.project5_vc.dto.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import paj.project5_vc.enums.UserRole;
import paj.project5_vc.websocket.DashWeb;


import java.util.ArrayList;

@Path("/users")
public class UserService {

    @EJB
    UserBean userBean;

    @EJB
    DashWeb dashWeb;

    // Get token timer(Session Timeout)
    @GET
    @Path("/tokenTimer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokenTimerValue(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        TokenDto tokenDto = userBean.getTokenTimer();
        return Response.status(200).entity(tokenDto).build();
    }

    // Get UserDto by Id
    @GET
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEditProfile(@HeaderParam("token") String token, @QueryParam("userId") int userId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        UserDto user = userBean.getProfile(token, userId);
        return Response.status(200).entity(user).build();

    }

    // Get list of usernames (Role dto)
    @GET
    @Path("/usernames")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsernameList(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        ArrayList<RoleDto> usernames = userBean.getAllUsernames();
        return Response.status(200).entity(usernames).build();
    }

    // Get list of active users (UserManagement dto)
    @GET
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@HeaderParam("token") String token, @QueryParam("role") UserRole role,
                                @QueryParam("order") String order,
                                @QueryParam("page") int page,
                                @QueryParam("pageSize") int pageSize) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        // Define default values if page number and page size <= 0
        if (page <= 0) {
            page = 1;
        }
        if (pageSize <= 0) {
            pageSize = 5;
        }
        // Call the getUsers method in your bean with pagination parameters
        UserTableDto users = userBean.getUsers(token, role, order, page, pageSize);
        if (users != null) {
            return Response.status(200).entity(users).build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Get list of deleted users (UserManagement dto)
    @GET
    @Path("/deletedUsers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDeletedUsers(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        ArrayList<UserManagementDto> users = userBean.getDeletedUsers(token);
        if (users != null) {
            return Response.status(200).entity(users).build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Get logged user
    @GET
    @Path("/logged")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoggedProfile(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        UserDto user = userBean.getLoggedProfile(token);
        return Response.status(200).entity(user).build();
    }

    // Get public profile by username
    @GET
    @Path("/profile/username")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublicProfile(@HeaderParam("token") String token, @QueryParam("username") String username) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        PublicProfileDto user = userBean.getPublicProfile(username);
        return Response.status(200).entity(user).build();

    }

    // Get role of logged user
    @GET
    @Path("/role")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response roleByToken(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        RoleDto userRole = userBean.getRole(token);
        return Response.status(200).entity(userRole).build();
    }

    // Change token timer(Session Timeout)
    @PUT
    @Path("/tokenTimer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTokenTimer(@HeaderParam("token") String token, TokenDto tokenTimer) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        if (userBean.setTokenTimer(token, tokenTimer)) {
            return Response.status(200).entity("Token Timer updated!").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Makes Login (Return Login Dto)
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("username") String username, @HeaderParam("password") String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return Response.status(400).entity("Username and Password cannot be empty").build();
        }
        LoginDto login = userBean.login(username, password);
        if (login != null) {
            return Response.status(200).entity(login).build();
        } else return Response.status(403).entity("Wrong Username or Password! Please try again.").build();
    }

    // Register new user
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(UserDto user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return Response.status(400).entity("Username cannot be empty").build();
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return Response.status(400).entity("Email cannot be empty").build();
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return Response.status(400).entity("First name cannot be empty").build();
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return Response.status(400).entity("Last name cannot be empty").build();
        }
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            return Response.status(400).entity("Phone cannot be empty").build();
        }
        // Proceed with registering the user
        if (userBean.register(user)) {
            dashWeb.send("DashboardUserUpdate");
            return Response.status(200).entity("Registration Successful! Please verify your email.").build();
        } else {
            return Response.status(401).entity("Verify all fields. Username and Email must be unique").build();
        }
    }

    // Validates a user
    @PUT
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmEmail(@QueryParam("validationToken") String token) {
        if (userBean.validateUser(token)) {
            dashWeb.send("DashboardUserUpdate");
            return Response.status(200).entity("User validated successfully!").build();
        } else {
            return Response.status(404).entity("User not found or validation failed!").build();
        }
    }

    // Send reset password email
    @PUT
    @Path("/reset/email")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response recoveryPasswordEmail(RoleDto user) {
        if (userBean.emailRecoveryPassword(user)) {
            return Response.status(200).entity("Email sent successfully!").build();
        } else {
            return Response.status(404).entity("Sending email failed!").build();
        }
    }

    // Reset a password for forgotten password
    @PUT
    @Path("/reset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(@QueryParam("resetToken") String resetToken, PasswordDto passwordDto) {
        if (userBean.resetForgottenPassword(resetToken, passwordDto)) {
            return Response.status(200).entity("Password updated successfully!").build();
        } else {
            return Response.status(404).entity("Password update failed!").build();
        }
    }

    // Makes Logout
    @PUT
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(@HeaderParam("token") String token) {
        if (userBean.logout(token)) {
            return Response.status(200).entity("Logout Successful!").build();
        } else {
            return Response.status(401).entity("Invalid Token!").build();
        }
    }

    // Edit user profile
    @PUT
    @Path("/profile")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editProfile(@HeaderParam("token") String token, UserDto user) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return Response.status(401).entity("Email cannot be empty").build();
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return Response.status(401).entity("First name cannot be empty").build();
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return Response.status(401).entity("Last name cannot be empty").build();
        }
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            return Response.status(401).entity("Phone cannot be empty").build();
        }
        if (userBean.editProfile(user, token)) {
            return Response.status(200).entity("Profile updated!").build();
        } else {
            return Response.status(403).entity("Unauthorized or invalid parameters").build();
        }
    }

    // Edit different user profile
    @PUT
    @Path("/othersProfile")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editOthersProfile(@HeaderParam("token") String token, UserDto user) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return Response.status(401).entity("Email cannot be empty").build();
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return Response.status(401).entity("First name cannot be empty").build();
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return Response.status(401).entity("Last name cannot be empty").build();
        }
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            return Response.status(401).entity("Phone cannot be empty").build();
        }
        if (userBean.editUsersProfile(user, token)) {
            return Response.status(200).entity("Profile updated!").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Create user password
    @PUT
    @Path("/create/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response firstPassword(PasswordDto newPassword) {
        if (newPassword.getNewPass() == null || newPassword.getNewPass().isEmpty()) {
            return Response.status(401).entity("Password cannot be empty").build();
        }
        if (newPassword.getConfirmPass() == null || newPassword.getConfirmPass().isEmpty()) {
            return Response.status(401).entity("Password cannot be empty").build();
        }
        if (userBean.validateNewPassword(newPassword)) {
            return Response.status(200).entity("New password created!").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Edit user password
    @PUT
    @Path("/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editPassword(@HeaderParam("token") String token, PasswordDto newPassword) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        if (newPassword.getNewPass() == null || newPassword.getNewPass().isEmpty()) {
            return Response.status(401).entity("Password cannot be empty").build();
        }
        if (newPassword.getConfirmPass() == null || newPassword.getConfirmPass().isEmpty()) {
            return Response.status(401).entity("Password cannot be empty").build();
        }
        if (userBean.editUserPassword(token, newPassword)) {
            return Response.status(200).entity("Password updated!").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Creates a new user
    @POST
    @Path("/createUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@HeaderParam("token") String token, UserDto user) {
        // Validate the UserDto inputs
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return Response.status(401).entity("Username cannot be empty").build();
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return Response.status(401).entity("Email cannot be empty").build();
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return Response.status(401).entity("First name cannot be empty").build();
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return Response.status(401).entity("Last name cannot be empty").build();
        }
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            return Response.status(401).entity("Phone cannot be empty").build();
        }
        if (user.getRole() == null) {
            return Response.status(401).entity("Role cannot be empty").build();
        }
        // Proceed with registering the user
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        UserDto newUser = userBean.createUser(token, user);
        if (newUser != null) {
            return Response.status(200).entity(newUser).build();
        } else return Response.status(403).entity("Unauthorized").build();
    }

    // Delete user by username (Recycle bin)
    @PUT
    @Path("/remove/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@HeaderParam("token") String token, @PathParam("userId") int userId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        if (userBean.deleteUser(token, userId)) {
            dashWeb.send("DashboardUserUpdate");
            return Response.status(200).entity("Profile deleted").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Edit user role
    @PUT
    @Path("/role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRole(RoleDto user, @HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        if (userBean.updateRole(user, token)) {
            return Response.status(200).entity("Role updated").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Remove user (Permanently)
    @DELETE
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeUser(@HeaderParam("token") String token, @PathParam("userId") int userId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid Token!").build();
        }
        if (userBean.removeUser(userId, token)) {
            return Response.status(200).entity("Profile removed").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }
}