package src.game;

import src.characters.Player;
import src.characters.Enemy;

import java.util.Scanner;
import java.util.Random;

public class Combat {
    public static void fight(Player p, Enemy e, Scanner sc) {
        Random rand = new Random();
        boolean playerTurn = rand.nextBoolean();

        while (p.isAlive() && e.isAlive()) {
            if (playerTurn) {
                System.out.println("\nYour turn! (HP: " + p.getHp() + ")");
                System.out.println("1. Attack  2. Guard  3. Flee");
                String choice = sc.nextLine().trim();

                switch (choice) {
                    case "1" -> p.basicAttack(e);
                    case "2" -> p.guard();
                    case "3" -> {
                        if (rand.nextInt(100) < 50) {
                            System.out.println("You fled successfully!");
                            return;
                        } else {
                            System.out.println("Failed to flee! Lose 10% HP.");
                            p.takeDamage(p.getHp() / 10);
                        }
                    }
                    default -> System.out.println("Invalid.");
                }
            } else {
                e.takeTurn(p);
            }
            playerTurn = !playerTurn;
        }

        if (p.isAlive()) {
            System.out.println("You defeated " + e.getName() + "!");
            p.gainExp(20); // simplified exp
        } else {
            System.out.println("You were slain by " + e.getName() + "...");
        }
    }
}
