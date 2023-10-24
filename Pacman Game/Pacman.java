import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;

public class Pacman {

	private PlayerCharacter playerChar;
	public boolean isInvincible;
	private Point2D position;
	private Direction direction;
	private Direction lastDirection;
	private Point2D velocity;
	private Image image;
	private PacmanState state;
	private int countdown;

	public Pacman(Point2D position, PlayerCharacter playerChar) throws Exception {
		this.playerChar = playerChar;
		this.isInvincible = false;
		this.position = position;
		this.direction = Direction.NONE;
		this.lastDirection = Direction.NONE;
		this.velocity = new Point2D(0, 0);
		this.state = PacmanState.NORMAL;
		this.countdown = 0;
		switch (playerChar) {
		case PACMAN:
			this.image = new Image(new FileInputStream("./Players/PacmanRight.gif"));
			break;
		case MRSPACMAN:
			this.image = new Image(new FileInputStream("./Players/MrsPacmanRight.gif"));
			break;
		case PACJR:
			this.image = new Image(new FileInputStream("./Players/PacJrRight.gif"));
			break;
		default:
			this.image = new Image(new FileInputStream("./Tiles/Error.png"));
			break;
		}
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public int getCountdown() {
		return countdown;
	}

	public void doCountdown() {
		countdown--;
	}

	public void setPosition(Point2D position) throws Exception {
		this.position = position;
	}

	public void setDirection(Direction direction) throws Exception {
		if (playerChar == PlayerCharacter.PACMAN) {
			switch (direction) {
			case UP:
				this.image = new Image(new FileInputStream("./Players/PacmanUp.gif"));
				break;
			case DOWN:
				this.image = new Image(new FileInputStream("./Players/PacmanDown.gif"));
				break;
			case LEFT:
				this.image = new Image(new FileInputStream("./Players/PacmanLeft.gif"));
				break;
			case RIGHT:
				this.image = new Image(new FileInputStream("./Players/PacmanRight.gif"));
				break;
			case NONE:
				this.image = new Image(new FileInputStream("./Players/PacmanRight.gif"));
				break;
			default:
				this.image = new Image(new FileInputStream("./Tiles/Error.png"));
				break;
			}
		} else if (playerChar == PlayerCharacter.MRSPACMAN) {
			switch (direction) {
			case UP:
				this.image = new Image(new FileInputStream("./Players/MrsPacmanUp.gif"));
				break;
			case DOWN:
				this.image = new Image(new FileInputStream("./Players/MrsPacmanDown.gif"));
				break;
			case LEFT:
				this.image = new Image(new FileInputStream("./Players/MrsPacmanLeft.gif"));
				break;
			case RIGHT:
				this.image = new Image(new FileInputStream("./Players/MrsPacmanRight.gif"));
				break;
			case NONE:
				this.image = new Image(new FileInputStream("./Players/MrsPacmanRight.gif"));
				break;
			default:
				this.image = new Image(new FileInputStream("./Tiles/Error.png"));
				break;
			}
		} else {
			switch (direction) {
			case UP:
				this.image = new Image(new FileInputStream("./Players/PacJrUp.gif"));
				break;
			case DOWN:
				this.image = new Image(new FileInputStream("./Players/PacJrDown.gif"));
				break;
			case LEFT:
				this.image = new Image(new FileInputStream("./Players/PacJrLeft.gif"));
				break;
			case RIGHT:
				this.image = new Image(new FileInputStream("./Players/PacJrRight.gif"));
				break;
			case NONE:
				this.image = new Image(new FileInputStream("./Players/PacJrRight.gif"));
				break;
			default:
				this.image = new Image(new FileInputStream("./Tiles/Error.png"));
				break;
			}
		}

		this.direction = direction;
	}

	public void setLastDirection(Direction lastDirection) throws Exception {
		this.lastDirection = lastDirection;
	}

	public void setVelocity(Point2D velocity) throws Exception {
		this.velocity = velocity;
	}

	public void setImage(Image image) throws Exception {
		this.image = image;
	}

	public Point2D getPosition() throws Exception {
		return position;
	}

	public Direction getDirection() throws Exception {
		return direction;
	}

	public Direction getLastDirection() throws Exception {
		return lastDirection;
	}

	public Point2D getVelocity() throws Exception {
		return velocity;
	}

	public Image getImage() throws Exception {
		return image;
	}

	public PacmanState getState() throws Exception {
		return state;
	}

	public void setState(PacmanState state) {
		this.state = state;
	}
}