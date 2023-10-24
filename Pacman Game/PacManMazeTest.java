import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;

public class PacManMazeTest extends Application {

	private Scene mazeScene;
    private Stage stage;
    private MediaPlayer mPlayer;

	@Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        System.out.println("1");
        primaryStage.setTitle("PacMan Maze Drawing Test");
        System.out.println("2");
        drawMaze(primaryStage);
    }

    public void drawMaze(Stage mazeStage) throws Exception {
        VBox vbox = new VBox(8);

        int[][] maze = parseMazeData(new File("mazedata.txt"));
        GridPane grid = new GridPane();
        for (int rows = 1; rows <= 22; rows++) {
            for (int columns = 1; columns <= 21; columns++) {
                ImageView tile = new ImageView();

                switch (maze[rows][columns]) {
                case 0:
                    tile.setImage(new Image(new FileInputStream("./Tiles/Empty.png")));
                    break;
                case 1:
                    boolean left = false;
                    boolean right = false;
                    boolean up = false;
                    boolean down = false;

                    if (maze[rows - 1][columns] == 1) {
                        up = true;
                    }
                    if (maze[rows + 1][columns] == 1) {
                        down = true;
                    }
                    if (maze[rows][columns - 1] == 1) {
                        left = true;
                    }
                    if (maze[rows][columns + 1] == 1) {
                        right = true;
                    }

                    if (left && right && (!up) && (!down)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/LeftRight.png")));
                    } else if (left && down && (!up) && (!right)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/LeftDown.png")));
                    } else if (left && up && (!right) && (!down)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/LeftUp.png")));
                    } else if (right && up && (!left) && (!down)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/RightUp.png")));
                    } else if (right && down && (!left) && (!up)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/RightDown.png")));
                    } else if (up && down && (!left) && (!right)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/UpDown.png")));
                    } else if (left && right && up && (!down)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/LeftRightUp.png")));
                    } else if (left && right && down && (!up)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/LeftRightDown.png")));
                    } else if (left && up && down && (!right)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/LeftUpDown.png")));
                    } else if (right && up && down && (!left)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/RightUpDown.png")));
                    } else if (left && right && up && down) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/LeftRightUpDown.png")));
                    } else if (left && (!right) && (!up) && (!down)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/Left.png")));
                    } else if (right && (!left) && (!up) && (!down)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/Right.png")));
                    } else if (up && (!left) && (!right) && (!down)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/Up.png")));
                    } else if (down && (!left) && (!right) && (!up)) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/Down.png")));
                    } else if (maze[rows][columns + 1] == 5) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/GhostGateLeft.png")));
                    } else if (maze[rows][columns - 1] == 5) {
                        tile.setImage(new Image(new FileInputStream("./Tiles/GhostGateRight.png")));
                    } else {
                        tile.setImage(new Image(new FileInputStream("./Tiles/Error.png")));
                    }
                    break;
                case 2:
                    tile.setImage(new Image(new FileInputStream("./Objects/Pellet.gif")));
                    break;
                case 3:
                    tile.setImage(new Image(new FileInputStream("./Objects/PowerPellet.gif")));
                    break;
                case 4:
                    tile.setImage(new Image(new FileInputStream("./Tiles/Empty.png")));
                    break;
                case 5:
                    tile.setImage(new Image(new FileInputStream("./Tiles/GhostGate.png")));
                    break;
                case 6:
                    tile.setImage(new Image(new FileInputStream("./Players/PacmanRight.gif")));
                    break;
                case 7:
                    tile.setImage(new Image(new FileInputStream("./Ghosts/BlinkyRight.gif")));
                    break;
                case 8:
                    tile.setImage(new Image(new FileInputStream("./Ghosts/PinkyRight.gif")));
                    break;
                case 9:
                    tile.setImage(new Image(new FileInputStream("./Ghosts/InkyLeft.gif")));
                    break;
                case 10:
                    tile.setImage(new Image(new FileInputStream("./Ghosts/ClydeLeft.gif")));
                    break;
                default:
                    tile.setImage(new Image(new FileInputStream("./Tiles/Error.png")));
                    break;
                }

                grid.add(tile, columns, rows);
            }
        }
        vbox.getChildren().add(grid);

        HBox hbox1 = new HBox(32);
        Font font = Font.loadFont("file:Fonts/ARCADE_R.ttf", 20);
        Text score = new Text ("SCORE: 0");
        score.setFont(font);
        score.setFill(Color.WHITE);
        Text name = new Text("*NAME HERE*");
        name.setFont(font);
        name.setFill(Color.WHITE);
        hbox1.getChildren().add(score);
        hbox1.getChildren().add(name);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox2 = new HBox(32);
        Text level = new Text("LEVEL: 1");
        level.setFont(font);
        level.setFill(Color.WHITE);
        Text lives = new Text("LIVES: 3");
        lives.setFont(font);
        lives.setFill(Color.WHITE);
        hbox2.getChildren().add(level);
        hbox2.getChildren().add(lives);
        hbox2.setAlignment(Pos.CENTER);
        vbox.getChildren().add(hbox1);
        vbox.getChildren().add(hbox2);

        mazeScene = new Scene(vbox, 672, 768, Color.rgb(50,60,57));
        mazeStage.setScene(mazeScene);
        mazeStage.show();
        Media introSound = new Media(new File("./Sounds/PacMan.wav").toURI().toString());
        mPlayer = new MediaPlayer(introSound);
        mPlayer.play();
    }	

    public int[][] parseMazeData(File input) {
        try {
            System.out.println("3");
            int i = 0;
            int j = 0;
            int[][] maze = new int[24][23];
            Scanner scan = new Scanner(input);
            while (scan.hasNextLine() && i < 24) {
                j = 0;
                while (scan.hasNextInt() && j < 23) {
                    maze[i][j] = scan.nextInt();
                    System.out.printf("%d ", maze[i][j]);
                    j++;
                }
                System.out.printf("\n");
                i++;
            }
            return maze;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}