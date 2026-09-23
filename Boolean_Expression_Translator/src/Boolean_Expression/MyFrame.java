package Boolean_Expression;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public final class MyFrame extends JFrame implements ActionListener
{
    // ---- palette ----
    private static final Color BG = new Color(0xF3, 0xF5, 0xF9);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color CARD_BORDER = new Color(0xE2, 0xE8, 0xF0);
    private static final Color ACCENT = new Color(0x4F, 0x46, 0xE5);
    private static final Color ACCENT_HOVER = new Color(0x43, 0x38, 0xCA);
    private static final Color ACCENT_PRESS = new Color(0x37, 0x30, 0xA3);
    private static final Color TEXT_DARK = new Color(0x1E, 0x29, 0x3B);
    private static final Color TEXT_MUTED = new Color(0x64, 0x74, 0x8B);
    private static final Color TRUE_BG = new Color(0xDC, 0xFC, 0xE7);
    private static final Color TRUE_FG = new Color(0x15, 0x80, 0x3D);
    private static final Color FALSE_BG = new Color(0xFE, 0xE2, 0xE2);
    private static final Color FALSE_FG = new Color(0xB9, 0x1C, 0x1C);

    private JPanel panel1,panel2;
    private JLabel result;
    private JButton button;
    private JTextField inputExpression, output,numberOfGates;
    private Set<Character> variableSet;
    private ArrayList<JTextField> textFields;
    private ArrayList<JLabel> labels;
    private HashMap<Character, JTextField> variableFields;
    private CircuitPanel circuitPanel;

    public MyFrame(String title)
    {
    super(title);

    this.setLayout(new BorderLayout());
    getContentPane().setBackground(BG);

    this.result = new JLabel("Result");
    this.inputExpression = new PlaceholderTextField(30, "e.g. (A+B)^~C");
    this.numberOfGates = new JTextField(60);
    this.output = new JTextField(6);
    this.button = new RoundedButton("Add Expression", ACCENT, ACCENT_HOVER, ACCENT_PRESS);
    this.circuitPanel = new CircuitPanel();

    variableSet = new HashSet<>();
    textFields = new ArrayList<>();
    labels = new ArrayList<>();
    variableFields = new HashMap<>();
    button.addActionListener(this);

    // ---------------- header ----------------
    JPanel header = new JPanel();
    header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
    header.setBackground(ACCENT);
    header.setBorder(new EmptyBorder(18, 24, 18, 24));
    JLabel title1 = new JLabel("Boolean Logic Simulator");
    title1.setForeground(Color.WHITE);
    title1.setFont(title1.getFont().deriveFont(Font.BOLD, 22f));
    JLabel subtitle = new JLabel("Enter an expression to see its result, gate counts, and circuit diagram.");
    subtitle.setForeground(new Color(0xE3, 0xE1, 0xFC));
    subtitle.setFont(subtitle.getFont().deriveFont(Font.PLAIN, 13f));
    header.add(title1);
    header.add(Box.createVerticalStrut(4));
    header.add(subtitle);
    this.add(header, BorderLayout.NORTH);

    // ---------------- sidebar (expression + variables) ----------------
    RoundedPanel sidebar = new RoundedPanel(new BorderLayout(0, 12), CARD_BG, CARD_BORDER);
    sidebar.setBorder(new EmptyBorder(18, 18, 18, 18));
    sidebar.setPreferredSize(new Dimension(320, 10));

    JPanel topControls = new JPanel();
    topControls.setOpaque(false);
    topControls.setLayout(new BoxLayout(topControls, BoxLayout.Y_AXIS));

    inputExpression.setAlignmentX(LEFT_ALIGNMENT);
    inputExpression.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
    button.setAlignmentX(LEFT_ALIGNMENT);
    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    topControls.add(sectionLabel("Expression"));
    topControls.add(Box.createVerticalStrut(6));
    topControls.add(inputExpression);
    topControls.add(Box.createVerticalStrut(10));
    topControls.add(button);
    topControls.add(Box.createVerticalStrut(18));
    topControls.add(sectionLabel("Variables"));
    topControls.add(Box.createVerticalStrut(6));

    sidebar.add(topControls, BorderLayout.NORTH);

    this.panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
    panel1.setOpaque(false);
    JScrollPane varsScroll = new JScrollPane(panel1);
    varsScroll.setBorder(null);
    varsScroll.getViewport().setOpaque(false);
    varsScroll.setOpaque(false);
    sidebar.add(varsScroll, BorderLayout.CENTER);

    JPanel westWrap = new JPanel(new BorderLayout());
    westWrap.setBackground(BG);
    westWrap.setBorder(new EmptyBorder(16, 16, 16, 8));
    westWrap.add(sidebar, BorderLayout.CENTER);
    this.add(westWrap, BorderLayout.WEST);

    // ---------------- circuit diagram card ----------------
    JScrollPane circuitScroll = new JScrollPane(circuitPanel);
    circuitScroll.setBorder(null);

    RoundedPanel circuitCard = new RoundedPanel(new BorderLayout(), CARD_BG, CARD_BORDER);
    circuitCard.setBorder(new EmptyBorder(14, 14, 14, 14));
    JPanel circuitTitleWrap = new JPanel(new BorderLayout());
    circuitTitleWrap.setOpaque(false);
    circuitTitleWrap.setBorder(new EmptyBorder(0, 4, 10, 0));
    circuitTitleWrap.add(sectionLabel("Circuit Diagram"), BorderLayout.WEST);
    circuitCard.add(circuitTitleWrap, BorderLayout.NORTH);
    circuitCard.add(circuitScroll, BorderLayout.CENTER);

    JPanel centerWrap = new JPanel(new BorderLayout());
    centerWrap.setBackground(BG);
    centerWrap.setBorder(new EmptyBorder(16, 8, 8, 16));
    centerWrap.add(circuitCard, BorderLayout.CENTER);
    this.add(centerWrap, BorderLayout.CENTER);

    // ---------------- result / gate-count card ----------------
    result.setFont(result.getFont().deriveFont(Font.BOLD, 13f));
    result.setForeground(TEXT_MUTED);

    output.setEditable(false);
    output.setHorizontalAlignment(JTextField.CENTER);
    output.setFont(output.getFont().deriveFont(Font.BOLD, 15f));
    output.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 1, true),
            new EmptyBorder(6, 10, 6, 10)));
    output.setBackground(new Color(0xF1, 0xF5, 0xF9));
    output.setForeground(TEXT_MUTED);
    output.setText("--");

    numberOfGates.setEditable(false);
    numberOfGates.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
    numberOfGates.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 1, true),
            new EmptyBorder(6, 10, 6, 10)));
    numberOfGates.setBackground(new Color(0xF8, 0xFA, 0xFC));
    numberOfGates.setForeground(TEXT_DARK);

    this.panel2 = new RoundedPanel(new FlowLayout(FlowLayout.LEFT, 16, 10), CARD_BG, CARD_BORDER);
    panel2.setBorder(new EmptyBorder(10, 16, 10, 16));

    JPanel southWrap = new JPanel(new BorderLayout());
    southWrap.setBackground(BG);
    southWrap.setBorder(new EmptyBorder(0, 16, 16, 16));
    southWrap.add(panel2, BorderLayout.CENTER);
    this.add(southWrap, BorderLayout.SOUTH);
    }

    private JLabel sectionLabel(String text)
    {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 12f));
        l.setForeground(TEXT_MUTED);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    public void variableMethod(String str)
    {

        try
        {
            String expression = new String();
            expression = inputExpression.getText();
            if(!expression.isEmpty())
            {
                for (char c : expression.toCharArray())
                {
                variableSet.add(c);
                System.out.println(variableSet);

                }
                System.out.println("---------------------------------");
                for (char item : variableSet)
                {
                    System.out.println(item);
                    if(item >= 65 && item <= 90 || item >= 97 && item<=122)
                    {

                        JLabel varLabel = new JLabel(String.valueOf(item));
                        varLabel.setOpaque(true);
                        varLabel.setBackground(new Color(0xEE, 0xF0, 0xFF));
                        varLabel.setForeground(new Color(0x1E, 0x29, 0x3B));
                        varLabel.setFont(varLabel.getFont().deriveFont(Font.BOLD, 13f));
                        varLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        varLabel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(0xCB, 0xD5, 0xE1), 1, true),
                                new EmptyBorder(4, 10, 4, 10)));

                        // Not for typing into -- this box just displays whatever
                        // value that variable ends up holding once it's evaluated.
                        JTextField varField = new JTextField(4);
                        varField.setEditable(false);
                        varField.setHorizontalAlignment(JTextField.CENTER);
                        varField.setFont(varField.getFont().deriveFont(Font.BOLD, 13f));
                        varField.setText("--");
                        varField.setBackground(new Color(0xF1, 0xF5, 0xF9));
                        varField.setForeground(TEXT_MUTED);
                        varField.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(0xCB, 0xD5, 0xE1), 1, true),
                                new EmptyBorder(4, 8, 4, 8)));

                        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
                        row.setOpaque(false);
                        row.setAlignmentX(LEFT_ALIGNMENT);
                        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                        row.add(varLabel);
                        row.add(varField);
                        panel1.add(row);

                        labels.add(varLabel);
                        textFields.add(varField);
                        variableFields.put(item, varField);
                    }
                }

                panel1.revalidate(); // Revalidate the panel to reflect changes
                panel1.repaint();
                System.out.println("---------------------------------");
                inputExpression.setText("");
            }

        }
        catch(Exception e)
        {System.out.println(e);}
    }

public String convertInput(String str) {
    System.out.println("---------------------------------");
    System.out.println( str);
    System.out.println("---------------------------------");
    java.util.HashMap<Character, Boolean> values = new java.util.HashMap<>();
    StringBuilder result = new StringBuilder();
    try {
        for (char variable : variableSet) {
            if ((variable >= 'A' && variable <= 'Z') || (variable >= 'a' && variable <= 'z'))
            {
                String input = javax.swing.JOptionPane.showInputDialog("Enter value for variable " + variable + ":");

                boolean value = (input != null && input.equals("1"));

                values.put(variable, value);

                // Show the value this variable is holding in its box.
                JTextField field = variableFields.get(variable);
                if (field != null) {
                    field.setText(value ? "1" : "0");
                    field.setBackground(value ? TRUE_BG : FALSE_BG);
                    field.setForeground(value ? TRUE_FG : FALSE_FG);
                }
            }
        }
        System.out.println(variableSet);
        System.out.println(values);

        // Convert the input string
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) {
                Boolean get = values.get(ch);
                if (get != null)
                {result.append(get ? "1" : "0");}
                else {
                    System.out.println("Value for variable " + ch + " is missing.");
                    return "";
                }
            } else {
                result.append(ch);
            }
        }

        if (result.length() > 0)
        { return result.toString();}
        else {
            System.out.println("Postfix expression is empty.");
            return "";
        }
    } catch (Exception e) {
        System.out.println("Exception occurred during conversion: " + e.getMessage());
        return "";
    }
}
public void evaluateNumberOfOperators(String str)
{
    int a,b,c;
    a=b=c=0;
    for (int i = 0; i < str.length(); i++) {
         if(str.charAt(i) == '^')
             a++;
         if(str.charAt(i) == '+')
             b++;
         if(str.charAt(i) == '~' || str.charAt(i) == '\'')
             c++;
    }
       panel2.add(numberOfGates);
       numberOfGates.setText("\t"+"No of AND Gates:" + a +"\t" +"No of OR Gates: " + b +"\t"+ "No of Not Gates:" + c);
       panel2.revalidate();
}

    @Override
    public void actionPerformed(ActionEvent e)
    {

        String expression = inputExpression.getText();
        if(e.getSource() == button && !inputExpression.getText().isEmpty() )
       {
           if(!variableSet.isEmpty())
           {
               System.out.println("++++++++++++++++++++++++++++++++");
               System.out.println(variableSet);
               variableSet.removeAll(variableSet);
               System.out.println(variableSet);
               System.out.println("++++++++++++++++++++++++++++++++");
               panel1.removeAll();
               labels.clear();
               textFields.clear();
               variableFields.clear();

               panel1.repaint();
               panel1.revalidate();

           }
           panel2.add(result);
           panel2.add(output);
           panel2.revalidate();


           variableMethod(inputExpression.getText());
           expression = InfixToPostfix.infixToPostfix(expression);

           // Keep a copy of the postfix expression WITH the variable letters
           // still in it (A, B, C ...) before convertInput turns them into
           // 0s and 1s -- that's what the circuit diagram needs to show
           // variable names instead of literal values.
           String postfixWithVariables = expression;

           expression = convertInput(expression);
           evaluateNumberOfOperators(expression);
           Boolean result = PostfixEvaluater.evaluatePostfix(expression);
           System.out.println(result);

           if(result == true)
           {
               output.setText("TRUE");
               output.setBackground(TRUE_BG);
               output.setForeground(TRUE_FG);
           }
           else if ( result == false)
           {
               output.setText("FALSE");
               output.setBackground(FALSE_BG);
               output.setForeground(FALSE_FG);
           }

           // Build and draw the gate diagram for this expression.
           CircuitNode root = CircuitBuilder.buildTree(postfixWithVariables);
           circuitPanel.setExpression(root);


       }

    }

    public static void main(String[] args)
    {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // fall back to the platform default look and feel
        }

        MyFrame frame = new MyFrame("LogicSimulator");
        frame.setSize(1280, 800);
        frame.setMinimumSize(new Dimension(1000, 650));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


    }

}