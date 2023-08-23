package dodge.model;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;
import dodge.fxui.DodgeFileSupport;
import org.junit.jupiter.api.DisplayName;

public class DodgeFileSupportTest {
	
	private DodgeFileSupport fileObjects;
	private int width = 16;
	private int height = 12;
	private Game game;
	
	@BeforeEach
	public void setup() {
		fileObjects = new DodgeFileSupport();
	}
	
/*Hjelpe-metoder for testing-------------------------------------------------------*/
	private void setupOneEnemy(int x, int y, String direction) {
		Enemy enemy = new Enemy(width, height);
		enemy.setPosX(x);
		enemy.setPosY(y);
		enemy.setSavedDirection(direction);
		
		Tile[][] board = game.getBoard();
		board[y][x].setEnemyType();
		game.setBoard(board);
		game.addActiveEnemy(enemy);
	}
	
	private void setupEnemies() {
		setupOneEnemy(1, 2, "UP");
		setupOneEnemy(1, 3, "DOWN");
		setupOneEnemy(1, 4, "LEFT");
		setupOneEnemy(1, 5, "RIGHT");
	}
	
	private Path getFileFromResource(String filename) throws FileNotFoundException {
		return Paths.get("src", "test", "resources").resolve(filename);
	}
	
	/*Faktisk testing-------------------------------------------------------*/
	
	@Test
	@DisplayName("Tester at saveGameState lagrer spillet riktig")
	public void testSaveGameState() { 
		game = new Game(width, height);
		game.setStartPlayer();
		setupEnemies();
		
		try {
			fileObjects.saveGameState("newGameState", game);
		} catch (FileNotFoundException e) {
			fail("Kunne ikke lagre fil");
		}
		
		byte[] testFile = null, newFile = null;
		
		try {
			newFile = Files.readAllBytes(fileObjects.getDodgePath("newGameState"));
		} catch (IOException e) {
			fail("Uventet feil i lesing fra sammenlikningsfil i testSaveGameState");
		}
		try {
			testFile = Files.readAllBytes(getFileFromResource("correctGameStateTest.dodge.txt"));
		} catch (IOException e) {
			fail("Uventet feil i lesing fra ny fil i test av testSaveGameState");
		}
		
		assertNotNull(testFile);
		assertNotNull(newFile);
		assertTrue(Arrays.equals(testFile, newFile), "Den lagrede filen stemmer ikke med fasiten");
	}
	
	
	@Test
	@DisplayName("Tester at loadGameState laster spillet riktig")
	public void testLoadGameState() { 
		//Lager en kopi av riktig fil i current directory
		Path destination = fileObjects.getDodgePath("kopiAvFasitTest");
		Path source;
		
		try {
			source = getFileFromResource("correctGameStateTest.dodge.txt");
		} catch (FileNotFoundException e1) {
			fail("Feil i lasting av fil til sammenlikning testLoadGameState");
			return;
		}

		try {
			Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {
			fail("Feil under kopiering av filer til oppsett for testLoadGameState");
		}
		
		try {
			game = fileObjects.loadGameState("kopiAvFasitTest", game);
		} catch (IOException e) {
			fail("Uventet feil i lasting fra fasit-filen");
		}
		
		assertEquals(game.getHeight(), height, 
				"Feil høyde");
		assertEquals(game.getWidth(), width, 
				"Feil bredde");
		assertEquals(game.getScore(), 0, 
				"Feil score, skulle vært 0, er " + game.getScore());
		assertFalse(game.getGameOver(), 
				"GameOver skulle vært false");
		assertTrue(game.getGamePaused(), 
				"GamePaused skulle vært true");
		
		assertTrue(game.getTile(1, 2).isEnemy(), 
				"Feil i henting av enemies posisjon, skulle vært (1, 2)");
		assertTrue(game.getTile(1,  3).isEnemy(), 
				"Feil i henting av enemies posisjon, skulle vært (1, 3)");
		assertTrue(game.getTile(1,  4).isEnemy(), 
				"Feil i henting av enemies posisjon, skulle vært (1, 4)");
		assertTrue(game.getTile(1,  5).isEnemy(), 
				"Feil i henting av enemies posisjon, skulle vært (1, 5)");
		
		assertEquals(game.getEnemy(1,  2).getDirection(), "UP", 
				"Feil i henting av direction, skulle vært \"UP\"");
		assertEquals(game.getEnemy(1,  3).getDirection(), "DOWN", 
				"Feil i henting av direction, skulle vært \"DOWN\"");
		assertEquals(game.getEnemy(1,  4).getDirection(), "LEFT", 
				"Feil i henting av direction, , skulle vært \"LEFT\"");
		assertEquals(game.getEnemy(1,  5).getDirection(), "RIGHT", 
				"Feil i henting av direction, skulle vært \"RIGHT\"");

		assertTrue(game.getTile(width/2, height/2).isPlayer(), "Player er plassert feil");
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (!(x == 1 && 0<y && y<6) && !(x==width/2 && y==height/2)) { //hvis ikke enemy og ikke player
					assertTrue(game.getTile(x, y).isClear(), "Enemy eller Player tiles er flere steder på brettet enn de skal være etter lasting");
				}
			}
		}
	}
	
	
	@Test
	@DisplayName("Tester lasting fra ikke-eksisterende fil")
	public void testLoadNonExistingFile() {
		assertThrows(
				FileNotFoundException.class, 
				() -> game = fileObjects.loadGameState("filSomIkkeFinnes", game), 
				"Ga ikke feil for fil som ikke finnes");
	}
	
	
	@AfterEach //Bruker AfterEach istedet for AfterAll fordi fileObjects.getDodgePath ikke er static. Fungerer på tilsvarende måte
	public void tearDown() {
		File newTestSaveFile = new File(fileObjects.getDodgePath("newGameState").normalize().toString());
		newTestSaveFile.delete();
		
		File copyOfCorrectGameState = new File(fileObjects.getDodgePath("kopiAvFasitTest").normalize().toString());
		copyOfCorrectGameState.delete();
	}

}
