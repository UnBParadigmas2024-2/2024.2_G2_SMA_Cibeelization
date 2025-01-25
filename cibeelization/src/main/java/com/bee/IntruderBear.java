package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class IntruderBear extends Agent {
    private static final Random random = new Random();
    private static final double chanceToLearn = 0.3; // 30% de chance de não voltar
    private String spawnerName;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            spawnerName = (String) args[0];
        }
        System.out.println("Um novo intruso apareceu: " + getLocalName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                System.out.println(getLocalName() + " está tentando roubar mel...");
                if (WorkerBee.quantityOfHoney >= 10) {
                    WorkerBee.quantityOfHoney -= 10;
                    System.out.println(getLocalName() + " roubou 10 unidades de mel!");
                } else {
                    System.out.println(getLocalName() + " ficou irritado por não encontrar mel suficiente e causou danos!");
                    JanitorBee.residual += 5;
                }

                if (random.nextDouble() < chanceToLearn) {
                    System.out.println(getLocalName() + " aprendeu a lição e não voltará!");
                    informSpawner();
                    doDelete(); // Intruder não voltará
                } else {
                    System.out.println(getLocalName() + " foi afastado, mas pode voltar.");
                }
                block();
            }
        });
    }

    private void informSpawner() {
        if (spawnerName != null) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("IntruderGone");
            msg.addReceiver(new jade.core.AID(spawnerName, jade.core.AID.ISLOCALNAME));
            send(msg);
        }
    }

    @Override
    protected void takeDown() {
        informSpawner();
        System.out.println("O intruso " + getLocalName() + " foi eliminado ou se retirou.");
    }
}
