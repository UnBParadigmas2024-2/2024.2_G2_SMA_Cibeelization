# Projeto Colmeia Multiagente

## Introdução

Sistemas multiagentes são um paradigma no qual entidades autônomas (agentes) interagem entre si e com o ambiente para alcançar objetivos específicos. Esses sistemas são amplamente utilizados em aplicações distribuídas, onde a colaboração ou competição entre agentes desempenha um papel central. Cada agente é uma entidade autônoma que interage com o ambiente e outros agentes para atingir seus objetivos. Agentes podem cooperar, competir ou operar de maneira independente, tornando esse paradigma altamente eficaz para sistemas distribuídos e dinâmicos.

JADE (Java Agent Development Framework) é um framework em Java para o desenvolvimento de sistemas multiagentes, oferecendo suporte para comunicação entre agentes, gerenciamento do ciclo de vida e outras funcionalidades essenciais para o desenvolvimento distribuído.

Neste projeto, simulamos uma colônia de abelhas, onde agentes desempenham papéis específicos, como rainha, operárias, zangões, limpadoras e intrusos. O objetivo do sistema é atingir uma quantidade definida de mel produzido, simulando o comportamento real das abelhas e os desafios enfrentados em uma colmeia.

### Agentes do Sistema

- **Rainha**: Responsável por criar novas abelhas (operárias ou zangões) periodicamente.
  - Chama um zangão para participar do vôo nupcial a cada 5 segundos, desde que haja uma única rainha.
  - Se houver mais de uma rainha, acontece a rinha de rainhas.
  - Após o acasalamento, a rainha gera 4 zangões, 20 abelhas operárias e 4 limpadoras.
  - Detecta ursos intrusos com 40% de chance de aparecerem, limitados a no máximo 3 por vez.
  - Morre de velhice após 30 segundos de vida.
- **Operárias**: Produzem mel ou geléia real e defendem a colmeia de intrusos.
  - Coleta polén a cada 2 segundos, com chance de ser afetada por inseticidas (5%).
  - Produz mel e geleia real a cada 2 segundos, se a colmeia estiver com resíduos abaixo de 10 unidades e o limite de coleta de pólen foi atingido.
  - Há limites mínimos diferentes de pólen coletado para fabricar mel ou geleia real.
  - Produz geleia real randomicamente com fator de 40% de probabilidade.
  - Come mel a cada 7 segundos. Quando tenta comer e não tem mel disponível, passa fome. Se acontecer mais duas vezes, morre de fome.
  - Morre de velhice após 15 segundos.
  - Se a quantidade de mel atingir o objetivo, a produção da colmeia é interrompida, todos os agentes são removidos e encerra o programa.
- **Zangões**: Contribuem para a reprodução junto com a rainha e ajudam na proteção da colmeia.
  - Tem tempo de vida limitado (16 segundos).
  - Não se alimenta.
  - Participa da reprodução.
  - Pode ser morto pelo intruso.
- **Limpadoras**: Garantem a limpeza da colmeia, removendo resíduos periodicamente.
  - Consome mel a cada 15 segundos.
  - Quanto tenta comer e não tem mel disponível, passa fome. Se acontecer novamente, morre de fome.
  - Morre de velhice após 15 segundos.
- **Intrusos**: Representados por ursos, aparecem de tempos em tempos e precisam ser combatidos.
  - A cada 3 ciclos, a rainha verifica a presença de novos ursos na região, com 40% de chance de surgir um intruso, até o limite máximo de 3 ursos de uma vez. 
  - A cada ciclo, o urso tenta roubar até 3 unidades de mel, gerando resíduos. Caso não encontre mel suficiente, causa danos menores e ainda aumenta os resíduos. 
  - Há 30% de chance de matar uma operária e 10% de matar um zangão em cada ciclo que ele aparece.
  - O urso tem 20% de chance de ser repelido e deixar a região; caso contrário, ele continua atacando nos ciclos seguintes. 
  - Os agentes Operária e Zangão reagem aos ataques e podem ajudar a repelir os invasores.
- **Inspetora**: Representa o apicultor e a cobrança de impostos pra receita, coletando o mel da colmeia.
  - Imprime log detalhado do status da colmeia a cada 5 segundos (ciclo).
  - Coleta mel a cada 20 segundos, simulando cobrança de impostos (reduz 70% do mel produzido).


## Metodologia

A implementação foi dividida em módulos com responsabilidades bem definidas, permitindo a colaboração eficiente entre os membros da equipe. 

### Etapas de Desenvolvimento

1. **Coordenação do Projeto**: Planejamento, integração de módulos e supervisão do progresso.
2. **Desenvolvimento dos Agentes**: Implementação dos comportamentos de cada agente.
3. **Comunicação entre Agentes**: Utilização do protocolo ACL (Agent Communication Language) do JADE.
4. **Interface Gráfica**: Criação de uma interface para monitorar a colmeia em tempo real.
5. **Condição de Parada**: Implementação de uma meta definida pela quantidade de mel produzido.
6. **Persistência**: Registro de estados importantes do sistema.
7. **Testes**: Garantia de funcionalidade e estabilidade do sistema.
8. **Documentação e Códigos**: Registro do processo.

### Divisão de Trabalho

| Módulo       | Interação                                                             | Responsável(eis)                       |
| ------------ | --------------------------------------------------------------------- | -------------------------------------- |
| **Rainha**   | Gerencia a colmeia, criando novas abelhas, coordenando interações gerais | Bruno Martins, Igor Penha, Leonardo Machado e Lucas Soares |
| **Rinha**    | Implementação de rinhas entre rainhas, garantindo que apenas uma sobreviva para liderar | Igor Penha e Leonardo Machado          |
| **Novas Rainhas** | Geração de novas rainhas quando a atual morre, garantindo a continuidade da colmeia | Igor Penha e Leonardo Machado          |
| **Operárias** | Produção de mel e geleia real, além da coleta de pólen               | Bruno Martins, Igor Penha, Leonardo Machado e Lucas Soares |
| **Operárias** | Defesa da colmeia contra intrusos, ajudando a repelir ameaças        | Heitor Marques e José Luís             |
| **Limpadora** | Mantém a colmeia limpa, removendo resíduos gerados na produção       | Bruno Martins, Igor Penha e Lucas Soares |
| **Intruso**  | Urso interage com a colmeia, roubando mel, atacando abelhas e podendo ser repelido | Heitor Marques e José Luís             |
| **Zangão**   | Apoia a rainha no nascimento de novas operárias e participa do voo nupcial | Bruno Ribeiro, Igor Penha e Leonardo Machado |
| **Inspetora**| Fornece relatórios periódicos da colmeia e coleta uma parte do mel como "imposto" | Igor Penha e Leonardo Machado          |
| **Interface**| Criação de uma interface para acompanhar o funcionamento da colmeia   | Raquel Eucária e Zenilda Pedrosa      |




| Módulo              | Função                                                                        | Responsáveis                                               |
| ------------------- | ----------------------------------------------------------------------------- | ---------------------------------------------------------- |
| Rainha              | Criação de novas abelhas                                                      | Bruno Martins, Igor Penha, Leonardo Machado e Lucas Soares |
| Rinha de abelhas    | Disputa entre abelhas candidatas a virar rainha                               | Igor Penha e Leonardo Machado                              |
| Novas Rainhas       | Criação de novas rainhas quando a rainha morre                                | Igor Penha e Leonardo Machado                              |
| Limpadora           | Limpa o resíduo gerado por abelhas operárias na produção de mel e geléia real | Bruno Martins, Igor Penha e Lucas Soares                   |
| Operárias           | Produção de mel e Geleia Real                                                 | Bruno Martins, Igor Penha, Leonardo Machado e Lucas Soares |
| Operárias           | Combate a intrusos                                                            | Heitor Marques e José Luis                                 |
| Intruso             | Aparição periódica e interação com as operárias                               | Heitor Marques e José Luis                                 |
| Zangão              | Gera Operárias junto com a rainha periodicamente                              | Bruno Ribeiro, Igor Penha e Leonardo Machado               |
| Inspetora           | Criação da abelha inspetora                                                   | Igor Penha e Leonardo Machado                              |
| Objetivo da colmeia | Implementação da condição de parada da produção de mel                        | Raquel Eucária e Zenilda Pedrosa                           |
| Interface Gráfica   | Monitoramento e controle                                                      | Raquel Eucária e Zenilda Pedrosa                           |
| Documentação        | Registro das informações                                                      | Raquel Eucária e Zenilda Pedrosa                           |


### Regras de Interação

- **Defesa Coordenada**: Quando um intruso aparece, a rainha alerta a colmeia, ativando os agentes operárias e Zangão para defender a colmeia.
- **Gerenciamento de Recursos**: O Apicultor coleta mel de tempos em tempos para si e para a receita federal.
- **Reprodução**: A rainha gera novas abelhas (operárias, limpadoras ou zangões) periodicamente, auxiliada pelos zangões.
- **Limpeza**: Limpadoras removem resíduos acumulados para manter a colmeia funcional.
- **Condição de Parada**: O sistema encerra ao atingir a quantidade de mel definida como objetivo.
- **Voo Nupcial**: A rainha chama o zangão para acasalarem, se não tiver zangão ela cria um.
- **Produção de mel**: Precisa de pólem para produzir e existe a probabilidade de produzir mel ou geleia. Essa operação gera resíduos, se estiver muito sujo a fabricação não acontece.
- **Morte por fome**: As operárias e limpadoras comem mel de tempos em tempos para sobreviverem, se não consumirem mel por mais de 3 ciclos elas morrem de fome.
- **Rainha morreu**: Se a rainha morre, as operárias começam a comer geleia real até atingir uma certa quantidade e se tornar rainha. 
- **Rinha de rainha**: Se no processo surgiu mais de uma rainha, elas se eliminam ("rinha") até sobrar apenas uma na colmeia.
- **Morte de velice**: Abelhas tem um período de vida pré-definido, após esse tempo elas morrem de velhice.
  

## Interface Gráfica

A interface gráfica foi implementada utilizando **javax.swing**, permitindo:
1. **Definição de Parâmetro Inicial**: Configuração da quantidade de mel que o inspetor deseja acumular (objetivo da colmeia - condição de parada).
2. **Monitoramento em Tempo Real**: Exibição da quantidade de mel, número de agentes e status da colmeia, além do comportamento de cada agente em tempo real.
3. **Mensagem Final**: Notificação ao atingir a condição de parada.

## Incrementos e Melhorias

Para melhorar o sistema em versões futuras, pode-se adicionar as seguintes funcionalidades:

- **Persistência**: Possibilidade de salvar o estado da colmeia.
- **Configuração Dinâmica**: Ajuste de parâmetros como frequência de intrusos e taxa de reprodução.
- **Relatórios**: Geração de relatórios sobre a produção de mel e combate a intrusos.

## Estrutura do Projeto

### Criação do Projeto JADE

#### **Configuração do Ambiente**:
   - Instale o Java.
   - Baixe o JADE no [site oficial](https://jade.tilab.com/).
   - Descompacte os arquivos em _C:\jade_
   - Execute o _make_ na pasta _cibeelization_.


#### **Classes Principais**:
   - [**App.java**](../cibeelization/src/main/java/com/bee/App.java): Inicializa a agente Inspetora e define os parâmetros.
   - [**Inspector.java**](../cibeelization/src/main/java/com/bee/Inspector.java): Imprime informações sobre o status da colmeia e coleta impostos.
   - [**QueenBee.java**](../cibeelization/src/main/java/com/bee/QueenBee.java): Gerencia a criação de novas abelhas operárias, limpadoras e zangões, o vôo nupcial, a rinha de rainhas e a detecção de intrusos.
   - [**WorkerBee.java**](../cibeelization/src/main/java/com/bee/WorkerBee.java): Implementa a produção de mel, geleia real e dinâmica de defesa, a dinâmica de morrer de fome e de velhice. Além da condição de parada da produção de mel.
   - [**JanitorBee.java**](../cibeelization/src/main/java/com/bee/JanitorBee.java): Implementa a dinâmica de limpeza da colmeia, a de morrer de fome e a de morrer de velhice.
   - [**DroneBee.java**](../cibeelization/src/main/java/com/bee/DroneBee.java): Implementação da resposta do convite de reprodução e da defesa da colmeia. 
   - [**IntruderBear.java**](../cibeelization/src/main/java/com/bee/IntruderBear.java): Implementa o comportamento de ataque do intruso, de mortes de abelhas e zangões e de fuga.
   - [**GUIStatusBee.java**](../cibeelization/src/main/java/com/bee/GUIStatusBee.java): Interface gráfica para controle e monitoramento.

### Estrutura de Código

O código completo das classes pode ser encontrado no repositório do projeto na seguinte pasta: [cibeelization/src/main/java/com/bee](../cibeelization/src/main/java/com/bee).

## Conclusão

O projeto "Cibeelization" demonstrou a eficácia do paradigma multiagente para simulações distribuídas. A implementação em JADE permitiu explorar conceitos como comunicação, interação e concorrência, simulando o comportamento de uma colmeia de abelhas, com alguns comportamentos fictícios criados pelos autores. Com a interface gráfica e a condição de parada, o sistema se torna acessível e interativo, além de demonstrar o comportamento dos agentes autônomos interagindo entre si.

## Bibliografia

> SERRANO, Milene. **Materiais da Disciplina de Paradigmas de Programação**. Faculdade de Ciências e Tecnologias em Engenharia (FCTE) – Campus Gama, Universidade de Brasília (UnB). Disponível em: [link](https://aprender3.unb.br/). Acesso em: 06 jan. 2025.

> FOUNDATION FOR INTELLIGENT PHYSICAL AGENTS. **FIPA** specifications. Disponível em: http://www.fipa.org/. Acesso em: 26 jan. 2025.

## Histórico de Versões

| Versão | Data       | Descrição                                                     | Autor                                              | Revisor                                               |
| ------ | ---------- | ------------------------------------------------------------- | -------------------------------------------------- | ----------------------------------------------------- |
| `1.0`  | 09/01/2025 | Versão inicial                                                | [Zenilda Vieira](https://github.com/zenildavieira) | [Raquel Eucária](https://github.com/raqueleucaria)    |
| `1.1`  | 22/01/2025 | remove base que saiu do trabalho e adiciona requisitos atuais | [Lucas Soares](https://github.com/lucasfs1007)     | [Bruno Marttins](https://github.com/gitbmvb)          |
| `1.2`  | 26/01/2025 | Complementa regras da colmeia                                 | [Igor Penha](https://github.com/igorpenhaa)        | [Heitor Marques](https://github.com/heitormsb)        |
| `1.3`  | 26/01/2025 | Atualização introdução e metodologia, interface gráfica, incrementos e melhorias e estrutura do projeto | [Zenilda Vieira](https://github.com/zenildavieira) | [Leonardo Machado](https://github.com/leonardogonmac) |

