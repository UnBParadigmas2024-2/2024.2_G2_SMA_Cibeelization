package com.bee;

import java.util.Random;
import java.lang.Thread;
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
    public static int janitorBeenumber = 0;
    public static int droneBeenumber = 0;
    public static int queenBeeNumber = 1;
    public static int queenBeeId = 1;
    public static int workerBeeId = 1;
    public static int droneBeeId = 1;
    public static int janitorBeeId = 1;
    public static int ordemAcasalamento = 1;
    private final Random random = new Random();
    
    @Override
    protected void setup() {
        System.out.println("A abelha rainha " + getLocalName() + " está pronta!");
        
        registerInDF();
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                doWait(1000);     
            }
        });
        
        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                if(queenBeeNumber == 1)
                    vooNupicial();
            }
        });

        addBehaviour(new TickerBehaviour(this, 30000) {
            @Override
            protected void onTick() {
                queenBeeNumber--;
                System.out.println("Morreu de velhice " + getLocalName());
                doDelete();
            }
        });

        addBehaviour(new TickerBehaviour(this, 50) {
            @Override
            public void onTick() {
                rinha();
            }
        }
        );
    }

    private synchronized void rinha(){
        if(queenBeeNumber > 1){
            if(random.nextDouble() < 0.5){
                System.out.println("Morreu rainha " + getLocalName() + " na rinha");
                queenBeeNumber--;
                doDelete();
            }
        }
    }

    private void chamaZangao(){
        String zangao = "Zangao" + ordemAcasalamento;
        try{
            //System.out.println("Chamou " + zangao + " para voo nupicial");
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("Venha");
            msg.addReceiver(new AID(zangao, AID.ISLOCALNAME));
            send(msg);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void vooNupicial(){
        if(droneBeeId == ordemAcasalamento){
            for(int i = 0; i < 4; i++)
                createDroneBee();
        }
        else{
            chamaZangao();
            for(int i = 0; i < 20; i++)
                createWorkerBee();
            for(int i = 0; i < 4; i++)
                createJanitorBee();
        }
    }

    private void createWorkerBee() {
        try {
            String workerName = "Operária" + workerBeeId++;
            workerBeeNumber++;
            getContainerController().createNewAgent(workerName, "com.bee.WorkerBee", null).start();
            //System.out.println("A " + getLocalName() + " criou uma nova operária: " + workerName);

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
            String droneName = "Zangao" + droneBeeId++;
            droneBeenumber++;
            getContainerController().createNewAgent(droneName, "com.bee.DroneBee", null).start();
            //System.out.println("A " + getLocalName() + " criou um novo zangão: " + droneName);

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Bem-vindo à colmeia!");
            msg.addReceiver(new AID(droneName, AID.ISLOCALNAME));
            send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createJanitorBee() {
        try {
            String janitorName = "Limpadora" + janitorBeeId++;
            janitorBeenumber++;
            getContainerController().createNewAgent(janitorName, "com.bee.JanitorBee", null).start();
            //System.out.println("A " + getLocalName() + " criou uma nova limpadora: " + janitorName);

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Bem-vindo à colmeia!");
            msg.addReceiver(new AID(janitorName, AID.ISLOCALNAME));
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
        //System.out.println("A abelha rainha " + getLocalName() + " irá morrer");
    }
}
