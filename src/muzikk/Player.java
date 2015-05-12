package muzikk;

/**
 * Created by filip on 2015-05-07.
 */
public class Player {
    private String name;
    private String actionButton;
    private int score;

    public Player(String name, String actionButton) {
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

        if(score<0){
            score=0;
        }
    }
    public void setName(String name) throws IllegalArgumentException {
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setActionButton(String key) throws IllegalArgumentException{
        actionButton = key;
    }
    public String getActionButton(){
        return actionButton;
    }
    public void setScore(int score) throws IllegalArgumentException{
        this.score = score;
    }
    public int getScore(){
        return score;
    }
}
