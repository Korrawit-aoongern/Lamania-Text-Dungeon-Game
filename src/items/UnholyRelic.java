package src.items;

import src.characters.Player;

public class UnholyRelic implements Item {
    @Override
    public String getName() { return "Unholy Relic"; }

    @Override
    public void use(Player target, java.util.Scanner sc) {
        System.out.println("The Unholy Relic hums. It's not consumable.");
    }
}
