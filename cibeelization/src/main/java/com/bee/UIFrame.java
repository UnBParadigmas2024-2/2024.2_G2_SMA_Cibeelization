package com.bee;


import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import examples.party.HostAgent;
import jade.core.behaviours.OneShotBehaviour;
import java.awt.event.*;

public class UIFrame extends JFrame {

    // Constants
    //////////////////////////////////

    // Static variables
    //////////////////////////////////

    // Instance variables
    //////////////////////////////////
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel pnl_main = new JPanel();
    JButton btn_stop = new JButton();
    Component component3;
    JButton btn_start = new JButton();
    Component component2;
    Box box_buttons;
    JPanel pnl_numGoal = new JPanel();
    BorderLayout borderLayout3 = new BorderLayout();
    JLabel lbl_numGoal = new JLabel();
    Box box_numGoal;
    JLabel lbl_goalCount = new JLabel();
    JSlider slide_numGoal = new JSlider();
    Component component1;
    Component component4;
    GridLayout gridLayout1 = new GridLayout();
    JLabel jLabel1 = new JLabel(); //status
    JLabel jLabel2 = new JLabel(); // goal
    JLabel lbl_time = new JLabel();
    JLabel jLabel4 = new JLabel();
    JLabel lbl_hiveState = new JLabel();
    Box box;
    JProgressBar prog_goalCount = new JProgressBar();
    Component component5;
    Component component6;
    
    // JLabel lbl_qtQueen = new JLabel();
    // JLabel lbl_qtWorker = new JLabel();
    // JLabel lbl_qtDrone = new JLabel();
    // JLabel lbl_resultBee = new JLabel();
    // JLabel lbl_qtHoney = new JLabel();
    // JLabel lbl_qtJelly = new JLabel();
    // JPanel pnl_result = new JPanel();
    // Component component7;
    // Component component8;

    protected QueenBee m_queen;

    // Constructors
    //////////////////////////////////
    public UIFrame( QueenBee queen ) {
        try {
            jbInit();
            System.out.println(" try...");
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println(" cath...");
        }

        m_queen = queen;
    }

    // External signature methods
    //////////////////////////////////


    // Internal implementation methods
    //////////////////////////////////

    /**
    * Configure a UI. Este código é gerado pelo designer do JBuilder.
    */
    public void jbInit() throws Exception {

        component3 = Box.createHorizontalStrut(10);
        component2 = Box.createHorizontalStrut(5);
        box_buttons = Box.createHorizontalBox();

        box_numGoal = Box.createHorizontalBox();
        component1 = Box.createGlue();
        component4 = Box.createHorizontalStrut(5);
        box = Box.createVerticalBox();
        component6 = Box.createGlue();
        component5 = Box.createGlue();
        this.getContentPane().setLayout(borderLayout1);
        pnl_main.setLayout(gridLayout1);
        btn_stop.setEnabled(false);
        btn_stop.setText("Stop");
        btn_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btn_stop_actionPerformed(e);
            }
        });
        btn_start.setText("Start");
        btn_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btn_start_actionPerformed(e);
            }
        });
        this.setTitle("Cibeelization");
        pnl_numGoal.setLayout(borderLayout3);
        lbl_numGoal.setText("Production Objective:");
        lbl_goalCount.setMaximumSize(new Dimension(30, 17));
        lbl_goalCount.setMinimumSize(new Dimension(30, 17));
        lbl_goalCount.setPreferredSize(new Dimension(30, 17));
        lbl_goalCount.setText("10");
        slide_numGoal.setValue(10);
        slide_numGoal.setMaximum(100);
        slide_numGoal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                slide_numGoal_stateChanged(e);
            }
        });
        gridLayout1.setRows(2);
        gridLayout1.setColumns(3);
        jLabel1.setToolTipText("");
        jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel1.setText("Status: ");
        jLabel4.setToolTipText("");
        jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel4.setText("Production: ");
        lbl_hiveState.setBackground(Color.white);
        lbl_hiveState.setText("Not started");
        prog_goalCount.setForeground(new Color(0, 255, 128));
        prog_goalCount.setStringPainted(true);
        this.getContentPane().add(pnl_main, BorderLayout.CENTER);
        pnl_main.add(jLabel1, null);
        pnl_main.add(lbl_hiveState, null);
        pnl_main.add(jLabel4, null);
        pnl_main.add(box, null);
        box.add(component5, null);
        box.add(prog_goalCount, null);
        box.add(component6, null);
        this.getContentPane().add(pnl_numGoal, BorderLayout.NORTH);
        pnl_numGoal.add(box_numGoal, BorderLayout.CENTER);
        pnl_numGoal.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) ) );

        box_numGoal.add(lbl_numGoal, null);
        box_numGoal.add(slide_numGoal, null);
        box_numGoal.add(lbl_goalCount, null);
        this.getContentPane().add(box_buttons, BorderLayout.SOUTH);
        box_buttons.add(component3, null);
        box_buttons.add(btn_start, null);
        box_buttons.add(component2, null);
        box_buttons.add(btn_stop, null);
        // box_buttons.add(component1, null);
        // box_buttons.add(btn_Exit, null);
        // box_buttons.add(component4, null);
        lbl_hiveState.setForeground( Color.black );

    }

    /**
     * Quando o controle deslizante para o número de produção muda, atualizamos o rótulo.
     */
    private void slide_numGoal_stateChanged(ChangeEvent e) {
        int goal = slide_numGoal.getValue();
        lbl_goalCount.setText(String.valueOf(goal));
        // System.out.println("Slide...");
    }

    /**
        * Mantém o estado habilitado/desabilitado dos controles principais, dependendo
        * se o sim está em execução ou parado.
     */
    void enableControls( boolean starting ) {
        // btn_start.setEnabled( !starting );
        // btn_stop.setEnabled( starting );
        // slide_numGuests.setEnabled( !starting );
    }

    /**
     * Quando o usuário clicar em parar, diga a rainha para parar a festa.
     */
    void btn_stop_actionPerformed(ActionEvent e) {
        // enableControls( false );

        // add a behavior to the host to end the party
    
    }

    /**
     * Quando o usuário clicar em iniciar, notifique o anfitrião para começar a festa.
     */
    private void btn_start_actionPerformed(ActionEvent e) {
        // enableControls( true );
        // // int goal = slide_numGoal.getValue();
        // // System.out.println("Production objective: " + goal);
        // // if (m_queen != null) {
        // //     m_queen.setGoal(goal);
        // // }
        // int goal = slide_numGoal.getValue();
        // System.out.println("Production objective: " + goal);

        // if (m_queen != null) {
        //     //Define the agent's objective
        //     m_queen.setGoal(goal);

        //     // Adds a OneShot behavior to start production
        //     m_queen.addBehaviour(new OneShotBehaviour() {
        //         public void action() {
        //             System.out.println("Iniciando a produção...");
        //             ((QueenBee) myAgent).makeWorker( );
        //         }
        //     });
        System.out.println("Starting production...");
    }

}
