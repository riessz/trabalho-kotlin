# Documentação da API — Perfil de Investidor

- **Base URL:** `http://localhost:8000` (emulador Android: `http://10.0.2.2:8000`)
- **Swagger UI:** `/docs` · **OpenAPI JSON:** `/openapi.json`
- **Formato:** JSON (UTF-8)

## Modelo de dados
```
usuarios(id PK, nome, email UNIQUE)
avaliacoes(id PK, usuario_id FK→usuarios, pontuacao, perfil, recomendacao, observacao, criado_em)
respostas(id PK, avaliacao_id FK→avaliacoes ON DELETE CASCADE, pergunta_id, valor)
```

## Endpoints
| Método | Rota | Descrição | Sucesso | Erros |
|---|---|---|---|---|
| POST | `/usuarios` | Cadastra usuário (ou retorna o existente pelo e-mail) | 201 / 200 | 422 |
| GET | `/usuarios/{id}` | Consulta usuário | 200 | 404 |
| GET | `/perguntas` | Lista perguntas e opções (valor 1–4) | 200 | — |
| POST | `/avaliacoes` | Recebe respostas, calcula pontuação e perfil, persiste | 201 | 404, 422 |
| GET | `/avaliacoes?usuario_id={id}` | Histórico do usuário (mais recentes primeiro) | 200 | — |
| GET | `/avaliacoes/{id}` | Detalhe com respostas | 200 | 404 |
| PUT | `/avaliacoes/{id}` | Altera a observação | 200 | 404, 422 |
| DELETE | `/avaliacoes/{id}` | Remove avaliação e respostas | 204 | 404 |

## Exemplos
**POST /usuarios**
```json
{ "nome": "Arthur", "email": "arthur@email.com" }
```
→ `{ "id": 1, "nome": "Arthur", "email": "arthur@email.com" }`

**GET /perguntas**
```json
[ { "id": 1, "texto": "Por quanto tempo pretende investir?",
    "opcoes": [ {"valor":1,"texto":"Menos de 1 ano"}, {"valor":2,"texto":"1 a 3 anos"},
                {"valor":3,"texto":"3 a 5 anos"}, {"valor":4,"texto":"Mais de 5 anos"} ] } ]
```

**POST /avaliacoes**
```json
{ "usuario_id": 1,
  "respostas": [ {"pergunta_id":1,"valor":3}, {"pergunta_id":2,"valor":2} ] }
```
→
```json
{ "id": 7, "usuario_id": 1, "pontuacao": 21, "perfil": "Moderado",
  "recomendacao": "50% renda fixa, 30% fundos, 20% ações",
  "observacao": null, "criado_em": "2026-11-15T20:10:00",
  "respostas": [ {"pergunta_id":1,"valor":3} ] }
```

**PUT /avaliacoes/{id}**
```json
{ "observacao": "Refazer em 6 meses" }
```

**Erro padrão (404/422)**
```json
{ "detail": "Avaliação não encontrada" }
```

## Regras
- A avaliação deve conter uma resposta para **todas** as perguntas (valor 1–4), senão 422.
- Perfil: 8–15 Conservador · 16–24 Moderado · 25–32 Arrojado.
- CORS liberado para desenvolvimento.
