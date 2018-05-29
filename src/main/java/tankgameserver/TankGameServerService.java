package tankgameserver;

import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/rest")
public class TankGameServerService {
    @Context
    ServletContext servletContext;

    @GET
    @Path("/needhost")
    public Response needHost() {
        Gson gson = new Gson();
        String output;
        try {
            if (this.servletContext.getAttribute("hashost").equals(true)) {
                output = gson.toJson(false);
            } else {
                output = gson.toJson(true);
            }
        } catch (Exception e){
            output = gson.toJson(true);
            this.servletContext.setAttribute("hashost", false);
        }


        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/sethost")
    public Response setHost() {
        this.servletContext.setAttribute("hashost", true);
        Gson gson = new Gson();
        String output = gson.toJson("Successfully set host");
        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/unhost")
    public Response unHost() {
        this.servletContext.setAttribute("hashost", false);
        Gson gson = new Gson();
        String output = gson.toJson("Successfully removed host");
        return Response.status(200).entity(output).build();
    }
}