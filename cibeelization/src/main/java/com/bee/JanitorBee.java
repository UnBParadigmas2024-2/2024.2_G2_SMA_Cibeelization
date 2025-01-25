package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class JanitorBee extends Agent {

    public static int residual = 0;
    private int mortePorFome = 0;

    @Override
    protected void setup() {
        //System.out.println("Nova limpadora nascida: " + getLocalName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    processMessage(msg);
                } else {
                    cleanHive();
                }
            }
        });

        addBehaviour(new TickerBehaviour(this, 15000) {
            @Override
            protected void onTick() {
                eat();
            }
        });

        addBehaviour(new TickerBehaviour(this, 15000) {
            @Override
            protected void onTick() {
                //System.out.println("A limpadora " + getLocalName() + " morreu de velhice");
                doDelete();
            }
        });
    }

    protected synchronized void eat() {
        if (WorkerBee.quantityOfHoney > 0) {
            WorkerBee.quantityOfHoney--;
            //System.out.println("A limpadora " + getLocalName() + " consumiu 1 unidade do mel");
            mortePorFome = 0;
        } 
        else {
            //System.out.println("A limpadora " + getLocalName() + " passou de fome");
            mortePorFome++;
            doWait(1000);
        }
        if(mortePorFome == 2){
            doDelete(); 
        }
    }

    protected void cleanHive() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (residual > 0) {
                    residual--;
                    //System.out.println("Limpando. Resíduo = " + residual);
                }
            }
        });
    }

    private void processMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.INFORM) {
           
        } else if (msg.getPerformative() == ACLMessage.REQUEST) {
            handleRequest(msg);
        }
    }

    private void handleRequest(ACLMessage msg) {
        if (msg.getContent().equalsIgnoreCase("Create JanitorBee")) {
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("JanitorBee created successfully.");
            send(reply);
        }
    }

    @Override
    protected synchronized void takeDown() {
        //System.out.println("A limpadora " + getLocalName() + " irá morrer");
        QueenBee.janitorBeenumber--;
    }
}
