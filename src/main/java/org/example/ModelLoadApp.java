package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class ModelLoadApp extends Application {
    List<String> jungleCities = Arrays.asList(
            "Manaus, Brazil",
            "Puerto Maldonado, Peru",
            "Leticia, Colombia",
            "Cairns, Australia",
            "Iquitos, Peru",
            "Kota Kinabalu, Malaysia",
            "Kisangani, Democratic Republic of the Congo",
            "Madang, Papua New Guinea",
            "La Ceiba, Honduras",
            "Liberia, Costa Rica"
    );
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 800;
    private static final double CIRCLE_RADIUS = 1;
    private static final Duration TRANSLATION_DURATION = Duration.seconds(10);
    private double translationDistance = 0.5; // Adjust as needed

    Scene createScene() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-35);
        camera.setFarClip(1000);
        Group model = loadModel(getClass().getResource("Gorilla_furious.OBJ"));
        model.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        model.getTransforms().add(new Translate(0, 0, 9));

        Text label = new Text();
        label.setTranslateZ(650);
        label.setTranslateY(-150);
        label.setTranslateX(-120);
        Timer timer = new Timer();
        // Schedule the task to run every minute
        // DEFENCE, LOGO
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        Random random = new Random();
                        String t = jungleCities.get((random.nextInt(jungleCities.size())));
                        String n = t.split(", ")[0];
                        String weather = OpenWeatherApi.getWeather(n);
                        System.out.println(weather);
                        // Parse the JSON string
                        JsonObject jsonObject = JsonParser.parseString(weather).getAsJsonObject();

                        // Extract specific values
//                        JsonObject weatherTag = jsonObject.get("weather").getAsJsonObject();
//                        String weatherSituation = weatherTag.get("main").getAsString();
//                        String weatherDescryption = weatherTag.get("description").getAsString();
//                        String city = jsonObject.get("name").getAsString();
//
//                        JsonObject mainTag = jsonObject.get("main").getAsJsonObject();
//                        String temp = mainTag.get("temp").getAsString();
//                        String tempFeelsLike = mainTag.get("feels_like").getAsString();

                        String cityName = jsonObject.get("name").getAsString();

                        // Extract weather information
                        JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
                        JsonObject weatherObject = weatherArray.get(0).getAsJsonObject(); // Assuming there is at least one element in the array

                        String mainWeather = weatherObject.get("main").getAsString();
                        String weatherDescription = weatherObject.get("description").getAsString();

                        // Extract main information
                        JsonObject mainObject = jsonObject.getAsJsonObject("main");
                        double temperature = mainObject.get("temp").getAsDouble()/10;
                        double temperatureFeelsLike = mainObject.get("feels_like").getAsDouble()/10;

                        // Now you can use cityName, mainWeather, weatherDescription, temperature, and temperatureFeelsLike
                        System.out.println("City: " + cityName);
                        System.out.println("Main Weather: " + mainWeather);
                        System.out.println("Weather Description: " + weatherDescription);
                        System.out.println("Temperature: " + temperature);
                        System.out.println("Feels Like Temperature: " + temperatureFeelsLike);

                        // Print the extracted values
                        StringBuilder b = new StringBuilder();
                        b.append("City Name: ").append(cityName).append(System.lineSeparator());
                        b.append("Weather: ").append(mainWeather).append(System.lineSeparator());
                        b.append("Weather Description: ").append(weatherDescription).append(System.lineSeparator());
                        b.append("Temperature: ").append(String.format("%.2f", temperature)).append(" C° -- ").append(String.format("%.2f", temperatureFeelsLike)).append(" C°").append(System.lineSeparator());
                        label.setText(b.toString());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 60 * 1000);

        Group root = new Group();
        root.getChildren().add(prepareImageView());
        root.getChildren().add(model);
        root.getChildren().add(label);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> {
                    createAndAnimateCircles(root);
                })
        );
        timeline.setCycleCount(500);
        timeline.play();

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT, true);
        scene.setCamera(camera);
        addTranslationAnimation(model, scene);

//        Timer timer = new Timer();
//
//        // Schedule the task to run every minute
//        timer.scheduleAtFixedRate(new WeatherApiTask(), 0, 60 * 1000); // 60 seconds * 1000 milliseconds
        return scene;
    }

    ImageView prepareImageView() {
        Image image = new Image(getClass().getResourceAsStream("/jungle_background.jpg"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2, -image.getHeight() / 2, 690));
        return imageView;
    }

    private void createAndAnimateCircles(Group root) {
        double v = Math.random() * SCREEN_WIDTH - SCREEN_WIDTH / 2;
//        System.out.println("X: " + v);
        Circle movingCircle = new Circle(CIRCLE_RADIUS, Color.YELLOW);
        movingCircle.setTranslateX(v);
        movingCircle.setTranslateY(-SCREEN_HEIGHT / 2);
        TranslateTransition translateTransition = new TranslateTransition(TRANSLATION_DURATION, movingCircle);
        translateTransition.setByY(SCREEN_HEIGHT);
        translateTransition.setCycleCount(Animation.INDEFINITE);
        translateTransition.play();
        root.getChildren().add(movingCircle);
    }

    Group loadModel(URL url) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }

    private void carryOutMusic(Stage primaryStage) {
        String audioFilePath = getClass().getResource("/kingOfTheBongo.mp3").toString();
        Media media = new Media(audioFilePath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    mediaPlayer.stop();
                    mediaPlayer.play();
                    break;
            }
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        carryOutMusic(stage);
        stage.setScene(createScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addTranslationAnimation(Group model, Scene scene) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1), model);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) {
                translateTransition.setToX(model.getTranslateX() - translationDistance);
                translateTransition.play();
            } else if (event.getCode() == KeyCode.D) {
                translateTransition.setToX(model.getTranslateX() + translationDistance);
                translateTransition.play();
            }
        });
    }
}
