package com.bee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Thread;
// import java.util.concurrent.*; 
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
// import jade.domain.DFService;
import jade.lang.acl.ACLMessage;
// import jade.domain.FIPAAgentManagement.DFAgentDescription;
// import jade.domain.FIPAAgentManagement.ServiceDescription;
// import jade.domain.FIPAException;
// import jade.core.AID;
// import jade.wrapper.AgentController;
// import jade.wrapper.ContainerController;
import jade.wrapper.AgentController;


public class WorkerBee extends Agent {

    private final Random random = new Random();
    // private final double chanceOfDying = 0.5;       // Chance de morte por condicoes externas
    // private final double chanceOfRoyalJelly = 0.1;  // Chance de produzir geleia real
    private final int minRequiredForHoney = 3;      // Quantidade minima de polen para produzir mel
    private final int minRequiredForRoyalJelly = 3; // Quantidade minima de polen para geleia real
    public static int quantityOfPollen = 0;         // Quantidade total de polen na colmeia
    public static int quantityOfHoney = 10;         // Quantidade total de mel na colmeia
    public static int honeyGoal = 30;               // Quantidade alvo de mel
    public static int quantityOfRoyalJelly = 50;    // Quantidade total de geleia real na colmeia
    private int eatenRoyalJelly = 0;                // Quantidade de geleia real consumida pela operaria
    private int mortePorFome = 0;                   // Contador para rastrear a fome e causar a morte


    // Lista estática para armazenar os agentes ativos
    public static List<Agent> activeBees = new ArrayList<>();

    @Override
    protected void setup() {
        // Inicializacao da operaria
        // Regras novas: controle de fome, morte por fome e producao com residuos
        //System.out.println("Nova operaria nascida! " + getLocalName());
        WorkerBee.activeBees.add(this);
        //System.out.println(getLocalName() + " foi adicionado aa lista de agentes ativos.");        

        // Comportamento ciclico para coletar polen, produzir mel ou geleia
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if(QueenBee.queenBeeNumber >= 0){
                    ACLMessage msg = myAgent.receive();
            
                    if (msg != null) {
                        processMessage(msg); // Processa mensagens recebidas
                    } else {
                        if(Math.random() < 0.5) {
                            collectPollen(); // Tenta coletar polen
                        }
                        block();
                    }
                    collectPollen(); // Regras antigas: repeticao de coleta
                    makeHoneyOrRoyalJelly(); // Tenta produzir mel ou geleia real
                }
            }
        });

        // Comportamento ciclico para verificar se a operaria deve virar rainha
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if(QueenBee.queenBeeNumber <= 0 && quantityOfRoyalJelly > 20){
                    eatRoyalJelly();
                    if(eatenRoyalJelly >= 5){
                        newQueen(); // Nova rainha gerada
                        doDelete(); // Operaria removida (ela virou rainha)
                    }
                }
            }
        });

        // Comportamento para consumir mel periodicamente
        addBehaviour(new TickerBehaviour(this, 7000) {
            @Override
            protected void onTick() {
                eatHoney(); // Regras novas: morte por fome em 3 ciclos
            }
        });

        // Comportamento para morte por velhice
        addBehaviour(new TickerBehaviour(this, 15000) {
            @Override
            protected void onTick() {
                System.out.println(getLocalName() + " morreu de velhice");
                doDelete(); // Regras antigas: termino natural da vida
            }
        });

        // Comportamento para responder a mensagens de intrusos
        // ========
        // = novo =
        // ========
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getContent().startsWith("KillBee: ")) {
                    String intruderName = msg.getContent().split(": ")[1];
                    System.out.println("Operaria " + getLocalName() + " morreu por ataque do intruso " + intruderName + "!");
                    doDelete(); // Regras novas: remocao direta por ataque
                } else {
                    block();
                }
            }
        });        
        // ========
    }

    public synchronized void eatHoney() {
        if(quantityOfHoney == 0){
            // Operaria sente fome, incrementa contador
            System.out.println("Operaria " + getLocalName() + " com fome.");
            mortePorFome++;
            doWait(1000); // Pausa antes da proxima tentativa
        }
        else {
            // Operaria consome mel, reseta o contador de fome
            System.out.println("Operaria " + getLocalName() + " comendo mel.");
            quantityOfHoney--;
            mortePorFome = 0;
        }

        if(mortePorFome == 3){
            // Morte da operaria apos 3 ciclos sem comer
            System.out.println("Operaria " + getLocalName() + " comendo mel.");
            doDelete();
        }
    }

    private synchronized void newQueen() {
        // Cria uma nova rainha
        try {
            String queenName = "QueenBee" + QueenBee.queenBeeId++;
            AgentController NewQueenBee2AC = getContainerController().createNewAgent(queenName, "com.bee.QueenBee", null);
            NewQueenBee2AC.start();
            QueenBee.queenBeeNumber++;
            System.out.println("A abelha operaria virou uma nova rainha: ");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void eatRoyalJelly() {
        // Consome geleia real para possivel transformacao em rainha
        // System.out.println("Operaria " + getLocalName() + " comendo geleinha.");
        int amnt = random.nextInt(5);
        if(quantityOfRoyalJelly > 0) {
            mortePorFome = 0; // Reseta a fome
        }
        
        if(amnt > quantityOfRoyalJelly){
            amnt  = quantityOfRoyalJelly;
            quantityOfRoyalJelly = 0;
            this.eatenRoyalJelly += amnt;    
        }
        else {
            quantityOfRoyalJelly -= amnt;
            this.eatenRoyalJelly += amnt;
        }

        try {
            Thread.sleep(50); // Simula o tempo de consumo
        }
        catch(InterruptedException e) {
            System.err.println("A thread foi interrompida: " + e.getMessage());
            Thread.currentThread().interrupt();                
        }
    }

    private synchronized void collectPollen() {
        // Simula a coleta de polen pela operaria
        // System.out.println("Operaria " + getLocalName() + " saiu para coletar polen");
        doWait(2000);

        if (random.nextDouble() < 0.05) {
            System.out.println("Operaria " + getLocalName() + " morreu por conta do inseticida!");

            ACLMessage deathMsg = new ACLMessage(ACLMessage.INFORM);
            deathMsg.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
            deathMsg.setContent("A operaria " + getLocalName() + " morreu para o inseticida :(");
            send(deathMsg);

            doDelete();
        } else {
            // System.out.println("A operaria " + getLocalName() + " acabou de retornar para a colmeia!");
            quantityOfPollen++;

            ACLMessage report = new ACLMessage(ACLMessage.INFORM);
            report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
            report.setContent("Pollen collected");
            send(report);
        }
    }

    private synchronized void makeHoneyOrRoyalJelly() {
        // System.out.println("Operaria " + getLocalName() + " esta tentando produzir algo...");
        // System.out.println("Polen = " + quantityOfPollen);
        doWait(2000);

        double randomChanceForJelly = random.nextDouble();

        if (quantityOfPollen >= minRequiredForRoyalJelly && randomChanceForJelly <= 0.4) {
            if (JanitorBee.residual < 10) {
                // produzir geleia real
                quantityOfPollen -= minRequiredForRoyalJelly;
                quantityOfRoyalJelly++;
                //System.out.println("Operaria " + getLocalName() + " produziu geleia real! Geleias totais: " + quantityOfRoyalJelly);
    
                ACLMessage report = new ACLMessage(ACLMessage.INFORM);
                report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
                report.setContent("Royal jelly produced");
                JanitorBee.residual += 2;
                // System.out.println("gerando + 2 de residuo");
                send(report);
            } else {
                System.out.println("tem mt residuo pra geleia real");
            }
        } else if (quantityOfPollen >= minRequiredForHoney) {
            if (JanitorBee.residual < 10) {
                // produzir mel
                quantityOfPollen -= minRequiredForHoney;
                quantityOfHoney++;
                // System.out.println("Operaria " + getLocalName() + " produziu mel! Mel total: " + quantityOfHoney);

                // Verificar se atingiu honeyGoal unidades de mel
                if (quantityOfHoney >= honeyGoal || (QueenBee.queenBeeNumber == 0 && QueenBee.WorkerBeeNumber == 0)) {
                    System.out.println("Operaria " + getLocalName() + " atingiu a producao maxima de mel (" + honeyGoal + ") e a colmeia vai parar.");

                    // Chama o método para remover todos os agentes
                    WorkerBee.terminateAllBees();

                    doDelete(); // Remove o próprio agente
                    return; // Interrompe o método
                }

                ACLMessage report = new ACLMessage(ACLMessage.INFORM);
                report.addReceiver(new jade.core.AID("BeeQueen", jade.core.AID.ISLOCALNAME));
                report.setContent("Honey produced");
                JanitorBee.residual += 1;
                // System.out.println("gerando + 1 de residuo");
                send(report);
            } else {
                System.out.println("mt residuo pra mel");
            }
        } else {
            // System.out.println("Operaria " + getLocalName() + " nao conseguiu produzir por falta de polen.");
        }
    }

    
    // Método para encerrar todos os agentes ativos
    public static synchronized void terminateAllBees() {
        // Itera sobre a lista de agentes ativos
        for (Agent bee : new ArrayList<>(activeBees)) {
            try {
                // Encerra o agente
                //System.out.println("Encerrando o agente: " + bee.getLocalName());
                bee.doDelete();
            } catch (Exception e) {
                //System.err.println("Erro ao encerrar o agente " + bee.getLocalName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Limpa a lista após encerrar os agentes
        activeBees.clear();

        // Encerra o programa
        System.out.println("Todos os agentes foram removidos. O programa foi encerrado.");
        InspectorBee.printa_recursos();
        System.exit(0);
        
    }


    private void processMessage(ACLMessage msg) {
        if (msg.getPerformative() == ACLMessage.INFORM) {

        } else if (msg.getPerformative() == ACLMessage.REQUEST) {
            handleRequest(msg);
        }
    }

    private void handleRequest(ACLMessage msg) {
        if (msg.getContent().equalsIgnoreCase("Create WorkerBee")) {
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("WorkerBee created successfully.");
            send(reply);
        }
    }

    @Override
    protected synchronized void takeDown() {
        WorkerBee.activeBees.remove(this);
        //System.out.println(getLocalName() + " foi removido da lista de agentes ativos.");
        // Diminui o numero de operarias na colmeia ao ser removida
        //System.out.println("======= takeDown ======= A operaria " + getLocalName() + " morreu");
        QueenBee.WorkerBeeNumber--;
    }
}
