# Entrega individual - Marcos Vinícius de Deus

**Disciplina**: FGA0210 - PARADIGMAS DE PROGRAMAÇÃO - T01 <br>
**Número do Grupo**: 02<br>
**Paradigma**: SMA<br>

## Justificativa
Como não consegui acompanhar o desenvolvimento do grupo, desenvolvi um projeto mais simples à parte, para aprender e entender um pouco mais sobre o paradigma SMA.

## Sobre
O projeto visa simular as dinâmicas de uma escola, onde as pessoas tem seus diferentes cargos, com diferentes atribuições, interagindo entre si para o funcionamento do ecossistema.

O projeto foi desenvolvido na linguagem *Java* e utiliza a tecnologia de sistemas multiagentes (SMA), baseando-se no framework JADE e utilizando o protocolo FIPA para comunicação e troca de mensagens entre os agentes.


As classes de agentes presentes no projeto incluem:

* **Ambiente escolar:** Cria novos alunos periodicamente.
* **Aluno:** Está matriculado em uma turma e presta atenção nas aulas.
* **Gerente de turma:** Matricula os novos alunos nas turmas, além de criar novas turmas quando as turmas estão cheias.
* **Professor:** Envia mensagem para os alunos.

Para mais detalhes, o arquivo [Descrição do projeto](school-simulation/docs/descricao-projeto.md) descreve mais detalhadamente o projeto.

## Instalação

1. **Certifique-se de ter os pré-requisitos instalados:**  
   - Java Development Kit (JDK) 8 ou superior.  
   - JADE Framework.  
   - Make (para usar o Makefile).  

2. **Clone o repositório:**  
   ```bash
   git clone https://github.com/UnBParadigmas2024-2/2024.2_G2_SMA_Cibeelization
   
   cd 2024.2_G2_SMA_Cibeelization
## Uso

Para executar o projeto, basta executar o script run.sh com os seguintes passos.

1. **Certifique-se de ter instalado o projeto corretamente (seção anterior).**

2. **Uma vez dentro do projeto, navegue até a pasta principal:**  
   ```bash
   cd school-simulation/
    ```
3. **Execute o script:**  
   ```bash
   ./run.sh
   ```

## Vídeo

https://youtu.be/xY3yq1-SAdo

## Fontes

[1] SERRANO, Milene. **Materiais da Disciplina de Paradigmas de Programação**. Faculdade de Ciências e Tecnologias em Engenharia (FCTE) – Campus Gama, Universidade de Brasília (UnB). Disponível em: [link](https://aprender3.unb.br/). Acesso em: 06 jan. 2025.

[2] FOUNDATION FOR INTELLIGENT PHYSICAL AGENTS. **FIPA** specifications. Disponível em: http://www.fipa.org/. Acesso em: 26 jan. 2025.
