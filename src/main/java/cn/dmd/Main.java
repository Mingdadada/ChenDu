package cn.dmd;

/**
 * Created by 杜明达 on 2017/3/27.
 */
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.util.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Path("/processEngine")
public class Main {
    @GET
    @Produces("text/plain")
    @Consumes("application/json")
    @Path("/create")
    public String createProcessEngine() {
        ProcessEngine processEngine = SingleProcessEngine.getSingleProcessEngine();
        String returnString = "";
        returnString = returnString + "processName[" + processEngine.getName() + "] ProcessVersion[" + processEngine.VERSION + "]";
        return "流程引擎创建成功 " + returnString;
    }

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/jsonTest")
    public String test() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "dumingda");
        map.put("age", "23");
        map.put("address", "Beijing");
        JSONObject json = new JSONObject(map);
        return json.toString();
    }

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/add")
    public String add1() {
        ProcessEngine processEngine = SingleProcessEngine.getSingleProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        String[] users = new String[] {"dumingda", "chenrao"};
        String[] groups = new String[] {"user"};
        for(String user : users) {
            for(String group : groups) {
                identityService.createMembership(user, group);
            }
        }
        return "0";
    }
}
