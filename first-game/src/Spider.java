/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 */
public class Spider extends Entity{
    /**
     * maxHealth stores the maximum possible health for this spider
     */
    private final int maxHealth;
    
    /**
     * health stores the current health for this spider
     */
    private int health;
    
    /**
     * This constructor is used to create a spider object to use in the game
     * @param maxHealth the maximum health of this Spider, also used to set its starting
     * health value
     * @param x the X position of this Spider in the game
     * @param y the Y position of this Spider in the game
     */
    public Spider(int maxHealth, int x, int y) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        xPos = x;
        yPos = y;
    }
    
    /**
     * Changes the current health value for this spider, setting the health to
     * maxHealth if the change would cause the health attribute to exceed maxHealth
     * @param change An integer representing the change in health for this spider.
     * Passing a positive value will increase the health, passing a negative value
     * will decrease the health.
     */
    public void changeHealth(int change) {
        health += change;
        if (health > maxHealth)
            health = maxHealth;
    }
    
    /**
     * Returns the current health value for this spider
     * @return the value of the health attribute for this spider
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Returns the maxHealth value for this spider
     * @return the value of the maxHealth attribute for this spider
     */
    public int getMaxHealth() {
        return maxHealth;
    }
}
