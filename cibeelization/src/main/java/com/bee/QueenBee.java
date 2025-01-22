package com.bee;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.util.ArrayList;
import java.util.List;

public class QueenBee extends Agent {
    static int workerBeeNumber = 0;
    
    @Override
    protected void setup() {
        System.out.println("A abelha rainha " + getLocalName() + " está pronta!");
        
        registerInDF();
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                createDroneBee();
            }
        });
        
        addBehaviour(new TickerBehaviour(this, 3000) {
            @Override
            protected void onTick() {
                createWorkerBee();
            }
        });
    }

    private void createWorkerBee() {
        try {
            String workerName = "Operária" + workerBeeNumber++;
            getContainerController().createNewAgent(workerName, "com.bee.WorkerBee", null).start();
            System.out.println("A abelha rainha criou uma nova operária: " + workerName);

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Bem-vinda à colmeia!");
            msg.addReceiver(new AID(workerName, AID.ISLOCALNAME));
            send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDroneBee() {
        try {
            String droneName = "Zangão";
            getContainerController().createNewAgent(droneName, "com.bee.DroneBee", null).start();
            System.out.println("A abelha rainha criou um novo zangão: " + droneName);

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Bem-vindo à colmeia!");
            msg.addReceiver(new AID(droneName, AID.ISLOCALNAME));
            send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerInDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("QueenBee-agent");
        sd.setName("QueenBee-service");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        System.out.println("A abelha rainha " + getLocalName() + " irá morrer");
    }
}
