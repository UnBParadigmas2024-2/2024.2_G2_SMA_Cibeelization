package com.bee;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
// import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
// import jade.wrapper.AgentController;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javax.swing.*;

public class InspectorBee extends Agent {

    private static final long serialVersionUID = 1L;
    
    public static final String GOAL_REACHED = "GOAL_REACHED";
    public static int ciclos = 0; // Contador de ciclos para monitoramento.
    public static int tlBee = 0; // TOTAL ABELHAS
    public static int tlProduction = 0; // TOTAL PRODUÇÃO
    private static final List<String> protectedAgents = Arrays.asList("rma", "df", "ams");
    public static int queenBeeNumber = 0;    // Numero de rainhas vivas (maximo de 1 por padrao).  
    private Hashtable<String,Integer> beesTotal;
    private Hashtable<String,Integer> productionTotal;
    private boolean goalReached = false;

    @Override
    protected void setup() {
        
        registerInDF();
        WorkerBee.activeBees.add(this);
        // Chama a janela da interface gráfica
        // Cria e mostra a janela
        // new GuiStatusBee();
        // Cria e exibe a janela GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuiStatusBee();
            }
        });
        
        // Regra nova: Criacao da Rainha (QueenBee).
        // A abelha da receita cria o agente "QueenBee" no inicio.
        // teste com contador + UUID
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                try {
                    String queenName = "QueenBee" + "("+queenBeeNumber+") - "+UUID.randomUUID().toString();
                    AgentController QueenBeeAC = getContainerController().createNewAgent(queenName, "com.bee.QueenBee", null);
                    QueenBeeAC.start();
                    // System.out.println(queenName);
                    queenBeeNumber++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Regra nova: Log periodico dos recursos da colmeia.
        // A cada 5 segundos, imprime o estado dos recursos.
        // MODIFICAÇÃO -> Se alcança a meta para de imprimir os recursos
        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                if (goalReached) {
                    stop();
                } else {
                    printa_recursos();
                }
            }
        });

        // Regra nova: Cobranca de imposto periodico.
        // A cada 20 segundos, reduz o mel em 70% para simular a cobranca de impostos.
        addBehaviour(new TickerBehaviour(this, 20000) {
            @Override
            protected void onTick() {
                imposto();
            }
        });

        // Regra nova: Delet os agentes quando alcança a meta
        // Quando recebe a mensagem de meta alcançada irá realizar a exclusão dos agentes existentes chamando deleteCibeelization
        // OBS: As saidas foram usadas paa delimitar as quando a impressao de recurso foi feita apos a meta ser alcançada (utilizar para melhorar a lógica)
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive( MessageTemplate.MatchPerformative( ACLMessage.INFORM ) );
                if (msg != null) {
                    if (GOAL_REACHED.equals(msg.getContent())) {
                        goalReached = true;
                        System.out.println("------------------------------");
                        System.out.println("======== META ALCANCADA =======");
                        System.out.println("------------------------------");
                        printa_recursos();
                        deleteCibeelization();
                    }
                } 
                else {
                    block();
                }
            }
        });
    }

    // Regra nova: Consulta os agentes que serao deletados
    // Cuida para nao excluir o inspector e a GUI (ams, df...)
    public void deleteCibeelization() {
        addBehaviour(new OneShotBehaviour() {
            private static final long serialVersionUID = 1L;
    
            @Override
            public void action() {
                try {
                    SearchConstraints sc = new SearchConstraints();
                    sc.setMaxResults(Long.MAX_VALUE);
    
                    // Busca todos os agentes registrados na AMS
                    AMSAgentDescription[] agents = AMSService.search(myAgent, new AMSAgentDescription(), sc);
    
                    // Itera sobre os agentes encontrados e deleta cada um
                    for (AMSAgentDescription agentDesc : agents) {
                        AID agentID = agentDesc.getName();
                        if (!agentID.equals(getAID()) && !protectedAgents.contains(agentID.getLocalName())) { // Evita deletar a si mesmo e agentes protegidos
                            doDelete(agentID);
                        }
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void doDelete(AID agentID) {
        try {
            if (!protectedAgents.contains(agentID.getLocalName())) {
                // Verifica se o agente ainda existe
                AgentController agentController = getContainerController().getAgent(agentID.getLocalName());
                if (agentController != null) {
                    agentController.kill();
                    System.out.println("Agente " + agentID.getLocalName() + " deletado.");
                }
            }
        } catch (ControllerException e) {
            System.out.println("Agente " + agentID.getLocalName() + " não encontrado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Regra nova: Simula a cobranca de impostos reduzindo o mel disponivel.
    public void imposto() {
        // System.out.println("A receita bateu na colmeia");
        WorkerBee.quantityOfHoney *= 0.3; // Reduz o mel para 30% do total (70% e removido).
    }

    // Regra nova: Exibe o estado atual da colmeia.
    // Modificação -> apresenta o total de produção -----> alterar para isso ser a meta
    // ALtera o local da variavel queenBeeNumber para a propria classe inspector  ------> fazer isso com as demais
    public static void printa_recursos() {
        tlBee = QueenBee.InspectorBeeNumber + queenBeeNumber + QueenBee.WorkerBeeNumber + QueenBee.janitorBeenumber + QueenBee.droneBeenumber;
        tlProduction = WorkerBee.quantityOfHoney + WorkerBee.quantityOfRoyalJelly;
        System.out.println("\n========== CICLO " + ciclos++ + " ==========");
        System.out.println("Inspetor: " + QueenBee.InspectorBeeNumber);
        System.out.println("Rainhas: " + queenBeeNumber);
        System.out.println("Operarias: " + QueenBee.WorkerBeeNumber);
        System.out.println("Limpadoras: " + QueenBee.janitorBeenumber);
        System.out.println("Zangoes: " + QueenBee.droneBeenumber);
        System.out.println("Polen: " + WorkerBee.quantityOfPollen);
        System.out.println("Mel: " + WorkerBee.quantityOfHoney);
        System.out.println("Geleia Real: " + WorkerBee.quantityOfRoyalJelly);
        System.out.println("Residuos: " + JanitorBee.residual);
        System.out.println("Intrusos proximos: " + QueenBee.intruderBearNumber);
        // ========
        // = novo =
        // ========
        System.out.println("------------------------------");
        System.out.println("Total de Abelhas: " + tlBee);
        System.out.println("Total de produção: " + tlProduction);
        System.out.println("==============================\n");
        // ========
    }

    // Registra o agente no Diretorio de Facilitadores (DF).
    private void registerInDF() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("InspectorBee-agent");
        sd.setName("InspectorBee-service");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd); // Registro no DF.
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized void takeDown() {
        WorkerBee.activeBees.remove(this);
        QueenBee.InspectorBeeNumber--;
        // System.out.println(getLocalName() + " foi removido da lista de agentes ativos.");
        // Remove o registro do agente no DF antes de encerrar.
        try {
            // System.out.println("======= takeDown ======= A abelha da receita (" + getLocalName() + ") morreu");
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
