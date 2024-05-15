package com.game_store.controller.ui_controller;

import com.game_store.model.Game;
import com.game_store.model.Publisher;
import com.game_store.model.Purchase;
import com.game_store.model.User;
import com.game_store.service.GameService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import javafx.stage.FileChooser;
import reactor.core.publisher.Mono;

import java.util.Optional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class PublisherHomeController implements Initializable {

    @FXML
    private Label welcomeLabel;

    @FXML
    private FlowPane gamesFlowPane;

    @FXML
    private TextField searchField;

    private final WebClient webClient = WebClient.create("http://localhost:8080/api");
    private File selectedImageFile;

    // set up page with username
    public void useUsernameToSetUpPage(String username) {
        Platform.runLater(() -> {
            welcomeLabel.setText("Welcome " + username);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(this::fillGamesFlowPane);
    }

    // fill games flow pane with the publisher's games
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

                // Create an image view and set its properties
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

    @FXML
    private void handleCreateGameButton() {
        Platform.runLater(() -> {
            // Clear the flow pane
            gamesFlowPane.getChildren().clear();

            // Create labels and text fields for game details
            Label titleLabel = new Label("Title:");
            titleLabel.setTextFill(Color.WHITE);

            TextField titleField = new TextField();
            titleField.setPromptText("Enter title");

            Label priceLabel = new Label("Price:");
            priceLabel.setTextFill(Color.WHITE);

            TextField priceField = new TextField();
            priceField.setPromptText("Enter price");

            Label genreLabel = new Label("Genre:");
            genreLabel.setTextFill(Color.WHITE);

            TextField genreField = new TextField();
            genreField.setPromptText("Enter genre");

            Label descriptionLabel = new Label("Description:");
            descriptionLabel.setTextFill(Color.WHITE);

            TextArea descriptionArea = new TextArea();
            descriptionArea.setPromptText("Enter description");
            descriptionArea.setWrapText(true);

            // Create a button to select an image
            Button selectImageButton = new Button("Select Image");
            selectImageButton.setOnAction(e -> selectImage());

            // Add input fields to VBox
            VBox inputBox = new VBox(titleLabel, titleField, priceLabel, priceField, genreLabel, genreField,
                    descriptionLabel, descriptionArea, selectImageButton);
            inputBox.setAlignment(Pos.CENTER_LEFT);
            inputBox.setSpacing(10);

            // Create a button to submit the game creation
            Button submitButton = new Button("Create");
            submitButton.setOnAction(e -> handleSubmitGame(titleField.getText(), priceField.getText(),
                    genreField.getText(), descriptionArea.getText()));
            submitButton.setTextFill(Color.BLACK);
            submitButton.setBackground(new Background(new BackgroundFill(Color.web("#00FF00"), null, null)));
            submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            // Add input fields and submit button to the FlowPane
            gamesFlowPane.getChildren().addAll(inputBox, submitButton);

            // Set FlowPane properties
            gamesFlowPane.setHgap(20);
            gamesFlowPane.setVgap(20);
            gamesFlowPane.setPadding(new Insets(20));
            gamesFlowPane.setStyle("-fx-background-color: #000000;"); // Set background color
        });
    }

    private void handleSubmitGame(String title, String price, String genre, String description) {
        // Create a Game object with the provided details
        Game game = new Game();
        game.setTitle(title);
        game.setPrice(new BigDecimal(price));
        game.setGenre(genre);
        game.setDescription(description);

        // Send a POST request to create the game
        webClient.post()
                .uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(game)
                .retrieve()
                .bodyToMono(Game.class)
                .subscribe(
                        createdGame -> Platform.runLater(() -> {
                            // Show a success message
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setHeaderText(null);
                            alert.setContentText("Game created successfully!");

                            // Set dialog pane background color to black
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.setBackground(
                                    new Background(new BackgroundFill(Color.web("#000000"), null, null)));

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
                                okButton.setBackground(
                                        new Background(new BackgroundFill(Color.web("#00FF00"), null, null)));
                            }

                            alert.showAndWait();

                            // Save the selected image with the game title as filename
                            saveGameImage(selectedImageFile, title);

                            // Refresh the games flow pane to display the newly created game
                            fillGamesFlowPane();
                        }),
                        error -> Platform.runLater(
                                () -> showErrorAlert("Error", "Failed to create game. Please try again later.")));
    }

    // handle logout action button
    @FXML
    private void handleLogoutActionButton(ActionEvent event) throws IOException {
        // Handle logout action
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/game_store/my_resources/Login.fxml"));
        Parent root = loader.load();
        SceneController.switchToScene(event, root);

    }

    // handle search action button
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

    // show game details alert
    // Show game details alert
    private void showGameDetailsAlert(Game game) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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

        // Set text color to green
        ButtonType okButtonType = alert.getButtonTypes().stream()
                .filter(buttonType -> buttonType.getButtonData().isDefaultButton())
                .findFirst().orElse(null);
        if (okButtonType != null) {
            Button okButton = (Button) alert.getDialogPane().lookupButton(okButtonType);
            okButton.setText("Close");
            okButton.setTextFill(Color.BLACK);
            okButton.setFont(Font.font("System", FontWeight.BOLD, 14));
            okButton.setBackground(new Background(new BackgroundFill(Color.web("#00FF00"), null, null)));
        }

        // Show alert and wait for user response
        alert.showAndWait();
    }

    // select image
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        selectedImageFile = fileChooser.showOpenDialog(null);
    }

    // save game image
    private void saveGameImage(File imageFile, String gameTitle) {
        if (imageFile != null) {
            String fileName = gameTitle.replaceAll("\\s", "") + ".png";
            try {
                URI uri = getClass().getResource("/com/game_store/my_resources/assets/").toURI();
                Path assetsPath = Paths.get(uri);
                File destFile = new File(assetsPath.resolve(fileName).toString());
                Files.copy(imageFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

}