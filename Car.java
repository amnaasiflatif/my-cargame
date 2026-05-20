import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Car extends JPanel implements KeyListener, ActionListener {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;

    private int carX = 100, carY = 500, carW = 50, carH = 100;
    private Image carImage, obstacleImage, frontImage, roadImage, duckImage, pickImage,buildings ,buildingsR,Welcome;    private ArrayList<Rectangle> obstacles;
    private static Clip audioClip;
    private Timer timer;
    private Timer loadingProgressTimer;
    private int score = 0;
    private int obstacleSpeed = 4;
    private boolean gameOver = false;
    private boolean gameStart = false;
    public boolean dispalyChoices =false;
    private int highScore = 0;
    private boolean carSelect = false;
    private boolean loading = true;
    private int loadingProgress = 0;
    private  int sideSpace = (width-700)/2;

    private Random rand;
    private JButton startButton;
    private JButton restartButton;
    private JButton car1Button;
    private JButton car2Button;
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private JButton helpButton;
    private JButton rulesButton;

    public Car() {
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        addKeyListener(this);
        setLayout(null);
        playAudio("resources/audios/song2.wav");
        roadImage = new ImageIcon("resources/images/runningroad.gif").getImage();
        duckImage = new ImageIcon("resources/images/duck.jpg").getImage();
        pickImage = new ImageIcon("resources/images/pickcar.jpg").getImage();
        obstacleImage = new ImageIcon("resources/images/obstacle.png").getImage();
        buildings = new ImageIcon("resources/images/buildings.gif").getImage();
        buildingsR = new ImageIcon("resources/images/buildingsR.gif").getImage();
        Welcome = new ImageIcon("resources/images/Welcome.gif").getImage();
        frontImage = new ImageIcon("resources/images/main.jpg").getImage();

        rand = new Random();
        timer = new Timer(20, this);
        timer.start();
        obstacles = new ArrayList<>();

        // Loading Progress Timer
        loadingProgressTimer = new Timer(50, e -> {
            if (loadingProgress < 100) {
                loadingProgress += 2;
            } else {
                loadingProgressTimer.stop();
            }
            repaint();
        });
        loadingProgressTimer.start();

        // Timer to finish loading screen after 8 seconds
        Timer loadingTimer = new Timer(3000, e -> {
            loading = false;
            setupButtons();// Call here so button appears after loading
            levelSelectionbuttons();
            startButton.setVisible(true); //  Show the button
            repaint();
        });
        loadingTimer.setRepeats(false);
        loadingTimer.start();
    }


    private void setupButtons() {
        // Start Button

        startButton = new JButton("Start Game");
        startButton.setBounds(550, 550, 200, 40);
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.addActionListener(e -> {
            gameStart = true;
            remove(startButton);
            showCarSelectionButtons();
            repaint();
        });
        //levelSelectionbuttons();
        //add(startButton);

        // Car Selection Buttons
        car1Button = new JButton("Car 1");
        car1Button.setBounds(470, 550, 120, 40);
        car1Button.setFont(new Font("Arial", Font.BOLD, 16));
        car1Button.addActionListener(e -> selectCar("resources/images/car2.png"));

        car2Button = new JButton("Car 2");
        car2Button.setBounds(700, 550, 120, 40);
        car2Button.setFont(new Font("Arial", Font.BOLD, 16));
        car2Button.addActionListener(e -> selectCar("resources/images/car1.png"));

        // Restart Button
        restartButton = new JButton("Restart");
        restartButton.setBounds(180, 500, 140, 40);
        restartButton.setFont(new Font("Arial", Font.BOLD, 18));
        restartButton.addActionListener(e -> {
            score = 0;
            remove(restartButton);
            replay();  // Reset the state
            setupButtons(); // Recreate buttons
            levelSelectionbuttons(); // Show level options again
            dispalyChoices = true;
            repaint();
        });

    }

    private void levelSelectionbuttons(){

        dispalyChoices = true;
        repaint();
        //Difficulty level selection butttons :
        easyButton = new JButton(" Easy ");
        easyButton.setBounds(sideSpace+700 ,250 ,220 ,50);
        easyButton.setBackground(Color.GREEN);
        easyButton.setFont(new Font("Arial", Font.BOLD, 20));
        easyButton.addActionListener(e->{
            obstacleSpeed =5;
            easyButton.setVisible(false);
            mediumButton.setVisible(false);
            hardButton.setVisible(false);
            helpButton.setVisible(false);
            rulesButton.setVisible(false);

            repaint();
            add(startButton);

        });

        mediumButton = new JButton(" Medium ");
        mediumButton.setBounds(sideSpace+700 ,350 ,220 ,50);
        mediumButton.setBackground(new Color(20, 216, 230));
        mediumButton.setFont(new Font("Arial", Font.BOLD, 20));
        mediumButton.addActionListener(e->{
            obstacleSpeed =7;
            easyButton.setVisible(false);
            mediumButton.setVisible(false);
            hardButton.setVisible(false);
            helpButton.setVisible(false);
            rulesButton.setVisible(false);

            repaint();
            add(startButton);
        });

        hardButton = new JButton(" Hard ");
        hardButton.setBounds(sideSpace+700 ,450 ,220 ,50);
        hardButton.setBackground(Color.RED);
        hardButton.setFont(new Font("Arial", Font.BOLD, 20));
        hardButton.addActionListener(e->{
            obstacleSpeed =11;
            easyButton.setVisible(false);
            mediumButton.setVisible(false);
            hardButton.setVisible(false);
            helpButton.setVisible(false);
            rulesButton.setVisible(false);

            repaint();
            add(startButton);
        });
        helpButton = new JButton(" Help ");
        helpButton.setBounds(sideSpace -170, 450, 220, 50);
        helpButton.setBackground(Color.ORANGE);
        helpButton.setFont(new Font("Arial", Font.BOLD, 20));
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHelpDialog();
            }
        });

        rulesButton = new JButton(" Rules ");
        rulesButton.setBounds(sideSpace + 700, 650, 220, 50);
        rulesButton.setBackground(Color.PINK);
        rulesButton.setFont(new Font("Arial", Font.BOLD, 20));
        rulesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRulesDialog();
            }
        });

        // After creating easyButton, mediumButton, hardButton, add:

        add(helpButton);
        add(rulesButton);

        add(easyButton);
        add(mediumButton);
        add(hardButton);
    }
    //buttons
    private void showCarSelectionButtons() {
        add(car1Button);
        add(car2Button);
        repaint();
    }
    private void hideCarSelectionButtons() {
        remove(car1Button);
        remove(car2Button);
    }
    //car selection
    private void selectCar(String imagePath) {
        carImage = new ImageIcon(imagePath).getImage();
        carSelect = true;
        hideCarSelectionButtons();
        requestFocusInWindow();
        gamestart();
        repaint();
    }
    //gamestart
    public void gamestart() {
        gameStart = true;
        playAudio("resources/audios/song1.wav");
        for (int i = 0; i < 5; i++) {
            int obsX = 20 + rand.nextInt(420);
            int obsY = -150 * (i + 1);
            obstacles.add(new Rectangle(obsX, obsY, 50, 100));
        }
    }
    private void showHelpDialog() {
        JOptionPane.showMessageDialog(this,
                "➡ Use Arrow Keys to Move Car\n➡ Avoid obstacles\n➡ Survive as long as you can!",
                "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRulesDialog() {
        JOptionPane.showMessageDialog(this,
                "📜 Game Rules:\n\n" +
                        "1. Select your level (Easy, Medium, Hard).\n" +
                        "2. Move car Left/Right using Arrow Keys.\n" +
                        "3. Dodge obstacles.\n" +
                        "4. Collision ends the game.\n" +
                        "5. Score as high as possible!",
                "Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    //gameover
    public void gameOver() {
        gameOver = true;
        setBackground(Color.WHITE);
        add(restartButton);
        repaint();
    }

    //score
    public void score() {
        if (score >= highScore) {
            highScore = score;
        }
    }
    //replay
    public void replay() {
        obstacles.clear();
        gameOver = false;
        carSelect = false;
        gameStart = false; // Important: game shouldn't be considered started yet
    }

    //audio play
    private static void playAudio(String filePath) {
        try {
            if (audioClip != null && audioClip.isRunning()) {
                audioClip.stop();
                audioClip.close();
            }
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    //painting
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(20, 216, 230)); // or any color you want for the background
        g.fillRect(0, 0, width, height);

        // Show RPM gif while loading or before game starts
        if (loading || !gameStart) {
            g.drawImage(Welcome, 0, 0, width, height, this);
            if (loading) {
                drawLoadingDialog(g);
            }
            return; // Exit early so nothing else is drawn
        }

        // If game hasn't started yet, just show the front image (already drawn)
        if (!gameStart) return;

        // If game started but car not selected
        if (!carSelect) {
            g.drawImage(pickImage, 0, 0, width, height, this);
            return;
        }

        // Game is in progress
        if (!gameOver) {
            g.drawImage(roadImage, (width-700)/2, 0, 700, height, this);
            g.drawImage(carImage,(width-700)/2+carX, carY, carW, carH, this);
            g.drawImage(buildings,0,0,sideSpace,height,this);
            g.drawImage(buildingsR,sideSpace+700,0,sideSpace,height,this);

            for (Rectangle obs : obstacles) {
                g.drawImage(obstacleImage, (width-700)/2 +obs.x, obs.y, obs.width, obs.height, this);
            }

            g.setColor(Color.YELLOW);
            g.fillRect((width-700)/2, 600, 700, 40);
            g.fillRect((width-700)/2, 0, 700, 40);

            score();
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(" Your Score : " + score, (width-700)/2 +20 +50, 25);
            g.drawString("High Score : " + highScore, (width-700)/2 +20+300, 25);

            if (score < highScore) {
                g.drawString("You are doing great!", sideSpace+150, 620);
            } else if (score == highScore) {
                g.drawString("Keep going high score is yours", sideSpace+150, 620);
            }
        }

        // Game over screen
        if (gameOver) {
            // Semi-transparent dark background
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, width, height);

            // Card settings
            int cardW = 400, cardH = 300;
            int cardX = (width - cardW) / 2;
            int cardY = (height - cardH) / 2;

            // Card background
            g.setColor(Color.WHITE);
            g.fillRoundRect(cardX, cardY, cardW, cardH, 30, 30);

            // Card border
            g.setColor(Color.BLACK);
            g.drawRoundRect(cardX, cardY, cardW, cardH, 30, 30);

            // Text
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(new Color(220, 20, 60)); // Red
            drawCentered(g, "Game Over", cardX, cardY + 60, cardW);

            g.setFont(new Font("Arial", Font.PLAIN, 28));
            g.setColor(Color.BLACK);
            drawCentered(g, "Your Score: " + score, cardX, cardY + 130, cardW);
            drawCentered(g, "High Score: " + highScore, cardX, cardY + 180, cardW);

            if (score == 0) {
                g.drawImage(duckImage, cardX + 125, cardY + 200, 150, 80, this);
            } else if (score == highScore) {
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.setColor(new Color(0, 150, 0)); // Green
                drawCentered(g, "New High Score!", cardX, cardY + 240, cardW);
            }
        }

    }
    private void drawCentered(Graphics g, String text, int x, int y, int width) {
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, x + (width - textWidth) / 2, y);
    }



    private void drawLoadingDialog(Graphics g) {
        int dialogWidth = 350;
        int dialogHeight = 75;
        int x = width / 2 - dialogWidth /2;
        int y = 530;

        g.setColor(new Color(138, 3, 3));
        g.fillRect(x, y, dialogWidth, dialogHeight);

        g.setColor(new Color(138, 3, 3));
        g.drawRect(x, y, dialogWidth, dialogHeight);

        g.setColor(Color.WHITE);
        Font font = new Font("SansSerif", Font.BOLD | Font.ITALIC, 20);
        g.setFont(font);

        // Text to display
        String text = "Loading: " + loadingProgress + "%";

        // Measure text
        FontMetrics fm = g.getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        // Center coordinates
        int textX = x + (dialogWidth - textWidth) / 2;
        int textY = y + (dialogHeight + textHeight) / 2 - 5;

        g.drawString(text, textX, textY);
    }




    public void actionEvent(ActionEvent e) {
        if (gameOver) return;
        for (Rectangle obs : obstacles) {
            obs.y += obstacleSpeed;
            if (obs.y > 600) {
                obs.y = -150;
                obs.x = 20 + rand.nextInt(600);
                score++;
            }
            if (obs.intersects(new Rectangle(carX, carY, carW, carH))) {
                gameOver();
            }
        }
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (gameStart && carSelect && !gameOver) {
            if (key == KeyEvent.VK_LEFT && carX > 25) {
                carX -= 15;
            } else if (key == KeyEvent.VK_RIGHT && carX < 700-carW) {
                carX += 15;
            } else if (key == KeyEvent.VK_UP && carY > 0) {
                carY -= 15;
            } else if (key == KeyEvent.VK_DOWN && carY < 600 - carH) {
                carY += 15;
            }
//            else if (key == KeyEvent.VK_F && obstacleSpeed < 9) {
//                obstacleSpeed += 1;
//            } else if (key == KeyEvent.VK_S && obstacleSpeed > 1) {
//                obstacleSpeed -= 1;
//            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void actionPerformed(ActionEvent e) {
        actionEvent(e);
    }


}