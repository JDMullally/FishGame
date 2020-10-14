package model;

import java.awt.Color;
import java.util.List;

public class Player implements IPlayer, Comparable<Player>{

    List<Penguin> penguinList;
    String username;
    int age;
    Color team;

    Player(int age, String username, List<Penguin> penguinList, Color team) throws IllegalArgumentException{
        for (Penguin p: penguinList) {
            if(!p.checkTeam(team) || age < 0) {
                throw new IllegalArgumentException("Penguins must be on same team as Player");
            }
        }
        this.penguinList = penguinList;
        this.team = team;
        this.age = age;
        this.username = username;
    }


    @Override
    public Tile makeMove(Penguin penguin, Tile tile) {
        return null;
    }

    @Override
    public int compareTo(Player player) {

        return this.age - player.age;
    }
}
