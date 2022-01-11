
import controller.GameController;
import java.awt.Dimension;
import javax.swing.JFrame;
import view.GraphicalUI;

/**
 *
 * @author Jiang Han
 */
public class Main {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        GraphicalUI panel = new GraphicalUI();
        panel.setFocusable(true);
        f.getContentPane().add("Center", panel);
        f.getContentPane().setPreferredSize(new Dimension(GraphicalUI.SCREEN_WIDTH, GraphicalUI.SCREEN_HEIGHT));
        f.pack();
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        new GameController(panel);
    }
}
