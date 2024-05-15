package com.game_store.controller.ui_controller;

import com.game_store.model.Game;
import com.game_store.model.Purchase;
import com.game_store.model.User;
import com.game_store.model.UserAchievement;
import com.game_store.service.GameService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Optional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class UserHomeController implements Initializable {

    @FXML
    private Label welcomeLabel;

    @FXML
    private FlowPane gamesFlowPane;

    @FXML
    private TextField searchField;

    private final WebClient webClient = WebClient.create("http://localhost:8080/api");

    public void useUsernameToSetUpPage(String username) {
        Platform.runLater(() -> {
            welcomeLabel.setText("Welcome " + username);
        });
    }

    @FXML
    private void fillGamesFlowPane() {
        webClient.get()
                .uri("/games")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Game>>() {
                })
                .subscribe(
                        games -> Platform.runLater(() -> displayGames(games)),
                        throwable -> showErrorAlert("Error", "Failed to fetch games. Please try again later."));
    }

    @FXML
    private void handleLibraryButtonAction() {
        String username = welcomeLabel.getText().replace("Welcome ", "");

        webClient.get()
                .uri("/users/username/{username}", username)
                .retrieve()
                .bodyToMono(User.class)
                .flatMap(user -> {
                    int userId = user.getUserID();
                    return webClient.get()
                            .uri("/purchases/user/{userId}", userId)
                            .retrieve()
                            .bodyToFlux(Purchase.class)
                            .flatMap(purchase -> {
                                int gameId = purchase.getGame().getGameId();
                                return webClient.get()
                                        .uri("/games/{gameId}", gameId)
                                        .retrieve()
                                        .bodyToMono(Game.class);
                            })
                            .collectList();
                })
                .subscribe(
                        games -> Platform.runLater(() -> displayLibraryGames(games)),
                        throwable -> Platform.runLater(
                                () -> showErrorAlert("Error", "Failed to fetch games. Please try again later.")));
    }

    @FXML
    private void handleLogoutActionButton(ActionEvent event) throws IOException {
        // Handle logout action
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/game_store/my_resources/Login.fxml"));
        Parent root = loader.load();
        SceneController.switchToScene(event, root);

    }

    @FXML
    private void handleSearchActionButton() {
        String searchQuery = searchField.getText();
        if (!searchQuery.isEmpty()) {
            webClient.get()
                    .uri("/games/search?query={query}", searchQuery)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Game>>() {
                    })
                    .subscribe(
                            games -> Platform.runLater(() -> displayGames(games)),
                            throwable -> showErrorAlert("Error", "Failed to search games. Please try again later."));
        } else {
            // Show an alert if the search query is empty
            showErrorAlert("Error", "Please enter a search query.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(this::fillGamesFlowPane);
    }

    // Display games in flow pane
    private void displayGames(List<Game> games) {
        Platform.runLater(() -> {
            gamesFlowPane.getChildren().clear();

            for (Game game : games) {
                Label titleLabel = new Label(game.getTitle());
                titleLabel.setTextFill(Color.web("#00FF00"));
                titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

                Label priceLabel = new Label(game.getPrice() + " $");
                priceLabel.setTextFill(Color.web("#00FF00"));
                priceLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

                // Create a horizontal box to hold the title and price labels
                HBox titlePriceBox = new HBox(titleLabel, priceLabel);
                titlePriceBox.setAlignment(Pos.CENTER);
                titlePriceBox.setSpacing(50);

                // Load the image of the game
                String fileName = game.getTitle().replaceAll("\\s", "") + ".png";
                InputStream inputStream = getClass()
                        .getResourceAsStream("/com/game_store/my_resources/assets/" + fileName);

                Image image;
                if (inputStream != null) {
                    image = new Image(inputStream);
                } else {
                    // Provide a default image when resource is not found
                    image = new Image("/com/game_store/my_resources/assets/default_image.png");
                }
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(250);

                VBox gameInfo = new VBox(imageView, titlePriceBox);
                gameInfo.setAlignment(Pos.CENTER);
                gameInfo.setSpacing(15);

                gameInfo.setOnMouseClicked(event -> {
                    // Handle click event here
                    showGameDetailsAlert(game);
                });

                gamesFlowPane.getChildren().add(gameInfo);
            }
        });
    }

    // Show game details alert
    private void showGameDetailsAlert(Game game) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Details");
        alert.setHeaderText(null);
        alert.setContentText("Title: " + game.getTitle() + "\n\n" +
                "Genre: " + game.getGenre() + "\n\n" +
                "Description: " + game.getDescription() + "\n\n" +
                "Publisher: " + game.getPublisher().getUsername() + "\n\n" +
                "Price: " + game.getPrice() + " $");

        // set the image of the game as the graphic of the alert
        InputStream inputStream = getClass()
                .getResourceAsStream("/com/game_store/my_resources/assets/" + game.getTitle() + ".png");
        Image image;
        if (inputStream != null) {
            image = new Image(inputStream);
        } else {
            // Provide a default image when resource is not found
            image = new Image("/com/game_store/my_resources/assets/default_image.png");
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(250);
        alert.setGraphic(imageView);

        // Set dialog pane background color to black
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setBackground(new Background(new BackgroundFill(Color.web("#000000"), null, null)));
        Label contentLabel = (Label) alert.getDialogPane().lookup(".content.label");
        contentLabel.setTextFill(Color.web("#00FF00"));
        contentLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        // Get the buy button and set its text color to black and background color to
        // green
        ButtonType buyButton = new ButtonType("Buy");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buyButton, cancelButton);

        // Set text color to green and text to black for buy button
        Button buyButtonNode = (Button) alert.getDialogPane().lookupButton(buyButton);
        buyButtonNode.setTextFill(Color.BLACK);
        buyButtonNode.setFont(Font.font("System", FontWeight.BOLD, 14));
        buyButtonNode.setBackground(new Background(new BackgroundFill(Color.web("#00FF00"), null, null)));

        // Set text color to green and text to black for cancel button
        Button cancelButtonNode = (Button) alert.getDialogPane().lookupButton(cancelButton);
        cancelButtonNode.setTextFill(Color.BLACK);
        cancelButtonNode.setFont(Font.font("System", FontWeight.BOLD, 14));
        cancelButtonNode.setBackground(new Background(new BackgroundFill(Color.web("#00FF00"), null, null)));

        // Show alert and wait for user response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buyButton) {
            // Handle buy action
            if (purchaseGame(game)) {
                // Show purchase success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("You have successfully bought the game: " + game.getTitle());

                // Set dialog pane background color to black
                DialogPane successDialogPane = successAlert.getDialogPane();
                successDialogPane.setBackground(new Background(new BackgroundFill(Color.web("#000000"), null, null)));

                // Set text color to green
                Label successContentLabel = (Label) successAlert.getDialogPane().lookup(".content.label");
                successContentLabel.setTextFill(Color.web("#00FF00"));
                successContentLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

                // Get the ok button and set its text color to black and background color to
                // green
                ButtonType okButtonType = successAlert.getButtonTypes().stream()
                        .filter(buttonType -> buttonType.getButtonData().isDefaultButton())
                        .findFirst().orElse(null);
                if (okButtonType != null) {
                    Button okButton = (Button) successAlert.getDialogPane().lookupButton(okButtonType);
                    okButton.setTextFill(Color.BLACK);
                    okButton.setFont(Font.font("System", FontWeight.BOLD, 14));
                    okButton.setBackground(new Background(new BackgroundFill(Color.web("#00FF00"), null, null)));
                }
                successAlert.showAndWait();
            }
        } else {
            // Handle cancel action or dialog closed
            System.out.println("Cancel clicked or dialog closed");
        }
    }

    // Show error alert
    private void showErrorAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            // Set dialog pane background color to black
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

    // Purchase game
    private boolean purchaseGame(Game game) {
        String username = welcomeLabel.getText().replace("Welcome ", "");
        // fetch user by username
        User user = webClient.get()
                .uri("/users/username/{username}", username)
                .retrieve()
                .bodyToMono(User.class)
                .block();
        // check if the purchase is already made
        List<Purchase> purchases = webClient.get()
                .uri("/purchases/user/{userId}", user.getUserID())
                .retrieve()
                .bodyToFlux(Purchase.class)
                .collectList()
                .block();

        if (purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getGame().getGameId() == game.getGameId()) {
                    showErrorAlert("Error", "You have already bought this game.");
                    return false;
                }
            }
        }
        // purchase the game
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setGame(game);
        purchase.setPricePaid(game.getPrice());

        // save the purchase
        ResponseEntity<Purchase> responseEntity = webClient.post()
                .uri("/purchases")
                .bodyValue(purchase)
                .retrieve()
                .toEntity(Purchase.class)
                .block();

        return responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful();
    }

    // Display library games in flow pane
    private void displayLibraryGames(List<Game> games) {
        Platform.runLater(() -> {
            gamesFlowPane.getChildren().clear();

            for (Game game : games) {
                Label titleLabel = new Label(game.getTitle());
                titleLabel.setTextFill(Color.web("#00FF00"));
                titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

                Label installLabel = new Label("Install");
                installLabel.setTextFill(Color.web("#00FF00"));
                installLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

                // Create a horizontal box to hold the title and price labels
                HBox titlePriceBox = new HBox(titleLabel, installLabel);
                titlePriceBox.setAlignment(Pos.CENTER);
                titlePriceBox.setSpacing(50);

                InputStream inputStream = getClass()
                        .getResourceAsStream("/com/game_store/my_resources/assets/" + game.getTitle() + ".png");
                Image image;
                if (inputStream != null) {
                    image = new Image(inputStream);
                } else {
                    // Provide a default image when resource is not found
                    image = new Image("/com/game_store/my_resources/assets/default_image.png");
                }
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(250);

                VBox gameInfo = new VBox(imageView, titlePriceBox);
                gameInfo.setAlignment(Pos.CENTER);
                gameInfo.setSpacing(15);

                gameInfo.setOnMouseClicked(event -> {
                    // Handle click event here
                    showInstallAlert("Install Game", "Downloading in progress..\n\n" +
                            "Please wait..\n\n" + game.getTitle() + " is being installed..");
                });

                gamesFlowPane.getChildren().add(gameInfo);
            }
        });
    }

    // Show library games install alert
    private void showInstallAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
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

    // handle my achievements button action
    @FXML
    void handleMyAchievementsButtonAction(ActionEvent event) {
        // get the username
        String username = welcomeLabel.getText().replace("Welcome ", "");
        // get the user by username
        User user = webClient.get()
                .uri("/users/username/{username}", username)
                .retrieve()
                .bodyToMono(User.class)
                .block();

        // get the user achievements

        List<UserAchievement> userAchievements = webClient.get()
                .uri("/userachievements/user/{userId}", user.getUserID())
                .retrieve()
                .bodyToFlux(UserAchievement.class)
                .collectList()
                .block();

        // clear the games flow pane
        Platform.runLater(() -> gamesFlowPane.getChildren().clear());

        // display the user achievements
        for (UserAchievement userAchievement : userAchievements) {
            String title = userAchievement.getAchievement().getName();
            String description = userAchievement.getAchievement().getDescription();

            Platform.runLater(() -> {
                Label titleLabel = new Label(title);
                titleLabel.setTextFill(Color.web("#00FF00"));
                titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

                Label descLabel = new Label(description);
                descLabel.setTextFill(Color.web("#00FF00"));
                descLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

                VBox titleDescBox = new VBox(titleLabel, descLabel);
                titleDescBox.setAlignment(Pos.CENTER);
                titleDescBox.setSpacing(50);

                // Add the titleDescBox to the gamesFlowPane
                gamesFlowPane.getChildren().add(titleDescBox);
            });
        }

    }

}
