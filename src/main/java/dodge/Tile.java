package dodge.model;

public class Tile {
	private char type = '.';
    private int x;
    private int y;
    
    public Tile(int x, int y) {
    	if(x < 0 || y < 0) {
    		throw new IllegalArgumentException("The tile can't have negative coordinates");
    	}
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public char getType() {
    	return type;
    }
    
    public void setX(int x) {
    	if(x < 0) {
    		throw new IllegalArgumentException("The tile can't have a negative x-coordinate");
    	}
    	this.x = x;
    }
    
    public void setY(int y) {
    	if(y < 0) {
    		throw new IllegalArgumentException("The tile can't have a negative y-coordinate");
    	}
    	this.y = y;
    }
    
    public void setType(char symbol) {
    	if(".xo".indexOf(symbol) == -1) {
    		throw new IllegalArgumentException("Only '.', 'x' and 'o' are valid types");
    	}
    	type = symbol;
    }
    
    public void clearTile() {
    	type = '.';
    }
    
    public void setPlayerType() {
    	type = 'x';
    }

    public void setEnemyType() {
    	type = 'o';
    }
    
    public boolean isClear() {
    	return type == '.';
    }

    public boolean isPlayer() {
        return type == 'x';
    }

    public boolean isEnemy() {
        return type == 'o';
    }
    

    @Override
    public String toString() {
        return Character.toString(type);
    }
}
