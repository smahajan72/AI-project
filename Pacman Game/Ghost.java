import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;

public class Ghost {

	private GhostType ghostType;
	private Point2D position;
	private Direction direction;
	private int countdown;
	// private Direction lastDirection;
	private Point2D velocity;
	private Point2D home;
	private Image image;
	private GhostState state;

	public Ghost(Point2D position, GhostType ghostType, Point2D home) throws Exception {
		this.ghostType = ghostType;
		this.position = position;
		this.home = home;
		this.countdown = 0;
		this.state = GhostState.NORMAL;
		
		switch (ghostType) {
		case BLINKY:
			this.image = new Image(new FileInputStream("./Ghosts/BlinkyRight.gif"));
			this.direction = Direction.RIGHT;
			this.velocity = new Point2D(0, 1);
			break;
		case INKY:
			this.image = new Image(new FileInputStream("./Ghosts/InkyRight.gif"));
			this.direction = Direction.LEFT;
			this.velocity = new Point2D(0, -1);
			break;
		case PINKY:
			this.image = new Image(new FileInputStream("./Ghosts/PinkyRight.gif"));
			this.direction = Direction.RIGHT;
			this.velocity = new Point2D(0, 1);
			break;
		case CLYDE:
			this.image = new Image(new FileInputStream("./Ghosts/ClydeRight.gif"));
			this.direction = Direction.LEFT;
		this.velocity = new Point2D(0, -1);
			break;
		default:
			this.image = new Image(new FileInputStream("./Tiles/Error.png"));
		}
	}

	public void setDirection(Direction direction) throws Exception {
		if (ghostType == GhostType.BLINKY) {
			switch (direction) {
			case RIGHT:
				this.image = new Image(new FileInputStream("./Ghosts/BlinkyRight.gif"));
				break;
			case LEFT:
				this.image = new Image(new FileInputStream("./Ghosts/BlinkyLeft.gif"));
				break;
			case UP:
				this.image = new Image(new FileInputStream("./Ghosts/BlinkyUp.gif"));
				break;
			case DOWN:
				this.image = new Image(new FileInputStream("./Ghosts/BlinkyDown.gif"));
				break;
			default:
				this.image = new Image(new FileInputStream("./Tiles/Error.png"));
			}
		} else if (ghostType == GhostType.INKY) {
			switch (direction) {
			case RIGHT:
				this.image = new Image(new FileInputStream("./Ghosts/InkyRight.gif"));
				break;
			case LEFT:
				this.image = new Image(new FileInputStream("./Ghosts/InkyLeft.gif"));
				break;
			case UP:
				this.image = new Image(new FileInputStream("./Ghosts/InkyUp.gif"));
				break;
			case DOWN:
				this.image = new Image(new FileInputStream("./Ghosts/InkyDown.gif"));
				break;
			default:
				this.image = new Image(new FileInputStream("./Tiles/Error.png"));
			}
		} else if (ghostType == GhostType.PINKY) {
			switch (direction) {
			case RIGHT:
				this.image = new Image(new FileInputStream("./Ghosts/PinkyRight.gif"));
				break;
			case LEFT:
				this.image = new Image(new FileInputStream("./Ghosts/PinkyLeft.gif"));
				break;
			case UP:
				this.image = new Image(new FileInputStream("./Ghosts/PinkyUp.gif"));
				break;
			case DOWN:
				this.image = new Image(new FileInputStream("./Ghosts/PinkyDown.gif"));
				break;
			default:
				this.image = new Image(new FileInputStream("./Tiles/Error.png"));
			}
		} else if (ghostType == GhostType.CLYDE) {
			switch (direction) {
			case RIGHT:
				this.image = new Image(new FileInputStream("./Ghosts/ClydeRight.gif"));
				break;
			case LEFT:
				this.image = new Image(new FileInputStream("./Ghosts/ClydeLeft.gif"));
				break;
			case UP:
				this.image = new Image(new FileInputStream("./Ghosts/ClydeUp.gif"));
				break;
			case DOWN:
				this.image = new Image(new FileInputStream("./Ghosts/ClydeDown.gif"));
				break;
			default:
				this.image = new Image(new FileInputStream("./Tiles/Error.png"));
			}
		}

		if (state == GhostState.SCARED) {
			this.image = new Image(new FileInputStream("./Ghosts/ScaredGhost.gif"));
		}

		if (state == GhostState.DEFEATED) {
			this.image = new Image(new FileInputStream("./Ghosts/Eyes.gif"));
		}

		this.direction = direction;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public Point2D getPosition() {
		return this.position;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public void doCountdown() {
		countdown--;
	}

	public int getCountdown() {
		return this.countdown;
	}

	public GhostState getState() {
		return this.state;
	}

	public void setState(GhostState state) throws Exception {
		this.state = state;

		switch (this.state) {
		case SCARED:
			this.image = new Image(new FileInputStream("./Ghosts/ScaredGhost.gif"));
			break;
		case NORMAL:
			this.setDirection(this.direction);
			break;
		case DEFEATED:
			this.position = this.home;
			this.image = new Image(new FileInputStream("./Ghosts/Eyes.gif"));
			this.countdown = 25;
		}
	}

	public void setVelocity(Point2D velocity) {
		this.velocity = velocity;
	}

	public Point2D getVelocity() {
		return this.velocity;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return this.image;
	}

	public void setHome(Point2D home) {
		this.home = home;
	}

	public Point2D getHome() {
		return this.home;
	}

}