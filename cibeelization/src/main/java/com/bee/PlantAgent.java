package com.bee;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import java.util.Random;

public class PlantAgent extends Agent{
    private boolean hasPollen;

    @Override
    protected void setup() {
        System.out.println("A planta " + getLocalName() + " está pronta");
    
        registerInDF();
        generatePollen();
    }

    private void registerInDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Plant-agent");
        sd.setName("Plant-service");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private void generatePollen() {
        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                hasPollen = new Random().nextBoolean();
            }
        });
    }

    public boolean collectPollen() {
        if (hasPollen) {
            hasPollen = false;
            return true;
        }
        return false;
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch(FIPAException e) {
            e.printStackTrace();
        }
        System.out.println("A planta " + getLocalName() + " foi removida");
    }
}
