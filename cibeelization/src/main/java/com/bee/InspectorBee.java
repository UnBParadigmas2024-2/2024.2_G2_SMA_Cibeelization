package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;


public class InspectorBee extends Agent{
    private int ciclos = 0;
    
    @Override
    protected void setup() {
        System.out.println("Criou Abelha da Receita");
        /*System.out.println("Iniciando a abelha-rainha");
        AgentController queenBee = mainContainer.createNewAgent("QueenBee", QueenBee.class.getName(), null);
        queenBee.start();*/
        registerInDF();
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {     
                try{
                    String queenName = "QueenBee";
                    getContainerController().createNewAgent(queenName, "com.bee.QueenBee", null).start();
                } 
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                printa_recursos();
            }
        });

        addBehaviour(new TickerBehaviour(this, 20000) {
            @Override
            protected void onTick() {
                imposto();
            }
        });


    }

    public void imposto(){
        System.out.println("A receita bateu na colmeia");
        WorkerBee.quantityOfHoney *= 0.3;
    }

    public void printa_recursos(){
        System.out.println("\n========== CICLO " + ciclos++ + " ==========");
        System.out.println("Operarias: " + QueenBee.workerBeeNumber);
        System.out.println("Limpadoras: " + QueenBee.janitorBeenumber);
        System.out.println("Zangões: " + QueenBee.droneBeenumber);
        System.out.println("Polen: " + WorkerBee.quantityOfPollen);
        System.out.println("Mel: " + WorkerBee.quantityOfHoney);
        System.out.println("Geleia Real: " + WorkerBee.quantityOfRoyalJelly);
        System.out.println("==============================\n");
    }

    private void registerInDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("InspectorBee-agent");
        sd.setName("InspectorBee-service");
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
        System.out.println("A abelha da receita irá morrer");
    }
}
