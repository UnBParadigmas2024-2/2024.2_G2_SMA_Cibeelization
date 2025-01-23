package com.bee;

import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class WorkerBee extends Agent {

    private final Random random = new Random();
    private final double chanceOfDying = 0.5;
    private final double chanceOfRoyalJelly = 0.1; 
    private final int minRequiredForHoney = 2;
    private final int minRequiredForRoyalJelly = 7;

    private static int quantityOfPollen = 0;
    private static int quantityOfHoney = 0;
    private static int quantityOfRoyalJelly = 0;

    @Override
    protected void setup() {
        System.out.println("Nova operária nascida! " + getLocalName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();

                if (msg != null) {
                    System.out.println(getLocalName() + " recebeu uma mensagem: " + msg.getContent());
                    processMessage(msg);
                } else {
                    if (Math.random() < 0.5) {
                        collectPollen();
                    }

                    // Tentar produzir algo se houver pólen suficiente
                    if (quantityOfPollen >= minRequiredForHoney) {
                        makeHoneyOrRoyalJelly();
                    }

                    block();
                }
            }
        });
    }

    private void collectPollen() {
        System.out.println("Operária " + getLocalName() + " saiu para coletar pólen");
        doWait(2000);

        if (random.nextDouble() < chanceOfDying) {
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
        System.out.println("Operária " + getLocalName() + " está tentando produzir algo...");
        doWait(2000);
        if (quantityOfPollen >= minRequiredForRoyalJelly && random.nextDouble() <= chanceOfRoyalJelly) {
            // produzir geleia real
            quantityOfPollen -= minRequiredForRoyalJelly;
            quantityOfRoyalJelly++;
            System.out.println("Operária " + getLocalName() + " produziu geleia real! Geleias totais: " + quantityOfRoyalJelly);

            ACLMessage report = new ACLMessage(ACLMessage.INFORM);
            report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
            report.setContent("Royal jelly produced");
            send(report);
        } else if (quantityOfPollen >= minRequiredForHoney) {
            // produzir mel
            quantityOfPollen -= minRequiredForHoney;
            quantityOfHoney++;
            System.out.println("Operária " + getLocalName() + " produziu mel! Mel total: " + quantityOfHoney);

            ACLMessage report = new ACLMessage(ACLMessage.INFORM);
            report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
            report.setContent("Honey produced");
            send(report);
        } else {
            System.out.println("Operária " + getLocalName() + " não conseguiu produzir por falta de pólen.");
        }
    }

    private void processMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.INFORM) {
            System.out.println(getLocalName() + " recebeu uma mensagem informativa: " + msg.getContent());
        } else if (msg.getPerformative() == ACLMessage.REQUEST) {
            handleRequest(msg);
        }
    }

    private void handleRequest(ACLMessage msg) {
        if (msg.getContent().equalsIgnoreCase("Create WorkerBee")) {
            System.out.println(getLocalName() + " recebeu um pedido para criação.");
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("WorkerBee created successfully.");
            send(reply);
        } else {
            System.out.println(getLocalName() + " recebeu um pedido desconhecido: " + msg.getContent());
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("A operária " + getLocalName() + " irá morrer");
    }
}
