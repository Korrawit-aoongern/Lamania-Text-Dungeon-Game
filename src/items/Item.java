package src.items;

import src.characters.Player;
import java.util.Scanner;

public interface Item {
    String getName();
    // use the item on target; supply Scanner for interactive items
    void use(Player target, Scanner sc);
}
