package src.items;

import src.characters.Player;

public class Gauntlets extends Armor {
    public Gauntlets(String name, int defBonus, int hpBonus) {
        super(name, defBonus, hpBonus);
    }

    @Override
    public void equip(Player target) {
        target.equipGauntlets(this);
        System.out.println("You equipped gauntlets: " + getName());
    }
}
