# "Dodge" - A Game Project

This game was developed as part of a final project during the course TDT4100 "Object-Oriented Programming with Java" at NTNU, during the spring of 2021. 
This repository was used to collaborate with my teammate, @elinehso, in creating the game we called "Dodge" using Java, with JavaFX for the graphic interface and JUnit5 for testing.

## About the game
"Dodge" is a game designed to test your reflexes. Your goal is to maneuver your character using the arrow buttons to avoid colliding with incoming blocks. Each successfully evaded block contributes to your score. 

## Collaborators
- @tildeeine
- @elinehso

## Project Structure
The structure of the project here on GitHub might not be exactly as it was originally, as both I and Eline were very new to Git and GitHub as a tool when we uploaded this project. We had some issues using GitHub with Eclipse IDE, as during the project we used GitLab. I currently don't have access to this GitLab user or the old project, but I have tried to recreate the folder hierarchy:
```
|-- src
|   |-- main
|   |   |-- java
|   |   |   |-- dodge
|   |   |       |-- Game.java
|   |   |       |-- Tile.java
|   |   |       |-- Enemy.java
|   |   |       |-- DodgeApp.java
|   |   |       |-- DodgeAppController.java
|   |   |   |-- fileSupport
|   |   |       |-- DodgeFileSupport.java
|   |   |       |-- DodgeFileReading.java
|   |-- test
|       |-- dodge
|            |-- GameTest.java
|            |-- TileTest.java
|            |-- EnemyTest.java
|            |-- DodgeFileSupportTest.java
|       |-- resources
|            |-- correctGameStateTest.dodge.txt
|   |-- fxml
|       |-- Dodge.fxml
|-- README.md
|-- .gitignore
|-- .classpath
|-- .project
|-- pom.xml
|-- pom_with_maven_fx.xml
```

## Explanation of Project Structure
- **'src/main/java/dodge'**: Contains the main Java classes responsible for the game's functionality.
    - **'Game.java'**: Entry point of the game Application.
    - **'Tile.java'**: Controls the logic for the game pieces, tiles. Both players and enemies are tile objects.
    - **'Enemy.java'**: Represents the blocks that the player needs to dodge.
- **'src/test/dodge'**: Houses JUnit5 tests for unit testing the game's components.
    - **'DodgeFileSupportTest'**: Test for file support classes.
    - **'EnemyTest'**: Tests enemy tile behaviour.
    - **'GameTest'**: Tests general game and player behaviour.
    - **'TileTest'**: Tests tile behaviour.
- **'src/test/resources/correctGameStateTest.dodge.txt'**: The correct game state file that is tested in file support tests. 
- **'src/main/fxml'**: Contains the files directly connected to fxml for the project.
    - **'Dodge.fxml'**: Contains the fxml setup and objects.
    - **'DodgeApp.java'**: Application which sets up the fxml file when ran.
    - **'DodgeAppController.java'**: Contains handles for the fxml objects to connect to calls.

## Topics covered / Things learned
- **Object-Oriented Programming**: Gained insight into designing and implementing a game using object-oriented principles
- **JavaFX**: Learn to build interactive graphical interfaces and create visual elements for the game
- **Unit testing with JUnit5**: Understand how to write unit tests to ensure the correctness of individual components
- **Game logic**: Explore the intricacies of game mechanics, including player movement, collision detection, simulated clock ticks, and score tracking.

