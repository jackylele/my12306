package cn.com.test.my12306.my12306.controller;

import cn.com.test.my12306.my12306.core.ClientTicket;
import cn.com.test.my12306.my12306.core.CommonUtil;
import cn.com.test.my12306.my12306.core.util.mail.MailUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class Config {

    @Autowired
    ClientTicket ct;

    private Thread t;
    public void test() throws Exception{
        /*if(t.isAlive()){
            t.stop();
        }*/
        ct.run();
    }

    @RequestMapping("/getConfig")
    @ResponseBody
    public String getConfig(){
        JsonObject json = new JsonObject();
        Gson gson = new Gson();
        Map map = new HashMap();
        map.put("queryNum",CommonUtil.queryNum);
        map.put("user",CommonUtil.user);
        map.put("seats",CommonUtil.seats);
        map.put("trains",CommonUtil.trains);
        map.put("date",CommonUtil.date);
        map.put("from",CommonUtil.from);
        map.put("to",CommonUtil.to);
        map.put("userName",CommonUtil.userName);
        map.put("userPwd",CommonUtil.userPwd);

        map.put("emailHost", MailUtils.emailHost);
        map.put("emailUsername",MailUtils.emailUsername);
        map.put("emailPassword",MailUtils.emailPassword);
        map.put("emailSender",MailUtils.emailSender);
        map.put("emailReceiver",MailUtils.emailReceiver);
        return gson.toJson(map);
    }

    @RequestMapping("/config")
    @ResponseBody
    public String config(String user,String date,String emailHost,String emailPassword,String emailReceiver,String emailSender,
                         String emailUsername,String from,String queryNum,String seats,String to,String trains,
                         String userName,String userPwd) throws Exception{

       CommonUtil.queryNum=Integer.valueOf(queryNum);
       CommonUtil.user=user;
        CommonUtil.seats=seats;
        CommonUtil.trains=trains;
        CommonUtil.date=date;
       CommonUtil.from=from;
      CommonUtil.to=to;
        CommonUtil.userName=userName;
        CommonUtil.userPwd=userPwd;

         MailUtils.emailHost=emailHost;
        MailUtils.emailUsername=emailUsername;
        MailUtils.emailPassword=emailPassword;
        MailUtils.emailSender=emailSender;
        MailUtils.emailReceiver=emailReceiver;

        test();
        return "刷新成功";
    }
}
