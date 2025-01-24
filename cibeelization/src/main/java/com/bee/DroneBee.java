package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class DroneBee extends Agent {
    @Override
    protected void setup() {
        System.out.println("Novo zangão nascido! " + getLocalName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    //System.out.println(getLocalName() + " recebeu uma mensagem: " + msg.getContent());
                    processMessage(msg);
                } else {
                    block();
                }
            }
        });
    }

    private void processMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.INFORM) {
            //System.out.println(getLocalName() + " recebeu uma mensagem informativa: " + msg.getContent());
        } else if (msg.getPerformative() == ACLMessage.REQUEST) {
            handleRequest(msg);
        }
    }

    private void handleRequest(ACLMessage msg) {
        if (msg.getContent().equalsIgnoreCase("Create DroneBee")) {
            //System.out.println(getLocalName() + " recebeu um pedido para criação.");
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("DroneBee created successfully.");
            send(reply);
        } else {
            //System.out.println(getLocalName() + " recebeu um pedido desconhecido: " + msg.getContent());
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("O zangão " + getLocalName() + " irá morrer");
    }
}
