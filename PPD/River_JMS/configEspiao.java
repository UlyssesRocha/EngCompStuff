/*
 * Created by JFormDesigner on Mon Apr 27 01:38:35 BRT 2015
 */

import java.awt.event.*;

import inverter.HelloWorldImplService;
import inverter.HelloWorldWS;
import net.jini.space.JavaSpace;

import javax.swing.*;
import java.util.ArrayList;
/**
 * @author asdasdas asdadasdsa
 */
public class configEspiao extends JPanel {
    Lookup finder;
    JavaSpace space;
    ArrayList<String> palavras = new ArrayList<String> ();
    HelloWorldImplService helloWorldService;
    HelloWorldWS helloWorld;

    public configEspiao() {
        initComponents();
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            finder = new Lookup(JavaSpace.class);
            space = (JavaSpace) finder.getService();

             helloWorldService = new HelloWorldImplService();
            helloWorld = (HelloWorldWS) helloWorldService.getHelloWorldImplPort();

            if (space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                System.exit(-1);
            }
            System.out.println("O servico JavaSpace foi encontrado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //verifica e recolhe mensagens
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                    Message template = new Message();
                                    template.avaliable = "no";
                                    Message msg = (Message) space.read(template, null, 60 * 1000);
                                    if(msg!=null){
                                        //Tratando mensagem
                                        System.out.println("Mensagem recebida de " + msg.sender + " para " + msg.receiver + " :  " + msg.content);
                                        for (int i = 0; i < palavras.size(); i++) {
                                            if (msg.content.contains(palavras.get(i))) {
                                                //AVISA QUE TEM UMA MENSAGEM SUSPEITA
                                                System.out.println("mensagem suspeita" + msg.content);
                                                helloWorld.helloWorld(msg.sender);//Mensagem para o webservice
                                            }
                                        }
                                    }

                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                    }
                }, 1
        );
    }

    private void addBtnMouseClicked(MouseEvent e) {
        // TODO add your code here
        palavras.add(newWord.getText()); // Adiciona no array de palavras a serem buscadas
        //adicioa na view do usuario
        listOfWords.append("\n" + newWord.getText());
        newWord.setText("");
    }

    private void initComponents () {
                                // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
                                // Generated using JFormDesigner Evaluation license - asdasdas asdadasdsa
                                scrollPane1 = new JScrollPane();
                                listOfWords = new JTextArea();
                                addBtn = new JButton();
                                remBtn = new JButton();
                                newWord = new JTextField();

                                //======== this ========

                                // JFormDesigner evaluation mark
                                setBorder(new javax.swing.border.CompoundBorder(
                                    new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                                        "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                                        javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                                        java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent e) {
                if ("border".equals(e.getPropertyName())) throw new RuntimeException();}});


                                //======== scrollPane1 ========
                                {

                                    //---- listOfWords ----
                                    listOfWords.setEditable(false);
                                    scrollPane1.setViewportView(listOfWords);
                                }

                                //---- addBtn ----
                                addBtn.setText("Adiciona");
                                addBtn.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        addBtnMouseClicked(e);
                                    }
                                });

                                //---- remBtn ----
                                remBtn.setText("Remove");

                                GroupLayout layout = new GroupLayout(this);
                                setLayout(layout);
                                layout.setHorizontalGroup(
                                    layout.createParallelGroup()
                                            .addGroup(layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addGroup(layout.createParallelGroup()
                                                            .addGroup(layout.createSequentialGroup()
                                                                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)
                                                                    .addContainerGap(8, Short.MAX_VALUE))
                                                            .addGroup(layout.createSequentialGroup()
                                                                    .addComponent(addBtn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                                                                    .addComponent(remBtn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                                                    .addGap(25, 25, 25))
                                                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                    .addComponent(newWord)
                                                                    .addContainerGap())))
                                );
                                layout.setVerticalGroup(
                                    layout.createParallelGroup()
                                            .addGroup(layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 314, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(newWord, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(30, 30, 30)
                                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(addBtn, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(remBtn, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
                                                    .addContainerGap(13, Short.MAX_VALUE))
                                );
                                // JFormDesigner - End of component initialization  //GEN-END:initComponents
                            }
        public static void main (String[]args){
            JFrame janela = new JFrame("");
            configEspiao espiao = new configEspiao();

            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.add(espiao);
            janela.setSize(300, 500);
            janela.setVisible(true);
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // Generated using JFormDesigner Evaluation license - asdasdas asdadasdsa
        private JScrollPane scrollPane1;
        private JTextArea listOfWords;
        private JButton addBtn;
        private JButton remBtn;
        private JTextField newWord;
        // JFormDesigner - End of variables declaration  //GEN-END:variables
                }
