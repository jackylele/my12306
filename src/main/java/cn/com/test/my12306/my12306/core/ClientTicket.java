/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package cn.com.test.my12306.my12306.core;

import cn.com.test.my12306.my12306.core.util.DateUtil;
import cn.com.test.my12306.my12306.core.util.FileUtil;
import cn.com.test.my12306.my12306.core.util.ImageUtil;
import cn.com.test.my12306.my12306.core.util.JsonBinder;
import cn.com.test.my12306.my12306.core.util.mail.MailUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A example that demonstrates how HttpClient APIs can be used to perform
 * form-based logon.
 */
@Component
public class ClientTicket /*implements ApplicationRunner*/{
    @Autowired
    CommonUtil commonUtil ;
    @Autowired
    ClientTicket ct;

    Thread t;


    @Autowired
    MailUtils mailUtils ;
    private static Logger logger = LogManager.getLogger(ClientTicket.class);
    private String rancode = "";
    BasicCookieStore cookieStore = null;
    CloseableHttpClient httpclient = null;
    public JsonBinder jsonBinder = JsonBinder.buildNonNullBinder(false);
    private String RAIL_DEVICEID ="";
    private String RAIL_EXPIRATION = "";


    private Map<String,Integer> trainSeatMap = new ConcurrentHashMap<String,Integer>();
    private Map<String,Long> trainSeatTimeMap = new ConcurrentHashMap<String,Long>();


    private String hosts="kyfw.12306.cn";
    private String leftTicketUrl = "leftTicket/query";
    ScheduledExecutorService es = null;
    //是否可以运行查票
    boolean canRun;

    public BlockingQueue<Map<String,String>> queue = new LinkedBlockingQueue<Map<String, String>>(10);

    public ClientTicket(BasicCookieStore cookieStore, CloseableHttpClient httpclient) {
        this.cookieStore = cookieStore;
        this.httpclient = httpclient;
        this.hosts = commonUtil.getIp();
        rancode = "";
    }

    public ClientTicket(BasicCookieStore cookieStore) {
        this.cookieStore = cookieStore;
        this.hosts = commonUtil.getIp();
        rancode = "";
    }

    public ClientTicket() {
        cookieStore = new BasicCookieStore();
        httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        rancode = "";
    }

    public CloseableHttpClient getHttpclient() {
        return this.httpclient;
    }

    public void setHttpclient(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;

    }

    public Map<String, Integer> getTrainSeatMap() {
        return trainSeatMap;
    }

    public void setTrainSeatMap(Map<String, Integer> trainSeatMap) {
        this.trainSeatMap = trainSeatMap;
    }

    public Map<String, Long> getTrainSeatTimeMap() {
        return trainSeatTimeMap;
    }

    public void setTrainSeatTimeMap(Map<String, Long> trainSeatTimeMap) {
        this.trainSeatTimeMap = trainSeatTimeMap;
    }

    public String getLeftTicketUrl() {
        return leftTicketUrl;
    }

    public void setLeftTicketUrl(String leftTicketUrl) {
        this.leftTicketUrl = leftTicketUrl;
    }

    public static void getAllHeaders(CloseableHttpResponse response){
        Header[] headers = response.getAllHeaders();
        System.out.println("开始获取header");
        for(Header h:headers){
            System.out.println(h.getName()+":"+h.getValue());
        }
        System.out.println("结束获取header");
    }

    public  void getAllCookies(BasicCookieStore cookieStore){
        System.out.println("开始获取cookie");
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
        System.out.println("结束获取cookie");
    }

    /**
     * 将cookie写入到文件
     */
    public  void writeCookies2File(){
        List<Cookie> cookies = cookieStore.getCookies();
        String c="";
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
                Cookie ck= cookies.get(i);
               c+=ck.getName()+","+ck.getValue()+","+ck.getDomain()+","+ck.getPath()+";";
            }
            if(c.endsWith(";")) c=c.substring(0,c.length()-1);
        FileUtil.saveAs(c,commonUtil.sessionPath+commonUtil.getUserName()+"_12306Session.txt");
        System.out.println("结束获取cookie");
    }

    public  void resetCookiesFile(){
        FileUtil.saveAs("",commonUtil.sessionPath+commonUtil.getUserName()+"_12306Session.txt");
    }

    /**
     * 将cookie读取到cookiestore 查看是否可以用
     */
    public void readFile2Cookie(){
        String context = FileUtil.readByLines(commonUtil.getSessionPath()+commonUtil.getUserName()+"_12306Session.txt");

        if(null!=context && !"".equals(context)){
            String[] aCookie = context.split(";");
            for(String ck:aCookie){
                String[] bCookie = ck.split(",");
                BasicClientCookie acookie = new BasicClientCookie(bCookie[0], bCookie[1]);
                acookie.setDomain(bCookie[2]);
                acookie.setPath(bCookie[3]);
                cookieStore.addCookie(acookie);

            }
        }

    }


    /**
     *
     * @param headers
     */
    public void shuapiao(Header[] headers){
        if(null==headers || headers.length==0){
            headers = new Header[3];
            headers[0] = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            headers[1] = new BasicHeader("Host","kyfw.12306.cn");
        }
        headers = new BasicHeader[7];
        headers[0] = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        headers[1] = new BasicHeader("Host","kyfw.12306.cn");
        headers[2] = new BasicHeader("Referer","https://kyfw.12306.cn/otn/login/init");
        headers[3] = new BasicHeader("Accept","*/*");
        headers[4] = new BasicHeader("Accept-Encoding","gzip, deflate");
        headers[5] = new BasicHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
        headers[6] = new BasicHeader("Content-Type","application/x-www-form-urlencoded");
        while(true) {
            try {
                headers[2] = new BasicHeader("Referer", "https://kyfw.12306.cn/otn/leftTicket/init");
                //https://kyfw.12306.cn/otn/leftTicket/queryZ?leftTicketDTO.train_date=2018-02-12&leftTicketDTO.from_station=SJP&leftTicketDTO.to_station=BJP&purpose_codes=ADULT

                HttpUriRequest shuapiao = RequestBuilder.get()
                        .setUri("https://" + hosts + "/otn/"+leftTicketUrl+"?leftTicketDTO.train_date=2018-02-25&leftTicketDTO.from_station=SJP&leftTicketDTO.to_station=BJP&purpose_codes=ADULT")
                        .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2]).addHeader(headers[3]).addHeader(headers[4]).addHeader(headers[5]).addHeader(headers[6])
                        .build();
                CloseableHttpResponse response = httpclient.execute(shuapiao);
                getAllCookies(this.cookieStore);
                HttpEntity entity = response.getEntity();
                Map<String, Object> rsmap = null;
                rsmap = this.jsonBinder.fromJson(EntityUtils.toString(entity), Map.class);
//                System.out.println(rsmap.size() + " " + rsmap);
                String status = rsmap.get("httpstatus") + "";
                if (status.equalsIgnoreCase("200")) {
                    Map data = (Map) rsmap.get("data");
                    if (data.size() > 0) {
                        List<String> arr = (List<String>) data.get("result");
                        System.out.println(arr.size() + "a " + arr.get(0));
                        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
                        commonUtil.jiexi(arr, map);
                        List< Map<String,String>> youpiao= commonUtil.getSecretStr(map, commonUtil.getTrains(), commonUtil.getSeats());
                        if(youpiao.size()>0){
                            for(Map<String,String> map1:youpiao){
                                queue.put(map1);
                            }
                         /*  Map<Integer,String> mapQueue=new HashMap<Integer,String>();
                           mapQueue.put(0,secretStr.split(",")[0]);//secret
                           mapQueue.put(1,secretStr.split(",")[1]);//席别
                           mapQueue.put(2,secretStr.split(",")[2]);//车次
                           System.out.println();
                           queue.put(mapQueue);*/
                        }

                    }
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                System.out.println("刷票时出错");
                e.printStackTrace();
            }

        }
    }

    /**
     * 多线程刷票
     * @param es1
     */
    public void xianchengshuapiao(ScheduledExecutorService es1){
        for (int i = 0; i < CommonUtil.queryNum; i++) {
            // 刷票线程
            System.out.println((i+1)+"线程刷票");
            Thread t = new Thread(new TicketQuery(queue,ct),"查询余票线程");
            try{
                es1.schedule(t ,1500,TimeUnit.MILLISECONDS);
            }catch (Exception e){}
        }
    }

    public void shutdownqueryThread(){
        ct.canRun =false;
        es.shutdownNow();
    }

    /**
     * 预定失败时 重新刷票启动
     * @param headers
     */
    public void reshua( Header[] headers){

        this.xianchengshuapiao(es);

        while(this.queue.size()==0){
            try{
            Thread.sleep(200);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("有票了 开始预订");
        es.shutdownNow();
        //订票线程？主程序订票
        new Thread(new TicketBook(ct,queue,httpclient,headers,cookieStore)).start();

    }
    public void shutdownqueryThread(ScheduledExecutorService es){
        try{
            es.shutdownNow();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * 获取验证码弹窗
     * @param url
     * @return
     * @throws IOException
     */
    public String getCode(String url,Header[] headers) throws IOException {
        //JFrame frame = new JFrame("验证码");
        this.rancode="";
        String codeName = System.currentTimeMillis()+".jpg";
        String imagePath = "";
        String imageBase64="";
        while(StringUtils.isEmpty(imagePath)){
            Map<String,String> rsMap = getCodeByte(url, headers,codeName);
            imagePath = rsMap.get("imagePath");
            imageBase64 = rsMap.get("imageBase64");
        }
        if(CommonUtil.autoCode.equals("1")){
            String rs=ImageUtil.shibie360(imageBase64);
//            String rsCode = ImageUtil.getZuobiao(rs);
            this.rancode=rs;
        }else {
//        new TipTest("","","请输入验证码");
//            JLabel label = new JLabel(new ImageIcon(getCodeByte(url, headers,codeName)),
//                    JLabel.CENTER);
            JLabel label = new JLabel(new ImageIcon(imagePath),
                    JLabel.CENTER);

            label.setBounds(0, 0, 295, 220);
            label.setVerticalAlignment(SwingConstants.TOP);
            label.addMouseListener(new RecordListener());

            JOptionPane.showConfirmDialog(null, label,
                    "请输入验证码", JOptionPane.DEFAULT_OPTION);

        }
//        String fileName = System.currentTimeMillis()+".jpg";
//        ImageUtil.download("https://aa/passport/captcha/captcha-image?login_site=E&module=login&rand=sjrand&"+Math.random(),fileName,"");
        return this.rancode;
    }


    /**
     * 获取指定url的验证码图片字节信息
     * @param url
     * @return
     */

    public  Map<String,String> getCodeByte(String url,Header[] headers,String codeName) {
        HttpGet get = new HttpGet(url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream in=null;
        OutputStream out = null;
        byte[] bytse = null;
        Map<String,String> rsMap = new HashMap<String,String>();
        String fileName="";
            try {
                HttpGet hget = new HttpGet("https://"+hosts+"/passport/captcha/captcha-image64?login_site=E&module=login&rand=sjrand&" + Math.random());
               for(Header h:headers){
                   hget.addHeader(h);
               }
                CloseableHttpResponse response = httpclient.execute(hget);

                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity, "UTF-8");
                Map<String, Object> rsmap = null;
                rsmap = this.jsonBinder.fromJson(content, Map.class);
                String code = rsmap.get("result_code")+"";
                String image =  rsmap.get("image")+"";
                if(null!=image){
                    String savePath=CommonUtil.sessionPath+CommonUtil.codePath;
                    File sf=new File(savePath);
                    if(!sf.exists()){
                        sf.mkdirs();
                    }
                    ImageUtil.GenerateImage(image,savePath+File.separator+codeName);
                    fileName = savePath+File.separator+codeName;
                    rsMap.put("imagePath",fileName);
                    rsMap.put("imageBase64",image);
                }else{
                    fileName="";
                }

            }catch(Exception e ){
               e.printStackTrace();
            }
            return rsMap;

    }

    public void run() throws Exception {
        if(es != null && !es.isTerminated())
            ct.shutdownqueryThread();
        es = Executors.newScheduledThreadPool(CommonUtil.queryNum);
        canRun = false;


        while(true){
            //是否是正常的预定时间
            if(DateUtil.isNormalTime()){
                canRun =true;
                break;
            }else{
                logger.info("维护时间，暂停查询");
                Thread.currentThread().sleep(1000);
            }
        }
        Header[] headers = new BasicHeader[3];
        headers[0] = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        headers[1] = new BasicHeader("Host","kyfw.12306.cn");
        headers[2] = new BasicHeader("Referer","https://kyfw.12306.cn/otn/index/init");

        try {
            //初始化页面

            //登陆
            boolean loginFlag=true;
            while( loginFlag){
                if(StringUtils.isEmpty(CommonUtil.userName) || StringUtils.isEmpty(CommonUtil.userPwd) || StringUtils.isEmpty(CommonUtil.user)
                        || StringUtils.isEmpty(CommonUtil.from) || StringUtils.isEmpty(CommonUtil.to) || StringUtils.isEmpty(CommonUtil.seats)
                        || StringUtils.isEmpty(CommonUtil.queryNum) || StringUtils.isEmpty(CommonUtil.trains) ){
                    logger.info("参数不能为空");
                    Thread.sleep(1000);
                    continue;
                }
                loginFlag= !ct.login(headers);
                if(!loginFlag){
                    logger.info("登陆成功，继续执行下面代码");
                    break;
                }

            }

            //刷票
            // ct.shuapiao(headers,"","");
           /* new  Thread(new Runnable(){
                public void run(){
                    System.out.println("主线程刷票");
                    ct.shuapiao(headers);
                }
            }).start();*/

            ct.queryInit(headers);//设置查询地址 queryA queryZ
            ct.getAllCookies(ct.cookieStore);
            BasicClientCookie acookie = new BasicClientCookie("current_captcha_type", "Z");
            acookie.setDomain("kyfw.12306.cn");
            acookie.setPath("/");
            cookieStore.addCookie(acookie);
            ct.getAllCookies(ct.cookieStore);


            ct.xianchengshuapiao(es);
            if(t != null && t.isAlive()) t.stop();
            //订票线程？主程序订票

            t = new Thread(new TicketBook(ct,queue,ct.getHttpclient(),headers,cookieStore),"订票线程");

            t.start();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            httpclient.close();
        }
    }

    public static void main(String[] args) throws Exception{
        ScheduledExecutorService threadPoolExecutor = Executors.newScheduledThreadPool(2);
        LinkedBlockingQueue q = new LinkedBlockingQueue(11);
        threadPoolExecutor.schedule(new Thread(){},100,TimeUnit.MILLISECONDS);
        threadPoolExecutor.schedule(new Thread(){
        },100,TimeUnit.MILLISECONDS);
        new Thread(){
            @Override
            public void run(){
                try{
                    System.out.println(q.take());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

        Thread.sleep(60000);
        q.put("eee");

    }

    /**
     *	鼠标点击验证码，记录坐标
     *
     * @author xiaoQ
     * @since 2011-12-21
     * @version 1.0
     */
    class RecordListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            //refreshRandCode();
            int x=e.getX();
            int y=e.getY();
            rancode+=rancode.equals("")?x+","+(y-30):","+x+","+(y-30);
            System.out.println(x+","+y+"  rancode:"+rancode);
        }
    }

    public void resetRancode(){
        this.rancode = "";
    }

    public boolean login(Header[] headers) throws Exception{
        try {
            httpclient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .build();
            logger.info("init page");
            headers[2] = new BasicHeader("Referer","https://kyfw.12306.cn/otn/index/init");
            HttpUriRequest initPage1 = RequestBuilder.get()//.post()
                    .setUri(new URI("https://"+this.hosts+"/otn/login/init"))
                    .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2])
                    .build();
            CloseableHttpResponse response3 = httpclient.execute(initPage1);
            try {
                HttpEntity entity = response3.getEntity();

                EntityUtils.consume(entity);


        } finally {
            response3.close();
        }
            headers[2] = new BasicHeader("Referer","https://kyfw.12306.cn/otn/login/init");
            logger.info("page inited and start to readFile2Cookie ");
            //设置一遍cookie
            readFile2Cookie();
            logger.info("check online status");
            //验证登陆状态
            Map<String,Object> onlineMap = this.checkOnlineStatus(headers);
            if(null!=onlineMap && onlineMap.size()>0){
                if("0".equalsIgnoreCase(onlineMap.get("result_code")+"")){
                    logger.info("验证时候已经登陆");
                    return true;
                }else{
                    logger.info("验证时候未登录");
                }
            }
            //设置多余的cookie
//            ct.getDeviceCookie(headers);
            boolean checkedCode=false;
            while(!checkedCode) {

                //获取验证码
                String valicode = "";
                while(valicode.equals("")){
                    valicode = this.getCode("", headers);
                }

                System.out.println("验证码：" + valicode);

                logger.info("start check code");
                //校验验证码
                HttpUriRequest checkCode = RequestBuilder.post()
                        .setUri(new URI("https://" + this.hosts + "/passport/captcha/captcha-check"))
                        .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2])
                        .addParameter("answer", valicode)
                        .addParameter("login_site", "E")
                        .addParameter("rand", "sjrand")
                        .build();
                CloseableHttpResponse response = httpclient.execute(checkCode);
                this.getAllCookies(this.cookieStore);

                Map<String, String> rsmap = null;
                try {
                    HttpEntity entity = response.getEntity();
                    rsmap = this.jsonBinder.fromJson(EntityUtils.toString(entity), Map.class);
//                System.out.println("校验：" + response.getStatusLine().getStatusCode() + " " + entity.getContent() + " abc " + EntityUtils.toString(entity));
                    if(null==rsmap){
                        System.out.println("验证码校验没有通过111");
                    }else  if (rsmap.get("result_code").equalsIgnoreCase("4")) {
                        logger.info("验证码校验通过");
                        checkedCode=true;
                    } else {
                        logger.info("验证码校验没有通过");
                    }
                } catch (Exception e) {
                    logger.info("验证码校验没有通过");
                    e.printStackTrace();
                } finally {
                    response.close();
                }
            }


            //登陆
            Map<String,String> map = new HashMap<String,String>();

            try {
                logger.info("start login");
                Thread.sleep(400);
                headers = new BasicHeader[7];
                headers[0] = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                headers[1] = new BasicHeader("Host","kyfw.12306.cn");
                headers[2] = new BasicHeader("Referer","https://kyfw.12306.cn/otn/login/init");
                //headers[3] = new BasicHeader("Accept","application/json, text/javascript, */*; q=0.01");
                headers[3] = new BasicHeader("Accept","*/*");
                headers[4] = new BasicHeader("Accept-Encoding","gzip, deflate");
                headers[5] = new BasicHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
                headers[6] = new BasicHeader("Content-Type","application/x-www-form-urlencoded");
                HttpUriRequest login = RequestBuilder.post()
                        .setUri("https://"+this.hosts+"/passport/web/login")
                        //.setUri(new URI("https://kyfw.12306.cn/passport/web/login"))
                        .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2]).addHeader(headers[3]).addHeader(headers[4]).addHeader(headers[5]).addHeader(headers[6])
                        .addParameter("username", commonUtil.getUserName())
                        .addParameter("password", commonUtil.getUserPwd())
                        .addParameter("appid", "otn")
                        .build();
                login.addHeader("X-Requested-With","XMLHttpRequest");
                login.addHeader("Connection","keep-alive");
                CloseableHttpResponse response = httpclient.execute(login);
                Thread.sleep(400);

                HttpEntity entity = response.getEntity();
//                Map<String, String> rsmap = null;
                Map<String, String> rsmap = this.jsonBinder.fromJson(EntityUtils.toString(entity), Map.class);
//                this.getAllCookies(this.cookieStore);
                //            System.out.println("登陆：" + response.getStatusLine().getStatusCode() + " " + entity.getContent() + " abc " + EntityUtils.toString(entity));
                if(null!=rsmap && rsmap.size()>0){
                    String code = String.valueOf(rsmap.get("result_code"))+"";
                    if (code.equalsIgnoreCase("0")) {
                        logger.info("登陆成功");

                        HttpUriRequest userLogin = RequestBuilder.post()
                                .setUri("https://"+this.hosts+"/otn/login/userLogin")
                                .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2]).addHeader(headers[3]).addHeader(headers[4]).addHeader(headers[5]).addHeader(headers[6])
                                .addParameter("_json_att", "")
                                .build();
                        login.addHeader("X-Requested-With","XMLHttpRequest");
                        login.addHeader("Connection","keep-alive");
                        response = httpclient.execute(login);
                        int statusCode = response.getStatusLine().getStatusCode();
                        logger.info("再次登陆userLogin："+ response.getStatusLine().getStatusCode());
                        if(statusCode==302){
                            EntityUtils.consume(response.getEntity());
                            HttpUriRequest reload = RequestBuilder.get()//.post()
                                    .setUri("https://"+this.hosts+"/otn/passport?redirect=/otn/login/userLogin")
                                    .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2]).addHeader(headers[3]).addHeader(headers[4]).addHeader(headers[5]).addHeader(headers[6])
                                    .build();

                            response = httpclient.execute(reload);
                            statusCode = response.getStatusLine().getStatusCode();
                            System.out.println("跳转："+ response.getStatusLine().getStatusCode());
                            EntityUtils.consume(response.getEntity());//消费掉
                        }



                    } else {
                        System.out.println("登陆失败");
                        return false;
                    }
                }else {
                    System.out.println("登陆失败，发生了302，被禁了？");
                    return false;
                }

            }catch (Exception e){
                e.printStackTrace();
                System.out.println("登陆时发生错误");
                return false;
            }
            logger.info("check online status again");
            //再次校验登陆状态
            onlineMap = this.checkOnlineStatus(headers);
            String tk ="";
            if(null!=onlineMap && onlineMap.size()>0){
                if("0".equals(onlineMap.get("result_code")+"")){
                    logger.info("再次验证时候已经登陆");
                    tk =onlineMap.get("newapptk").toString();

                }else{
                    System.out.println("再次验证时候未登录,重新登录");
                    return false;
                }
            }


            logger.info("start check client and get tk");
            //校验客户端

            HttpUriRequest uamauthclient = RequestBuilder.post()
                    .setUri("https://"+this.hosts+"/otn/uamauthclient")
                    .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2]).addHeader(headers[3]).addHeader(headers[4]).addHeader(headers[5]).addHeader(headers[6])
                    .addParameter("tk", tk)
                    .build();
            uamauthclient.addHeader("X-Requested-With","XMLHttpRequest");
            uamauthclient.addHeader("Connection","keep-alive");
            CloseableHttpResponse response = httpclient.execute(uamauthclient);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode==200){
                HttpEntity entity = response.getEntity();


                String jsonStr= EntityUtils.toString(entity);
                System.out.println("entity " +jsonStr);
                map = this.jsonBinder.fromJson(jsonStr,Map.class);
                logger.info("校验通过：{}",map.get("username"));
                logger.info("cookie中是否有tk参数");
                this.getAllCookies(this.cookieStore);
                logger.info("tk cookie获取结束");
                HttpUriRequest userLogin = RequestBuilder.get()
                        .setUri("https://"+this.hosts+"/otn/login/userLogin")
                        .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2]).addHeader(headers[3]).addHeader(headers[4]).addHeader(headers[5]).addHeader(headers[6])
                        .build();
                userLogin.addHeader("X-Requested-With","XMLHttpRequest");
                userLogin.addHeader("Connection","keep-alive");
                response = httpclient.execute(userLogin);
                statusCode = response.getStatusLine().getStatusCode();
                logger.info("userLogin statusCode:{}",statusCode);
                EntityUtils.consume(response.getEntity());

                //跳转到 用户页面

                HttpUriRequest initMy12306 = RequestBuilder.get()
                        .setUri("https://"+this.hosts+"/otn/index/initMy12306")
                        .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2]).addHeader(headers[3]).addHeader(headers[4]).addHeader(headers[5]).addHeader(headers[6])
                        .build();
                initMy12306.addHeader("X-Requested-With","XMLHttpRequest");
                initMy12306.addHeader("Connection","keep-alive");
                response = httpclient.execute(initMy12306);
                statusCode = response.getStatusLine().getStatusCode();
                logger.info("initMy12306 statusCode:{}",statusCode);
                EntityUtils.consume(response.getEntity());

                logger.info("用户登录成功");
                //将成功的cookie写入文件
                writeCookies2File();
                return true;

            }

        }catch (Exception e){
            logger.error("登陆时发生错误",e);
        }
        return false;
    }

    /**
     * 增加另外两个cookie
     * @param headers
     */
    private void getDeviceCookie(Header[] headers){
        HttpUriRequest getDevice = RequestBuilder.get()//.post()
                .setUri("https://"+hosts+"/otn/HttpZF/logdevice?algID=iExnZxdyh4&hashCode=2EgNFh3Z_s7TjiwZrAbzjkVg8aGr2ty5y-CTVyrFUM8&FMQw=0&q4f3=zh-CN&VySQ=FGF4f4vSNpAHLEk8PaI2MM-cyFVlF53-&VPIf=1&custID=133&VEek=unknown&dzuS=0&yD16=0&EOQP=5898b2e342e89ecdecb05e6531359f3d&lEnu=3232238122&jp76=4135096c30ba4dbfda5c27ac46d91938&hAqN=Win32&platform=WEB&ks0Q=b9a555dce60346a48de933b3e16ebd6e&TeRS=1040x1920&tOHY=24xx1080x1920&Fvje=i1l1o1s1&q5aJ=-8&wNLf=99115dfb07133750ba677d055874de87&0aew=Mozilla/5.0%20(Windows%20NT%2010.0;%20Win64;%20x64)%20AppleWebKit/537.36%20(KHTML,%20like%20Gecko)%20Chrome/63.0.3239.132%20Safari/537.36&E3gR=af9ced95817938a0d8f7d89f50a47765&timestamp="+(new Date()).getTime())
                .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2])
                .build();
        CloseableHttpResponse response2 = null;//httpclient.execute(getDevice);
        try {
            response2 = httpclient.execute(getDevice);
            HttpEntity entity = response2.getEntity();
            getAllHeaders(response2);


            System.out.println("Login form get: " + response2.getStatusLine());
            String jsonStr= EntityUtils.toString(entity);
            jsonStr=jsonStr.replace("callbackFunction('","").replace("')","");
            System.out.println("entity " +jsonStr);
            Map<String,String> map = new HashMap<>();
            map = jsonBinder.fromJson(jsonStr,Map.class);
            System.out.println("Login form get: " +map);
            RAIL_DEVICEID = map.get("dfp");
            BasicClientCookie acookie = new BasicClientCookie("RAIL_DEVICEID", map.get("dfp"));
            acookie.setDomain(".12306.cn");
            acookie.setPath("/");
            cookieStore.addCookie(acookie);
            BasicClientCookie bcookie = new BasicClientCookie("RAIL_EXPIRATION", map.get("exp"));
            RAIL_EXPIRATION =  map.get("exp");
            bcookie.setDomain(".12306.cn");
            bcookie.setPath("/");
            cookieStore.addCookie(bcookie);

//            getAllCookies(cookieStore);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null!=response2){
                try{
                    response2.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<String,Object> checkOnlineStatus(Header[] headers){
        Map<String,Object> map = new HashMap<String,Object>();
        HttpUriRequest getDevice = RequestBuilder.post()
                .setUri("https://"+hosts+"/passport/web/auth/uamtk")
                .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2])
                .addParameter("appid","otn")
                .build();
        for(Header h:headers){
            getDevice.addHeader(h);
        }
        CloseableHttpResponse response2 = null;//httpclient.execute(getDevice);
        try {
            response2 = httpclient.execute(getDevice);
            HttpEntity entity = response2.getEntity();


            String jsonStr= EntityUtils.toString(entity);
            logger.info("checkOnlineStatus entity:{}",jsonStr);
            map = jsonBinder.fromJson(jsonStr,Map.class);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null!=response2){
                try{
                    response2.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return map;
    }

    public void sendSuccessMail(String msg){
        mailUtils.send(msg);
    }

    public String queryInit(Header[] headers){
            CloseableHttpResponse response=null;
            String queryUrl ="";
            String responseBody = "";
            try{
                if(headers.length<7){
                    headers = new BasicHeader[7];
                    headers[0] = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                    headers[1] = new BasicHeader("Host","kyfw.12306.cn");
                    headers[2] = new BasicHeader("Referer","https://kyfw.12306.cn/otn/login/init");
                    headers[3] = new BasicHeader("Accept","*/*");
                    headers[4] = new BasicHeader("Accept-Encoding","gzip, deflate");
                    headers[5] = new BasicHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
                    headers[6] = new BasicHeader("Content-Type","application/x-www-form-urlencoded");
                }
                headers[2] = new BasicHeader("Referer","https://kyfw.12306.cn/otn/index/init");
                HttpUriRequest confirm = RequestBuilder.post()
                        .setUri(new URI("https://kyfw.12306.cn/otn/leftTicket/init"))
                        .addHeader(headers[0]).addHeader(headers[1]).addHeader(headers[2]).addHeader(headers[3]).addHeader(headers[4]).addHeader(headers[5]).addHeader(headers[6])
                        .addParameter("_json_att", "")
                        .build();
                response = httpclient.execute(confirm);



                if(response.getStatusLine().getStatusCode()==200){
                    HttpEntity entity = response.getEntity();
                    responseBody =EntityUtils.toString(entity);
                    logger.info("查询页面初始化成功");
                    Pattern p=Pattern.compile("CLeftTicketUrl \\= '(.*?)';");
                    Matcher m=p.matcher(responseBody);
                    while(m.find()){
                        queryUrl=m.group(1);
                        setLeftTicketUrl(queryUrl);
                        logger.info("查询的地址是：{}",queryUrl);
                    }
                    headers[2]= new BasicHeader("Referer","https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc");
                }else{
                    logger.warn("查询页面初始化失败 status错误:",response.getStatusLine().getStatusCode());
                }

            }catch (Exception e){
                logger.error("查询页面初始化失败:{}",responseBody,e);
            }finally {
                try{
                    response.close();
                }catch (Exception e){

                }
            }
            return queryUrl;

    }

   /* public void getCodeByte(Header[] headers) {
        byte[] bytse = null;
        try {
            HttpGet hget = new HttpGet("https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp&" + Math.random());
            for(Header h:headers){
                hget.addHeader(h);
            }
            CloseableHttpResponse response = httpclient.execute(hget);

            HttpEntity entity = response.getEntity();
            bytse=  EntityUtils.toByteArray(entity);
            EntityUtils.consume(entity); //Consume response content
        }catch(Exception e ){
            e.printStackTrace();
        }

    }*/

    public void resetCookieStore(){
//        cookieStore = new BasicCookieStore();
        cookieStore.clear();
//        httpclient = HttpClients.custom()
//                .setDefaultCookieStore(cookieStore)
//                .build();
//        ct.cookieStore = cookieStore;
//        ct.httpclient = httpclient;
    }

}
