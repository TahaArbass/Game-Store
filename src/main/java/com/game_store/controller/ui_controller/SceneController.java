package com.game_store.controller.ui_controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

    private static Stage stage;
    private static Scene scene;

    public static void switchToScene(ActionEvent event, Parent root) throws IOException {
        Platform.runLater(() -> {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true); // Maximize the stage
            stage.show();
        });
    }
}

