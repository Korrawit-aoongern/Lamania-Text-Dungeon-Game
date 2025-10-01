package src.items;

import src.characters.Player;
import java.util.Scanner;

public class Staff implements Item {
    private final String name;
    private final int magicAtkBonus; // โบนัสพลังเวทย์
    

    public Staff(String name, int magicAtkBonus) {
        this.name = name;
        this.magicAtkBonus = magicAtkBonus;
        
    }

    @Override
    public String getName() {
        return name;
    }

    // ใช้ Staff กับ Player (ติดตั้งอาวุธประเภทไม้เท้า)
    @Override
    public void use(Player target, Scanner sc) {
        target.equipStaff(this);
    }

    public int getMagicAtkBonus() {
        return magicAtkBonus;
    }

   
}
