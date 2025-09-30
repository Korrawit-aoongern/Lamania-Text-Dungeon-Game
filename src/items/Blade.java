package src.items;

import src.characters.Player;
import java.util.Scanner;

public class Blade implements Item {
    private final String name;
    private final int atkBonus;
    private final int penBonus;

    public Blade(String name, int atkBonus, int penBonus) {
        this.name = name;
        this.atkBonus = atkBonus;
        this.penBonus = penBonus;
    }

    @Override
    public String getName() {
        return name;
    }

    // ใช้ Blade กับ Player (เช่น ติดตั้งอาวุธ)
    @Override
    public void use(Player target, Scanner sc) {
        target.equipBlade(this);
    }

    public int getAtkBonus() {
        return atkBonus;
    }

    public int getPenBonus() {
        return penBonus;
    }
}
