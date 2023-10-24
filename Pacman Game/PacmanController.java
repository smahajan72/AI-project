import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

public class PacmanController implements EventHandler<KeyEvent> {
	final private static double FPS = 5.0;

	private PacmanModel pacmanModel;
	private PacmanView pacmanView;

	private Timer timer;
	private boolean paused;

	public PacmanController(PacmanModel model, PacmanView view) throws Exception {
		this.pacmanModel = model;
		this.pacmanView = view;
		this.paused = false;
		// System.out.println("PacmanController about to initialize()...");
		// initialize();
	}

	public void initialize() throws Exception {
		update(Direction.NONE);
		this.startTimer();
	}

    private void startTimer() throws Exception {
        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                    	try {
                    		// System.out.println("This should print out 5 times per second");
                    		update(pacmanModel.getPacman().getDirection());
                    	} catch (Exception e) {
                    		System.out.println(e.getMessage());
                    	}
                    }
                });
            }
        };

        long frameTime = (long)(1000.0 / FPS);
        this.timer.schedule(timerTask, 0, frameTime);
    }

    public void terminate() throws Exception {
		timer.cancel();
	}
	
	public void pause() throws Exception {
		timer.cancel();
		paused = true;
	}

	public void unpause() throws Exception {
		paused = false;
		startTimer();
	}

	public void update(Direction direction) throws Exception {
		this.pacmanModel.step(direction);
		this.pacmanView.update(pacmanModel, pacmanView.getStage());
	}

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
			// System.out.println("You pressed a button");
			try {
				pacmanModel.getPacman().setDirection(direction);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
			// pacmanModel.getPacman().setDirection(direction);
		}
	}

	public PacmanModel getModel() {
		return pacmanModel;
	}
}