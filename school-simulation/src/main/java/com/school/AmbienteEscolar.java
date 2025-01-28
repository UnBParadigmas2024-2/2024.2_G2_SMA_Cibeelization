package com.school;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class AmbienteEscolar {
    static int studentCounter = 1;

    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        ContainerController container = runtime.createMainContainer(profile);

        try {
            // Cria o professor e o gerente de turma
            AgentController teacher = container.createNewAgent(
                "Professor1", "com.school.Professor", null);
            
            AgentController classManager = container.createNewAgent(
                "GerenteTurma", "com.school.GerenteTurma", null);
            
            teacher.start();
            classManager.start();

            // Cria novos alunos a cada 5 segundos
            while (true) {
                AID gerenteTurmaAID = new AID("GerenteTurma", AID.ISLOCALNAME);

                Object[] argsAluno = new Object[]{gerenteTurmaAID};

                AgentController student = container.createNewAgent(
                    "Aluno" + studentCounter, "com.school.Aluno", argsAluno);
                student.start();
                studentCounter++;
                Thread.sleep(5000);
            }
        } catch (StaleProxyException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}