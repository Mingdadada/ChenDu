package cn.dmd;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.util.IoUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by 杜明达 on 2017/3/30.
 */
public class SingleProcessEngine {
    private static final ProcessEngine SINGLE_PROCESSENGINE = initProcessEngine();
    private SingleProcessEngine() {

    }
    private static ProcessEngine initProcessEngine() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        return processEngine;
    }
    public static ProcessEngine getSingleProcessEngine() {
        //init groups and users in database
        initGroups();
        initUsers();
        return SINGLE_PROCESSENGINE;
    }

    private static void initGroups() {
        String[] userGroups = new String[] {"user"};
        for(String groupId : userGroups) {
            createGroup(groupId, "user");
        }
        String[] adminGroups = new String[] {"admin"};
        for(String groupId : adminGroups) {
            createGroup(groupId, "admin");
        }
    }
    private static void createGroup(String groupId, String type) {
        IdentityService identityService = SINGLE_PROCESSENGINE.getIdentityService();
        if(identityService.createGroupQuery().groupId(groupId).count() == 0) {
            Group newGroup = identityService.newGroup(groupId);
            newGroup.setName(groupId.substring(0, 1).toUpperCase() + groupId.substring(1));
            newGroup.setType(type);
            identityService.saveGroup(newGroup);
        }
    }

    private static void initUsers() {
        createUser("dumingda", "Mingda", "Du", "dumingda", null, null, Arrays.asList("admin"), null);
        createUser("chenrao", "Rao", "Chen", "chenrao", null, null, Arrays.asList("admin"), null);

        createUser("jiaohuan", "jiao", "huan", "jiaohuan", null, null, Arrays.asList("user"), null);

    }
    private static void createUser(String userId, String firstName, String lastName, String password, String email, String imageResource, List<String> groups, List<String> userInfo) {
        IdentityService identityService = SINGLE_PROCESSENGINE.getIdentityService();
        if(identityService.createUserQuery().userId(userId).count() == 0) {
            User newUser = identityService.newUser(userId);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setPassword(password);
            if(email != null) {
                newUser.setEmail(email);
            }
            identityService.saveUser(newUser);

            if(groups != null) {
                for(String group : groups) {
                    identityService.createMembership(userId, group);
                }
            }

            if(imageResource != null) {
                byte[] pictureBytes = IoUtil.readInputStream(new Object().getClass().getClassLoader().getResourceAsStream(imageResource), null);
                Picture picture = new Picture(pictureBytes, "image/jpeg");
                identityService.setUserPicture(userId, picture);
            }

            if(userInfo != null) {
                for(int i = 0; i < userInfo.size(); i+=2) {
                    identityService.setUserInfo(userId, userInfo.get(i), userInfo.get(i+1));
                }
            }
        }
    }

    public static String getMD5(String string) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch(Exception e) {
            throw new Exception("MD5 encoding failed");
        }
    }
    public static String generateToken(String userId, String password) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        String string = "" + userId + password + date;
        try {
            return getMD5(string);
        }catch(Exception e) {
            throw e;
        }
    }
}
