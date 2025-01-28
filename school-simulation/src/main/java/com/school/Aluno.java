package com.school;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class Aluno extends Agent {
    private AID gerenteTurmaAID;

    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            gerenteTurmaAID = (AID) args[0];
        }

        // Notifica o GerenteTurma sobre o novo aluno
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("NOVO_ALUNO");
        msg.addReceiver(gerenteTurmaAID);
        send(msg);

        addBehaviour(new ComportamentoAluno());
    }

    private class ComportamentoAluno extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                if (msg.getContent().equals("INICIAR_AULA")) {
                    System.out.println(getLocalName() + " está prestando atenção na aula.");
                }
            }
            block();
        }
    }
}