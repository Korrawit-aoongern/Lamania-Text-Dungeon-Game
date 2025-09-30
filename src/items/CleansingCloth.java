package src.items;

import src.characters.Player;

public class CleansingCloth implements Item {
    @Override
    public String getName() { return "Cleansing Cloth"; }

    @Override
    public void use(Player target, java.util.Scanner sc) {
        // Activate a 100-step effect that reduces encounters by 50%
        target.applyCleansingCloth(100);
    }
}
