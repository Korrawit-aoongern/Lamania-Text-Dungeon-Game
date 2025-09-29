package src.items;

import src.characters.Player;

public class HolyChalice implements Item {
    @Override
    public String getName() { return "Holy Chalice"; }

    @Override
    public void use(Player target, java.util.Scanner sc) {
        // revival is primarily handled in Combat when player dies, but allow manual use as well
        if (!target.getAlive()) {
            int hp = (int)Math.round(target.getMaxHp() * 0.8);
            target.setHp(hp);
            System.out.println("The Holy Chalice restores " + hp + " HP and revives " + target.getName() + "!");
        } else {
            System.out.println("The Holy Chalice glows but nothing happens.");
        }
    }
}
