package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class IntruderBear extends Agent {
    @Override
    protected void setup() {
        System.out.println("Um novo intruso apareceu: " + getLocalName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(getLocalName() + " recebeu uma mensagem: " + msg.getContent());
                    if (msg.getContent().equalsIgnoreCase("DefenderColmeia")) {
                        retaliate();
                    }
                } else {
                    // Simula a aparição do urso na colmeia
                    System.out.println(getLocalName() + " está se aproximando da colmeia...");
                    doWait(5000); // Aguarda antes de agir novamente
                }
                block();
            }
        });
    }

    private void retaliate() {
        System.out.println(getLocalName() + " está atacando as operárias!");
        doWait(2000); // Simula o tempo de ataque
        System.out.println(getLocalName() + " foi repelido!");
    }

    @Override
    protected void takeDown() {
        System.out.println("O intruso " + getLocalName() + " foi eliminado.");
    }
}