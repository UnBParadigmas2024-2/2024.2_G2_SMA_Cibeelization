package com.school;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Professor extends Agent {
    protected void setup() {
        addBehaviour(new ComportamentoProfessor(this, 5000)); // A cada 5 segundos
    }

    private class ComportamentoProfessor extends TickerBehaviour {
        public ComportamentoProfessor(Agent a, long period) {
            super(a, period);
        }

        public void onTick() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("INICIAR_AULA");
            
            // Envia a mensagem para todos os alunos
            for (int i = 1; i <= AmbienteEscolar.studentCounter; i++) {
                msg.addReceiver(getAID("Aluno" + i));
            }
            
            send(msg);
            System.out.println("Professor enviou mensagem para os alunos.");
        }
    }
}