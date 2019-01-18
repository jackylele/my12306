package cn.com.test.my12306.my12306.core.util;

import cn.com.test.my12306.my12306.core.TicketHttpClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class ImageUtil {
	static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	static BASE64Decoder decoder = new sun.misc.BASE64Decoder();
	/**
	 * //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * @param imgFile
	 * @return
	 */
	public static String GetImageStr(String imgFile){
		InputStream in = null;
		byte[] data = null;
		//读取图片字节数组
		try{
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		}catch (IOException e){
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);//返回Base64编码过的字节数组字符串
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成图片
	 * @param base64str
	 * @param savepath
	 * @return
	 */
	public static boolean GenerateImage(String base64str,String savepath){
		if (base64str == null) //图像数据为空
			return false;
		// System.out.println("开始解码");
		BASE64Decoder decoder = new BASE64Decoder();
		try{
			//Base64解码
			byte[] b = decoder.decodeBuffer(base64str);
			//  System.out.println("解码完成");
			for(int i=0;i<b.length;++i){
				if(b[i]<0){//调整异常数据
					b[i]+=256;
				}
			}
			// System.out.println("开始生成图片");
			//生成jpeg图片
			OutputStream out = new FileOutputStream(savepath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		}catch (Exception e){
			return false;
		}
	}

	/**
	 * 将图片转换成二进制
	 * @return
	 */
	public static String getImageBinary(String imgFile){
		File f = new File(imgFile);
		BufferedImage bi;
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			byte[] bytes = baos.toByteArray();

			return encoder.encodeBuffer(bytes).trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] getImageBinary1(String imgFile){
		File f = new File(imgFile);
		BufferedImage bi;
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			byte[] bytes = baos.toByteArray();

			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换为图片
	 * @param base64String
	 */
	public static void base64StringToImage(String base64String){
		try {
			byte[] bytes1 = decoder.decodeBuffer(base64String);

			ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
			BufferedImage bi1 =ImageIO.read(bais);
			File w2 = new File("e://QQ.jpg");//可以是jpg,png,gif格式
			ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void download(String urlString, String filename,String savePath) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		//设置请求超时为5s
		con.setConnectTimeout(5*1000);
		// 输入流
		InputStream is = con.getInputStream();
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf=new File(savePath);
		if(!sf.exists()){
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}



	public static void  main1(String[] args){
		String base64 = ImageUtil.GetImageStr("F:\\临时\\pic\\1.jpg");
		CloseableHttpResponse response=null;
		JsonBinder jsonBinder = JsonBinder.buildNonNullBinder(false);
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore)
				.build();
		try {
			/*HttpUriRequest checkCode = RequestBuilder.post()
					.setUri(new URI("http://ec2-18-191-165-87.us-east-2.compute.amazonaws.com:8002"))
					.addParameter("name", "hello_123")
					.addParameter("password", "hello_123")
					.addParameter("imgData", base64)
					.build();*/
			HttpUriRequest checkCode = RequestBuilder.post()
					.setUri(new URI("http://123.57.138.40:9443/12306/code"))
					.addParameter("user", "111")
					.addParameter("key", "123456")
//					.addParameter("file", base64)
					.addParameter("file", ImageUtil.getImageBinary("F:\\临时\\pic\\1.jpg"))
					.build();
			response = httpclient.execute(checkCode);

			Map<String, Object> rsmap = null;

			HttpEntity entity = response.getEntity();
			String responseBody = EntityUtils.toString(entity);
			rsmap = jsonBinder.fromJson(responseBody, Map.class);
			System.out.println(rsmap);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 识别图片验证码
	 * @param savePath 存放路径
	 * @param imageName 图片名称
	 * @return
	 */
	public static String shibie(String savePath,String imageName){
		String rs ="";
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost uploadFile = new HttpPost("http://123.57.138.40:9443/12306/code");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//			builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
			ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
			builder.addTextBody("user", "111", contentType);
			builder.addTextBody("key", "123456", contentType);
			// 把文件加到HTTP的post请求中
//			File f = new File("F:\\临时\\pic\\1.jpg");
			File f = new File(savePath+File.separatorChar+imageName);
			builder.addBinaryBody("file",new FileInputStream(f),ContentType.APPLICATION_OCTET_STREAM,f.getName());
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String sResponse=EntityUtils.toString(responseEntity, "UTF-8");
			System.out.println("Post 返回结果"+sResponse);
			rs = sResponse;
		}catch (Exception e){
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 识别图片验证码
	 * @param imageBase64 图片的base64
	 * @return 识别成功的验证码 可以直接用于校验
	 */
	public static String shibie360(String imageBase64){
		JsonBinder jsonBinder = JsonBinder.buildNonNullBinder(false);
		String rs ="";
		try {

			String token ="";
			String check ="";
			StringEntity stringEntity = new StringEntity("{\"base64\":\""+imageBase64+"\"}", "application/json", "utf-8");
			HttpUriRequest checkCode = RequestBuilder.post()
					.setUri(new URI("http://60.205.200.159/api")).setEntity(stringEntity)
					.build();
			CloseableHttpClient httpClient= TicketHttpClient.getClient();
			CloseableHttpResponse response = httpClient.execute(checkCode);
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "UTF-8");
			System.out.println(content);
			Map<String, Object> rsmap = jsonBinder.fromJson(content, Map.class);
			if(String.valueOf(rsmap.get("success")).equals("true")){
				token =String.valueOf(rsmap.get("token"));
				check = String.valueOf(rsmap.get("check"));
			}

			Header header0 = new BasicHeader("Host","check.huochepiao.360.cn");
			Header header1 = new BasicHeader("Upgrade-Insecure-Requests","1");
			Header header2 = new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36 QIHU 360EE");
//			Header header3 = new BasicHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			Header header3 = new BasicHeader("Content-Type","application/json;charset=UTF-8");
			int a=1;
			stringEntity = new StringEntity("{\"check\":\""+check+"\",\"img_buf\":\""+imageBase64+"\",\"logon\":"+a+",\"type\":\"D\"}","utf-8");
			stringEntity.setContentType("application/json;charset=UTF-8");
//			stringEntity.setContentEncoding("UTF-8");
			HttpUriRequest getVcode = RequestBuilder.post()
					.setUri(new URI("http://check.huochepiao.360.cn/img_vcode"))
					.setEntity(stringEntity)
					.addHeader(header0).addHeader(header1).addHeader(header2).addHeader(header3)
					.build();
			CloseableHttpResponse response1 = httpClient.execute(getVcode);
			HttpEntity entity1 = response1.getEntity();
			String content1 = EntityUtils.toString(entity1, "UTF-8");
			System.out.println(content1);
			Map<String, Object> rsmap1 = jsonBinder.fromJson(content1, Map.class);
			Object res = rsmap1.get("res");

			if(!StringUtils.isEmpty(res)){
				rs = res.toString().replaceAll("\\),\\(",",").replaceAll("\\)","").replaceAll("\\(","");
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 将验证码结果转换成坐标
	 * @param resStr
	 * @return
	 */
	public static String getZuobiao(String resStr){
		JsonBinder jsonBinder = JsonBinder.buildNonNullBinder(false);
		Map<String, Object> rsmap = null;
		String zuobiao ="";
		try {
			int baseX = 10,baseY=35;
			//{"time": "6.638509s", "result": [2, 6], "msg": "success", "code": 0, "text": ["雨靴"]}
			rsmap = jsonBinder.fromJson(resStr, Map.class);

			if(null!=rsmap && (rsmap.get("code")+"").equals("0")){
				String zb = rsmap.get("result")+"";
				if(!"".equals(zb) && zb.contains("[")){
					zb = zb.replace("[","").replace("]","").replaceAll(" ","");
					String[] zbArr = zb.split(",");
					for(String z:zbArr){
						int x1= (int) (Math.random() * 5 + 1);
						int y1= (int) (Math.random() * 5 + 1);
						int z1=Integer.parseInt(z);
						zuobiao += baseX+(z1%4==0?4-1:z1%4-1)*70+x1+",";
						zuobiao+= baseY+ Integer.parseInt(z)/5*70+y1;
						zuobiao+=",";
					}

				}
			}
			if(zuobiao.endsWith(",")){
				zuobiao=zuobiao.substring(0,zuobiao.length()-1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zuobiao;
	}

	public static String test2(){
		String rs ="";
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost uploadFile = new HttpPost("http://123.57.138.40:9443/12306/code");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//			builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
			ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
			builder.addTextBody("name", "hello_123", contentType);
			builder.addTextBody("password", "hello_123", contentType);
			builder.addTextBody("imgData", "hello_123", contentType);
			// 把文件加到HTTP的post请求中
//			File f = new File("F:\\临时\\pic\\1.jpg");
//			builder.addBinaryBody("file",new FileInputStream(f),ContentType.APPLICATION_OCTET_STREAM,f.getName());
//			HttpEntity multipart = builder.build();
//			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String sResponse=EntityUtils.toString(responseEntity, "UTF-8");
			System.out.println("Post 返回结果"+sResponse);
			rs = sResponse;

		}catch (Exception e){
			e.printStackTrace();
		}
		return rs;
	}

	public static void  main(String[] args){
//		ImageUtil.shibie("F:\\临时\\pic","1.jpg");
//		String abc="a+1+b++c";
//		System.out.println(abc.replaceAll("\\+"," "));
		ImageUtil.shibie360("");
		test2();
	}


}