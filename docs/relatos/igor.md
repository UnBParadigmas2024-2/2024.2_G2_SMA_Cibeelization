# Igor e Silva Penha - 211029352

## Lições Aprendidas
A principal lição aprendida foi a compreensão da autonomia dos agentes, a qual permite eles tomarem decisões independentes apenas analisando a situação e objetivo. Esse entendimento foi fundamental para compreender o funcionamento de um sistema multiagente, em que cada agente toma suas próprias decisões, o que influencia os demais agentes e tornando o comportamento do sistema como um todo menos previsível.

Além disso, aprendi sobre os desafios de escalabilidade em sistemas multiagentes. A inserção de um novo agente não adiciona só novas interações e comunicações diretas entre eles, mas também pode impactar agentes que sequer têm contato direto com ele. Como no caso do zangão, ele afetava diretamente a rainha e o intruso, porém afetava indiretamente as operárias e limpadoras, pois sem ele acasalando com a rainha ela não seriam criadas. Dessa forma, esse efeito em cadeia destaca a complexidade de gerenciar sistemas com grande número de agentes interconectados.

Outra lição importante foi entender as regras e dinâmicas de uma colmeia como inspiração para sistemas multiagentes. Porém percebi que os comportamentos emergentes podem desviar significativamente dos resultados planejados. Portanto, mesmo com regras bem definidas, o comportamento de sistemas complexos nem sempre é previsível, o que trouxe a necessidade de alguns vários testes para definir a quantidade de abelhas geradas pela rainha no voo, de zangão, quantidade de mel e geleia real inicial, periodicidade das limpadoras limparem e que o voo nupcial acontecer entre outros.

## Percepções

Acredito que o semestre atípico trouxe dificuldades adicionais devido ao desgaste acumulado, o que impactou o ritmo do trabalho. Além disso, a organização do trabalho em equipe foi desafiadora, já que não era trivial paralelizar o projeto. Toda alteração/criação de um agente influenciava os demais, gerando potenciais conflitos que precisavam ser cuidadosamente resolvidos. Trabalhar com um paradigma novo em uma linguagem com a qual não pratico demandou esforço extra.

Inicialmente, a complexidade do projeto era reduzida, mas, à medida que ele evoluía e ganhava mais sentido, o nível de trabalho necessário aumentava e trazia desafios inesperados. Outro aspecto positivo foi a oportunidade de nos inspirarmos em fenômenos da vida real.  

Desenvolver um projeto de Sistemas Multiagentes foi, sem dúvida, desafiador, mas também recompensador. Esse processo exigiu uma compreensão profunda das interações locais e seus impactos globais, além de uma constante busca por criatividade e rigor técnico. As percepções adquiridas ao longo do projeto destacaram não apenas a riqueza do paradigma SMA, mas também o seu imenso potencial para aplicações práticas e soluções criativas.


## Contribuições e Fragilidades

Embora não tenha iniciado o trabalho base junto com Bruno e Lucas, me esforcei para compreender o que estava sendo feito e como o projeto estava estruturado. Naturalmente, acabei contribuindo em diversas partes do projeto e assumi uma posição de liderança dentro do grupo. Fato devido ao pelo meu engajamento com o projeto, aprofundamento nos conhecimentos tanto de programação quanto de colmeias :) e visão mais ampla do sistema como um todo. Além disso, dediquei-me a produzir códigos indispensáveis para o funcionamento do projeto, garantindo a integração e o alinhamento entre suas diferentes partes.

Apesar de a maioria dos meus commits estarem como co-author, isso se deu pelo fato de eu trabalhar em várias frentes simultaneamente. Colaborei com Bruno Martins na parte da limpadora e na correção de bugs, além de atuar com Leonardo na parte central, incluindo o voo nupcial e a "rinha das rainhas" e outros. Muitas alterações foram realizadas diretamente nos computadores deles, e não no meu, o que também contribuiu para essa dinâmica de co-author.

Uma das fragilidades encontradas no projeto foi a utilização de variáveis globais. Abordagem escolhida devido à falta de tempo e por ser a solução mais simples. No entanto, essa está longe de ser a solução ideal e pela iminência dos deadlocks, o que comprometeu a robustez da implementação, e a refatoração para uma solução mais estruturada acabou sendo postergada.

##  Trabalhos Futuros

- Implementar uma solução utilizando singleton, por exemplo;
- Ampliar o sistema para fora da colmeia, como a interação das abelhas com as flores e a polinização;
- Ampliar o sistema dentro da colmeia, como a criação das larvas e desenvolver sua especialidade, podendo ser operária, rainha, zangão;
- Melhorar a interação com o intruso;
- Adicionar mais interações entre os agentes;
- Interface com o usuário ser mais interativa, por exemplo deixar ele escolher a quantidade de mel produzida para parar, aparições de intrusos e por ai vai.
