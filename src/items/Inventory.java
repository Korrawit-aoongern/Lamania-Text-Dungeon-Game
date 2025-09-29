package src.items;

import src.characters.Player;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<ItemStack> items = new ArrayList<>();

    public void addItem(Item item, int quantity) {
        // try to find same item
        for (ItemStack s : items) {
            if (s.getItem().getName().equals(item.getName())) {
                s.add(quantity);
                return;
            }
        }
        items.add(new ItemStack(item, Math.min(99, quantity)));
    }

    public ItemStack find(String name) {
        for (ItemStack s : items) {
            if (s.getItem().getName().equals(name)) return s;
        }
        return null;
    }

    public List<ItemStack> getItems() { return items; }

    public boolean useItem(String name, Player target, java.util.Scanner sc) {
        ItemStack s = find(name);
        if (s == null || s.getCount() <= 0) return false;
        s.getItem().use(target, sc);
        s.remove(1);
        return true;
    }
}
