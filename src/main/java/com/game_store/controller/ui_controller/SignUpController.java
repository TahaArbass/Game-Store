package com.game_store.controller.ui_controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game_store.model.Publisher;
import com.game_store.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import reactor.core.publisher.Mono;

/**
 * FXML Controller class
 *
 * @author Evolution IV
 */
public class SignUpController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private CheckBox publisherCB;

    @FXML
    private Button signUpBtn;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField usernameTF;

    @FXML
    private PasswordField passwordPF;

    @FXML
    private Label statusLabel;

    private final WebClient webClient = WebClient.create("http://localhost:8080/api");

    @FXML
    private void handleSignUpButtonAction(ActionEvent event) {
        String username = usernameTF.getText();
        String password = passwordPF.getText();
        String email = emailTF.getText();

        if (publisherCB.isSelected()) {
            signUpPublisher(username, password, email, event);
        } else {
            signUpUser(username, password, email, event);
        }
    }

    private void signUpPublisher(String username, String password, String email, ActionEvent event) {
        Publisher publisher = new Publisher(username, email, password);
        signUp("/publishers/signup", publisher, event, "/com/game_store/my_resources/PublisherHome.fxml");
    }

    private void signUpUser(String username, String password, String email, ActionEvent event) {
        User user = new User(username, email, password);
        signUp("/users/signup", user, event, "/com/game_store/my_resources/UserHome.fxml");
    }

    private <T> void signUp(String uri, T body, ActionEvent event, String fxmlPath) {
        String jsonBody = convertObjectToJson(body);
        System.out.println("Request Body: " + jsonBody);

        webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonBody))
                .retrieve()
                .onStatus(httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
                        response -> response.bodyToMono(String.class).flatMap(errorBody -> {
                            showAlert("Registration failed", errorBody);
                            return Mono.empty();
                        }))
                .onStatus(httpStatus -> httpStatus == HttpStatus.UNAUTHORIZED, response -> {
                    showAlert("Unauthorized", "You are not authorized to perform this action.");
                    return Mono.empty();
                })
                .bodyToMono(User.class)
                .subscribe(createdUser -> {
                    try {
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
                        showAlert("Error", "Error occurred while signing up.");
                        e.printStackTrace();
                    }
                }, error -> {
                    showAlert("Error", "Error occurred while signing up.");
                    error.printStackTrace();
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

    @FXML
    void handleLoginButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./../../my_resources/Login.fxml"));
        Parent root = loader.load();
        SceneController.switchToScene(event, root);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
}
