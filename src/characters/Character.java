package src.characters;

import src.skills.AbstractSkill;
import src.status.Buff;
import src.status.BuffManager;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    protected String name;
    protected int level, hp, sp, atk, def, mag, pen, exp;
    protected int maxHp;
    protected int maxSp;
    protected List<AbstractSkill> skills = new ArrayList<>();

    protected BuffManager buffManager = new BuffManager();
    protected boolean stunned = false;
    protected boolean barrierActive = false;
    protected boolean immune = false;
    protected int atkModifier = 0;
    protected int defModifier = 0;
    protected int magModifier = 0;
    protected int penModifier = 0;
    protected int damageMultiplier = 0;
    protected double damageTakenModifier = 1.0;
    protected boolean isAlive = getAlive();


    public Character(String name, int level, int hp, int sp, int atk, int def, int mag, int pen) {
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.maxHp = hp; // assume starting HP is full
        this.sp = sp;
        this.maxSp = sp;
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
    public int getAtk() { return atk + atkModifier; }
    public int getDef() { return def + defModifier; }
    public int getMag() { return mag + magModifier; }
    public int getPen() { return pen + penModifier; }
    public int getDamageMultiplier() { return damageMultiplier; }
    public double getDamageTakenModifier() { return damageTakenModifier; }
    public int getLevel() { return level; }
    public boolean getAlive() { return hp > 0; }
    public BuffManager getBuffManager() { return buffManager; }

    public void setDamageTakenModifier(double modifier) {
        this.damageTakenModifier = modifier;
    }
    public void setMaxHp(int newMaxHp) {
        this.maxHp = newMaxHp;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }
    public void setDef(int newDef) {
        this.def = newDef;
    }
    public void setHp(int newHp) {
        this.hp = Math.min(newHp, maxHp);
        if (this.hp < 0) {
            this.hp = 0;
            isAlive = false;
        }
    }

    // SP management
    public boolean consumeSp(int amount) {
        if (sp >= amount) {
            sp -= amount;
            return true;
        }
        return false;
    }

    public void applyBuff(Buff buff) { buffManager.addBuff(this, buff); }
    public void tickBuffs() { buffManager.tick(this); }

    //stunned status
    public boolean isStunned() { return stunned; }
    public void setStunned(boolean s) { stunned = s; }

    // Setter for healing
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    // SP regeneration / addition helper
    public void regenSp(int amount) {
        this.sp = Math.min(maxSp, this.sp + amount);
        System.out.println(name + " regenerates " + amount + " SP. (SP: " + sp + "/" + maxSp + ")");
    }

    public int getMaxSp() { return maxSp; }
    public void setMaxSp(int ms) { this.maxSp = ms; if (sp > maxSp) sp = maxSp; }
    public void setSp(int s) { this.sp = Math.min(s, maxSp); }

    // Temporary stat modification helpers used by Buffs
    public void modifyAtk(int delta) { this.atk += delta; }
    public void modifyDef(int delta) { this.def += delta; }
    public void modifyMag(int amt) { magModifier += amt; }
    public void modifyPen(int amt) { penModifier += amt; }
    public void modifyDamageMultiplier(int amt) { damageMultiplier += amt; }

    // Barrier & Immunity
    public boolean hasBarrier() { return barrierActive; }
    public void setBarrierActive(boolean val) { barrierActive = val; }
    public void setImmune(boolean val) { immune = val; }
    public boolean isImmune() { return immune; }

    // Polymorphic combat behavior
    public abstract void takeTurn(Character opponent);

    public void takeDamage(int dmg, int attackerPen) {
        if (immune) {
            System.out.println(name + " is immune to damage!");
            return;
        }
        else {
            int finalDMG = Math.max(0, (int)Math.round((dmg - (getDef() - attackerPen)) * damageTakenModifier));
            hp -= finalDMG;
            if (hp < 0) {
                hp = 0;
                isAlive = false;
            }
            System.out.println(name + " takes " + finalDMG + " damage! (HP left: " + hp + ")");
        }
    }

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
