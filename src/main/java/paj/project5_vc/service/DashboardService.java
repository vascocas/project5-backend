package paj.project5_vc.service;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import paj.project5_vc.bean.TaskBean;
import paj.project5_vc.bean.UserBean;
import paj.project5_vc.dto.DayCount;
import paj.project5_vc.dto.TasksSummary;

import java.util.*;

@Path("/dashboard")
public class DashboardService {

    @EJB
    TaskBean taskBean;
    @EJB
    UserBean userBean;


    // Count of total number of users, validated users, and unvalidated users
    @GET
    @Path("users/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersCount(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        String userCounts = userBean.getUsersCount(token);
        return Response.status(200).entity(userCounts).build();
    }

    //Count of tasks per state
    @GET
    @Path("tasks/state")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasksStateCount(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        List<TasksSummary> stateTasks = taskBean.countTasksByStatus(token);
        return Response.status(200).entity(stateTasks).build();
    }

    //List of categories ordered by frequency
    @GET
    @Path("categories/frequency")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoriesFrequency(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        List<TasksSummary> categoryTasks = taskBean.getCategoryTasksBySum(token);
        return Response.status(200).entity(categoryTasks).build();
    }

    //Average number of tasks per user
    @GET
    @Path("average/tasks/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAverageTasksPerUser(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        double averageTasksPerUser = userBean.getAverageTasksPerUser(token);
        // Round the averageTasksPerUser to two decimal places
        double resultTasksPerUser = Math.round(averageTasksPerUser * 100.0) / 100.0;
        return Response.status(200).entity(resultTasksPerUser).build();
    }

    // Endpoint to get the average task duration
    @GET
    @Path("tasks/average/duration")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAverageTaskDuration(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        double averageDuration = taskBean.getAverageTaskDuration();
        // Round the averageTaskDuration to two decimal places
        double resultAverageDuration = Math.round(averageDuration * 100.0) / 100.0;
        return Response.status(200).entity(resultAverageDuration).build();
    }

    // Get the count of validated users for the last week
    @GET
    @Path("users/weekly/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRegisteredUsersCountWeekly(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        List<DayCount> validatedUsersCountForLastWeek = userBean.getRegisteredUsersCountForLastWeek();
        return Response.status(200).entity(validatedUsersCountForLastWeek).build();
    }

    // Get the cumulative count of completed tasks for the last week
    @GET
    @Path("tasks/completed/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompletedTasksCountWeekly(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        List<DayCount> result = taskBean.getCompletedTasksCumulative();
        return Response.status(200).entity(result).build();
    }

}
