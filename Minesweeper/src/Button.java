import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;

/**
 *
 * @author prabhjeet.bains
 */
public class Button extends JButton {
    private int value;
    private boolean exposed;
    private boolean flagged;
    int[] location = new int[2];

    public Button(){
        exposed = false;
        // sets the prefered size of each button
        this.setPreferredSize(new Dimension(50,50));
        this.setBackground(Color.LIGHT_GRAY);//sets button colour
        this.setIcon(MineSweeper.TILE);//sets default icon

    }

    public void expose(){
        exposed = true;
        this.setFont(new Font("Roman", Font.BOLD,18));
        if(getValue()!=-1){
            this.setIcon(null);//removes image if exposed
            if(getValue()!=0){
                this.setText(Integer.toString(getValue()));
            }

        }
        //when mine is pressed shows mine icon instead of -1 and changes button colour to red
        if(getValue()==-1){
            this.setIcon(MineSweeper.MINE);
            this.setBackground(Color.RED);
        }
    }

    public boolean isExposed(){
        return exposed;
    }

    public int getValue(){
        return value;
    }
    public void setValue(int newValue){
        value = newValue;
    }

    public int getLocX(){
        return location[0];//returns x value
    }

    public int getLocY(){
        return location[1];//returns y value
    }

    public void setLoc(int row, int col){
        location[0] = row;
        location[1] = col;
    }
    public void flag(){
        flagged = true;
        this.setIcon(MineSweeper.FLAG);
    }

    public boolean isFlagged(){
        return flagged;
    }
    public void unflag(){
        this.setIcon(MineSweeper.TILE);
        flagged = false;
    }

}

