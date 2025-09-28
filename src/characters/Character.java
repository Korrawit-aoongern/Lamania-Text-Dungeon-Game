package src.characters;

import src.skills.AbstractSkill;
import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    protected String name;
    protected int level, hp, sp, atk, def, mag, pen, exp;
    protected int maxHp; // 🔹 add maxHp so potions can heal proportionally
    protected List<AbstractSkill> skills = new ArrayList<>();

    public Character(String name, int level, int hp, int sp, int atk, int def, int mag, int pen) {
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.maxHp = hp; // assume starting HP is full
        this.sp = sp;
        this.atk = atk;
        this.def = def;
        this.mag = mag;
        this.pen = pen;
        this.exp = 0;
    }

    // Encapsulation (getters/setters)
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getSp() { return sp; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public int getMag() { return mag; }
    public int getPen() { return pen; }
    public int getLevel() { return level; }

    // Setter for healing
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    // Polymorphic combat behavior
    public abstract void takeTurn(Character opponent);

    public void takeDamage(int dmg) {
        int reduced = Math.max(0, dmg - (def - pen));
        hp -= reduced;
        if (hp < 0) hp = 0;
        System.out.println(name + " takes " + reduced + " damage! (HP left: " + hp + ")");
    }

    public boolean isAlive() { return hp > 0; }

    public void addSkill(AbstractSkill s) { skills.add(s); }
    public List<AbstractSkill> getSkills() { return skills; }

    public int getMaxSkillSlots() {
        return 6;
    }

    public boolean hasSkill(AbstractSkill skill) {
        for (AbstractSkill s : skills) {
            if (s.getName().equals(skill.getName())) return true;
        }
        return false;
    }

}
