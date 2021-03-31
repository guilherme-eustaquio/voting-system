# voting-system

A aplicação foi escrita em Java 8 com as frameworks Spring Boot, Spring Boot Webflux e Spring Data Reactive.

Para executar a aplicação, é necessário:
 - Gradle
 - Docker

. No diretório raiz, para compilar o projeto executa-se: gradle clean build
. Para executar o projeto, abra o terminal aberto e execute o comando sem aspas "docker-compose up"

. A documentação e acesso a api se encontra: http://localhost:8080/swagger-ui.html
	
. A modelagem do banco de dados se encontra no diretório attachments
	- Vale notar que o spring data reactive não suporta a referência entre entidades (@DBRef), no entanto, foi colocado uma chave
	nas entidades e validações que garantem a consistência dos dados.
	- A collection associated se refere ao participante da pauta.
	- A collection theme se refere a pauta em si (a pergunta).
	- A collection session se refere a sessão de votação (definida pelo tempo) e referenciada pela theme
	- A collection associated_session se refere as respostas do associado
	- A collection answer_type se refere aos tipos de respostas disponíveis pelo associado (Sim e Não)

. O serviço de validação de cpf foi desenvolvido em nodeJS e pode ser acessado neste repositório: https://github.com/guilherme-eustaquio/cpfvalidator e o link do serviço mesmo é: http://youhome.herokuapp.com/users/{cpf}
	- O serviço, pelo fato de ser do heroku free, pode demorar a ter uma resposta pelo fato do mesmo
	dormir caso não for utilizado por um intervalo de tempo. No entanto, a aplicação Voting System não é
	bloqueada enquanto espera esta resposta.

. Os serviços do Voting System são assíncronos



