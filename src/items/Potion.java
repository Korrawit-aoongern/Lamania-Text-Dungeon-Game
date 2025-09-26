package src.items;

import src.characters.Player;

public class Potion implements Item {
    private final String name;
    private final int healAmount;

    public Potion(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
    }

    @Override
    public String getName() { return name; }

    @Override
    public void use(Player target) {
        System.out.println(target.getName() + " drinks " + name + " and recovers " + healAmount + " HP!");
        // just simple add hp
    }
}
