package com.rsa.rsaencryption;
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class SwingDemo {
    static JFrame frame = new JFrame("Encryption");
    static JPanel panel = new JPanel();
    static JPanel nextPanel = new JPanel();
    static JPanel buttonPane;
    static JPanel fieldsPanel;
    static JMenuBar menuBar = new JMenuBar();
    static JMenuBar mb = new JMenuBar();
    JLabel privateKey;
    JLabel publicKey;
    JTextField cashField;
    JTextField checksField;
    JTextArea textArea;
    JScrollPane jScrollPane;
    JMenu fileMenu = new JMenu("Меню");
    JMenu fileMenuforWork = new JMenu("Файл");
    JMenuItem openFile = new JMenuItem("Открыть", KeyEvent.VK_N);
    JMenuItem saveFile = new JMenuItem("Сохранить");
    JMenu settingsMenu = new JMenu("Настройки");
    JMenuItem openMenu = new JMenuItem("Настройки");
    JMenu reference = new JMenu("Справка");
    JMenuItem aboutProgram = new JMenuItem("О программе");
    JMenuItem aboutDeveloper = new JMenuItem("О разработчике");
    JButton encrypt = new JButton("Зашифровать");
    JButton decrypt = new JButton("Дешифровать");
    JButton generate = new JButton("Сгенерировать ключи");

    public void CreateButtons() {
        nextPanel.add("North", encrypt);
        nextPanel.add("North", decrypt);
        nextPanel.revalidate();

    }
    SwingDemo() {
        RSAEncryption child = new RSAEncryption(this);
        MenuEngine menuEngine = new MenuEngine(this, child);
        textArea = new JTextArea();
        textArea.setColumns(5);
        textArea.setRows(5);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        jScrollPane = new JScrollPane (textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.setLayout(new BorderLayout());
        panel.add("Center", jScrollPane);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        fileMenu.add(fileMenuforWork);
        fileMenuforWork.add(openFile);
        fileMenuforWork.add(saveFile);
        fileMenuforWork.setMnemonic(KeyEvent.VK_F);
        menuBar.add(settingsMenu);
        menuBar.add(reference);
        reference.add(aboutDeveloper);
        reference.add(aboutProgram);
        settingsMenu.add(openMenu);
        openFile.addActionListener(menuEngine);
        saveFile.addActionListener(menuEngine);
        openMenu.addActionListener(menuEngine);
        encrypt.addActionListener(menuEngine);
        decrypt.addActionListener(menuEngine);
        generate.addActionListener(menuEngine);
        aboutDeveloper.addActionListener(menuEngine);
        aboutProgram.addActionListener(menuEngine);
        encrypt.setToolTipText("Вы можете выделить текст и зашифровать только его!");
        decrypt.setToolTipText("Вы можете выделить текст и дешифровать только его!");
    }
    public void createSettingsWindow() { // создаем окно настроек (доработать)
        JFrame frames = new JFrame("Настройки");
        frames.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frames.setSize(340, 340);
        frames.setVisible(true);
        buttonPane = new JPanel();
        fieldsPanel = new JPanel();
        privateKey = new JLabel("Приватный ключ");
        publicKey = new JLabel("Публичный ключ");
        cashField = new JTextField("");
        checksField = new JTextField("");
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
        buttonPane.setLayout(new FlowLayout());
        fieldsPanel.add(privateKey);
        fieldsPanel.add(cashField);
        fieldsPanel.add(publicKey);
        fieldsPanel.add(checksField);
        buttonPane.add(generate);
        frames.add(fieldsPanel, BorderLayout.PAGE_START);
        frames.add(buttonPane, BorderLayout.PAGE_END);
        frames.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frames.pack();
        frames.setVisible(true);


    }

    public static void main(final String[] args) {
        SwingDemo demo = new SwingDemo();
        panel.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIManager.put("MenuBar.background", Color.ORANGE);// цвет для верхней панели
        frame.setJMenuBar(mb);
        frame.setJMenuBar(menuBar);
        frame.setSize(550, 550);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(nextPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
