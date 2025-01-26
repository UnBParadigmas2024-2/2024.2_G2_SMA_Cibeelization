package com.bee;

import java.util.Random;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.core.AID;
// import jade.wrapper.AgentController;

// import java.lang.Thread;
// import jade.wrapper.ContainerController;
// import jade.wrapper.ControllerException;
// import java.util.ArrayList;
// import java.util.List;

public class QueenBee extends Agent {
    // Variaveis estaticas para manter o estado global da colmeia.
    public static int InspectorBeeNumber = 0;          // Numero de abelhas operarias.
    public static int WorkerBeeNumber = 0;          // Numero de abelhas operarias.
    public static int janitorBeenumber = 0;  // Numero de abelhas limpadoras.
    public static int droneBeenumber = 0;    // Numero de zangoes.
    public static int queenBeeNumber = 0;    // Numero de rainhas vivas (maximo de 1 por padrao).
    public static int intruderBearNumber = 0;  // Numero de ursos intrusos detectados.
    public static int intruderBearId = 1;     // ID sequencial para os ursos intrusos.
    public static int InspectorBeeId = 1;        // ID sequencial para rainhas.
    public static int queenBeeId = 1;        // ID sequencial para rainhas.
    public static int WorkerBeeId = 1;       // ID sequencial para operarias.
    public static int droneBeeId = 1;        // ID sequencial para zangoes.
    public static int janitorBeeId = 1;      // ID sequencial para limpadoras.

    public static int ordemAcasalamento = 1; // ID do proximo zangao chamado para acasalamento.
    private final Random random = new Random(); // Objeto para gerar numeros aleatorios.



    @Override
    protected void setup() {
        // System.out.println("A abelha rainha " + getLocalName() + " esta pronta!");
        WorkerBee.activeBees.add(this);
        // System.out.println(getLocalName() + " foi adicionado aa lista de agentes ativos.");

        // Registro no Diretorio de Facilitadores (DF).
        registerInDF();

        // Regra antiga: A rainha espera 1 segundo antes de iniciar comportamentos adicionais.
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                doWait(1000); // Pequena pausa inicial.
            }
        });

        // Regra nova: Executa o voo nupcial a cada 5 segundos, desde que exista uma unica rainha.
        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                if (queenBeeNumber == 1) {
                    voonupcial();
                }
            }
        });

        // Regra nova: Remove a rainha por "velhice" apos 30 segundos de vida.
        addBehaviour(new TickerBehaviour(this, 30000) {
            @Override
            protected void onTick() {
                queenBeeNumber--;
                // System.out.println("Morreu de velhice " + getLocalName());
                doDelete();
            }
        });

        // Regra nova: Simula "rinha" entre rainhas caso existam multiplas rainhas.
        // ?????????????????????? como checa se tem mais de uma rainha?
        addBehaviour(new TickerBehaviour(this, 50) {
            @Override
            protected void onTick() {
                rinha();
            }
        });

        // Regra nova: Detecta intrusos (ursos) na colmeia a cada 3 segundos.
        // ========
        // = novo =
        // ========
        addBehaviour(new TickerBehaviour(this, 3000) {
            @Override
            protected void onTick() {
                detectIntruders();
            }
        });
        // ========
    }

    // Regra nova: Simula uma batalha entre rainhas ("rinha").
    private synchronized void rinha() {
        if (queenBeeNumber > 1) { // So ocorre se houver mais de uma rainha.
            if (random.nextDouble() < 0.5) { // 50% de chance de uma rainha morrer.
                // System.out.println("Morreu rainha " + getLocalName() + " na rinha");
                queenBeeNumber--;
                doDelete(); // Remove a rainha derrotada.
            }
        }
    }

    // Regra antiga: Chama um zangao especifico para acasalamento.
    private void chamaZangao() {
        String zangao = "Zangao" + ordemAcasalamento;
        try {
            // System.out.println("Chamou " + zangao + " para voo nupcial");
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("Venha");
            msg.addReceiver(new AID(zangao, AID.ISLOCALNAME));
            send(msg); // Envia mensagem para o zangao.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Regra nova: Chama um zangao para acasalamento ou cria zangoes e abelhas operarias e limpadoras.
    private void voonupcial() {
        if (droneBeeId == ordemAcasalamento) {
            // Cria 4 novos zangoes.
            for (int i = 0; i < 4; i++) {
                createDroneBee();
            }
        } else {
            // Chama um zangao existente e cria novos membros para a colmeia.
            chamaZangao();
            for (int i = 0; i < 20; i++) {
                createWorkerBee(); // Cria 20 operarias.
            }
            for (int i = 0; i < 4; i++) {
                createJanitorBee(); // Cria 4 limpadoras.
            }
        }
    }


    // Criacao de abelhas operarias.
    private void createWorkerBee() {
        try {
            String workerName = "Operaria" + WorkerBeeId++;
            AgentController WorkerBeeAC = getContainerController().createNewAgent(workerName, "com.bee.WorkerBee", null);
            WorkerBeeAC.start();
            QueenBee.WorkerBeeNumber++;
            // System.out.println("A " + getLocalName() + " criou uma nova operaria: " + workerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Criacao de zangoes.
    private void createDroneBee() {
        try {
            String droneName = "Zangao" + droneBeeId++;
            AgentController DroneBeeAC = getContainerController().createNewAgent(droneName, "com.bee.DroneBee", null);
            DroneBeeAC.start();
            QueenBee.droneBeenumber++;
            // System.out.println("A " + getLocalName() + " criou um novo zangao: " + droneName);
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Bem-vindo a colmeia!");
            msg.addReceiver(new AID(droneName, AID.ISLOCALNAME));
            send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Criacao de abelhas limpadoras.
    private void createJanitorBee() {
        try {
            String janitorName = "Limpadora" + janitorBeeId++;
            AgentController JanitorBeeAC = getContainerController().createNewAgent(janitorName, "com.bee.JanitorBee", null);
            JanitorBeeAC.start();
            QueenBee.janitorBeenumber++;
            // System.out.println("A " + getLocalName() + " criou uma nova limpadora: " + janitorName);

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Bem-vindo a colmeia!");
            msg.addReceiver(new AID(janitorName, AID.ISLOCALNAME));
            send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Regra nova: Detecta intrusos (ursos) com 40% de chance de aparecerem.
    private void detectIntruders() {
        if (intruderBearNumber < 3 && random.nextDouble() < 0.4) { 
            // Limite de 3 intrusos ativos.
            // 40% de chance de aparecer um urso
            try {
                String intruderName = "Urso" + intruderBearId++;
                //AgentController IntruderBeeAC = getContainerController().createNewAgent(intruderName, "com.bee.IntruderBear", new Object[]{getLocalName()});
                AgentController IntruderBeeAC = getContainerController().createNewAgent(intruderName, "com.bee.IntruderBear", null);
                IntruderBeeAC.start();
                QueenBee.intruderBearNumber++;

                // System.out.println("Novo intruso detectado: " + intruderName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Registro do agente no DF.
    private void registerInDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("QueenBee-agent");
        sd.setName("QueenBee-service");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    // Desregistro do agente no DF antes de ser finalizado.
    @Override
    protected synchronized void takeDown() {
        WorkerBee.activeBees.remove(this);
        QueenBee.queenBeeNumber--;
        // System.out.println(getLocalName() + " foi removido da lista de agentes ativos.");
        try {
            // System.out.println("======= takeDown ======= A abelha rainha (" + getLocalName() + ") morreu");
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
