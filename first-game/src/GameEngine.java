
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The GameEngine class is responsible for managing information about the game,
 * creating levels, the player and ghosts, as well as updating information when
 * a key is pressed while the game is running.
 *
 */
public class GameEngine {

    /**
     * An enumeration type to represent different types of level that make up a
     * level. Each type has a corresponding image file that is used to draw the
     * right tile to the screen for each tile in a level. Floors and doors are
     * open for ghosts and the player to move into, walls should be impassable
     * for the player, the bank is where the player must deposit captured
     * ghosts, a breach is a point where new ghosts can be added to the level to
     * replace captured ghosts.
     */
    public enum TileType {
        WALL, FLOOR1, FLOOR2, BANK, BREACH, DOOR, BREACHCLOSED, WEB;
    }

    /**
     * The width of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_WIDTH = 50;         //default = 35

    /**
     * The height of the level, measured in tiles. Changing this may cause the
     * display to draw incorrectly, and as a minimum the size of the GUI would
     * need to be adjusted.
     */
    public static final int LEVEL_HEIGHT = 28;        //default = 18

    /**
     * A random number generator that can be used to include randomised choices
     * in the creation of levels, in choosing places to spawn the player and
     * ghosts, and to randomise movement and damage. Passing an integer (e.g.
     * 123) to the constructor called here will give fixed results - the same
     * numbers will be generated every time - WHICH CAN BE VERY USEFUL FOR
     * TESTING AND BUGFIXING!
     */
    private Random rng = new Random();

    /**
     * The current level number for the game. As the player completes levels the
     * level number should be increased and can be used to increase the
     * difficulty e.g. by creating additional breaches and ghosts with more
     * health etc.
     */
    private int levelNumber = 1;  //current level

    /**
     * The current turn number. Increased by one every turn. Used to control
     * effects that only occur on certain turn numbers.
     */
    private int turnNumber = 1;

    /*
    A global variable to track the number of breaches
     */
    private int numOfBreaches = 0;

    /**
     * The GUI associated with a GameEngine object. This link allows the engine
     * to pass level (level) and entity information to the GUI to be drawn.
     */
    private GameGUI gui;

    /**
     * The 2 dimensional array of level the represent the current level. The
     * size of this array should use the LEVEL_HEIGHT and LEVEL_WIDTH attributes
     * when it is created.
     */
    private TileType[][] level;

    /**
     * An ArrayList of Point objects used to create and track possible locations
     * to place the player and ghosts when a new level is created.
     */
    private ArrayList<Point> spawnLocations;

    /**
     * A Player object that is the current player. This object stores the state
     * information for the player, including energy and the current position
     * (which is a pair of co-ordinates that corresponds to a tile in the
     * current level - see the Entity class for more information on the
     * co-ordinate system used as well as the coursework specification
     * document).
     */
    private Player player;

    /**
     * An array of Ghost objects that represents the ghosts in the current level
     * of the game. Elements in this array should be an object of the type Ghost
     * (meaning that a ghost is active (not defeated) and needs to be drawn or
     * moved) or should be null (which means nothing is drawn or processed for
     * movement). null values in this array are skipped during drawing and
     * movement processing. Ghosts (Ghost objects) that the player defeats can
     * be replaced with the value null in this array which removes them from the
     * game, using syntax such as ghosts[i] = null
     */
    private Ghost[] ghosts;

    private Spider[] spider;

    ArrayList<ArrayList<Point>> possiblePaths = new ArrayList<ArrayList<Point>>();

    /**
     * Constructor that creates a GameEngine object and connects it with a
     * GameGUI object.
     *
     * @param gui The GameGUI object that this engine will pass information to
     * in order to draw levels and entities to the screen.
     */
    public GameEngine(GameGUI gui) {
        this.gui = gui;
    }

    /**
     * Generates a new level. The method builds a 2D array of TileType values
     * that will be used to draw the level to the screen and to add a variety of
     * tiles into each level. Tiles can be floors, walls, banks (to deposit
     * ghosts), doors or breaches (to add new ghosts).
     *
     * @return A 2D array of TileType values representing the tiles in the
     * current level of the game. The size of this array should use the width
     * and height of the game level using the LEVEL_WIDTH and LEVEL_HEIGHT
     * attributes.
     */
    private TileType[][] generateLevel() {
        //YOUR CODE HERE
        TileType[][] genLevel = new TileType[LEVEL_WIDTH][LEVEL_HEIGHT];
        for (int i = 0; i < genLevel.length; i++) {
            for (int j = 0; j < genLevel[i].length; j++) {
                //genLevel[i][j] = TileType.FLOOR1;
                if (levelNumber >= 3) {
                    genLevel[i][j] = TileType.FLOOR2;
                } else {
                    genLevel[i][j] = TileType.FLOOR1;
                }
                
                if (i == 0 || i == (LEVEL_WIDTH - 1)) {
                    genLevel[i][j] = TileType.WALL;
                }

                if (j == 0 || j == (LEVEL_HEIGHT - 1)) {
                    genLevel[i][j] = TileType.WALL;
                }
            }
        }
        
        ///This is used to test out spider movement.
        genLevel[2][1] = TileType.WALL;
        genLevel[2][4] = TileType.WALL;
        genLevel[2][6] = TileType.WALL;

        return genLevel;        //change this to return the 2D arrayof TileType values that you create above
    }

    /**
     * Generates spawn points for the player and ghosts. The method processes
     * the level 2D array and finds positions that are suitable for spawning,
     * i.e. empty tiles such as floors. Suitable positions should then be added
     * to the ArrayList as Point objects - Points are a simple kind of object
     * that contain an X and a Y co-ordinate stored using the int primitive
     * type.
     *
     * @return An ArrayList containing Point objects representing suitable X and
     * Y co-ordinates in the current level where the player or ghosts can be
     * added into the level.
     */
    private ArrayList<Point> getSpawns() {
        ArrayList<Point> s = new ArrayList<Point>();
        // YOUR CODE HERE
        for (int i = 0; i < LEVEL_WIDTH; i++) {
            for (int j = 0; j < LEVEL_HEIGHT; j++) {
                if (isNextTileFree(i, j, level) == true) {
                    Point open = new Point(i, j);
                    s.add(open);
                }
            }
        }
        return s;
    }

    /**
     * Adds ghosts in suitable locations in the current level. The first version
     * of this method should picked fixed positions for ghosts by calling the
     * constructor for the Ghost class and using fixed values for the X and Y
     * position of the Ghost to be added. Ghost objects created this way should
     * then be added into an array of Ghost objects that will be returned by
     * this method. Ghost objects added to this array will then be drawn to the
     * screen using the existing code in the GameGUI class.
     *
     * The second version of this method described in a later task should use
     * the spawnLocations ArrayList to pick suitable positions to add ghosts,
     * removing these positions from the spawns ArrayList as they are used
     * (using the remove() method) to avoid multiple ghosts spawning in the same
     * location and to prevent them from being added on top of banks, breaches
     * etc. The method then creates ghosts by instantiating the Ghost class,
     * setting health and the X and Y position for the ghost using the Point
     * object removed from the spawns ArrayList.
     *
     * @return An array of Ghost objects representing the ghosts for the current
     * level of the game
     */
    private Ghost[] addGhosts() {
        //YOUR CODE HERE
        int numOfGhosts = levelNumber + 2;
        if (numOfGhosts > 8) {
            numOfGhosts = 8;
        }

        Ghost[] newGhosts = new Ghost[numOfGhosts];

        for (int i = 0; i < newGhosts.length; i++) {
            Point gPoint = findRandomPoint();
            newGhosts[i] = new Ghost(3, gPoint.x, gPoint.y);
        }
        return newGhosts;        //change this to return an array of ghost objects
    }

    private Spider[] addSpider() {
        Spider[] newSpider = new Spider[1];

        Point newPos = findRandomPoint();
        ArrayList<Point> s = new ArrayList<Point>();
        s = getSpawns();

        //Hardcoded x and y values are placeholders newPos is meant to be the
        //actual spawning location for the spider.
        Spider spd = new Spider(1, s.get(12).x, s.get(10).y);
        newSpider[0] = spd;

        return newSpider;
    }

    /**
     * Creates a Player object in the game. The method instantiates the Player
     * class and assigns values for the energy and position.
     *
     * The first version of this method should use fixed a fixed position for
     * the player to start, by setting fixed X and Y values when calling the
     * constructor in the Player class.
     *
     * The second version of this method should use the spawns ArrayList to
     * select a suitable location to spawn the player and removes the Point from
     * the spawns ArrayList. This will prevent the Player from being added to
     * the game inside a wall, bank or breach for example.
     *
     * @return A Player object representing the player in the game
     */
    private Player createPlayer() {
        //YOUR CODE HERE
        Point pPoint = findRandomPoint();
        Player player = new Player(3, pPoint.x, pPoint.y); // default: X = 3, Y = 5
        return player;        //change this to return a Player object
    }

    /**
     * Handles the movement of the player when attempting to move left in the
     * game. This method is already called by the GameInputHandler class when
     * the user has pressed the left arrow key on the keyboard. The method
     * should check whether the tile to the left of the player is clear for
     * movement and, if it is, update the player object's X and Y attribute
     * values with the new position. If the tile to the left of the player is
     * not empty the method will not update the player position, but may make
     * other changes to the game, such as damaging a ghost in the tile to the
     * left, depositing a ghost, refilling energy etc.
     */
    public void movePlayerLeft() {
        int currentPos = player.xPos;
        int newXPos = currentPos - 1;
        // Increments the turn number used for level generation complexity.
        // I only want this to be used when a player does a legal move.
        turnNumber++;

        if (whichTile((newXPos), player.yPos, level) == TileType.BREACH) {
            if (player.hasGhost() == true) {
                level[newXPos][player.yPos] = TileType.BREACHCLOSED;
                player.depositGhost();
            }
        }

        if (whichTile((newXPos), player.yPos, level) == TileType.BANK) {
            player.changeEnergy(10);
            player.depositGhost();
        }
        if (isNextTileFree((newXPos), player.yPos, level) == true) {
            player.xPos -= 1;
        }
        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i] != null) {
                if ((player.xPos == ghosts[i].xPos) && (player.yPos == ghosts[i].yPos)) {
                    player.xPos = currentPos;
                    hitGhost(ghosts[i]);
                }
            }
        }

        // This makes sure the player does not move out of the level
        if (player.xPos <= 0) {
            player.xPos = currentPos;
        }
    }

    /**
     * Handles the movement of the player when attempting to move right in the
     * game. This method is already called by the GameInputHandler class when
     * the user has pressed the right arrow key on the keyboard. The method
     * should check whether the tile to the right of the player is clear for
     * movement and, if it is, update the player object's X and Y attribute
     * values with the new position. If the tile to the right of the player is
     * not empty the method will not update the player position, but may make
     * other changes to the game, such as damaging a ghost in the tile to the
     * right, depositing a ghost, refilling energy etc.
     */
    public void movePlayerRight() {
        int currentPos = player.xPos;
        int newXPos = currentPos + 1;
        // Increments the turn number used for level generation complexity.
        // I only want this to be used when a player does a legal move.
        turnNumber++;

        if (whichTile((newXPos), player.yPos, level) == TileType.BREACH) {
            if (player.hasGhost() == true) {
                level[newXPos][player.yPos] = TileType.BREACHCLOSED;
                player.depositGhost();
            }
        }

        if (whichTile((newXPos), player.yPos, level) == TileType.BANK) {
            player.changeEnergy(10);
            player.depositGhost();
        }
        if (isNextTileFree((newXPos), player.yPos, level) == true) {
            player.xPos += 1;
        }
        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i] != null) {
                if ((player.xPos == ghosts[i].xPos) && (player.yPos == ghosts[i].yPos)) {
                    player.xPos = currentPos;
                    hitGhost(ghosts[i]);
                } else {
                    // Do nothing
                }
            }
        }

        // This makes sure the player does not move out of the level
        if (player.xPos >= LEVEL_WIDTH) {
            player.xPos = LEVEL_WIDTH;
        }
    }

    /**
     * Handles the movement of the player when attempting to move up in the
     * game. This method is already called by the GameInputHandler class when
     * the user has pressed the up arrow key on the keyboard. The method should
     * check whether the tile above the player is clear for movement and, if it
     * is, update the player object's X and Y attribute values with the new
     * position. If the tile above the player is not empty the method will not
     * update the player position, but may make other changes to the game, such
     * as damaging a ghost in the tile above, depositing a ghost, refilling
     * energy etc.
     */
    public void movePlayerUp() {
        int currentPos = player.yPos;
        int newYPos = currentPos - 1;
        // Increments the turn number used for level generation complexity.
        // I only want this to be used when a player does a legal move.
        turnNumber++;

        if (whichTile((player.xPos), newYPos, level) == TileType.BREACH) {
            if (player.hasGhost() == true) {
                level[player.xPos][newYPos] = TileType.BREACHCLOSED;
                player.depositGhost();
            }
        }

        if (whichTile(player.xPos, (newYPos), level) == TileType.BANK) {
            player.changeEnergy(10);
            player.depositGhost();
        }
        if (isNextTileFree(player.xPos, (newYPos), level) == true) {
            player.yPos -= 1;
        }
        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i] != null) {
                if ((player.yPos == ghosts[i].yPos) && (player.xPos == ghosts[i].xPos)) {
                    player.yPos = currentPos;
                    hitGhost(ghosts[i]);
                } else {
                    // Do nothing
                }
            }
        }

        // This makes sure the player does not move out of the level
        if (player.yPos < 0) {
            player.yPos = currentPos;
        }
    }

    /**
     * Handles the movement of the player when attempting to move down in the
     * game. This method is already called by the GameInputHandler class when
     * the user has pressed the down arrow key on the keyboard. The method
     * should check whether the tile below the player is clear for movement and,
     * if it is, update the player object's X and Y attribute values with the
     * new position. If the tile below the player is not empty the method will
     * not update the player position, but may make other changes to the game,
     * such as damaging a ghost in the tile below, depositing a ghost, refilling
     * energy etc.
     */
    public void movePlayerDown() {
        int currentPos = player.yPos;
        int newYPos = currentPos + 1;
        // Increments the turn number used for level generation complexity.
        // I only want this to be used when a player does a legal move.
        turnNumber++;

        if (whichTile((player.xPos), newYPos, level) == TileType.BREACH) {
            if (player.hasGhost() == true) {
                level[player.xPos][newYPos] = TileType.BREACHCLOSED;
                player.depositGhost();
            }
        }

        // if (player.yPos + 1) == method(true) >> player.yPos +=1 else do nothing.
        if (whichTile(player.xPos, (newYPos), level) == TileType.BANK) {
            player.changeEnergy(10);
            player.depositGhost();
        }

        if (isNextTileFree(player.xPos, (newYPos), level) == true) {
            player.yPos += 1;
        }
        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i] != null) {
                if ((player.yPos == ghosts[i].yPos) && (player.xPos == ghosts[i].xPos)) {
                    player.yPos = currentPos;
                    hitGhost(ghosts[i]);
                }
            }
        }

        // This makes sure the player does not move out of the level
        if (player.yPos >= LEVEL_HEIGHT) {
            player.yPos = LEVEL_HEIGHT;
        }
    }

    /**
     * Reduces a ghost's health in response to the player attempting to move
     * into the same square as the ghost (attacking the ghost).
     *
     * @param g The Ghost object corresponding to the ghost in the game that the
     * player just attempted to move into the same tile as.
     */
    private void hitGhost(Ghost g) {
        if (player.hasGhost() == false) {
            if (player.getEnergy() > 0) {
                g.changeHealth(-1);
                player.changeEnergy(-1);
            }
        }
    }

    /**
     * Moves all ghosts on the current level. The method iterates over all
     * elements of the ghosts array (using a for loop) and checks if each one is
     * null (using an if statement inside that for loop). For every element of
     * the array that is NOT null, this method calls the moveGhost method and
     * passes it the current array element.
     */
    private void moveGhosts() {
        for (int j = 0; j < ghosts.length; j++) {
            if (ghosts[j] != null) {
                moveGhost(ghosts[j]);
            }
        }
    }

    /**
     * Moves a specific ghost in the game. The method updates the X and Y
     * attributes of the Ghost object passed to the method to set its new
     * position.
     *
     * @param g The Ghost that needs to be moved
     */
    private void moveGhost(Ghost g) {
        int rand = rng.nextInt(4);
        boolean panic = isGhostPanicking(player.xPos, player.yPos, g.xPos, g.yPos);
        int ghostPos;
        if ((panic == true)) {
            rand = chooseBestDirection(player.xPos, player.yPos, g.xPos, g.yPos);
        }
        switch (rand) {
            case 0:
                ghostPos = g.xPos;
                g.xPos += 1;
                if ((player.xPos == g.xPos) && (player.yPos == g.yPos)) {
                    g.xPos = ghostPos;
                }
                if (g.xPos > (LEVEL_WIDTH - 1)) {
                    g.xPos = ghostPos;
                }
                break;
            case 1:
                ghostPos = g.xPos;
                g.xPos -= 1;
                if ((player.xPos == g.xPos) && (player.yPos == g.yPos)) {
                    g.xPos = ghostPos;
                }
                if (g.xPos < 0) {
                    g.xPos = ghostPos;
                }
                break;
            case 2:
                ghostPos = g.yPos;
                g.yPos += 1;
                if ((player.xPos == g.xPos) && (player.yPos == g.yPos)) {
                    g.yPos = ghostPos;
                }
                if (g.yPos > (LEVEL_HEIGHT - 1)) {
                    g.yPos = ghostPos;
                }
                break;
            case 3:
                ghostPos = g.yPos;
                g.yPos -= 1;
                if ((player.xPos == g.xPos) && (player.yPos == g.yPos)) {
                    g.yPos = ghostPos;
                }
                if (g.yPos < 0) {
                    g.yPos = ghostPos;
                }
                break;
            case 4:
                break;
        }
    }

    private void moveSpiders() {
        for (int j = 0; j < spider.length; j++) {
            if (spider[j] != null) {
                moveSpider(spider[j], player);
            }
        }
    }

    //This method when called will move the spider towards the player.
    //Over 10 hours were spent on the spider algorithm.
    private void moveSpider(Spider s, Player player) {
        /*
        ArrayList<Point> pointList = new ArrayList<>();

        int spiderX = s.xPos;
        int spiderY = s.yPos;
        int playerX = player.xPos;
        int playerY = player.yPos;

        pointList.add(new Point(spiderX, spiderY));
        possiblePaths = new ArrayList<ArrayList<Point>>();

        int counter = 0;
        pathFinding(spiderX, spiderY, pointList, counter);

        ArrayList<Point> currentShortestPath = possiblePaths.get(0);
        int oldValue = possiblePaths.get(0).size();

        for (int i = 0; i < possiblePaths.size(); i++) {

            if (possiblePaths.get(i).size() < currentShortestPath.size()) {
                currentShortestPath = possiblePaths.get(i);
            }
        }

        s.xPos = currentShortestPath.get(1).x;
        s.yPos = currentShortestPath.get(1).y;

        */
        /*
        Directions 0 = right, 1 = left, 2 = down, 3 = up.
         */
 /*
        Point newPointRight = numOfTilesFree(spiderX, spiderY, 0, level);
        Point newPointLeft = numOfTilesFree(spiderX, spiderY, 1, level);
        Point newPointDown = numOfTilesFree(spiderX, spiderY, 2, level);        
        Point newPointUp = numOfTilesFree(spiderX, spiderY, 3, level);
        
        pointList.add(newPointRight);
        pointList.add(newPointLeft);
        pointList.add(newPointDown);
        pointList.add(newPointUp);
       
        
        Point newPoint = findClosestLocation(pointList);
        
        int sXDifference = newPoint.x - player.getX();
        int sYDifference = newPoint.y - player.getY();
        
        if (sXDifference > 0){
            s.xPos--;
        } else {
            s.xPos++;
        } if (sYDifference > 0){
            s.yPos++;
        } else {
            s.yPos--;
        }
        
        System.out.println(sXDifference + " " + sYDifference);
        
         */
        //System.out.println("Distance to the next wall down: " + numOfTilesFree(spiderX, spiderY, 2, level));
        //System.out.println("Distance to the next wall up: " + numOfTilesFree(spiderX, spiderY, 3, level));
    }

    /**
     * Processes the ghosts array to find any Ghost in the array with 0 or less
     * health. Any Ghost in the array with 0 or less health should be set to
     * null; when drawing or moving ghosts the null elements in the ghosts array
     * are skipped.
     */
    private void cleanDefeatedGhosts() {
        for (int j = 0; j < ghosts.length; j++) {
            if (ghosts[j] != null) {
                if (ghosts[j].getHealth() == 0) {
                    ghosts[j] = null;
                    player.captureGhost();
                    // This spawns a ghost if a ghost is killed and a breach exists
                    if (doBreachesExist() == true) {
                        for (int k = 0; k < LEVEL_WIDTH; k++) {
                            for (int l = 0; l < LEVEL_HEIGHT; l++) {
                                if (level[k][l] == TileType.BREACH) {
                                    ghosts[j] = new Ghost(3, k, l);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void cleanDefeatedSpiders() {
        for (int j = 0; j < spider.length; j++) {
            if (spider[j] != null) {
                if (spider[j].getHealth() == 0) {
                    spider[j] = null;
                }
            }
        }
    }

    /**
     * This method is called when the number of ghosts in the level is zero and
     * the player is also not currently carrying a captured ghost, meaning that
     * the player has captured all ghosts and deposited them all, "completing"
     * the level. This method is similar to the startGame method and uses SOME
     * identical code.
     *
     * This method should increase the current level number, create a new level
     * by calling the generateLevel method and setting the level attribute using
     * the returned 2D array, add new Ghosts, and finally place the player in
     * the level.
     *
     * A second version of this method in a later task should also find suitable
     * positions to add ghosts and the player using the spawnLocations ArrayList
     * and code in the getSpawns method.
     */
    private void nextLevel() {
        levelNumber++;
        startGame();
        placePlayer();
    }

    /**
     * The first version of this method should place the player in the game
     * level by setting new, fixed X and Y values for the player object in this
     * class.
     *
     * The second version of this method in a later task should place the player
     * in a game level by choosing a position from the spawnLocations ArrayList,
     * removing the spawn position as it is used. The method sets the players
     * position in the level by calling its setPosition method with the X and Y
     * values of the Point taken from the spawnLocations ArrayList.
     */
    private void placePlayer() {
        Point placePoint = findRandomPoint();

        if (isSurroundingAreaFree(placePoint.x, placePoint.y, level) == false) {
            placePlayer();
        } else {
            player.xPos = placePoint.x;
            player.yPos = placePoint.y;
        }
    }

    private boolean isGhostPanicking(int pPosX, int pPosY, int gPosX, int gPosY) {
        boolean panic = false;
        int absX = Math.abs(pPosX - gPosX);
        int absY = Math.abs(pPosY - gPosY);

        if ((absX + absY) <= 4) {
            panic = true;
        }

        return panic;
    }

    private int chooseBestDirection(int pPosX, int pPosY, int gPosX, int gPosY) {
        // dir = 0 is move to the right
        // dir = 1 is move to the left
        // dir = 2 is move down
        // dir = 3 is move up
        // dir = 4 is do nothing
        int dir = 4;
        int xVal = gPosX - pPosX;
        int yVal = gPosY - pPosY;

        if ((xVal < 0) && (gPosX != 0) && (isNextTileFree((gPosX - 1), gPosY, level) == true)) {
            dir = 1;
        } else if ((xVal > 0) && (gPosX != (LEVEL_WIDTH - 1)) && (isNextTileFree((gPosX + 1), gPosY, level) == true)) {
            dir = 0;
        } else if ((yVal < 0) && (gPosY != 0) && (isNextTileFree(gPosX, (gPosY - 1), level) == true)) {
            dir = 3;
        } else if ((yVal > 0) && (gPosY != (LEVEL_HEIGHT - 1)) && (isNextTileFree(gPosX, (gPosY + 1), level) == true)) {
            dir = 2;
        }

        return dir;
    }

    private boolean isNextTileFree(int posX, int posY, TileType[][] levelArray) {
        // Making sure that the next tile is in the array.
        if (posX > LEVEL_WIDTH) {
            posX = LEVEL_WIDTH;
        } else if (posX < 0) {
            posX = 0;
        }
        if (posY > LEVEL_HEIGHT) {
            posY = LEVEL_HEIGHT;
        } else if (posY < 0) {
            posY = 0;
        }

        TileType nextTile = levelArray[posX][posY];
        if (nextTile == TileType.FLOOR1 || nextTile == TileType.FLOOR2 || nextTile == TileType.DOOR) {
            return true;
        } else {
            return false;
        }
    }

    /*
    This method returns the distance the spider is able to move relative to the
    direction it is checking.
    0 = right, 1 = left, 2 = down, 3 = up.
    case 1 and 3 have negative values.
    
    For ArrayList just ignore the last point because the last point is a wall.
    0th index point is current location
     */
    private Point numOfTilesFree(int posX, int posY, int direction, TileType[][] levelArray) {

        ArrayList<Point> pointList0 = new ArrayList<>();

        int distance = 0;
        switch (direction) {
            case 0:
                while (isNextTileFree((posX + distance), posY, levelArray) == true) {
                    distance++;
                    // This point captures the current position and puts it into
                    // an arrayList.
                    Point dX = new Point((posX + distance), posY);
                    pointList0.add(dX);
                    //System.out.println(distance);
                }
                break;
            case 1:
                while (isNextTileFree((posX + distance), posY, levelArray) == true) {
                    distance--;
                    // All x value positions are negative
                    Point dX = new Point((posX + distance), posY);
                    pointList0.add(dX);
                    //System.out.println(distance);
                }
                break;
            case 2:
                while (isNextTileFree(posX, (posY + distance), levelArray) == true) {
                    distance++;

                    Point dY = new Point(posX, (posY + distance));
                    pointList0.add(dY);
                    //System.out.println(distance);
                }
                break;
            case 3:
                while (isNextTileFree(posX, (posY + distance), levelArray) == true) {
                    distance--;
                    // All y value positions are negative
                    Point dY = new Point(posX, (posY + distance));
                    pointList0.add(dY);
                    //System.out.println(distance);
                }
                break;
        }

        Point correctPoint = findClosestLocation(pointList0);

        return correctPoint;
    }

    private int numOf2ndFreeTiles(int posX, int posY, int direction, TileType[][] levelArray) {
        ArrayList<Point> pointList0 = new ArrayList<>();
        ArrayList<Point> pointList1 = new ArrayList<>();
        ArrayList<Point> pointList2 = new ArrayList<>();
        ArrayList<Point> pointList3 = new ArrayList<>();

        int distance = 0;
        switch (direction) {
            case 0:
                while (isNextTileFree((posX + distance), posY, levelArray) == true) {
                    distance++;
                    // This point captures the current position and puts it into
                    // an arrayList.
                    Point xD = new Point((posX + distance), posY);
                    pointList0.add(xD);
                    //System.out.println(distance);
                }
                break;
            case 1:
                while (isNextTileFree((posX + distance), posY, levelArray) == true) {
                    distance--;
                    // All x value positions are negative
                    Point xD = new Point((posX + distance), posY);
                    pointList1.add(xD);
                    //System.out.println(distance);
                }
                break;
            case 2:
                while (isNextTileFree(posX, (posY + distance), levelArray) == true) {
                    distance++;

                    Point xY = new Point(posX, (posY + distance));
                    pointList2.add(xY);
                    //System.out.println(distance);
                }
                break;
            case 3:
                while (isNextTileFree(posX, (posY + distance), levelArray) == true) {
                    distance--;
                    // All y value positions are negative
                    Point xY = new Point(posX, (posY + distance));
                    pointList3.add(xY);
                    //System.out.println(distance);
                }
                break;
        }
        return distance;
    }

    private void pathFinding(int sX, int sY, ArrayList<Point> path, int counter) {
        counter++;
        System.out.println("-------------------------------------------------------");
        System.out.println("Current x: " + sX + " " + "Current y: " + sY);
        // we have array of points creating the path so far
        // we have the current tile coordinates
        // for each tile around it (as long as it's not the previous one) we check 
        // - if it's player, then add path to global (?) paths and break
        // - if it's wall, then break
        // - if it's in the spawns array (available), then add it to the path and call itself for it
        for (int i = sX - 1; i <= sX + 1; i++) {
            boolean playerFound = false;
            for (int j = sY - 1; j <= sY + 1; j++) { // for each tile around the current one
                System.out.println("i: " + " " + i + "j: " + j);
                
                if (path.size() == 1) { // check if the point is not the previous one in the path
                    System.out.println("current tile x: " + i + " " +  "current tile y: " + j);
                    System.out.println("player x: " + player.xPos + " " +  "player y: " + player.yPos);
                    if (i == player.xPos && j == player.yPos) { // woo hoo thet's the player, we can add path to possible ones
                        // add path to global array
                        possiblePaths.add(path);
                        System.out.println("That's the player1");
                        playerFound = true;
                        break;
                    } else if (!isNextTileFree(i, j, level)) {
                        System.out.println("Not available tile1");
//                        playerFound = true;
//                        break; // path rejected
                    } else {
                        System.out.println("Continue recursive1");
                        path.add(new Point(i, j));
                        pathFinding(i, j, path, counter);
                    }
                } else {
//                     || !(sX == path.get(path.size() - 2).x && sY == path.get(path.size() - 2).y)
                    System.out.println("current tile x: " + i + " " + "current tile y: " + j);
                    System.out.println("player x: " + player.xPos + " " +  "player y: " + player.yPos);
                    boolean isPresent = false;
                    for (int k = 0; k < path.size(); k++) {
                        if (path.get(k).x == i && path.get(k).y == j) {
                            isPresent = true;
                            break;
                        }
                    }
                    if (isPresent) {
                        System.out.println("Tile previously checked so ignore it");
                        break;
                    } else {
                        System.out.println("Posiible tile to consider");
                        if (i == player.xPos && j == player.yPos) { // woo hoo thet's the player, we can add path to possible ones
                            // add path to global array
                            possiblePaths.add(path);
                            System.out.println("That's the player");
                            playerFound = true;
                            break;
                        } else if (!isNextTileFree(i, j, level)) {
                            System.out.println("Not available tile");
//                            playerFound = true;
//                            break; // path rejected
                        } else {
                            System.out.println("Continue recursive");
                            path.add(new Point(i, j));
                            pathFinding(i, j, path, counter);
                        }
                    }
                }
            }
//            if (playerFound) {
//                break;
//            }
        }
    }

    private Point findClosestLocation(ArrayList<Point> array) {
        Point bestPos = array.get(0);
        int bestPosX = bestPos.x;
        int bestPosY = bestPos.y;
        int pXPos = player.xPos;
        int pYPos = player.yPos;

        for (int i = 0; i < array.size(); i++) {
            Point point = array.get(i);
            int pointX = point.x;
            int pointY = point.y;

            int originalComparison = Math.abs(pXPos - bestPosX) + Math.abs(pYPos - bestPosY);
            int newComparison = Math.abs(pXPos - pointX) + Math.abs(pXPos - pointY);

            if (originalComparison > newComparison) {
                bestPosX = pointX;
                bestPosY = pointY;
                bestPos = new Point(bestPosX, bestPosY);
            }

        }

        return bestPos;

    }

    private TileType whichTile(int posX, int posY, TileType[][] levelArray) {
        return levelArray[posX][posY];
    }

    private Point findRandomPoint() {
        int spawn = rng.nextInt(spawnLocations.size());

        return spawnLocations.get(spawn);
    }

    private TileType spawnBank(TileType[][] array) {
        Point pos = findRandomPoint();

        TileType bank = TileType.BANK;
        array[pos.x][pos.y] = bank;
        return bank;
    }

    private void spawnMultipleBanks(int num) {
        for (int i = 0; i < num; i++) {
            spawnBank(level);
        }
    }

    private void spawnTerrain1(int n) {

        for (int i = 0; i < n; i++) {
            Point newPos = findRandomPoint();

            int newPosX = newPos.x - 1;
            int newPosY = newPos.y + 1;
            if (var1Availability(newPosX, newPosY) == true) {
                //Each additional wall spawned here will completely change the way
                //the game looks. Any combination of 1 wall 2 walls or 3 walls is
                //possible.
                
                level[newPos.x][newPos.y] = TileType.WALL;
                //level[newPos.x + 1][newPos.y] = TileType.WALL;
                //level[newPos.x][newPos.y + 1] = TileType.WALL;
            }
        }
    }

    private boolean isSurroundingAreaFree(int xPos, int yPos, TileType[][] array) {
        boolean answer = false;
        int areaCounter = 0;

        if (isNextTileFree(xPos + 1, yPos, array) == true) {
            areaCounter++;
        }
        if (isNextTileFree(xPos - 1, yPos, array) == true) {
            areaCounter++;
        }
        if (isNextTileFree(xPos, yPos - 1, array) == true) {
            areaCounter++;
        }
        if (isNextTileFree(xPos, yPos + 1, array) == true) {
            areaCounter++;
        }

        if (areaCounter == 4) {
            answer = true;
        }

        return answer;
    }

    // If I was just 15% more mathematically gifted or if I decided to do this to perfection
    // I could just find a way to use the golden ratio to do this but for now hard coding will suffice.
    // UPDATE!
    // If I had 2 braincells to rub together I could have realised that nested arrays solve my problem. (THEY DID)
    private boolean var1Availability(int oX, int oY) {
        ArrayList<Point> var1 = new ArrayList<>();

        int xRange = 4; // Exclusive value. This is meant to represent the number of tiles in the x direction
        int yRange = 3; // Exclusive value. This is meant to represent the number of tiles in the y direction
        boolean enoughSpace = false; // Return boolean value.

        // For loop that checks every tile in the array list
        // i representing the x axis and j representing the y axis.
        for (int i = 0; i < xRange; i++) {
            for (int j = 0; j < yRange; j++) {
                Point open = new Point(i, j);
                var1.add(open);
                /*
                Checking for x direction to not exceed width of the game
                for some reason y direction does not have such a bug
                 */
                if (!((open.x + oX) >= LEVEL_WIDTH)) {
                    if (isNextTileFree(open.x + oX, open.y + oY, level) == false) {
                        break;
                    }
                }
            }
        }
        if (var1.size() == 12) {
            enoughSpace = true;
        }
        return enoughSpace;
    }

    private void spawnBreaches() {
        int numberOfBreaches = 1;

        if (turnNumber > 2) {
            numberOfBreaches = 2;
        }

        for (int i = 0; i < numberOfBreaches; i++) {
            Point breachPoint = findRandomPoint();

            level[breachPoint.x][breachPoint.y] = TileType.BREACH;
        }
    }

    private boolean doBreachesExist() {
        for (int i = 0; i < LEVEL_WIDTH; i++) {
            for (int j = 0; j < LEVEL_HEIGHT; j++) {
                if (level[i][j] == TileType.BREACH) {
                    return true;
                }
            }
        }
        return false;
    }

    private void spawnDoors() {
        for (int i = 0; i < LEVEL_WIDTH; i++) {
            for (int j = 0; j < LEVEL_HEIGHT; j++) {
                int newI = i;
                int newJ = j;
                if (isNextTileFree(i, j, level) == true) {
                    if (isNextTileFree(i + 1, j, level) == false && (whichTile(i + 1, j, level) == TileType.WALL)) {
                        if (isNextTileFree(i - 1, j, level) == false && (whichTile(i - 1, j, level) == TileType.WALL)) {
                            level[newI][newJ] = TileType.DOOR;
                        }
                    }

                }

            }
        }
    }

    /**
     * Performs a single turn of the game when the user presses a key on the
     * keyboard. The method cleans defeated ghosts and moves any undefeated
     * ghosts. Finally it requests the GUI to redraw the game level by passing
     * it the level, player and ghosts objects for the current level.
     *
     * A second version of this method in a later task will also check if all
     * ghosts in the current level have been captured and deposited in a bank,
     * and if they have, will call the nextLevel() method to generate a new,
     * harder level.
     */
    public void doTurn() {
        cleanDefeatedGhosts();
        moveGhosts();

        cleanDefeatedSpiders();
        moveSpiders();
        gui.updateDisplay(level, player, ghosts, spider);
        int ghostCounter = 0;

        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i] != null) {
                ghostCounter++;
            }
        }

        if ((ghostCounter == 0) && (player.hasGhost() == false)) {
            nextLevel();
        }
    }

    /**
     * Starts a game. This method generates a level, finds spawn positions in
     * the level, adds ghosts and the player and then requests the GUI to update
     * the level on screen using the information on level, player and ghosts.
     */
    public void startGame() {
        level = generateLevel();
        spawnLocations = getSpawns();
        ghosts = addGhosts();
        spider = addSpider();
        player = createPlayer();
        gui.updateDisplay(level, player, ghosts, spider);
        /*
        Comments for spawnTerrain method/algorithm
        25 is a good lower bound if you want more unique wall types.
        100 is good if you want a small complexity to the game.
        1000 is great for a maze like game however I suspect that there is a miniscule chance of bugging your character in a wall
        that could be fixed if I decide to check the adjacent tiles for the player spawn.
        In fact if the player spawns in a wall you can walk out of it since my code only checks for next tiles
        rather than the current tile the player stands on. For now this will suffice.
        10000 will create more predictable patterns
        It seems to be that around 3000 the game starts creating predictable patterns.
        At around 10 million the game takes a few seconds to be generated.
         */
        spawnTerrain1(turnNumber); // 25 is a good number if no other terrain would be spawned.
        spawnMultipleBanks(5);
        spawnBreaches();
        spawnDoors();
    }
}
