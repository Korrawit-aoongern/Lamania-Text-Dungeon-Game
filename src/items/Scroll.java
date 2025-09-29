package src.items;

import src.characters.Player;
import src.skills.AbstractSkill;


public class Scroll implements Item {
    private String name;
    private AbstractSkill skill;

    public Scroll(String name, AbstractSkill skill) {
        this.name = name;
        this.skill = skill;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void use(Player player, java.util.Scanner in) {
        if (player.hasSkill(skill)) {
            System.out.println("You already know " + skill.getName() + ".");
            return;
        }

        if (player.getSkills().size() < player.getMaxSkillSlots()) {
            player.addSkill(skill);
            System.out.println("You read the " + name + " and learn " + skill.getName() + "!");
            return;
        }

        // Skill slots full: offer replacement interactively using provided Scanner
        System.out.println("Your skill slots are full. Replace a skill? (y/n)");
        String ans = in.nextLine().trim().toLowerCase();

        if (ans.equals("y")) {
            System.out.println("Choose a skill to replace:");
            for (int i = 0; i < player.getSkills().size(); i++) {
                System.out.println((i + 1) + ". " + player.getSkills().get(i).getName());
            }
            System.out.println((player.getSkills().size() + 1) + ". Cancel");

            int choice = -1;
            try {
                choice = Integer.parseInt(in.nextLine().trim()) - 1;
            } catch (NumberFormatException ignored) {}

            if (choice >= 0 && choice < player.getSkills().size()) {
                AbstractSkill removed = player.getSkills().get(choice);
                player.getSkills().set(choice, skill);
                System.out.println("You forgot " + removed.getName() + " and learned " + skill.getName() + "!");
            } else {
                System.out.println("You decide not to use the scroll.");
            }
        } else {
            System.out.println("You decide not to use the scroll.");
        }
    }

    public AbstractSkill getSkill() {
        return skill;
    }
}

