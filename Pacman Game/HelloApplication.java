package com.example.demo1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // open start scene with page title
        stage.setTitle("Pacman");
        stage.setScene(getStartScene(stage));
        stage.show();
    }

    /**
     * Start screen creation method. Called when the user opens the application or selects "back" at the configuration
     * screen.
     *
     * @param stage the application's stage
     * @return the start scene
     */
    private Scene getStartScene(Stage stage) {
        // HBox to contain the start scene elements
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(400, 100, 580, 250));
        hbox.setSpacing(100);

        // main text on the screen
        Text title = new Text("Pacman!");

        // create start button which leads to the configuration scene
        Button buttonStart = new Button("Start");
        buttonStart.setPrefSize(100, 20);
        // lambda expression to change the current scene when user selects "start"
        buttonStart.setOnAction(e -> {
            stage.setScene(getConfigScene(stage));
        });

        // create exit button which will close the apllication when selected
        Button buttonExit = new Button("Exit");
        buttonExit.setPrefSize(100, 20);
        buttonExit.setOnAction(e -> {
            stage.close();
        });

        // add the title, start button, and exit button to the HBox
        hbox.getChildren().addAll(title, buttonStart, buttonExit);
        // place the HBox in the scene, set window dimensions to 1000 x 1000
        return new Scene(hbox, 1000, 1000);
    }

    /**
     * Configuration scene creation method. Called when the user selects "start" on the start scene.
     *
     * @param stage the application's stage
     * @return the configuration scene
     */
    private Scene getConfigScene(Stage stage) {
        // VBox to contain all of the config screen elements
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(100));
        vbox.setSpacing(20);

        // main text on the screen
        Text title = new Text("Configuration Screen");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        // back button which sends user to the start scene when selected
        Button buttonBack = new Button("Back");
        buttonBack.setOnAction(e -> {
            stage.setScene(getStartScene(stage));
        });

        // creation of the start button
        Button buttonStart = new Button("Start Game");
        buttonStart.setPrefSize(100, 20);
        // not visible until the user makes a character selection
        buttonStart.setVisible(false);

        // call to add the configuration input elements to the VBox, as well as the start and back buttons
        vbox.getChildren().addAll(getConfigInputBox(stage, buttonStart), buttonStart, buttonBack);

        // place the VBox in the scene, set window dimensions to 1000 x 1000
        return new Scene(vbox, 1000, 1000);
    }

    /**
     * Creates the input area to be placed in the configuration scene. Called when the configuration screen is made.
     *
     * @param stage the application's stage
     * @param buttonStart the button which will start the game (not visible when passed in)
     * @return the input elements and labels
     */
    private VBox getConfigInputBox(Stage stage, Button buttonStart) {
        // create an HBox to hold a text field that prompts the user to input their name
        HBox nameConfig = new HBox();
        Label labelName = new Label("Name:");
        TextField nameField = new TextField();
        nameConfig.setSpacing(20);
        nameConfig.getChildren().addAll(labelName, nameField);

        // create an HBox to hold a combo box that prompts the user to select a difficulty
        HBox levelConfig = new HBox();
        Label labelLevel = new Label("Level:");
        ComboBox<String> levelSelect = new ComboBox<>();
        // add options to the combo box
        levelSelect.getItems().add("Easy");
        levelSelect.getItems().add("Medium");
        levelSelect.getItems().add("Hard");
        levelConfig.setSpacing(20);
        levelConfig.getChildren().addAll(labelLevel, levelSelect);

        // create an HBox to hold selectable character options
        HBox characters = new HBox();
        characters.setSpacing(50);

        // upload the images of the three character options
        Image character1 = new Image("https://static.wikia.nocookie.net/pacman/images/2/24/Pac-Man-0.png/revision/latest?cb=20190526005949", 100, 100, false, false);
        Image character2 = new Image("https://static.wikia.nocookie.net/pacman/images/1/1b/Mspacman-0.png/revision/latest?cb=20181031204120", 100, 100, false, false);
        Image character3 = new Image("https://static.wikia.nocookie.net/pacman/images/3/33/Babypac_namco.png/revision/latest?cb=20181105040459", 100, 100, false, false);

        // make the character's image appear on buttons
        Button button1 = getCharacterButton(character1);
        Button button2 = getCharacterButton(character2);
        Button button3 = getCharacterButton(character3);

        // create a label to precede the character buttons
        Label characterLabel = new Label("Choose character:");

        // add the label and all character options to the character HBox
        characters.getChildren().addAll(characterLabel, button1, button2, button3);

        /*
         * The next section handles what happens when the user selects a character. The process is the same regardless
         * of which character is chosen. The currently selected character is highlighted in yellow. The start button
         * will be made visible. If "start" is pressed, the name input and level selection fields are checked for valid
         * inputs. If both are valid, the player's name, level selection, and chosen character will be passed into the
         * game screen.
         */
        button1.setOnAction(e -> {
            // highlight selected character and make start button visible
            characterButtonHandler(button1, button2, button3, buttonStart);
            buttonStart.setOnAction(event -> {
                String nameInput = nameField.getText();
                String levelChosen = levelSelect.getValue();
                // when start button is pressed, the name and level values will be checked (errors thrown if necessary)
                // then, game scene created
                startButtonHandler(nameInput, levelChosen, stage, "character1");
            });
        });
        button2.setOnAction(e -> {
            characterButtonHandler(button2, button1, button3, buttonStart);
            buttonStart.setOnAction(event -> {
                String nameInput = nameField.getText();
                String levelChosen = levelSelect.getValue();
                startButtonHandler(nameInput, levelChosen, stage, "character2");
            });
        });
        button3.setOnAction(e -> {
            characterButtonHandler(button3, button1, button2, buttonStart);
            buttonStart.setOnAction(event -> {
                String nameInput = nameField.getText();
                String levelChosen = levelSelect.getValue();
                startButtonHandler(nameInput, levelChosen, stage, "character3");
            });
        });

        // create and return VBox which will hold all the HBoxes containing input information
        VBox inputs = new VBox();
        inputs.setSpacing(50);
        inputs.getChildren().addAll(nameConfig, levelConfig, characters);

        return inputs;
    }

    /**
     * Lambda expression handler for when a character button is selected. The chosen character will be highlighted, and
     * the button to start the game becomes visible.
     *
     * @param chosenChar the character contained by the button which the user pressed
     * @param notChosen1 the first unselected character
     * @param notChosen2 the second unselected character
     * @param buttonStart the button which will start the game
     */
    private void characterButtonHandler(Button chosenChar, Button notChosen1, Button notChosen2, Button buttonStart) {
        chosenChar.setStyle("-fx-background-color: #ffda00; ");
        notChosen1.setStyle("-fx-background-color: #ffffff; ");
        notChosen2.setStyle("-fx-background-color: #ffffff; ");
        buttonStart.setVisible(true);
    }

    /**
     * Lambda expression handler for when the user presses the button to start the game. The name and level inputs are
     * checked for validity, and errors are thrown to the user if they are invalid. If all inputs are valid, the game
     * scene is called.
     *
     * @param nameInput the name the user typed in the config text field
     * @param levelChosen the level the user selected from the config combo box
     * @param stage the stage to contain all elements
     * @param chosenChar a string representing the character which the user selected the image of
     */
    private void startButtonHandler(String nameInput, String levelChosen, Stage stage, String chosenChar) {
        // invalid names are null, empty, or only spaces
        if (nameInput == null || nameInput.isEmpty() || nameInput.isBlank()) {
            // call the method to throw an invalid name error
            nameError();
        // a level must be selected to continue
        } else if (levelChosen == null) {
            // call the method to throw an invalid level selection
            levelError();
        // if the name and level selections are valid, the stage is prompted to load the game scene
        } else {
            getGameScreen(stage, chosenChar, nameInput, levelChosen);
        }
    }

    /**
     * Creates a button containing the image of a character. Called when the configuration scene is created.
     *
     * @param image the image of the character to place in the button
     * @return a button containing the image of a character
     */
    private Button getCharacterButton(Image image) {
        // load the image
        ImageView charView = new ImageView(image);
        charView.setImage(image);

        // create the button, add the image, and set the background color
        Button charButton = new Button();
        charButton.setGraphic(charView);
        charButton.setStyle("-fx-background-color: #ffffff; ");

        return charButton;
    }

    /**
     * Placeholder method to load a dummy game screen with the relevant variables passed in.
     *
     * @param stage the application's stage
     * @param character a string representing the user's chosen character
     * @param name a string representing the user's name input
     * @param level a string representing the user's selected level
     */
    private void getGameScreen(Stage stage, String character, String name, String level) {
        VBox vbox = new VBox();
        Text title = new Text("This will be the game screen");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Scene scene = new Scene(vbox, 1000, 1000);
        Text characterChoice = new Text(character);
        Text playerName = new Text(name);
        Text levelChoice = new Text(level);

        // when the "back to start" button is selected, the user is taken back the start scene
        Button buttonBackToStart = new Button("Back to Start");
        buttonBackToStart.setOnAction(e -> {
            stage.setScene(getStartScene(stage));
        });

        vbox.getChildren().addAll(title, characterChoice, playerName, levelChoice, buttonBackToStart);

        stage.setScene(scene);
    }

    /**
     * An error pop-up window appears when the user's name input is not valid.
     */
    public static void nameError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Please enter a valid name");
        alert.showAndWait();
    }

    /**
     * An error pop-up window appears when the user fails to select a level before attempting to start the game.
     */
    public static void levelError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Please select a level");
        alert.showAndWait();
    }

    /**
     * Launches the program (if it is not run from the command line)
     * @param args java main method signature
     */
    public static void main(String[] args) {
        launch();
    }
}