package main;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Please enter the total number of players(human, AI, and PC):");

        int num = in.nextInt();

        ArrayList<Player> input = new ArrayList<>(num);

        System.out.println("Please enter the total number of real humans:");

        int human = in.nextInt();

        System.out.println("Please enter the total number of AIs:");

        int ai = in.nextInt();

        for(int h = 1; h <= human; ++h){
            System.out.println("Please enter the name of players:");
            String name = in.next();
            input.add(new Player(0, h, name));
            System.out.println("Add Human User:" + name);
        }
        for (int a = human+1; a <= human+ai; ++a) {
            input.add(new Player(1, a, "AI " + Integer.toString(a-human)));
        }
        for (int p = human+ai+1; p <= num; ++p){
            input.add(new Player(-1, p, "PC " + Integer.toString(p-human-ai)));
        }

        Game game = new Game(input, num);

        System.out.println("Game Start!");
        System.out.println("===================");
        System.out.println("===================");

        while (game.getWinner().getId() == 0) {
            //keep playing game until there is a winner
            game.nextRound();
            System.out.println("****************");
        }
    }
}
