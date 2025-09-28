package src.items;

import src.characters.Player;
import src.skills.AbstractSkill;

import java.util.Scanner;

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
    public void use(Player player) {
        Scanner in = new Scanner(System.in);

        if (player.hasSkill(skill)) {
            System.out.println("You already know " + skill.getName() + ".");
            in.close();
            return;
        }

        if (player.getSkills().size() < player.getMaxSkillSlots()) {
            player.addSkill(skill);
            System.out.println("You read the " + name + " and learn " + skill.getName() + "!");
        } else {
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
        in.close();
    }

    public AbstractSkill getSkill() {
        return skill;
    }
}

