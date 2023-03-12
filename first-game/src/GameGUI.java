

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import GameEngine.TileType;

/**
 * The GameGUI class is responsible for rendering graphics to the screen to
 * display the game level, player and ghosts. The GameGUI class passes keyboard
 * events to a registered GameInputHandler to be handled.
 *
 */
public class GameGUI extends JFrame {

    /**
     * The three final int attributes below set the size of some graphical
     * elements, specifically the display height and width of tiles in the game
     * and the height of health/energy bars for Entity objects in the game. Tile
     * sizes should match the size of the image files used in the game.
     */
    public static final int TILE_WIDTH = 32;//32
    public static final int TILE_HEIGHT = 32;//32
    public static final int BAR_HEIGHT = 3;

    /**
     * The canvas is the area that graphics are drawn to. It is an internal
     * class of the GameGUI class.
     */
    Canvas canvas;

    /**
     * Constructor for the GameGUI class. It calls the initGUI method to
     * generate the required objects for display.
     */
    public GameGUI() {
        initGUI();
    }

    /**
     * Registers an object to be passed keyboard events captured by the GUI.
     *
     * @param i the GameInputHandler object that will process keyboard events
     * to make the game respond to input
     */
    public void registerKeyHandler(GameInputHandler i) {
        addKeyListener(i);
    }

    /**
     * Method to create and initialise components for displaying elements of the
     * game on the screen.
     */
    private void initGUI() {
        add(canvas = new Canvas());     //adds canvas to this frame
        setTitle("BoastGusters");
        setSize(1616, 935);             // default 1136, 615
        setLocationRelativeTo(null);        //sets position of frame on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Method to update the graphical elements on the screen, usually after
     * player and/or ghosts have moved when a keyboard event was handled. The
     * method requires three arguments and displays corresponding information on
     * the screen.
     *
     * @param tiles A 2-dimensional array of TileTypes. This is the tiles of the
     * current game level that should be drawn to the screen.
     * @param player An Player object. This object is used to draw the player in
     * the right tile and display its energy. null can be passed for this
     * argument, in which case no player will be drawn.
     * @param ghosts An array of Ghost objects that is processed to
     * draw ghosts with a health bar in tiles. null can be passed for this
     * argument in which case no ghosts will be drawn. Elements in the
     * ghosts array can also be null, in which case nothing will be drawn for that
     * array element.
     */
    public void updateDisplay(TileType[][] tiles, Player player, Ghost[] ghosts, Spider[] spiders) {
        canvas.update(tiles, player, ghosts, spiders);
    }

}

/**
 * Internal class used to draw elements within a JPanel. The Canvas class loads
 * images from an asset folder inside the main project folder.
 *
 * @author prtrundl
 */
class Canvas extends JPanel {

    private BufferedImage floor1;
    private BufferedImage floor2;
    private BufferedImage wall;
    private BufferedImage player;
    private BufferedImage playerfull;
    private BufferedImage ghost;
    private BufferedImage door;
    private BufferedImage bank;
    private BufferedImage breach;
    private BufferedImage breachclosed;
    private BufferedImage spider;
    private BufferedImage web;

    TileType[][] currentTiles;  //the current 2D array of tiles to display
    Player currentPlayer;       //the current player object to be drawn
    Ghost[] currentGhosts;      //the current array of ghosts to draw
    Spider[] currentSpiders;    //the current array of spiders to draw

    /**
     * Constructor that loads tile images for use in this class
     */
    public Canvas() {
        loadTileImages();
    }

    /**
     * Loads tiles images from a fixed folder location within the project
     * directory
     */
    private void loadTileImages() {
        try {
            floor1 = ImageIO.read(new File("assets/floor1.png"));
            assert floor1.getHeight() == GameGUI.TILE_HEIGHT
                    && floor1.getWidth() == GameGUI.TILE_WIDTH;
            floor2 = ImageIO.read(new File("assets/floor2.png"));
            assert floor2.getHeight() == GameGUI.TILE_HEIGHT
                    && floor2.getWidth() == GameGUI.TILE_WIDTH;
            wall = ImageIO.read(new File("assets/wall.png"));
            assert wall.getHeight() == GameGUI.TILE_HEIGHT
                    && wall.getWidth() == GameGUI.TILE_WIDTH;
            player = ImageIO.read(new File("assets/player.png"));
            assert player.getHeight() == GameGUI.TILE_HEIGHT
                    && player.getWidth() == GameGUI.TILE_WIDTH;
            playerfull = ImageIO.read(new File("assets/playerfull.png"));
            assert playerfull.getHeight() == GameGUI.TILE_HEIGHT
                    && playerfull.getWidth() == GameGUI.TILE_WIDTH;
            ghost = ImageIO.read(new File("assets/ghost.png"));
            assert ghost.getHeight() == GameGUI.TILE_HEIGHT
                    && ghost.getWidth() == GameGUI.TILE_WIDTH;
            bank = ImageIO.read(new File("assets/bank.png"));
            assert bank.getHeight() == GameGUI.TILE_HEIGHT
                    && bank.getWidth() == GameGUI.TILE_WIDTH;
            door = ImageIO.read(new File("assets/door.png"));
            assert door.getHeight() == GameGUI.TILE_HEIGHT
                    && door.getWidth() == GameGUI.TILE_WIDTH;
            breach = ImageIO.read(new File("assets/breach.png"));
            assert breach.getHeight() == GameGUI.TILE_HEIGHT
                    && breach.getWidth() == GameGUI.TILE_WIDTH;
            breachclosed = ImageIO.read(new File("assets/breachclosed.png"));
            assert breachclosed.getHeight() == GameGUI.TILE_HEIGHT
                    && breachclosed.getWidth() == GameGUI.TILE_WIDTH;
            spider = ImageIO.read(new File("assets/spider.png"));
            assert spider.getHeight() == GameGUI.TILE_HEIGHT
                    && spider.getWidth() == GameGUI.TILE_WIDTH;
            web = ImageIO.read(new File("assets/web.png"));
            assert web.getHeight() == GameGUI.TILE_HEIGHT
                    && web.getWidth() == GameGUI.TILE_WIDTH;
        } catch (IOException e) {
            System.out.println("Exception loading images: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    /**
     * Updates the current graphics on the screen to display the tiles, player
     * and ghosts
     *
     * @param t The 2D array of TileTypes representing the current level of the
     * game
     * @param player The current player object, used to draw the player and its
     * energy
     * @param ghosts The array of ghosts to display on the level with their health bar
     */
    public void update(TileType[][] t, Player player, Ghost[] ghosts, Spider[] spiders) {
        currentTiles = t;
        currentPlayer = player;
        currentGhosts = ghosts;
        currentSpiders = spiders;
        repaint();
    }

    /**
     * Override of method in super class, it draws the custom elements for this
     * game such as the tiles, player and ghosts.
     *
     * @param g Graphics drawing object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLevel(g);
    }

    /**
     * Draws graphical elements to the screen to display the current game
     * level tiles, the player and the ghosts. If the currentTiles, currentPlayer or currentGhosts
     * objects are null they will not be drawn.
     *
     * @param g
     */
    private void drawLevel(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (currentTiles != null) {
            for (int i = 0; i < currentTiles.length; i++) {
                for (int j = 0; j < currentTiles[i].length; j++) {
                    switch (currentTiles[i][j]) {
                        case FLOOR1:
                            g2.drawImage(floor1, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case FLOOR2:
                            g2.drawImage(floor2, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case WALL:
                            g2.drawImage(wall, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case BANK:
                            g2.drawImage(bank, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case DOOR:
                            g2.drawImage(door, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case BREACH:
                            g2.drawImage(breach, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case BREACHCLOSED:
                            g2.drawImage(breachclosed, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                            break;
                        case WEB:
                            g2.drawImage(web, i * GameGUI.TILE_WIDTH, j * GameGUI.TILE_HEIGHT, null);
                    }
                }
            }
        }
        if (currentGhosts != null) {
            for (Ghost gst : currentGhosts) {
                if (gst != null) {
                    g2.drawImage(ghost, gst.getX() * GameGUI.TILE_WIDTH, gst.getY() * GameGUI.TILE_HEIGHT, null);
                    drawHealthBar(g2, gst);
                }
            }
        }
        if (currentSpiders != null){
            for (Spider spd : currentSpiders){
                if (spd != null){
                    g2.drawImage(spider, spd.getX() * GameGUI.TILE_WIDTH, spd.getY() * GameGUI.TILE_HEIGHT, null);
                }
            }
        }
        
        if (currentPlayer != null) {
            g2.drawImage(currentPlayer.hasGhost() ? playerfull : player, currentPlayer.getX() * GameGUI.TILE_WIDTH, currentPlayer.getY() * GameGUI.TILE_HEIGHT, null);
            drawEnergyBar(g2, currentPlayer);
        }
        g2.dispose();
    }

    /**
     * Draws a health bar for the given Ghost at the bottom of the tile that
     * the Ghost is located in.
     *
     * @param g2 The graphics object to use for drawing
     * @param g The ghost that the health bar will be drawn for
     */
    private void drawHealthBar(Graphics2D g2, Ghost g) {
        double remainingHealth = (double) g.getHealth() / (double) g.getMaxHealth();
        g2.setColor(Color.RED);
        g2.fill(new Rectangle2D.Double(g.getX() * GameGUI.TILE_WIDTH, g.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH, GameGUI.BAR_HEIGHT));
        g2.setColor(Color.GREEN);
        g2.fill(new Rectangle2D.Double(g.getX() * GameGUI.TILE_WIDTH, g.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH * remainingHealth, GameGUI.BAR_HEIGHT));
    }

    /**
     * Draws an energy bar for the given Player at the bottom of the tile that
     * the Player is located in.
     *
     * @param g2 The graphics object to use for drawing
     * @param p The Player that the energy bar will be drawn for
     */
    private void drawEnergyBar(Graphics2D g2, Player p) {
        double remainingEnergy = (double) p.getEnergy() / (double) p.getMaxEnergy();
        g2.setColor(Color.BLUE);
        g2.fill(new Rectangle2D.Double(p.getX() * GameGUI.TILE_WIDTH, p.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH, GameGUI.BAR_HEIGHT));
        g2.setColor(Color.CYAN);
        g2.fill(new Rectangle2D.Double(p.getX() * GameGUI.TILE_WIDTH, p.getY() * GameGUI.TILE_HEIGHT + 29, GameGUI.TILE_WIDTH * remainingEnergy, GameGUI.BAR_HEIGHT));
    }
}
