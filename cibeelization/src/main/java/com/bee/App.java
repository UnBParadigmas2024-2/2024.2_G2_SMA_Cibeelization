package com.bee;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class App {
    public static void main(String[] args) {
        // Inicializa a instância do runtime do JADE.
        Runtime runtime = Runtime.instance();

        // Cria um perfil para configurar o contêiner principal.
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost"); // Define o host principal como "localhost".
        profile.setParameter(Profile.GUI, "true");           // Habilita a interface grafica do JADE para monitoramento.

        // Cria o contêiner principal do sistema multiagente.
        AgentContainer mainContainer = runtime.createMainContainer(profile);

        try {
            // Regra nova: Inicializacao do agente "InspectorBee".
            // O agente InspectorBee e responsavel por:
            // 1. Gerar logs detalhados do sistema (nova regra).
            // 2. Simular um apicultor que retira 70% do mel produzido em intervalos regulares (nova regra).
            // 3. Morrer apos um periodo de tempo predefinido (nova regra).
            
            // System.out.println("Iniciando a abelha da receita");
            
            // Cria o agente InspectorBee e o inicia dentro do contêiner principal.
            AgentController InspectorBeeAC = mainContainer.createNewAgent("InspectorBee", InspectorBee.class.getName(), null);
            InspectorBeeAC.start();

        } catch (StaleProxyException e) {
            // Caso ocorra um erro na criacao ou inicializacao de agentes, ele sera tratado aqui.
            System.err.println("Error while starting agents:");
            e.printStackTrace();
        }
    }
}
