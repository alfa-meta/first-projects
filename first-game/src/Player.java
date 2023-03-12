
/**The Player class is a subclass of Entity and adds specific state and
 * behaviour for the player in the game including energy, the ability to change
 * this value, and whether or not the player is currently holding a captured
 * ghost.
 *

 */
public class Player extends Entity {
    
    /**
     * maxEnergy stores the maximum possible energy for this player
     */
    private final int maxEnergy;
    
    /**
     * energy stores the current energy for this player
     */
    private int energy;
    
    /**
     * carryingGhost is used to track whether the player has captured a ghost. The
     * player can carry one ghost at a time (a value of true means the player currently
     * has a ghost captured) and must deposit the ghost in the 
     * ghost bank before it can capture another one, at which time this attribute
     * should be set to false.
     */
    private boolean carryingGhost;
    
    /**
     * This constructor is used to create a Player object to use in the game
     * @param maxEnergy the maximum health of this Player, also used to set its starting
     * energy value
     * @param x the X position of this Player in the game
     * @param y the Y position of this Player in the game
     */
    public Player(int maxEnergy, int x, int y) {
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        carryingGhost = false;
        xPos = x;
        yPos = y;
    }
    
    /**
     * Changes the current energy value for this Player, setting the energy to
     * maxEnergy if the change would cause the energy attribute to exceed maxEnergy
     * @param change An integer representing the change in energy for this Player.
     * Passing a positive value will increase the energy, passing a negative value
     * will decrease the energy.
     */
    public void changeEnergy(int change) {
        energy += change;
        if (energy > maxEnergy)
            energy = maxEnergy;
    }
    
    /**
     * Returns the current health value for this Ghost
     * @return the value of the health attribute for this Ghost
     */
    public int getEnergy() {
        return energy;
    }
    
    /**
     * Returns the maxHealth value for this Ghost
     * @return the value of the maxHealth attribute for this Ghost
     */
    public int getMaxEnergy() {
        return maxEnergy;
    }
    
    /**
     * Returns a value representing whether this Player is currently carrying a
     * captured ghost.
     * @return true if the player is carrying a captured ghost, false otherwise.
     */
    public boolean hasGhost() {
        return carryingGhost;
    }
    
    /**
     * Sets the carryingGhost attribute to true, representing the fact that the
     * player has captured a ghost.
     */
    public void captureGhost() {
        carryingGhost = true;
    }
    
    /**
     * Sets the carryingGhost attribute to false, representing the fact that the
     * player has deposited a captured ghost.
     */
    public void depositGhost() {
        carryingGhost = false;
    }
}
