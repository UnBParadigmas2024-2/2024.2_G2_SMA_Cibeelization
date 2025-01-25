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
    private static int quantityOfPollen = 0;
    public static int quantityOfHoney = 50;
    private static int quantityOfRoyalJelly = 50;
    private int eatenRoyalJelly = 0;
    private int mortePorFome = 0;

    @Override
    protected void setup() {
        System.out.println("[" + getLocalName() + "] nasceu");

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
                        try{
                            Thread.sleep(500);
                            newQueen();
                
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
        addBehaviour(new TickerBehaviour(this, 7000) {
            @Override
            protected void onTick() {
                eatHoney();
            }
        });
        addBehaviour(new TickerBehaviour(this, 15000) {
            @Override
            protected void onTick() {
                System.out.println(getLocalName() + " morreu de velhice");
                doDelete();
            }
        });
    }

    public synchronized void eatHoney(){
        if(quantityOfHoney == 0){
            System.out.println("[" + getLocalName() + "] está com fome");
            mortePorFome++;
            doWait(1000);
        }
        else{
            System.out.println("[" + getLocalName() + "] comendo mel.");
            quantityOfHoney--;
            mortePorFome = 0;
        }

        if(mortePorFome == 3){
            System.out.println("[" + getLocalName() + "] morreu de fome");
            doDelete();
        }
    }

    public synchronized void newQueen(){
        QueenBee.queenBeeNumber++;
        try{
            String queenName = "QueenBee" + QueenBee.queenBeeId++;
            getContainerController().createNewAgent(queenName, "com.bee.QueenBee", null).start();
            System.out.println("[" + getLocalName() + "] virou a nova rainha");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void eatRoyalJelly(){
        System.out.println("[" + getLocalName() + "] comendo geleia.");
        int amnt = random.nextInt(10);
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

    private void collectPollen() {
        System.out.println("[" + getLocalName() + "] saiu para coletar pólen");
        doWait(2000);

        if (random.nextDouble() < -1) {
            System.out.println("[" + getLocalName() + "] morreu pelo inseticida!");

            ACLMessage deathMsg = new ACLMessage(ACLMessage.INFORM);
            deathMsg.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
            deathMsg.setContent("A operária " + getLocalName() + " morreu para o inseticida :(");
            send(deathMsg);

            doDelete();
        } else {
            System.out.println("[" + getLocalName() + "] retornou para a colméia");
            quantityOfPollen++;

            ACLMessage report = new ACLMessage(ACLMessage.INFORM);
            report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
            report.setContent("Pollen collected");
            send(report);
        }
    }

    private synchronized void makeHoneyOrRoyalJelly() {
        System.out.println("[" + getLocalName() + "] produzindo algo");
        doWait(2000);

        double randomChanceForJelly = random.nextDouble();

        if (quantityOfPollen >= minRequiredForRoyalJelly && randomChanceForJelly <= 0.4) {
            if (JanitorBee.residual < 10) {
                // produzir geleia real
                quantityOfPollen -= minRequiredForRoyalJelly;
                quantityOfRoyalJelly++;
                System.out.println("[" + getLocalName() + "] produziu geleia real");
    
                ACLMessage report = new ACLMessage(ACLMessage.INFORM);
                report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
                report.setContent("Royal jelly produced");
                JanitorBee.residual += 2;
                send(report);
            } else {
                System.out.println("[Colmeia] Muito resíduo para a produção de geleia real");
            }
        } else if (quantityOfPollen >= minRequiredForHoney) {
            if (JanitorBee.residual < 10) {
                // produzir mel
                quantityOfPollen -= minRequiredForHoney;
                quantityOfHoney++;
                System.out.println("[" + getLocalName() + "] produziu mel");
    
                ACLMessage report = new ACLMessage(ACLMessage.INFORM);
                report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
                report.setContent("Honey produced");
                JanitorBee.residual += 1;
                send(report);
            } else {
                System.out.println("[Colmeia] Muito resíduo para a produção de mel");
            }
        } else {
            System.out.println("[" + getLocalName() + "] não produziu por falta de pólen");
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
    protected void takeDown() {
        System.out.println("[" + getLocalName() + "] morreu");
    }
}
