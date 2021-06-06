import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Savepoint;
import javax.swing.*;

public class SwingDemo {
    static JFrame frame = new JFrame("Encryption");
    static JPanel panel = new JPanel();
    static JMenuBar menuBar = new JMenuBar();
    static JMenuBar mb = new JMenuBar();
    JMenu fileMenu = new JMenu("Меню");
    JMenu fileMenuforWork = new JMenu("Файл");
    JMenuItem openFile = new JMenuItem("Открыть", KeyEvent.VK_N);
    JMenuItem saveFile = new JMenuItem("Сохранить", KeyEvent.VK_N);
    JMenu settingsMenu = new JMenu("Настройки");
    JMenuItem openMenu = new JMenuItem("Настройки");
    JMenu reference = new JMenu("Справка");
    JMenuItem aboutProgram = new JMenuItem("О программе");
    JMenuItem aboutDeveloper = new JMenuItem("О разработчике");
    JButton encrypt = new JButton("Зашифровать");
    JButton decrypt = new JButton("Дешифровать");

    public void CreateButtons() {
        panel.add("South", encrypt);
        panel.add("South", decrypt);
        panel.revalidate();

    }

    SwingDemo() {
        RSAEncryption child = new RSAEncryption();
        MenuEngine menuEngine = new MenuEngine(this, child);
        GridLayout gl = new GridLayout(12, 12);
        panel.setLayout(gl);
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
        openMenu.addActionListener(menuEngine);
    }

    public static void main(final String[] args) {
        SwingDemo demo = new SwingDemo();
        panel.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIManager.put("MenuBar.background", Color.ORANGE);// цвет для верхней панели
        frame.setJMenuBar(mb);
        frame.setJMenuBar(menuBar);
        frame.setSize(450, 450);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
