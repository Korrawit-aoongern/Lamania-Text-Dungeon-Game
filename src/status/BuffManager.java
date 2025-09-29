package src.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import src.characters.Character;

public class BuffManager {
    private List<Buff> activeBuffs = new ArrayList<>();

    public void addBuff(Character target, Buff buff) {
        // remove if same buff exists (refresh)
        for (Buff b : activeBuffs) {
            if (b.getName().equals(buff.getName())) {
                b.remove(target);
                activeBuffs.remove(b);
                break;
            }
        }
        activeBuffs.add(buff);
        buff.apply(target);
    }

    public void tick(Character target) {
        Iterator<Buff> it = activeBuffs.iterator();
        while (it.hasNext()) {
            Buff buff = it.next();
            buff.tick(target);
            if (buff.getDuration() == 0) {
                it.remove();
            }
        }
    }

    public void clearAll(Character target) {
        for (Buff b : activeBuffs) b.remove(target);
        activeBuffs.clear();
    }

    public List<Buff> getActiveBuffs() { return activeBuffs; }
}
