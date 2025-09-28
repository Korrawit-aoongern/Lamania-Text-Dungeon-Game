package src.status;

import src.characters.Player;

public abstract class Buff {
    protected String name;
    protected int duration; // in turns, -1 = permanent
    protected boolean isDebuff; // true if debuff, false if buff

    public Buff(String name, int duration, boolean isDebuff) {
        this.name = name;
        this.duration = duration;
        this.isDebuff = isDebuff;
    }

    public String getName() { return name; }
    public int getDuration() { return duration; }
    public boolean isDebuff() { return isDebuff; }

    public void tick(Player player) {
        if (duration > 0) {
            duration--;
            if (duration == 0) onExpire(player);
        }
    }

    public abstract void apply(Player player);   // when gained
    public abstract void remove(Player player);  // when lost
    public void onExpire(Player player) { remove(player); }
}

