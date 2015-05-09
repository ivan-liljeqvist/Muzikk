package muzikk;

import javafx.scene.input.KeyCode;

/**
 * Created by filip on 2015-05-07.
 */
public class Player {
    private String name;
    private KeyCode actionButton;
    private int score;

    public Player(String name, KeyCode actionButton) {
        try {
            setName(name);
            setActionButton(actionButton);
            score = 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Name input must be of type string");
        }
    }
    public void increaseScore(){
        score++;
    }
    public void decreaseScore(){
        score--;
    }
    public void setName(String name) throws IllegalArgumentException {
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setActionButton(KeyCode key) throws IllegalArgumentException{
        actionButton = key;
    }
    public KeyCode getActionButton(){
        return actionButton;
    }
    public void setScore(int score) throws IllegalArgumentException{
        this.score = score;
    }
    public int getScore(){
        return score;
    }
}
