package main;

/**
 * Let the card be the number + color
 * color 1, 2, 3, 4 = red, yellow, green, blue
 * number 0, 1-9, 10 = skip, 11 = reverse, 12 = draw two
 * color 0 = special card value -4 = wild draw four card value -1 = wild
 * @author chris
 */
public class Card {

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor(){
        return color;
    }

    @Override
    public String toString() {
        String colors, values;
        switch(color) {
            case 1:
                colors = "Red";
                break;
            case 2:
                colors = "Yellow";
                break;
            case 3:
                colors = "Green";
                break;
            case 4:
                colors = "Blue";
                break;
            default:
                colors = "Wild";
                break;
        }
        switch (value){
            case -4:
                values = "Draw Four";
                break;
            case -1:
                values = "";
                break;
            case 10:
                values = "Skip";
                break;
            case 11:
                values = "Reverse";
                break;
            case 12:
                values = "Draw Two";
                break;
            default:
                values = Integer.toString(value);
                break;
        }

        return "{" + colors +
                " " + values +
                '}';
    }

    protected int value;
    protected int color;

    public Card(int color, int value){
        this.color = color;
        this.value = value;
    }

    public Card(Card other){
        this.color = other.getColor();
        this.value = other.getValue();
    }

}
