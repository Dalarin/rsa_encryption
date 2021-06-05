import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Savepoint;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class SwingDemo {
    static JFrame frame = new JFrame("Encryption");
    static JMenuBar menuBar = new JMenuBar();
    static JMenuBar mb = new JMenuBar();
    JMenu fileMenu = new JMenu("Меню");
    JMenu fileMenuforWork = new JMenu("Файл");
    JMenuItem openFile = new JMenuItem("Открыть", KeyEvent.VK_N);
    JMenuItem saveFile = new JMenuItem("Сохранить", KeyEvent.VK_N);
    JMenu reference = new JMenu("Справка");
    JMenuItem aboutProgram = new JMenuItem("О программе");
    JMenuItem aboutDeveloper = new JMenuItem("О разработчике");

    SwingDemo()
    {
        MenuEngine menuEngine = new MenuEngine(this);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        fileMenu.add(fileMenuforWork);
        fileMenuforWork.add(openFile);
        fileMenuforWork.add(saveFile);
        fileMenuforWork.setMnemonic(KeyEvent.VK_F);
        menuBar.add(reference);
        reference.add(aboutDeveloper);
        reference.add(aboutProgram);
        openFile.addActionListener(menuEngine);
    }
    public static void main(final String[] args) {
        SwingDemo demo = new SwingDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIManager.put("MenuBar.background", Color.ORANGE);// цвет для верхней панели
        frame.setJMenuBar(mb);
        frame.setJMenuBar(menuBar);
        frame.setSize(450, 450);
        frame.setVisible(true);
    }
}
