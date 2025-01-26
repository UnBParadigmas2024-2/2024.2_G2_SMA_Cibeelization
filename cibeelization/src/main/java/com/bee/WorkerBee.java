package com.bee;

import java.util.Random;
import java.lang.Thread;
import java.util.concurrent.*; 
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
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
    private final double chanceOfRoyalJelly = 0.1; 
    private final int minRequiredForHoney = 3;
    private final int minRequiredForRoyalJelly = 3;
    public static int quantityOfPollen = 0;
    public static int quantityOfHoney = 10;
    public static int quantityOfRoyalJelly = 50;
    private int eatenRoyalJelly = 0;
    private int mortePorFome = 0;

    @Override
    protected void setup() {
        //System.out.println("Nova operária nascida! " + getLocalName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if(QueenBee.queenBeeNumber >= 0){
                    ACLMessage msg = myAgent.receive();
            
                    if (msg != null) {
                        processMessage(msg);
                    } else {
                        if(Math.random() < 0.5) {
                            collectPollen();
                        }
                        block();
                    }
                    collectPollen();
                    makeHoneyOrRoyalJelly();
                }
            }
        }
        );

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if(QueenBee.queenBeeNumber == 0 && quantityOfRoyalJelly > 20){
                    eatRoyalJelly();
                    if(eatenRoyalJelly >= 5){
                        newQueen();
                        doDelete();
                    }
                }
            }
        }
        );
        addBehaviour(new TickerBehaviour(this, 7000) {
            @Override
            protected void onTick() {
                eatHoney();
            }
        });
        addBehaviour(new TickerBehaviour(this, 15000) {
            @Override
            protected void onTick() {
                //System.out.println(getLocalName() + " morreu de velhice");
                doDelete();
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getContent().startsWith("KillBee: ")) {
                    String intruderName = msg.getContent().split(": ")[1];
                    System.out.println("Operária " + getLocalName() + " morreu por ataque do intruso " + intruderName + "!");
                    doDelete();
                } else {
                    block();
                }
            }
        });        
    }

    public synchronized void eatHoney(){
        if(quantityOfHoney == 0){
            //System.out.println("Operaria " + getLocalName() + " com fome.");
            mortePorFome++;
            doWait(1000);
        }
        else{
            //System.out.println("Operaria " + getLocalName() + " comendo mel.");
            quantityOfHoney--;
            mortePorFome = 0;
        }

        if(mortePorFome == 3){
            //System.out.println("Operaria " + getLocalName() + " morreu de fome");
            doDelete();
        }
    }

    private synchronized void newQueen(){
        QueenBee.queenBeeNumber++;
        try{
            String queenName = "QueenBee" + QueenBee.queenBeeId++;
            getContainerController().createNewAgent(queenName, "com.bee.QueenBee", null).start();
            System.out.println("A abelha operária virou uma nova rainha: ");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void eatRoyalJelly(){
        //System.out.println("Operaria " + getLocalName() + " comendo geleinha.");
        int amnt = random.nextInt(5);
        if(quantityOfRoyalJelly > 0)
            mortePorFome = 0;
        
        if(amnt > quantityOfRoyalJelly){
            amnt  = quantityOfRoyalJelly;
            this.quantityOfRoyalJelly = 0;
            this.eatenRoyalJelly += amnt;    
        }
        else{
            this.quantityOfRoyalJelly -= amnt;
            this.eatenRoyalJelly += amnt;
        }

        try{
            Thread.sleep(50);
        }
        catch(InterruptedException e) {
            System.err.println("A thread foi interrompida: " + e.getMessage());
            Thread.currentThread().interrupt();                
        }
    }

    private synchronized void collectPollen() {
        //System.out.println("Operária " + getLocalName() + " saiu para coletar pólen");
        doWait(2000);

        if (random.nextDouble() < 0.05) {
            System.out.println("Operária " + getLocalName() + " morreu por conta do inseticida!");

            ACLMessage deathMsg = new ACLMessage(ACLMessage.INFORM);
            deathMsg.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
            deathMsg.setContent("A operária " + getLocalName() + " morreu para o inseticida :(");
            send(deathMsg);

            doDelete();
        } else {
            //System.out.println("A operária " + getLocalName() + " acabou de retornar para a colméia!");
            quantityOfPollen++;

            ACLMessage report = new ACLMessage(ACLMessage.INFORM);
            report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
            report.setContent("Pollen collected");
            send(report);
        }
    }

    private synchronized void makeHoneyOrRoyalJelly() {
        //System.out.println("Operária " + getLocalName() + " está tentando produzir algo...");
        //System.out.println("Polen = " + quantityOfPollen);
        doWait(2000);

        double randomChanceForJelly = random.nextDouble();

        if (quantityOfPollen >= minRequiredForRoyalJelly && randomChanceForJelly <= 0.4) {
            if (JanitorBee.residual < 10) {
                // produzir geleia real
                quantityOfPollen -= minRequiredForRoyalJelly;
                quantityOfRoyalJelly++;
                //System.out.println("Operária " + getLocalName() + " produziu geleia real! Geleias totais: " + quantityOfRoyalJelly);
    
                ACLMessage report = new ACLMessage(ACLMessage.INFORM);
                report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
                report.setContent("Royal jelly produced");
                JanitorBee.residual += 2;
                //System.out.println("gerando + 2 de residuo");
                send(report);
            } else {
                //System.out.println("tem mt residuo pra geleia real");
            }
        } else if (quantityOfPollen >= minRequiredForHoney) {
            if (JanitorBee.residual < 10) {
                // produzir mel
                quantityOfPollen -= minRequiredForHoney;
                quantityOfHoney++;
                //System.out.println("Operária " + getLocalName() + " produziu mel! Mel total: " + quantityOfHoney);
    
                ACLMessage report = new ACLMessage(ACLMessage.INFORM);
                report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
                report.setContent("Honey produced");
                JanitorBee.residual += 1;
                //System.out.println("gerando + 1 de residuo");
                send(report);
            } else {
                //System.out.println("mt residuo pra mel");
            }
        } else {
            //System.out.println("Operária " + getLocalName() + " não conseguiu produzir por falta de pólen.");
        }
    }

    private void processMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.INFORM) {

        } else if (msg.getPerformative() == ACLMessage.REQUEST) {
            handleRequest(msg);
        }
    }

    private void handleRequest(ACLMessage msg) {
        if (msg.getContent().equalsIgnoreCase("Create WorkerBee")) {
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("WorkerBee created successfully.");
            send(reply);
        }
    }

    @Override
    protected synchronized void takeDown() {
        //System.out.println("A operária " + getLocalName() + " irá morrer");
        QueenBee.workerBeeNumber--;
    }
}
