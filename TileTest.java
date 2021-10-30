package dodge.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class TileTest {
	
	private Tile tile;
	
	@BeforeEach
	public void setup() {
		tile = new Tile(0,0);
	}
	
	@Test
	@DisplayName("Tester konstruktøren")
	public void testConstructor() {
		assertEquals(tile.getX(), new Tile(0,0).getX());
		assertEquals(tile.getY(), new Tile(0,0).getY());
		
		tile = new Tile(10,13);
		assertEquals(10, tile.getX());
		assertEquals(13, tile.getY());
		assertThrows(IllegalArgumentException.class,() -> new Tile(-1,0), 
				"Kan ikke ta inn en negativ x-posisjon i Tile");
		assertThrows(IllegalArgumentException.class,() -> new Tile(1,-1), 
				"Kan ikke ta inn en negativ y-posisjon i Tile");
	}
	
	@Test
	@DisplayName("Sjekker at setX() fungerer")
	public void testSetX() {
		tile.setX(7);
		assertEquals(7, tile.getX());
		assertThrows(IllegalArgumentException.class,() -> tile.setX(-1), 
				"IllegalArgumentException skal kastes når man setter x til en negativ verdi");
	}
	
	@Test
	@DisplayName("Sjekker at setY() fungerer")
	public void testSetY() {
		tile.setY(10);
		assertEquals(10, tile.getY());
		assertThrows(IllegalArgumentException.class,() -> tile.setY(-1), 
				"IllegalArgumentException skal kastes når man setter y til en negativ verdi");
	}
	
	@Test
	@DisplayName("Tester at typene er riktig")
	public void testSetType() {
		tile.setType('o');
		assertTrue(tile.isEnemy());
		tile.setType('x');
		assertTrue(tile.isPlayer());
		tile.clearTile();
		assertEquals('.', tile.getType());
		
		assertThrows(IllegalArgumentException.class,() -> tile.setType('N'), 
				"IllegalArgumentException skal kastes når man setter Tile til en ugyldig type");
	}
	
	@Test
	@DisplayName("Sjekker at setClearTile fungerer")
	public void testClearTile() {
		tile.setEnemyType();
		assertFalse(tile.isClear());
		tile.clearTile();
		assertTrue(tile.isClear());
	}
	
	@Test
	@DisplayName("Sjekker at setPlayerType fungerer")
	public void testSetPlayerType() {
		assertFalse(tile.isPlayer());
		tile.setPlayerType();
		assertTrue(tile.isPlayer());
	}
	
	@Test
	@DisplayName("Sjekker at setEnemyType fungerer")
	public void testSetEnemyType() {
		assertFalse(tile.isEnemy());
		tile.setEnemyType();
		assertTrue(tile.isEnemy());
	}
	
	
}
