package paj.project5_vc.service;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import paj.project5_vc.bean.TaskBean;
import paj.project5_vc.bean.UserBean;
import paj.project5_vc.dto.TaskDto;

import java.util.ArrayList;

@Path("/dashboard")
public class DashboardService {

    private static final long serialVersionUID = 1L;

    @EJB
    TaskBean taskBean;
    @Inject
    UserBean userBean;


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


}
