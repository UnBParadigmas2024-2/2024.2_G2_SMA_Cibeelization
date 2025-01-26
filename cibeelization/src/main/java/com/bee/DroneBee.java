package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class DroneBee extends Agent {
    

    
    @Override
    protected void setup() {
        // Inicializacao do agente DroneBee
        WorkerBee.activeBees.add(this);
        // System.out.println(getLocalName() + " foi adicionado aa lista de agentes ativos.");
    
        
        // System.out.println("Novo zangao nascido! " + getLocalName());

        // Comportamento ciclico para processar mensagens recebidas
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    // Mensagem recebida pelo zangao
                    // System.out.println(getLocalName() + " recebeu uma mensagem: " + msg.getContent());
                    processMessage(msg);
                } else {
                    block(); // Aguarda ate que uma nova mensagem seja recebida
                }
            }
        });

        // Comportamento periodico para determinar o tempo de vida do zangao
        addBehaviour(new TickerBehaviour(this, 16000) { 
            // O zangao vive por 16 segundos
            @Override
            protected void onTick() {
                // System.out.println("Morreu de velhice " + getLocalName());
                doDelete(); // Remove o agente da plataforma
            }
        });

        // Comportamento adicional para lidar com mensagens de ataque de intrusos
        // ========
        // = novo =
        // ========
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getContent().startsWith("KillBee: ")) {
                    // Mensagem recebida indicando que o zangao foi morto por um intruso
                    // String intruderName = msg.getContent().split(": ")[1];
                    // System.out.println("Zangao " + getLocalName() + " morreu por ataque do intruso " + intruderName + "!");
                    doDelete(); // Remove o agente da plataforma
                } else {
                    block();
                }
            }
        });
        // ========
    }

    // Processa mensagens recebidas pelo zangao
    private void processMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.INFORM) {
            // Mensagem informativa recebida
            // System.out.println(getLocalName() + " recebeu uma mensagem informativa: " + msg.getContent());
        } else if (msg.getPerformative() == ACLMessage.REQUEST) {
            // Pedido recebido
            handleRequest(msg);
        }
    }

    // Trata pedidos recebidos pelo zangao
    private void handleRequest(ACLMessage msg) {
        if (msg.getContent().equalsIgnoreCase("Create DroneBee")) {
            // Pedido para criacao de um novo zangao
            // System.out.println(getLocalName() + " recebeu um pedido para criacao.");
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("DroneBee created successfully.");
            send(reply);
        } else if (msg.getContent().equalsIgnoreCase("Venha")) {
            // Pedido para participar do voo nupcial
            // System.out.println(getLocalName() + " foi para voo nupcial");
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("Bora");
            send(reply);
            doDelete(); // O zangao e removido apos participar do voo nupcial
        } else {
            // System.out.println(getLocalName() + " recebeu um pedido desconhecido: " + msg.getContent());
        }
    }

    @Override
    protected synchronized void takeDown() {
        WorkerBee.activeBees.remove(this);
        // System.out.println(getLocalName() + " foi removido da lista de agentes ativos.");
        // O zangao nao e registrado nem desregistrado
        // ele nao precisa ser descoberto por outros agentes
        // Finalizacao do agente DroneBee
        // System.out.println("======= takeDown ======= O zangao (" + getLocalName() + ") morreu");
        QueenBee.ordemAcasalamento++; // Atualiza a ordem do voo nupcial
        QueenBee.droneBeenumber--; // Reduz o numero de zangoes
    }
}
