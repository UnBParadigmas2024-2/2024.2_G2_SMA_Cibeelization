package com.bee;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class App {
    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = runtime.createMainContainer(profile);

        try {
            System.out.println("Iniciando a abelha da receita");
            AgentController inspectorBee = mainContainer.createNewAgent("InspectorBee", InspectorBee.class.getName(), null);
            inspectorBee.start();
        } catch (StaleProxyException e) {
            System.err.println("Error while starting agents:");
            e.printStackTrace();
        }
    }
}
