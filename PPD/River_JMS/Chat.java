import net.jini.space.JavaSpace;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout;
/*
 * Created by JFormDesigner on Mon Apr 27 01:08:17 BRT 2015
 */



/**
 * @author unknown
 */
public class Chat extends JPanel {
    Lookup finder;
    JavaSpace space;
    String userName = new String();
    String destinatario = new String();

    Map<String, String> textos = new HashMap<String,String>();

    public static void main(String[] args) {

        JFrame janela = new JFrame("");
        Chat meuPainel = new Chat();

        janela.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        janela.add(meuPainel);
        janela.setSize(600,450);
        janela.setVisible(true);
    }

    public Chat() {
        //Dados iniciais, destino e origem
        do{
            userName = JOptionPane.showInputDialog("Informe seu nome de usuario ");
        }while(userName.isEmpty());
        do{
            destinatario = JOptionPane.showInputDialog("Informe o nome do usuario de destino ");
        }while(destinatario.isEmpty());

        //carregaview
        initComponents();

        //Seta view
        remetenteLbl.setText(userName);
        destinatarioLbl.setText(destinatario);

        try {
            System.out.println("Procurando pelo servico JavaSpace...");

            //servidor do apache RIVER!
             finder = new Lookup(JavaSpace.class);
             space = (JavaSpace) finder.getService();

            if (space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                System.exit(-1);
            }
            System.out.println("O servico JavaSpace foi encontrado.");

        }catch (Exception e) {
            e.printStackTrace();
        }

        //verifica e recolhe mensagens
        //Eu sei que essa nao e a forma adequada de se fazer isso
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Message template = new Message();
                                template.receiver = Chat.this.userName;
                                Message msg = (Message) space.take(template, null, JavaSpace.NO_WAIT);
                                if(msg != null){
                                    System.out.println("Mensagem recebida de " + msg.sender + ": " + msg.content);

                                    textos.put(msg.sender, textos.get(msg.sender) + "\n" + msg.sender + " enviou :" + msg.content);
                                    textTxt.setText(textos.get(msg.sender));
                                }

                            } catch (Exception x) {
                                x.printStackTrace();
                            }
                        }
                    }
                },
                10
        );
;

    }


    private void sendBtnMouseClicked(MouseEvent e) {
        // TODO add your code here
        try{
            if (!(msgTxt.getText() == null || msgTxt.getText().equals(""))) {
                //Producao da mensagem
                Message msg = new Message();
                msg.content = msgTxt.getText();
                msg.sender = userName;
                msg.receiver = destinatario;
                msg.avaliable = "no";
                space.write(msg, null, 60 * 1000);

                //Gui Local
                //Pega o texto que tiver do usuario e concaten cm a msg que vc ta mandando agora!
                textos.put(msg.receiver, textos.get(msg.receiver)+"\n\nEu enviei :"+msg.content);
                textTxt.setText(textos.get(msg.receiver));
                msgTxt.setText("");
            }
        }catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - asdasdas asdadasdsa
        sendBtn = new JButton();
        textPane1 = new JTextPane();
        scrollPane1 = new JScrollPane();
        textTxt = new JTextPane();
        msgTxt = new JTextField();
        label1 = new JLabel();
        remetenteLbl = new JLabel();
        label3 = new JLabel();
        destinatarioLbl = new JLabel();

        //======== this ========

        // JFormDesigner evaluation mark
        setBorder(new javax.swing.border.CompoundBorder(
            new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


        //---- sendBtn ----
        sendBtn.setText("Send");
        sendBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendBtnMouseClicked(e);
            }
        });

        //======== scrollPane1 ========
        {

            //---- textTxt ----
            textTxt.setEditable(false);
            textTxt.setEnabled(false);
            scrollPane1.setViewportView(textTxt);
        }

        //---- label1 ----
        label1.setText("De");

        //---- remetenteLbl ----
        remetenteLbl.setText("text");

        //---- label3 ----
        label3.setText("Para");

        //---- destinatarioLbl ----
        destinatarioLbl.setText("text");

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup()
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 413, GroupLayout.PREFERRED_SIZE)
                        .addComponent(msgTxt, GroupLayout.PREFERRED_SIZE, 413, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(label1)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(remetenteLbl, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sendBtn, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(destinatarioLbl, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addComponent(label3))
                    .addContainerGap(1, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(label1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(remetenteLbl)
                                .addGap(18, 18, 18)
                                .addComponent(label3)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(destinatarioLbl)))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(textPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(msgTxt, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendBtn, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)))
                    .addGap(10, 10, 10))
        );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - asdasdas asdadasdsa
    private JButton sendBtn;
    private JTextPane textPane1;
    private JScrollPane scrollPane1;
    private JTextPane textTxt;
    private JTextField msgTxt;
    private JLabel label1;
    private JLabel remetenteLbl;
    private JLabel label3;
    private JLabel destinatarioLbl;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
