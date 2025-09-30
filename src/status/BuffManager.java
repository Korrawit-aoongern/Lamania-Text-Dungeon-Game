package src.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import src.characters.Character;

public class BuffManager {
    private List<Buff> activeBuffs = new ArrayList<>();

    public void addBuff(Character target, Buff buff) {
        // Adjust 1-turn buffs to 2 turns unless explicitly from 'guard'
        if (buff.getDuration() == 1 && (buff.getSource() == null || !buff.getSource().equalsIgnoreCase("guard"))) {
            buff.setDuration(2);
        }

        // stacking rules: allow multiple buffs of same name only if source differs
        Iterator<Buff> it = activeBuffs.iterator();
        while (it.hasNext()) {
            Buff b = it.next();
            if (b.getName().equals(buff.getName())) {
                // Determine if they're from the same source.
                String s1 = b.getSource();
                String s2 = buff.getSource();
                boolean sameSource;
                if (s1 == null && s2 == null) sameSource = true; // both unspecified -> treat as same skill/source
                else if (s1 != null && s1.equals(s2)) sameSource = true;
                else sameSource = false;

                // if same source, refresh/replace; if different source, allow stacking
                if (sameSource) {
                    b.remove(target);
                    it.remove();
                    break;
                }
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
