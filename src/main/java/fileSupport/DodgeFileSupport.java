package dodge.fxui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import dodge.model.Game;
import dodge.model.Enemy;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DodgeFileSupport implements DodgeFileReading{
	
	private final static String DODGE_EXTENSION = ".dodge.txt";
	
	public Path getDodgeUserFolderPath() { 
		return Path.of(System.getProperty("user.home"), "tdt4100", "dodge");
	}
	
	public Path getDodgePath(String filename) {
        return getDodgeUserFolderPath().resolve(filename + DODGE_EXTENSION);
    }
	
	private boolean ensureDodgeUserFolder(Game game) {
		try {
            Files.createDirectories(getDodgeUserFolderPath());
            return true;
        } catch (IOException ioe) {
        	Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Could not load from file");
			alert.setContentText("The program could not find or create path to save game state.");
			game.setGamePaused();
			alert.showAndWait();
            return false;
        }
	}
	
	@Override
	public void saveGameState(String filename, Game game) throws FileNotFoundException{
		Path dodgePath = getDodgePath(filename);
		ensureDodgeUserFolder(game);
		
		try (PrintWriter writer = new PrintWriter(dodgePath.normalize().toString())) { 
			writer.println(game.getWidth());
			writer.println(game.getHeight());
			writer.println(game.getScore());
			writer.println(game.getGameOver());
			
			List<String> enemyDirections = new ArrayList<>();
			for (int y = 0; y < game.getHeight(); y++) {
				for (int x = 0; x < game.getWidth(); x++) {
					writer.print(game.getTile(x, y)); 
					if (game.getTile(x, y).isEnemy()) { 
						enemyDirections.add(game.getEnemy(x, y).getDirection()); 
					}
				}
			}
			writer.println(); 
			writer.println(String.join(",", enemyDirections)); 
			writer.flush();
		}
	}
	
	@Override
	public Game loadGameState(String filename, Game game) throws IOException {
		Path dodgePath = getDodgePath(filename); 
		game = null;
		
		try (Scanner scanner = new Scanner(new File(dodgePath.normalize().toString()))) {
			int width = scanner.nextInt();
			int height = scanner.nextInt();
			int score = scanner.nextInt();
			
			Game loadedGame = new Game(width, height); 
			loadedGame.setScore(score); 

			if (scanner.nextBoolean()) {
				loadedGame.setGameOver();
			}

			scanner.nextLine();
			String board = scanner.nextLine();
			String directionArray = scanner.nextLine();
			
			List<String> directions = Arrays.asList(directionArray.split(","));

			int enemyIndex = 0;
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					char type = board.charAt(y * width + x);
					loadedGame.getTile(x, y).setType(type);
					
					if (loadedGame.getTile(x, y).isPlayer()) {
						loadedGame.setPosPlayer(x, y);
					}
					if (loadedGame.getTile(x, y).isEnemy()) {
						Enemy savedEnemy = new Enemy(width, height);
						loadedGame.addActiveEnemy(savedEnemy);
						
						String direction = directions.get(enemyIndex);
						enemyIndex++;
						savedEnemy.setSavedDirection(direction);
						savedEnemy.setPosX(x);
						savedEnemy.setPosY(y);

						loadedGame.startMovementEnemy(savedEnemy, savedEnemy.getDirection());
					}
				}
			}
			loadedGame.setGamePaused();
			return loadedGame;
		}
	}
}
