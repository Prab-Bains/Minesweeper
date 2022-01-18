import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 *
 * @author prabhjeet.bains
 */
public class MouseHandler implements MouseListener {
    MineSweeper game;

    public MouseHandler(MineSweeper game){
        this.game = game;

    }


    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        Button tile = (Button)me.getSource();
        //only able to click tiles if playing = true
        if(game.playing) {
            if (SwingUtilities.isRightMouseButton(me)) {// checks if right mouse clicked
                game.flag(tile);
            }

            // checks to see if left mouse click and not flagged before exposing the tile
            if (SwingUtilities.isLeftMouseButton(me) && !tile.isFlagged()) {
                // runs the move method if a tile is pressed
                game.move(tile);

                // runs the clearZero method if a 0 tile is pressed
                if (tile.getValue() == 0) {
                    game.clearZero(tile);
                }

                // runs the gameOver method if a mine is pressed
                if (tile.getValue() == -1 && !game.gameWon()) {
                    game.gameOver();
                }

                if (game.gameWon()) {
                    game.winGame();
                }
            }
        }
    }


    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

}
