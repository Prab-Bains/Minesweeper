import java.awt.GridLayout;
import java.awt.Image;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author prabhjeet.bains
 */
public class MineSweeper extends JFrame {
    ArrayList<Integer> mineNums;
    int MINE_NUMS = 10;
    int rows = 8;
    int cols = 8;
    boolean playing = true;
    int unclearedTiles;
    int[][] grid;
    Button [][] buttons;
    static ImageIcon TILE = new ImageIcon("Tile.png");
    static ImageIcon MINE = new ImageIcon("Mine.png");
    static ImageIcon FLAG = new ImageIcon("Flag.png");



    public MineSweeper(){
        // Call JFrame constructor explicitly (from the class above)

        // Adds title to the frame
        super("Minesweeper");

        //Standard JFrame set up
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // allows you to see the frame
        this.setVisible(true);

        // initializing 2d array of buttons
        buttons = new Button[rows][cols];

        TILE = scaleIcon(TILE);
        MINE = scaleIcon(MINE);
        FLAG = scaleIcon(FLAG);

        unclearedTiles = (rows*cols)-MINE_NUMS;

    }

    private static ImageIcon scaleIcon(ImageIcon icon) {
        int buttonSize = 50;
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(buttonSize, buttonSize,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }


    // method that generates the random location of the mine by using random numbers
    public void createRandomNumber() {

        mineNums = new ArrayList(); // holds the numbers where the mines will be placed
        Random rand = new Random();
        int upperbound = rows*cols;

        // while loop to make sure no duplicate numbers are added
        while (mineNums.size() < MINE_NUMS) {
            int randomNum = rand.nextInt(upperbound);
            if (!mineNums.contains(randomNum)) {
                mineNums.add(randomNum);

            }
        }
    }

    // checks to see if a spot on the grid is on the edge
    public boolean outOfBounds(int row, int col) {
        return row >= (rows) || row < 0 || col >= (cols) || col < 0;
    }

    // checks to see if current location is a mine or not
    public boolean isMine(int row, int col) {
        return (grid[row][col] == -1);

    }

    // adds the values to the squares surrounding the mines
    public void findValue(int row, int col) {

        // Inside square values (adds the values to any mine that is not on the edge)
        if (isMine(row, col)) {
            for (int row2 = row - 1; row2 <= row + 1; row2++) {
                for (int col2 = col - 1; col2 <= col + 1; col2++) {
                    if (!outOfBounds(row2, col2)){
                        if (!isMine(row2, col2)) {
                            grid[row2][col2]++;
                        }
                    }
                }
            }
        }
    }



    public void createGrid() {
        grid = new int[rows][cols]; // creates the actual grid
        int counter = 0; // use counter variable to match random number to a location on the grid
        createRandomNumber(); // generates random location of the mines
        int mineVal = -1;

        // looks through each square in the grid and adds the mines
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mineNums.contains(counter)) {
                    grid[i][j] = mineVal;
                }
                counter++;
            }
        }

        // adds the corresponding values to surrounding squares once a mine is created
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                findValue(i, j);
            }
        }

        // creating New JPanel object
        JPanel frame = new JPanel();
        // setting the layout to be in the shape of a grid
        frame.setLayout(new GridLayout(rows, cols));

        // this for loops adds the buttons to the frame
        for(int i = 0; i<rows; i++){
            for(int j = 0; j<cols; j++){
                Button tile = new Button();// creates new button
                tile.addMouseListener(new MouseHandler(this));
                buttons[i][j] = tile;//adding the button to the 2d array
                frame.add(tile);
                tile.setValue(grid[i][j]);// setting the value of the grid
                tile.setLoc(i, j);

            }
        }

        this.add(frame);
        this.pack();// fits frame to content


        // prints out the grid
        for (int[] nums : grid) {
            for (int num : nums) {
                if (num != -1) {
                    System.out.print(" " + num + " ");
                } else {
                    System.out.print(num + " ");
                }
            }
            System.out.println();
        }
    }



    /*when a zero is pressed it will look at the surrounding boxes and clear
      them, if the surrounding boxes are equal to 0 it will rerun the method
      to clear the tiles around that 0*/
    public void clearZero(Button tile){
        int row = tile.getLocX();// finds x value of tile
        int col = tile.getLocY();// finds y value of tile

        // checks a 3x3 array around the 0 tile
        for (int row2 = row - 1; row2 <= row + 1; row2++) {
            for (int col2 = col - 1; col2 <= col + 1; col2++) {

                //if not mine/ in bounds / not already exposed
                if (!outOfBounds(row2, col2) && buttons[row2][col2].getValue() != -1 && !buttons[row2][col2].isExposed()) {
                    if(!buttons[row2][col2].isFlagged()){
                        buttons[row2][col2].expose(); //expose the tile
                        unclearedTiles--;
                    }



                    if(buttons[row2][col2].getValue() == 0){//if surrounding mine is 0
                        clearZero(buttons[row2][col2]); //rerun the method
                    }
                }
            }
        }
    }

    /*exposes the tile if a unclicked tile is pressed*/
    public void move(Button tile){
        if(!tile.isExposed() && !tile.isFlagged()){
            tile.expose();
            if(tile.getValue()!=-1){
                unclearedTiles--;
            }
        }
    }

    //places a flag icon on the tile or removes the flag if already flagged
    public void flag(Button tile){
        if(!tile.isExposed() && !tile.isFlagged()){
            tile.flag();
        }
        else{
            tile.unflag();
        }
    }




    /*looks through array of button and exposes them all*/
    public void gameOver(){
        playing = false;
        for(Button[] row: buttons){
            for(Button tile : row){
                if(tile.isFlagged() && tile.getValue()==-1){
                    tile.setIcon(FLAG);
                }else{
                    tile.expose();
                }

            }
        }
        System.out.println("GAME OVER");
    }

    // checks to see if game is won
    public boolean gameWon(){
        return unclearedTiles==0;
    }

    //flags all the mines
    public void winGame(){
        playing = false;
        for(Button[] row: buttons){
            for(Button tile : row){
                if(!tile.isExposed()){
                    tile.setIcon(FLAG);
                }
            }
        }
        System.out.println("YOU BEAT MINESWEEPER!!");
    }



    public static void main(String[] args) {
        // call the method to visually see the grid
        MineSweeper game = new MineSweeper();
        if(game.playing){
            game.createGrid();
        }



    }
}



