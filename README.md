# Mural Comunitario

## Objetivo

O **Mural Comunitario** e uma aplicacao web para registro e divulgacao de comunicados importantes entre moradores de um bairro urbano. A plataforma permite cadastrar, listar, editar, excluir e acompanhar avisos sobre abastecimento de agua, seguranca, infraestrutura, iluminacao publica, coleta de lixo e outras ocorrencias relevantes para a comunidade.

## Relacao com projeto extensionista

Este sistema foi pensado como apoio a um **projeto extensionista**, fortalecendo a comunicacao comunitaria por meio de tecnologia acessivel e centralizada. A proposta e aproximar moradores, liderancas locais e agentes comunitarios, facilitando o compartilhamento de informacoes do dia a dia do bairro e incentivando participacao social, organizacao coletiva e resposta mais rapida a problemas urbanos.

## Tecnologias usadas

- React
- Vite
- Java
- Spring Boot
- MySQL
- Docker
- Docker Compose

## Estrutura de pastas

```text
/mural-comunitario
  /frontend
  /backend
  docker-compose.yml
  README.md
```

## Como rodar o projeto com Docker

### Pre-requisitos

- Docker instalado
- Docker Compose habilitado

### Executar a aplicacao

Dentro da pasta `mural-comunitario`, rode:

```bash
docker compose up --build
```

Esse comando:

- sobe o banco MySQL
- compila e inicia o backend Spring Boot
- inicia o frontend React com Vite

## Como acessar

- Frontend: http://localhost:5173
- Backend: http://localhost:8081/api/comunicados
- MySQL: localhost:3306

## Como parar

Para encerrar os containers:

```bash
docker compose down
```

## Como subir em producao

Pre-requisitos no servidor:

- Docker instalado
- Docker Compose habilitado
- DNS `microcorelabs.com.br` apontando para o IP publico do servidor
- Portas 80 e 443 liberadas no firewall

Para subir a aplicacao em producao:

```bash
./startprod.sh
```

Esse comando cria automaticamente o arquivo `.env.prod` com senhas aleatorias, compila as imagens, sobe MySQL, backend, frontend estatico e Caddy como proxy reverso HTTPS.

Depois acesse:

```text
https://microcorelabs.com.br
```

Para ver o estado dos containers:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml ps
```

Para parar a producao sem apagar dados:

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yml down
```

## Como apagar os dados do banco

Para remover os containers, a rede e tambem o volume persistente do MySQL:

```bash
docker compose down -v
```

## Endpoints da API

Base URL:

```text
http://localhost:8081/api/comunicados
```

- `GET /api/comunicados`
  Lista todos os comunicados.

- `GET /api/comunicados/{id}`
  Busca um comunicado pelo identificador.

- `POST /api/comunicados`
  Cria um novo comunicado.

- `PUT /api/comunicados/{id}`
  Atualiza os dados de um comunicado existente.

- `DELETE /api/comunicados/{id}`
  Remove um comunicado.

- `GET /api/comunicados/categoria/{categoria}`
  Filtra comunicados por categoria.

- `GET /api/comunicados/status/{status}`
  Filtra comunicados por status.

- `POST /api/comunicados/{id}/resolver`
  Atualiza o status do comunicado para `Resolvido`.

## Dados iniciais

Ao iniciar a aplicacao com o banco vazio, o backend insere automaticamente comunicados de exemplo com situacoes realistas de Rio Branco/AC, como:

- falta de agua em bairro residencial
- buraco em avenida principal
- problema de iluminacao publica
- alerta de seguranca em rua do bairro
- atraso na coleta de lixo

Isso facilita demonstracoes, testes e capturas para apresentacao academica.

## Prints sugeridos para o relatorio academico

- Tela inicial completa com cabecalho, formulario e lista de comunicados.
- Cadastro de um novo comunicado preenchido no formulario.
- Lista de comunicados com diferentes categorias e status.
- Exemplo de filtro por categoria.
- Exemplo de filtro por status.
- Comunicados apos uso da acao "Marcar como resolvido".
- Containers em execucao no terminal com `docker compose up --build`.
- Requisicao da API funcionando em ferramenta como navegador, Postman ou Insomnia.

## Possiveis melhorias futuras

- Autenticacao de usuarios e perfis de acesso.
- Upload de imagens para anexar evidencias aos comunicados.
- Busca por palavra-chave.
- Paginacao e ordenacao da lista.
- Dashboard com indicadores por categoria e status.
- Notificacoes por e-mail ou WhatsApp.
- Geolocalizacao com mapa do bairro.
- Historico de atualizacoes dos comunicados.
- Painel administrativo para moderacao de conteudo.
- Testes automatizados no frontend e backend.

## Observacoes

- O backend depende do MySQL estar saudavel antes de iniciar.
- O frontend consome a API pela variavel `VITE_API_URL`.
- O banco usa volume Docker para persistencia dos dados.
