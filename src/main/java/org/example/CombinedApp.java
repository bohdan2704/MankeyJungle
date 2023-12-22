package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.ModelLoadApp;
import org.example.OpenPageJavaFX;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class CombinedApp extends Application {

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostName());
        if(InetAddress.getLocalHost().getHostName().equals("DESKTOP-2LC2671")){
            try {
                if (KeyManager.checkKey()) {
                    launch(args);
                }
                else {
                    System.out.println("Access denied. Bad license key!");
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Access denied. Bad computer to launch!");
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Launch ModelLoadApp in the first window
        Stage modelLoadStage = new Stage();
        ModelLoadApp modelLoadApp = new ModelLoadApp();
        modelLoadApp.start(modelLoadStage);

        // Launch OpenPageJavaFX in the second window
        Stage openPageStage = new Stage();
        OpenPageJavaFX openPageJavaFX = new OpenPageJavaFX();
        openPageJavaFX.start(openPageStage);

        // Optionally, set up additional configurations for the primary stage
//        primaryStage.setTitle("Primary Stage");
//        primaryStage.show();
    }
}
