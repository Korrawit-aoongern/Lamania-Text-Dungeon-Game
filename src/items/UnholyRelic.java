package src.items;

import src.characters.Player;

public class UnholyRelic implements Item {
    @Override
    public String getName() { return "Unholy Relic"; }

    @Override
    public void use(Player target, java.util.Scanner sc) {
        // Activate a 100-step effect that increases encounters by 50%
        target.applyUnholyRelic(100);
    }
}
