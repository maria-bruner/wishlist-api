# Wishlist API

Este projeto é uma API de **lista de desejos (wishlist)** para um e-commerce, desenvolvida em **Spring Boot** com **MongoDB**.

Ele permite que os clientes adicionem, removam, listem e verifiquem produtos em suas listas de desejos. A aplicação conta com monitoramento de performance via **Prometheus** e **Grafana** para acompanhamento em tempo real.

---

## Funcionalidades

- *Adicionar item à wishlist*: Coloque um produto desejado na sua lista (com limite de 20 itens por cliente).
- *Remover item da wishlist*: Remove um produto da sua lista de desejos.
- *Listar todos os itens da wishlist*: Veja todos os produtos em sua lista.
- *Verificar se um item está na wishlist*: Verifique se um produto já foi adicionado à lista.
- *Validações de regras de negócio*: O sistema respeita as regras do e-commerce, como o limite de itens na lista.
- *Inserção automática de 25 produtos*: 25 produtos iniciais são automaticamente inseridos ao subir a aplicação.
- *Testes unitários*: Garantia de qualidade com cobertura dos principais casos de uso.
- *Monitoramento via Prometheus e Grafana*: Acompanhamento das métricas da aplicação.

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**: A base do nosso projeto.
- **MongoDB**: Banco de dados NoSQL.
- **Docker & Docker Compose**: Para orquestrar e gerenciar os serviços.
- **JUnit 5 & Mockito**: Testes unitários para garantir a qualidade.
- **Arquitetura Hexagonal**: Organiza o código para facilitar a manutenção e separa responsabilidades.
- **Prometheus & Grafana**: Monitoramento de métricas de performance.
- **Spring Data MongoDB**: Integração simplificada com o MongoDB.
- **Migrations MongoDB**: Fornece dados iniciais.
- **Swagger**: Documentação das API's Rest e interface para facilitar os testes.

---

## Como Rodar o Projeto com Docker

### Pré-requisitos

- *Docker* e *Docker Compose* instalados.

### Subindo os Containers

1. Clone o repositório:

```bash
    git clone https://github.com/maria-bruner/wishlist-api.git
```

2. Execute o comando abaixo para subir a aplicação com todos os serviços:

```bash
    docker-compose up --build
```

Isso irá:
- Baixar as imagens necessárias.
- Construir a aplicação.
- Subir os serviços (MongoDB, API, Prometheus e Grafana).

---

## Informações do projeto

### Estrutura do Documento Wishlist
Este documento é responsável por somente referenciar informações de produto e o usuário. Não sendo responsável por guardar nenhuma informação de disponibilidade de produto, valor ou quantidade. Seguindo essa linha, a sua estrutura ficou nesse formato:
* wishlist_id: responsável por ser o identificador único.
* user_id: responsável por guardar o identificador do usuário.
* product_id: responsável por guardar o identificador do produto.
* product_name: responsável por guardar o nome do produto.
* product_value: responsável por guardar o valor do produto.
* url_image: responsável por guardar a url de imagem do produto.

### Monitoramento com Prometheus e Grafana

#### Prometheus

- Acesse o Prometheus no seguinte endereço:

    
    http://localhost:9090
    

- O arquivo prometheus.yml já está configurado corretamente no projeto para coletar as métricas da aplicação.

#### Grafana

- Acesse o Grafana no seguinte endereço:

    
    http://localhost:3000
    

- *Usuário padrão*: admin
- *Senha padrão*: admin

*Importante*: Ao acessar o Grafana pela primeira vez, será solicitado que você altere a senha padrão.

1. Depois de logar, clique em *Dashboards* no menu lateral.
2. Procure pelo dashboard chamado *"Overview Wishlist"*.
3. Para que o Grafana comece a exibir dados, interaja com a API usando o Swagger (ver abaixo) para adicionar, remover ou listar itens. O Grafana coleta as métricas baseadas nas interações da API.

#### Testando a API com Swagger

A API expõe seus endpoints através do *Swagger*, que oferece uma interface interativa para testar as funcionalidades.

- *URL do Swagger*: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Você pode acessar essa URL no navegador e testar os endpoints da API de forma fácil.

#### Testes
- Os testes estão localizados dentro de src/test. Foram escritos utilizando:

##### JUnit 5

- Mockito para mocks e simulações
- Testes focados na lógica de negócio dos casos de uso
- Execute os testes com:

```bash
./mvnw test
```

#### docker-compose.yml
O sistema completo é orquestrado com Docker Compose e contém os seguintes serviços:
* mongo: Banco de dados MongoDB
* wishlist-api: API Spring Boot
* prometheus: Monitoramento das métricas
* grafana: Visualização das métricas

---
