package cn.com.test.my12306.my12306.core.util;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class DateUtil {
	
	public static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH"); 
	public static SimpleDateFormat longSdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	public static SimpleDateFormat ymdSdf = new SimpleDateFormat("yyyyMMdd");
	
	 /** 
     * 获得本天的开始时间，即2012-01-01 00:00:00 
     * 
     * @return 
     */ 
    public Date getCurrentDayStartTime(Date now) { 
        try { 
            now = shortSdf.parse(shortSdf.format(now)); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return now; 
    } 
    
    /** 
     * 获得本天的结束时间，即2012-01-01 23:59:59 
     * 
     * @return 
     */ 
    public   Date getCurrentDayEndTime(Date now) { 
        try { 
            now = longSdf.parse(shortSdf.format(now) + " 23:59:59"); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return now; 
    } 
	
	public static void assembleTime(Map<String,Object> logMap) {
		String visitTime = (String)logMap.get("visitTime");
		Calendar c = Calendar.getInstance();
		Date now = new Date(Long.valueOf(visitTime));
    	c.setTime(now);
    	int year = c.get(Calendar.YEAR);
    	int month = c.get(Calendar.MONTH) + 1;
    	int quarter = getQuarter(month);
    	int weekOfYear = c.get(Calendar.WEEK_OF_YEAR);
    	int weekOfMonth = c.get(Calendar.WEEK_OF_MONTH);
    	int dayOfYear = c.get(Calendar.DAY_OF_YEAR);
    	int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    	int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    	int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
    	int minute = c.get(Calendar.MINUTE);
    	int second = c.get(Calendar.SECOND);
    	logMap.put("year", year);
    	logMap.put("quarter", quarter);
    	logMap.put("month", month);
    	logMap.put("weekOfYear", weekOfYear);
    	logMap.put("weekOfMonth", weekOfMonth);
    	logMap.put("dayOfYear", dayOfYear);
    	logMap.put("dayOfMonth", dayOfMonth);
    	logMap.put("dayOfWeek", dayOfWeek);
    	logMap.put("hourOfDay", hourOfDay);
    	logMap.put("minute", minute);
    	logMap.put("second", second);
    	logMap.put("visitTime",Long.valueOf(visitTime));
    	logMap.put("visitDay",shortSdf.format(now));
	}
	/**
	 * 获取time加上i个小时之后的时间
	 * @param time 2016-06-01 08
	 * @param x  5
	 * @return 2016-06-01 13
	 */
	public static String getNextXHour(String time,int x){
		 Calendar cal = Calendar.getInstance();
         
         SimpleDateFormat ftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         SimpleDateFormat ftime1 = new SimpleDateFormat("yyyy-MM-dd HH");
         
         Date trialTime;
		try {
			trialTime = ftime1.parse(time);
			 cal.setTime(trialTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
         cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + x);
         
		return ftime1.format(cal.getTime());
	}
	
	
	/**
	 * 获取前一天
	 * 
	 * @param
	 * @return yesterday
	 */
	public static final String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(cal.getTime());
	}
	
	
	/**
	 * 计算t1加上（减去）days 天后的日期
	 * @param t1  日期 格式yyyy-MM-dd
	 * @param days int 天数 可以是负数
	 * @return 计算t1加上（减去）days 天后的日期,如果加上后的日期大于昨天，则返回昨天。
	 * @throws ParseException
	 */
	public static final String getDaysAfter(String t1,  int days)
			throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date dd = df.parse(t1);
		cal.setTime(dd);
		cal.add(Calendar.DAY_OF_MONTH, days);
		String day = DateUtil.getYesterday();
		if (Integer.parseInt(df.format(cal.getTime()).replaceAll("-", "")) <= Integer
				.parseInt(DateUtil.getYesterday().replaceAll("-", ""))) {
			day = df.format(cal.getTime());
		}
		return day;
	}
	
	/**
	 * 计算t1加上（减去）days 天后的日期
	 * @param t1 日期 格式yyyy-MM-dd
	 * @param days int 天数 可以是负数
	 * @return 计算t1加上（减去）days 天后的日期,不管加上后的日期是否大于昨天
	 * @throws ParseException 
	 */
	public static final String getDaysAfter1(String t1,  int days)
			throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date dd = df.parse(t1);
		cal.setTime(dd);
		cal.add(Calendar.DAY_OF_MONTH, days);
		/* day = DateUtil.getYesterday();
		if (Integer.parseInt(df.format(cal.getTime())) <= Integer
				.parseInt(DateUtil.getYesterday())) {
			day = df.format(cal.getTime());
		}*/
		 String day = df.format(cal.getTime());
		return day;
	}
	
	/**
	 * 计算t2,t1(t2>=t1)两个日期的间隔天数
	 * @param t1
	 * @param t2
	 * @return t2-t1的天数
	 * @throws ParseException
	 */
	public static final int getBetweenDay(String t1, String t2)
			throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		int i = new Long((df.parse(t2).getTime() - df.parse(t1).getTime())
				/ (1000 * 60 * 60 * 24)).intValue();
		return i;
	}
	
	/**
	 * 获取访问时间的日期字符串yyyyMMdd
	 * @param logMap
	 * @return
	 */
	public static String getVisitDay(Map<String,Object> logMap){
		Long visitTime = (Long)logMap.get("visitTime");
		Date visit = new Date(visitTime);
		return ymdSdf.format(visit);
	}
	
	public static String getCurrentDay(Date date){
		return ymdSdf.format(date);
	}
	
	public static long getDayNum(long endDay,long startDay) {
		try {
			Date end = new Date(endDay);
			Date start = new Date(startDay);
			long diffTime = shortSdf.parse(shortSdf.format(end)).getTime() -shortSdf.parse(shortSdf.format(start)).getTime();
			return diffTime/1000/60/60/24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int getDayNum(String endDay,String startDay) {
		try {
			 SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd");
			Date end = fdate.parse(endDay);
			Date start = fdate.parse(startDay);
			long diffTime = shortSdf.parse(shortSdf.format(end)).getTime() -shortSdf.parse(shortSdf.format(start)).getTime();
			return (int)(diffTime/1000/60/60/24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 获取两个时间之间的小时数
	 * @param endDay 2016-11-23 10
	 * @param startDay 2016-11-23 08
	 * @return
	 */
	public static long getHourNum(String endDay,String startDay) {
		try {
			 SimpleDateFormat ftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	         SimpleDateFormat ftime1 = new SimpleDateFormat("yyyy-MM-dd HH");
	         
	         Date endTime = ftime1.parse(endDay);
	         Date startTime = ftime1.parse(startDay);
			long diffTime = longHourSdf.parse(longHourSdf.format(endTime)).getTime() -longHourSdf.parse(longHourSdf.format(startTime)).getTime();
			return diffTime/1000/60/60;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	

    public static int getQuarter(int month) {
    	int quarter = 0;
    	if(month >=1 && month <=3) {
    		quarter = 1;
    	}else if(month >=4 && month <=6) {
    		quarter = 2;
    	}else if(month >=7 && month <=9) {
    		quarter = 3;
    	}else if(month >=9 && month <=12) {
    		quarter = 4;
    	}
    	return quarter;
    }
    
    public static String getNextDay(String dateStr) {
    	try {
			Date currDay = ymdSdf.parse(dateStr);
			Date nextDay = new Date(currDay.getTime()+1000*60*60*24);
			return ymdSdf.format(nextDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }
    /**
     * 获取dateStr时间加上X天后的时间
     * @param dateStr YYYY-MM-DD 
     * @param x
     * @return YYYY-MM-DD
     */
    public static String getNextXDay(String dateStr,int x) {
    	try {
//    		SimpleDateFormat format=null;
//    		if(formatter.equals("")){
//    			format = new SimpleDateFormat("yyyy-MM-dd");
//    		}else{
//    			format= new SimpleDateFormat(formatter);
//    		}
			Date currDay = shortSdf.parse(dateStr);
			Date nextDay = new Date(currDay.getTime()+x*1000*60*60*24);
			return shortSdf.format(nextDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }

	/**
	 * 查看是否是正常的订票时间
	 * @return true 正常订票时间 false：维护时间
	 */
    public static boolean isNormalTime(){
    	boolean runFlag = false;
    	String startTimeStr = "06:00:00";
    	String endTimeStr ="23:00:00";
		String format = "HH:mm:ss";
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
		String now = sf.format(new Date());
//		now = "05:58:00";
		Date nowTime;
		try {
			nowTime = new SimpleDateFormat(format).parse(now);
			Date startTime = new SimpleDateFormat(format).parse(startTimeStr);
			Date endTime = new SimpleDateFormat(format).parse(endTimeStr);
			if (isEffectiveDate(nowTime, startTime, endTime)) {
				runFlag = true;
//				System.out.println("系统时间在"+startTimeStr+"点到"+endTimeStr+"点之间.");
			} else {
				runFlag = false;
				System.out.println("系统时间不在"+startTimeStr+"点到"+endTimeStr+"点之间.");
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return runFlag;
	}

	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 *
	 * @param nowTime   当前时间
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return
	 * @author jqlin
	 */
	public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
		if (nowTime.getTime() == startTime.getTime()
				|| nowTime.getTime() == endTime.getTime()) {
			return true;
		}

		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}
	@Test
	public void testaaa(){
		System.out.println(isNormalTime());
	}
} 
