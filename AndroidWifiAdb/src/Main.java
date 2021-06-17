import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Main {

    static JTextField input = new JTextField();
    static JTextArea area = new JTextArea();

    public static void main(String[] args) {

            JFrame window = new JFrame("WIFI ADB Connector");
            Image img = Toolkit.getDefaultToolkit().createImage("wifi.png");

            window.setSize(305, 190);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.getContentPane().setBackground(Color.white);
            window.setTitle("Wifi ADB");
            window.setIconImage(img);
            window.setLayout(null);
            window.setResizable(false);
            window.setLocationRelativeTo(null);

        try(FileReader reader = new FileReader("ipinfo.txt"))
        {
            int c;
            while((c=reader.read())!=-1){

                System.out.print((char)c);
                String savedText = input.getText();
                input.setText(savedText + (char)c);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

            input.setFont(new Font("Arial", Font.BOLD, 40));
            input.setBounds(10, 10, 270, 50);
            input.setBackground(Color.white);
            input.setHorizontalAlignment(JTextField.LEFT);

            area.setFont(new Font("Arial", Font.BOLD, 15));
            area.setBounds(10, 120, 270, 50);
            area.setBackground(Color.WHITE);
            area.setText("Connection info...");

            JButton jbutton2 = new JButton();
            jbutton2 = new JButton();
            jbutton2.setFont(new Font("Arial", Font.PLAIN, 20));
            jbutton2.setText("Connect");
            jbutton2.setMargin(new Insets(0, 0, 0, 0));
            jbutton2.setSize(200, 50);
            jbutton2.setBounds(10, 60, 270, 50);
            window.add(jbutton2);

            ActionListener num_button = new GoNumListener();
            jbutton2.addActionListener(num_button);

            window.add(input);
            window.add(area);


                window.setVisible(true);
    }

    public static class GoNumListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {

            String name = ((JButton)e.getSource()).getText();

            if (name.equals("Connect")){
                try {
                    try(FileWriter writer = new FileWriter("ipinfo.txt", false))
                    {
                        String text = input.getText();
                        writer.write(text);

                        writer.flush();
                    }
                    catch(IOException ex){

                        System.out.println(ex.getMessage());
                    }
                    connect(input.getText());
                    System.out.println(input.getText());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        }
    }

    public static void connect(String ip) throws Exception{
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd \"C:\\Users\\rusla\\AppData\\Local\\Android\\Sdk\\platform-tools\" && adb tcpip 5555 && adb connect " + ip);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
            area.setText("  " + line);
        }

    }
}