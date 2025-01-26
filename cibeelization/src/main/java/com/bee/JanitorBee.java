package com.bee;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class JanitorBee extends Agent {

    // Variavel compartilhada para rastrear residuos na colmeia
    public static int residual = 0;
    private int mortePorFome = 0; // Contador para mortes causadas por falta de mel


    @Override
    protected void setup() {
        // Inicializacao do agente JanitorBee
        // System.out.println("Nova limpadora nascida: " + getLocalName());
        WorkerBee.activeBees.add(this);
        // System.out.println(getLocalName() + " foi adicionado aa lista de agentes ativos.");
        

        // Comportamento ciclico para processar mensagens e executar a limpeza
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    // Processar mensagem recebida
                    processMessage(msg);
                } else {
                    // Se nenhuma mensagem foi recebida, realiza a limpeza da colmeia
                    cleanHive();
                }
            }
        });

        // Comportamento periodico para simular o consumo de mel a cada 15 segundos
        addBehaviour(new TickerBehaviour(this, 15000) {
            @Override
            protected void onTick() {
                eat();
            }
        });

        // Comportamento periodico para determinar o tempo de vida da abelha
        addBehaviour(new TickerBehaviour(this, 15000) { 
            // Limite de vida de 15 segundos
            @Override
            protected void onTick() {
                // System.out.println("A limpadora " + getLocalName() + " morreu de velhice");
                doDelete(); // Remove o agente apos o tempo de vida
            }
        });
    }

    // Metodo sincronizado para simular o consumo de mel
    protected synchronized void eat() {
        if (WorkerBee.quantityOfHoney > 0) {
            WorkerBee.quantityOfHoney--; // Consome uma unidade de mel
            // System.out.println("A limpadora " + getLocalName() + " consumiu 1 unidade de mel");
            mortePorFome = 0; // Reseta o contador de fome
        } else {
            // Quando nao ha mel disponivel
            // System.out.println("A limpadora " + getLocalName() + " passou fome");
            mortePorFome++;
            doWait(1000); // Aguarda 1 segundo antes de tentar novamente
        }
        if (mortePorFome == 2) {
            // Remove a abelha se passar fome duas vezes seguidas
            // System.out.println("A limpadora " + getLocalName() + " morreu de fome");
            doDelete();
        }
    }

    // Metodo para limpar residuos na colmeia
    protected void cleanHive() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (residual > 0) {
                    residual--; // Reduz o nivel de residuos na colmeia
                    // System.out.println("Limpando. Residuo restante = " + residual);
                }
            }
        });
    }

    // Processa mensagens recebidas
    private void processMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.INFORM) {
            // Mensagem informativa recebida (nao usada atualmente)
        } else if (msg.getPerformative() == ACLMessage.REQUEST) {
            handleRequest(msg);
        }
    }

    // Trata pedidos recebidos
    private void handleRequest(ACLMessage msg) {
        if (msg.getContent().equalsIgnoreCase("Create JanitorBee")) {
            // Responde a solicitacao de criacao de uma nova abelha limpadora
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("JanitorBee created successfully.");
            send(reply);
        }
    }

    @Override
    protected synchronized void takeDown() {
        WorkerBee.activeBees.remove(this);
        // System.out.println(getLocalName() + " foi removido da lista de agentes ativos.");
        // A limpadora nao e registrada nem desregistrada
        // ela nao precisa ser descoberta por outros agentes
        // Finalizacao do agente JanitorBee
        // System.out.println("======= takeDown ======= A limpadora (" + getLocalName() + ") morreu");
        QueenBee.janitorBeenumber--; // Atualiza o numero de abelhas limpadoras
    }
}
