package dodge.fxui;


import java.io.FileNotFoundException;
import java.io.IOException;

import dodge.model.Game;

public interface DodgeFileReading {

	Game loadGameState(String name, Game game) throws IOException;
	
	void saveGameState(String filename, Game game) throws FileNotFoundException;
	
}
