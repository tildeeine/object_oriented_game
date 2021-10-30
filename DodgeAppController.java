package dodge.fxui;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import dodge.model.Game;
import dodge.model.Tile;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class DodgeAppController {
	
	private Game game;
	private final static String FILENAME = "save_file";
	
	@FXML
	Pane board;
	
	@FXML 
	Text gameScore;
	
	@FXML
	private void initialize() {
		game = new Game(22, 12);
		game.setStartPlayer(); 
		createBoard();

		updateBoardRegularly(game.getEnemySpeed());	
	}
	
	private String getTileColor(Tile tile) {
		if(tile.isPlayer()) {
			return "#e5303a";
		} 
		if(tile.getX() == 0 || tile.getY() == 0 || tile.getX() == game.getWidth()-1 || tile.getY() == game.getHeight() -1) {
			return "#00000000";
				
		}
		if(tile.isEnemy()) {
			return "#24d628";
		}else {
			return "#020000";
		}
	}
	
	/* Board-relatert---------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	private void createBoard() {
		board.getChildren().clear();
		for (int y = 0; y < game.getHeight(); y++) {
			for (int x = 0; x < game.getWidth(); x++) {
				Pane tile = new Pane();
				tile.setTranslateX(x*26); 
				tile.setTranslateY(y*26);
				
				tile.setPrefHeight(30); 
				tile.setPrefWidth(30); 
				
				tile.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
				board.getChildren().add(tile);
			}
		}
	}
	
	private void updateBoardRegularly(int enemySpeed) { 
		Runnable runnable = new Runnable() { 
			
			@Override
			public void run() {
				drawBoard();
			}
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(runnable, 0, enemySpeed, TimeUnit.MILLISECONDS);
	}

	private void drawBoard(){
		for (int y = 0; y < game.getHeight(); y++) {
			for (int x = 0; x < game.getWidth(); x++) {
				if (game.getGameOver()) {
					board.getChildren().get(y*game.getWidth() + x).setStyle("-fx-background-color: #020000;"); 
				}
				else {
					board.getChildren().get(y*game.getWidth() + x).setStyle("-fx-background-color: " + getTileColor(game.getTile(x, y)) + ";");
				}
			}
		}
 
		if (game.getGameOver()) {
			Text lostText = new Text();
			lostText.setText("GAME OVER");
            lostText.setStyle("-fx-font-size: 40px;");
            lostText.setFill(Color.RED);
            lostText.setTranslateX(((double) game.getWidth() * 20) / 2 - 40);
            lostText.setTranslateY(((double) game.getHeight() * 20) / 2 + 40);
            board.getChildren().add(lostText);
            
            Text scoreText = new Text();
            scoreText.setText("Your score was: " + game.getScore());
            scoreText.setStyle("-fx-font-size: 20px;");
            scoreText.setFill(Color.WHITE);
            scoreText.setTranslateX(((double) game.getWidth() * 20) / 2 -8);
            scoreText.setTranslateY(((double) game.getHeight() * 20) / 2 + 90);
            board.getChildren().add(scoreText);
		}
	}
	
	/* HANDLES--------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	@FXML
	private void handleQuitGame() {
		game.quit();
	}
	
	@FXML
	private void handlePlayPauseGame() {
		if (game.getGamePaused()) {
			updateBoardRegularly(game.getEnemySpeed());
		}
		game.setGamePaused();
	}
	
	@FXML
	private void handleUp() {
		game.moveUp();
		drawBoard();
	}
	@FXML
	private void handleDown() {
		game.moveDown();
		drawBoard();
	}
	@FXML
	private void handleLeft() {
		game.moveLeft();
		drawBoard();
	}
	@FXML
	private void handleRight() {
		game.moveRight();
		drawBoard();
	}
	
	DodgeFileSupport fileSupport = new DodgeFileSupport();
	
	@FXML
	private void handleSave() {
		try {
			fileSupport.saveGameState(FILENAME, game);

		} catch (FileNotFoundException e) {
		}
	}
	
	@FXML 
	private void handleLoad() throws IOException {
		try {
			Game newGame = fileSupport.loadGameState(FILENAME, game);
			this.game = newGame;
			createBoard();
			updateBoardRegularly(game.getEnemySpeed());
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("No previous saves");
			alert.setContentText("The program could not find a previously saved file to load from.");
			game.setGamePaused();
			alert.showAndWait();
		}
	}
}
