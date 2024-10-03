package dodge.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {

	private int height;
	private int width;
	private int score = 0;
	private int enemySpeed = 200;
	private int enemySpawnFrequency = 1000;

	private Tile[][] board;
	private Tile player;

	private boolean isGameOver, isGamePaused = false;
	private List<Enemy> activeEnemies = new ArrayList<>(); // Bruker lister istedet for collection fordi vi trenger
															// index i bruk av random

	public Game(int width, int height) {
		if (width <= 0 || height <= 0 || width > 22 || height > 12) {
			throw new IllegalArgumentException("The board must be at least 1x1, and maximum 22x12");
		}
		this.height = height;
		this.width = width;
		this.board = new Tile[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				board[y][x] = new Tile(x, y);
			}
		}

		createEnemies();
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void quit() {
		System.exit(0);
	}

	public void addActiveEnemy(Enemy enemy) {
		activeEnemies.add(enemy);
	}

	public boolean getGameOver() {
		return isGameOver;
	}

	public void setGameOver() {
		isGameOver = true;
	}

	public boolean getGamePaused() {
		return isGamePaused;
	}

	public void setGamePaused() {
		isGamePaused = !isGamePaused;
	}

	public Tile[][] getBoard() {
		Tile[][] returnBoard = board;
		return returnBoard;
	}

	public void setBoard(Tile[][] board) {
		this.board = board;
	}

	private boolean isTile(int x, int y) {
		return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
	}

	public Tile getTile(int x, int y) {
		if (!isTile(x, y)) {
			throw new IllegalArgumentException("Coordinates out of bounds");
		}
		return this.board[y][x];
	}

	public void setStartPlayer() {
		this.player = new Tile(width / 2, height / 2);
		placePlayerOnBoard();
	}

	public void setPosPlayer(int x, int y) {
		if (x < 1 || x >= width - 1 || y < 1 || y >= height - 1) {
			throw new IllegalArgumentException("The player's position is outside it's assigned area");
		}
		this.player = new Tile(x, y);
		placePlayerOnBoard();
	}

	private void placePlayerOnBoard() {
		board[player.getY()][player.getX()].setPlayerType();
	}

	public void moveUp() {
		movePlayer(0, -1);
	}

	public void moveDown() {
		movePlayer(0, 1);
	}

	public void moveRight() {
		movePlayer(1, 0);
	}

	public void moveLeft() {
		movePlayer(-1, 0);
	}

	private boolean checkPlayerValid(int dx, int dy) {
		if (player == null
				|| getGameOver()
				|| (player.getX() + dx) < 1
				|| (player.getX() + dx) >= (getWidth() - 1)
				|| (player.getY() + dy) < 1
				|| (player.getY() + dy) >= (getHeight() - 1)
				|| dx > 1 || dx < -1 || dy > 1 || dx < -1
				|| getGamePaused()) {
			return false;
		}
		return true;
	}

	private boolean canPlayerMove(int dx, int dy) { 
		if !(!checkPlayerValid(dx, dy)) {
			return false;
		}

		int x = player.getX() + dx;
		int y = player.getY() + dy;
		
		return isTile(x, y);
	}

	private void movePlayer(int dx, int dy) {
		if (!canPlayerMove(dx, dy)) {
			return;
		}
		int gx = player.getX();
		int gy = player.getY();
		board[gy][gx].clearTile();

		int nx = player.getX() + dx;
		int ny = player.getY() + dy;

		if (board[ny][nx].isEnemy()) {
			setGameOver();
		}

		player.setX(nx);
		player.setY(ny);

		board[ny][nx].setPlayerType();
	}

	public int getEnemySpeed() {
		return enemySpeed;
	}

	public Enemy getEnemy(int x, int y) {
		for (Enemy enemy : activeEnemies) {
			if (enemy.getPosX() == x && enemy.getPosY() == y) {
				return enemy;
			}
		}
		throw new IllegalArgumentException("Tried to get non-existing enemies");
	}

	private void createEnemies() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				if (getGameOver() || getGamePaused()) {
					return;
				}

				Enemy runnableEnemy = new Enemy(width, height);
				activeEnemies.add(runnableEnemy);
				runnableEnemy.setRandomDirection();
				runnableEnemy.setStartPos();

				startMovementEnemy(runnableEnemy, runnableEnemy.getDirection());
			}
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(runnable, 500, enemySpawnFrequency, TimeUnit.MILLISECONDS);
	}

	public void startMovementEnemy(Enemy enemy, String direction) {
		Runnable runnable = new Runnable() {
			boolean addToScore = true;

			@Override
			public void run() {
				if (getGameOver() || getGamePaused()) {
					return;
				}

				List<Integer> nextPos = enemy.getNextPosEnemy(direction, enemy.getPosX(), enemy.getPosY());

				int nx = nextPos.get(0);
				int ny = nextPos.get(1);

				int gx = enemy.getPosX();
				int gy = enemy.getPosY();

				board[gy][gx].clearTile();

				if (!enemy.canMove(nx, ny)) {
					if (addToScore) {
						score++;
						activeEnemies.remove(enemy);
						addToScore = false;
					}
					return;
				}

				enemy.setPosX(nx);
				enemy.setPosY(ny);

				if (board[ny][nx].isPlayer()) {
					setGameOver();
				}
				if (enemy.getPosX() > getWidth() && enemy.getPosY() > getHeight()) {
					return;
				}

				board[ny][nx].setEnemyType();
			}
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(runnable, 100, enemySpeed, TimeUnit.MILLISECONDS);
	}
}
