package com.bee;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
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
    static int beeLarvaNumber = 0;
    
    @Override
    protected void setup() {
        System.out.println("A abelha rainha " + getLocalName() + " está pronta!");
        
        registerInDF();
        
        addBehaviour(new TickerBehaviour(this, 3000) {
            @Override
            protected void onTick() {
                createBeeLarva();
            }
        });
    }

    private void createBeeLarva() {
        try {
            String larvaName = "Larva" + beeLarvaNumber++;
            getContainerController().createNewAgent(larvaName, "com.bee.BeeLarva", null).start();
            System.out.println("A abelha rainha criou uma nova larva: " + larvaName);

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Bem-vinda à colmeia!");
            msg.addReceiver(new AID(larvaName, AID.ISLOCALNAME));
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
