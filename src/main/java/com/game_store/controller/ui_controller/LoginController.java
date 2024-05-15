package com.game_store.controller.ui_controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.game_store.controller.ui_controller.UserHomeController;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game_store.model.Publisher;
import com.game_store.model.User;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private CheckBox publisherCB;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField usernameTF;

    @FXML
    private PasswordField passwordPF;

    @FXML
    private Label statusLabel;

    private final WebClient webClient = WebClient.create("http://localhost:8080/api/");

    @FXML
    void handleMouseEnter() {
        // white bg + black txt
        setBackground("#FFFFFF", "#000000");
        System.out.println("You entered me, change bg to white!");
    }

    @FXML
    void handleMouseExit() {
        // green bg + black txt
        setBackground("#00FF00", "#000000");
        System.out.println("You exited me, change bg to green!");
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameTF.getText();
        String password = passwordPF.getText();

        // Check if publisher checkbox is selected
        if (publisherCB.isSelected()) {
            loginPublisher(username, password, event);
        } else {
            loginUser(username, password, event);
        }
    }

    private void loginPublisher(String username, String password, ActionEvent event) {
        Publisher publisher = new Publisher(username, password);
        login("/publishers/login", publisher, event, "/com/game_store/my_resources/PublisherHome.fxml");
    }

    private void loginUser(String username, String password, ActionEvent event) {
        User user = new User(username, password);
        login("/users/login", user, event, "/com/game_store/my_resources/UserHome.fxml");
    }

    private <T> void login(String uri, T body, ActionEvent event, String fxmlPath) {
        webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(convertObjectToJson(body)))
                .retrieve()
                .onStatus(httpStatus -> httpStatus == HttpStatus.UNAUTHORIZED, response -> {
                    showAlert("Login failed", "Incorrect username or password!!! Try Again.");
                    return null;
                })
                .bodyToMono(String.class)
                .subscribe(response -> {
                    try {
                        // debugging and seeing the request body
                        System.out.println("Request Body: " + convertObjectToJson(body));
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                        Parent root = loader.load();
                        SceneController.switchToScene(event, root);
                        
                        // if user is publisher, set up the page with the username
                        if(body instanceof Publisher) {
                            PublisherHomeController controller = loader.getController();
                            controller.useUsernameToSetUpPage(usernameTF.getText());
                        }
                        else {
                            UserHomeController controller = loader.getController();
                            controller.useUsernameToSetUpPage(usernameTF.getText());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private String convertObjectToJson(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setBackground(new Background(new BackgroundFill(Color.web("#000000"), null, null)));

            // Set text color to green
            Label contentLabel = (Label) alert.getDialogPane().lookup(".content.label");
            contentLabel.setTextFill(Color.web("#00FF00"));
            contentLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

            // Get the ok button and set its text color to black and background color to
            // green
            ButtonType okButtonType = alert.getButtonTypes().stream()
                    .filter(buttonType -> buttonType.getButtonData().isDefaultButton())
                    .findFirst().orElse(null);
            if (okButtonType != null) {
                Button okButton = (Button) alert.getDialogPane().lookupButton(okButtonType);
                okButton.setTextFill(Color.BLACK);
                okButton.setFont(Font.font("System", FontWeight.BOLD, 14));
                okButton.setBackground(new Background(new BackgroundFill(Color.web("#00FF00"), null, null)));
            }

            alert.showAndWait();
        });
    }

    @FXML
    void handleSignUpButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/game_store/my_resources/SignUp.fxml"));
        Parent root = loader.load();
        SceneController.switchToScene(event, root);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setBackground(String bgColor, String textColor) {
        BackgroundFill bgFill = new BackgroundFill(Color.web(bgColor), null, null);
        loginBtn.setBackground(new Background(bgFill));
        loginBtn.setTextFill(Color.web(textColor));
    }
}
