package team_2p4p.mes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MesApplication {

	public static void main(String[] args) {

		SpringApplication.run(MesApplication.class, args);
	}

}
