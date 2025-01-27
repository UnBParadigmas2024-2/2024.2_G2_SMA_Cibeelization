package com.bee;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GuiStatusBee extends JFrame {

    // Variáveis de instância para os labels
    private JLabel lblInspectorBeeNumberValue;
    private JLabel lblWorkerBeeNumberValue;
    private JLabel lblJanitorBeenumberValue;
    private JLabel lblDroneBeenumberValue;
    private JLabel lblQueenBeeNumberValue;
    private JLabel lblIntruderBearNumberValue;
    private JLabel lblQuantityOfPollenValue;
    private JLabel lblQuantityOfHoneyValue;
    private JLabel lblQuantityOfRoyalJellyValue;
    private JTextArea txtStatus;

    // Criação da janela principal
    public GuiStatusBee() {

        // Criação da janela principal
        // JFrame frame = new JFrame("Bee Colony Status - Ciclo " + InspectorBee.ciclos);
        JFrame frame = new JFrame("Status da Cibeelization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 20, 20));

        // Labels para exibir as variáveis
        JLabel lblInspectorBeeNumber = new JLabel("Inspetora:");
        lblInspectorBeeNumberValue = new JLabel(String.valueOf(QueenBee.InspectorBeeNumber));
        panel.add(lblInspectorBeeNumber);
        panel.add(lblInspectorBeeNumberValue);

        JLabel lblWorkerBeeNumber = new JLabel("Operárias:");
        lblWorkerBeeNumberValue = new JLabel(String.valueOf(QueenBee.WorkerBeeNumber));
        panel.add(lblWorkerBeeNumber);
        panel.add(lblWorkerBeeNumberValue);

        JLabel lblJanitorBeenumber = new JLabel("Limpadoras:");
        lblJanitorBeenumberValue = new JLabel(String.valueOf(QueenBee.janitorBeenumber));
        panel.add(lblJanitorBeenumber);
        panel.add(lblJanitorBeenumberValue);

        JLabel lblDroneBeenumber = new JLabel("Zangões:");
        lblDroneBeenumberValue = new JLabel(String.valueOf(QueenBee.droneBeenumber));
        panel.add(lblDroneBeenumber);
        panel.add(lblDroneBeenumberValue);

        JLabel lblQueenBeeNumber = new JLabel("Rainhas:");
        lblQueenBeeNumberValue = new JLabel(String.valueOf(QueenBee.queenBeeNumber));
        panel.add(lblQueenBeeNumber);
        panel.add(lblQueenBeeNumberValue);

        JLabel lblIntruderBearNumber = new JLabel("Ursos Intrusos:");
        lblIntruderBearNumberValue = new JLabel(String.valueOf(QueenBee.intruderBearNumber));
        panel.add(lblIntruderBearNumber);
        panel.add(lblIntruderBearNumberValue);

        JLabel lblQuantityOfPollen = new JLabel("Pólen:");
        lblQuantityOfPollenValue = new JLabel(String.valueOf(WorkerBee.quantityOfPollen));
        panel.add(lblQuantityOfPollen);
        panel.add(lblQuantityOfPollenValue);

        JLabel lblQuantityOfHoney = new JLabel("Mel:");
        lblQuantityOfHoneyValue = new JLabel(String.valueOf(WorkerBee.quantityOfHoney));
        panel.add(lblQuantityOfHoney);
        panel.add(lblQuantityOfHoneyValue);

        JLabel lblQuantityOfRoyalJelly = new JLabel("Geleia Real:");
        lblQuantityOfRoyalJellyValue = new JLabel(String.valueOf(WorkerBee.quantityOfRoyalJelly));
        panel.add(lblQuantityOfRoyalJelly);
        panel.add(lblQuantityOfRoyalJellyValue);

        // Adicionando o JTextArea para o campo de status
        JLabel lblStatus = new JLabel("Status:");
        txtStatus = new JTextArea(10, 40);
        txtStatus.setEditable(false);  // O campo não pode ser editado pelo usuário
        txtStatus.setLineWrap(true);   // Quebra de linha automática
        JScrollPane scrollPane = new JScrollPane(txtStatus);  // Para permitir rolar o texto
        panel.add(lblStatus);
        panel.add(scrollPane);

        // Adiciona o painel à janela
        frame.add(panel);
        frame.setVisible(true);

        // Atualiza as variáveis e exibe os valores a cada 5 segundos
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Atualizando os valores exibidos nas labels
                lblInspectorBeeNumberValue.setText(String.valueOf(QueenBee.InspectorBeeNumber));
                lblWorkerBeeNumberValue.setText(String.valueOf(QueenBee.WorkerBeeNumber));
                lblJanitorBeenumberValue.setText(String.valueOf(QueenBee.janitorBeenumber));
                lblDroneBeenumberValue.setText(String.valueOf(QueenBee.droneBeenumber));
                lblQueenBeeNumberValue.setText(String.valueOf(QueenBee.queenBeeNumber));
                lblIntruderBearNumberValue.setText(String.valueOf(QueenBee.intruderBearNumber));
                lblQuantityOfPollenValue.setText(String.valueOf(WorkerBee.quantityOfPollen));
                lblQuantityOfHoneyValue.setText(String.valueOf(WorkerBee.quantityOfHoney));
                lblQuantityOfRoyalJellyValue.setText(String.valueOf(WorkerBee.quantityOfRoyalJelly));

                // Exibindo a mensagem de status na JTextArea
                txtStatus.append("Updated bee statuses!\n");
                txtStatus.setCaretPosition(txtStatus.getDocument().getLength());  // Rolando até o final

            }
        });

        // Inicia o timer
        timer.start();
    }



    public static void main(String[] args) {
        // Cria e exibe a janela GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuiStatusBee();
            }
        });
    }
}
