package model.state;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * A Player represents a player in the Fish Game. A player is made up of a color, age, and List of
 * IPenguins they can place on the board.  A player should be able to display it's age,
 * color and Penguins.  It should also be able to add Penguins to it's current list of Penguins.
 */
public class Player implements IPlayer {

    private final Color color; // The Player's color [unique]
    private final int age; // the age of the player
    private List<IPenguin> penguins; // the Player's List of Penguins
    private int score; // the score the penguin currently has

    /**
     * Constructor takes in a Player's color, age, and List of IPenguins
     *
     * @param color Player's color
     * @param age Player's age
     * @param penguins Player's List of IPenguins
     * @throws IllegalArgumentException
     */
    public Player(Color color, int age, List<IPenguin> penguins) throws IllegalArgumentException {
        this(color, age, penguins, 0);
    }

    /**
     * Constructor takes in a Player's color, age, and List of IPenguins
     *
     * @param color Player's color
     * @param age Player's age
     * @param penguins Player's List of IPenguins
     * @param score Player's current score
     * @throws IllegalArgumentException
     */
    public Player(Color color, int age, List<IPenguin> penguins, int score) throws IllegalArgumentException {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        } else if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        } else if (penguins == null ) {
            throw new IllegalArgumentException("Penguins cannot be null");
        }

        this.color = color;
        this.age = age;
        this.penguins = this.validPenguins(penguins, color);
        this.score = score;
    }

    /**
     * Returns a valid list of penguins or throws an exception if the List of IPenguins is invalid.
     * A List of IPenguins is invalid if the Player's color isn't the same as all of the Penguin's
     * colors.
     *
     * @param penguins List of IPenguin
     * @param color Color
     * @return List of IPenguin
     */
    private List<IPenguin> validPenguins(List<IPenguin> penguins, Color color) {
        for (IPenguin p : penguins) {
            if(p.getColor().getRGB() != color.getRGB()) {
                throw new IllegalArgumentException("Penguins must be on same team as Player");
            }
        }

        return new ArrayList<>(penguins);
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public void addScore(int points) {
        this.score += points;
    }

    @Override
    public List<IPenguin> getPenguins() {
        return new ArrayList<>(this.penguins);
    }

    @Override
    public void addPenguin(IPenguin penguin) {
        this.penguins.add(penguin);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Player) {
            Player other = (Player) o;
            return this.age == other.age && this.color.getRGB() == other.getColor().getRGB();
        }
        return false;
    }

    @Override
    public IPlayer clone() {
        return new Player(new Color(this.color.getRGB()), this.age, new ArrayList<>(this.penguins), this.score);
    }
}
