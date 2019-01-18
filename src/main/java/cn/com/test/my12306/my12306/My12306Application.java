package cn.com.test.my12306.my12306;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
public class My12306Application{// implements CommandLineRunner

	public static void main(String[] args) {
		SpringApplication.run(My12306Application.class, args);

	}


}



















	/*@Autowired
	ClientTicket ct;*/
//		SpringApplicationBuilder builder = new SpringApplicationBuilder(My12306Application.class);
//		builder.headless(true).web(true).run(args);

	/*@Override
	public void run(String... args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
		System.out.println("开始执行");
		ct.run();
        latch.await();
	}*/