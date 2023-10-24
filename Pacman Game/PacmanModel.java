import java.lang.Math;
import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;

public class PacmanModel {

	private Tile[][] maze;
	private int score;
	private int multiplier;
	private int multiplierCountdown;
	private int repellentCountdown;
	private int level;
	private int lives;
	private Difficulty difficulty;
	private int pelletsRemaining;
	private boolean isWon;
	private boolean isLost;
	private boolean isGameOver;
	private Pacman pacman;
	private Ghost blinky;
	private Ghost inky;
	private Ghost pinky;
	private Ghost clyde;
	private FruitSpawner spawner;
	private PlayerCharacter playerChar;
	// private boolean isInvincible;
	private Direction direction;
	// private Point2D pacmanCoord;
	private Point2D pacmanHome;
	private int ghostsEaten;
	private int invMusicFlag;
	private int fxFlag;

	/**
	 * Start a new game when this object is created
	 * @param difficulty the selected game difficulty
	 * @param playerChar the selected player character
	 */
	public PacmanModel(Difficulty difficulty, PlayerCharacter playerChar) throws Exception {
		this.playerChar = playerChar;
		this.difficulty = difficulty;
		this.score = 0;
		this.level = 1;
		this.ghostsEaten = 0;
		this.multiplier = 1;
		this.multiplierCountdown = 0;
		this.repellentCountdown = 0;
		this.startGame();
	}

	public PacmanModel(Difficulty difficulty, PlayerCharacter playerChar, int score, int level, int ghostsEaten, int lives) throws Exception {
		this.playerChar = playerChar;
		this.difficulty = difficulty;
		this.score = score;
		this.level = level;
		this.ghostsEaten = ghostsEaten;
		this.lives = lives;
		this.multiplier = 1;
		this.multiplierCountdown = 0;
		this.repellentCountdown = 0;
		this.startGame();
	}

	/**
	 * Sets instance variables and initializes the game board
	 */
	public void startGame() throws Exception {
		this.isLost = false;
		this.isWon = false;
		// this.isInvincible = false;
		this.maze = initialize(difficulty);
	}

	/**
	 * Initializes the data model of the maze as a two dimensional array of tiles
	 * @param difficulty the selected difficulty level
	 * @return the two dimensional array of tiles representing the maze
	 */
	public Tile[][] initialize(Difficulty difficulty) throws Exception {
		int[][] intData = new int[24][23];

		switch (difficulty) {
		case EASY:
			intData = parseMazeData(new File("EasyMazeData.txt"));
			break;
		case MEDIUM:
			intData = parseMazeData(new File("MediumMazeData.txt"));
			break;
		case HARD:
			intData = parseMazeData(new File("HardMazeData.txt"));
			break;
		}

		// System.out.println(intData);
		// System.out.println("About to do the big loop in initialize() in PacmanModel.java");

		pelletsRemaining = 0;
		Tile[][] maze = new Tile[24][23];
		boolean firstWarpSet = false;
		Point2D firstWarp = new Point2D(0, 0);
		for (int rows = 1; rows <= 22; rows++) {
			for (int columns = 1; columns <= 21; columns++) {
				// System.out.printf("%d ", intData[rows][columns]);
				switch (intData[rows][columns]) {
				case 0:
					maze[rows][columns] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")));
					break;
				case 1:
					Tile tile = new Tile(TileType.WALL);

					boolean left = false;
                    boolean right = false;
                    boolean up = false;
                    boolean down = false;

                    if (intData[rows - 1][columns] == 1) {
                        up = true;
                    }
                    if (intData[rows + 1][columns] == 1) {
                        down = true;
                    }
                    if (intData[rows][columns - 1] == 1) {
                        left = true;
                    }
                    if (intData[rows][columns + 1] == 1) {
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
                    } else {
                        tile.setImage(new Image(new FileInputStream("./Tiles/Error.png")));
                    }

                    maze[rows][columns] = tile;
                	break;
            	case 2:
            		maze[rows][columns] = new Tile(TileType.PELLET, new Image(new FileInputStream("./Objects/Pellet.gif")));
            		pelletsRemaining++;
            		break;
            	case 3:
            		maze[rows][columns] = new Tile(TileType.POWERPELLET, new Image(new FileInputStream("./Objects/PowerPellet.gif")));
            		pelletsRemaining++;
            		break;
            	case 4:
            		maze[rows][columns] = new Tile(TileType.WARPPOINT, new Image(new FileInputStream("./Tiles/Empty.png")));
            		maze[rows][columns].isWarpPoint = true;

            		if (firstWarpSet == false) {
            			firstWarpSet = true;
            			firstWarp = new Point2D(rows, columns);
            		} else {
            			maze[rows][columns].setWarpLocation(new Point2D(firstWarp.getX(), firstWarp.getY()));
            			maze[(int)firstWarp.getX()][(int)firstWarp.getY()].setWarpLocation(new Point2D(rows, columns));
            		}

            		break;
            	case 5:
            		maze[rows][columns] = new Tile(TileType.GHOSTGATE, new Image(new FileInputStream("./Tiles/GhostGate.png")));
            		break;
            	case 6:
            		maze[rows][columns] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")));
            		this.pacman = new Pacman(new Point2D(rows, columns), playerChar);
            		this.pacmanHome = new Point2D(rows, columns);
            		break;
            	case 7:
            		maze[rows][columns] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")));
            		this.blinky = new Ghost(new Point2D(rows, columns), GhostType.BLINKY, new Point2D(rows, columns));
            		blinky.setCountdown(40);
            		break;
            	case 8:
            		maze[rows][columns] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")));
            		this.pinky = new Ghost(new Point2D(rows, columns), GhostType.PINKY, new Point2D(rows, columns));
            		pinky.setCountdown(25);
            		break;
            	case 9:
            		maze[rows][columns] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")));
            		this.inky = new Ghost(new Point2D(rows, columns), GhostType.INKY, new Point2D(rows, columns));
            		inky.setCountdown(25);
            		break;
            	case 10:
            		maze[rows][columns] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")));
            		this.clyde = new Ghost(new Point2D(rows, columns), GhostType.CLYDE, new Point2D(rows, columns));
            		clyde.setCountdown(10);
            		break;
            	case 11:
            		this.spawner = new FruitSpawner(new Point2D(rows, columns));
            		maze[rows][columns] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")));
            		break;
            	default:
            		maze[rows][columns] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")));
				}
			}
			// System.out.printf("\n");
		}

		//System.out.println("About to look for decision points in PacmanModel.java ...");

		// Find and mark decision points
		for (int rows = 1; rows <= 22; rows++) {
			for (int columns = 2; columns <= 20; columns++) {
				//System.out.printf("rows: %d, columns: %d\n", rows, columns);
				if (maze[rows][columns].getType() != TileType.WALL) {
					//System.out.println("made it through checking if it's a wall...");
					int x = 0;

                    if (maze[rows - 1][columns].getType() != TileType.WALL) {
                        x++;
                    }
                    //System.out.println("1");
                    if (maze[rows + 1][columns].getType() != TileType.WALL) {
                        x++;
                    }
                    //System.out.println("2");
                    if (maze[rows][columns - 1].getType() != TileType.WALL) {
                    	//System.out.println("2.5");
                        x++;
                    }
                    //System.out.println("3");
                    if (maze[rows][columns + 1].getType() != TileType.WALL) {
                        x++;
                    }
                    //System.out.println("4");

                    if (x >= 3) {
                    	//System.out.println("Trying to set as a decision point...");
                    	maze[rows][columns].isDecisionPoint = true;
                    	// maze[rows][columns].setImage(new Image(new FileInputStream("./Tiles/Error.png")));
                    	//System.out.println("Successfully set.");
                    }
				}
			}
		}

		//System.out.println("PacmanModel.java initialize() finished");
		return maze;
	}

	/**
     * A method to retrieve maze data from a text file for use in building the maze.
     * @param input the text file from which to retrieve the maze data
     * @return a two dimensional int array representing the layout of the maze
     */
    public int[][] parseMazeData(File input) throws IOException {
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
                    // System.out.printf("%d ", maze[i][j]);
                    j++;
                }
                // System.out.printf("\n");
                i++;
            }
            return maze;
        } catch (IOException e) {
            // System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * A method to move Pacman through the maze, checking to make sure he does not move into any walls.
     * @param direction the last directional input Pacman received
     */
    public void movePlayer(Direction direction) throws Exception {
    	Point2D newVelocity = changeDirection(direction);
    	Point2D newPosition = pacman.getPosition().add(newVelocity);

    	if (direction.equals(pacman.getLastDirection())) {
    		if (maze[(int) newPosition.getX()][(int) newPosition.getY()].getType() == TileType.WALL) {
    			pacman.setVelocity(changeDirection(Direction.NONE));
    			pacman.setLastDirection(Direction.NONE);
    		} else {
    			pacman.setVelocity(newVelocity);
    			pacman.setPosition(newPosition);
    		}
    		if (maze[(int)pacman.getPosition().getX()][(int)pacman.getPosition().getY()].isWarpPoint) {
    			pacman.setPosition(maze[(int)pacman.getPosition().getX()][(int)pacman.getPosition().getY()].getWarpLocation());
    		}
    	} else {
    		if (maze[(int) newPosition.getX()][(int) newPosition.getY()].getType() == TileType.WALL) {
    			newVelocity = changeDirection(pacman.getLastDirection());
    			newPosition = pacman.getPosition().add(newVelocity);
    			if (maze[(int) newPosition.getX()][(int) newPosition.getY()].getType() == TileType.WALL) {
    				pacman.setVelocity(changeDirection(Direction.NONE));
    				pacman.setLastDirection(Direction.NONE);
    			} else {
    				pacman.setVelocity(changeDirection(pacman.getLastDirection()));
    				pacman.setPosition(pacman.getPosition().add(pacman.getVelocity()));
    			}
    		} else {
    			pacman.setVelocity(newVelocity);
    			pacman.setPosition(newPosition);
    			pacman.setDirection(direction);
    			pacman.setLastDirection(direction);
    		}
    		if (maze[(int)pacman.getPosition().getX()][(int)pacman.getPosition().getY()].isWarpPoint) {
    			pacman.setPosition(maze[(int)pacman.getPosition().getX()][(int)pacman.getPosition().getY()].getWarpLocation());
    		}
    	}
    }

    /**
     * A method to move the ghosts through the maze, making decisions about which direction to move
     */
    public void moveGhosts() throws Exception {
    	Random random = new Random();

    	// MOVING BLINKY (the red one)
    	// Blinky will continue traveling in the same direction until he reaches a decision point. When
    	// he does, he will choose which direction to go based on which direction will put him closest
    	// to Pacman.
    	if (difficulty != Difficulty.EASY) {
    		if (blinky.getCountdown() == 0) {
	    		Point2D blinkyPos = blinky.getPosition();
		    	Point2D potentialBlinkyPos = blinky.getPosition().add(blinky.getVelocity());
	    		if (maze[(int) blinkyPos.getX()][(int) blinkyPos.getY()].isDecisionPoint) {
	    			double bLeftDist = -99;
	    			if (maze[(int)blinkyPos.getX()][(int)blinkyPos.getY() - 1].getType() != TileType.WALL) {
	    				Point2D bLeft = new Point2D(blinky.getPosition().getX(), blinky.getPosition().getY() - 1);
		    			bLeftDist = bLeft.distance(pacman.getPosition());
	    			}
	    			double bRightDist = -99;
	    			if (maze[(int)blinkyPos.getX()][(int)blinkyPos.getY() + 1].getType() != TileType.WALL) {
		    			Point2D bRight = new Point2D(blinky.getPosition().getX(), blinky.getPosition().getY() + 1);
	    				bRightDist = bRight.distance(pacman.getPosition());
	    			}
	    			double bUpDist = -99;
		    		if (maze[(int)blinkyPos.getX() - 1][(int)blinkyPos.getY()].getType() != TileType.WALL) {
	    				Point2D bUp = new Point2D(blinky.getPosition().getX() - 1, blinky.getPosition().getY());
	    				bUpDist = bUp.distance(pacman.getPosition());
	    			}
		    		double bDownDist = -99;
	    			if (maze[(int)blinkyPos.getX() + 1][(int)blinkyPos.getY()].getType() != TileType.WALL) {
	    				Point2D bDown = new Point2D(blinky.getPosition().getX() + 1, blinky.getPosition().getY());
	    				bDownDist = bDown.distance(pacman.getPosition());
		    		}

		    		double lowestDist = 0;

		    		if (blinky.getState() == GhostState.SCARED || pacman.getState() == PacmanState.REPELLENT) {
		    			lowestDist = Math.max(Math.max(bLeftDist, bRightDist), Math.max(bUpDist, bDownDist));
		    		} else {
						lowestDist = Math.min(Math.min(Math.abs(bLeftDist), Math.abs(bRightDist)), Math.min(Math.abs(bUpDist), Math.abs(bDownDist)));		    			
		    		}

		    		// double lowestDist = Math.min(Math.min(bLeftDist, bRightDist), Math.min(bUpDist, bDownDist));
	    			Direction newBlinkyDir = Direction.NONE;

		    		if (lowestDist == bLeftDist) {
	    				newBlinkyDir = Direction.LEFT;
	    			} else if (lowestDist == bRightDist) {
	    				newBlinkyDir = Direction.RIGHT;
		    		} else if (lowestDist == bUpDist) {
	    				newBlinkyDir = Direction.UP;
	    			} else if (lowestDist == bDownDist) {
	    				newBlinkyDir = Direction.DOWN;
		    		}

		    		blinky.setDirection(newBlinkyDir);
	    			blinky.setVelocity(changeDirection(newBlinkyDir));
	    			blinky.setPosition(blinky.getPosition().add(blinky.getVelocity()));

	    			if (maze[(int)blinky.getPosition().getX()][(int)blinky.getPosition().getY()].isWarpPoint) {
	    				blinky.setPosition(maze[(int)blinky.getPosition().getX()][(int)blinky.getPosition().getY()].getWarpLocation());
	    			}
		    	} else if (maze[(int)potentialBlinkyPos.getX()][(int)potentialBlinkyPos.getY()].getType() == TileType.WALL) {
	    			Direction newBlinkyDir = Direction.NONE;

		    		if (maze[(int)blinkyPos.getX()][(int)blinkyPos.getY() - 1].getType() != TileType.WALL) {
	    				if (blinky.getDirection() != Direction.RIGHT) {
	    					newBlinkyDir = Direction.LEFT;
	    				}
		    		}
	    			if (maze[(int) blinkyPos.getX()][(int) blinkyPos.getY() + 1].getType() != TileType.WALL) {
	    				if (blinky.getDirection() != Direction.LEFT) {
	    					newBlinkyDir = Direction.RIGHT;
	    				}
		    		}
	    			if (maze[(int) blinkyPos.getX() - 1][(int) blinkyPos.getY()].getType() != TileType.WALL) {
	    				if (blinky.getDirection() != Direction.DOWN) {
	    					newBlinkyDir = Direction.UP;
	    				}
		    		}
	    			if (maze[(int) blinkyPos.getX() + 1][(int) blinkyPos.getY()].getType() != TileType.WALL) {
	    				if (blinky.getDirection() != Direction.UP) {
	    					newBlinkyDir = Direction.DOWN;
	    				}
		    		}

		    		if (newBlinkyDir == Direction.NONE) {
	    				if (maze[(int)blinkyPos.getX()][(int)blinkyPos.getY() - 1].getType() != TileType.WALL) {
	    					newBlinkyDir = Direction.LEFT;
		    			}
	    				if (maze[(int) blinkyPos.getX()][(int) blinkyPos.getY() + 1].getType() != TileType.WALL) {
	    					newBlinkyDir = Direction.RIGHT;
			    		}
	    				if (maze[(int) blinkyPos.getX() - 1][(int) blinkyPos.getY()].getType() != TileType.WALL) {
	    					newBlinkyDir = Direction.UP;
		    			}
	    				if (maze[(int) blinkyPos.getX() + 1][(int) blinkyPos.getY()].getType() != TileType.WALL) {
	    					newBlinkyDir = Direction.DOWN;
			    		}
	    			}

		    		blinky.setDirection(newBlinkyDir);
	    			blinky.setVelocity(changeDirection(newBlinkyDir));
	    			blinky.setPosition(blinky.getPosition().add(blinky.getVelocity()));

	    			if (maze[(int)blinky.getPosition().getX()][(int)blinky.getPosition().getY()].isWarpPoint) {
	    				blinky.setPosition(maze[(int)blinky.getPosition().getX()][(int)blinky.getPosition().getY()].getWarpLocation());
	    			}
		    	} else {
	    			blinky.setPosition(potentialBlinkyPos);

	    			if (maze[(int)blinky.getPosition().getX()][(int)blinky.getPosition().getY()].isWarpPoint) {
	    				blinky.setPosition(maze[(int)blinky.getPosition().getX()][(int)blinky.getPosition().getY()].getWarpLocation());
	    			}
	    		}
    		} else {
    			blinky.doCountdown();
    		}
    	}

    	// MOVING INKY (the blue one)
    	// Inky will try to work together with Pinky to trap Pacman. Whenever he reaches a decision point,
    	// he will try to move towards the tile 4 spaces to the right of Pacman if Pacman is moving left
    	// or right, or towards the tile 4 spaces above Pacman if Pacman is moving up or down.
    	if (inky.getCountdown() == 0) {
	    	Point2D inkyPos = inky.getPosition();
	    	Point2D potentialInkyPos = inky.getPosition().add(inky.getVelocity());
	    	if (maze[(int) inkyPos.getX()][(int) inkyPos.getY()].isDecisionPoint && (pacman.getDirection() == Direction.LEFT || pacman.getDirection() == Direction.RIGHT)) {
	    		double iLeftDist = -99;
	    		if (maze[(int)inkyPos.getX()][(int)inkyPos.getY() - 1].getType() != TileType.WALL) {
	    			Point2D iLeft = new Point2D(inky.getPosition().getX(), inky.getPosition().getY() - 1);
	    			iLeftDist = iLeft.distance(pacman.getPosition().getX() + 4, pacman.getPosition().getY());
	    		}
	    		double iRightDist = -99;
	    		if (maze[(int)inkyPos.getX()][(int)inkyPos.getY() + 1].getType() != TileType.WALL) {
	    			Point2D iRight = new Point2D(inky.getPosition().getX(), inky.getPosition().getY() + 1);
	    			iRightDist = iRight.distance(pacman.getPosition().getX() + 4, pacman.getPosition().getY());
	    		}
	    		double iUpDist = -99;
	    		if (maze[(int)inkyPos.getX() - 1][(int)inkyPos.getY()].getType() != TileType.WALL) {
	    			Point2D iUp = new Point2D(inky.getPosition().getX() - 1, inky.getPosition().getY());
	    			iUpDist = iUp.distance(pacman.getPosition().getX() + 4, pacman.getPosition().getY());
	    		}
	    		double iDownDist = -99;
	    		if (maze[(int)inkyPos.getX() + 1][(int)inkyPos.getY()].getType() != TileType.WALL) {
	    			Point2D iDown = new Point2D(inky.getPosition().getX() + 1, inky.getPosition().getY());
	    			iDownDist = iDown.distance(pacman.getPosition().getX() + 4, pacman.getPosition().getY());
	    		}

	    		double lowestDist = 0;

	    		if (inky.getState() == GhostState.SCARED || pacman.getState() == PacmanState.REPELLENT) {
		    		lowestDist = Math.max(Math.max(iLeftDist, iRightDist), Math.max(iUpDist, iDownDist));
		    	} else {
					lowestDist = Math.min(Math.min(Math.abs(iLeftDist), Math.abs(iRightDist)), Math.min(Math.abs(iUpDist), Math.abs(iDownDist)));		    			
		    	}

	    		// double lowestDist = Math.min(Math.min(iLeftDist, iRightDist), Math.min(iUpDist, iDownDist));
	    		Direction newInkyDir = Direction.NONE;

	    		if (lowestDist == iLeftDist) {
	    			newInkyDir = Direction.LEFT;
	    		} else if (lowestDist == iRightDist) {
	    			newInkyDir = Direction.RIGHT;
	    		} else if (lowestDist == iUpDist) {
	    			newInkyDir = Direction.UP;
	    		} else if (lowestDist == iDownDist) {
	    			newInkyDir = Direction.DOWN;
	    		}

	    		inky.setDirection(newInkyDir);
	    		inky.setVelocity(changeDirection(newInkyDir));
	    		inky.setPosition(inky.getPosition().add(inky.getVelocity()));

	    		if (maze[(int)inky.getPosition().getX()][(int)inky.getPosition().getY()].isWarpPoint) {
	    			inky.setPosition(maze[(int)inky.getPosition().getX()][(int)inky.getPosition().getY()].getWarpLocation());
	    		}
	    	} else if (maze[(int) inkyPos.getX()][(int) inkyPos.getY()].isDecisionPoint && (pacman.getDirection() == Direction.UP || pacman.getDirection() == Direction.DOWN)) {
	    		double iLeftDist = -99;
	    		if (maze[(int)inkyPos.getX()][(int)inkyPos.getY() - 1].getType() != TileType.WALL) {
	    			Point2D iLeft = new Point2D(inky.getPosition().getX(), inky.getPosition().getY() - 1);
	    			iLeftDist = iLeft.distance(pacman.getPosition().getX(), pacman.getPosition().getY() + 4);
	    		}
	    		double iRightDist = -99;
	    		if (maze[(int)inkyPos.getX()][(int)inkyPos.getY() + 1].getType() != TileType.WALL) {
	    			Point2D iRight = new Point2D(inky.getPosition().getX(), inky.getPosition().getY() + 1);
	    			iRightDist = iRight.distance(pacman.getPosition().getX(), pacman.getPosition().getY() + 4);
	    		}
	    		double iUpDist = -99;
	    		if (maze[(int)inkyPos.getX() - 1][(int)inkyPos.getY()].getType() != TileType.WALL) {
	    			Point2D iUp = new Point2D(inky.getPosition().getX() - 1, inky.getPosition().getY());
	    			iUpDist = iUp.distance(pacman.getPosition().getX(), pacman.getPosition().getY() + 4);
	    		}
	    		double iDownDist = -99;
	    		if (maze[(int)inkyPos.getX() + 1][(int)inkyPos.getY()].getType() != TileType.WALL) {
	    			Point2D iDown = new Point2D(inky.getPosition().getX() + 1, inky.getPosition().getY());
	    			iDownDist = iDown.distance(pacman.getPosition().getX(), pacman.getPosition().getY() + 4);
	    		}

	    		double lowestDist = 0;

	    		if (inky.getState() == GhostState.SCARED || pacman.getState() == PacmanState.REPELLENT) {
		    		lowestDist = Math.max(Math.max(iLeftDist, iRightDist), Math.max(iUpDist, iDownDist));
		    	} else {
					lowestDist = Math.min(Math.min(Math.abs(iLeftDist), Math.abs(iRightDist)), Math.min(Math.abs(iUpDist), Math.abs(iDownDist)));		    			
		    	}

	    		//double lowestDist = Math.min(Math.min(iLeftDist, iRightDist), Math.min(iUpDist, iDownDist));
	    		Direction newInkyDir = Direction.NONE;

	    		if (lowestDist == iLeftDist) {
	    			newInkyDir = Direction.LEFT;
	    		} else if (lowestDist == iRightDist) {
	    			newInkyDir = Direction.RIGHT;
	    		} else if (lowestDist == iUpDist) {
	    			newInkyDir = Direction.UP;
	    		} else if (lowestDist == iDownDist) {
	    			newInkyDir = Direction.DOWN;
	    		}

	    		inky.setDirection(newInkyDir);
	    		inky.setVelocity(changeDirection(newInkyDir));
	    		inky.setPosition(inky.getPosition().add(inky.getVelocity()));

	    		if (maze[(int)inky.getPosition().getX()][(int)inky.getPosition().getY()].isWarpPoint) {
	    			inky.setPosition(maze[(int)inky.getPosition().getX()][(int)inky.getPosition().getY()].getWarpLocation());
	    		}
	    	} else if (maze[(int)potentialInkyPos.getX()][(int)potentialInkyPos.getY()].getType() == TileType.WALL) {
	    		Direction newInkyDir = Direction.NONE;

	    		if (maze[(int)inkyPos.getX()][(int)inkyPos.getY() - 1].getType() != TileType.WALL) {
	    			if (inky.getDirection() != Direction.RIGHT) {
	    				newInkyDir = Direction.LEFT;
	    			}
	    		}
	    		if (maze[(int) inkyPos.getX()][(int) inkyPos.getY() + 1].getType() != TileType.WALL) {
	    			if (inky.getDirection() != Direction.LEFT) {
	    				newInkyDir = Direction.RIGHT;
	    			}
	    		}
	    		if (maze[(int) inkyPos.getX() - 1][(int) inkyPos.getY()].getType() != TileType.WALL) {
	    			if (inky.getDirection() != Direction.DOWN) {
	    				newInkyDir = Direction.UP;
	    			}
	    		}
	    		if (maze[(int) inkyPos.getX() + 1][(int) inkyPos.getY()].getType() != TileType.WALL) {
	    			if (inky.getDirection() != Direction.UP) {
	    				newInkyDir = Direction.DOWN;
	    			}
	    		}

	    		if (newInkyDir == Direction.NONE) {
	    			if (maze[(int)inkyPos.getX()][(int)inkyPos.getY() - 1].getType() != TileType.WALL) {
	    				newInkyDir = Direction.LEFT;
		    		}
	    			if (maze[(int) inkyPos.getX()][(int) inkyPos.getY() + 1].getType() != TileType.WALL) {
	    				newInkyDir = Direction.RIGHT;
		    		}
	    			if (maze[(int) inkyPos.getX() - 1][(int) inkyPos.getY()].getType() != TileType.WALL) {
	    				newInkyDir = Direction.UP;
		    		}
	    			if (maze[(int) inkyPos.getX() + 1][(int) inkyPos.getY()].getType() != TileType.WALL) {
	    				newInkyDir = Direction.DOWN;
		    		}
	    		}

	    		inky.setDirection(newInkyDir);
	    		inky.setVelocity(changeDirection(newInkyDir));
	    		inky.setPosition(inky.getPosition().add(inky.getVelocity()));

	    		if (maze[(int)inky.getPosition().getX()][(int)inky.getPosition().getY()].isWarpPoint) {
	    			inky.setPosition(maze[(int)inky.getPosition().getX()][(int)inky.getPosition().getY()].getWarpLocation());
	    		}
	    	} else {
	    		inky.setPosition(potentialInkyPos);

	    		if (maze[(int)inky.getPosition().getX()][(int)inky.getPosition().getY()].isWarpPoint) {
	    			inky.setPosition(maze[(int)inky.getPosition().getX()][(int)inky.getPosition().getY()].getWarpLocation());
	    		}
	    	}
    	} else {
    		inky.doCountdown();
    	}

    	// MOVING PINKY (the pink one)
    	// Pinky will try to work together with Inky to trap Pacman. Whenever Pacman is moving left or right, she
    	// will move towards the tile 4 spaces to the left of him. Whenever Pacman is moving up or down, she will
    	// move towards the tile 4 spaces below him.
    	// NOTE - Pinky only exists in the Medium and Hard Difficulties.
    	if (difficulty != Difficulty.EASY) {
    		if (pinky.getCountdown() == 0) {
	    		Point2D pinkyPos = pinky.getPosition();
	    		Point2D potentialPinkyPos = pinky.getPosition().add(pinky.getVelocity());
	    		if (maze[(int) pinkyPos.getX()][(int) pinkyPos.getY()].isDecisionPoint && (pacman.getDirection() == Direction.LEFT || pacman.getDirection() == Direction.RIGHT)) {
	    			double pLeftDist = -99;
		    		if (maze[(int)pinkyPos.getX()][(int)pinkyPos.getY() - 1].getType() != TileType.WALL) {
	    				Point2D pLeft = new Point2D(pinky.getPosition().getX(), pinky.getPosition().getY() - 1);
	    				pLeftDist = pLeft.distance(pacman.getPosition().getX() - 4, pacman.getPosition().getY());
	    			}
		    		double pRightDist = -99;
	    			if (maze[(int)pinkyPos.getX()][(int)pinkyPos.getY() + 1].getType() != TileType.WALL) {
	    				Point2D pRight = new Point2D(pinky.getPosition().getX(), pinky.getPosition().getY() + 1);
	    				pRightDist = pRight.distance(pacman.getPosition().getX() - 4, pacman.getPosition().getY());
		    		}
	    			double pUpDist = -99;
	    			if (maze[(int)pinkyPos.getX() - 1][(int)pinkyPos.getY()].getType() != TileType.WALL) {
	    				Point2D pUp = new Point2D(pinky.getPosition().getX() - 1, pinky.getPosition().getY());
		    			pUpDist = pUp.distance(pacman.getPosition().getX() - 4, pacman.getPosition().getY());
	    			}
	    			double pDownDist = -99;
	    			if (maze[(int)pinkyPos.getX() + 1][(int)pinkyPos.getY()].getType() != TileType.WALL) {
		    			Point2D pDown = new Point2D(pinky.getPosition().getX() + 1, pinky.getPosition().getY());
	    				pDownDist = pDown.distance(pacman.getPosition().getX() - 4, pacman.getPosition().getY());
	    			}

	    			double lowestDist = 0;

	    			if (pinky.getState() == GhostState.SCARED || pacman.getState() == PacmanState.REPELLENT) {
		    			lowestDist = Math.max(Math.max(pLeftDist, pRightDist), Math.max(pUpDist, pDownDist));
		    		} else {
						lowestDist = Math.min(Math.min(Math.abs(pLeftDist), Math.abs(pRightDist)), Math.min(Math.abs(pUpDist), Math.abs(pDownDist)));		    			
		    		}

		    		// double lowestDist = Math.min(Math.min(pLeftDist, pRightDist), Math.min(pUpDist, pDownDist));
	    			Direction newPinkyDir = Direction.NONE;

		    		if (lowestDist == pLeftDist) {
	    				newPinkyDir = Direction.LEFT;
	    			} else if (lowestDist == pRightDist) {
	    				newPinkyDir = Direction.RIGHT;
		    		} else if (lowestDist == pUpDist) {
	    				newPinkyDir = Direction.UP;
	    			} else if (lowestDist == pDownDist) {
	    				newPinkyDir = Direction.DOWN;
		    		}

		    		pinky.setDirection(newPinkyDir);
	    			pinky.setVelocity(changeDirection(newPinkyDir));
	    			pinky.setPosition(pinky.getPosition().add(pinky.getVelocity()));

	    			if (maze[(int)pinky.getPosition().getX()][(int)pinky.getPosition().getY()].isWarpPoint) {
	    				pinky.setPosition(maze[(int)pinky.getPosition().getX()][(int)pinky.getPosition().getY()].getWarpLocation());
	    			}
		    	} else if (maze[(int) pinkyPos.getX()][(int) pinkyPos.getY()].isDecisionPoint && (pacman.getDirection() == Direction.UP || pacman.getDirection() == Direction.DOWN)) {
	    			double pLeftDist = -99;
	    			if (maze[(int)pinkyPos.getX()][(int)pinkyPos.getY() - 1].getType() != TileType.WALL) {
	    				Point2D pLeft = new Point2D(pinky.getPosition().getX(), pinky.getPosition().getY() - 1);
	    				pLeftDist = pLeft.distance(pacman.getPosition().getX(), pacman.getPosition().getY() - 4);
		    		}
	    			double pRightDist = -99;
	    			if (maze[(int)pinkyPos.getX()][(int)pinkyPos.getY() + 1].getType() != TileType.WALL) {
	    				Point2D pRight = new Point2D(pinky.getPosition().getX(), pinky.getPosition().getY() + 1);
	    				pRightDist = pRight.distance(pacman.getPosition().getX(), pacman.getPosition().getY() - 4);
		    		}
	    			double pUpDist = -99;
	    			if (maze[(int)pinkyPos.getX() - 1][(int)pinkyPos.getY()].getType() != TileType.WALL) {
	    				Point2D pUp = new Point2D(pinky.getPosition().getX() - 1, pinky.getPosition().getY());
	    				pUpDist = pUp.distance(pacman.getPosition().getX(), pacman.getPosition().getY() - 4);
		    		}
	    			double pDownDist = -99;
	    			if (maze[(int)pinkyPos.getX() + 1][(int)pinkyPos.getY()].getType() != TileType.WALL) {
	    				Point2D pDown = new Point2D(pinky.getPosition().getX() + 1, pinky.getPosition().getY());
	    				pDownDist = pDown.distance(pacman.getPosition().getX(), pacman.getPosition().getY() - 4);
	    			}

	    			double lowestDist = 0;

	    			if (pinky.getState() == GhostState.SCARED || pacman.getState() == PacmanState.REPELLENT) {
		    			lowestDist = Math.max(Math.max(pLeftDist, pRightDist), Math.max(pUpDist, pDownDist));
		    		} else {
						lowestDist = Math.min(Math.min(Math.abs(pLeftDist), Math.abs(pRightDist)), Math.min(Math.abs(pUpDist), Math.abs(pDownDist)));		    			
		    		}

		    		// double lowestDist = Math.min(Math.min(pLeftDist, pRightDist), Math.min(pUpDist, pDownDist));
	    			Direction newPinkyDir = Direction.NONE;

		    		if (lowestDist == pLeftDist) {
	    				newPinkyDir = Direction.LEFT;
	    			} else if (lowestDist == pRightDist) {
	    				newPinkyDir = Direction.RIGHT;
		    		} else if (lowestDist == pUpDist) {
	    				newPinkyDir = Direction.UP;
	    			} else if (lowestDist == pDownDist) {
	    				newPinkyDir = Direction.DOWN;
		    		}

		    		pinky.setDirection(newPinkyDir);
	    			pinky.setVelocity(changeDirection(newPinkyDir));
	    			pinky.setPosition(pinky.getPosition().add(pinky.getVelocity()));

	    			if (maze[(int)pinky.getPosition().getX()][(int)pinky.getPosition().getY()].isWarpPoint) {
	    				pinky.setPosition(maze[(int)pinky.getPosition().getX()][(int)pinky.getPosition().getY()].getWarpLocation());
	    			}
		    	} else if (maze[(int)potentialPinkyPos.getX()][(int)potentialPinkyPos.getY()].getType() == TileType.WALL) {
	    			Direction newPinkyDir = Direction.NONE;

		    		if (maze[(int)pinkyPos.getX()][(int)pinkyPos.getY() - 1].getType() != TileType.WALL) {
	    				if (pinky.getDirection() != Direction.RIGHT) {
	    					newPinkyDir = Direction.LEFT;
	    				}
		    		}
		    		if (maze[(int) pinkyPos.getX()][(int) pinkyPos.getY() + 1].getType() != TileType.WALL) {
	    				if (pinky.getDirection() != Direction.LEFT) {
	    					newPinkyDir = Direction.RIGHT;
	    				}
		    		}
	    			if (maze[(int) pinkyPos.getX() - 1][(int) pinkyPos.getY()].getType() != TileType.WALL) {
	    				if (pinky.getDirection() != Direction.DOWN) {
	    					newPinkyDir = Direction.UP;
	    				}
		    		}
	    			if (maze[(int) pinkyPos.getX() + 1][(int) pinkyPos.getY()].getType() != TileType.WALL) {
	    				if (pinky.getDirection() != Direction.UP) {
	    					newPinkyDir = Direction.DOWN;
	    				}
		    		}

		    		if (newPinkyDir == Direction.NONE) {
	    				if (maze[(int)pinkyPos.getX()][(int)pinkyPos.getY() - 1].getType() != TileType.WALL) {
	    					newPinkyDir = Direction.LEFT;
		    			}
	    				if (maze[(int) pinkyPos.getX()][(int) pinkyPos.getY() + 1].getType() != TileType.WALL) {
		    				newPinkyDir = Direction.RIGHT;
			    		}
	    				if (maze[(int) pinkyPos.getX() - 1][(int) pinkyPos.getY()].getType() != TileType.WALL) {
	    					newPinkyDir = Direction.UP;
		    			}
		    			if (maze[(int) pinkyPos.getX() + 1][(int) pinkyPos.getY()].getType() != TileType.WALL) {
	    					newPinkyDir = Direction.DOWN;
		    			}
	    			}

		    		pinky.setDirection(newPinkyDir);
	    			pinky.setVelocity(changeDirection(newPinkyDir));
	    			pinky.setPosition(pinky.getPosition().add(pinky.getVelocity()));

	    			if (maze[(int)pinky.getPosition().getX()][(int)pinky.getPosition().getY()].isWarpPoint) {
	    				pinky.setPosition(maze[(int)pinky.getPosition().getX()][(int)pinky.getPosition().getY()].getWarpLocation());
	    			}
		    	} else {
	    			pinky.setPosition(potentialPinkyPos);

	    			if (maze[(int)pinky.getPosition().getX()][(int)pinky.getPosition().getY()].isWarpPoint) {
	    				pinky.setPosition(maze[(int)pinky.getPosition().getX()][(int)pinky.getPosition().getY()].getWarpLocation());
	    			}
	    		}
    		} else {
    			pinky.doCountdown();
    		}
    	}

    	// MOVING CLYDE (the orange one)
    	// Clyde, being the oddball of the group, doesn't do much in the way of intelligent decision making.
    	// Whenever he reaches a decision point, he will choose which direction to move randomly.
    	if (clyde.getCountdown() == 0) {
	    	Point2D clydePos = clyde.getPosition();
	    	Point2D potentialClydePos = clyde.getPosition().add(clyde.getVelocity());
	    	if (maze[(int) clydePos.getX()][(int) clydePos.getY()].isDecisionPoint) {
	    		int randomDecision = random.nextInt(4);
	    		Direction newClydeDir = Direction.NONE;

	    		switch (randomDecision) {
	    		case 0:
	    			newClydeDir = Direction.LEFT;
	    			break;
	    		case 1:
	    			newClydeDir = Direction.RIGHT;
	    			break;
	    		case 2:
	    			newClydeDir = Direction.UP;
	    			break;
	    		case 3:
	    			newClydeDir = Direction.DOWN;
	    			break;
	    		default:
	    			newClydeDir = Direction.LEFT;
	    		}

	    		potentialClydePos = clyde.getPosition().add(changeDirection(newClydeDir));
	    		while (maze[(int)potentialClydePos.getX()][(int)potentialClydePos.getY()].getType() == TileType.WALL) {
	    			int randomDecision2 = random.nextInt(4);

	    			switch (randomDecision2) {
		    		case 0:
	    				newClydeDir = Direction.LEFT;
	    				break;
		    		case 1:
	    				newClydeDir = Direction.RIGHT;
	    				break;
	    			case 2:
	    				newClydeDir = Direction.UP;
		    			break;
	    			case 3:
	    				newClydeDir = Direction.DOWN;
	    				break;
	    			default:
	    				newClydeDir = Direction.LEFT;
	    			}

	    			potentialClydePos = clyde.getPosition().add(changeDirection(newClydeDir));
	    		}

	    		clyde.setDirection(newClydeDir);
	    		clyde.setVelocity(changeDirection(newClydeDir));
	    		clyde.setPosition(clyde.getPosition().add(clyde.getVelocity()));

	    		if (maze[(int)clyde.getPosition().getX()][(int)clyde.getPosition().getY()].isWarpPoint) {
	    			clyde.setPosition(maze[(int)clyde.getPosition().getX()][(int)clyde.getPosition().getY()].getWarpLocation());
	    		}
	    	} else if (maze[(int)potentialClydePos.getX()][(int)potentialClydePos.getY()].getType() == TileType.WALL) {
	    		Direction newClydeDir = Direction.NONE;

	    		if (maze[(int)clydePos.getX()][(int)clydePos.getY() - 1].getType() != TileType.WALL) {
	    			if (clyde.getDirection() != Direction.RIGHT) {
	    				newClydeDir = Direction.LEFT;
	    			}
	    		}
	    		if (maze[(int) clydePos.getX()][(int) clydePos.getY() + 1].getType() != TileType.WALL) {
	    			if (clyde.getDirection() != Direction.LEFT) {
	    				newClydeDir = Direction.RIGHT;
	    			}
	    		}
	    		if (maze[(int) clydePos.getX() - 1][(int) clydePos.getY()].getType() != TileType.WALL) {
	    			if (clyde.getDirection() != Direction.DOWN) {
	    				newClydeDir = Direction.UP;
	    			}
	    		}
	    		if (maze[(int) clydePos.getX() + 1][(int) clydePos.getY()].getType() != TileType.WALL) {
	    			if (clyde.getDirection() != Direction.UP) {
	    				newClydeDir = Direction.DOWN;
	    			}
	    		}

	    		if (newClydeDir == Direction.NONE) {
	    			if (maze[(int)clydePos.getX()][(int)clydePos.getY() - 1].getType() != TileType.WALL) {
	    				newClydeDir = Direction.LEFT;
		    		}
	    			if (maze[(int) clydePos.getX()][(int) clydePos.getY() + 1].getType() != TileType.WALL) {
	    				newClydeDir = Direction.RIGHT;
		    		}
	    			if (maze[(int) clydePos.getX() - 1][(int) clydePos.getY()].getType() != TileType.WALL) {
	    				newClydeDir = Direction.UP;
		    		}
	    			if (maze[(int) clydePos.getX() + 1][(int) clydePos.getY()].getType() != TileType.WALL) {
	    				newClydeDir = Direction.DOWN;
		    		}
	    		}
	    		

	    		clyde.setDirection(newClydeDir);
	    		clyde.setVelocity(changeDirection(newClydeDir));
	    		clyde.setPosition(clyde.getPosition().add(clyde.getVelocity()));

	    		if (maze[(int)clyde.getPosition().getX()][(int)clyde.getPosition().getY()].isWarpPoint) {
	    			clyde.setPosition(maze[(int)clyde.getPosition().getX()][(int)clyde.getPosition().getY()].getWarpLocation());
	    		}
	    	} else {
	    		clyde.setPosition(potentialClydePos);

	    		if (maze[(int)clyde.getPosition().getX()][(int)clyde.getPosition().getY()].isWarpPoint) {
	    			clyde.setPosition(maze[(int)clyde.getPosition().getX()][(int)clyde.getPosition().getY()].getWarpLocation());
	    		}
	    	}
    	} else {
    		clyde.doCountdown();
    	}
    }

    public void checkCollisions() throws Exception {

    	// Check for ghost collisions. Pacman should lose the game if he touches a ghost
    	// in its normal state. If the ghost is scared, he should eat the ghost. If the ghost is defeated, nothing
    	// should happen. Ghosts should only be scared while Pacman is invincible, and once eaten, they should stay
    	// defeated until Pacman is no longer invincible.
    	if (difficulty != Difficulty.EASY) {
    		if (pacman.getPosition().equals(blinky.getPosition()) || pacman.getPosition().equals(inky.getPosition()) || pacman.getPosition().equals(pinky.getPosition()) || pacman.getPosition().equals(clyde.getPosition())) {
    			if (pacman.getState() == PacmanState.INVINCIBLE) {
    				if (pacman.getPosition().equals(blinky.getPosition()) && blinky.getState() == GhostState.SCARED) {
    					blinky.setPosition(blinky.getHome());
    					blinky.setState(GhostState.DEFEATED);
    					ghostsEaten++;
    					blinky.setCountdown(99);
    					score += 100 * multiplier;
    				}
    				if (pacman.getPosition().equals(pinky.getPosition()) && pinky.getState() == GhostState.SCARED) {
    					pinky.setPosition(pinky.getHome());
    					pinky.setState(GhostState.DEFEATED);
    					ghostsEaten++;
    					pinky.setCountdown(99);
    					score += 100 * multiplier;
    				}
    				if (pacman.getPosition().equals(inky.getPosition()) && inky.getState() == GhostState.SCARED) {
    					inky.setPosition(inky.getHome());
    					inky.setState(GhostState.DEFEATED);
    					ghostsEaten++;
    					inky.setCountdown(99);
    					score += 100 * multiplier;
    				}
    				if (pacman.getPosition().equals(clyde.getPosition()) && clyde.getState() == GhostState.SCARED) {
    					clyde.setPosition(clyde.getHome());
    					clyde.setState(GhostState.DEFEATED);
    					ghostsEaten++;
    					clyde.setCountdown(99);
    					score += 100 * multiplier;
    				}
    			} else if (pacman.getPosition().equals(blinky.getPosition()) && pacman.getState() != PacmanState.INVINCIBLE && blinky.getState() == GhostState.NORMAL) {
    				lose();
    			} else if (pacman.getPosition().equals(pinky.getPosition()) && pacman.getState() != PacmanState.INVINCIBLE && pinky.getState() == GhostState.NORMAL) {
    				lose();
    			} else if (pacman.getPosition().equals(inky.getPosition()) && pacman.getState() != PacmanState.INVINCIBLE && inky.getState() == GhostState.NORMAL) {
    				lose();
    			} else if (pacman.getPosition().equals(clyde.getPosition()) && pacman.getState() != PacmanState.INVINCIBLE && clyde.getState() == GhostState.NORMAL) {
    				lose();
    			}
    		}
    	} else {
    		if (pacman.getPosition().equals(inky.getPosition()) || pacman.getPosition().equals(clyde.getPosition())) {
    			if (pacman.getState() == PacmanState.INVINCIBLE) {
    				if (pacman.getPosition().equals(inky.getPosition()) && inky.getState() == GhostState.SCARED) {
    					inky.setPosition(inky.getHome());
    					inky.setState(GhostState.DEFEATED);
    					ghostsEaten++;
    					inky.setCountdown(99);
    					score += 100 * multiplier;
    				}
    				if (pacman.getPosition().equals(clyde.getPosition()) && clyde.getState() == GhostState.SCARED) {
    					clyde.setPosition(clyde.getHome());
    					clyde.setState(GhostState.DEFEATED);
    					ghostsEaten++;
    					clyde.setCountdown(99);
    					score += 100 * multiplier;
    				}
    			} else if (pacman.getPosition().equals(inky.getPosition()) && pacman.getState() != PacmanState.INVINCIBLE && inky.getState() == GhostState.NORMAL) {
    				lose();
    			} else if (pacman.getPosition().equals(clyde.getPosition()) && pacman.getState() != PacmanState.INVINCIBLE && clyde.getState() == GhostState.NORMAL) {
    				lose();
    			}
    		}
    	}
    }

    public void step(Direction direction) throws Exception {

    	invMusicFlag = 0;
    	fxFlag = 0;

    	// Move Pacman and the Ghosts
    	moveGhosts();
    	checkCollisions();
    	movePlayer(direction);
    	checkCollisions();

    	// Check to see if Pacman has eaten all the pellets
    	if (pelletsRemaining == 0) {
    		win();
    	}

    	// If Pacman is not invincible, then the Ghosts need to go back to their normal state.
    	if (pacman.getState() != PacmanState.INVINCIBLE) {
    		if (difficulty != Difficulty.EASY) {
    			if (blinky.getState() == GhostState.SCARED) {
	    			blinky.setState(GhostState.NORMAL);
	    		}
	    		if (pinky.getState() == GhostState.SCARED) {
	    			pinky.setState(GhostState.NORMAL);
	    		}
    		}
    		if (inky.getState() == GhostState.SCARED) {
    			inky.setState(GhostState.NORMAL);
    		}
    		if (clyde.getState() == GhostState.SCARED) {
    			clyde.setState(GhostState.NORMAL);
    		}
    		if (difficulty != Difficulty.EASY) {
    			if (blinky.getState() == GhostState.DEFEATED) {
	    			blinky.setState(GhostState.NORMAL);
	    			blinky.setCountdown(0);
	    		}
	    		if (pinky.getState() == GhostState.DEFEATED) {
	    			pinky.setState(GhostState.NORMAL);
	    			pinky.setCountdown(0);
	    		}
    		}
    		if (inky.getState() == GhostState.DEFEATED) {
    			inky.setState(GhostState.NORMAL);
    			inky.setCountdown(0);
    		}
    		if (clyde.getState() == GhostState.DEFEATED) {
    			clyde.setState(GhostState.NORMAL);
    			clyde.setCountdown(0);
    		}
    	}

    	// Decrement the multiplier countdown if needed. If the countdown has reached zero and Pacman
    	// has an active multiplier, the multiplier should be reset to one.
    	if (multiplierCountdown == 0 && multiplier != 1) {
    		multiplier = 1;
    	} else if (multiplierCountdown != 0) {
    		multiplierCountdown--;
    	}

    	// Decrement the repellent countdown if needed. If the countdown has reached zero and Pacman
    	// is in the repellent state, Pacman should be returned to his normal state.
    	if (repellentCountdown == 0 && pacman.getState() == PacmanState.REPELLENT) {
    		pacman.setState(PacmanState.NORMAL);
    	} else if (repellentCountdown != 0) {
    		repellentCountdown--;
    	}

    	// Decrement Pacman's invincibility countdown if necessary. If Pacman is invincible and the countdown
    	// reaches zero, Pacman should be returned to his normal state, as should the ghosts.
    	if (pacman.getCountdown() != 0 && pacman.getState() == PacmanState.INVINCIBLE) {
    		pacman.doCountdown();
    	} else if (pacman.getCountdown() == 0 && pacman.getState() == PacmanState.INVINCIBLE) {
    		if (difficulty != Difficulty.EASY) {
    			if (blinky.getState() != GhostState.NORMAL) {
    				blinky.setState(GhostState.NORMAL);
    				blinky.setCountdown(0);
    			}
    			if (pinky.getState() != GhostState.NORMAL) {
    				pinky.setState(GhostState.NORMAL);
    				pinky.setCountdown(0);
    			}
    		}
    		if (inky.getState() != GhostState.NORMAL) {
    			inky.setState(GhostState.NORMAL);
    			inky.setCountdown(0);
    		}
    		if (clyde.getState() != GhostState.NORMAL) {
    			clyde.setState(GhostState.NORMAL);
    			clyde.setCountdown(0);
    		}
    		pacman.setState(PacmanState.NORMAL);
    	}

    	// Check to see if Pacman has eaten a pellet or power pellet
    	Tile currentTile = maze[(int) pacman.getPosition().getX()][(int) pacman.getPosition().getY()];
    	if (currentTile.getType() == TileType.PELLET) {
    		if (currentTile.isDecisionPoint) {
    			maze[(int) pacman.getPosition().getX()][(int) pacman.getPosition().getY()] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")), true);
    		} else {
    			maze[(int) pacman.getPosition().getX()][(int) pacman.getPosition().getY()] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")), false);
    		}

    		pelletsRemaining--;
    		score += 10 * multiplier;
    	}
    	if (currentTile.getType() == TileType.POWERPELLET) {
    		if (currentTile.isDecisionPoint) {
    			maze[(int) pacman.getPosition().getX()][(int) pacman.getPosition().getY()] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")), true);
    		} else {
    			maze[(int) pacman.getPosition().getX()][(int) pacman.getPosition().getY()] = new Tile(TileType.EMPTY, new Image(new FileInputStream("./Tiles/Empty.png")), false);
    		}

    		pelletsRemaining--;
    		score += 50 * multiplier;

    		pacman.setState(PacmanState.INVINCIBLE);
    		pacman.setCountdown(45);
    		if (difficulty != Difficulty.EASY) {
    			if (blinky.getState() == GhostState.NORMAL) {
    				blinky.setState(GhostState.SCARED);
    			}
    			if (pinky.getState() == GhostState.NORMAL) {
    				pinky.setState(GhostState.SCARED);
    			}
    		}
    		if (inky.getState() == GhostState.NORMAL) {
    			inky.setState(GhostState.SCARED);
    		}
    		if (clyde.getState() == GhostState.NORMAL) {
    			clyde.setState(GhostState.SCARED);
    		}

    		invMusicFlag = 1;
    	}

    	// Update the fruit spawner. If Pacman has eaten a fruit, one of two effects should occur based on the type
    	// of fruit he has eaten. If the fruit is a cherry, Pacman's point multiplier should be set to 2 for 6 seconds.
    	// If the fruit is a melon, Pacman should be set to a repellent state for 4 seconds. Once a fruit is eaten, 
    	// It will disappear and the spawner's countdown should be reset so that a new fruit does not spawn for the
    	// next 10 seconds.
    	spawner.update();
    	if (pacman.getPosition().equals(spawner.getPosition()) && spawner.getType() != FruitType.NONE) {
    		switch (spawner.getType()) {
    		case CHERRY:
    			multiplier = 2;
    			multiplierCountdown = 30;
    			spawner.setType(FruitType.NONE);
    			maze[(int)spawner.getPosition().getX()][(int)spawner.getPosition().getY()].setImage(spawner.getImage());
    			spawner.setCountdown(50);
    			fxFlag = 1;
    			break;
    		case MELON:
    			if (pacman.getState() != PacmanState.INVINCIBLE) {
    				pacman.setState(PacmanState.REPELLENT);
	    			repellentCountdown = 20;
	    			spawner.setType(FruitType.NONE);
	    			maze[(int)spawner.getPosition().getX()][(int)spawner.getPosition().getY()].setImage(spawner.getImage());
	    			spawner.setCountdown(50);
	    			fxFlag = 1;
    			}
    			break;
    		default:
    			maze[(int)spawner.getPosition().getX()][(int)spawner.getPosition().getY()].setImage(spawner.getImage());
    		}
    	}
    	maze[(int)spawner.getPosition().getX()][(int)spawner.getPosition().getY()].setImage(spawner.getImage());
    }

    public void lose() throws Exception {
		lives--;
		if (lives < 0) {
			isLost = true;
			isGameOver = true;
		} else {
			isLost = true;
		}
    }

    public void resetPositions() throws Exception {
    	pacman.setVelocity(new Point2D(0, 0));
		pacman.setPosition(pacmanHome);
		pacman.setDirection(Direction.NONE);
		pacman.setLastDirection(Direction.NONE);

		if (difficulty != Difficulty.EASY) {
			blinky.setPosition(blinky.getHome());
			pinky.setPosition(pinky.getHome());
		}

		inky.setPosition(inky.getHome());
		clyde.setPosition(clyde.getHome());
    }

    public void win() throws Exception {
    	isWon = true;
    }

    public Point2D changeDirection(Direction direction) throws Exception {
		switch (direction) {
		case UP:
			return new Point2D(-1, 0);
		case DOWN:
			return new Point2D(1, 0);
		case LEFT:
			return new Point2D(0, -1);
		case RIGHT:
			return new Point2D(0, 1);
		default:
			return new Point2D(0, 0);
		}
	}

	public int getFxFlag() {
		return fxFlag;
	}

	public Tile[][] getMaze() throws Exception {
		return maze;
	}

	public Tile getMazeTile(int x, int y) throws Exception {
		Tile tile = maze[x][y];
		return tile;
	}

	public Direction getDirection() throws Exception {
		return direction;
	}

	public void setDirection(Direction direction) throws Exception {
		this.direction = direction;
	}

	public int getScore() {
		return score;
	}

	public Pacman getPacman() {
		return pacman;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public Ghost getBlinky() {
		return blinky;
	}

	public Ghost getInky() {
		return inky;
	}

	public Ghost getPinky() {
		return pinky;
	}

	public Ghost getClyde() {
		return clyde;
	}

	public boolean getIsLost() {
		return isLost;
	}

	public void setIsLost(boolean isLost) {
		this.isLost = isLost;
	}

	public boolean getIsGameOver() {
		return isGameOver;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public String getDifficultyString() {
		switch (difficulty) {
		case EASY:
			return new String("Easy");
		case MEDIUM:
			return new String("Medium");
		case HARD:
			return new String("Hard");
		default:
			return new String("Medium");
		}
	}

	public int getMultiplier() {
		return multiplier;
	}

	public int getGhostsEaten() {
		return ghostsEaten;
	}

	public int getLevel() {
		return level;
	}

	public boolean getIsWon() {
		return isWon;
	}

	public String getPlayerCharString() {
		switch (playerChar) {
		case PACMAN:
			return new String("Pacman");
		case MRSPACMAN:
			return new String("Mrs. Pacman");
		case PACJR:
			return new String("Pac Jr.");
		default:
			return new String("Pacman");
		}
	}

	public int getInvMusicFlag() {
		return invMusicFlag;
	}

}