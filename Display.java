import javax.swing.*;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
public class Display extends Car {


 public static void main(String[] args) {



  JFrame frame = new JFrame("Drive Survive game");
  Image icon = Toolkit.getDefaultToolkit().getImage("resources/images/iconcar.jpg");
  frame.setIconImage(icon);
  Car game = new Car();
  frame.add(game);
  frame.pack();
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setLocationRelativeTo(null);
  frame.setVisible(true);
 }
}