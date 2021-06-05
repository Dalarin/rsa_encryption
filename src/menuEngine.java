import java.awt.LayoutManager;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.*;
import java.util.Scanner;

class VerticalMenuBar extends JMenuBar {
    private static final LayoutManager grid = new GridLayout(0, 1);

    public VerticalMenuBar() {
        setLayout(grid);
    }
}

class MenuEngine extends Component implements ActionListener {
    SwingDemo parent;

    MenuEngine(SwingDemo parent) {
        this.parent = parent;
    }

    public void parseFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();
        String content = stringBuilder.toString();
        System.out.println(content);
    }

    private void getPathFile() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        File selectedFile = null;
        boolean statusOfFile = false;
        Scanner in = null;
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            try {
                in = new Scanner(selectedFile);
                statusOfFile = true;
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }
        if (statusOfFile)
            parseFile(selectedFile);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem clickedButton = (JMenuItem) e.getSource();
        Object src = e.getSource();
        if (src == parent.openFile) {
            try {
                getPathFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}


