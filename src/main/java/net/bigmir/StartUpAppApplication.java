package net.bigmir;

import net.bigmir.model.Friend;
import net.bigmir.model.SimpleUser;
import net.bigmir.model.StartUp;
import net.bigmir.model.UserRole;
import net.bigmir.services.SimpleUserService;
import net.bigmir.services.StartUpService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class StartUpAppApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        SpringApplication.run(StartUpAppApplication.class, args);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }


    @Bean
    public CommandLineRunner init(final SimpleUserService simpleUserService, final StartUpService startUpService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//              Users default creation
                SimpleUser petya = new SimpleUser("Petya Buhankin", "petya_smth@smth.ua", "+380981234567", "$2a$10$ZuNnngcU6gFzSBmuGqHGm..gtRmemf40s9aXpOkeumQfymeV.k3cm", null, UserRole.USER);
                SimpleUser sasha = new SimpleUser("Sasha Grona", "sasha_grona@bigmir.net", "+380990394853", "$2a$10$ZuNnngcU6gFzSBmuGqHGm..gtRmemf40s9aXpOkeumQfymeV.k3cm", null, UserRole.ADMIN);
                SimpleUser start = new SimpleUser("StartUpApp", "startupappteam@gmail.com", "+380961234567", "$2a$10$ZuNnngcU6gFzSBmuGqHGm..gtRmemf40s9aXpOkeumQfymeV.k3cm", null, UserRole.USER);


                Friend friendPetya = new Friend(petya);
                Friend friendSasha = new Friend(sasha);
                Friend friendStart = new Friend(start);

                sasha.addFriend(friendPetya);
                sasha.addFriend(friendStart);
                start.addFriend(friendSasha);

                simpleUserService.saveUser(petya);
                simpleUserService.saveUser(sasha);
                simpleUserService.saveUser(start);

                for (int i = 0; i < 30; i++) {
                    simpleUserService.saveUser(new SimpleUser("USER" + i, i + "petya_smth@smth.ua", null, null, null, UserRole.USER));
                }


                StartUp startUpSasha = new StartUp.StartUpBuilder()
                        .mergeByName("StartUpApp")
                        .addIdea("To create app making business-life easier")
                        .addStartCapital(Long.valueOf(10000))
                        .addLocation("Kyiv")
                        .build();


                StartUp startUpStart = new StartUp.StartUpBuilder()
                        .mergeByName("Just a business")
                        .addIdea("Just for testing")
                        .addStartCapital(Long.valueOf(100000))
                        .addLocation("New York")
                        .build();
                startUpService.createNewStartUp(startUpSasha, sasha);
                startUpService.createNewStartUp(startUpStart, start);
                simpleUserService.saveUser(sasha);
                simpleUserService.saveUser(start);

            }
        };
    }
}
