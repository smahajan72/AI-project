import javafx.scene.image.Image;
import javafx.geometry.Point2D;
import java.io.File;
import java.io.FileInputStream;

public class Tile {

	private Image image;
	public boolean isDecisionPoint;
	public boolean isWarpPoint;
	private Point2D warpLocation;
	private TileType type;

	public Tile(TileType type) throws Exception {
		this.image = new Image(new FileInputStream("./Tiles/Error.png"));
		this.type = type;
		this.isDecisionPoint = false;
		this.isWarpPoint = false;
		this.warpLocation = new Point2D(0, 0);
	}

	public Tile(TileType type, Image image) throws Exception {
		this.image = image;
		this.type = type;
		this.isDecisionPoint = false;
		this.isWarpPoint = false;
		this.warpLocation = new Point2D(0, 0);
	}

	public Tile(TileType type, Image image, boolean isDecisionPoint) throws Exception {
		this.image = image;
		this.type = type;
		this.isDecisionPoint = isDecisionPoint;
		this.isWarpPoint = false;
		this.warpLocation = new Point2D(0, 0);
	}

	public Point2D getWarpLocation() {
		return warpLocation;
	}

	public void setWarpLocation(Point2D warpLocation) {
		this.warpLocation = warpLocation;
	}

	public TileType getType() throws Exception {
		return type;
	}

	public Image getImage() throws Exception {
		return image;
	}

	public void setType(TileType type) throws Exception {
		this.type = type;
	}

	public void setImage(Image image) throws Exception {
		this.image = image;
	}
}