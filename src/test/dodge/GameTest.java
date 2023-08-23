package dodge.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class GameTest {
	
	private Game game;
	private int width = 6;
	private int height = 4; 
	
	@BeforeEach
	public void setup() {
		game = new Game(width, height);
	}
	
	@DisplayName("Tester konstruktøren")
	@Test
	public void testConstructur() { //good
		assertEquals(width, game.getWidth());
		assertEquals(height, game.getHeight());
		
		assertEquals(game.getWidth(), game.getBoard()[0].length);
		assertEquals(game.getHeight(), game.getBoard().length);
		
		
		game = new Game(10, 5);
		assertEquals(10, game.getWidth());
		assertEquals(5, game.getHeight()); 
		
		assertEquals(game.getWidth(), game.getBoard()[0].length);
		assertEquals(game.getHeight(), game.getBoard().length);
		
		assertThrows(IllegalArgumentException.class,() -> new Game(-1, 1), 
				"IllegalArgumentException skal kastes når man setter bredden til en negativ verdi");
		
		assertThrows(IllegalArgumentException.class,() -> new Game(0, 1), 
				"IllegalArgumentException skal kastes når man setter bredden til 0");
		
		assertThrows(IllegalArgumentException.class,() -> new Game(1, -1), 
				"IllegalArgumentException skal kastes når man setter høyden til en negativ verdi");
		
		assertThrows(IllegalArgumentException.class,() -> new Game(1, 0), 
				"IllegalArgumentException skal kastes når man setter høyden til 0");
		
		assertThrows(IllegalArgumentException.class,() -> new Game(23, 1), 
				"IllegalArgumentException skal kastes når man setter bredden over 22");
		
		assertThrows(IllegalArgumentException.class,() -> new Game(1, 13), 
				"IllegalArgumentException skal kastes når man setter høyden over 12");
	}
	
	@DisplayName("Tester getTile")
	@Test
	public void testGetTile() { 
		assertThrows(IllegalArgumentException.class,() -> game.getTile(-1, -1), 
				"IllegalArgumentException skal kastes når man kaller på en ugyldig Tile");
		assertThrows(IllegalArgumentException.class,() -> game.getTile(game.getWidth(), game.getHeight()), 
				"IllegalArgumentException skal kastes når man kaller på en ugyldig Tile");
		
		assertEquals(game.getBoard()[0][0], game.getTile(0, 0)); 
		assertEquals(game.getBoard()[3][2], game.getTile(2, 3));
		assertEquals(game.getBoard()[game.getHeight()-1][game.getWidth()-1], game.getTile(game.getWidth()-1, game.getHeight()-1)); 
	}
	
	/* Player bevegelse------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	@DisplayName("Tester initialiseringen av player på starten av spillet")
	@Test
	public void testSetStartPlayer() {	
		game.setStartPlayer();
		assertTrue(game.getTile(game.getWidth()/2, game.getHeight()/2).isPlayer());
		
		game = new Game(10, 5);
		game.setStartPlayer();
		
		assertTrue(game.getTile(game.getWidth()/2, game.getHeight()/2).isPlayer());
		
		//tester placePlayerOnBoard indirekte
		assertTrue(game.getBoard()[game.getHeight()/2][game.getWidth()/2].isPlayer()); 
	}
	
	@DisplayName("Tester posisjonen av lagret player")
	@Test
	public void testSetPosPlayer() { 
		game.setPosPlayer(width-2, height-2);
		assertTrue(game.getTile(width-2, height-2).isPlayer());
		assertThrows(IllegalArgumentException.class,() -> game.setPosPlayer(width-1, height-1), 
				"IllegalArgumentException skal kastes hvis posisjonen til den lagra spilleren er utfor spilleområdet");
		
		game.setPosPlayer(1, 1);
		assertTrue(game.getTile(1, 1).isPlayer());
		assertThrows(IllegalArgumentException.class,() -> game.setPosPlayer(0, 0), 
				"IllegalArgumentException skal kastes hvis posisjonen til den lagra spilleren er utfor spilleområdet");
	}
	
	
	@DisplayName("Tester bevegelsen til player oppover")
	@Test
	public void testMoveUp() {
		game.setStartPlayer();
		assertTrue(game.getTile(game.getWidth()/2, game.getHeight()/2).isPlayer());
		
		game.setGamePaused(); //tester indirekte canPlayerMove
		game.moveUp();
		assertFalse(game.getTile(game.getWidth()/2, game.getHeight()/2-1).isPlayer());
		
		game.setGamePaused();
		game.moveUp();
		assertTrue(game.getTile(game.getWidth()/2, game.getHeight()/2-1).isPlayer());
		assertFalse(game.getTile(game.getWidth()/2, game.getHeight()/2).isPlayer());
		
		//tester indirekte canPlayerMove
		game.moveUp();
		assertTrue(game.getTile(game.getWidth()/2, game.getHeight()/2-1).isPlayer());
	}
	
	@DisplayName("Tester bevegelsen til player mot høyre")
	@Test
	public void testMoveRight() {
		game.setPosPlayer(2,1);
		assertTrue(game.getTile(2, 1).isPlayer());
		
		game.moveRight();
		assertFalse(game.getTile(2, 1).isPlayer());
		assertTrue(game.getTile(3, 1).isPlayer());
		
		game.setGamePaused();
		game.moveRight();
		assertFalse(game.getTile(4, 1).isPlayer());
		
		game.setGamePaused();
		game.moveRight();
		assertTrue(game.getTile(4, 1).isPlayer());
		
		game.moveRight();
		assertTrue(game.getTile(4, 1).isPlayer());
	}
	
	@DisplayName("Tester bevegelsen til player nedover")
	@Test
	public void testMoveDown() {
		game.setPosPlayer(4,1);
		assertTrue(game.getTile(4, 1).isPlayer());
		
		game.setGamePaused();
		game.moveDown();
		assertFalse(game.getTile(4, 2).isPlayer());
		
		game.setGamePaused();
		game.moveDown();
		assertFalse(game.getTile(4, 1).isPlayer());
		assertTrue(game.getTile(4, 2).isPlayer());
		
		game.moveDown();
		assertTrue(game.getTile(4, 2).isPlayer());
	}
	
	@DisplayName("Tester bevegelsen til player mot venstre")
	@Test
	public void testMoveLeft() {
		game.setPosPlayer(4,2);
		assertTrue(game.getTile(4, 2).isPlayer());
		
		game.moveLeft();
		assertFalse(game.getTile(4, 2).isPlayer()); 
		assertTrue(game.getTile(3, 2).isPlayer());
		
		game.setGamePaused();
		game.moveLeft();
		assertFalse(game.getTile(2, 2).isPlayer());
		
		game.setGamePaused();
		game.moveLeft();
		assertTrue(game.getTile(2, 2).isPlayer());
		
		game.moveLeft();
		assertTrue(game.getTile(1, 2).isPlayer());

		game.moveLeft();
		assertTrue(game.getTile(1, 2).isPlayer());
	}
	
	@DisplayName("Tester getEnemy")
	@Test
	public void getEnemy() {
		Enemy enemy = new Enemy(width, height);
		enemy.setPosX(2);
		enemy.setPosY(2);
		
		assertThrows(IllegalArgumentException.class, () -> game.getEnemy(2, 2));
		
		game.addActiveEnemy(enemy);
		assertEquals(enemy, game.getEnemy(2, 2));
		
	}
	//Komplisert å teste innholdet i en runnable når man bruker ScheduledExecutor, så tester ikke startMovementEnemy 
	//Vår studass sa dette gikk greit da dette egentlig er utenfor pensum
}
