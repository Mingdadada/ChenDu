package cn.dmd;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 杜明达 on 2017/4/4.
 */
@Path("/user")
public class User {
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json")
    @Path("/login")
    public Response userLogin(@FormParam("userId") String userId,
                              @FormParam("password") String password) {
        ProcessEngine processEngine = SingleProcessEngine.getSingleProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        Map<String, Object> result = new HashMap<String, Object>();
        //check whether userId is exist
        if(identityService.createUserQuery().userId(userId).count() == 0) {
            result.put("status", -1);
            result.put("comments", "user not exist");
            Response.ResponseBuilder responseBuilder = Response.ok().entity(result);
            return responseBuilder.build();
        }
        else {
            if(identityService.checkPassword(userId, password)) {
                try {
                    result.put("status", 1);
                    result.put("comments", "login successful");
                    String token = SingleProcessEngine.generateToken(userId, password);
                    Response.ResponseBuilder responseBuilder = Response.ok().entity(result)
                            .cookie(NewCookie.valueOf("token=" + token));
                    return responseBuilder.build();
                } catch(Exception e) {
                    result.put("status", -2);
                    result.put("comments", e.getMessage());
                    Response.ResponseBuilder responseBuilder = Response.ok().entity(result);
                    return responseBuilder.build();
                }
            }
            else {
                result.put("status", -3);
                result.put("comments", "password illegal");
                Response.ResponseBuilder responseBuilder = Response.ok().entity(result);
                return responseBuilder.build();
            }
        }
    }

    @GET
    public Response getUserMessage(@CookieParam("token") String token) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 1);
        result.put("comments", "密码错误");
        Response.ResponseBuilder responseBuilder = Response.ok().entity(result);
        return responseBuilder.build();
    }
}
