package src.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import src.characters.Player;

public class BuffManager {
    private List<Buff> activeBuffs = new ArrayList<>();

    public void addBuff(Player player, Buff buff) {
        // remove if same buff exists (refresh)
        for (Buff b : activeBuffs) {
            if (b.getName().equals(buff.getName())) {
                b.remove(player);
                activeBuffs.remove(b);
                break;
            }
        }
        activeBuffs.add(buff);
        buff.apply(player);
    }

    public void tick(Player player) {
        Iterator<Buff> it = activeBuffs.iterator();
        while (it.hasNext()) {
            Buff buff = it.next();
            buff.tick(player);
            if (buff.getDuration() == 0) {
                it.remove();
            }
        }
    }

    public void clearAll(Player player) {
        for (Buff b : activeBuffs) b.remove(player);
        activeBuffs.clear();
    }

    public List<Buff> getActiveBuffs() { return activeBuffs; }
}
