# Rest with Spring Boot and Java 
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/RenanClaude/rest-with-spring-boot-and-java/blob/main/LICENSE) <br/>
[![Docker Hub Repo](https://img.shields.io/docker/pulls/renanc/rest-with-spring-boot.svg)](https://hub.docker.com/repository/docker/renanc/rest-with-spring-boot)

# Sobre o projeto

Rest with Spring Boot and Java é uma aplicação full stack web, com foco total no back-end, construída durante o curso [REST API's RESTFul do 0 à AWS c. Spring Boot 3 Java e Docker](https://www.udemy.com/course/restful-apis-do-0-a-nuvem-com-springboot-e-docker/) na plataforma Udemy.

A aplicação consiste em disponibilizar uma API RESTful capacitada para ter usuários e acesso a registro de livros e de pessoas no banco de dados. As principais funcionalidades são Login/Logout, paginação, criar, ler/recuperar, atualizar e deletar registros de pessoas e de livros.
Obs: Atualmente o front-end disponibiliza tais funcionalidades de 'CRUD' (create, read, update, delete) apenas para livros, havendo também a possiblidade de login e logout.

## Layout web
![Tela de login](https://github.com/RenanClaude/assets/blob/main/rest-spring-boot-tela-de-login.png)

![Tela da lista de livros](https://github.com/RenanClaude/assets/blob/main/rest-spring-boot-tela-de-livros.png)

![Tela da lista de adicionar livro](https://github.com/RenanClaude/assets/blob/main/rest-spring-boot-tela-de-adicionar-livro.png)

![Tela da lista de atualizar livro](https://github.com/RenanClaude/assets/blob/main/rest-spring-boot-tela-de-atualizar-livro.png)

# Tecnologias utilizadas
## Back end
- Java
- Spring Boot
- JPA / Hibernate
- Spring Security
- JWT
- HATEOAS
- Dozer
- Mockito
- JUnit
- Maven
- Flyway
- MySQL
- Docker
- GitHub Actions
## Front end
- HTML / CSS / Javascript
- ReactJS
## Implantação no Docker Hub
- [Imagem no Docker Hub](https://hub.docker.com/repository/docker/renanc/rest-with-spring-boot/general)

# Como executar o projeto

## Back end
Pré-requisitos: Java 17

```bash
# clonar repositório
git clone https://github.com/RenanClaude/rest-with-spring-boot-and-java.git

# entrar na pasta do projeto back end
cd rest-with-spring-boot-and-java/
cd FirstStepsInJavaWithSpringBoot/
cd rest-with-spring-boot-and-java/


# executar o projeto
mvn spring-boot:run

```

## Front end web
Pré-requisitos: npm

```bash
# clonar repositório
git clone https://github.com/RenanClaude/rest-with-spring-boot-and-java.git

# entrar na pasta do projeto front end web
cd rest-with-spring-boot-and-java/
cd FirstStepsInJavaWithSpringBoot/
cd frontend-with-react-and-javascript/
cd client/

# instalar dependências
npm install

# executar o projeto
npm start
```

# Autor

Renan Claude Grossl

https://www.linkedin.com/in/renan-claude-dev/
