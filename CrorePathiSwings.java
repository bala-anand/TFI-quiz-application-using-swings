
// - Changed the Console output to swing GUI
// - For graphical interface (JFrame, JLabel, JButton, JOptionPane )
//  JFrame - displays all other GUI components of the quiz application
//  JLabel - Displays static or dynamic text, such as questions, rewards, and instructions
//  JButton - Provides clickable buttons for users to select answers
//  JOptionPane - Shows dialog boxes to collect user input and display messages 
//  JPanal - options are arranged in JPanel with GridLayout for neat vertical stacking.
package crorepathi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class CrorePathiSwings extends JFrame {
    // Data
    private final String[] questions = {
        "Who is known as the \"Megastar\" of Telugu cinema?",
        "Which Telugu film became the first to gross over ₹1000 crore worldwide?",
        "Who directed the blockbuster movie \"Magadheera\"?",
        "Which actress is popularly known as the \"Lady Superstar\" of Tollywood?",
        "Which film won the National Film Award for Best Feature Film in Telugu in 2021?",
        "Who composed the music for the film \"Ala Vaikunthapurramuloo\"?",
        "Which actor played the lead role in \"Arjun Reddy\"?",
        "What is the real name of the actor known as \"Superstar Krishna\"?",
        "Which Telugu film was India's official entry to the Oscars in 2017?",
        "Which of the following is a famous film studio located in Hyderabad?"
    };
    private final String[][] options = {
    		{"Chiranjeevi", "Nagarjuna", "Balakrishna", "Venkatesh"},
    		{"Baahubali: The Beginning", "Baahubali: The Conclusion", "RRR", "Pushpa: The Rise"},
    		{"S. S. Rajamouli", "Trivikram Srinivas", "Sukumar", "Puri Jagannadh"},
    		{"Anushka Shetty", "Samantha Ruth Prabhu", "Kajal Aggarwal", "Tamannaah Bhatia"},
    		{"Jersey", "Mahanati", "Colour Photo", "Ala Vaikunthapurramuloo"},
    		{"Devi Sri Prasad", "Thaman S", "Ilaiyaraaja", "Mani Sharma"},
    		{"Nani", "Vijay Deverakonda", "Ram Charan", "Allu Arjun"},
    		{"Ghattamaneni Siva Rama Krishna Murthy", "Akkineni Nageswara Rao", "Nandamuri Taraka Rama Rao", "Konidela Siva Sankara Vara Prasad"},
    		{"Baahubali: The Beginning", "Gautamiputra Satakarni", "Viraata Parvam", "Baahubali: The Conclusion"},
    		{"Ramoji Film City", "Filmistan Studios", "AVM Studios", "Prasad Studios"}
    };
    private final int[] correctAnswers = {1, 2, 1, 1, 3, 2, 2, 1, 1, 1};
    private final int[] rewards = {1000, 2000, 5000, 10000, 20000, 40000, 80000, 160000, 320000, 1000000};

    // State
    private String name, city, dob;
    private int currentQuestion = 0;  //for maintaining the current question index
    private int earnedReward = 0;     //for maintaining the current earned money
    private boolean audienceUsed = false, fiftyFiftyUsed = false;

    // UI Components
    private JLabel rewardLabel, questionLabel;  //questions are shown in the JLabel
    private JButton[] optionButtons = new JButton[4];  //JButton displays options
    private JButton audienceBtn, fiftyFiftyBtn; //JButton displays audience poll and fifty fifty options
    private JPanel optionsPanel, lifelinePanel; //options are arranged in JPanel with GridLayout for neat vertical stacking.

    public CrorePathiSwings() {
        setTitle("Crorepathi Quiz Game");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Gather user info
        name = JOptionPane.showInputDialog(this, "Enter your name:");  // Collecting user info using JOptionPane
        city = JOptionPane.showInputDialog(this, "Enter your city:");
        dob = JOptionPane.showInputDialog(this, "Enter your Date of Birth (DD-MM-YYYY):");

        // Reward Tree in a JLabel at the top of the window, using HTML for formatting.
        StringBuilder rewardTree = new StringBuilder("<html><b>Reward Tree:</b><br>");
        for (int i = 0; i < rewards.length; i++) {
            rewardTree.append((i + 1)).append(" - ₹").append(rewards[i]).append("<br>");
        }
        rewardTree.append("</html>");
        JLabel rewardTreeLabel = new JLabel(rewardTree.toString());
        rewardTreeLabel.setForeground(Color.ORANGE);

        // Reward label
        rewardLabel = new JLabel(name + ", Your current reward is ₹" + earnedReward);
        rewardLabel.setForeground(Color.BLUE);

        // Question label
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setForeground(Color.DARK_GRAY);

        // Option buttons
        optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].setBackground(Color.LIGHT_GRAY);
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            int idx = i;
            optionButtons[i].addActionListener(e -> checkAnswer(idx + 1));
            optionsPanel.add(optionButtons[i]);
        }

        // Lifeline buttons
        /*Clicking a lifeline button triggers the 
        lifeline logic and shows results in a popup (JOptionPane.showMessageDialog).*/
        lifelinePanel = new JPanel(new FlowLayout());
        audienceBtn = new JButton("Audience Poll");
        fiftyFiftyBtn = new JButton("Fifty Fifty");
        audienceBtn.addActionListener(e -> useAudiencePoll());
        fiftyFiftyBtn.addActionListener(e -> useFiftyFifty());
        lifelinePanel.add(audienceBtn);
        lifelinePanel.add(fiftyFiftyBtn);

        // Layout  for rewardTreeLabel(west),rewardlabel(east),questionlabel(north),optionpanel(center)
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(rewardTreeLabel, BorderLayout.WEST);
        topPanel.add(rewardLabel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(questionLabel, BorderLayout.NORTH);
        centerPanel.add(optionsPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(lifelinePanel, BorderLayout.SOUTH);

        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestion >= questions.length) {
            showFinalResult();
            return;
        }
        questionLabel.setText("<html><b>Q" + (currentQuestion + 1) + ": " + questions[currentQuestion] + "</b></html>");
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText((i + 1) + ". " + options[currentQuestion][i]);
            optionButtons[i].setEnabled(true);
            optionButtons[i].setBackground(Color.LIGHT_GRAY);
        }
        audienceBtn.setEnabled(!audienceUsed);
        fiftyFiftyBtn.setEnabled(!fiftyFiftyUsed);
    }

    private void checkAnswer(int answer) {
        for (JButton btn : optionButtons) btn.setEnabled(false);

        if (answer == correctAnswers[currentQuestion]) {
            earnedReward = rewards[currentQuestion];
            rewardLabel.setText(name + ", Your current reward is ₹" + earnedReward);
            JOptionPane.showMessageDialog(this, "Congrats! Your answer is correct!", "Correct", JOptionPane.INFORMATION_MESSAGE);
            currentQuestion++;
            loadQuestion();
        } else {
            JOptionPane.showMessageDialog(this, "Sorry! Your answer is wrong.\nYou have earned ₹" + earnedReward, "Wrong Answer", JOptionPane.ERROR_MESSAGE);
            showFinalResult();
        }
    }

    private void useAudiencePoll() {
        audienceUsed = true;
        audienceBtn.setEnabled(false);

        Random rand = new Random();
        int correctPercent = 50 + rand.nextInt(31);
        int[] otherPercents = new int[3];
        int remaining = 100 - correctPercent;
        for (int i = 0; i < 2; i++) {
            otherPercents[i] = rand.nextInt(remaining + 1);
            remaining -= otherPercents[i];
        }
        otherPercents[2] = remaining;

        int correctIndex = correctAnswers[currentQuestion] - 1;
        int count = 0;
        StringBuilder poll = new StringBuilder("<html><b>Audience Poll Results:</b><br>");
        for (int i = 0; i < 4; i++) {
            int percent = (i == correctIndex) ? correctPercent : otherPercents[count++];
            poll.append(options[currentQuestion][i]).append(": ").append(percent).append("%<br>");
        }
        poll.append("</html>");
        JOptionPane.showMessageDialog(this, poll.toString(), "Audience Poll", JOptionPane.INFORMATION_MESSAGE);
    }

    private void useFiftyFifty() {
        fiftyFiftyUsed = true;
        fiftyFiftyBtn.setEnabled(false);

        int correctIndex = correctAnswers[currentQuestion] - 1;
        int randomIncorrect;
        do {
            randomIncorrect = new Random().nextInt(4);
        } while (randomIncorrect == correctIndex);

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setEnabled(i == correctIndex || i == randomIncorrect);
        }
    }

    private void showFinalResult() {
        StringBuilder result = new StringBuilder();
        result.append("<html><b>Thank you for playing, ").append(name).append("!</b><br>");
        result.append("City: ").append(city).append("<br>");
        result.append("DOB: ").append(dob).append("<br>");
        result.append("<b>Total Reward Earned: ₹").append(earnedReward).append("</b></html>");
        JOptionPane.showMessageDialog(this, result.toString(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);  //end the game after displaying the dialogue
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CrorePathiSwings().setVisible(true));
    }
}
