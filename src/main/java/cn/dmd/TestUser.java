package cn.dmd;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.*;

/**
 * Created by 杜明达 on 2017/4/1.
 */
class dmd implements Serializable{
    private String name;

    public byte getB() {
        return b;
    }

    public void setB(byte b) {
        this.b = b;
    }

    private byte b;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    private int a;

    private int c;
    public void setC(int _c){
        c = _c;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@Path("/testUser")
public class TestUser {
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("application/json")
    @Path("/login")
    public Response userLogin(@FormParam("userId") String userId, @FormParam("password") String password,
                              @CookieParam("userId") String cookieUserId,
                              @CookieParam("token") String token) {
        System.out.println(token);
        System.out.println(cookieUserId);
        ProcessEngine processEngine = SingleProcessEngine.getSingleProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        //get the exist token from database, so we can check the token
        String temp = identityService.getUserInfo(userId, "token");
        System.out.println(temp);
        try {
            if(identityService.checkPassword(userId, password)) {
                ;
            }
        }
        catch(Exception e) {
            ;
        }

        dmd d = new dmd();
        d.setName("dn");
        d.setB((byte)12);
        d.setA(4);
        d.setC(3);

        //set cookie and return to client
        Response.ResponseBuilder responseBuilder = Response.ok()
                .entity(d)
                .cookie(NewCookie.valueOf("token="+"11111111111111111"))
                .cookie(NewCookie.valueOf("userId="+userId));
        return responseBuilder.build();
    }
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("applicaiton/json")
    @Path("/setToken")
    public int setToken(@CookieParam("token") String token,
                             @CookieParam("userId") String userId) {
        //add user token to database
        ProcessEngine processEngine = SingleProcessEngine.getSingleProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        identityService.setUserInfo(userId, "token", token);
        return 0;
    }
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("applicaiton/json")
    @Path("logout")
    public Response userLogout(@CookieParam("userId") String userId,
                               @CookieParam("token") String token) {
        System.out.println("token = " + token);
        System.out.println("userId = " + userId);

        ProcessEngine processEngine = SingleProcessEngine.getSingleProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        String temp = identityService.getUserInfo(userId, "token");
        if(temp.equals(token)) {
            System.out.println("equal");
        }
        else {
            System.out.println("not equal");
        }
        Response.ResponseBuilder response = Response.ok()
                .entity("0")
                .cookie(NewCookie.valueOf("token=null" + ""));
        return response.build();
    }
}
