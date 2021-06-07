import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.crypto.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.*;
import java.security.spec.*;

import org.apache.commons.codec.binary.Base64;

import java.util.List;
import java.util.Scanner;


import java.security.NoSuchAlgorithmException;

class RSAEncryption {
    private static final int MAX_ENCRYPT_BLOCK = 117; // максимальный размер шифрования RSA (в байтах)
    private static final int MAX_DECRYPT_BLOCK = 128; // максимальный размер дешифрования RSA (в байтах)

    private static KeyPair getKeyPair() throws Exception { // генерируем пару для генерации ключей
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    public void genKeys() throws Exception { // генерируем два ключа и сохраняем их в файл
        KeyPair keyPair = getKeyPair();
        String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded())); // getEncoded возвращает данные в формате DER
        String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
        saveKeys(privateKey, publicKey);
    }

    public static PrivateKey getPrivateKey(String privateKey) throws Exception { // создаем приватный ключ из строки
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKey(String publicKey) throws Exception { // создаем публичный ключ из строки
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }


    private void saveKeys(String privateKey, String publicKey) throws IOException { // сохраняем ключи в файл (тут бы еще hex шифрование)
        BufferedWriter writer = new BufferedWriter(new FileWriter("private.key"));
        writer.write(privateKey);
        writer.close();
        writer = new BufferedWriter(new FileWriter("public.pub"));
        writer.write(publicKey);
        writer.close();

    }


    private static String readPrivateKey() throws IOException { // читаем ключ из файла (для того, чтоб пользователь сам выбирал, когда генерировать новый ключ)
        BufferedReader reader = new BufferedReader(new FileReader("private.key"));
        String currentLine = reader.readLine();
        reader.close();
        return currentLine;
    }

    private String readPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException { // читаем ключ из файла (для того, чтоб пользователь сам выбирал, когда генерировать новый ключ)
        BufferedReader reader = new BufferedReader(new FileReader("public.pub"));
        String currentLine = reader.readLine();
        reader.close();
        return currentLine;
    }


    public String Encryption(String text) throws
            Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(readPublicKey()));
        int inputLen = text.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // Кодируем один из сегментов текста
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(text.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(text.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return new String(Base64.encodeBase64String(encryptedData));
    }

    public static String Decryption(String cryptedTEXT) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(readPrivateKey()));
        byte[] dataBytes = Base64.decodeBase64(cryptedTEXT);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        System.out.println(new String(decryptedData, "UTF-8"));
        return new String(decryptedData, "UTF-8");

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

    private void parseDOCXFile(File file) { // парсим DOCX файл с помощью библиотеки Apache
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

    private void createSettingsWindow() { // создаем окно настроек (доработать)
        JFrame frames = new JFrame("Настройки");
        frames.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frames.setSize(300, 300);
        frames.setVisible(true);

    }

    private void getPathFile() throws IOException { // получаем ссылку на файл
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


    private void parseTXTFile(File file) throws IOException { // парсим TXT документ
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

    private void writeTEXT() throws IOException { // оно должно записывать текст в файл. Не реализовано, пока что
        FileWriter fileWriter = new FileWriter(this.pathOfFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.printf(textInFile);
        printWriter.close();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src instanceof JMenuItem) {
            JMenuItem clickedButton = (JMenuItem) e.getSource();
            if (src == parent.openFile) {
                try {
                    getPathFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (src == parent.openMenu) {
                createSettingsWindow();
                try {
                    child.genKeys();
                } catch (Exception noSuchAlgorithmException) {
                    noSuchAlgorithmException.printStackTrace();
                }
            }
        } else if (src instanceof JButton) {
            JButton clickednotItem = (JButton) e.getSource();
            if (src == parent.encrypt) {
                File privateKey = new File("D:\\javaProject\\practicecppp\\private.key");
                File publicKey = new File("D:\\javaProject\\practicecppp\\public.pub");
                if ((privateKey.exists() && !privateKey.isDirectory()) && (publicKey.exists() && !publicKey.isDirectory())) {
                    try {
                        textInFile = child.Encryption(textInFile);
                    } catch (Exception noSuchPaddingException) {
                        noSuchPaddingException.printStackTrace();
                    }
                }
            } else if (src == parent.decrypt) {
                File privateKey = new File("D:\\javaProject\\practicecppp\\private.key");
                File publicKey = new File("D:\\javaProject\\practicecppp\\public.pub");
                System.out.println("SOMETHING HAPPENED");
                if ((privateKey.exists() && !privateKey.isDirectory()) && (publicKey.exists() && !publicKey.isDirectory())) {
                    try {
                        RSAEncryption.Decryption(textInFile);
                        System.out.println("SOMETHING HAPPENED");
                    } catch (Exception noSuchPaddingException) {
                        noSuchPaddingException.printStackTrace();
                    }
                }

            }
        }
    }
}

