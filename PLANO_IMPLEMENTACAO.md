# Plano de Implementação — App "Perfil de Investidor" (AP2)

Entrega/apresentação: **16/11**. Stack: Android (Kotlin + XML + Retrofit) · API FastAPI · SQLite.

## 1. Requisitos do professor → solução
| Exigência (peso) | Solução |
|---|---|
| ≥5 Activities (1,5) | Login, Home, Questionário, Resultado, Histórico, Detalhe (6) |
| Intents (1,0) | Explícitas entre todas as telas; implícitas: compartilhar (ACTION_SEND) e abrir site (ACTION_VIEW) |
| ≥6 tipos de View (UI 1,0) | TextView, EditText, Button, RadioGroup/RadioButton, Spinner, Switch, ProgressBar, RecyclerView, CardView, ImageView |
| API REST + Swagger (1,0) | FastAPI, Swagger em `/docs` |
| Banco relacional (1,0) | SQLite via SQLAlchemy |
| Qualidade do código (1,0) | Pacotes por camada, nomes coerentes, sem duplicação |
| Funcionamento (1,0) | Fluxo completo testado antes da apresentação |
| Apresentação (2,5) | Roteiro de demo + saber explicar cada camada |
| Bônus +0,5 | Rodar em celular físico (e/ou deploy no Render) |

## 2. Estrutura do repositório
```
trabalho-kotlin/
├── backend/
│   ├── app/
│   │   ├── main.py        # app FastAPI, CORS, routers
│   │   ├── database.py    # engine, SessionLocal, Base
│   │   ├── models.py      # Usuario, Avaliacao, Resposta
│   │   ├── schemas.py     # Pydantic (entrada/saída JSON)
│   │   ├── scoring.py     # perguntas + regra de pontuação → perfil
│   │   └── routers/       # usuarios, perguntas, avaliacoes
│   └── requirements.txt
├── android/               # projeto Kotlin
│   └── app/src/main/java/com/arthur/investidor/
│       ├── ui/            # Activities + adapter
│       ├── data/api/      # ApiService, RetrofitClient
│       ├── data/model/    # DTOs
│       └── util/          # Sessao (SharedPreferences)
├── docs/API.md
├── PLANO_IMPLEMENTACAO.md
└── README.md
```

## 3. Regra de negócio
- 8 perguntas, cada resposta vale 1–4 pontos.
- Total 8–32: **8–15 Conservador**, **16–24 Moderado**, **25–32 Arrojado**.
- Cada perfil devolve uma recomendação de alocação.

## 4. Plano em 3 etapas (com commits frequentes)

> Regra: um commit a cada passo concluído e testado, mensagem no imperativo, `git push` ao fim de cada etapa.

### Etapa 1 — Backend e API (~2h)  → entrega: API funcionando no Swagger
| Passo | Conteúdo | Commit |
|---|---|---|
| 1.1 | Estrutura do repo, `.gitignore`, `requirements.txt`, docs (`PLANO_IMPLEMENTACAO.md`, `docs/API.md`) | `docs: plano de implementação e documentação da API` |
| 1.2 | `database.py`, `models.py` (3 tabelas com FK) | `feat(api): modelos e conexão SQLite` |
| 1.3 | `schemas.py`, `scoring.py` (perguntas + regra do perfil) | `feat(api): schemas e regra de pontuação` |
| 1.4 | Routers `usuarios`, `perguntas` | `feat(api): endpoints de usuários e perguntas` |
| 1.5 | Router `avaliacoes` (CRUD) + `main.py` com CORS | `feat(api): CRUD de avaliações e Swagger` |
| 1.6 | Teste de ponta a ponta no `/docs` | `test(api): roteiro de teste da API` |

### Etapa 2 — App Android: base + fluxo principal (~3h)  → entrega: login → questionário → resultado salvo
| Passo | Conteúdo | Commit |
|---|---|---|
| 2.1 | Projeto Kotlin em `android/`, dependências, permissão INTERNET, cleartext | `feat(app): projeto Android base` |
| 2.2 | `RetrofitClient`, `ApiService`, DTOs, `Sessao` | `feat(app): camada de rede com Retrofit` |
| 2.3 | `LoginActivity` + `HomeActivity` (CardViews) | `feat(app): telas de login e home` |
| 2.4 | `QuestionarioActivity` (RadioGroup, ProgressBar, Spinner) | `feat(app): questionário integrado à API` |
| 2.5 | `ResultadoActivity` + Intents implícitas (compartilhar, abrir site) | `feat(app): resultado e intents implícitas` |

### Etapa 3 — Histórico, polimento e entrega (~2h)  → entrega: tudo pronto para apresentar
| Passo | Conteúdo | Commit |
|---|---|---|
| 3.1 | `HistoricoActivity` (RecyclerView + CardView, excluir) | `feat(app): histórico de avaliações` |
| 3.2 | `DetalheActivity` (respostas + editar observação) | `feat(app): detalhe e edição da avaliação` |
| 3.3 | Paleta única, Snackbar/Toast, validações, erros de rede | `style(app): consistência visual e feedback` |
| 3.4 | Teste em celular físico (BASE_URL = IP do PC, uvicorn `--host 0.0.0.0`) — bônus | `chore: configuração para dispositivo físico` |
| 3.5 | README com prints, descrição da API, link do Swagger; repo público | `docs: README final com prints` |

## 5. Roteiro da apresentação
1. Swagger: executar um POST ao vivo.
2. App: login → questionário → resultado → compartilhar.
3. Histórico: excluir item e confirmar na API.
4. Explicar: Intents explícitas × implícitas, Retrofit, camadas do backend, relação das tabelas.

## 6. Checklist final
- [ ] ≥5 Activities  - [ ] Intent implícita  - [ ] ≥6 Views  - [ ] Swagger acessível
- [ ] Dados persistem no SQLite  - [ ] README com prints  - [ ] Repo público
- [ ] Backend rodando antes da aula  - [ ] Bônus (celular físico)
