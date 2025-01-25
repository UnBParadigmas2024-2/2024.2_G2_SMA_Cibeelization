package com.bee;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.AgentController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IntruderSpawner extends Agent {
    private static final int MAX_INTRUDERS = 3; // Limite máximo de intrusos ativos
    private int intruderCount = 0; // Contador global de intrusos criados
    private final List<String> activeIntruders = new ArrayList<>(); // Lista de intrusos ativos

    @Override
    protected void setup() {
        System.out.println("IntruderSpawner inicializado!");

        // Ticker para verificar e criar novos IntruderBear
        addBehaviour(new TickerBehaviour(this, 15000) { // A cada 15 segundos
            @Override
            protected void onTick() {
                if (activeIntruders.size() < MAX_INTRUDERS) {
                    try {
                        String intruderName = "IntruderBear" + intruderCount++;
                        AgentController intruder = getContainerController().createNewAgent(intruderName, "com.bee.IntruderBear", new Object[]{getLocalName()});
                        intruder.start();
                        activeIntruders.add(intruderName);
                        System.out.println("Gerado novo IntruderBear: " + intruderName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Número máximo de IntruderBear alcançado (" + MAX_INTRUDERS + ").");
                }
            }
        });

        // Ticker para limpar intrusos inativos
        addBehaviour(new TickerBehaviour(this, 10000) { // A cada 10 segundos
            @Override
            protected void onTick() {
                Iterator<String> iterator = activeIntruders.iterator();
                while (iterator.hasNext()) {
                    String name = iterator.next();
                    try {
                        // Verifica se o agente ainda existe
                        getContainerController().getAgent(name);
                    } catch (Exception e) {
                        // Se não existe, remove da lista
                        iterator.remove();
                        System.out.println("Removido intruso inativo: " + name);
                    }
                }
                System.out.println("Intrusos ativos: " + activeIntruders);
            }
        });
    }

    public void removeIntruder(String intruderName) {
        activeIntruders.remove(intruderName);
        System.out.println("IntruderBear removido: " + intruderName);
    }
}
