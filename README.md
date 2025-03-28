# 🏥 WebScraping ANS - Operadoras de Saúde

Este projeto automatiza o *web scraping*, extração e transformação de dados de operadoras de planos de saúde da ANS, armazenando as informações em um banco PostgreSQL, e oferecendo uma interface de consulta via API REST e frontend Vue.js.

---

## ⚡ Tecnologias

- Java + Spring Boot
- Vue.js 3 + Axios
- PostgreSQL (via Docker Compose)
- Web Scraping com JSoup
- PDF Parsing com Tabula (Tabula-java)

---

## 🚀 Como Executar

### 1. Subir Banco de Dados

```bash
cd src/deploy/docker
docker-compose up -d
```

O banco estará disponível em `localhost:5432` com:

- **Database:** `postgres`
- **User:** `postgres`
- **Senha:** `postgres`

### 2. Executar Backend

Execute a classe `Main.java`:

```bash
# via IDE (Rider/IntelliJ) ou terminal:
mvn spring-boot:run
```

Esse runner executa todo o pipeline de scraping, transformação e persistência dos dados.

### 3. Rodar o Frontend Vue

```bash
cd src/FrontEnd/vue-front
npm install
npm run dev
```

Abra: [http://localhost:5173](http://localhost:5173)

---

## 🌐 Interface Web

A interface permite buscar operadoras de saúde por:

- **Registro ANS**
- **Razão Social**
- **CNPJ**
- **Cidade**

Com suporte a paginação:

- Botões "Anterior" / "Próxima"
- Exibição de página atual

---

## 🔍 API Endpoint Principal

### `GET /api/operadoras/find`

Consulta paginada com filtro `query`.

**Parâmetros:**

- `query`: busca textual (opcional)
- `page`: página (default: 0)
- `size`: tamanho da página (default: 30)

**Exemplo:**

```
GET /api/operadoras/find?query=Salvador&page=0&size=10
```

---

## 📄 Coleção de Requisições (Postman)

Arquivo: `ColecaoRequisicoes.json`

### Exemplos Incluídos:

- Buscar por "Unimed"
- Buscar por "Bradesco"
- Buscar por cidade: "Salvador", "Rio de Janeiro"

Importe no Postman: `File > Import`

---

## 📊 Principais Classes

### `StartupRunner`

Executa o pipeline completo:

- Download de PDFs, CSVs e ZIPs
- Conversão e transformação de dados
- Preenchimento do banco

### `WebScraper`

Realiza web scraping com JSoup:

- Busca e faz download de arquivos PDF/CSV/ZIP
- Compacta arquivos

### `DataTransformer`

Transforma PDFs em CSV:

- Usa Tabula (algoritmo de planilha)
- Substitui e limpa conteúdo textual
- Compacta o resultado

---

## 🎓 Autor

Yan Carvalho - Engenharia de Software, UCSal

[GitHub](https://github.com/yancarvalho13)

---

## 📂 Estrutura de Pastas

```
├── src
│   ├── application/webScraping     # Backend Java
│   ├── documents                   # CSVs baixados
│   ├── downloads                   # PDFs baixados/convertidos
│   ├── deploy/docker               # Docker Compose
│   └── FrontEnd/vue-front         # Frontend Vue.js
```

---

## ✅ To Do

- Automação completa via agendamento (cron)
- Tela de detalhamento de operadora no frontend

