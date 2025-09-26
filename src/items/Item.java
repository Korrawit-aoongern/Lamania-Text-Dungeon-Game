package src.items;

import src.characters.Player;

public interface Item {
    String getName();
    void use(Player target);
}
