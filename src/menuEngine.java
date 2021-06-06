import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.List;
import java.util.Scanner;

class RSAEncryption {
    public PrivateKey genPrivateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        return pair.getPrivate();
    }


    public void savePrivateKey() throws NoSuchAlgorithmException, IOException {
        try (FileOutputStream fos = new FileOutputStream("private.der")) {
            fos.write(genPrivateKey().getEncoded());
        }
    }

    private static String File2String(File fileName) throws java.io.IOException {
        File file = new File(String.valueOf(fileName));
        char[] buffer = null;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        buffer = new char[(int) file.length()];
        int i = 0;
        int c = bufferedReader.read();
        while (c != -1) {
            buffer[i++] = (char) c;
            c = bufferedReader.read();
        }
        return new String(buffer);
    }


    private PrivateKey readRSAPEMKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get("private.der"));
        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        System.out.println(kf.generatePrivate(spec));
        return kf.generatePrivate(spec);
    }


    public void generatePublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey myPrivateKey = readRSAPEMKey();
        RSAPrivateCrtKeySpec privk = (RSAPrivateCrtKeySpec) myPrivateKey;
        RSAPublicKeySpec publicKeySpec = new java.security.spec.RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey myPublicKey = keyFactory.generatePublic(publicKeySpec);
        System.out.println(myPublicKey);

    }
}

class MenuEngine extends Component implements ActionListener {
    SwingDemo parent;
    RSAEncryption child;

    MenuEngine(SwingDemo parent, RSAEncryption child) {
        this.parent = parent;
        this.child = child;
    }

    private static boolean statusOfFile = false;

    private void parseDOCXFile(File file) {
        String textofFile = "";
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

    private void createSettingsWindow() {
        JFrame frames = new JFrame("Настройки");
        frames.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frames.setSize(300, 300);
        frames.setVisible(true);

    }

    private void parseTXTFile(File file) throws IOException {
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
                parent.CreateButtons();
            } else if (parts[1].equals("docx")) {
                parseDOCXFile(selectedFile);
                parent.CreateButtons();
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
        } else if (src == parent.openMenu) {
            createSettingsWindow();
            try {
                child.generatePublicKey();
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
            }
        }
    }
}


