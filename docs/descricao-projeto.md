# Projeto Colmeia Multiagente

## Introdução

No paradigma de sistemas multiagentes, cada agente é uma entidade autônoma que interage com o ambiente e outros agentes para atingir seus objetivos. Agentes podem cooperar, competir ou operar de maneira independente, tornando esse paradigma altamente eficaz para sistemas distribuídos e dinâmicos.

JADE (Java Agent Development Framework) é um framework poderoso para a criação de sistemas multiagentes em Java, que fornece ferramentas para comunicação entre agentes e gerenciamento de ciclo de vida dos mesmos.

Neste projeto, vamos simular o comportamento de uma colmeia para uma implementação em Java usando JADE, onde os agentes representarão diferentes papéis como rainha, operárias, zangões e  e intrusos.

**_melhorar a introdução teórica incluindo bibliografia..._**

### Agentes do Sistema

* Rainha: Cria novas operária periodicamente.
* Operárias: Produzem mel/Geleia Real ou atacam intrusos quando detectados.
* Intruso: Aparece periodicamente e precisa ser combatido pelas operárias.
* Zangão: Ajuda na proteção da colmeia e na reprodução com a rainha.

## Metodologia

A implementação será dividida em partes, com cada membro da equipe responsável por uma função ou aspecto específico do sistema. As funções e responsabilidades são:

1. Coordenação do Projeto: Gerenciar as tarefas, integração e documentação.
2. Desenvolvimento dos Agentes: Implementar o comportamento de cada um dos agentes: rainha, larva, operária, intruso, etc.
3. Comunicação: Garantir que os agentes se comuniquem corretamente usando o protocolo ACL do JADE.
4. Desenvolvimento de Regras de Interação: Definir regras de interação entre agentes.
5. Concurrency: Garantir que o sistema lida corretamente com threads e concorrência.
6. Persistência: Implementar mecanismos para salvar o estado da colmeia.
7.  Interface Gráfica: Criar uma interface gráfica que represente o estado da colmeia em tempo real.
8.  Testes: Criar casos de teste para garantir que o sistema funciona como esperado.
9.  Documentação: Escrever a documentação do código e do sistema.
10. Otimização: Identificar gargalos e otimizar o desempenho do sistema.
    
**_revisar a metodologia e continuar descrevendo aqui outras funções se houver..._**

### Divisão de Trabalho

Cada membro da equipe será responsável por desenvolver e integrar os seguintes agentes:

| Módulo    | Interação                                       | Responsável(eis) |
| --------- | ----------------------------------------------- | ---------------- |
| Rainha    | Criação e comunicação de novas larvas           |  Bruno Martins e Lucas Felipe   |
| Operárias | Produção de mel/Geleia Real e combate a intrusos            |                  |
| Intruso   | Aparição periódica e interação com as operárias | Heitor Marques e José Luis |
| Zangão    |Gera Operárias junto com a rainha periodicamente  | |


### Regras de interação entre agentes

* Defesa coordenada: Quando um intruso aparece, o Guardião alerta a colmeia, ativando os agentes Soldado e Zangão para defender a colmeia.
* Gerenciamento de recursos: O Apicultor coleta mel apenas quando o estoque é alto, enquanto a Forrageira trabalha para garantir que a produção não pare.
* Cuidado com as larvas: A Enfermeira verifica constantemente o estado das larvas para garantir que recebam mel suficiente para se transformarem em operárias.
* Clima e exploração: O Meteorologista avisa o Exploradora e o Forrageira sobre condições climáticas ruins para evitar perdas desnecessárias.
* Limpeza da colmeia: A Limpadora trabalha para manter a colmeia limpa e garantir que as operárias possam se concentrar em suas tarefas.


**_continuar escrevendo aqui as demais interações..._**

### Incrementos e Melhorias

Para melhorar o sistema, adicionamos as seguintes funcionalidades:

* Monitoramento em tempo real: Interface gráfica para visualizar o estado da colmeia. ????????????????
* Persistência: Salvar o estado da colmeia em um banco de dados. ????????????????
* Configurações Dinâmicas: Permitir ajustes nos parâmetros do sistema (tempo de vida das operárias, frequência de intrusos, etc.). ????????????????
* Relatórios: Gerar relatórios sobre a produção de mel e o número de intrusos combatidos. ????????????????

**_continuar escrevendo aqui os demais incrementos e melhorias se houver..._**

## Estrutura Inicial do Projeto

### Criação do Projeto JADE

1. Setup do JADE:
    * Baixe o JADE no site oficial.
    * Adicione o JAR do JADE ao seu projeto.

2. Classe Principal:

```java
_código_
```

3. Agente Rainha:

```java
_código_
```

4. Agente Intruso:

```java
_código_
```

**_continuar com os codigos..._**


## Conclusão

Este projeto oferece uma excelente oportunidade para explorar o paradigma multiagente usando JADE em Java. A divisão do trabalho permitirá que todos os membros da equipe contribuam significativamente para o sucesso do projeto. O sistema final será capaz de simular o comportamento de uma colmeia com agentes autônomos interagindo entre si.

**_melhorar e continuar a conclusão..._**

## Referências Bibliográficas

[1]   


## Histórico de versões

| Versão | Data       | Descrição      |                       Autor                        |                      Revisor                       |
| :----: | ---------- | -------------- | :------------------------------------------------: | :------------------------------------------------: |
| `1.0`  | 09/01/2025 | Versão inicial | [Zenilda Vieira](https://github.com/zenildavieira) | [Raquel Eucaria](https://github.com/raqueleucaria) |
| `2.0`  | 22/01/2025 | remove base que saiu do trabalho e adiciona requisitos atuais | [Lucas Felipe](https://github.com/lucasfs1007) | |
