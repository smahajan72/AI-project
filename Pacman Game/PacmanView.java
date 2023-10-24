// package com.example.demo1;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Application;
import javafx.geometry.Pos;
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
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;


public class PacmanView extends Application {

    private static MediaPlayer mPlayer;
    private static MediaPlayer fxPlayer;
    private static Scene mazeScene;
    private Stage stage;
    private String name;
    private PacmanController controller;
    // private PacmanState prevState;

    @Override
    public void start(Stage stage) throws Exception {
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
    private Scene getStartScene(Stage stage) throws Exception {
        // VBox to contain the start scene elements
        VBox vbox = new VBox();
        vbox.setSpacing(100);

        // main text on the screen
        ImageView title = new ImageView(new Image(new FileInputStream("./Tiles/title.png")));

        // create start button which leads to the configuration scene
        Button buttonStart = new Button("Start");
        // buttonStart.setPrefSize(100, 20);
        buttonStart.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 20));
        // lambda expression to change the current scene when user selects "start"
        buttonStart.setOnAction(e -> {
            try {
                Media sound = new Media(new File("./Sounds/Fruit.wav").toURI().toString());
                fxPlayer = new MediaPlayer(sound);
                fxPlayer.play();
                stage.setScene(getConfigScene(stage));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        // create exit button which will close the apllication when selected
        Button buttonExit = new Button("Exit");
        // buttonExit.setPrefSize(100, 20);
        buttonExit.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 20));
        buttonExit.setOnAction(e -> {
            stage.close();
        });

        // add the title, start button, and exit button to the HBox
        vbox.getChildren().addAll(title, buttonStart, buttonExit);
        vbox.setAlignment(Pos.CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.rgb(50,60,57), null, null)));
        // place the VBox in the scene, set window dimensions to 672 x 800
        return new Scene(vbox, 672, 800);
    }

    /**
     * Configuration scene creation method. Called when the user selects "start" on the start scene.
     *
     * @param stage the application's stage
     * @return the configuration scene
     */
    private Scene getConfigScene(Stage stage) throws Exception {
        // VBox to contain all of the config screen elements
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(100));
        vbox.setSpacing(40);

        // main text on the screen
        Text title = new Text("Welcome to Pacman!");
        title.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 30));
        title.setFill(Color.YELLOW);
        vbox.getChildren().add(title);

        // back button which sends user to the start scene when selected
        Button buttonBack = new Button("Back");
        buttonBack.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 20));
        buttonBack.setOnAction(e -> {
            try {
                stage.setScene(getStartScene(stage));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        // creation of the start button
        Button buttonStart = new Button("Start Game");
        buttonStart.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 20));
        // buttonStart.setPrefSize(100, 20);
        // not visible until the user makes a character selection
        buttonStart.setVisible(false);

        // call to add the configuration input elements to the VBox, as well as the start and back buttons
        vbox.getChildren().addAll(getConfigInputBox(stage, buttonStart), buttonStart, buttonBack);
        vbox.setBackground(new Background(new BackgroundFill(Color.rgb(50,60,57), null, null)));
        vbox.setAlignment(Pos.CENTER);

        // place the VBox in the scene, set window dimensions to 672 x 800
        return new Scene(vbox, 672, 800);
    }

    /**
     * Creates the input area to be placed in the configuration scene. Called when the configuration screen is made.
     *
     * @param stage the application's stage
     * @param buttonStart the button which will start the game (not visible when passed in)
     * @return the input elements and labels
     */
    private VBox getConfigInputBox(Stage stage, Button buttonStart) throws Exception {
        // create an HBox to hold a text field that prompts the user to input their name
        HBox nameConfig = new HBox();
        Label labelName = new Label("Name:");
        labelName.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 12));
        labelName.setTextFill(Color.WHITE);
        TextField nameField = new TextField();
        nameConfig.setSpacing(20);
        nameConfig.getChildren().addAll(labelName, nameField);

        // create an HBox to hold a combo box that prompts the user to select a difficulty
        HBox levelConfig = new HBox();
        Label labelLevel = new Label("Level:");
        labelLevel.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 12));
        labelLevel.setTextFill(Color.WHITE);

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
        Image character1 = new Image(new FileInputStream("./Players/PacmanRight.gif"));
        Image character2 = new Image(new FileInputStream("./Players/MrsPacmanRight.gif"));
        Image character3 = new Image(new FileInputStream("./Players/PacJrRight.gif"));

        // make the character's image appear on buttons
        Button button1 = getCharacterButton(character1);
        Button button2 = getCharacterButton(character2);
        Button button3 = getCharacterButton(character3);

        // create a label to precede the character buttons
        Label characterLabel = new Label("Character:");
        characterLabel.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 12));
        characterLabel.setTextFill(Color.WHITE);

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
                try {
                    startButtonHandler(nameInput, levelChosen, stage, "PacMan");
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            });
        });
        button2.setOnAction(e -> {
            characterButtonHandler(button2, button1, button3, buttonStart);
            buttonStart.setOnAction(event -> {
                String nameInput = nameField.getText();
                String levelChosen = levelSelect.getValue();
                try {
                    startButtonHandler(nameInput, levelChosen, stage, "Mrs. Pacman");    
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                
            });
        });
        button3.setOnAction(e -> {
            characterButtonHandler(button3, button1, button2, buttonStart);
            buttonStart.setOnAction(event -> {
                String nameInput = nameField.getText();
                String levelChosen = levelSelect.getValue();
                try {
                    startButtonHandler(nameInput, levelChosen, stage, "Pac Jr.");
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            });
        });

        // create and return VBox which will hold all the HBoxes containing input information
        VBox inputs = new VBox();
        inputs.setBackground(new Background(new BackgroundFill(Color.rgb(50,60,57), null, null)));
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
        notChosen1.setStyle("-fx-background-color: #323C39; ");
        notChosen2.setStyle("-fx-background-color: #323C39; ");
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
    private void startButtonHandler(String nameInput, String levelChosen, Stage stage, String chosenChar) throws Exception {
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
            getGameScreen(stage, chosenChar, nameInput, levelChosen, 1, 0, 0, -1);
        }
    }

    /**
     * Creates a button containing the image of a character. Called when the configuration scene is created.
     *
     * @param image the image of the character to place in the button
     * @return a button containing the image of a character
     */
    private Button getCharacterButton(Image image) throws Exception {
        // load the image
        ImageView charView = new ImageView(image);
        charView.setImage(image);

        // create the button, add the image, and set the background color
        Button charButton = new Button();
        charButton.setGraphic(charView);
        charButton.setStyle("-fx-background-color: #323C39; ");

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
    private void getGameScreen(Stage mazeStage, String playerChar, String name, String level, int levelNum, int score, int ghostsEaten, int lives) throws Exception {
        this.name = name;

        VBox vbox = new VBox(8);

        PlayerCharacter pChar = PlayerCharacter.PACMAN;

        switch (playerChar) {
        case "Pac Jr.":
            pChar = PlayerCharacter.PACJR;
            break;
        case "Mrs. Pacman":
            pChar = PlayerCharacter.MRSPACMAN;
            break;
        default:
            pChar = PlayerCharacter.PACMAN;
            break;
        }

        Difficulty diff = Difficulty.MEDIUM;

        switch (level) {
        case "Easy":
            diff = Difficulty.EASY;
            break;
        case "Hard":
            diff = Difficulty.HARD;
            break;
        default:
            diff = Difficulty.MEDIUM;
            break;
        }

        PacmanModel model = new PacmanModel(diff, pChar, score, levelNum, ghostsEaten, lives);
        //System.out.println("model initialized");
        PacmanController controller = new PacmanController(model, this);
        this.controller = controller;
        // model.setController(controller);

        //System.out.println("Ab to make the GridPane and such in PacmanView.java");
        // Use the maze data to draw the maze using appropriate tiles
        GridPane grid = new GridPane();
        for (int rows = 1; rows <= 22; rows++) {
            for (int columns = 1; columns <= 21; columns++) {
                ImageView tile = new ImageView();
                tile.setImage(model.getMazeTile(rows, columns).getImage());
                if (rows == (int) model.getPacman().getPosition().getX() && columns == (int) model.getPacman().getPosition().getY()) {
                    tile.setImage(model.getPacman().getImage());
                }
                if (model.getDifficulty() != Difficulty.EASY) {
                    if (rows == (int) model.getBlinky().getPosition().getX() && columns == (int) model.getBlinky().getPosition().getY()) {
                        tile.setImage(model.getBlinky().getImage());
                    }
                }
                if (rows == (int) model.getInky().getPosition().getX() && columns == (int) model.getInky().getPosition().getY()) {
                    tile.setImage(model.getInky().getImage());
                }
                if (model.getDifficulty() != Difficulty.EASY) {
                    if (rows == (int) model.getPinky().getPosition().getX() && columns == (int) model.getPinky().getPosition().getY()) {
                       tile.setImage(model.getPinky().getImage());
                    }
                }
                if (rows == (int) model.getClyde().getPosition().getX() && columns == (int) model.getClyde().getPosition().getY()) {
                    tile.setImage(model.getClyde().getImage());
                }
                grid.add(tile, columns, rows);
            }
        }
        //System.out.println("GridPane created.");

        vbox.getChildren().add(grid);

        HBox hbox1 = new HBox(32);
        Font font = Font.loadFont("file:Fonts/ARCADE_R.ttf", 20);
        Text scoreText = new Text (String.format("SCORE: %d", model.getScore()));
        scoreText.setFont(font);
        scoreText.setFill(Color.WHITE);
        Text nameText = new Text(name);
        nameText.setFont(font);
        nameText.setFill(Color.WHITE);
        hbox1.getChildren().add(scoreText);
        hbox1.getChildren().add(nameText);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox2 = new HBox(32);
        Text levelText = new Text(String.format("LEVEL: %d", model.getLevel()));
        levelText.setFont(font);
        levelText.setFill(Color.WHITE);
        int nlives = 0;
        switch (diff) {
        case EASY:
            nlives = 5;
            break;
        default:
            nlives = 3;
        }
        if (diff == Difficulty.EASY) {
            model.setLives(nlives);
        } else if (diff != Difficulty.EASY && model.getLives() == -1) {
            model.setLives(nlives);
        }
        Text livesText = new Text (String.format("LIVES: %d", model.getLives()));
        livesText.setFont(font);
        livesText.setFill(Color.WHITE);
        hbox2.getChildren().add(levelText);
        hbox2.getChildren().add(livesText);
        hbox2.setAlignment(Pos.CENTER);
        HBox hbox3 = new HBox(32);
        Text stateText;
        if (model.getPacman().getState() == PacmanState.INVINCIBLE) {
            stateText = new Text("Eat Ghosts!");
            stateText.setFont(font);
            stateText.setFill(Color.YELLOW);
        } else if (model.getPacman().getState() == PacmanState.REPELLENT) {
            stateText = new Text("Ghost Repellent!");
            stateText.setFont(font);
            stateText.setFill(Color.GREEN);
        } else {
            stateText = new Text("Eat pellets!");
            stateText.setFont(font);
            stateText.setFill(Color.WHITE);
        }
        Text multText;
        if (model.getMultiplier() != 1) {
            multText = new Text("Double Points!");
            multText.setFont(font);
            multText.setFill(Color.ORANGE);
        } else {
            multText = new Text("No Multiplier");
            multText.setFont(font);
            multText.setFill(Color.WHITE);
        }
        hbox3.getChildren().add(stateText);
        hbox3.getChildren().add(multText);
        hbox3.setAlignment(Pos.CENTER);
        vbox.getChildren().add(hbox1);
        vbox.getChildren().add(hbox2);
        vbox.getChildren().add(hbox3);
        vbox.setBackground(new Background(new BackgroundFill(Color.rgb(50,60,57), null, null)));

        mazeScene = new Scene(vbox, 672, 800);

        mazeScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                boolean keyPressed = true;
                KeyCode code = keyEvent.getCode();
                Direction direction = Direction.NONE;

                switch (code) {
                case LEFT:
                    direction = Direction.LEFT;
                    break;
                case RIGHT:
                    direction = Direction.RIGHT;
                    break;
                case UP:
                    direction = Direction.UP;
                    break;
                case DOWN:
                    direction = Direction.DOWN;
                    break;
                default:
                    keyPressed = false;
                    break;
                }
                if (keyPressed) {
                    keyEvent.consume();
                    //System.out.println("You pressed a button");
                    try {
                        model.getPacman().setDirection(direction);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    // model.getPacman().setDirection(direction);
                }
            }
        });

        // prevState = model.getPacman().getState();

        mazeStage.setScene(mazeScene);
        mazeStage.show();
        this.stage = mazeStage;
        Media introSound;
        if (pChar == PlayerCharacter.MRSPACMAN) {
            introSound = new Media(new File("./Sounds/MrsPacman.wav").toURI().toString());
        } else {
            introSound = new Media(new File("./Sounds/Pacman.wav").toURI().toString());
        }
        mPlayer = new MediaPlayer(introSound);

        mPlayer.setOnEndOfMedia(() -> {
                try {
                    controller.initialize();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
        });

        mPlayer.play();
        
    }

    /**
    public static void update(PacmanModel model, Stage mazeStage) throws Exception {
        mazeStage.getRoot().getChildren().get(0).getChildren().get()   
    }
    */

    public void update(PacmanModel model, Stage mazeStage) throws Exception {
        VBox vbox = new VBox(8);

        if (model.getIsLost()) {
            controller.pause();
            mPlayer = new MediaPlayer(new Media(new File("./Sounds/Death.wav").toURI().toString()));
            
            mPlayer.setOnEndOfMedia(() -> {
                model.setIsLost(false);
                try {
                    model.resetPositions();
                    controller.unpause(); 
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            });  

            mPlayer.play();
        }

        GridPane grid = new GridPane();
        for (int rows = 1; rows <= 22; rows++) {
            for (int columns = 1; columns <= 21; columns++) {
                ImageView tile = new ImageView();
                tile.setImage(model.getMazeTile(rows, columns).getImage());
                if (rows == (int) model.getPacman().getPosition().getX() && columns == (int) model.getPacman().getPosition().getY()) {
                    tile.setImage(model.getPacman().getImage());
                }
                if (model.getDifficulty() != Difficulty.EASY) {
                    if (rows == (int) model.getBlinky().getPosition().getX() && columns == (int) model.getBlinky().getPosition().getY()) {
                        tile.setImage(model.getBlinky().getImage());
                    }
                }
                if (rows == (int) model.getInky().getPosition().getX() && columns == (int) model.getInky().getPosition().getY()) {
                    tile.setImage(model.getInky().getImage());
                }
                if (model.getDifficulty() != Difficulty.EASY) {
                    if (rows == (int) model.getPinky().getPosition().getX() && columns == (int) model.getPinky().getPosition().getY()) {
                        tile.setImage(model.getPinky().getImage());
                    }
                }
                if (rows == (int) model.getClyde().getPosition().getX() && columns == (int) model.getClyde().getPosition().getY()) {
                    tile.setImage(model.getClyde().getImage());
                }
                grid.add(tile, columns, rows);
            }
        }
        // System.out.println("GridPane created.");

        vbox.getChildren().add(grid);

        HBox hbox1 = new HBox(32);
        Font font = Font.loadFont("file:Fonts/ARCADE_R.ttf", 20);
        Text scoreText = new Text (String.format("SCORE: %d", model.getScore()));
        scoreText.setFont(font);
        scoreText.setFill(Color.WHITE);
        Text nameText = new Text(name);
        nameText.setFont(font);
        nameText.setFill(Color.WHITE);
        hbox1.getChildren().add(scoreText);
        hbox1.getChildren().add(nameText);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox2 = new HBox(32);
        Text levelText = new Text(String.format("LEVEL: %d", model.getLevel()));
        levelText.setFont(font);
        levelText.setFill(Color.WHITE);
        Text livesText = new Text (String.format("LIVES: %d", model.getLives()));
        livesText.setFont(font);
        livesText.setFill(Color.WHITE);
        hbox2.getChildren().add(levelText);
        hbox2.getChildren().add(livesText);
        hbox2.setAlignment(Pos.CENTER);
        HBox hbox3 = new HBox(32);
        Text stateText;
        if (model.getPacman().getState() == PacmanState.INVINCIBLE) {
            stateText = new Text("Eat Ghosts!");
            stateText.setFont(font);
            stateText.setFill(Color.YELLOW);
            if (model.getInvMusicFlag() == 1) {
                Media invMusic = new Media(new File("./Sounds/SuperPacMan.wav").toURI().toString());
                mPlayer.stop();
                mPlayer = new MediaPlayer(invMusic);
                mPlayer.play();
            }
        } else if (model.getPacman().getState() == PacmanState.REPELLENT) {
            stateText = new Text("Ghost Repellent!");
            stateText.setFont(font);
            stateText.setFill(Color.GREEN);
        } else {
            stateText = new Text("Eat pellets!");
            stateText.setFont(font);
            stateText.setFill(Color.WHITE);
        }
        Text multText;
        if (model.getMultiplier() != 1) {
            multText = new Text("Double Points!");
            multText.setFont(font);
            multText.setFill(Color.ORANGE);
        } else {
            multText = new Text("No Multiplier");
            multText.setFont(font);
            multText.setFill(Color.WHITE);
        }
        hbox3.getChildren().add(stateText);
        hbox3.getChildren().add(multText);
        hbox3.setAlignment(Pos.CENTER);
        vbox.getChildren().add(hbox1);
        vbox.getChildren().add(hbox2);
        vbox.getChildren().add(hbox3);
        vbox.setBackground(new Background(new BackgroundFill(Color.rgb(50,60,57), null, null)));

        if (model.getFxFlag() == 1) {
            Media sound = new Media(new File("./Sounds/Fruit.wav").toURI().toString());
            fxPlayer = new MediaPlayer(sound);
            fxPlayer.play();
        }

        mazeScene = new Scene(vbox, 672, 800);

        mazeScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                boolean keyPressed = true;
                KeyCode code = keyEvent.getCode();
                Direction direction = Direction.NONE;

                switch (code) {
                case LEFT:
                    direction = Direction.LEFT;
                    break;
                case RIGHT:
                    direction = Direction.RIGHT;
                    break;
                case UP:
                    direction = Direction.UP;
                    break;
                case DOWN:
                    direction = Direction.DOWN;
                    break;
                case ESCAPE:
                    try {
                        controller.terminate();
                        mazeStage.setScene(getConfigScene(mazeStage));
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                default:
                    keyPressed = false;
                    break;
                }
                if (keyPressed) {
                    keyEvent.consume();
                    // System.out.println("You pressed a button");
                    try {
                        model.getPacman().setDirection(direction);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    // model.getPacman().setDirection(direction);
                }
            }
        });

        // prevState = model.getPacman().getState();

        mazeStage.setScene(mazeScene);
        mazeStage.show();

        if (model.getIsGameOver()) {
            controller.terminate();
            gameOver(mazeStage);
        }

        if (model.getIsWon()) {
            controller.terminate();
            win(mazeStage);
        }
    }

    public void gameOver(Stage stage) {
        stage.setTitle("Game Over!");
        stage.setScene(getEndScene(stage));
        stage.show();
        mPlayer.stop();
        mPlayer = new MediaPlayer(new Media(new File("./Sounds/GameOver.wav").toURI().toString()));
        mPlayer.play();
    }

    public Scene getEndScene(Stage stage) {
        // HBox to contain the start scene elements
        VBox vbox = new VBox();
        vbox.setSpacing(100);

        // main text on the screen
        Text title = new Text("Game over!");
        title.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 30));
        title.setFill(Color.RED);

        VBox vbox2 = new VBox();
        vbox2.setSpacing(20);
        Text scoreText = new Text(String.format("SCORE: %d", controller.getModel().getScore()));
        scoreText.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 15));
        scoreText.setFill(Color.YELLOW);
        Text ghostText = new Text(String.format("GHOSTS GOBBLED: %d", controller.getModel().getGhostsEaten()));
        ghostText.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 15));
        ghostText.setFill(Color.YELLOW);
        Text levelText = new Text(String.format("LEVELS COMPLETED: %d", controller.getModel().getLevel() - 1));
        levelText.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 15));
        levelText.setFill(Color.YELLOW);
        vbox2.getChildren().add(scoreText);
        vbox2.getChildren().add(ghostText);
        vbox2.getChildren().add(levelText);
        vbox2.setAlignment(Pos.CENTER);

        // create start button which leads to the configuration scene
        Button buttonStart = new Button("Restart");
        buttonStart.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 20));
        // buttonStart.setPrefSize(100, 20);
        // lambda expression to change the current scene when user selects "start"
        buttonStart.setOnAction(e -> {
            try {
                stage.setScene(getConfigScene(stage));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        // create exit button which will close the apllication when selected
        Button buttonExit = new Button("Exit Game");
        buttonExit.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 20));
        // buttonExit.setPrefSize(100, 20);
        buttonExit.setOnAction(e -> {
            stage.close();
        });

        // add the title, start button, and exit button to the HBox
        vbox.getChildren().addAll(title, vbox2, buttonStart, buttonExit);
        vbox.setAlignment(Pos.CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.rgb(50,60,57), null, null)));
        // place the HBox in the scene, set window dimensions to 672 x 800
        return new Scene(vbox, 672, 800);
    }

    public void win(Stage stage) {
        stage.setTitle("Pacman");
        stage.setScene(getWinScene(stage));
        stage.show();
        mPlayer.stop();
        mPlayer = new MediaPlayer(new Media(new File("./Sounds/LevelComplete.wav").toURI().toString()));
        mPlayer.play();
    }

    public Scene getWinScene(Stage stage) {
        // HBox to contain the start scene elements
        VBox vbox = new VBox();
        vbox.setSpacing(100);

        // main text on the screen
        Text title = new Text("Level Complete!");
        title.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 30));
        title.setFill(Color.GREEN);

        VBox vbox2 = new VBox();
        vbox2.setSpacing(20);
        Text scoreText = new Text(String.format("SCORE: %d", controller.getModel().getScore()));
        scoreText.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 15));
        scoreText.setFill(Color.YELLOW);
        Text ghostText = new Text(String.format("GHOSTS GOBBLED: %d", controller.getModel().getGhostsEaten()));
        ghostText.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 15));
        ghostText.setFill(Color.YELLOW);
        Text levelText = new Text(String.format("LEVELS COMPLETED: %d", controller.getModel().getLevel()));
        levelText.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 15));
        levelText.setFill(Color.YELLOW);
        vbox2.getChildren().add(scoreText);
        vbox2.getChildren().add(ghostText);
        vbox2.getChildren().add(levelText);
        vbox2.setAlignment(Pos.CENTER);

        // create start button which leads to the configuration scene
        Button buttonContinue = new Button("Continue");
        buttonContinue.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 20));
        // buttonStart.setPrefSize(100, 20);
        // lambda expression to change the current scene when user selects "start"
        buttonContinue.setOnAction(e -> {
            try {
                getGameScreen(stage, controller.getModel().getPlayerCharString(), name, controller.getModel().getDifficultyString(), controller.getModel().getLevel() + 1, controller.getModel().getScore(), controller.getModel().getGhostsEaten(), controller.getModel().getLives());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        // create start button which leads to the configuration scene
        Button buttonStart = new Button("Restart");
        buttonStart.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 20));
        // buttonStart.setPrefSize(100, 20);
        // lambda expression to change the current scene when user selects "start"
        buttonStart.setOnAction(e -> {
            try {
                stage.setScene(getConfigScene(stage));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        // create exit button which will close the apllication when selected
        Button buttonExit = new Button("Exit Game");
        buttonExit.setFont(Font.loadFont("file:Fonts/ARCADE_R.ttf", 20));
        // buttonExit.setPrefSize(100, 20);
        buttonExit.setOnAction(e -> {
            stage.close();
        });

        // add the title, start button, and exit button to the HBox
        vbox.getChildren().addAll(title, vbox2, buttonContinue, buttonStart, buttonExit);
        vbox.setAlignment(Pos.CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.rgb(50,60,57), null, null)));
        // place the HBox in the scene, set window dimensions to 672 x 800
        return new Scene(vbox, 672, 800);
    }


    /**
     * An error pop-up window appears when the user's name input is not valid.
     */
    public static void nameError() throws Exception {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Please enter a valid name");
        Media errorSound = new Media(new File("./Sounds/Death.wav").toURI().toString());
        mPlayer = new MediaPlayer(errorSound);
        mPlayer.play();
        alert.showAndWait();
    }

    /**
     * An error pop-up window appears when the user fails to select a level before attempting to start the game.
     */
    public static void levelError() throws Exception {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Please select a level");
        Media errorSound = new Media(new File("./Sounds/Death.wav").toURI().toString());
        mPlayer = new MediaPlayer(errorSound);
        mPlayer.play();
        alert.showAndWait();
    }

    /**
     * A method to retrieve maze data from a text file for use in building the maze.
     * @param input the text file from which to retrieve the maze data
     * @return a two dimensional int array representing the layout of the maze
     */
    public int[][] parseMazeData(File input) throws Exception {
        try {
            // System.out.println("3");
            int i = 0;
            int j = 0;
            int[][] maze = new int[24][23];
            Scanner scan = new Scanner(input);
            while (scan.hasNextLine() && i < 24) {
                j = 0;
                while (scan.hasNextInt() && j < 23) {
                    maze[i][j] = scan.nextInt();
                    //System.out.printf("%d ", maze[i][j]);
                    j++;
                }
                //System.out.printf("\n");
                i++;
            }
            return maze;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Stage getStage() {
        return this.stage;
    }

    /**
     * Launches the program (if it is not run from the command line)
     * @param args java main method signature
     */
    public static void main(String[] args) {
        launch();
    }
}