package org.example;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;

public class ModelLoadApp extends Application {
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int CIRCLE_RADIUS = 1;
    private static final Duration TRANSLATION_DURATION = Duration.seconds(10);
    private static final Duration CYCLE_DELAY = Duration.seconds(1);
    private double translationDistance = 1; // Adjust as needed

    private Scene createScene() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-35);
        camera.setFarClip(1000);
        Group model = loadModel(getClass().getResource("Gorilla_furious.OBJ"));
        model.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        model.getTransforms().add(new Translate(0, 0, 9));


        Circle movingCircle = new Circle(CIRCLE_RADIUS, Color.BLUE);
        movingCircle.setLayoutY(-SCREEN_HEIGHT/2);

        Group root = new Group();
        root.getChildren().add(prepareImageView());
        root.getChildren().add(model);
        root.getChildren().add(movingCircle);

        Scene scene = new Scene(root, 1080, 720, true);
        //        scene.setFill(Color.GREENYELLOW);
        scene.setCamera(camera);
        // Add translation animation based on key events
        addTranslationAnimation(model, scene);

        // Create a translation animation for the circle
        TranslateTransition translateTransition = new TranslateTransition(TRANSLATION_DURATION, movingCircle);
        translateTransition.setByY(SCREEN_HEIGHT - 2 * CIRCLE_RADIUS);

        // Create a timeline to introduce a delay between cycles
        Timeline timeline = new Timeline(
                new KeyFrame(CYCLE_DELAY, e -> {
                    translateTransition.setFromY(-2 * CIRCLE_RADIUS);
                    translateTransition.setToY(SCREEN_HEIGHT - 2 * CIRCLE_RADIUS);
                    translateTransition.playFromStart();
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        // Start the translation animation
        translateTransition.play();

        // Start the timeline for cycle delays
        timeline.play();
        return scene;
    }

    private ImageView prepareImageView() {
        Image image = new Image(getClass().getResourceAsStream("/jungle_background.jpg"));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2, -image.getHeight() / 2, 690));
        return imageView;
    }

    private Group loadModel(URL url) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(createScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addTranslationAnimation(Group model, Scene scene) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(20), model);

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
