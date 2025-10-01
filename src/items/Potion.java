package src.items;

import src.characters.Player;

public class Potion implements Item {
    private String name;
    private int healPercent; // % of max HP

    public Potion(String name, int healPercent) {
        this.name = name;
        this.healPercent = healPercent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void use(Player player, java.util.Scanner sc) {
        int healAmount = (player.getMaxHp() * healPercent) / 100;
        int before = player.getHp();
        player.heal(healAmount);
        int actualHealed = player.getHp() - before;

        System.out.println("You drink " + name + " and restore " + actualHealed + " HP.");
        if (player.getHp() == player.getMaxHp()) {
            System.out.println("Your HP is fully restored.");
        }
    }

    // Factory methods
    public static Potion smallPotion() {
        return new Potion("Small HP Potion", 25);
    }

    public static Potion mediumPotion() {
        return new Potion("Medium HP Potion", 50);
    }

    public static Potion largePotion() {
        return new Potion("Large HP Potion", 75);
    }

    public static Potion supremePotion() {
        return new Potion("Supreme HP Elixir", 100);
    }
}
