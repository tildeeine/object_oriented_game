package dodge.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy {
	private final static List<String> DIRECTIONS = Arrays.asList("RIGHT", "LEFT", "UP", "DOWN");
	private int height;
	private int width;
	private int posX;
	private int posY;
	private String direction;

	public Enemy(int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("The enemy must be on a non-negative board");
		}
		this.height = height;
		this.width = width;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosX(int x) {
		if (x < 0 || x >= width) {
			throw new IllegalArgumentException("The position in x-direction can't be outside the board");
		}
		posX = x;
	}

	public void setPosY(int y) {
		if (y < 0 || y >= height) {
			throw new IllegalArgumentException("The position in y-direction can't be outside the board");
		}
		posY = y;
	}

	public String getDirection() {
		return direction;
	}

	private void checkDirection(String direction) {
		if (!(DIRECTIONS.contains(direction))) {
			throw new IllegalArgumentException("Direction input is not valid");
		}
	}

	public void setRandomDirection() {
		Random random = new Random();
		String randomDirection = DIRECTIONS.get(random.nextInt(DIRECTIONS.size()));
		checkDirection(randomDirection);
		direction = randomDirection;
	}

	public void setSavedDirection(String direction) {
		checkDirection(direction);
		this.direction = direction;
	}

	public void setStartPos() {
		Tile enemy = getStartTile(direction);
		posX = enemy.getX();
		posY = enemy.getY();
	}

	public boolean canMove(int nx, int ny) {
		return (!(this == null || nx >= width || ny >= height || nx < 0 || ny < 0));
	}

	public Tile getStartTile(String direction) {
		Tile start = new Tile(0, 0);
		checkDirection(direction);
		this.direction = direction;

		switch (direction) {
			case "RIGHT":
				start.setX(0);
				start.setY(ThreadLocalRandom.current().nextInt(0, height));
				break;
			case "LEFT":
				start.setX(width - 1);
				start.setY(ThreadLocalRandom.current().nextInt(0, height));
				break;
			case "UP":
				start.setX(ThreadLocalRandom.current().nextInt(0, width));
				start.setY(height - 1);
				break;
			case "DOWN":
				start.setX(ThreadLocalRandom.current().nextInt(0, width));
				start.setY(0);
				break;
			default:
				break;
		}
	}

	public List<Integer> getNextPosEnemy(String direction, int posX, int posY) {
		int x = 0;
		int y = 0;

		checkDirection(direction);

		switch (direction) {
			case "RIGHT":
				if posX > width {
					throw new IllegalArgumentException("The enemy can't move outside the board");
					break;
				}
				x = posX + 1;
				y = posY;
				break;
			case "LEFT":
				if posX < 0 {
					throw new IllegalArgumentException("The enemy can't move outside the board");
					break;
				}
				x = posX - 1;
				y = posY;
				break;
			case "UP":
				x = posX;
				y = posY - 1;
				break;
			case "DOWN":
				x = posX;
				y = posY + 1;
				break;
			default:
				break;
		}

		List<Integer> nextMoveCoordinates = Arrays.asList(x, y);

		return nextMoveCoordinates;
	}
}
