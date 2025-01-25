package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class DroneBee extends Agent {
    
    @Override
    protected void setup() {
        System.out.println("[" + getLocalName() + "] nasceu");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    processMessage(msg);
                } 
                else {
                    block();
                }
            }
        });

        addBehaviour(new TickerBehaviour(this, 25000) {
            @Override
            protected void onTick() {
                System.out.println("[" + getLocalName() + "] morreu de velhice");
                doDelete();
            }
        });
    }

    private void processMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.INFORM) {
        } 
        else if (msg.getPerformative() == ACLMessage.REQUEST) {
            handleRequest(msg);
        }
    }

    private void handleRequest(ACLMessage msg) {
        if (msg.getContent().equalsIgnoreCase("Create DroneBee")) {
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("DroneBee created successfully.");
            send(reply);
        } 
        if(msg.getContent().equalsIgnoreCase("Venha")){
            System.out.println("[" + getLocalName() + "] foi para voo nupcial");
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("Bora");
            send(reply);
            doDelete();
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("[" + getLocalName() + "] morreu");
        QueenBee.ordemAcasalamento++;
    }
}
