package homework11;

import java.io.File;
import java.io.FileReader;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.awt.*;
import javax.swing.*;

/**
 * ������֧���˱�--Ӧ�ó���
 * ���ܣ�1. ��ȡtxt�ļ��м�¼���û�����֧�������ʾ��
 *      2. ������֧�����¼��ϵͳ�Զ�������û��Ľ����
 * ����&ʹ�÷�������homework11ѹ������ѹ������ӵ���Ŀsrc�ļ����£�ֱ�ӵ�����У�
 *             �����ѡ���ı��ļ����롱����Ĭ��·��ΪD�̣�ѡ���ļ����µ�file.txt��������ʾ��֧�����
 *             ���"������֧��¼"����д���룬֧������ΪDouble���ͣ���ϵͳ�Զ����¼�������
 * �ļ�˵����homework11.java����Ŀ���룻
 *         file.txt: �û�����֧���txt��¼�ļ���
 *         image.jpg: �û���ͷ��
 * ���ü�����1. Swing��JFrame/JButton�Ĵ��弰��� ͼ�λ��û����棻
 *         2. ͼ���ȡ����ʾ���ܣ�ImageIcon������
 *         3. txt�ļ���ȡ���ܣ�����ѡ���ļ���
 *         4. �Զ���������������һ�εĽ�������ε����룬֧���������µ�һ�ֽ����
 *         5. ����������ΪUsers�࣬����������˽���������ݶ�ȡ��
 */
public class homework11 extends JFrame {
    private JFrame frame = null;

    private File file = null;
    private FileReader fileReader = null;
    private JFileChooser fileChooser = null;

    private JLabel image = null;
    private JLabel title = null;
    private JButton loadin = null;

    private DefaultTableModel tableModel = null;
    private JTable table = null;

    private JButton btnInsert = null;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new homework11();
        });
    }
    public homework11() {
        initialize();
    }

    private void initialize() {
        JPanel pnlTop = new JPanel();
        ImageIcon img = new ImageIcon("src\\homework11\\image.jpg");
        img.setImage(img.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
        image = new JLabel(img);
        title = new JLabel("������֧����", JLabel.CENTER);
        loadin = new JButton("ѡ���ı��ļ�����");
        pnlTop.add(image);
        pnlTop.add(title);
        pnlTop.add(loadin);

        tableModel = new DefaultTableModel();
        table=new JTable(tableModel);

        JPanel pnlBottom = new JPanel();
        btnInsert = new JButton("������֧��¼");
        pnlBottom.add(btnInsert);

        frame = new JFrame("������֧����");
        frame.add(pnlTop, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(pnlBottom, BorderLayout.SOUTH);

        loadin.addActionListener(e -> doLoadin());
        btnInsert.addActionListener(e -> doInsert());

        frame.setSize(500, 500);
        frame.setLocation(300, 150);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * ������֧��¼�ı��ļ�
     */
    private void doLoadin() {
        fileChooser=new JFileChooser("D:\\");
        if(fileChooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION) {
            file=fileChooser.getSelectedFile();
            try {
                fileReader=new FileReader(file);
                Vector<String> vector=new Vector<String>();
                vector.add("ID");vector.add("����");vector.add("����");vector.add("֧��");vector.add("����");
                Vector<Vector<String>> dataVector=new Vector<Vector<String>>();
                Vector<String> dataVector2=new Vector<String>();
                String string="";
                while(fileReader.ready()) {
                    char ch=(char) fileReader.read();
                    if(ch==' ') {
                        dataVector2.add(string);
                        string="";
                    }
                    else if(ch=='\n') {
                        dataVector2.add(string);
                        string="";
                        dataVector.add(dataVector2);
                        dataVector2=new Vector<String>();
                    }
                    else {
                        string=string+ch;
                    }
                }
                tableModel.setDataVector(dataVector, vector);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ������֧��¼
     */
    private void doInsert(){
        double lastsurplus = Double.parseDouble(tableModel.getValueAt(tableModel.getRowCount()-1, tableModel.getColumnCount()-1).toString());

        ContactsDetailDialog inputDialog = new ContactsDetailDialog();
        inputDialog.uiClear();
        inputDialog.setVisible(true);
        System.out.println(inputDialog.isOkPressed());
        if (!inputDialog.isOkPressed())
            return;

        Users inputContact = inputDialog.ui2entity();
        System.out.println(inputContact);
        
        Vector<String> newdataVector=new Vector<String>();
        newdataVector.add(tableModel.getValueAt(tableModel.getRowCount()-1, 0).toString());
        newdataVector.add(tableModel.getValueAt(tableModel.getRowCount()-1, 1).toString());
        newdataVector.add(inputContact.getIncome().toString());
        newdataVector.add(inputContact.getExpense().toString());
        newdataVector.add(String.valueOf((lastsurplus+inputContact.getIncome()-inputContact.getExpense())));

        tableModel.addRow(newdataVector);
    }
}

/**
 * �������
 */
class ContactsDetailDialog extends JDialog {
    boolean okPressed = false;

    private JLabel lblIncome = null;
    private JTextField txtIncome = null;
    private JLabel lblExpense = null;
    private JTextField txtExpense = null;

    private JPanel pnlInput = null;
    private JPanel pnlButtons = null;

    private JButton btnOk = null;
    private JButton btnCancel = null;

    public ContactsDetailDialog() {
        lblIncome = new JLabel("����");
        txtIncome = new JTextField();
        lblExpense = new JLabel("֧��");
        txtExpense = new JTextField();

        pnlInput = new JPanel();
        pnlInput.setLayout(new GridLayout(2, 2));

        pnlInput.add(lblIncome);
        pnlInput.add(txtIncome);
        pnlInput.add(lblExpense);
        pnlInput.add(txtExpense);

        pnlButtons = new JPanel();
        btnOk = new JButton("Ok");
        btnCancel = new JButton("Cancel");
        pnlButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlButtons.add(btnOk);
        pnlButtons.add(btnCancel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(pnlInput, BorderLayout.CENTER);
        getContentPane().add(pnlButtons, BorderLayout.SOUTH);

        setSize(300, 150);
        this.setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnOk.addActionListener(e -> {
            okPressed = true;
            dispose();
        });
        btnCancel.addActionListener(e -> {
            okPressed = false;
            dispose();
        });
    }

    public boolean isOkPressed() {
        return okPressed;
    }

    public Users ui2entity() {
        Users c = new Users();
        c.setIncome(Double.parseDouble(txtIncome.getText()));
        c.setExpense(Double.parseDouble(txtExpense.getText()));
        return c;
    }

    public void uiClear() {
        txtIncome.setText("");
        txtExpense.setText("");
    }
}

/**
 * ������֧�����
 */
class Users {
    private int id;
    private String name = null;
    private Double income = null;
    private Double expense = null;
    private Double surplus = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public Double getSurplus() {
        return surplus;
    }

    public void setSurplus(Double surplus) {
        this.surplus = surplus;
    }

}