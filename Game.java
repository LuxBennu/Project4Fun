package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private final int numberOfPlayers;
    private Board gameBoard;
    private Player winner = new Player();
    private boolean start;

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Player getWinner(){
        return this.winner;
    }

    public Game(ArrayList<Player> players, int users){
        gameBoard = new Board(players);
        numberOfPlayers = users;
        start = false;
    }

    public boolean getStart() {
        return start;
    }

    public void nextRound(){
        if(!start) start = true;
        Player currPlayer = gameBoard.getPlayers();
        Card top = gameBoard.getDiscard();
        Scanner in = new Scanner(System.in);
        Random rand = new Random();
        //pc don't do blind fish and always choose the first available card
        if(currPlayer.getType() == -1){
            System.out.println("Current Player " + currPlayer.getName()+" with Hand size" + currPlayer.getHand().size() + ":");
            for(int card = 0; card < currPlayer.getHand().size(); ++card){
                System.out.print(currPlayer.getHand().get(card)+"  ");
            }
            System.out.println();
            autoRun(currPlayer, rand);
        }
        else if(currPlayer.getType() == 1){
            //this for AI
            //print current card in hand for test
            System.out.println("Current Player " + currPlayer.getName()+" with Hand size" + currPlayer.getHand().size() + ":");
            for(int card = 0; card < currPlayer.getHand().size(); ++card){
                System.out.print(currPlayer.getHand().get(card)+"  ");
            }
            System.out.println();
            //when there are more than half of unuseful card, invoke the blind fishing function for AI
            if(currPlayer.preMatch(top, gameBoard.getDeclaredColor())){
                for(int card = 0; card < currPlayer.getHand().size(); ++card){
                    Card test = currPlayer.getHand().get(card);
                    if(test.getColor() > 0 && test.getColor() != top.getColor() && test.value != top.getValue()){
                        gameBoard.blindFishing(currPlayer, test);
                        break;
                    }
                }
//                System.out.println("After Blind Fishing, " + currPlayer.getName()+" with Hand:");
//                //show how many cards that other players got
//                for(int card = 0; card < currPlayer.getHand().size(); ++card){
//                    System.out.print(currPlayer.getHand().get(card)+"  ");
//                }
//                System.out.println();
            }
            autoRun(currPlayer, rand);
        }
        else if(currPlayer.getType() == 0){
            //human player
            System.out.println("Discard pile: " + gameBoard.getDiscard());
            //print current card in hand
            System.out.println("Current Player " + currPlayer.getName()+" with Hand:");
            for(int card = 0; card < currPlayer.getHand().size(); ++card){
                System.out.print(currPlayer.getHand().get(card)+"  ");
            }
            System.out.println();
            String predict = ":Better do not";
            if(currPlayer.preMatch(top, gameBoard.getDeclaredColor())){
                predict = ":Better do";
            }
            //choose if we need to invoke the blind fishing function, also return the system judgement
            System.out.println("Blind Fish? Yes/No" + predict);
            String blind = in.next();

            if(blind.equals("Yes") || blind.equals("YES")){
                ArrayList<Card> toFish;
                toFish = currPlayer.getFish(top);
                System.out.println("Please choose the index of card to blind fish(start with 1): ");
                for(int card = 0; card < toFish.size(); ++card){
                    System.out.print(toFish.get(card)+"  ");
                }
                int fish = in.nextInt();
                gameBoard.blindFishing(currPlayer, toFish.get(fish-1));
                System.out.println("After Blind Fishing, " + currPlayer.getName()+" with Hand:");
                for(int card = 0; card < currPlayer.getHand().size(); ++card){
                    System.out.print(currPlayer.getHand().get(card)+"  ");
                }
                System.out.println();
            }

            //show how many cards that other players got
            gameBoard.otherPlayers(currPlayer);

            ArrayList<Card> toPlay= currPlayer.findMatch(top);
            if(toPlay.size() > 0){
                System.out.println("Please choose the index of card to play(start with 1): ");
                for(int card = 0; card < toPlay.size(); ++card){
                    System.out.print(toPlay.get(card)+"  ");
                }
                int play = in.nextInt();
                Card discard = toPlay.get(play-1);
                gameBoard.handCard(discard);

                if(discard.getColor() == 0){
                    System.out.println("Please select a color(1, 2, 3, 4 = red, yellow, green, blue)");
                    int color = in.nextInt();
                    boardUpdateColor(color);
                }
                //check if the user used skip or reverse
                if(discard.getValue() == 10){
                    gameBoard.setCurrentPlayer(2);
                }else if(discard.getValue() == 11){
                    gameBoard.setClockwise();
                    gameBoard.setCurrentPlayer(1);
                }else{
                    gameBoard.setCurrentPlayer(1);
                }
                currPlayer.getHand().remove(discard);
                if(currPlayer.getHand().size() == 0){
                    winner = currPlayer;
                }
            }else{
                //user dose not play any card and have to draw some
                ArrayList<Card> canPlay = gameBoard.drawCards(currPlayer);
                if(canPlay.size() > 0){
                    System.out.println("Please choose one card to play(start with 1): ");
                    for(int card = 0; card < canPlay.size(); ++card){
                        System.out.print(canPlay.get(card)+"  ");
                    }
                    int play = in.nextInt();
                    Card discard = canPlay.get(play-1);
                    gameBoard.handCard(discard);
                    if(discard.getValue() == 10){
                        gameBoard.setCurrentPlayer(2);
                    }else if(discard.getValue() == 11){
                        gameBoard.setClockwise();
                        gameBoard.setCurrentPlayer(1);
                    }else{
                        gameBoard.setCurrentPlayer(1);
                    }

                    if(discard.getColor() == 0){
                        System.out.println("Please select a color(1, 2, 3, 4 = red, yellow, green, blue)");
                        int color = in.nextInt();
                        boardUpdateColor(color);
                    }
                    canPlay.remove(discard);
                }
                //add all of them into player's hand
                while(canPlay.size()>0){
                    currPlayer.getHand().add(canPlay.get(0));
                    canPlay.remove(0);
                }
            }
        }

    }

    private void autoRun(Player currPlayer, Random rand) {
        if(currPlayer.match(gameBoard.getDiscard(), gameBoard)){
            if(currPlayer.getHand().size() == 0){
                winner = currPlayer;
            }
        }else if(currPlayer.specialMatch(gameBoard.getDiscard(), gameBoard)){
            System.out.println("Please select a color(1, 2, 3, 4 = red, yellow, green, blue)");
            int user = rand.nextInt(4);
            boardUpdateColor(user);
        }else{
            if(gameBoard.drawWildCard(currPlayer)){
                System.out.println("Please select a color(1, 2, 3, 4 = red, yellow, green, blue)");
                int user = rand.nextInt(4);
                boardUpdateColor(user);
            }
        }
    }

    public void boardUpdateColor(int color){
        gameBoard.setDeclaredColor(color);
    }

    public int boardUpdatedColor(){
        return gameBoard.getDeclaredColor();
    }
}
