package com.bee;

import java.util.Random;
import java.lang.Thread;
import java.util.concurrent.*; 
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.lang.acl.ACLMessage;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;


public class WorkerBee extends Agent {

    private final Random random = new Random();
    private final double chanceOfDying = 0.5;
    private final double chanceOfRoyalJelly = 0.95;
    private final int minRequiredforHoney = 2;
    private final int minRequeredForRoyalJelly = 7;
    
    private static int quantityOfPollen = 0;
    private static int quantityOfHoney = 0;
    private static int quantityOfRoyalJelly = 0;
    private int eatenRoyalJelly = 0;
    
    @Override
    protected void setup() {
        System.out.println("Nova operária nascida! " + getLocalName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
        
                if (msg != null) {
                    //System.out.println(getLocalName() + " recebeu uma mensagem: " + msg.getContent());
                    processMessage(msg);
                    // if ("Collect pollen".equalsIgnoreCase(msg.getContent())) {
                    //     collectPollen();
                    // }
                } else {
                    if(Math.random() < 0.5) {
                        collectPollen();
                    }
                    block();
                }
            }
        }
        );

            addBehaviour(new CyclicBehaviour() {
                @Override
                public void action() {
                    if(QueenBee.queenBeeNumber == 0){
                        eatRoyalJelly();
                        if(eatenRoyalJelly >= 100){
                            try{
                                Thread.sleep(500);
                                QueenBee.queenBeeNumber++;
                                try{
                                    String queenName = "QueenBee" + QueenBee.queenBeeId++;
                                    getContainerController().createNewAgent(queenName, "com.bee.QueenBee", null).start();
                                    System.out.println("A abelha operária virou uma nova rainha: ");
                                } 
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                    
                                doDelete();
                            }
                            catch (InterruptedException e) {
                                System.err.println("A thread foi interrompida: " + e.getMessage());
                                Thread.currentThread().interrupt();                
                            }
                        }
                    }
                }
            }
            );
    }

    private void eatRoyalJelly(){
        //System.out.println("Operaria " + getLocalName() + " comendo geleinha.");
        this.eatenRoyalJelly += random.nextInt(10);
        try{
            Thread.sleep(50);
        }
        catch(InterruptedException e) {
            System.err.println("A thread foi interrompida: " + e.getMessage());
            Thread.currentThread().interrupt();                
        }
    }

    private void collectPollen() {
        System.out.println("Operária " + getLocalName() + " saiu para coletar pólen");
        doWait(2000);

        if (random.nextDouble() < -1) {
            System.out.println("Operária " + getLocalName() + " morreu por conta do inseticida!");

            ACLMessage deathMsg = new ACLMessage(ACLMessage.INFORM);
            deathMsg.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
            deathMsg.setContent("A operária " + getLocalName() + " morreu para o inseticida :(");
            send(deathMsg);

            doDelete();
        } else {
            System.out.println("A operária " + getLocalName() + " acabou de retornar para a colméia!");
            quantityOfPollen++;

            ACLMessage report = new ACLMessage(ACLMessage.INFORM);
            report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
            report.setContent("Pollen collected");
            send(report);
        }
    }

    private void makeHoneyOrRoyalJelly() {
        // System.out.println("Operária " + getLocalName() + " irá começar a produzir algo...");
        // doWait(2000);

        // if (random.nextDouble() <= chanceOfRoyalJelly) {
        //     // if(quantityOfPollen >= )
        //     System.out.println("Operária " + getLocalName() + " produziu mais mel!");

        //     ACLMessage makeSuccessMsg = new ACLMessage(ACLMessage.INFORM);
        //     deathMsg.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
        //     deathMsg.setContent("A operária " + getLocalName() + " produziu mais mel!");
        //     send(makeSuccessMsg);
        // } else {
        //     System.out.println("Operária " + getLocalName() + " produziu mais geléia real!");

        //     ACLMessage makeSuccessMsg = new ACLMessage(ACLMessage.INFORM);
        //     deathMsg.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
        //     deathMsg.setContent("A operária " + getLocalName() + " produziu mais geléia real!");
        //     send(makeSuccessMsg);
        // }
    }

    private void processMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.INFORM) {
            //System.out.println(getLocalName() + " recebeu uma mensagem informativa: " + msg.getContent());
        } else if (msg.getPerformative() == ACLMessage.REQUEST) {
            handleRequest(msg);
        }
    }

    private void handleRequest(ACLMessage msg) {
        if (msg.getContent().equalsIgnoreCase("Create WorkerBee")) {
            //System.out.println(getLocalName() + " recebeu um pedido para criação.");
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("WorkerBee created successfully.");
            send(reply);
        } else {
            //System.out.println(getLocalName() + " recebeu um pedido desconhecido: " + msg.getContent());
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("A operária " + getLocalName() + " irá morrer");
    }
}
