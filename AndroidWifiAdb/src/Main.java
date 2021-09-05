import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class Main {

    static JTextField inputIP = new JTextField();
    static JTextField inputPath = new JTextField();
    static JTextArea area = new JTextArea();
    static JTextArea areaPath = new JTextArea();
    static JTextArea areaIP = new JTextArea();

    public static void main(String[] args) {

            JFrame window = new JFrame("WIFI ADB Connector");
            Image img = Toolkit.getDefaultToolkit().createImage("wifi.png");

            window.setSize(1200, 330);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.getContentPane().setBackground(Color.white);
            window.setTitle("Wifi ADB");
            window.setIconImage(img);
            window.setLayout(null);
            window.setResizable(false);
            window.setLocationRelativeTo(null);

        try {
            File myObj = new File("ipinfo.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
                inputIP.setText(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("pathinfo.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
                inputPath.setText(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

            areaIP.setFont(new Font("Arial", Font.BOLD, 20));
            areaIP.setBounds(10, 10, 1150, 50);
            areaIP.setBackground(Color.WHITE);
            areaIP.setText("Phone`s local IP address");
            window.add(areaIP);

            inputIP.setFont(new Font("Arial", Font.BOLD, 40));
            inputIP.setBounds(10, 35, 1150, 50);
            inputIP.setBackground(Color.white);
            inputIP.setHorizontalAlignment(JTextField.LEFT);
            window.add(inputIP);

            areaPath.setFont(new Font("Arial", Font.BOLD, 20));
            areaPath.setBounds(10,  95, 1150, 50);
            areaPath.setBackground(Color.WHITE);
            areaPath.setText("Path to platform-tools");
            window.add(areaPath);

            inputPath.setFont(new Font("Arial", Font.BOLD, 40));
            inputPath.setBounds(10, 120, 1150, 50);
            inputPath.setBackground(Color.white);
            inputPath.setHorizontalAlignment(JTextField.LEFT);
            window.add(inputPath);

            JButton jbutton2 = new JButton();
            jbutton2.setFont(new Font("Arial", Font.BOLD, 25));
            jbutton2.setText("Connect");
            jbutton2.setBounds(10, 180, 1150, 50);
            window.add(jbutton2);
            ActionListener num_button = new GoNumListener();
            jbutton2.addActionListener(num_button);

            area.setFont(new Font("Arial", Font.BOLD, 25));
            area.setBounds(10, 240, 1150, 50);
            area.setBackground(Color.WHITE);
            area.setText("Connection info...");
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
                        String text = inputIP.getText();
                        writer.write(text);
                        writer.flush();
                    }
                    catch(IOException ex){

                        System.out.println(ex.getMessage());
                    }

                    try(FileWriter writer = new FileWriter("pathinfo.txt", false))
                    {
                        String text = inputPath.getText();
                        writer.write(text);
                        writer.flush();
                    }
                    catch(IOException ex){

                        System.out.println(ex.getMessage());
                    }
                    connect(inputPath.getText() ,inputIP.getText());
                    System.out.println(inputIP.getText());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public static void connect(String path, String ip) throws Exception{
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "cd " + path + " && adb tcpip 5555 && adb connect " + ip);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), "866"));
        final String[] line = new String[1];
        new Thread(() -> {
            while (true) {
                try {
                    line[0] = r.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (line[0] == null) { break; }
                System.out.println(line[0]);
                area.setText(line[0]);
            }
        }).start();
    }
}