package src.characters;

public class Enemy extends Character {
    public Enemy(String name, int level, int hp, int sp, int atk, int def, int mag, int pen) {
        super(name, level, hp, sp, atk, def, mag, pen);
    }

    @Override
    public void takeTurn(Character opponent) {
        System.out.println(getName() + " attacks!");
        opponent.takeDamage(atk, pen);
    }
}
