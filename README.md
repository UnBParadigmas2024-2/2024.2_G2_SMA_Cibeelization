# 🐝 Cibeelization

> "*O todo sem a parte não é todo; A parte sem o todo não é parte; Mas se a parte o faz todo, sendo parte; Não se diga, que é parte, sendo todo.*" - Gregório de Matos

**Disciplina**: FGA0210 - PARADIGMAS DE PROGRAMAÇÃO - T01 <br>
**Número do Grupo**: 02<br>
**Paradigma**: SMA<br>

## Alunos
| Matrícula  | Aluno                              |
| ---------- | ---------------------------------- |
| 21/1039288 | Bruno Campos Ribeiro               |
| 21/1039297 | Bruno Martins Valério Bomfim       |
| 20/2016462 | Heitor Marques                     |
| 21/1029352 | Igor e Silva Penha                 |
| 19/0057858 | José Luís Ramos Teixeira           |
| 21/1029405 | Leonardo Gonçalves Machado         |
| 20/2016767 | Lucas Felipe Soares                |
| 20/0062379 | Marcos Vinícius de Deus            |
| 20/2045268 | Raquel T. Eucária Pereira da Costa |
| 21/2002907 | Zenilda Pedrosa Vieira             |

## Sobre
**Cibeelization** é um projeto que visa simular as dinâmicas de uma colmeia de abelhas, onde as abelhas - com classe, funções e comportamento definidos - interagem entre si em pról do desenvolvimento e equilíbrio da sociedade como um todo, criando assim um ecossistema dinâmico e complexo.

O projeto foi desenvolvido na linguagem *Java* e utiliza a tecnologia de sistemas multiagentes (SMA), baseando-se no framework JADE e utilizando o protocolo FIPA para comunicação e troca de mensagens entre os agentes.

![](/docs/assets/beemovie.png)

**Figura 1:** Cena do filme [Bee Movie (2007)](https://en.wikipedia.org/wiki/Bee_Movie).

As classes de agentes presentes no **Cibeelization** incluem:

* **Rainha:** Cria novas operárias periodicamente.
* **Operárias:** Produzem mel/Geleia Real ou atacam intrusos quando detectados.
* **Limpadoras:** Limpam os resíduos gerados pela produção de mel e geleia real pelas abelhas operárias. Tal produção é interrompida quando a quantidade de resíduo atinge um limite máximo.
* **Intruso:** Aparece periodicamente e precisa ser combatido pelas operárias.
* **Zangão:** Ajuda na proteção da colmeia e na reprodução com a rainha.

Mais detalhes sobre os agentes, os requisitos funcionais e os não-funcionais podem ser lidos na íntegra na documentação da [descrição do projeto](docs/descricao-projeto.md).

## Screenshots
<div align='center'>
  <img src="./docs/img/1.png" /><br><br>
  <label><strong> Imagem 1:</strong> Descrição da imagem <br> <strong>Fonte:</strong> <a href="https://github.com/fulano">Fulano de Tal</a>, 2025.</label><br><br><br>
  <img src="./docs/img/2.png" /><br><br>
  <label><strong>Imagem 2:</strong> Descrição da imagem<br> <strong>Fonte:</strong> <a href="https://github.com/fulano"> Fulano de Tal</a> e <a href="https://github.com/deltrano">Deltrano Beltrano</a>, 2025.</label><br><br>
</div>

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

Para executar o projeto, basta executar o makefile através dos seguintes passos.

1. **Certifique-se de ter instalado o projeto corretamente (seção anterior).**

2. **Uma vez dentro do projeto, navegue até a pasta principal:**  
   ```bash
   cd cibeelization/

3. **Execute o makefile:**  
   ```bash
   make

4. **Após finalizar a execução, limpe os arquivos de compilação:**  
   ```bash
   make clean

## Vídeo

## Participações

| Nome do Membro                | Contribuição                                   | Significância da Contribuição para o Projeto (Excelente/Boa/Regular/Ruim/Nula) | Comprobatórios (ex. links para commits) |
| ----------------------------- | --------------------------------------------- | ------------------------------------------- | -------------------------------------- |
| Bruno Campos Ribeiro          |                                               |                                             |                                        |
| Bruno Martins Valério Bomfim  |                                               |                                             |                                        |
| Heitor Marques                |                                               |                                             |                                        |
| Igor e Silva Penha            |                                               |                                             |                                        |
| José Luís Ramos Teixeira      |                                               |                                             |                                        |
| Leonardo Gonçalves Machado    |                                               |                                             |                                        |
| Lucas Felipe Soares           |                                               |                                             |                                        |
| Marcos Vinícius de Deus       |                                               |                                             |                                        |
| Raquel T. Eucaria Pereira da Costa |                                          |                                             |                                        |
| Zenilda Pedrosa Vieira        |                                               |                                             |                                        |

## Outros

Acesse o relato de cada membro do grupo referente às lições aprendidas, percepções, contribuições, fragilidades e trabalhos futuros através dos links abaixo:

- [Bruno Campos Ribeiro](docs/relatos/brunoRibeiro.md)
- [Bruno Martins Valério Bomfim](docs/relatos/brunoMartins.md)
- [Heitor Marques](docs/relatos/heitor.md)
- [Igor e Silva Penha](docs/relatos/igor.md)
- [José Luís Ramos Teixeira](docs/relatos/jose.md)
- [Leonardo Gonçalves Machado](docs/relatos/leonardo.md)
- [Lucas Felipe Soares](docs/relatos/lucas.md)
- [Marcos Vinícius de Deus](docs/relatos/marcos.md)
- [Raquel T. Eucaria Pereira da Costa](docs/relatos/raquel.md)
- [Zenilda Pedrosa Vieira](docs/relatos/zenilda.md)

## Fontes

[1] SERRANO, Milene. **Materiais da Disciplina de Paradigmas de Programação**. Faculdade de Ciências e Tecnologias em Engenharia (FCTE) – Campus Gama, Universidade de Brasília (UnB). Disponível em: [link](https://aprender3.unb.br/). Acesso em: 06 jan. 2025.

[2] FOUNDATION FOR INTELLIGENT PHYSICAL AGENTS. **FIPA** specifications. Disponível em: http://www.fipa.org/. Acesso em: 26 jan. 2025.