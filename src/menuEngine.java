import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;


class MenuEngine extends Component implements ActionListener {
    SwingDemo parent;

    MenuEngine(SwingDemo parent) {
        this.parent = parent;
    }

    public void parseDOCXFile(File file) throws IOException {
        String textofFile;
        try {
            File newfile = new File(String.valueOf(file));
            FileInputStream fis = new FileInputStream(newfile.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                System.out.println(para.getText()); // здесь нужно вызывать функцию шифрования, построчно (походу)
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseTXTFile(File file) throws IOException {
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
        String[] parts = String.valueOf(selectedFile).split("\\.");
        if (statusOfFile) { // тут надо разбить на строку и проверять txt или docx
            if (parts[1].equals("txt")) {
                parseTXTFile(selectedFile);
            } else if (parts[1].equals("docx")) {
                parseDOCXFile(selectedFile);
            }
        }
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


