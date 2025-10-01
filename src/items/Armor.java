package src.items;

import src.characters.Player;
import java.util.Scanner;

public abstract class Armor implements Item {
    private final String name;
    private final int defBonus;
    private final int hpBonus;

    public Armor(String name, int defBonus, int hpBonus) {
        this.name = name;
        this.defBonus = defBonus;
        this.hpBonus = hpBonus;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getDefBonus() {
        return defBonus;
    }

    public int getHpBonus() {
        return hpBonus;
    }

    // บังคับให้ Armor ทุกชิ้นติดตั้งเข้าช่องที่ถูกต้อง
    public abstract void equip(Player target);

    @Override
    public void use(Player target, Scanner sc) {
        equip(target);
    }
}
