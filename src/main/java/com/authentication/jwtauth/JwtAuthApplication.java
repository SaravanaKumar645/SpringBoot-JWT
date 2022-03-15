package com.authentication.jwtauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class JwtAuthApplication {

    public static void main(String[] args) {

        SpringApplication.run(JwtAuthApplication.class, args);
    }
//    @Bean
//    CommandLineRunner commandLineRunner(UserRepository userRepository){
//        return args -> {
//            UserModel sarav=new UserModel(
//                    "Saravana Kumar",
//                    "sarav023b@gmail.com",
//                    "12345",
//                    "7639399279"
//            );
//            userRepository.save(sarav);
//        };
//    }
}
