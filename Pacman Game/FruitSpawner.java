import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;

public class FruitSpawner {

	private Point2D position;
	private int countdown;
	private FruitType fruitType;
	private Image image;

	public FruitSpawner(Point2D position) throws Exception {
		this.position = position;
		this.countdown = 50;
		this.fruitType = FruitType.NONE;
		this.image = new Image(new FileInputStream("./Tiles/Empty.png"));
	}

	public void update() throws Exception {
		if (countdown == 0 && fruitType == FruitType.NONE) {
			spawnFruit();
		} else if (fruitType == FruitType.NONE) {
			countdown--;
		}
	}

	public void spawnFruit() throws Exception {
		Random randomGenerator = new Random();
		int fruitDecision = randomGenerator.nextInt(3);
		switch (fruitDecision) {
		case 0:
			fruitType = FruitType.NONE;
			this.image = new Image(new FileInputStream("./Tiles/Empty.png"));
			break;
		case 1:
			fruitType = FruitType.CHERRY;
			this.image = new Image(new FileInputStream("./Objects/Cherry.gif"));
			break;
		case 2:
			fruitType = FruitType.MELON;
			this.image = new Image(new FileInputStream("./Objects/Melon.gif"));
			break;
		default:
			fruitType = FruitType.NONE;
			this.image = new Image(new FileInputStream("./Tiles/Empty.png"));
			break;
		}

		countdown = 50;
	}

	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public int getCountdown() {
		return countdown;
	}

	public void setCountdown(int timer) {
		this.countdown = countdown;
	}

	public FruitType getType() {
		return fruitType;
	}

	public void setType(FruitType fruitType) throws Exception {
		this.fruitType = fruitType;
		switch (fruitType) {
		case CHERRY:
			this.image = new Image(new FileInputStream("./Objects/Cherry.gif"));
			break;
		case MELON:
			this.image = new Image(new FileInputStream("./Objects/Melon.gif"));
			break;
		case NONE:
			this.image = new Image(new FileInputStream("./Tiles/Empty.png"));
			break;
		default:
			this.image = new Image(new FileInputStream("./Tiles/Empty.png"));
			break;
		}
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}