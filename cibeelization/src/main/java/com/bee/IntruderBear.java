package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Random;

public class IntruderBear extends Agent {
    private static final Random random = new Random();
    private String queenName; // Nome da abelha rainha para comunicação
    private final HashSet<String> attackedBees = new HashSet<>(); // Controle local de abelhas atacadas

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            queenName = (String) args[0];
        }
        System.out.println("Intruso " + getLocalName() + " apareceu na região!");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                //System.out.println(getLocalName() + " está atacando a colmeia!");
                
                // Tenta roubar mel
                if (WorkerBee.quantityOfHoney > 0) {
                    int honeyStolen = Math.min(3, WorkerBee.quantityOfHoney); // Garante que não fique negativo
                    WorkerBee.quantityOfHoney -= honeyStolen;
                    JanitorBee.residual += 10; // Aumenta resíduos
                    //System.out.println("Intruso " + getLocalName() + " roubou " + honeyStolen + " unidades de mel, causando estragos!");
                } else {
                    //System.out.println("Intruso " + getLocalName() + " não roubou mel, mas causou danos!");
                    JanitorBee.residual += 5; // Já que não tinha mel aumenta menos os resíduos
                }

                // Notifica a rainha sobre o ataque
                notifyQueen();

                // Chance de matar zangões ou operárias
                causeBeeDeaths();

                // Chance de ser repelido por zangões
                if (random.nextDouble() < 0.2) {
                    //System.out.println(getLocalName() + " foi atacado pelos zangões e fugiu!");
                    doDelete(); // Urso vai embora
                } else {
                    //System.out.println(getLocalName() + " foi afastado, mas pode voltar.");
                    doWait(3000); // Simula o tempo de espera antes de tentar atacar novamente (3 ciclos)
                }
            }
        });
    }

    private void notifyQueen() {
        if (queenName != null) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("IntruderDetected");
            msg.addReceiver(new jade.core.AID(queenName, jade.core.AID.ISLOCALNAME));
            send(msg);
        }
    }

    private void causeBeeDeaths() {
        if (QueenBee.workerBeeNumber > 0) {
            String workerName = "Operária" + (QueenBee.workerBeeId - QueenBee.workerBeeNumber);
            if (!attackedBees.contains(workerName) && isAgentAlive(workerName) && random.nextDouble() < 0.3) {
                // Marca como atacada
                attackedBees.add(workerName);

                // Envia mensagem para eliminar a operária
                ACLMessage killMessage = new ACLMessage(ACLMessage.REQUEST);
                killMessage.setContent("KillBee: " + getLocalName());
                killMessage.addReceiver(new jade.core.AID(workerName, jade.core.AID.ISLOCALNAME));
                send(killMessage);

                //System.out.println("Operária " + workerName + " morreu por ataque do intruso " + getLocalName() + "!");
            }
        } else if (QueenBee.droneBeenumber > 0) {
            String droneName = "Zangao" + (QueenBee.droneBeeId - QueenBee.droneBeenumber);
            if (!attackedBees.contains(droneName) && isAgentAlive(droneName) && random.nextDouble() < 0.1) {
                // Marca como atacado
                attackedBees.add(droneName);

                // Envia mensagem para eliminar o zangão
                ACLMessage killMessage = new ACLMessage(ACLMessage.REQUEST);
                killMessage.setContent("KillBee: " + getLocalName());
                killMessage.addReceiver(new jade.core.AID(droneName, jade.core.AID.ISLOCALNAME));
                send(killMessage);

                //System.out.println("Zangão " + droneName + " morreu por ataque do intruso " + getLocalName() + "!");
            }
        } else {
            System.out.println(getLocalName() + " não encontrou zangões ou operárias para atacar!");
        }
    }

    // Método para verificar se o agente ainda está vivo
    private boolean isAgentAlive(String agentName) {
        try {
            getContainerController().getAgent(agentName);
            return true; // O agente ainda existe
        } catch (Exception e) {
            return false; // O agente não existe mais
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("Intruso " + getLocalName() + " fugiu da região.");
        QueenBee.intruderBearNumber--;
    }
}
