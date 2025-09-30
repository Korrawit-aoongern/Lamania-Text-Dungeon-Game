package src.status;

import src.characters.Character;

public abstract class Buff {
    protected String name;
    protected int duration; // in turns, -1 = permanent
    protected boolean isDebuff; // true if debuff, false if buff
    protected String source = null; // origin identifier to allow stacking rules

    public Buff(String name, int duration, boolean isDebuff) {
        this.name = name;
        this.duration = duration;
        this.isDebuff = isDebuff;
    }

    public String getSource() { return source; }
    public void setSource(String s) { this.source = s; }
    public void setDuration(int d) { this.duration = d; }

    public String getName() { return name; }
    public int getDuration() { return duration; }
    public boolean isDebuff() { return isDebuff; }

    public void tick(Character target) {
        if (duration > 0) {
            duration--;
            if (duration == 0) onExpire(target);
        }
    }

    public abstract void apply(Character target);   // when gained
    public abstract void remove(Character target);  // when lost
    public void onExpire(Character target) { remove(target); }
}

