package main;

import java.util.ArrayList;

public class Player extends Board {
    public int getId() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    private int type;

    private final int pid;
    private final String name;
    private ArrayList<Card> hand;

    public int getType() {
        return type;
    }

    public Player(){
        this.pid = 0;
        this.name = "";
    }

    public Player(int type, int pid, String name){
        super();
        this.type = type;
        this.pid = pid;
        this.name = name;
        hand = new ArrayList<>();
    }

    public void add(Card card) {
        this.hand.add(card);
    }

    public boolean match(Card card, Board gamepad){
        if(card.getValue() > 9 ||card.getColor() == 0) return false;
        for(int i = 0; i < this.getHand().size(); ++i){
            Card curr = this.getHand().get(i);
            if(curr.getColor() == card.getColor() || curr.getValue() == card.getValue()){
                gamepad.handCard(card);
                if(curr.getValue() == 11){
                    gamepad.setClockwise();
                }
                if(curr.getValue() == 10){
                    gamepad.setCurrentPlayer(2);
                }else{
                    gamepad.setCurrentPlayer(1);
                }
                int color = curr.getColor();
                int value = curr.getValue();
                System.out.println("findmatch " + curr.toString());
                this.getHand().remove(curr);
                if(value > 0){
                    this.matchSame(color, value, gamepad,i);
                }
                return true;
            }
        }
        return false;
    }

    public boolean matchSame(int color, int value, Board gamepad, int loc){
        for(int index= loc; index< this.getHand().size(); ++index){
            Card curr = this.getHand().get(index);
            if(curr.getColor() == color && curr.getValue() == value){
                gamepad.handCard(curr);
                System.out.println("findmatchsame " + curr.toString());
                this.getHand().remove(curr);
                return true;
            }
        }
        return false;
    }

    public boolean specialMatch(Card card, Board gamepad){
        for(int index= 0; index< this.getHand().size(); ++index){
            Card curr = this.getHand().get(index);
            if(curr.getColor() == 0){
                gamepad.handCard(card);
                if(curr.getValue() == 11){
                    gamepad.setClockwise();
                }
                if(curr.getValue() == 10){
                    gamepad.setCurrentPlayer(2);
                }else{
                    gamepad.setCurrentPlayer(1);
                }
                System.out.println("find special match " + curr.getColor()+":"+ curr.getValue());
                this.getHand().remove(index);
                return true;
            }
        }
        return false;
    }

    boolean preMatch(Card card, int declaredColor) {
        int count = 0;
        int size = hand.size();
        int color = card.getColor();
        if(color == 0) color = declaredColor;
        for(int h = 0; h < size; ++h){
            if(hand.get(h).getColor() != color && hand.get(h).getValue() != card.getValue()){
                ++count;
            }
        }
        if(count > size/2){
            return true;
        }
        else {
            return false;
        }
    }

    ArrayList<Card> getFish(Card card) {
        ArrayList<Card> objects = new ArrayList<>();
        int size = hand.size();
        for(int h = 0; h < size; ++h){
            if(hand.get(h).getColor() > 0){
                if(hand.get(h).getColor() != card.getColor() && hand.get(h).getValue() != card.getValue()){
                    objects.add(hand.get(h));
                }
            }
        }
        return objects;
    }

    ArrayList<Card> findMatch(Card top) {
        ArrayList<Card> objects = new ArrayList<>();
        int size = hand.size();
        for(int h = 0; h < size; ++h){
            if(hand.get(h).getColor() != 0){
                if(hand.get(h).getColor() == top.getColor() || hand.get(h).getValue() == top.getValue()){
                    objects.add(hand.get(h));
                }
            }else{
                objects.add(hand.get(h));
            }
        }
        return objects;
    }

}
