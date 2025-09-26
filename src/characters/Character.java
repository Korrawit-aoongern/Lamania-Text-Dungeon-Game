package src.characters;

import src.skills.Skill;
import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    protected String name;
    protected int level, hp, sp, atk, def, mag, pen, exp;
    protected List<Skill> skills = new ArrayList<>();

    public Character(String name, int level, int hp, int sp, int atk, int def, int mag, int pen) {
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.sp = sp;
        this.atk = atk;
        this.def = def;
        this.mag = mag;
        this.pen = pen;
        this.exp = 0;
    }

    // Encapsulation (getters)
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getSp() { return sp; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public int getMag() { return mag; }
    public int getPen() { return pen; }
    public int getLevel() { return level; }

    // Polymorphic combat behavior
    public abstract void takeTurn(Character opponent);

    public void takeDamage(int dmg) {
        int reduced = Math.max(0, dmg - (def - pen));
        hp -= reduced;
        System.out.println(name + " takes " + reduced + " damage! (HP left: " + hp + ")");
    }

    public boolean isAlive() { return hp > 0; }

    public void addSkill(Skill s) { skills.add(s); }
    public List<Skill> getSkills() { return skills; }
}