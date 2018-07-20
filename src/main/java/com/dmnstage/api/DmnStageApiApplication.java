package com.dmnstage.api;

import com.dmnstage.api.entities.Admin;
import com.dmnstage.api.entities.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class DmnStageApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DmnStageApiApplication.class, args);
        /*String path;
        String pathFormat="api.dmnstage.com/img/#Category#/#Product#/#Subproduct#/#Year#-#Month#-#Day#.jpg";
        pathFormat=pathFormat.replace("#Category#",Subproduct.pro)*/


    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User A1 = new Admin("AbdellahASKI", "654321", "Abdellah@aski.me", "+212707970909", "Abdellah", "ASKI");

        Thread.sleep(6000);
        System.out.println(A1.toString());

    }
}
