package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Board {

    public Board() {
        currentPlayer = 0;
        declaredColor = 0;
        players = new ArrayList<Player>();
    }

    public Player getPlayers() {
        return players.get(currentPlayer);
    }

    public Card getDiscard() {
        return discard.get(discard.size() - 1);
    }

    public void setDeclaredColor(int declaredColor) {
        this.declaredColor = declaredColor;
    }

    public int getDeclaredColor() {
        return this.declaredColor;
    }

    //the index of the current player
    private int currentPlayer;

    //variable to remember the direction of game board
    private int declaredColor;

    private boolean clockwise = true;

    private final ArrayList<Player> players;

    private ArrayList<Card> pile;

    private ArrayList<Card> discard;

    //true means current player played wild card and should choose color
    public boolean drawWildCard(Player player) {
        boolean played;
        boolean wild = false;
        //if we have less than 4 cards in the pile, create a new pile
        if (pile.size() < 4) {
            Card top = new Card(getDiscard());
            discard.remove(getDiscard());
            while (pile.size() > 0) {
                discard.add(pile.get(0));
                pile.remove(0);
            }
            pile = new ArrayList<>(discard);
            Collections.shuffle(pile);
            discard = new ArrayList<>();
            discard.add(top);
        }
        //check if we need to draw more than one card, when we draw the card that match the top of discard pile, we shall play one of them
        if (getDiscard().getValue() == 12) {
            boolean[] table = isPlayedTwo(player, false, false);
            played = table[0];
            wild = table[1];
            if (!played) setCurrentPlayer(1);
        } else if (getDiscard().getValue() == -4) {
            boolean[] table = isPlayedTwo(player, false, false);
            played = table[0];
            wild = table[1];
            table = isPlayedTwo(player, played, wild);
            played = table[0];
            wild = table[1];
            if (!played) setCurrentPlayer(1);
        } else {
            Card only = pile.get(0);
            pile.remove(0);
            if (only.getValue() == getDiscard().getValue() || only.getColor() == getDiscard().getColor()) {
                wild = isWild(false, only);
            } else {
                player.add(only);
                setCurrentPlayer(1);
            }
        }
        return wild;
    }

    public String blindFishing(Player player) {
        /*
          achieve the blind fishing function
          current user switch a random card with a random player
         */
        String mixCards;
        Random rand = new Random();
        int user = rand.nextInt(players.size());
        Player opponent = players.get(user);
        while (opponent == player) {
            user = rand.nextInt(players.size());
            opponent = players.get(user);
        }
        int loc1 = rand.nextInt(player.getHand().size());
        int loc2 = rand.nextInt(opponent.getHand().size());
        opponent.add(player.getHand().get(loc1));
        player.add(opponent.getHand().get(loc2));
        mixCards = player.getHand().get(loc1).toString() + opponent.getHand().get(loc2).toString();
        player.getHand().remove(loc1);
        opponent.getHand().remove(loc2);
        return mixCards;
    }

    void otherPlayers(Player player) {
        for (Player other : players) {
            if (other.getId() != player.getId()) {
                System.out.println(other.getName() + ":" + other.getHand().size() + "; ");
            }
        }
//        System.out.println();
    }

    private boolean isWild(boolean wild, Card only) {
        discard.add(only);
        if (only.getValue() == 11) {
            setClockwise();
        }
        if (only.getValue() == 10) {
            setCurrentPlayer(2);
        } else {
            setCurrentPlayer(1);
        }
        if (only.getValue() < 0) wild = true;
        return wild;
    }

    private boolean[] isPlayedTwo(Player player, boolean played, boolean wild) {
        Card one = pile.get(0);
        boolean[] table = new boolean[2];
        pile.remove(0);
        if (!played && (one.getValue() == getDiscard().getValue() || one.getColor() == getDiscard().getColor())) {
            wild = isWild(wild, one);
            played = true;
        } else {
            player.add(one);
        }

        Card two = pile.get(0);
        pile.remove(0);
        if (!played && (two.getValue() == getDiscard().getValue() || one.getColor() == getDiscard().getColor())) {
            wild = isWild(wild, two);
            played = true;
        } else {
            player.add(two);
        }
        table[0] = played;
        table[1] = wild;
        return table;
    }

    public void initial(ArrayList<Card> pile, ArrayList<Player> players) {
        //initial a full size card
        for (int i = 1; i <= 4; ++i) {
            for (int j = 0; j < 13; ++j) {
                pile.add(new Card(i, j));
                if (j != 0) {
                    pile.add(new Card(i, j));
                }
            }
        }
        int wild = 4;
        while (wild != 0) {
            pile.add(new Card(0, -4));
            pile.add(new Card(0, -1));
            --wild;
        }
        Collections.shuffle(pile);

        //for each player, give them the initial cards
        for (Player player : players) {
            for (int j = 0; j < 7; ++j) {
                player.add(pile.get(0));
                pile.remove(0);
            }
        }

        //The first initial card
        Card init = pile.get(0);
        while(init.getColor() == 0 || init.getValue() > 9){
            pile.add(init);
            init = pile.get(0);
            pile.remove(0);
        }
        discard.add(init);
        pile.remove(0);

        clockwise = Boolean.TRUE;

        currentPlayer = 0;
    }

    public void setCurrentPlayer(int i) {
        if (clockwise) {
            this.currentPlayer = (currentPlayer + i) % (players.size());
        } else {
            this.currentPlayer = (currentPlayer - i + players.size()) % (players.size());
        }

    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setClockwise() {
        this.clockwise = !this.clockwise;
    }

    public boolean isClockwise() {
        return clockwise;
    }

    public void handCard(Card card) {
        discard.add(card);
    }

    public Board(ArrayList<Player> players) {
        this.players = players;
        pile = new ArrayList<>();
        discard = new ArrayList<>();
        initial(pile, players);
    }

    public void blindFishing(Player currPlayer, Card test) {
        Random rand = new Random();
        int user = rand.nextInt(players.size());
        Player opponent = players.get(user);
        while (opponent == currPlayer) {
            user = rand.nextInt(players.size());
            opponent = players.get(user);
        }
        int loc = rand.nextInt(opponent.getHand().size());
        opponent.add(test);
        currPlayer.add(opponent.getHand().get(loc));
        currPlayer.getHand().remove(test);
        opponent.getHand().remove(loc);
    }

    ArrayList<Card> drawCards(Player currPlayer) {
        ArrayList<Card> toDraw = new ArrayList<>();
        if (pile.size() < 4) {
            Card top = new Card(getDiscard());
            discard.remove(getDiscard());
            while (pile.size() > 0) {
                discard.add(pile.get(0));
                pile.remove(0);
            }
            pile = new ArrayList<>(discard);
            Collections.shuffle(pile);
            discard = new ArrayList<>();
            discard.add(top);
        }
        int count = getDiscard().getValue();
        //check if we need to draw more than one card, when we draw the card that match the top of discard pile, we shall play one of them
        while(count != 0){
            Card curr = pile.get(0);
            if(curr.getColor() == 0 || curr.getColor() == getDiscard().getColor() || curr.getValue() == curr.getValue()){
                toDraw.add(curr);
            }else{
                currPlayer.getHand().add(curr);
            }
            pile.remove(0);
            count--;
        }
        return toDraw;
    }
}
