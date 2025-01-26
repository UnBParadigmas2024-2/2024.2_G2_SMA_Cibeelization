package com.bee;

import jade.core.Agent;
// import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
// import jade.lang.acl.ACLMessage;
// import jade.wrapper.AgentController;
import jade.wrapper.AgentController;

import javax.swing.*;

public class InspectorBee extends Agent {
    public static int ciclos = 0; // Contador de ciclos para monitoramento.


    @Override
    protected void setup() {
        // Inicializacao do agente.
        WorkerBee.activeBees.add(this);
        // System.out.println(getLocalName() + " foi adicionado aa lista de agentes ativos.");

        // Chama a janela da interface gráfica
        // Cria e mostra a janela
        // new GuiStatusBee();
        // Cria e exibe a janela GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuiStatusBee();
            }
        });
        


        // System.out.println("Criou Abelha da Receita");
        /*// System.out.println("Iniciando a abelha-rainha");
        AgentController queenBee = mainContainer.createNewAgent("QueenBee", QueenBee.class.getName(), null);
        queenBee.start();*/

        // Registro no Diretorio de Facilitadores (DF).
        registerInDF();

        // Regra nova: Criacao da Rainha (QueenBee).
        // A abelha da receita cria o agente "QueenBee" no inicio.
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                try {
                    String queenName = "QueenBee";
                    AgentController QueenBeeAC = getContainerController().createNewAgent(queenName, "com.bee.QueenBee", null);
                    QueenBeeAC.start();
                    QueenBee.queenBeeNumber++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Regra nova: Log periodico dos recursos da colmeia.
        // A cada 5 segundos, imprime o estado dos recursos.
        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                printa_recursos();
            }
        });

        // Regra nova: Cobranca de imposto periodico.
        // A cada 20 segundos, reduz o mel em 70% para simular a cobranca de impostos.
        addBehaviour(new TickerBehaviour(this, 20000) {
            @Override
            protected void onTick() {
                imposto();
            }
        });
    }

    // Regra nova: Simula a cobranca de impostos reduzindo o mel disponivel.
    public void imposto() {
        // System.out.println("A receita bateu na colmeia");
        WorkerBee.quantityOfHoney *= 0.3; // Reduz o mel para 30% do total (70% e removido).
    }

    // Regra nova: Exibe o estado atual da colmeia.
    public static void printa_recursos() {
        System.out.println("\n========== CICLO " + ciclos++ + " ==========");
        System.out.println("Inspetor: " + QueenBee.InspectorBeeNumber);
        System.out.println("Rainhas: " + QueenBee.queenBeeNumber);
        System.out.println("Operarias: " + QueenBee.WorkerBeeNumber);
        System.out.println("Limpadoras: " + QueenBee.janitorBeenumber);
        System.out.println("Zangoes: " + QueenBee.droneBeenumber);
        System.out.println("Polen: " + WorkerBee.quantityOfPollen);
        System.out.println("Mel: " + WorkerBee.quantityOfHoney);
        System.out.println("Geleia Real: " + WorkerBee.quantityOfRoyalJelly);
        // ========
        // = novo =
        // ========
        System.out.println("Residuos: " + JanitorBee.residual);
        System.out.println("Intrusos proximos: " + QueenBee.intruderBearNumber);
        // ========
        System.out.println("==============================\n");
    }

    // Registra o agente no Diretorio de Facilitadores (DF).
    private void registerInDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("InspectorBee-agent");
        sd.setName("InspectorBee-service");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd); // Registro no DF.
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized void takeDown() {
        WorkerBee.activeBees.remove(this);
        QueenBee.InspectorBeeNumber--;
        // System.out.println(getLocalName() + " foi removido da lista de agentes ativos.");
        // Remove o registro do agente no DF antes de encerrar.
        try {
            // System.out.println("======= takeDown ======= A abelha da receita (" + getLocalName() + ") morreu");
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
