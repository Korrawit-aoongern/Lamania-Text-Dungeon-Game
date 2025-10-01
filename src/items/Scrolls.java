package src.items;

import src.skills.*;

public class Scrolls {
    public static Scroll singleSlash() {
        return new Scroll("Single Slash Scroll", new SingleSlash());
    }

    public static Scroll doubleSlash() {
        return new Scroll("Double Slash Scroll", new DoubleSlash());
    }

    public static Scroll tripleSlash() {
        return new Scroll("Triple Slash Scroll", new TripleSlash());
    }

    public static Scroll spinningSlash() {
        return new Scroll("Spinning Slash Scroll", new SpinningSlash());
    }

    public static Scroll downwardSlash() {
        return new Scroll("Downward Slash Scroll", new DownwardSlash());
    }

    public static Scroll lungeForward() {
        return new Scroll("Lunge Forward Scroll", new LungeForward());
    }

    public static Scroll tornadoBlade() {
        return new Scroll("Tornado Blade Scroll", new TornadoBlade());
    }

    public static Scroll flameStrike() {
        return new Scroll("Flame Strike Scroll", new FlameStrike());
    }

    public static Scroll waterSoothing() {
        return new Scroll("Water Soothing Scroll", new WaterSoothing());
    }

    public static Scroll holyBlessing() {
        return new Scroll("Holy Blessing Scroll", new HolyBlessing());
    }

    public static Scroll thunderStrike() {
        return new Scroll("Thunder Strike Scroll", new ThunderStrike());
    }

    public static Scroll plagueSplit() {
        return new Scroll("Plague Split Scroll", new PlagueSplit());
    }

    public static Scroll poisonInfuse() {
        return new Scroll("Poison Infuse Scroll", new PoisonInfuse());
    }

    public static Scroll tripleShadowStep() {
        return new Scroll("Triple Shadow Step Scroll", new TripleShadowStep());
    }

    public static Scroll heavensFall() {
        return new Scroll("Heaven's Fall Scroll", new HeavensFall());
    }

    public static Scroll smiteStomp() {
        return new Scroll("Smite Stomp Scroll", new SmiteStomp());
    }

    public static Scroll holyBarrier() {
        return new Scroll("Holy Barrier Scroll", new HolyBarrier());
    }

    public static Scroll astralBlade() {
        return new Scroll("Astral Blade Scroll", new AstralBlade());
    }

    public static Scroll astralFury() {
        return new Scroll("Astral Fury Scroll", new AstralFury());
    }

    public static Scroll heal() {
        return new Scroll("Heal Scroll", new src.skills.Heal());
    }
}

