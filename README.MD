# Voting System 

Sistema de votação de pautas desenvolvido em Java 8 com Spring Boot, Spring Data Reactive MongoDB, Spring WebFlux, JUNIT 5, Docker, Docker-compose e Rabbit MQ.

# Sobre o sistema

O sistema permite criar uma pauta para que seja realizada uma sessão de tempo limitada para votação simultânea e publicar o seu resultado após o tempo fornecido da sessão.

## Tecnologias utilizadas

 - Spring Boot 4.4
 - Spring WebFlux
 - Spring Data Reactive com MongoDB
 - Docker
 - Docker-compose
 - Rabbit MQ

## Diagrama do banco de dados

<a href="https://ibb.co/7KsnPJG"><img src="https://i.ibb.co/6FjHzR4/diagrama-banco-de-dados-votingsystem.png" alt="diagrama-banco-de-dados-votingsystem" border="0"></a><br /><a target='_blank' href='https://pt-br.imgbb.com/'></a><br />

## Execução

Para executar o projeto é necessário o [gradle 6.4.1](https://docs.gradle.org/6.4.1/release-notes.html) ou similar e [Docker](https://docs.docker.com/engine/install/ubuntu/). 

Para construir o projeto, na sua raiz, execute no terminal:

    gradle clean build

Para executá-lo:
	

    docker-compose up --build

Para acessar a documentação aplicação, basta acessar http://localhost:8080/swagger.html

Para acessar o RabbitMQ, basta acessar http://localhost:15672

    usuario:guest, senha:guest

## Arquitetura

Foi utilizado o WebFlux para garantir que as requisições de  clientes sejam assíncronas. O Spring Data Reactive acessa o banco de dados MongoDB com o  mesmo intuito: garantir que não exista ações bloqueantes no sistema.

Retirado do site https://subscription.packtpub.com/book/application_development/9781788995979/5/ch05lvl1sec40/spring-mvc-versus-webflux
![retirado do site https://subscription.packtpub.com/book/application_development/9781788995979/5/ch05lvl1sec40/spring-mvc-versus-webflux](https://static.packt-cdn.com/products/9781788995979/graphics/d2af6e5b-5d26-448d-b54c-64b42d307736.png)

No entanto, a modelagem do sistema em si é basicamente MVC, isto é, temos as camadas repository, service e controller. Há também a camada validation destinada a realizar validações de entradas, violações de chaves, etc.

## Rabbit MQ
O RabbitMQ foi utilizado ao criar uma sessão no backend. Ao criar uma nova sessão (/sessions do tipo POST), uma thread é criada e suspensa de acordo com o tempo fornecido na criação da sessão. Assim que o tempo for atingido, a thread paralelamente é ativada e é enviado a mensagem via RabbitMQ.
