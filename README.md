# Movies Battle

API REST para uma aplicação ao estilo card game, onde serão informados dois
filmes e o jogador deve acertar aquele que possui melhor avaliação no IMDB.

## Sobre o projeto

### Instalação e execução

Os pré-requisitos para execução deste projeto são: JDK (versão 17) e Maven. 

Para rodar, efetuo o clone deste projeto, efetue o build do projeto e execute. 

Segue a lista de comandos: 

```sh
git clone 
cd movie-battle
mvn clean install
java -jar target/movies-battle-0.0.1-SNAPSHOT.jar
```

### Documentação de API

Para mais detalhes sobre as APIs acesse: http://localhost:8080/swagger-ui/index.html

Neste mesmo link você consegue efetuar os testes utilizando o botão "Try it".

### Funcionamento

1. Antes de tudo, é necessário efetuar login utilizando o seguinte método:
```sh
curl --location --request POST 'http://localhost:8080/v1/login' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'username=frbolseiro' \
--data-urlencode 'password=123'
```
A resposta deste método traz um "user token". Informe este código sempre no header "x-usertoken" das chamadas seguintes.

Foram criados dois usuários de testes: frbolseiro e legvfolha. A senha para ambos é "123".

2. Para iniciar uma partida, utilize o seguinte método:
```sh
curl --location --request POST 'http://localhost:8080/v1/match?action=start' \
--header 'x-usertoken: b554a8a7-1d6d-4e6f-8432-a5b882cb5433'
```
A resposta trará um código de Match. Utilize para interagir nas próximas chamadas.

3. Próximo passo é obter os dados do próximo quiz. Isso é obtido pelo seguinte método:
```sh
curl --location --request GET 'http://localhost:8080/v1/match/1/quiz' \
--header 'x-usertoken: b554a8a7-1d6d-4e6f-8432-a5b882cb5433'
```
Como resposta, você terá a descrição de dois filmes: movieTitleLeft e movieTitleRight. 

4. Para aplicar um palpite de qual filme tem o melhor score no IMDB, utilize o seguinte comando:
```sh
curl --location --request POST 'http://localhost:8080/v1/match/1/quiz?choice=right' \
--header 'x-usertoken: b554a8a7-1d6d-4e6f-8432-a5b882cb5433'
```
Informe no atributo `choice` as opções: `LEFT` ou `RIGHT`.

Como resposta, você verá se o palpite foi correto ou não pelo atributo `correct`.

5. Repita os passos 3 e 4 para novas rodadas na partida. 

6. A partida encerra de duas formas: Quando o usuário atingir três erros ou a qualquer momento ao chamar o seguinte método:
```sh
curl --location --request POST 'http://localhost:8080/v1/match?action=terminate' \
--header 'x-usertoken: b554a8a7-1d6d-4e6f-8432-a5b882cb5433'
```

7. Para saber o ranking dos usuários, acesse:
```sh
curl --location --request GET 'http://localhost:8080/v1/ranking?top=10'
```

### Observações

Utilizamos uma base de dados de filmes brasileiros, extraído de https://www.kaggle.com/rounakbanik/the-movies-dataset/version/7?select=movies_metadata.csv

O score IMDB é obtido em tempo de execução, no momento que é gerado um quiz, por meio da chamada da API http://www.omdbapi.com/.

### Tecnologias

Estamos utilizando neste projeto:
* Java (versão 17);
* Maven para Gerenciamento das Dependências;
* Banco de dados H2 _embebed_na aplicação;
* Spring Boot para execução do container Spring.
* Spring Web para exposição da API;
* Spring Data JPA para mapeamento Objeto Relacional;
* Filter para a camada de autenticação;
* Rest Template para consumo de API externa.

__Está no nosso roadmap a criação de testes unitários e integrados para as APIs__.

## Teoria 

### Interfaces

Se você olhar atentamento o código, poderá perceber que há "classes" Java que possuem apenas a definição dos métodos, mas sem a implementação.
Podemos pegar como exemplo o arquivo `src\main\java\letscode\challenge\moviesbattle\repositories\MovieRepository.java`:

```java
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Integer findTopByOrderByIdDesc();
}
```
Você deve estar se perguntando: Como o Java sabe o que ele deve executar neste método, uma vez que não existe código ali? _Qual é a mágica por trás disto_?

O que você está observando não são classes, mas trata-se de outro conceito de Programação Orientada a Objetos chamado de __Interfaces__.
Nós utilizamos Interfaces quando queremos atribuir características ou capacidades às nossas classes.

> __Para ilucidar o conceito!!!__
> 
> Vários animais possuem a capacidade de "emitir som", mas cada animal (ou grupo de animais) emitim sons à sua maneira: Os cachorros "latem", os gatos "miam", os leões "rugem", os pássaros "piam" etc.
Em outras palavras: Trata-se da mesma capacidade, mas o modo como cada animal o faz é diferente.

Em POO, temos a interface que define a capacidade, mas é papel da classe definir o que é preciso para atender àquela capacidade (ou melhor dizendo, _implementar_).

Um exemplo prático: Se quisermos que os objetos que geramos tenham uma capacidade de gerar um arquivo a partir dos valores, podemos fazer com que a classe implemente a interface `Serializable`.

Assim, não existe mágica: A interface sempre é utilizada por alguma classe, que precisa definir a implementação para àquele comportamento.
No exemplo acima, o framework "Spring Data Repositories" contém a implementação para o comportamento de cada método que criamos dentro de uma interface do tipo `Repository`. Para saber mais a respeito, acesse: https://docs.spring.io/spring-data/data-commons/docs/current-SNAPSHOT/reference/html/#repositories

### Injeção de Dependências

Vamos a outro conceito que você deve estar imaginando que é mágica. 

Obserse o seguinte trecho de código, extraído de `src\main\java\letscode\challenge\moviesbattle\controllers\UserController.java`:
```java
public class UserController {
    
    private final RankingService rankingService;
    
    public UserController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping
    public List<UserRanking> listAll(@RequestParam Short top) {
        return rankingService.getUserRanking(top);
    }
}
```
Se você procurar em nosso código, não verá ninguém instanciando a classe `UserController`, tampouco informando um valor para o parâmetro do construtor `rankingService`.

_Como este código funciona, sem lançar um NullPointerException?_

Embora em nosso código não haja ninguém instanciando de maneira literal, tem alguém que instancia por nós: Trata-se da __Injeção de Dependências__ do Container Spring (também conhecido como Spring IoC Container).

> __Para ilucidar o conceito!!!__
>
> Todos os dias você acorda, troca de roupa e vai até a padaria em frente de casa para comprar pão para o seu desjejum. Agora imagine que, ao acordar, a padaria entregasse o pão quentinho todos os dias na sua casa. Qual dos dois cenários vocês escolheria?  

O Container nos auxilia entregando os objetos corretos para nossa utilização, sem que nos preocupemos em como instanciar as classes. E para isso ele conta com as referências que informamos utilizando _Anotações_.

Repare que na classe RankingService nós informamos a anotação `@Service` à nível de classe. Desta maneira o container vai instância e entregar um objeto de RankingService para qualquer outra classe que precisar utilizar.

Se removermos a anotação, obtemos o seguinte erro ao subir a aplicação:
```
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 4 of constructor in letscode.challenge.moviesbattle.services.MatchService required a bean of type 'letscode.challenge.moviesbattle.services.RankingService' that could not be found.


Action:

Consider defining a bean of type 'letscode.challenge.moviesbattle.services.RankingService' in your configuration.
```
Esta descrito que MatchService necessita de um objeto do tipo RankingService, mas que não encontrou. Além disto, ele sugere para configurarmos RankingService como um "bean".

"Beans" no Spring são objetos gerenciados pelo Spring Container.

A utilização de injeção de Dependências auxiliam muito a organização do código-fonte, pois remove a complexidade de instanciação dos objetos. Desta forma, podemos manter o código limpo apenas com as definições da classe.

### Spring Web

Já que estamos falando de Container Sprint, vamos falar de outro framework que auxilia muito o nosso trabalho: Spring Web.

Em nosso projeto utilizamos Spring Web para criar a API Rest. 

Segue um exemplo de utilização, extraído da classe `src\main\java\letscode\challenge\moviesbattle\controllers\MatchController.java`:
```java
@RestController
@RequestMapping("/v1/match")
public class MatchController {
    @PostMapping
    public ResponseEntity<MatchResponse> startOrTerminateMatch(@RequestParam String action, @RequestHeader("x-usertoken") String userToken) {
        // ...
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> getMatchById(@PathVariable Long id) {
        // ...
    }
}
```

O Spring Web nos auxilia em criar um ponto de junção (também conhecido como _binding_) entre a API que expõe métodos HTTP e o nosso código Java.

Desta forma, se a aplicação consumidora enviar uma chamada HTTP para o endereço `POST http://<HOST>/v1/match`, o container vai identificar a ação e efetuar uma chamada ao método `startOrTerminateMatch` de `MatchController`.

Além disto, o container também se encarrega da transformação dos dados que foram enviados (normalmente no formato JSON) para um objeto Java. 

O mesmo também acontece na resposta: Ao efetuar um `return` dentro do método, o container vai se encarregar de montar uma resposta HTTP e enviar de volta para a aplicação consumidora. Também efetua a conversão de objetos Java para texto caso necessário. 

Contudo o Spring Web não se limita somente a API Rest. É possível ainda criar APIs de outros formatos (como SOAP ou GraphQL) ou criação de páginas, utilizando Spring Web MVC.

A utilização do Spring Web facilita (e muito) a tarefa do desenvolvedor, uma vez que pode focar na implementação da lógica de negócio, sem se preocupar na complexidade do protocolo HTTP.

_Se você chegou até aqui, agradeço muito a oportunidade de apresentar um pouco o meu trabalho e espero nos falar em breve. Até mais!_