package dodge.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;

import dodge.model.Enemy;

import org.junit.jupiter.api.DisplayName;

public class EnemyTest { 
	
	private Enemy enemy;
	private int width = 10;
	private int height = 12;
	
	@BeforeEach
	public void setup() {
		enemy = new Enemy(width,height);
	}
	
	@Test
	@DisplayName("Tester konstruktøren")
	public void testConstructor() {
		assertThrows(IllegalArgumentException.class,() -> new Enemy(-1, 1), "IllegalArgumentException skal kastes når man setter bredden til en negativ verdi");
		assertThrows(IllegalArgumentException.class,() -> new Enemy(1, -1), "IllegalArgumentException skal kastes når man setter høyden til en negativ verdi");
	}
	
	@Test
	@DisplayName("Tester at setPosX fungerer som den skal")
	public void testSetPosX() {
		assertThrows(IllegalArgumentException.class,() -> enemy.setPosX(-1), "x-posisjonen kan ikke være negativ");
		assertThrows(IllegalArgumentException.class,() -> enemy.setPosX(width), "x-posisjonen kan ikke være utenfor brettet");
		
		enemy.setPosX(4);
		assertEquals(4, enemy.getPosX());
		enemy.setPosX(0);
		assertEquals(0, enemy.getPosX());
		enemy.setPosX(width-1);
		assertEquals(width-1, enemy.getPosX());
	}
	
	@Test
	@DisplayName("Tester at setPosY fungerer som den skal")
	public void testSetPosY() {
		assertThrows(IllegalArgumentException.class,() -> enemy.setPosY(-1), "y-posisjonen kan ikke være negativ");
		assertThrows(IllegalArgumentException.class,() -> enemy.setPosY(height), "y-posisjonen kan ikke være utenfor brettet");
		
		enemy.setPosY(5);
		assertEquals(5, enemy.getPosY());
		enemy.setPosY(0);
		assertEquals(0, enemy.getPosY());
		enemy.setPosY(height-1);
		assertEquals(height-1, enemy.getPosY());
	}
	
	@Test
	@DisplayName("Tester at enemy får en random direction fra lista")
	public void testSetRandomDirection() {
		List<String> directions = Arrays.asList("RIGHT", "LEFT", "UP", "DOWN"); //Kun for testing for å beholde god innkapsling i Enemy
		assertNull(enemy.getDirection());
		
		enemy.setRandomDirection();
		assertTrue(directions.contains(enemy.getDirection()));
	}
	
	@Test
	@DisplayName("Tester at setSavedDirection fungerer")
	public void testSetSavedDirection() {
		enemy.setSavedDirection("LEFT");
		assertEquals("LEFT", enemy.getDirection());
		
		enemy.setSavedDirection("RIGHT");
		assertEquals("RIGHT", enemy.getDirection());
		
		enemy.setSavedDirection("UP");
		assertEquals("UP", enemy.getDirection());
		
		enemy.setSavedDirection("DOWN");
		assertEquals("DOWN", enemy.getDirection());
		
		//tester indirekte checkDirection
		assertThrows(IllegalArgumentException.class, () -> enemy.setSavedDirection("left"));
	}
	
	@Test
	@DisplayName("Tester at setStartPos fungerer")
	public void testSetStartPos() {
		enemy.getStartTile("RIGHT");
		enemy.setStartPos();
		assertEquals(0, enemy.getPosX());
		assertTrue(enemy.getPosY() >= 0 && enemy.getPosY() < height);
		
		enemy.getStartTile("LEFT");
		enemy.setStartPos();
		assertEquals(width-1, enemy.getPosX());
		assertTrue(enemy.getPosY() >= 0 && enemy.getPosY() < height);
		
		enemy.getStartTile("UP");
		enemy.setStartPos();
		assertEquals(height-1, enemy.getPosY());
		assertTrue(enemy.getPosX() >= 0 && enemy.getPosX() < width);
		
		enemy.getStartTile("DOWN");
		enemy.setStartPos();
		assertEquals(0, enemy.getPosY());
		assertTrue(enemy.getPosX() >= 0 && enemy.getPosX() < width);
	}
	
	@Test
	@DisplayName("Tester canMove")
	public void testCanMove() {
		assertFalse(enemy.canMove(-1, 0));
		assertFalse(enemy.canMove(width, 0));
		assertFalse(enemy.canMove(width-1, height));
		
		assertTrue(enemy.canMove(width-1, height-1));
		assertTrue(enemy.canMove(0, 0));
		assertTrue(enemy.canMove(width/2, height/2));
	}
	
	private boolean checkStartTile(String direction) {
		return (enemy.getStartTile(direction).getX() >= 0 
				&& enemy.getStartTile(direction).getX() < width 
				&& enemy.getStartTile(direction).getY() >= 0 
				&& enemy.getStartTile(direction).getY() < height);
	}
	
	@Test
	@DisplayName("Tester getStartTile")
	public void testGetStartTile() { 
		assertEquals(0, enemy.getStartTile("RIGHT").getX());
		assertEquals(height-1, enemy.getStartTile("UP").getY());
		assertEquals(width-1, enemy.getStartTile("LEFT").getX());
		assertEquals(0, enemy.getStartTile("DOWN").getY());
		
		assertTrue(checkStartTile("RIGHT"));
		assertTrue(checkStartTile("LEFT"));
		assertTrue(checkStartTile("UP"));
		assertTrue(checkStartTile("DOWN"));
		
		assertFalse(enemy.getStartTile("RIGHT").getX() < 0 && enemy.getStartTile("RIGHT").getX() >= width);
		assertFalse(enemy.getStartTile("LEFT").getX() < 0 && enemy.getStartTile("LEFT").getX() >= width);
		assertFalse(enemy.getStartTile("UP").getY() < 0 && enemy.getStartTile("UP").getY() >= height);
		assertFalse(enemy.getStartTile("DOWN").getY() < 0 && enemy.getStartTile("DOWN").getY() >= height);
		
		assertThrows(IllegalArgumentException.class,() -> enemy.getStartTile("left"), "Retningen kan bare være RIGHT, LEFT, UP eller DOWN");
	}
	
	@Test
	@DisplayName("Tester getNextPosEnemy")
	public void testGetNextPosEnemy() {
		assertEquals(Arrays.asList(1,0), enemy.getNextPosEnemy("RIGHT", 0, 0));
		assertEquals(Arrays.asList(width-1,0), enemy.getNextPosEnemy("RIGHT", width-2, 0));
		assertFalse(enemy.getStartTile("RIGHT").isEnemy());
		
		assertEquals(Arrays.asList(0,0), enemy.getNextPosEnemy("LEFT", 1, 0));
		assertFalse(enemy.getStartTile("RIGHT").isEnemy());
		
		assertEquals(Arrays.asList(width-2,0), enemy.getNextPosEnemy("LEFT", width-1, 0));
		assertEquals(Arrays.asList(0,0), enemy.getNextPosEnemy("UP", 0, 1));
		assertEquals(Arrays.asList(0,height-2), enemy.getNextPosEnemy("UP", 0, height-1));
		assertEquals(Arrays.asList(0,1), enemy.getNextPosEnemy("DOWN", 0, 0));
		assertEquals(Arrays.asList(0,height-1), enemy.getNextPosEnemy("DOWN", 0, height-2));
		
		assertThrows(IllegalArgumentException.class,() -> enemy.getNextPosEnemy("right", 0, 0), "Retningen kan bare være RIGHT, LEFT, UP eller DOWN.");
	}
	
}
