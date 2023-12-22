package org.example;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class OpenPageJavaFX extends Application {

    public static void main(String[] args) {
        System.setProperty("prism.order", "sw");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        String model = String.valueOf(getClass().getResource("/web/index.html"));
        System.out.println(model);
        // Load a web page
        webView.getEngine().load(model);

        Scene scene = new Scene(webView, 800, 600);
        stage.setScene(scene);
        stage.setTitle("JavaFX WebView Example");
        stage.show();
    }
}
