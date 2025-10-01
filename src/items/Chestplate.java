package src.items;

import src.characters.Player;

public class Chestplate extends Armor {
    public Chestplate(String name, int defBonus, int hpBonus) {
        super(name, defBonus, hpBonus);
    }

    @Override
    public void equip(Player target) {
        target.equipChestplate(this);
        System.out.println("You equipped chestplate: " + getName());
    }
}
