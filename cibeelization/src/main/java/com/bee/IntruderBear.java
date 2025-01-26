package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Random;


public class IntruderBear extends Agent {
    private static final Random random = new Random();
    private String queenName; // Nome da abelha rainha para comunicacao
    private final HashSet<String> attackedBees = new HashSet<>(); // Controle local de abelhas atacadas para evitar ataques repetidos


    @Override
    protected void setup() {
        WorkerBee.activeBees.add(this);
        // System.out.println(getLocalName() + " foi adicionado aa lista de agentes ativos.");

        // Inicializacao do agente intruso
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            queenName = (String) args[0]; // Obtem o nome da rainha a partir dos argumentos
        }
        // System.out.println("Intruso " + getLocalName() + " apareceu na regiao!");

        // Comportamento ciclico para simular os ataques do intruso
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                // System.out.println(getLocalName() + " esta atacando a colmeia!");
                // O intruso tenta roubar mel e causar estragos
                if (WorkerBee.quantityOfHoney > 0) {
                    int honeyStolen = Math.min(3, WorkerBee.quantityOfHoney); 
                    // Garante que nao fique negativo
                    WorkerBee.quantityOfHoney -= honeyStolen;
                    JanitorBee.residual += 10; // Aumenta residuos na colmeia
                    // System.out.println("Intruso " + getLocalName() + " roubou " + honeyStolen + " unidades de mel!");
                } else {
                    // Caso nao haja mel, ainda causa danos
                    JanitorBee.residual += 5;
                    // Ja que nao tinha mel aumenta menos os residuos
                    // System.out.println("Intruso " + getLocalName() + " nao encontrou mel, mas causou danos!");
                }

                // Notifica a rainha sobre o ataque
                notifyQueen();

                // Tenta matar zangoes ou operarias
                causeBeeDeaths();

                // Chance do intruso ser repelido pelos zangoes
                if (random.nextDouble() < 0.2) {
                    // System.out.println(getLocalName() + " foi atacado pelos zangoes e fugiu!");
                    doDelete(); // O intruso foge e e removido
                } else {
                    // System.out.println(getLocalName() + " foi afastado, mas pode voltar.");
                    doWait(3000); // Aguarda 3 segundos antes de tentar novamente
                }
            }
        });
    }

    // Metodo para notificar a rainha sobre a presenca do intruso
    private void notifyQueen() {
        if (queenName != null) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("IntruderDetected"); 
            // Mensagem de aviso para a rainha
            msg.addReceiver(new jade.core.AID(queenName, jade.core.AID.ISLOCALNAME));
            send(msg);
        }
    }

    // Metodo para simular mortes de abelhas causadas pelo intruso
    private void causeBeeDeaths() {
        if (QueenBee.WorkerBeeNumber > 0) {
            // Tenta atacar uma operaria
            String workerName = "Operaria" + (QueenBee.WorkerBeeId - QueenBee.WorkerBeeNumber);
            if (!attackedBees.contains(workerName) && isAgentAlive(workerName) && random.nextDouble() < 0.3) {
                // Marca como atacada
                attackedBees.add(workerName);

                // Envia mensagem para eliminar a operaria
                ACLMessage killMessage = new ACLMessage(ACLMessage.REQUEST);
                killMessage.setContent("KillBee: " + getLocalName());
                killMessage.addReceiver(new jade.core.AID(workerName, jade.core.AID.ISLOCALNAME));
                send(killMessage);

                // System.out.println("Operaria " + workerName + " morreu por ataque do intruso " + getLocalName() + "!");
            }
        } else if (QueenBee.droneBeenumber > 0) {
            // Tenta atacar um zangao
            String droneName = "Zangao" + (QueenBee.droneBeeId - QueenBee.droneBeenumber);
            if (!attackedBees.contains(droneName) && isAgentAlive(droneName) && random.nextDouble() < 0.1) {
                // Marca como atacado
                attackedBees.add(droneName);

                // Envia mensagem para eliminar o zangao
                ACLMessage killMessage = new ACLMessage(ACLMessage.REQUEST);
                killMessage.setContent("KillBee: " + getLocalName());
                killMessage.addReceiver(new jade.core.AID(droneName, jade.core.AID.ISLOCALNAME));
                send(killMessage);

                // System.out.println("Zangao " + droneName + " morreu por ataque do intruso " + getLocalName() + "!");
            }
        } else {
            // System.out.println(getLocalName() + " nao encontrou zangoes ou operarias para atacar!");
        }
    }

    // Verifica se um agente ainda esta vivo
    private boolean isAgentAlive(String agentName) {
        try {
            getContainerController().getAgent(agentName); // Tenta acessar o agente
            return true; // Agente existe
        } catch (Exception e) {
            return false; // Agente nao existe
        }
    }

    @Override
    protected synchronized void takeDown() {
        WorkerBee.activeBees.remove(this);
        // System.out.println(getLocalName() + " foi removido da lista de agentes ativos.");
        // System.out.println(WorkerBee.allAgents);
        // Finalizacao do agente intruso
        // System.out.println("======= takeDown ======= Intruso (" + getLocalName() + ") fugiu da regiao.");
        QueenBee.intruderBearNumber--; // Atualiza o numero de intrusos
    }
}
