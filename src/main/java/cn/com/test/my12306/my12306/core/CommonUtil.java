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

import cn.com.test.my12306.my12306.core.util.JsonBinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class CommonUtil {

    public JsonBinder jsonBinder = JsonBinder.buildNonNullBinder(false);

    private static final Logger logger = LogManager.getLogger(TicketQuery.class);

    public static String user="张无忌";

    public static String seats="二等";
//    public static String trains ="G6704";
    public static  String trains ="G80,G488,G616,G82,G618,G518,G516,G88,G66,G620,G72,G658";
    public static String date ="2018-08-10";//yyyy-mm-dd
    public static String from ="石家庄";
    public static String to ="北京";
    public static String autoCode = "0";
    public static final String codePath="myCode";

    public static int queryNum=5;//查询余票的线程数

    public static String userName="xxx";//12306 账号
    public static String userPwd="yyy";//12306 账号

//hosts 参考：https://github.com/ebert-chan/kyfw12306/blob/master/hosts.json
    //https://github.com/testerSunshine/12306/blob/master/cdn_list
    private static final String[] ips =new String[]{"112.28.196.95","114.112.160.29","125.90.206.64","59.80.29.126","125.77.147.254","42.123.117.51","111.20.250.247","111.40.183.212","120.226.49.98","58.220.71.55","112.28.196.93","175.43.20.70","119.84.128.86","111.6.176.138","183.236.28.251","27.155.108.240","218.76.105.50","113.104.14.228","113.104.14.227","114.112.160.31","114.112.160.30","61.162.100.36","120.206.189.65","114.112.160.32","222.221.102.18","117.27.245.54","117.169.93.106","112.17.27.55","36.250.248.254","58.223.166.253","124.116.133.63","112.240.60.252","221.194.180.54","183.240.52.131","101.66.224.248","218.12.228.85","112.30.151.93","42.81.144.191","42.248.92.251","106.120.178.48","222.186.145.101","150.138.167.191","111.62.92.126","124.167.218.28","122.136.46.119","120.221.23.221","36.248.5.254","27.195.145.124","117.148.163.220","60.211.208.36","59.80.28.254","59.47.74.15","36.250.248.48","36.250.248.49","117.27.241.254","110.19.204.254","183.56.172.124","27.155.72.49","112.47.20.156","61.149.22.254","182.242.50.252","223.111.19.111","117.148.128.254","117.23.2.241","182.140.218.254","122.224.186.252","123.53.139.41","61.136.167.74","123.53.139.42","36.104.132.151","111.19.233.145","117.145.179.69","14.21.78.112","36.99.32.75","111.161.122.181","218.76.94.252","112.90.135.238","182.242.50.166","223.113.14.97","117.169.113.24","183.56.168.252","61.132.238.126","157.255.76.56","117.161.66.88","124.236.97.49","124.236.28.254","180.97.178.147","113.207.0.70","183.253.58.92","122.70.142.148","122.70.142.147","180.97.178.140","123.183.164.251","219.147.93.62","117.161.19.126","219.157.114.127","110.242.21.78","111.161.22.62","218.98.47.254","59.80.29.40","118.180.15.31","221.235.187.96","58.223.164.28","112.51.125.254","112.240.60.88","113.142.88.31","118.118.216.252","59.44.30.106","60.9.0.28","60.9.0.29","123.138.203.32","123.183.164.40","180.163.150.154","222.138.255.252","183.216.176.116","36.250.74.35"};


    public static String sessionPath="C:/my12306/";//存储cookie的位置


    public static Map<String,String> stationMap = new HashMap<String,String>();
    public static  Map<String,String> seatMap = new HashMap<String,String>();
    public static  List<String> fromList = new ArrayList<String>();
    public static  List<String> toList = new ArrayList<String>();

    public String getUser() {
        return user;
    }
    @Value("${passengerNames}")
    public void setUser(String user) {
        CommonUtil.user = user;
    }

    public String getSeats() {
        return seats;
    }

    @Value("${toBuySeat}")
    public void setSeats(String seats) {
        CommonUtil.seats = seats;
    }

    public String getTrains() {
        return trains;
    }

    @Value("${toBuyTrains}")
    public void setTrains(String trains) {
        CommonUtil.trains = trains;
    }

    public String getDate() {
        return date;
    }

    @Value("${BuyDate}")
    public void setDate(String date) {
        CommonUtil.date = date;
    }

    public String getFrom() {
        return from;
    }

    @Value("${BuyFrom}")
    public void setFrom(String from) {
        CommonUtil.from = from;
    }

    public String getTo() {
        return to;
    }

    @Value("${BuyTo}")
    public void setTo(String to) {
        CommonUtil.to = to;
    }

    public int getQueryNum() {
        return queryNum;
    }

    @Value("${queryThreads}")
    public void setQueryNum(int queryNum) {
        CommonUtil.queryNum = queryNum;
    }

    public String getUserName() {
        return userName;
    }

    @Value("${12306.userName}")
    public void setUserName(String userName) {
        CommonUtil.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    @Value("${12306.userPwd}")
    public void setUserPwd(String userPwd) {
        CommonUtil.userPwd = userPwd;
    }

    public String getSessionPath() {
        return sessionPath;
    }

    @Value("${sessionPath}")
    public void setSessionPath(String sessionPath) {
        CommonUtil.sessionPath = sessionPath;
    }

    public String getAutoCode() {
        return autoCode;
    }
    @Value("${autoCode}")
    public void setAutoCode(String autoCode) {
        CommonUtil.autoCode = autoCode;
    }

    public Map<String, String> getStationMap() {
        return stationMap;
    }

    public void setStationMap(Map<String, String> stationMap) {
        this.stationMap = stationMap;
    }

    public Map<String, String> getSeatMap() {
        return seatMap;
    }

    public void setSeatMap(Map<String, String> seatMap) {
        this.seatMap = seatMap;
    }

    public List<String> getFromList() {
        return fromList;
    }

    public void setFromList(List<String> fromList) {
        this.fromList = fromList;
    }

    public List<String> getToList() {
        return toList;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    @PostConstruct
    public void init(){
        /*
        public static String BUSS_SEAT = "9"; // 商务座
	public static String BEST_SEAT = "p";// 特等座(余票)
	public static String ONE_SEAT = "M";// 一等座(余票)
	public static String TWO_SEAT = "O";// 二等座(余票)
	public static String VAG_SLEEPER = "6";// 高级软卧(余票)
	public static String SOFT_SLEEPER = "4";// 软卧(余票)
	public static String HARD_SLEEPER = "3";// 硬卧(余票)
	public static String SOFT_SEAT = "2";// 软座(余票)
	public static String HARD_SEAT = "1";// 硬座(余票)
	public static String NONE_SEAT = "1";// 无座(余票)
         */

        seatMap.put("二等","O");
        seatMap.put("一等","M");
        seatMap.put("硬卧","3");
        seatMap.put("硬座","1");
        seatMap.put("软卧","4");

        //https://github.com/AlbertHooGoGo/12306/blob/master/stationlist.py  获取
        String station_names="一间堡:YJT,一面坡:YPB,一面山:YST,七台河:QTB,七甸:QDM,七营:QYJ,七里河:QLD,万乐:WEB,万发屯:WFB,万宁:WNQ,万州:WYW,万州北:WZE,万年:WWG,万源:WYY,三义井:OYD,三井子:OJT,三亚:SEQ,三关口:OKJ,三十家:SRD,三十里堡:SST,三原:SAY,三合庄:SVP,三堂集:SDH,三家寨:SMM,三家店:ODP,三明:SMS,三明北:SHS,三水:SJQ,三水北:ARQ,三水南:RNQ,三汇镇:OZW,三江南:SWZ,三江县:SOZ,三江口:SKD,三河县:OXP,三源浦:SYL,三穗:QHW,三营:OEJ,三道湖:SDL,三都县:KKW,三门县:OQH,三门峡:SMF,三门峡南:SCF,三门峡西:SXF,三间房:SFX,三阳川:SYJ,上万:SWP,上园:SUD,上杭:JBS,上板城:SBP,上板城南:OBP,上海:SHH,上海南:SNH,上海虹桥:AOH,上海西:SXH,上腰墩:SPJ,上虞:BDH,上西铺:SXM,上饶:SRG,上高镇:SVK,下台子:EIP,下城子:XCB,下板城:EBP,下社:XSV,下花园:XYP,下马塘:XAT,世博园:ZWT,东:FDC,东丰:DIL,东乡:DXG,东二道河:DRB,东京城:DJB,东光:DGP,东升:DRQ,东台:DBH,东城南:IYQ,东安东:DCZ,东岔:DCJ,东庄:DZV,东戌:RXP,东戴河:RDD,东方:UFQ,东方红:DFB,东明县:DNF,东明村:DMD,东来:RVD,东津:DKB,东海:DHB,东海县:DQH,东淤地:DBV,东港北:RGT,东湾:DRJ,东胜:DOC,东胜西:DYC,东至:DCH,东莞:RTQ,东莞东:DMQ,东营:DPK,东营南:DOK,东辛庄:DXD,东边井:DBB,东通化:DTL,东镇:DNV,两家:UJT,两当:LDY,中华门:VNH,中卫:ZWJ,中和:ZHX,中宁:VNJ,中宁东:ZDJ,中宁南:ZNJ,中寨:ZZM,中山:ZSQ,中山北:ZGQ,中川机场:ZJJ,中牟:ZGF,丰乐镇:FZB,丰城:FCG,丰城南:FNG,丰水村:FSJ,丰都:FUW,丰镇:FZC,丰顺:FUQ,临城:UUP,临川:LCG,临江:LQL,临汾:LFV,临汾西:LXV,临沂:LVK,临沂北:UYK,临河:LHC,临泽:LEJ,临泽南:LDJ,临海:UFH,临清:UQK,临湘:LXQ,临澧:LWQ,临西:UEP,临邑:LUK,临颍:LNF,临高南:KGQ,丹东:DUT,丹东西:RWT,丹凤:DGY,丹徒:RUH,丹阳:DYH,丹阳北:EXH,丹霞山:IRQ,丽水:USH,丽江:LHM,乃林:NLD,义乌:YWH,义县:YXD,义马:YMF,乌伊岭:WPB,乌兰哈达:WLC,乌兰察布:WPC,乌兰浩特:WWT,乌奴耳:WRX,乌尔旗汗:WHX,乌拉山:WSC,乌拉特前旗:WQC,乌海:WVC,乌海西:WXC,乌西:WXR,乌鲁木齐:WAR,乌鲁木齐南:WMR,乌龙泉南:WFN,乐东:UQQ,乐善村:LUM,乐山:IVW,乐山北:UTW,乐平市:LPG,乐昌:LCQ,乐昌东:ILQ,乐清:UPH,乐都:LDO,乐都南:LVO,九三:SSX,九台:JTL,九台南:JNL,九江:JJG,九郎山:KJQ,乳山:ROK,乾县:QBY,乾安:QOT,二密河:RML,二营:RYJ,二连:RLC,二道沟门:RDP,二道湾:RDX,二龙:RLD,二龙山屯:ELA,于家堡:YKP,于都:YDG,云东海:NAQ,云居寺:AFP,云山:KZQ,云彩岭:ACP,云梦:YMN,云浮东:IXQ,云霄:YBS,五五:WVR,五原:WYC,五叉沟:WCT,五台山:WSV,五大连池:WRB,五女山:WET,五家:WUB,五寨:WZV,五常:WCB,五府山:WFG,五棵树:WKT,五莲:WLK,五营:WWB,五道沟:WDL,五道河:WHP,五龙背:WBT,五龙背东:WMT,井冈山:JGG,井南:JNP,井店:JFP,井陉:JJP,亚:JUQ,亚布力:YBB,亚布力南:YWB,亚龙湾:TWQ,交城:JNV,京山:JCN,亭亮:TIZ,亮甲店:LRT,亳州:BZH,什里店:OMP,仁布:RUO,介休:JXV,介休东:JDV,从江:KNW,仙人桥:XRL,仙林:XPH,仙桃西:XAN,仙游:XWS,代县:DKV,仪征:UZH,仲恺:KKQ,任丘:RQP,伊图里河:YEX,伊宁:YMR,伊宁东:YNR,伊尔施:YET,伊拉哈:YLX,伊敏:YMX,伊春:YCB,伊林:YLB,伊通:YTL,会同:VTQ,会昌北:XEG,低庄:DVQ,低窝铺:DWJ,余姚:YYH,余姚北:CTH,余杭:EVH,余江:YHG,余粮堡:YLD,佛坪:FUY,佛山:FSQ,佛山西:FOQ,佳木斯:JMB,依安:YAX,侯马:HMV,侯马西:HPV,保定:BDP,保定东:BMP,保康:BKD,信丰:EFG,信宜:EEQ,信阳:XUN,信阳东:OYN,修文县:XWE,修武:XWF,修武西:EXF,倭肯:WQB,偃师:YSF,偏岭:PNT,偏店:PRP,元宝山:YUD,元氏:YSP,元谋:YMM,先锋:NQQ,光山:GUN,光明城:IMQ,光泽:GZS,克一河:KHX,克东:KOB,克山:KSB,克拉玛依:KHR,免渡河:MDX,兑镇:DWV,兖州:YZK,全州南:QNZ,全椒:INH,八仙筒:VXD,八方山:FGQ,八步:BBE,八角台:BTD,八达岭:ILP,八面城:BMD,八面通:BMB,公主岭:GLT,公主岭南:GBT,公庙子:GMC,公营子:GYD,六合:KLH,六合镇:LEX,六安:UAH,六枝:LIW,六盘山:UPJ,六盘水:UMW,六道河子:LVP,兰岗:LNB,兰州:LZJ,兰州东:LVJ,兰州新区:LQJ,兰州西:LAJ,兰棱:LLB,兰溪:LWH,兰考:LKF,兰考南:LUF,兰陵北:COK,共青城:GAG,关岭:GLE,关林:GLF,兴业:SNZ,兴义:XRZ,兴凯:EKB,兴和西:XEC,兴国:EUG,兴城:XCD,兴宁:ENQ,兴安:XAZ,兴安北:XDZ,兴平:XPY,兴泉堡:XQJ,兴隆县:EXP,兴隆店:XDD,兴隆镇:XZB,内乡:NXF,内江:NJW,内江北:NKW,册亨:CHZ,冕宁:UGW,军粮城北:JMP,农安:NAT,冠豸山:GSS,冯屯:FTX,冷水江东:UDQ,准格尔:ZEC,凌海:JID,凌源:LYD,凌源东:LDD,凤凰城:FHT,凤凰机场:FJQ,凤县:FXY,凤城东:FDT,凤州:FZY,凤阳:FUH,凭祥:PXZ,凯里:KLW,凯里南:QKW,刀尔登:DRD,分宜:FYG,刘家店:UDT,刘家河:LVT,创业村:CEX,利川:LCN,利津南:LNK,到保:RBT,前卫:QWD,前山:QXQ,前磨头:QMP,前苇塘:QWP,前进镇:QEB,前锋:QFB,剑门关:JME,加格达奇:JGX,勃利:BLB,勉县:MVY,包头:BTC,包头东:BDC,化州:HZZ,化德:HGC,北井子:BRT,北京:BJP,北京东:BOP,北京北:VAP,北京南:VNP,北京西:BXP,北台:BTT,北宅:BVP,北安:BAB,北屯:BYP,北屯市:BXR,北戴河:BEP,北流:BOZ,北海:BHZ,北滘:IBQ,北碚:BPW,北票南:RPD,北营:BIV,北马圈子:BRP,十堰:SNN,十家子:SJD,十渡:SEP,千河:QUY,千阳:QOY,午汲:WJP,华城:VCQ,华家:HJT,华容:HRN,华容东:HPN,华容南:KRN,华山:HSY,华山北:HDY,华蓥:HUW,卓资东:ZDC,卓资山:ZZC,南丰:NFG,南丹:NDZ,南京:NJH,南京南:NKH,南仇:NCK,南充:NCW,南充北:NCE,南关岭:NLT,南华北:NHS,南博山:NBK,南口:NKP,南口前:NKT,南召:NAF,南台:NTT,南城:NDG,南城司:NSP,南大庙:NMP,南头:NOQ,南宁:NNZ,南宁东:NFZ,南宁西:NXZ,南宫东:NFP,南岔:NCB,南峪:NUP,南平:NPS,南平北:NBS,南平南:NNS,南昌:NCG,南昌西:NXG,南曹:NEF,南朗:NNQ,南木:NMX,南杂木:NZT,南桥:NQD,南江:FIW,南江口:NDQ,南河川:NHJ,南湖东:NDN,南湾子:NWP,南翔北:NEH,南芬:NFT,南芬北:NUT,南观村:NGP,南通:NUH,南部:NBE,南阳:NFF,南阳寨:NYF,南陵:LLH,南雄:NCQ,南靖:NJS,博乐:BOR,博克图:BKX,博兴:BXK,博白:BBZ,博鳌:BWQ,卢龙:UAP,卧里屯:WLX,卫东:WVT,卫星:WVB,卫辉:WHF,即墨北:JVK,原平:YPV,厦门:XMS,厦门北:XKS,厦门高崎:XBS,友好:YOB,双丰:OFB,双吉:SML,双城北:SBB,双城堡:SCB,双峰北:NFQ,双河镇:SEL,双流机场:IPW,双流西:IQW,双牌:SBZ,双辽:ZJD,双阳:OYT,双鸭山:SSB,发耳:FEM,口东:KEQ,口前:KQL,古东:GDV,古交:GJV,古城镇:GZB,古浪:GLJ,古田:GTS,古田会址:STS,古田北:GBS,古莲:GRX,古镇:GNQ,句容西:JWH,台前:TTK,台安:TID,台安南:TAD,台州:TZH,叶城:YER,叶柏寿:YBD,司家岭:OLK,合川:WKW,合浦:HVZ,合肥:HFH,合肥北城:COH,合肥南:ENH,合肥西:HTH,合阳:HAY,合阳北:HTY,吉安:VAG,吉文:JWX,吉林:JLL,吉舒:JSL,吉首:JIQ,同心:TXJ,同江:TJB,后湖:IHN,吐列毛杜:TMD,吐哈:THR,吐鲁番:TFR,吐鲁番北:TAR,向塘:XTG,向阳:XDB,吕梁:LHV,吴圩:WYZ,吴堡:WUY,吴官田:WGM,吴家屯:WJT,吴家川:WCJ,吴桥:WUP,周口:ZKN,周家:ZOB,周家屯:ZOD,周水子:ZIT,呼兰:HUB,呼和浩特:HHC,呼和浩特东:NDC,呼鲁斯太:VTJ,咋子:ZAL,和什托洛盖:VSR,和平:VAQ,和田:VTR,和硕:VUR,和静:HJR,和龙:HLL,咸宁:XNN,咸宁东:XKN,咸宁北:XRN,咸宁南:UNN,咸阳:XYY,咸阳秦都:XOY,哈密:HMR,哈尔滨:HBB,哈尔滨东:VBB,哈尔滨北:HTB,哈尔滨西:VAB,哈拉海:HIT,哈拉苏:HAX,哈达铺:HDJ,哲里木:ZLC,唐家湾:PDQ,唐山:TSP,唐山北:FUP,唐河:THF,商丘:SQF,商丘南:SPF,商南:ONY,商城:SWN,商河:SOK,商洛:OLY,商都:SXC,喀什:KSR,喀喇其:KQX,喇嘛甸:LMX,喜德:EDW,嘉兴:JXH,嘉兴南:EPH,嘉善:JSH,嘉善南:EAH,嘉峪关:JGJ,嘉峪关南:JBJ,嘉峰:JFF,嘉祥:JUK,嘎什甸子:GXD,四会:AHQ,四合永:OHD,四平:SPT,四平东:PPT,四方台:STB,四道湾:OUD,团结:TIX,园墩:YAJ,固原:GUJ,固始:GXN,固安:GFP,固镇:GEH,图们:TML,图们北:QSL,图强:TQX,图里河:TEX,土地堂东:TTN,土桥子:TQJ,土溪:TSW,土牧尔台:TRC,土贵乌拉:TGC,土门子:TCJ,坂田:BTQ,坡底下:PXJ,坪上:PSK,坪石:PSQ,垫江:DJE,城固:CGY,城固北:CBY,城子坦:CWT,城阳:CEK,塔哈:THX,塔尔气:TVX,塔崖驿:TYP,塔河:TXX,塔石嘴:TIM,塘沽:TGP,塘豹:TBQ,墨玉:MUR,壮志:ZUX,复盛:FAW,夏官营:XGJ,夏石:XIZ,夏邑县:EJH,大丰:KRQ,大余:DYG,大元:DYZ,大关:RGW,大兴:DXX,大兴沟:DXL,大其拉哈:DQX,大冶北:DBN,大口屯:DKP,大同:DTV,大坝:DBJ,大埔:DPI,大堡:DVT,大孤山:RMT,大安:RAT,大安北:RNT,大官屯:DTT,大屯:DNT,大巴:DBD,大平房:DPD,大庆:DZX,大庆东:LFX,大庆西:RHX,大成:DCT,大战场:DTJ,大拟:DNZ,大方南:DNE,大旺:WWQ,大朗镇:KOQ,大杖子:DAP,大杨树:DUX,大板:DBC,大林:DLD,大武口:DFJ,大涧:DFP,大湾子:DFM,大灰厂:DHP,大王滩:DZZ,大理:DKM,大田边:DBM,大盘石:RPP,大石头:DSL,大石头南:DAL,大石寨:RZT,大石桥:DQT,大磴沟:DKJ,大禾塘:SOQ,大竹园:DZY,大红旗:DQD,大英东:IAW,大苴:DIM,大荔:DNY,大营:DYV,大营子:DZD,大营镇:DJP,大虎山:DHD,大足南:FQW,大连:DLT,大连北:DFT,大连西:GZT,大通西:DTO,大陆号:DLC,大雁:DYX,大青沟:DSD,天义:TND,天岗:TGL,天柱山:QWH,天桥岭:TQL,天水:TSJ,天水南:TIJ,天河机场:TJN,天河街:TEN,天津:TJP,天津北:TBP,天津南:TIP,天津西:TXP,天祝:TZJ,天镇:TZV,天门:TMN,天门南:TNN,太原:TYV,太原东:TDV,太原北:TBV,太原南:TNV,太和北:JYN,太姥山:TLS,太平川:TIT,太平镇:TEB,太湖:TKH,太谷:TGV,太谷西:TIV,太阳升:TQT,太阳山:TYJ,夹心子:JXT,奇峰塔:QVP,奈曼:NMD,奉化:FHH,奎屯:KTR,奎山:KAB,如东:RIH,如皋:RBH,始兴:IPQ,姚千户屯:YQT,姚安:YAC,姚家:YAT,姚渡:AOJ,姜堰:UEH,姜家:JJB,威海:WKK,威海北:WHK,威箐:WAM,威舍:WSM,威虎岭北:WBL,娄山关南:LSE,娄底:LDQ,娄底南:UOQ,娘子关:NIP,婺源:WYG,嫩江:NGX,子洲:ZZY,子长:ZHY,孙吴:SKB,孙家:SUB,孙镇:OZY,孝南:XNV,孝感:XGN,孝感东:GDN,孝感北:XJN,孝西:XOV,孟家岗:MGB,孟庄:MZF,孤家子:GKT,孤山口:GSP,宁东:NOJ,宁东南:NDJ,宁乡:NXQ,宁国:NNH,宁安:NAB,宁家:NVT,宁强南:NOY,宁德:NES,宁明:NMZ,宁村:NCZ,宁武:NWV,宁波:NGH,宁波东:NVH,宁海:NHH,宁陵县:NLF,安亭北:ASH,安仁:ARG,安化:PKQ,安口窑:AYY,安图:ATL,安图西:AXL,安塘:ATV,安多:ADO,安定:ADP,安家:AJB,安平:APT,安广:AGT,安庆:AQH,安庆西:APH,安康:AKY,安德:ARW,安溪:AXS,安达:ADX,安阳:AYF,安阳东:ADF,安陆:ALN,安靖:PYW,安顺:ASW,安顺西:ASE,安龙:AUZ,宋:SOB,宋城路:SFF,宏庆:HEY,官厅:GTP,官厅西:KEP,官字井:GOT,官高:GVP,定南:DNG,定州:DXP,定州东:DOP,定襄:DXV,定西:DSJ,定西北:DNJ,定边:DYJ,定远:EWH,定陶:DQK,宜兴:YUH,宜城:YIN,宜宾:YBW,宜州:YSZ,宜昌:YCN,宜昌东:HAN,宜春:YEG,宜春西:YCG,宜耐:YVM,宜良北:YSM,宝华山:BWH,宝坻:BPP,宝拉格:BQC,宝林:BNB,宝泉岭:BQB,宝清:BUB,宝鸡:BJY,宝鸡南:BBY,宝龙山:BND,宣化:XHP,宣和:XWJ,宣城:ECH,宣威:XWM,宣汉:XHY,容县:RXZ,容桂:RUQ,宽甸:KDT,宾阳:UKZ,宿州:OXH,宿州东:SRH,宿松:OAH,密云北:MUP,密山:MSB,富县:FEY,富县东:FDY,富宁:FNM,富川:FDZ,富拉尔基:FRX,富海:FHX,富源:FYM,富源北:FBM,富裕:FYX,富锦:FIB,寒岭:HAT,寒葱沟:HKB,察素齐:CSC,寮步:LTQ,对青山:DQB,寿阳:SYV,将乐:JLS,小东:XOD,小哨:XAM,小寺沟:ESP,小岭:XLB,小市:XST,小得江:EJM,小扬气:XYX,小新街:XXM,小月旧:XFM,小村:XEM,小榄:EAQ,小河沿:XYD,小河镇:EKY,小董:XEZ,小西庄:XXP,小金口:NKQ,小雨谷:XHM,尖山:JPQ,尖峰:PFQ,尚家:SJB,尚志:SZB,尤溪:YXS,尹地:YDM,尼勒克:NIR,尼木:NMO,屏边:PBM,山丹:SDJ,山坡东:SBN,山城镇:SCL,山市:SQB,山河屯:SHL,山海关:SHD,山阴:SNV,岐山:QAY,岑溪:CNZ,岔江:CAM,岢岚:KLV,岩会:AEP,岱岳:RYV,岳家井:YGJ,岳池:AWW,岳阳:YYQ,岳阳东:YIQ,岷县:MXJ,峡江:EJG,峨眉:EMW,峨眉山:IXW,峨边:EBW,峻德:JDB,崇仁:CRG,崇左:CZZ,崔黄口:CHP,崖州:YUQ,嵩明:SVM,嵯岗:CAX,巢湖:CIH,巢湖东:GUH,工农湖:GRT,左岭:ZSN,巨宝:JRT,巨野:JYK,巩义:GXF,巩义南:GYF,巴东:BNN,巴中:IEW,巴中东:BDE,巴山:BAY,巴彦高勒:BAC,巴林:BLX,巴楚:BCR,布列开:BLR,布海:BUT,师宗:SEM,师庄:SNM,带岭:DLB,常山:CSU,常州:CZH,常州北:ESH,常平:DAQ,常平东:FQQ,常平南:FPQ,常庄:CVK,常德:VGQ,帽儿山:MRB,干塘:GNJ,干沟:GGL,平关:PGM,平凉:PIJ,平凉南:POJ,平南南:PAZ,平原:PYK,平原东:PUK,平原堡:PPJ,平台:PVT,平坝南:PBE,平型关:PGV,平安:PAL,平安镇:PZT,平安驿:PNO,平山:PSB,平岗:PGL,平峪:PYP,平庄:PZD,平庄南:PND,平度:PAK,平房:PFB,平旺:PWV,平昌:PCE,平果:PGZ,平河口:PHM,平泉:PQP,平洋:PYX,平湖:PHQ,平田:PTM,平社:PSV,平遥:PYV,平遥古城:PDV,平邑:PIK,平顶山:PEN,平顶山西:BFF,广元:GYW,广元南:GAW,广南卫:GNM,广南县:GXM,广宁:FBQ,广宁寺:GQT,广宁寺南:GNT,广安:VJW,广安南:VUW,广州:GZQ,广州东:GGQ,广州北:GBQ,广州南:IZQ,广州西:GXQ,广德:GRH,广水:GSN,广汉:GHW,广汉北:GVW,广通北:GPM,庄桥:ZQH,庄河北:ZUT,庆丰:QFT,庆安:QAB,庆盛:QSQ,庆阳山:QSJ,庐山:LSG,庐江:UJH,库伦:KLD,库尔勒:KLR,库车:KCR,库都尔:KDX,应县:YZV,应城:YHN,庙城:MAP,庙山:MSN,庙岭:MLL,庙庄:MZJ,康城:KCP,康庄:KZP,康熙岭:KXZ,康金井:KJB,廉江:LJZ,廊坊:LJP,廊坊北:LFP,延吉:YJL,延吉西:YXL,延安:YWY,延庆:YNP,建三江:JIB,建始:JRN,建宁县北:JCS,建安:JUL,建昌:JFD,建水:JSM,建湖:AJH,建瓯:JVS,建瓯西:JUS,建设:JET,建阳:JYS,开化:KHU,开原:KYT,开原西:KXT,开安:KAT,开封:KFF,开封北:KBF,开江:KAW,开福寺:FLQ,开通:KTT,开阳:KVW,开鲁:KLC,弋江:RVH,弋阳:YIG,弓棚子:GPT,张兰:ZLV,张家口:ZKP,张家口南:ZMP,张家川:ZIJ,张家界:DIQ,张掖:ZYJ,张掖西:ZEJ,张桥:ZQY,张百湾:ZUP,张维屯:ZWB,张辛:ZIP,弥勒:MLM,弥渡:MDF,归流河:GHT,当涂东:OWH,当阳:DYN,彝良:ALW,彬县:BXY,彭山:PSW,彭山北:PPW,彭州:PMW,彭水:PHW,彭泽:PZG,彭阳:PYJ,彰武:ZWD,徐家:XJB,徐州:XCH,徐州东:UUH,徐水:XSP,徐闻:XJQ,得耳布尔:DRX,徘徊北:PHP,微子镇:WQP,德令哈:DHO,德伯斯:RDT,德保:RBZ,德兴:DWG,德兴东:DDG,德安:DAG,德州:DZP,德州东:DIP,德惠:DHT,德惠西:DXT,德昌:DVW,德清:DRH,德清西:MOH,德阳:DYW,徽县:HYY,忻州:XXV,怀仁:HRV,怀仁东:HFV,怀化:HHQ,怀化南:KAQ,怀柔:HRP,怀柔北:HBP,怀集:FAQ,恩施:ESN,恭城:GCZ,息县:ENN,息烽:XFW,悬钟:XRP,惠东:KDQ,惠农:HMJ,惠安:HNS,惠山:VCH,惠州:HCQ,惠州南:KNQ,惠州西:VXQ,惠环:KHQ,慈利:CUQ,成吉思汗:CJX,成都:CDW,成都东:ICW,成都南:CNW,成高子:CZB,戚墅堰:QYH,扎兰屯:ZTX,扎赉诺尔西:ZXX,扎鲁特:ZLD,打柴沟:DGJ,扬州:YLH,扶余:FYT,扶余北:FBT,扶绥:FSZ,承德:CDP,承德东:CCP,抚宁:FNP,抚州:FZG,抚州东:FDG,抚州北:FBG,抚松:FSL,抚远:FYB,抚顺:FST,抚顺北:FET,拉古:LGB,拉哈:LHX,拉林:LAB,拉萨:LSO,拉鲊:LEM,招柏:ZBP,换新天:VTB,揭阳:JRQ,攀枝花:PRW,攸县:YOG,攸县南:YXG,敖力布告:ALD,敖汉:YED,敦化:DHL,敦煌:DHJ,文地:WNZ,文安:WBP,文昌:WEQ,文水:WEV,文登:WBK,文登东:WGK,斜河涧:EEP,新乐:ELP,新乡:XXF,新乡东:EGF,新会:EFQ,新余:XUG,新余北:XBG,新保安:XAP,新兴县:XGQ,新化:EHQ,新化南:EJQ,新华:XHB,新华屯:XAX,新县:XSN,新友谊:EYB,新和:XIR,新坪田:XPM,新城子:XCT,新安:EAM,新安县:XAF,新帐房:XZX,新干:EGG,新晃:XLQ,新晃西:EWQ,新李:XLJ,新杖子:ERP,新松浦:XOB,新林:XPX,新民:XMD,新江:XJM,新沂:VIH,新津:IRW,新津南:ITW,新窝铺:EPD,新立屯:XLD,新立镇:XGT,新绛:XJV,新绰源:XRX,新肇:XZT,新邱:XQD,新郑机场:EZF,新都东:EWW,新阳镇:XZJ,新青:XQB,施家嘴:SHM,施秉:AQW,旅顺:LST,旌德:NSH,旗下营:QXC,旗下营南:QNC,无为:IIH,无锡:WXH,无锡东:WGH,无锡新区:IFH,日喀则:RKO,日照:RZK,旧庄窝:JVP,旬阳:XUY,旬阳北:XBY,旺苍:WEW,昂昂溪:AAX,昆山:KSH,昆山南:KNH,昆明:KMM,昆明南:KOM,昆明西:KXM,昆都仑召:KDC,昆阳:KAM,昌乐:CLK,昌图:CTT,昌图西:CPT,昌平:CPP,昌平北:VBP,昌黎:CLP,明光:MGH,明城:MCL,明水河:MUT,明港:MGN,明港东:MDN,明珠:MFQ,春湾:CQQ,春阳:CAL,昭化:ZHW,昭山:KWQ,昭通:ZDW,晋中:JZV,晋城:JCF,晋城北:JEF,晋州:JXP,晋江:JJS,晏城:YEK,晨明:CMB,普兰店:PLT,普宁:PEQ,普安:PAN,普安县:PUE,普定:PGW,普湾:PWT,普者黑:PZM,普雄:POW,景州:JEP,景德镇:JCG,景德镇北:JDG,景泰:JTJ,暖泉:NQJ,暮云:KIQ,曲水县:QSO,曲江:QIM,曲阜:QFK,曲阜东:QAK,曲靖:QJM,曲靖北:QBM,曹县:CXK,曹子里:CFP,曾口:ZKE,曾家坪子:ZBW,月亮田:YUM,月山:YBF,朔州:SUV,朗乡:LXB,望都:WDP,朝天:CTE,朝阳南:CYD,朝阳地:CDD,朝阳川:CYL,朝阳镇:CZL,木里图:MUD,本溪:BXT,本溪新城:BVT,本溪湖:BHT,朱家沟:ZUB,朱家窑:ZUJ,朱日和:ZRC,李家:LJB,李家坪:LIJ,李旺:VLJ,李石寨:LET,杏树:XSB,杏树屯:XDT,杜家:DJL,来宾:UBZ,来宾北:UCZ,来舟:LZS,杨岗:YRB,杨村:YBP,杨杖子:YZD,杨林:YLM,杨柳青:YQP,杨树岭:YAD,杨陵:YSY,杨陵南:YEY,杭州:HZH,杭州东:HGH,杭州南:XHH,杭锦后旗:HDC,松原:VYT,松原北:OCT,松山湖北:KUQ,松树:SFT,松树镇:SSL,松桃:MZQ,松江:SAH,松江南:IMH,松江河:SJL,松江镇:OZL,松河:SBM,松滋:SIN,板城:BUP,板塘:NGQ,林东:LRC,林口:LKB,林海:LXX,林源:LYX,林盛堡:LBT,林西:LXC,果松:GSL,枝城:ZCN,枝江北:ZIN,枣庄:ZEK,枣庄东:ZNK,枣庄西:ZFK,枣强:ZVP,枣林:ZIV,枣阳:ZYN,枫林:FLN,柏果:BGM,柞水:ZSY,查布嘎:CBC,查干湖:VAT,柳园:DHR,柳园南:LNR,柳州:LZZ,柳林南:LKV,柳树屯:LSD,柳江:UQZ,柳河:LNL,柴岗:CGT,柴沟堡:CGV,柴河:CHB,树木岭:FMQ,栟茶:FWH,株洲:ZZQ,株洲南:KVQ,株洲西:ZAQ,根河:GEX,格尔木:GRO,桂平:GAZ,桂林:GLZ,桂林北:GBZ,桂林西:GEZ,桃山:TAB,桃村:TCK,桃村北:TOK,桐乡:TCH,桐城:TTH,桐子林:TEW,桐柏:TBF,桐梓:TZW,桐梓东:TDE,桐梓北:TBE,桑园子:SAJ,桑根达来:OGC,桓台:VTK,桥头:QAT,桥西:QXJ,桦南:HNB,桦林:HIB,梁山:LMK,梁平:UQW,梁平南:LPE,梁底下:LDP,梅州:MOQ,梅河口:MHL,梧州:WZZ,梧州南:WBZ,梨树镇:LSB,棋子湾:QZQ,棕溪:ZOY,棠海:THM,楚山:CSB,楚雄南:COM,榆中:IZJ,榆林:ALY,榆树:YRT,榆树台:YUT,榆树屯:YSX,榆树沟:YGP,榆次:YCV,榆社:YSV,榕江:RVW,槐荫:IYN,樟木头:ZOQ,樟木头东:ZRQ,樟树:ZSG,樟树东:ZOG,横峰:HFG,横沟桥东:HNN,横道河子:HDB,歙县:OVH,歙县北:NPH,正定:ZDP,正定机场:ZHP,正镶白旗:ZXC,武义:RYH,武义北:WDH,武乡:WVV,武功:WGY,武夷山:WAS,武夷山东:WCS,武夷山北:WBS,武威:WUJ,武威南:WWJ,武安:WAP,武山:WSJ,武当山:WRN,武昌:WCN,武汉:WHN,武清:WWP,武穴:WXN,武胜:WSE,武陟:WIF,武隆:WLW,歪头山:WIT,毛坝:MBY,毛坝关:MGY,毛陈:MHN,民乐:MBJ,民和南:MNO,民权:MQF,民权北:MIF,水家湖:SQH,水富:OTW,水泉:SID,水洋:OYP,水洞:SIL,水源:OYJ,永丰营:YYM,永乐店:YDY,永修:ACG,永嘉:URH,永城北:RGH,永安:YAS,永安乡:YNB,永定:YGS,永寿:ASY,永川:YCW,永川东:WMW,永州:AOQ,永康:RFH,永康南:QUH,永泰:YTS,永济:YIV,永济北:AJV,永登:YDJ,永福南:YBZ,永郎:YLW,汉中:HOY,汉口:HKN,汉寿:VSQ,汉川:HCN,汉沽:HGP,汉源:WHW,汉阴:HQY,汐子:XZD,汕头:OTQ,汕尾:OGQ,汝州:ROF,汝箕沟:RQJ,汝阳:RYF,江华:JHZ,江宁:JJH,江宁西:OKH,江山:JUH,江所田:JOM,江桥:JQX,江永:JYZ,江油:JFW,江油北:JBE,江津:JJW,江源:SZL,江边村:JBG,江都:UDH,江门东:JWQ,池州:IYH,汤原:TYB,汤山城:TCT,汤旺河:THB,汤池:TCX,汤逊湖:THN,汤阴:TYF,汨罗:MLQ,汨罗东:MQQ,汪清:WQL,汾河:FEV,汾阳:FAV,沁县:QVV,沁阳:QYF,沂南:YNK,沂水:YUK,沃皮:WPT,沈丘:SQN,沈家:OJB,沈家河:OJJ,沈阳:SYT,沈阳东:SDT,沈阳北:SBT,沈阳南:SOT,沐滂:MPQ,沙县:SAS,沙后所:SSD,沙坡头:SFJ,沙坪坝:CYW,沙城:SCP,沙岭子:SLP,沙岭子西:IXP,沙桥:SQM,沙沱:SFM,沙河:SHP,沙河口:SKT,沙河市:VOP,沙海:SED,沙湾县:SXR,沟帮子:GBD,沥林北:KBQ,沧州:COP,沧州西:CBP,沭阳:FMH,河口北:HBM,河口南:HKJ,河唇:HCZ,河津:HJV,河源:VIQ,河边:HBV,河间西:HXP,治安:ZAD,沿河城:YHP,泉州:QYS,泉州东:QRS,泉阳:QYL,泊头:BZP,法启:FQE,泗县:GPH,泗水:OSK,泗洪:GQH,泗阳:MPH,泡子:POD,泥河子:NHD,泰和:THG,泰宁:TNS,泰安:TMK,泰山:TAK,泰州:UTH,泰康:TKX,泰来:TLX,泽普:ZPR,泽润里:ZLM,泾县:LOH,泾川:JAJ,洋县西:YXY,洋河:GTH,洛湾三江:KRW,洛门:LMJ,洛阳:LYF,洛阳东:LDF,洛阳龙门:LLF,洞井:FWQ,洞庙河:DEP,洪河:HPB,洪洞:HDV,洪洞西:HTV,洮南:TVT,流水沟:USP,济南:JNK,济南东:JAK,济南西:JGK,济宁:JIK,济源:JYF,浑河:HHT,浠水:XZN,浩良河:HHB,浮图峪:FYP,海东西:HDO,海伦:HLB,海北:HEB,海口:VUQ,海口东:HMQ,海坨子:HZT,海城:HCT,海城西:HXT,海宁:HNH,海宁西:EUH,海安县:HIH,海拉尔:HRX,海林:HRB,海湾:RWH,海石湾:HSO,海阳:HYK,海阳北:HEK,海龙:HIL,涉县:OEP,涞源:LYP,涟源:LAQ,涡阳:GYH,涪陵:FLW,涪陵北:FEW,涵江:HJS,涿州:ZXP,涿州东:ZAP,淄博:ZBK,淮北:HRH,淮北北:PLH,淮南:HAH,淮南东:HOH,淮安:AUH,淮安南:AMH,淮滨:HVN,深井子:SWT,深圳:SZQ,深圳东:BJQ,深圳北:IOQ,深圳坪山:IFQ,深圳西:OSQ,深州:OZP,清华园:QHP,清原:QYT,清徐:QUV,清水:QUJ,清水北:QEJ,清水县:QIJ,清河:QIP,清河城:QYP,清河门:QHD,清涧县:QNY,清远:QBQ,渑池:MCF,渑池南:MNF,渠县:QRW,渠旧:QJZ,渠黎:QLZ,温岭:VHH,温州:RZH,温州南:VRH,温春:WDB,渭南:WNY,渭南北:WBY,渭南南:WVY,渭南镇:WNJ,渭津:WJL,渭源:WEJ,湖口:HKG,湖州:VZH,湘乡:XXQ,湘府路:FVQ,湘潭:XTQ,湘潭北:EDQ,湛江:ZJZ,湛江西:ZWQ,湟源:HNO,湾沟:WGL,溆浦:EPQ,溆浦南:EMQ,源潭:YTQ,源迁:AQK,溧水:LDH,溧阳:LEH,滁州:CXH,滁州北:CUH,滕州:TXK,滕州东:TEK,满归:MHX,满洲里:MLX,滦县:UXP,滦平:UPP,滦河:UDP,滦河沿:UNP,滨州:BIK,滨江:BJB,滨海:FHP,滨海北:FCP,滴道:DDB,漠河:MVX,漫水湾:MKW,漯河:LON,漯河西:LBN,漳县:ZXJ,漳州:ZUS,漳州东:GOS,漳平:ZPS,漳浦:ZCS,潍坊:WFK,潘家店:PDP,潜江:QJN,潞城:UTP,潢川:KCN,潮州:CKQ,潮汕:CBQ,潮阳:CNQ,潼关:TGY,潼南:TVW,澄城:CUY,澧县:LEQ,濑湍:LVZ,濮阳:PYF,灌水:GST,火连寨:HLT,灯塔:DGT,灵丘:LVV,灵宝:LBF,灵宝西:LPF,灵武:LNJ,灵璧:GMH,灵石:LSV,灵石东:UDV,炎陵:YAG,烟台:YAK,烟台南:YLK,烟台西:YTK,烟筒屯:YUX,烟筒山:YSL,热水:RSD,焉耆:YSR,焦作:JOF,焦作东:WEF,照福铺:ZFM,熊岳城:XYT,燕子砭:YZY,燕家庄:AZK,燕山:AOP,燕岗:YGW,燕郊:AJP,牙克石:YKX,牙屯堡:YTZ,牛家:NJB,牛心台:NXT,牟平:MBK,牡丹江:MDB,犀浦:XIW,犀浦东:XAW,独山:RWW,独立屯:DTX,狮山:KSQ,狮山北:NSQ,狼尾山:LRJ,猛洞河:MUQ,玉屏:YZW,玉山:YNG,玉山南:YGG,玉林:YLZ,玉泉:YQB,玉溪:AXM,玉溪西:YXM,玉田县:ATP,玉石:YSJ,玉舍:AUM,玉门:YXJ,王兆屯:WZB,王团庄:WZJ,王安镇:WVP,王家湾:WJJ,王家营西:KNM,王岗:WGB,王府:WUT,王杨:WYB,王瞳:WTP,玛纳斯:MSR,玛纳斯湖:MNR,珞璜南:LNE,珠斯花:ZHD,珠海:ZHQ,珠海北:ZIQ,珠窝:ZOP,班猫箐:BNM,珲春:HUL,琼海:QYQ,瑞安:RAH,瑞昌:RCG,瑞昌西:RXG,瑞金:RJG,璧山:FZW,瓜州:GZJ,瓢儿屯:PRT,瓦屋山:WAH,瓦房店:WDT,瓦房店西:WXT,瓦拉干:WVX,瓦窑田:WIM,甘旗卡:GQD,甘河:GAX,甘泉:GQY,甘泉北:GEY,甘洛:VOW,甘草店:GDJ,甘谷:GGJ,田东:TDZ,田东北:TBZ,田师府:TFT,田心东:KQQ,田林:TFZ,田阳:TRZ,甲山:JOP,甸心:DXM,界首市:JUN,略阳:LYY,疏勒:SUR,疏勒河:SHJ,登沙河:DWT,白云鄂博:BEC,白城:BCT,白壁关:BGV,白奎堡:BKB,白山市:HJL,白旗:BQP,白水县:BGY,白水江:BSY,白水镇:BUM,白沙坡:BPM,白沙铺:BSN,白沟:FEP,白河:BEL,白河东:BIY,白河县:BEY,白泉:BQL,白洋淀:FWP,白涧:BAP,白狼:BAT,白石山:BAL,白芨沟:BJJ,白银市:BNJ,白银西:BXJ,白音他拉:BID,白音华南:FNC,白音察干:BYC,白音胡硕:BCD,白马井:BFQ,白鸡坡:BBM,百宜:FHW,百浪:BRZ,百色:BIZ,百里峡:AAP,皇姑屯:HTT,皋兰:GEJ,皮口:PUT,皮口南:PKT,皮山:PSR,益阳:AEQ,盐城:AFH,盐池:YKJ,盐津:AEW,盖州:GXT,盖州西:GAT,盘关:PAM,盘山:PUD,盘州:PAE,盘锦:PVD,盘锦北:PBD,盘龙城:PNN,眉山:MSW,眉山东:IUW,石人:SRL,石人城:SRB,石嘴山:QQJ,石坝:OBJ,石城:SCT,石头:OTB,石家庄:SJP,石家庄东:SXP,石家庄北:VVP,石山:SAD,石岘:SXL,石岭:SOL,石峡子:SXJ,石景山南:SRP,石林:SLM,石林南:LNM,石林西:SYM,石柱县:OSW,石桥:SQE,石桥子:SQT,石梯:STE,石河子:SZR,石泉县:SXY,石磷:SPB,石门县:OMQ,石门县北:VFQ,石龙:SLQ,砀山:DKH,砀山南:PRH,砚川:YYY,确山:QSN,碧江:BLQ,碧鸡关:BJM,碾子山:NZX,磁县:CIP,磁山:CSP,磁窑:CYK,磁西:CRP,磐安镇:PAJ,磐石:PSL,磨刀石:MOB,礼泉:LGY,祁东:QMQ,祁东北:QRQ,祁县:QXV,祁县东:QGV,祁家堡:QBT,祁门:QIH,祁阳:QWQ,祁阳北:QVQ,神头:SEV,神州:SRQ,神木:OMY,神树:SWB,神池:SMV,祥云:EXM,禄丰南:LQM,福利区:FLJ,福利屯:FTB,福安:FAS,福山口:FKP,福山镇:FZQ,福州:FZS,福州南:FYS,福泉:VMW,福海:FHR,福清:FQS,福田:NZQ,福鼎:FES,禹城:YCK,禹城东:YSK,离堆公园:INW,秀山:ETW,秦安:QGJ,秦家:QJB,秦家庄:QZV,秦岭:QLY,秦皇岛:QTP,秧草地:YKM,稷山:JVV,穆棱:MLB,窑上:ASP,立志:LZX,章丘:ZTK,章党:ZHT,章古台:ZGD,端州:WZQ,竹园坝:ZAW,笔架山:BSB,简阳:JYW,简阳南:JOW,米易:MMW,米沙子:MST,米脂:MEY,精河:JHR,精河南:JIR,索伦:SNT,索图罕:SHX,紫荆关:ZYP,紫阳:ZVY,綦江:QJW,綦江东:QDE,繁峙:FSV,繁昌西:PUH,红光镇:IGW,红兴隆:VHB,红安:HWN,红安西:VXN,红寺堡:HSJ,红山:VSB,红岘台:HTJ,红彦:VIX,红星:VXB,红果:HEM,红江:HFM,红砂岘:VSJ,红花沟:VHD,纪家沟:VJD,纳雍:NYE,纸坊东:ZMN,绅坊:OLH,织金:IZW,织金北:ZJE,绍兴:SOH,绍兴东:SSH,绍兴北:SLH,经棚:JPC,绕阳河:RHD,统军庄:TZP,绥中:SZD,绥中北:SND,绥化:SHB,绥德:ODY,绥棱:SIB,绥芬河:SFB,绥阳:SYB,绩溪北:NRH,绩溪县:JRH,绵阳:MYW,绿化:LWJ,绿博园:LCF,缙云:JYH,缙云西:PYH,罗城:VCZ,罗山:LRN,罗平:LPM,罗江:LJW,罗江东:IKW,罗源:LVS,羊堡:ABM,羊尾哨:YWM,羊者窝:AEM,羊臼河:YHM,羊草:YAB,美兰:MHQ,美溪:MEB,老城镇:ACQ,老府:UFD,老莱:LAX,老营:LXL,老边:LLT,耒阳:LYQ,耒阳西:LPQ,聊城:UCK,肃宁:SYP,肇东:ZDB,肇庆:ZVQ,肇庆东:FCQ,肥东:FIH,背荫河:BYB,胜芳:SUP,胶州:JXK,胶州北:JZK,能家:NJD,自贡:ZGW,舍力虎:VLD,舒兰:SLL,舒城:OCH,良各庄:LGP,艾家村:AJJ,艾河:AHP,芙蓉南:KCQ,芜湖:WHH,芦台:LTP,芦沟:LOM,芦溪:LUG,芦潮港:UCH,芨岭:JLJ,花园:HUN,花园口:HYT,花家庄:HJM,花山南:KNN,花桥:VQH,花棚子:HZM,花湖:KHN,芷江:ZPQ,苇子沟:WZL,苇河:WHB,苍南:CEH,苍溪:CXE,苍石:CST,苏家屯:SXT,苏尼特左旗:ONC,苏州:SZH,苏州北:OHH,苏州园区:KAH,苏州新区:ITH,英吉沙:YIR,英德:YDQ,英德西:IIQ,茂名:MDQ,茂名西:MMZ,茂林:MLD,茂舍祖:MOM,范家屯:FTT,范镇:VZK,茅岭:MLZ,茅草坪:KPM,茶卡:CVO,茶陵:CDG,茶陵南:CNG,荆州:JBN,荆门:JMN,草市:CSL,草河口:CKT,草海:WBW,荣成:RCK,荣昌:RCW,荣昌北:RQW,荷塘:KXQ,莆田:PTS,莎车:SCR,莒南:JOK,莒县:JKK,莫尔道嘎:MRX,莱芜东:LWK,莱芜西:UXK,莱西:LXK,莱西北:LBK,莱阳:LYK,莲江口:LHB,获嘉:HJF,菇园:GYL,菏泽:HIK,萍乡:PXG,萍乡北:PBG,营口:YKT,营口东:YGT,营城子:YCT,营山:NUW,营盘水:YZJ,营街:YAM,萧县北:QSH,萨拉齐:SLC,落坡岭:LPP,落垡:LOP,葛店南:GNN,葛根庙:GGT,葡萄菁:PTW,葫芦岛:HLD,葫芦岛北:HPD,葵潭:KTQ,蒙自:MZM,蒙自北:MBM,蒲城:PCY,蒲城东:PEY,蓝村:LCK,蓟州:JKP,蓬安:PAW,蓬莱市:POK,蔡家坡:CJY,蔡家沟:CJT,蔡山:CON,蔺家楼:ULK,蕲春:QRN,藁城:GEP,藁城南:GUP,藤县:TAZ,虎什哈:HHP,虎林:VLB,虎石台:HUT,虎门:IUQ,虞城县:IXH,虢镇:GZY,蚌埠:BBH,蚌埠南:BMH,蛟河:JHL,蛟河西:JOL,融安:RAZ,融水:RSZ,衡南:HNG,衡山:HSQ,衡山西:HEQ,衡水:HSP,衡水北:IHP,衡阳:HYQ,衡阳东:HVQ,衢州:QEH,裴德:PDB,褚家湾:CWJ,襄垣:EIF,襄汾:XFV,襄汾西:XTV,襄河:XXB,襄阳:XFN,襄阳东:XWN,西丰:XFT,西乌旗:XWC,西乡:XQY,西八里:XLP,西哲里木:XRD,西固:XIJ,西固城:XUJ,西大庙:XMP,西宁:XNO,西安:XAY,西安北:EAY,西安南:CAY,西小召:XZC,西岗子:NBB,西峡:XIF,西平:XPN,西平西:EGQ,西昌:ECW,西昌南:ENW,西林:XYB,西柳:GCT,西湖东:WDQ,西街口:EKM,西阳村:XQF,西麻山:XMB,观沙岭:FKQ,角美:JES,讷河:NHX,许三湾:XSJ,许家台:XTJ,许家屯:XJT,许昌:XCF,许昌东:XVF,诏安:ZDS,诸城:ZQK,诸暨:ZDH,读书铺:DPM,谢家镇:XMT,谭家井:TNJ,谷城:GCN,谷山:FFQ,豆庄:ROP,豆张庄:RZP,豆罗:DLV,贲红:BVC,贵安:GAE,贵定:GTW,贵定北:FMW,贵定南:IDW,贵定县:KIW,贵港:GGZ,贵溪:GXG,贵阳:GIW,贵阳东:KEW,贵阳北:KQW,费县:FXK,贺家店:HJJ,贺州:HXZ,贺胜桥东:HLN,贾鲁河:JLF,资中:ZZW,资中北:WZW,资溪:ZXS,资阳:ZYW,资阳北:FYW,赛汗塔拉:SHC,赣州:GZG,赤壁:CBN,赤壁北:CIN,赤峰:CFD,赤峰西:CID,赵光:ZGB,赵城:ZCV,赶水:GSW,赶水东:GDE,超梁沟:CYP,越西:YHW,路口铺:LKQ,车转湾:CWM,轩岗:XGV,轮台:LAR,辛集:ENP,辛集南:IJP,辰清:CQB,辰溪:CXQ,辽中:LZD,辽源:LYL,辽阳:LYT,达家沟:DJT,达州:RXW,达拉特旗:DIC,达拉特西:DNC,迁安:QQP,迎宾路:YFW,迎春:YYB,运城:YNV,运城北:ABV,运粮河:YEF,近海:JHD,进贤:JUG,进贤南:JXG,连云港:UIH,连云港东:UKH,连山关:LGT,连江:LKS,迤资:YQM,通化:THL,通化县:TXL,通北:TBB,通安驿:TAJ,通州:TOP,通州西:TAP,通沟:TOL,通海:TAM,通渭:TWJ,通辽:TLD,通远堡:TYT,通远堡西:TST,通途:TUT,通道:TRQ,遂宁:NIW,遂平:SON,遂溪:SXZ,道州:DFZ,道清:DML,道滘:RRQ,遵义:ZYE,遵义南:ZNE,遵义西:ZIW,邓州:DOF,邢台:XTP,邢台东:EDP,那曲:NQO,那罗:ULZ,那铺:NPZ,邯郸:HDP,邯郸东:HPP,邳州:PJH,邵东:FIQ,邵家堂:SJJ,邵武:SWS,邵阳:SYQ,邵阳北:OVQ,邹城:ZIK,郁南:YKQ,郑州:ZZF,郑州东:ZAF,郑州西:XPF,郓城:YPK,郫县:PWW,郫县西:PCW,郭家店:GDT,郭磊庄:GLP,郯城:TZK,郴州:CZQ,郴州西:ICQ,都匀:RYW,都匀东:KJW,都昌:DCG,都格:DMM,都江堰:DDW,鄂尔多斯:EEC,鄂州:ECN,鄂州东:EFN,鄄城:JCK,鄠邑:KXY,鄯善:SSR,鄯善北:SMR,鄱阳:PYG,酉阳:AFW,酒泉:JQJ,酒泉南:JNJ,醴陵:LLG,醴陵东:UKQ,里木店:LMB,重庆:CQW,重庆北:CUW,重庆南:CRW,重庆西:CXW,野三坡:AIP,金华:JBH,金华南:RNH,金坑:JKT,金城江:JJZ,金宝屯:JBD,金寨:JZH,金山北:EGH,金山屯:JTB,金州:JZT,金昌:JCJ,金月湾:PYQ,金杖子:JYD,金沟屯:VGP,金河:JHX,金银潭:JTN,金马村:JMM,钟家村:ZJY,钟山:ZSZ,钟山西:ZAZ,钟祥:ZTN,钦州:QRZ,钦州东:QDZ,铁力:TLB,铁厂:TCL,铁岭:TLT,铁岭西:PXT,铜仁:RDQ,铜仁南:TNW,铜陵:TJH,铜陵北:KXH,银川:YIJ,银浪:YJX,银滩:CTQ,银瓶:KPQ,锡林浩特:XTC,锦州:JZD,锦州南:JOD,锦河:JHB,锦界:JEY,镇城底:ZDV,镇安:ZEY,镇平:ZPF,镇江:ZJH,镇江南:ZEH,镇西:ZVT,镇赉:ZLT,镇远:ZUW,镜铁山:JVJ,长临河:FVH,长兴:CBH,长兴南:CFH,长农:CNJ,长冲:CCM,长坡岭:CPM,长垣:CYF,长城:CEJ,长寿:EFW,长寿北:COW,长寿湖:CSE,长山屯:CVT,长岭子:CLT,长庆桥:CQJ,长征:CZJ,长春:CCT,长春南:CET,长春西:CRT,长武:CWY,长汀:CES,长汀南:CNS,长汀镇:CDB,长沙:CSQ,长沙南:CWQ,长沙西:RXQ,长治:CZF,长治北:CBF,长甸:CDT,长葛:CEF,长阳:CYN,门源:MYO,闵集:MJN,闻喜:WXV,闻喜西:WOV,闽清:MQS,闽清北:MBS,阆中:LZE,阎良:YNY,阜南:FNH,阜宁:AKH,阜新南:FXD,阜阳:FYH,防城港北:FBZ,阳信:YVK,阳城:YNF,阳岔:YAL,阳平关:YAY,阳新:YON,阳明堡:YVV,阳春:YQQ,阳曲:YQV,阳朔:YCZ,阳泉:AQP,阳泉北:YPP,阳泉曲:YYV,阳澄湖:AIH,阳谷:YIK,阳邑:ARP,阳高:YOV,阿克苏:ASR,阿克陶:AER,阿勒泰:AUR,阿南庄:AZM,阿图什:ATR,阿城:ACB,阿尔山:ART,阿尔山北:ARX,阿巴嘎旗:AQC,阿房宫:EGY,阿拉山口:AKR,阿木尔:JTX,阿里河:AHX,阿金:AJD,阿龙山:ASX,陆丰:LLQ,陆川:LKZ,陆良:LRM,陇南:INJ,陇县:LXY,陇西:LXJ,陈官营:CAJ,陈相屯:CXT,陵城:LGK,陵水:LIQ,陶家屯:TOT,陶赖昭:TPT,隆化:UHP,隆安东:IDZ,隆昌:LCW,隆昌北:NWW,随州:SZN,雁翅:YAP,雁荡山:YGH,集宁南:JAC,集安:JAL,雨格:VTM,零陵:UWZ,雷州:UAQ,霍城:SER,霍尔果斯:HFR,霍州:HZV,霍州东:HWV,霍林郭勒:HWD,霍邱:FBH,霞浦:XOS,露水河:LUL,霸州:RMP,霸州西:FOP,青县:QXP,青城山:QSW,青堆:QET,青山:QSB,青岛:QDK,青岛北:QHK,青川:QCE,青州市:QZK,青田:QVH,青白江东:QFW,青神:QVW,青莲:QEW,青铜峡:QTJ,青龙:QIB,青龙山:QGH,靖宇:JYL,靖州:JEQ,靖西:JMZ,靖边:JIY,靖远:JYJ,靖远西:JXJ,静海:JHP,革居:GEM,鞍山:AST,鞍山西:AXT,韦庄:WZY,韩城:HCY,韩府湾:HXJ,韩麻营:HYP,韶关:SNQ,韶关东:SGQ,韶山:SSQ,韶山南:INQ,项城:ERN,顺义:SOP,顺德:ORQ,顺德学院:OJQ,顺昌:SCS,颍上:YVH,额济纳:EJC,风陵渡:FLV,饶平:RVQ,饶阳:RVP,首山:SAT,香兰:XNB,香坊:XFB,香樟路:FNQ,马三家:MJT,马兰:MLR,马林:MID,马桥河:MQB,马皇:MHZ,马莲河:MHB,马鞍山:MAH,马鞍山东:OMH,马龙:MGM,驻马店:ZDN,驻马店西:ZLN,驼腰岭:TIL,骆驼巷:LTJ,高台:GTJ,高台南:GAJ,高各庄:GGP,高安:GCG,高密:GMK,高山子:GSD,高州:GSQ,高平:GPF,高村:GCV,高桥镇:GZD,高楼房:GFM,高滩:GAY,高碑店:GBP,高碑店东:GMP,高花:HGD,高邑:GIP,高邑西:GNP,魏善庄:WSP,魏杖子:WKD,鲁山:LAF,鲁番:LVM,鲅鱼圈:BYT,鲘门:KMQ,鳌江:ARH,鸡东:JOB,鸡冠山:JST,鸡西:JXB,鸭园:YYL,鸳鸯镇:YYJ,鹤北:HMB,鹤壁:HAF,鹤壁东:HFF,鹤岗:HGB,鹤庆:HQM,鹤立:HOB,鹰手营子:YIP,鹰潭:YTG,鹰潭北:YKG,鹿寨:LIZ,鹿寨北:LSZ,鹿道:LDL,麓谷:BNQ,麦园:MYS,麻城:MCN,麻城北:MBN,麻尾:VAW,麻山:MAB,麻阳:MVQ,黄冈:KGN,黄冈东:KAN,黄冈西:KXN,黄口:KOH,黄土店:HKP,黄山:HKH,黄山北:NYH,黄州:VON,黄村:HCP,黄松甸:HDL,黄柏:HBL,黄梅:VEH,黄河景区:HCF,黄泥河:HHL,黄流:KLQ,黄瓜园:HYM,黄石:HSN,黄石东:OSN,黄石北:KSN,黄羊滩:HGJ,黄羊镇:HYJ,黄花筒:HUD,黄陵:ULY,黄陵南:VLY,黎城:UCP,黎塘:LTZ,黑井:HIM,黑冲滩:HCJ,黑台:HQB,黑旺:HWK,黑水:HOT,黑河:HJB,黔江:QNW,鼎湖东:UWQ,鼎湖山:NVQ,齐齐哈尔:QHX,齐齐哈尔南:QNB,龙丰:KFQ,龙井:LJL,龙华:LHP,龙南:UNG,龙口市:UKK,龙嘉:UJL,龙塘坝:LBM,龙山镇:LAS,龙岩:LYS,龙川:LUQ,龙市:LAG,龙江:LJX,龙沟:LGJ,龙泉寺:UQJ,龙洞堡:FVW,龙游:LMH,龙爪沟:LZT,龙里:LLW,龙里北:KFW,龙镇:LZA,龙骨甸:LGM";
        String[] stations = station_names.toString().split(",");
        String[] fromArr = from.split(",");
        String[] toArr = to.split(",");
        for(int i=0;i<stations.length;i++){
            String[] snames=stations[i].split(":");
            stationMap.put(snames[0],snames[1]);
            if(snames[0].contains(from)){
                fromList.add(snames[1]);
            }
            if (snames[0].contains(to)) {
                toList.add(snames[1]);
            }
        }

        logger.info(stationMap.size());
    }




    public void jiexi(List<String> checi,Map<String,Map<String,String>> map){
        map.clear();
        for(String a:checi){
            Map<String,String> map1= new ConcurrentHashMap<String,String>();
            String[] b = a.split("\\|");

            String secret =b[0];
            secret = URLDecoder.decode(secret);//解码
            String chehao=b[3];
            map1.put("二等",b[30]);
            map1.put("一等",b[31]);
            map1.put("硬卧",b[28]);
            map1.put("硬座",b[29]);
            map1.put("secret",secret);
            map1.put("leftTicket",b[12]);
            map1.put("train_no",b[2]);
            map1.put("fromStationTelecode",b[6]);
            map1.put("toStationTelecode",b[7]);
            map1.put("train_location",b[15]);
            map1.put("chehao",chehao);
            map.put(chehao,map1);
//            logger.info(map1);
           /* if(chehao.equals("G6704") || chehao.equalsIgnoreCase("G530")){
                logger.info(a);
            }*/
        }
    }

    public String getIp(){
        String ip="kyfw.12306.cn";
        int len = ips.length;
        Random random = new Random();
        int a=random.nextInt(len-1);
        return ips[a];
    }

    /**
     * 获取起点站code
     * @return
     */
    public String getFromCode(){
        String code ="";
        try{
            code=fromList.get(new Random().nextInt(fromList.size()));
        }catch (Exception e){
            logger.info("获取起点站code出错");
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 获取终点站code
     * @return
     */
    public String getToCode(){
        String code ="";
        try{
            code=toList.get(new Random().nextInt(toList.size()));
        }catch (Exception e){
            logger.info("获取终点站code出错");
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 获取车次str
     * @param map
     * @param trains
     * @param seats 二等 硬座等
     * @return
     */
    public  List< Map<String,String>> getSecretStr( Map<String,Map<String,String>> map,String trains,String seats){
        String[] che=trains.split(",");
        String[] xb=seats.split(",");
        List< Map<String,String>> youpiaoList=new ArrayList<Map<String,String>>();
        long time =System.currentTimeMillis()/1000;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime=simpleDateFormat.format(new Date(time*1000));
        for(String c:che){
            Map<String,String> xibiemap = map.get(c);
            if(null!=xibiemap){
                for(String xbie:xb){
                    String cnt=xibiemap.get(xbie);
                    String secretStr = xibiemap.get("secret");
                    if(null!=cnt && !"无".equals(cnt) && !"".equals(cnt)){
                        if("".equals(secretStr)){
                            logger.info(c+" "+xbie+"未开售");
                        }else  if(!"有".equals(cnt) && Integer.parseInt(cnt)<CommonUtil.user.split(",").length){
                            logger.info(c+" "+xbie+" 有票："+cnt+"但是不够"+CommonUtil.user+" "+CommonUtil.user.split(",").length+"个,忽略" );
                        }else{
                            logger.info(c+" "+xbie+" 有票："+cnt );
                            xibiemap.put("toBuySeat",xbie);
                            youpiaoList.add(xibiemap);
                        }
                    }else{
                        if(time%5==0){
                            logger.info(c+" "+date+"无票");
//                            logger.info(c+" "+dateTime+"无票");
                        }
                    }
                }

            }
        }
        return youpiaoList;
    }

    public String getSecretStr1( Map<String,Map<String,String>> map,String trains,String seats){
        String secretStr="";
        String[] che=trains.split(",");
        String[] xb=seats.split(",");
        for(String c:che){
            Map<String,String> xibiemap = map.get(c);
            if(null!=xibiemap){
                for(String xbie:xb){
                    String cnt=xibiemap.get(xbie);
                    if(null!=cnt && !"无".equals(cnt)){
                        logger.info(c+" "+xbie+" 有票："+cnt);
                        //secretStr =secretStr+xibie+checi
                        secretStr =xibiemap.get("secret");
                        secretStr=secretStr+","+xbie;
                        secretStr=secretStr+","+c;

                    }
                }

            }
        }
        return secretStr;
    }


    public String getToday(){
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return shortSdf.format(cal.getTime());
    }

    @Test
    public void tttt(){
        Random random = new Random();
        logger.info(random.nextInt(10));
        logger.info(getFromCode());
    }

    @Test
    public void testPath(){
       logger.info(CommonUtil.class.getClassLoader().getResource(""));
        logger.info(CommonUtil.class.getClassLoader().getResource("/"));
        logger.info(CommonUtil.class.getResource(""));
        logger.info(CommonUtil.class.getResource("/"));
    }

}
