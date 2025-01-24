package com.bee;

import java.util.Random;
import java.lang.Thread;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.util.ArrayList;
import java.util.List;

public class QueenBee extends Agent {
    static int workerBeeNumber = 0;
    public static int queenBeeNumber = 1;
    public static int queenBeeId = 1;
    public static int workerBeeId = 1;
    public static int droneBeeId = 1;
    private final Random random = new Random();
    
    @Override
    protected void setup() {
        System.out.println("A abelha rainha " + getLocalName() + " está pronta!");
        
        registerInDF();
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                try{
                    Thread.sleep(1000);
                }
                catch(InterruptedException e){
                    System.err.println("A thread foi interrompida: " + e.getMessage());
                    Thread.currentThread().interrupt();                
                }     
                if(queenBeeNumber == 1)
                    createDroneBee();
            }
        });
        
        addBehaviour(new TickerBehaviour(this, 3000) {
            @Override
            protected void onTick() {
                if(queenBeeNumber == 1)
                    createWorkerBee();
            }
        });

        addBehaviour(new TickerBehaviour(this, 10000) {
            @Override
            protected void onTick() {
                queenBeeNumber--;
                System.out.println("Morreu de velhice");
                doDelete();
            }
        });

        addBehaviour(new TickerBehaviour(this, 100) {
            @Override
            public void onTick() {
                if(queenBeeNumber > 1){
                    if(random.nextDouble() < 0.5){
                        System.out.println("Morreu rainha " + getLocalName() + " na rinha");
                        queenBeeNumber--;
                        doDelete();
                    }
                }
            }
        }
        );

    }

    private void createWorkerBee() {
        try {
            String workerName = "Operária" + workerBeeId++;
            workerBeeNumber++;
            getContainerController().createNewAgent(workerName, "com.bee.WorkerBee", null).start();
            System.out.println("A " + getLocalName() + " criou uma nova operária: " + workerName);

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Bem-vinda à colmeia!");
            msg.addReceiver(new AID(workerName, AID.ISLOCALNAME));
            send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDroneBee() {
        try {
            String droneName = "Zangão" + droneBeeId++;
            getContainerController().createNewAgent(droneName, "com.bee.DroneBee", null).start();
            System.out.println("A " + getLocalName() + " criou um novo zangão: " + droneName);

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Bem-vindo à colmeia!");
            msg.addReceiver(new AID(droneName, AID.ISLOCALNAME));
            send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        //System.out.println("A abelha rainha " + getLocalName() + " irá morrer");
    }
}
