
/**The Ghost class is a subclass of Entity and adds specific state and
 * behaviour for the ghosts in the game including health and the ability
 * to change health values.
 *
 */
public class Ghost extends Entity {
    
    /**
     * maxHealth stores the maximum possible health for this ghost
     */
    private final int maxHealth;
    
    /**
     * health stores the current health for this ghost
     */
    private int health;
    
    /**
     * This constructor is used to create a Ghost object to use in the game
     * @param maxHealth the maximum health of this Ghost, also used to set its starting
     * health value
     * @param x the X position of this Ghost in the game
     * @param y the Y position of this Ghost in the game
     */
    public Ghost(int maxHealth, int x, int y) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        xPos = x;
        yPos = y;
    }
    
    /**
     * Changes the current health value for this Ghost, setting the health to
     * maxHealth if the change would cause the health attribute to exceed maxHealth
     * @param change An integer representing the change in health for this Ghost.
     * Passing a positive value will increase the health, passing a negative value
     * will decrease the health.
     */
    public void changeHealth(int change) {
        health += change;
        if (health > maxHealth)
            health = maxHealth;
    }
    
    /**
     * Returns the current health value for this Ghost
     * @return the value of the health attribute for this Ghost
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Returns the maxHealth value for this Ghost
     * @return the value of the maxHealth attribute for this Ghost
     */
    public int getMaxHealth() {
        return maxHealth;
    }
    
    
}
