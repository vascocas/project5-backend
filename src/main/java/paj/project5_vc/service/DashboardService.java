package paj.project5_vc.service;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import paj.project5_vc.bean.TaskBean;
import paj.project5_vc.bean.UserBean;
import paj.project5_vc.dto.CategoryTasksSummary;
import paj.project5_vc.dto.TaskDto;

import java.util.ArrayList;
import java.util.List;

@Path("/dashboard")
public class DashboardService {

    private static final long serialVersionUID = 1L;

    @EJB
    TaskBean taskBean;
    @EJB
    UserBean userBean;


    // Count of total number of users, validated users, and unvalidated users
    @GET
    @Path("usersCount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersCount(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        String userCountsJson = userBean.getUsersCount(token);
        if (userCountsJson.isEmpty()) {
            // Handle the case where the user is not authenticated or authorized
            return Response.status(403).entity("User not authorized").build();
        }
        return Response.status(200).entity(userCountsJson).build();
    }


    //Count of tasks per state: This could also be retrieved in a single request since it's related to task data.

    // EM CONSTRUÇÃO
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasksStateCount(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        return Response.status(200).entity(userBean.getUsersCount(token)).build();
    }

    //List of categories ordered by frequency
    @GET
    @Path("categoriesSum")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoriesFrequency(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        List<CategoryTasksSummary> categoryTasks = taskBean.getCategoryTasksBySum(token);
        if (categoryTasks.isEmpty()) {
            // Handle the case where the user is not authorized
            return Response.status(403).entity("User not authorized").build();
        }
        return Response.status(200).entity(categoryTasks).build();
    }



    //Average number of tasks per user: This could be part of the user-related data request.

    // Endpoint to get Average Tasks Per User
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAverageTasksPerUser(@HeaderParam("token") String token) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        double averageTasksPerUser = userBean.getAverageTasksPerUser(token);
        return Response.status(200).entity(averageTasksPerUser).build();
    }

    //
    //Graph showing the number of registered users over time: This data might need to be retrieved separately since it involves a time series.
    //
    //Cumulative graph showing the total number of tasks completed over time: Similar to the previous point, this might need to be fetched separately due to its time-based nature.ila

}
