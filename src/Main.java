
import controller.GameController;
import java.awt.Dimension;
import java.net.URL;
import javax.swing.ImageIcon;
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
        
        URL logoUrl = Main.class.getClassLoader().getResource("2048_logo.png");
        ImageIcon imgLogo = new ImageIcon(logoUrl);
        f.setIconImage(imgLogo.getImage());
        f.setTitle("Stunning 2048 - Jiang Han (6181801034)");
        
        f.pack();
        f.setResizable(false);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        new GameController(panel);
    }
}
