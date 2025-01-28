package com.school;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.List;

public class GerenteTurma extends Agent {
    private List<Integer> turmas = new ArrayList<>();

    protected void setup() {
        addBehaviour(new GerenteBehaviour());
        addBehaviour(new RelatorioBehaviour(this, 20000));
    }

    private class GerenteBehaviour extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive();
            if (msg != null && msg.getContent().equals("NOVO_ALUNO")) {
                // Adiciona o aluno à última turma, ou cria uma nova turma se necessário
                if (turmas.isEmpty() || turmas.get(turmas.size() - 1) >= 10) {
                    turmas.add(0);
                }
                turmas.set(turmas.size() - 1, turmas.get(turmas.size() - 1) + 1); // Adiciona o aluno à última turma
                System.out.println("Novo aluno adicionado à turma " + turmas.size() + ". Total de alunos: " + turmas.get(turmas.size() - 1));
            }
            block();
        }
    }

    private class RelatorioBehaviour extends TickerBehaviour {
        public RelatorioBehaviour(Agent a, long period) {
            super(a, period);
        }

        public void onTick() {
            System.out.println("\n===== Relatório de Turmas =====");
            if (turmas.isEmpty()) {
                System.out.println("Nenhuma turma criada ainda.");
            } else {
                for (int i = 0; i < turmas.size(); i++) {
                    System.out.println("Turma " + (i + 1) + ": " + turmas.get(i) + " alunos");
                }
            }
            System.out.println("==============================\n");
        }
    }
}