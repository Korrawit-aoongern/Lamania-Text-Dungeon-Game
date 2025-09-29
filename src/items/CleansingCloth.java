package src.items;

import src.characters.Player;

public class CleansingCloth implements Item {
    @Override
    public String getName() { return "Cleansing Cloth"; }

    @Override
    public void use(Player target, java.util.Scanner sc) {
        System.out.println("The Cleansing Cloth shimmers. It's not consumable.");
    }
}
