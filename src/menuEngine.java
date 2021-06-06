import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.List;
import java.util.Scanner;

class RSAEncryption {
    public boolean statusOfKeys = false;

    public void genKeys() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        savePublicKey(pair.getPublic());
        savePrivateKey(pair.getPrivate());
        statusOfKeys = true;
    }


    private void savePrivateKey(PrivateKey privateKey) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("private.key")) {
            fos.write(privateKey.getEncoded());
        }
    }

    private void savePublicKey(PublicKey publicKey) {
        try (FileOutputStream fos = new FileOutputStream("public.pub")) {
            fos.write(publicKey.getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PrivateKey readPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get("private.key"));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        System.out.println(kf.generatePrivate(spec));
        return kf.generatePrivate(spec);
    }

    private PublicKey readPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get("public.pub"));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);

    }

    public byte[] Encryption(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher encrypt = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, readPublicKey());
        return encrypt.doFinal(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String Decryption(String cryptedTEXT) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException, InvalidKeyException {
        Cipher decrypt = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        decrypt.init(Cipher.DECRYPT_MODE, readPrivateKey());
        return new String(decrypt.doFinal(cryptedTEXT.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}

class MenuEngine extends Component implements ActionListener {
    SwingDemo parent;
    RSAEncryption child;
    String textInFile = "";
    String pathOfFile = "";

    MenuEngine(SwingDemo parent, RSAEncryption child) {
        this.parent = parent;
        this.child = child;
    }

    private static boolean statusOfFile = false;

    private void parseDOCXFile(File file) {
        try {
            File newfile = new File(String.valueOf(file));
            FileInputStream fis = new FileInputStream(newfile.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                textInFile += para.getText();
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
        textInFile = stringBuilder.toString();

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
        if (statusOfFile) { // тут надо разбить на строку и проверять txt или docx
            this.pathOfFile = String.valueOf(selectedFile);
            String[] parts = String.valueOf(selectedFile).split("\\.");
            if (parts[1].equals("txt")) {
                parseTXTFile(selectedFile);
                parent.CreateButtons();
            } else if (parts[1].equals("docx")) {
                parseDOCXFile(selectedFile);
                parent.CreateButtons();
            }
        }
    }

    private void writeTEXT() throws IOException {
        FileWriter fileWriter = new FileWriter(this.pathOfFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.printf(textInFile);
        printWriter.close();

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
        } else if (src == parent.encrypt) {
            File privateKey = new File("D:\\javaProject\\practicecppp\\private.key");
            File publicKey = new File("D:\\javaProject\\practicecppp\\public.pub");
            if ((privateKey.exists() && !privateKey.isDirectory()) && (publicKey.exists() && !publicKey.isDirectory())) {
                try {
                    child.Encryption(textInFile);
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | IOException | InvalidKeySpecException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                }
            }
        } else if (src == parent.decrypt) {
            File privateKey = new File("D:\\javaProject\\practicecppp\\private.key");
            File publicKey = new File("D:\\javaProject\\practicecppp\\public.pub");
            if ((privateKey.exists() && !privateKey.isDirectory()) && (publicKey.exists() && !publicKey.isDirectory())) {
                try {
                    RSAEncryption.Decryption(textInFile);
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | IOException | InvalidKeySpecException | InvalidKeyException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                }
            }

        }
    }
}


