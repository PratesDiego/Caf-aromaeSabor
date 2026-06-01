# Café Aroma e Sabor

Este repositório contém uma aplicação web simples para gerenciar produtos e movimentações de estoque de uma cafeteria, construída com Spring Boot, Thymeleaf, Spring Data JPA e Spring Security.

> Observação: este README descreve o estado atual do projeto, como executar localmente, endpoints disponíveis e dicas para desenvolvimento/testes.

---

## Sumário

- Visão geral
- Funcionalidades
- Estrutura do projeto
- Tecnologias
- Como rodar (MySQL)
- Executando com H2 (modo rápido / testes)
- Endpoints e páginas
- Usuários de teste (credenciais)
- Notas sobre segurança e CSRF
- Problemas comuns e como debugar
- Boas práticas e próximos passos
- Contribuição

---

## Visão geral

Aplicação de demonstração para gerenciar o catálogo de produtos e registrar movimentações de estoque (entrada/saída). Possui telas para listar, inserir, alterar e excluir produtos, e para registrar movimentações de estoque.

A interface é construída com Thymeleaf e os dados são persistidos via Spring Data JPA. A autenticação utiliza Spring Security com usuários em memória por padrão.

---

## Funcionalidades

- Autenticação (Spring Security) com usuários em memória.
- Listagem de produtos.
- Inserção de novos produtos (formulário).
- Edição de produtos existentes.
- Exclusão de produtos.
- Registro e listagem de movimentações de estoque (entrada/saída).
- Templates Thymeleaf com recursos estáticos (CSS em `static/css/styles.css`).
- Mensagens de sucesso/erro exibidas nas views.

---

## Estrutura do projeto (resumida)

- `src/main/java` - código fonte Java (controllers, models, config, repositories)
- `src/main/resources/templates` - templates Thymeleaf
- `src/main/resources/static` - recursos estáticos (CSS/JS/imagens)
- `src/main/resources/application.properties` - configurações (DB, JPA)

Principais classes/folders:
- `com.example.demoCafeAromaESabor.Controller` – controllers (Home, Produto, Estoque, Login, etc.)
- `com.example.demoCafeAromaESabor.model` – entidades (Produto, MovimentacaoEstoque, Usuario)
- `com.example.demoCafeAromaESabor.repository` – Spring Data repositories
- `com.example.demoCafeAromaESabor.config.SecurityConfig` – configuração do Spring Security

---

## Tecnologias

- Java 21
- Spring Boot
- Spring MVC + Thymeleaf
- Spring Data JPA (Hibernate)
- Spring Security
- MySQL (configurado por padrão)
- H2 (opcional, para testes locais)
- Maven

---

## Como rodar (MySQL — padrão do projeto)

1. Garanta que você tenha Java 21 e Maven instalado (ou use os scripts `mvnw`/`mvnw.cmd` incluídos).
2. Configure o MySQL local conforme `src/main/resources/application.properties` (padrão do projeto):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cafe_aroma_e_sabor?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=Senai@2024
spring.jpa.hibernate.ddl-auto=update
```

3. No diretório do projeto (onde está `mvnw.cmd`) execute:

```powershell
cd "C:\Users\TIDEV41\Documents\Diego\3 semestre\demoCafeAromaESabor"
.\mvnw.cmd spring-boot:run
```

4. Abra no navegador:

- Home: `http://localhost:8080/home`
- Login: `http://localhost:8080/login`
- Produtos (listagem): `http://localhost:8080/produto`
- Inserir produto: `http://localhost:8080/produto/form-inserir`

---

## Executando com H2 (modo rápido / testes)

Se não quiser depender do MySQL local, substitua temporariamente as propriedades de datasource por estas (por exemplo criando `application-h2.properties` ou editando `application.properties` para testes):

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

Em seguida rode a aplicação como no passo anterior. O console H2 ficará disponível em `/h2-console` se habilitado.

---

## Endpoints e páginas

- GET `/home` — página inicial
- GET `/login` — tela de login
- GET `/produto` — listagem de produtos
- GET `/produto/form-inserir` — formulário inserir produto
- POST `/produto` — salvar novo produto
- GET `/produto/form-alterar?id={id}` — formulário de alteração
- POST `/produto/alterar` — atualizar produto
- GET `/produto/deletar?id={id}` — excluir produto
- GET/POST `/estoque` — páginas de movimentação de estoque (registro/listagem)

Observação: alguns formulários incorporam o token CSRF e templates usam Thymeleaf extras para controle de visibilidade de elementos (login/logout condicional).

---

## Usuários de teste (definidos em `SecurityConfig`)

- admin / adminpass (roles: USER, ADMIN)
- user / userpass (roles: USER)

A tela de login exibe essas credenciais para facilitar testes.

---

## Notas sobre segurança e CSRF

- A aplicação usa Spring Security. Todos os formulários POST devem enviar o token CSRF para serem aceitos. Os templates foram atualizados para incluir o campo hidden do token:

```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" />
```

- Por solicitação durante o desenvolvimento, as rotas de `/produto` foram permitidas para acesso anônimo para facilitar teste da interface. Em produção é recomendado:
  - Permitir apenas GET anônimo (listar/visualizar) e exigir autenticação para POST/DELETE; ou
  - Exigir autenticação para todas operações sensíveis.

Exemplo recomendado (conceitual) — permitir GETs publicamente e exigir autenticação para operações de escrita:

```java
.requestMatchers(HttpMethod.GET, "/produto", "/produto/**").permitAll()
.requestMatchers("/produto", "/produto/**").authenticated() // para POST/PUT/DELETE
```

Se quiser, eu aplico essa restrição para você.

---

## Problemas comuns e solução rápida

1. Whitelabel Error / HTTP 500 ao abrir `/produto`:
   - Causa comum: template chama um método que não existe na entidade (`produto.isEstoqueBaixo()` por exemplo). Solução aplicada: o método foi adicionado à entidade `Produto`.

2. Erro 403 ao submeter formulário (Salvar produto):
   - Causa: falta do token CSRF no formulário. Solução: os formulários foram atualizados para incluir o input hidden com `${_csrf.token}`.

3. Erro ao salvar produto por campo ausente:
   - Se a entidade define um campo como NOT NULL e o formulário não envia esse valor, a persistência pode falhar. Ex.: `quantidadeMinima` foi adicionado e o controller define um valor padrão se não informado.

4. Erro de conexão com banco (Hibernate/MySQL):
   - Verifique se o MySQL está rodando, se as credenciais estão corretas e se o schema existe (ou permita `createDatabaseIfNotExist=true`). Para testes use H2.

5. Mensagem `Missing required 'lang' attribute` em templates:
   - Aviso HTML — não impede execução. Pode ser corrigido adicionando `lang="pt-br"` ao elemento `<html>` nos templates.

---

## Boas práticas / próximos passos recomendados

- Validar entradas com annotations (`@NotNull`, `@Size`, `@DecimalMin`) e usar `@Valid` no controller.
- Restringir operações de escrita a usuários autenticados (mudar SecurityConfig para permitir apenas GETs publicamente).
- Extrair header/footer para fragments Thymeleaf para reduzir duplicação.
- Implementar tratamento global de exceções (`@ControllerAdvice`) e páginas de erro amigáveis.
- Adicionar testes unitários de controller e integração de repositório.

---

## Contribuição

1. Fork o repositório
2. Crie uma branch com sua feature: `git checkout -b minha-feature`
3. Faça commits claros e envie um pull request

---

## Licença

Este projeto é para fins educacionais/demonstração. Se quiser, eu adiciono uma licença (MIT/Apache2) ao repositório.

---

Se quiser, eu salvo este arquivo como `README.md` no repositório (já foi atualizado).
