package com.game_store;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class GameStore extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("./my_resources/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Game Hub");
        stage.setScene(scene);
        Image iconImage = new Image(getClass().getResourceAsStream("./my_resources/assets/logo.png"));
        stage.getIcons().add(iconImage);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ApiApplication.main(args);
        launch(args);
    }

}
