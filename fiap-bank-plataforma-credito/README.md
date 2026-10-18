# FIAP Bank - Plataforma de Crédito 💳

Esteira e motor de decisão de crédito do FIAP Bank, **refatorada para DDD** em Maven multi-módulo e com
API **RESTful**, mantendo as rotas legadas por meio de um **Adapter** para que o Angular (porta 4200) e a
Mesa de Operações Thymeleaf (porta 8082) continuem funcionando sem nenhuma alteração.

---

## 🔎 Diagnóstico do estado inicial (o que estava errado, onde e por quê)

| # | Problema | Onde | Por que é um problema |
| :-: | :--- | :--- | :--- |
| 1 | Monólito de 1 módulo: entidade JPA, DTO, serviço, client e controller no mesmo `jar` | `pom.xml` único | Nada impede, fisicamente, que o controller use o repositório; as camadas só existiam por convenção. |
| 2 | **Entidade JPA = modelo de domínio** (`@Entity`/`@Table`/`@Column` em `model.SolicitacaoCredito`) com getters/setters públicos para tudo | `model/SolicitacaoCredito` | Domínio acoplado a Jakarta Persistence; qualquer camada pode colocar a solicitação em estado inválido (`setStatus(APROVADO)`). |
| 3 | **DTO conhece a entidade** (`SolicitacaoCreditoResponseDTO.fromEntity`) | `dto/` | Inversão de dependência ao contrário: a camada de contrato depende do modelo persistente. |
| 4 | **Regra de negócio dentro do serviço junto com infraestrutura**: motor de crédito, repositório JPA, notificação e client Feign no mesmo `PlataformaCreditoService` | `service/` | Regras de negócio não testáveis sem Spring/JPA; responsabilidades misturadas. |
| 5 | `SolicitacaoCreditoRepository extends JpaRepository` usado diretamente pelo serviço | `repository/` | O domínio/aplicação dependem do Spring Data em vez de depender de uma porta. |
| 6 | **Todas as rotas são `POST` com verbo no path** (`/solicitarCredito`, `/listarTodasSolicitacoes`, `/aprovarSolicitacao`...) e até consultas usam `POST` com corpo | `CreditoOperacoesNonRestfulController` | Viola a semântica HTTP (consulta deveria ser `GET`, atualização `PUT`) e o conceito de recurso REST (substantivo + verbo HTTP). Respostas de criação voltam `200` sem `Location`. |
| 7 | `IllegalArgumentException` para "não encontrado" e **nenhum** `@RestControllerAdvice` | `PlataformaCreditoService` | Solicitação inexistente vira **500**; payload inválido não tem tratamento. |
| 8 | **Nenhuma Bean Validation** nos DTOs de entrada | `dto/` | `cpfCliente` nulo gera `NullPointerException` (500); parcelas `0` geram divisão por zero. |
| 9 | **Core Bancário "inventado" no fallback**: se o Core cai, devolve `saldo 12500.00`, conta `12345-6` e `ATIVA` para *qualquer* CPF | `CoreBancarioClientService` | Dado fictício apresentado ao cliente como real. Além disso, o `Map<String,Object>` devolvido pelo Core era repassado cru. |
| 10 | Retorno do Core consultado em `solicitarCredito` e **descartado** | `PlataformaCreditoService` | Chamada de rede inútil que só adiciona latência/falha. |
| 11 | Aprovar/reprovar aceita **qualquer status** (dá para reprovar uma solicitação já aprovada) | `aprovarSolicitacao`/`reprovarSolicitacao` | Regra de transição de estado ausente. |
| 12 | `System.out.println` como log, `@CrossOrigin(origins="*")` por controller | vários | Sem nível de log; CORS espalhado. |

---

## 🧱 Arquitetura (4 módulos Maven)

```
fiap-bank-plataforma-credito/            (pom agregador - packaging pom)
├── fiap-bank-plataforma-credito-domain/          Domínio puro
├── fiap-bank-plataforma-credito-infrastructure/  JPA, Spring Data, Adapters e Clients
├── fiap-bank-plataforma-credito-application/     DTOs, Bean Validation, Services
└── fiap-bank-plataforma-credito-presentation/    Controllers, Adapter legado, @SpringBootApplication
```

Hierarquia de dependências (garantida pelo `maven-enforcer-plugin` em cada módulo):

```
presentation ──► application ──► infrastructure ──► domain
                      └──────────────────────────────►┘
```

| Módulo | Conteúdo |
| :--- | :--- |
| **domain** | `SolicitacaoCredito` (entidade comportamental: `aprovarPelaMesa`, `reprovarPelaMesa`, sem setters), VO `Cpf`, enum `StatusSolicitacao`, `ContaCore`, `DecisaoCredito`, domain service `MotorDeCredito`, **portas**: `SolicitacaoCreditoRepository`, `ScrGateway`, `BureauGateway`, `CrmGateway`, `CoreBancarioGateway`, exceptions de domínio. Só depende de Lombok (provided). |
| **infrastructure** | `SolicitacaoCreditoJpaEntity`, `SolicitacaoCreditoJpaRepository` (Spring Data), `SolicitacaoCreditoRepositoryAdapter`, mapper, `CoreBancarioFeignClient` + `CoreBancarioGatewayAdapter` (OpenFeign), `ScrClientAdapter`, `BureauClientAdapter`, `CrmClientAdapter`. |
| **application** | DTOs em `record` com Bean Validation, `SolicitacaoCreditoService`, `ContaClienteService`, mappers e exceptions de aplicação (espelham as de domínio, para a presentation nunca importar `domain`). |
| **presentation** | `PlataformaCreditoApplication`, `SolicitacaoCreditoController`, `ContaClienteController`, **`CreditoLegacyAdapterController`**, `GlobalExceptionHandler`, `CorsConfig`, `DataInitializer`, `application.yml`. |

Lombok é usado em entidades, services, controllers e adapters (`@Getter`, `@Setter`, `@Builder`,
`@RequiredArgsConstructor`, `@NoArgsConstructor`, `@Slf4j`), configurado via `annotationProcessorPaths` no pom pai.

---

## 🌐 API RESTful nova (`/api/v1`)

| Método | Endpoint | Sucesso | Descrição |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/v1/solicitacoes-credito` | `201` + `Location` | Abre uma solicitação e executa o motor de crédito |
| `GET` | `/api/v1/solicitacoes-credito` | `200` | Lista todas (mais recentes primeiro) |
| `GET` | `/api/v1/solicitacoes-credito?cpf={cpf}` | `200` | Lista as de um cliente |
| `GET` | `/api/v1/solicitacoes-credito/{id}` | `200` | Busca por id |
| `PUT` | `/api/v1/solicitacoes-credito/{id}/decisao` | `200` | Decisão da Mesa. Body: `{"decisao":"APROVADO\|REPROVADO","parecer":"..."}` |
| `GET` | `/api/v1/clientes/{cpf}/conta` | `200` | Conta do cliente no Core Bancário |

Erros (`GlobalExceptionHandler`): `400` payload/CPF inválido · `404` não encontrado · `409` solicitação não está
em análise · `503` Core Bancário indisponível · `500` inesperado.

## 🔁 Retrocompatibilidade: mapa rota legada → serviço

`CreditoLegacyAdapterController` mantém as 7 rotas antigas, **mesmos paths, mesmos payloads, mesmos campos de resposta**,
e delega para os mesmos serviços da API nova (sem regra de negócio duplicada).

| Rota legada (continua funcionando) | Equivalente RESTful |
| :--- | :--- |
| `POST /api/credito/solicitarCredito` | `POST /api/v1/solicitacoes-credito` |
| `POST /api/credito/consultarStatusCredito` | `GET /api/v1/solicitacoes-credito?cpf=` |
| `POST /api/credito/buscarSolicitacaoPorId` | `GET /api/v1/solicitacoes-credito/{id}` |
| `POST /api/credito/listarTodasSolicitacoes` | `GET /api/v1/solicitacoes-credito` |
| `POST /api/credito/aprovarSolicitacao` | `PUT /api/v1/solicitacoes-credito/{id}/decisao` (`APROVADO`) |
| `POST /api/credito/reprovarSolicitacao` | `PUT /api/v1/solicitacoes-credito/{id}/decisao` (`REPROVADO`) |
| `POST /api/credito/consultarClienteCore` | `GET /api/v1/clientes/{cpf}/conta` |

Detalhe importante: o Core novo devolve o campo `status`, mas o Angular lê `statusConta`. O Adapter converte
(`status` → `statusConta`) para o front-end não perceber a mudança.

---

## ⚖️ Decisões de design que merecem registro

- **Transição de estado no domínio:** só dá para aprovar/reprovar solicitação `EM_ANALISE` (senão `409`). A Mesa só exibe
  os botões para `EM_ANALISE`, então nenhuma tela legada é afetada.
- **Core offline não inventa mais dados:** `consultarClienteCore` devolve `503`/`404` em vez de saldo fictício. O Angular
  já trata erro dessa chamada. A abertura de crédito **não** depende mais do Core (a consulta era descartada).
- **SCR, Bureau e CRM** continuam simulados (mesmas regras do legado: CPF terminado em `9` tem restrição; score
  determinístico por CPF; notificação via log), agora atrás de portas do domínio. Trocar por clients reais não toca
  em domain/application.
- **CRM:** o legado chamava de "NotificacaoService"; tratamos como o client do CRM pedido no enunciado.
- **Classe principal na raiz** `br.com.fiap.bank.plataforma`: o scan padrão do Spring Boot enxerga todos os módulos.
- **Seed:** com as regras do legado, Ana e Carlos são aprovados automaticamente e Mariana é reprovada (SCR). Nenhuma
  nasce `EM_ANALISE`; para ver a Mesa agir, crie pelo Angular uma solicitação com parcela > 30% da renda
  (ex.: R$ 50.000 em 12x com renda de R$ 5.000).
- **Spring Cloud 2024.0.0 com Spring Boot 4.1.1:** mantido como no `pom.xml` original do professor. Se o `mvn` acusar
  incompatibilidade do OpenFeign, ajustar `spring-cloud.version` no pom pai para a release train compatível com Boot 4.

---

## 🚀 Como executar

Pré-requisitos: **JDK 21** e **Maven 3.9+**. Suba o Core Bancário (8080) antes.

```bash
cd fiap-bank-plataforma-credito
mvn clean install
mvn -pl fiap-bank-plataforma-credito-presentation spring-boot:run
```

- API: `http://localhost:8081/api/v1/solicitacoes-credito`
- H2 Console: `http://localhost:8081/h2-console` (JDBC `jdbc:h2:mem:plataformacreditodb`, user `sa`, senha vazia)
- URL do Core configurável por `CORE_BANCARIO_URL` (padrão `http://localhost:8080/api/v1`).
