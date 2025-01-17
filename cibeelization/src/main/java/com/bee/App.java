package com.bee;

import jade.core.Agent;

public class App extends Agent {
  private static final long serialVersionUID = 1L;

  protected void setup() {
    System.out.println("Z-Z-Z-Z Hello world! Z-Z-Z-Z");
    System.out.println("My name: " + getLocalName());
  }
}
