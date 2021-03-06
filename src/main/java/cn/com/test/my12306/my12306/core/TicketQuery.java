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
import cn.com.test.my12306.my12306.core.util.JsonBinder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class TicketQuery implements  Runnable{

    public JsonBinder jsonBinder = JsonBinder.buildNonNullBinder(false);
    private static Logger logger = LogManager.getLogger(TicketQuery.class);
    CommonUtil commonUtil = new CommonUtil();

    private BlockingQueue<Map<String,String>> queue ;
    private ClientTicket ct;
    public TicketQuery(BlockingQueue<Map<String,String>> queue,ClientTicket ct){
        this.queue=queue;
        this.ct = ct;
    }

    /*public TicketQuery() {
        this.ct = ApplicationContextProvider.getBean(ClientTicket.class);
        this.commonUtil = ApplicationContextProvider.getBean(CommonUtil.class);
    }*/

    @SuppressWarnings("unchecked")
    @Override
    public void run(){

        //超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3500).setConnectionRequestTimeout(3500)
                .setSocketTimeout(3500).build();


        Map<String,Integer> trainSeatMap = ct.getTrainSeatMap();
        Map<String,Long> trainSeatTimeMap = ct.getTrainSeatTimeMap();
        Map<String,String> m = new HashMap<>();
        while(ct.canRun){

            if(!DateUtil.isNormalTime()){
                logger.info("维护时间，暂停查询");
                try{
                    Thread.sleep(10000);
                }catch (Exception e){
                }
                return ;
            }



            CloseableHttpClient httpClient=null;
            try{

            httpClient = TicketHttpClient.getClient();

            String queryIp = commonUtil.getIp();

            String urlStr = "http://"+queryIp+"/otn/"+ct.getLeftTicketUrl()+"?leftTicketDTO.train_date="+commonUtil.getDate()+"&leftTicketDTO.from_station="+ commonUtil.getFromCode()+"&leftTicketDTO.to_station="+ commonUtil.getToCode()+"&purpose_codes=ADULT";
//            System.out.println("ip:"+queryIp);
            HttpGet httpget = new HttpGet(urlStr);
            httpget.setHeader("Host", "kyfw.12306.cn");//设置host
                httpget.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity, "UTF-8");
                Map<String, Object> rsmap = null;
                rsmap = this.jsonBinder.fromJson(content, Map.class);
                String status = rsmap.get("httpstatus")+"";
                if(status.equalsIgnoreCase("200")){
                    Map data = (Map)rsmap.get("data");
                    if(data.size()>0){
                        List<String> arr = (List<String>)data.get("result");
//                        System.out.println(arr.size()+"a "+arr.get(0));
                        Map<String,Map<String,String>> map = new ConcurrentHashMap<String,Map<String,String>>();
                        commonUtil.jiexi(arr,map);
                        List< Map<String,String>> youpiao= commonUtil.getSecretStr(map,commonUtil.getTrains(),commonUtil.getSeats());
                       if(youpiao.size()>0){
                           for(Map<String,String> map1:youpiao){
                               String chehao = map1.get("chehao");
                               String tobuySeat = map1.get("toBuySeat");
                               Long shijian = trainSeatTimeMap.get(chehao+"_"+tobuySeat);
                               Integer cishu =trainSeatMap.get(chehao+"_"+tobuySeat);
                                if(null!=shijian ){
                                    if(System.currentTimeMillis()>shijian){
                                        trainSeatMap.put(chehao+"_"+tobuySeat,0);
                                        cishu=0;
                                    }
                                }
                               if(null!=cishu ){
                                if(cishu>3){
                                    //放入小黑屋15秒
                                    trainSeatTimeMap.put(chehao+"_"+tobuySeat,System.currentTimeMillis()+15*1000);
                                    trainSeatMap.put(chehao+"_"+tobuySeat,0);
                                    logger.error(chehao+"_"+tobuySeat+"放入小黑屋15秒");
                                }else{
                                    trainSeatMap.put(chehao+"_"+tobuySeat,cishu+1);
                                    queue.put(map1);
                                    logger.info("队列内容:{}",queue);
                                }
                               }else{//第一次
                                   queue.put(map1);
                                   trainSeatMap.put(chehao+"_"+tobuySeat,0);
                               }

                           }
                       }
                       logger.info("{}:查询成功",queryIp);
                    }
                }
            }catch (ConnectTimeoutException e1){
//                logger.error("ConnectTimeout查询超时1");
            }catch (SocketTimeoutException se){
//                logger.error("socketTimeout查询超时");
            }catch (Exception e){
//                logger.error("查询出錯");
            }finally {
                try{
                    httpClient.close();
                }catch (Exception e){

                }
            }

        }

    }


}
