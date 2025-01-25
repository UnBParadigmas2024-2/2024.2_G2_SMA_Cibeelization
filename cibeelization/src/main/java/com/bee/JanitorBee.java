package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class JanitorBee extends Agent {

    public static int residual = 0;
    public boolean firstCicle = true;

    @Override
    protected void setup() {
        System.out.println("Nova limpadora nascida: " + getLocalName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    //System.out.println(getLocalName() + " recebeu uma mensagem: " + msg.getContent());
                    processMessage(msg);
                } else {
                    cleanHive();
                    eat();
                    dieForAge();
                    block();
                }
            }
        });

        addBehaviour(new TickerBehaviour(this, 20000) {
            @Override
            protected void onTick() {
                if (firstCicle) {
                    System.out.println("primeiro ciclo");
                    firstCicle = !firstCicle;
                } else eat();
            }
        });
    }

    protected void dieForAge() {
        addBehaviour(new TickerBehaviour(this, 15000) {
            @Override
            protected void onTick() {
                System.out.println("A limpadora " + getLocalName() + " morreu de velhice");
                doDelete();
            }
        });
    }

    protected synchronized void eat() {
        if(!firstCicle) {
            if (WorkerBee.quantityOfHoney > 0) {
                WorkerBee.quantityOfHoney--;
                System.out.println("A limpadora " + getLocalName() + " consumiu 1 unidade do mel");
            } else {
                System.out.println("A limpadora " + getLocalName() + " morreu de fome");
                doDelete();
            }    
        } else {
            firstCicle = false;
        }
    }

    protected void cleanHive() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (residual > 0) {
                    residual--;
                    System.out.println("Limpando. Resíduo = " + residual);
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
        if (msg.getContent().equalsIgnoreCase("Create JanitorBee")) {
            //System.out.println(getLocalName() + " recebeu um pedido para criação.");
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("JanitorBee created successfully.");
            send(reply);
        } else {
            //System.out.println(getLocalName() + " recebeu um pedido desconhecido: " + msg.getContent());
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("A limpadora " + getLocalName() + " irá morrer");
    }
}
