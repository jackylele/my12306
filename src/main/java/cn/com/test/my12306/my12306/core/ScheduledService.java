
package cn.com.test.my12306.my12306.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 调度控制
 */
@Component
public class ScheduledService {

    @Autowired
    ClientTicket ct;

    /**
     * 晚上23点关闭任务<br/>
     * 主要关闭检票线程 主线程等待
     */
    @Scheduled(cron="0 0 23 * * *")
    public void shutdownCT(){
        System.out.println("服务器维护时间，关闭多线程查票");
        ct.shutdownqueryThread();
    }


    /**
     * 早上6点重新开启任务
     * cron需晚于DateUtil中的startTimeStr
     */
    @Scheduled(cron="50 58 5 * * *")
    public void startCT(){
        System.out.println("启动主线程开始刷票");
        try {
            ct.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
