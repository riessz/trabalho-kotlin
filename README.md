# trabalho-kotlin — App Perfil de Investidor

Aplicativo Android (Kotlin) que aplica um questionário, classifica o perfil de investidor (Conservador, Moderado ou Arrojado) e guarda o histórico por meio de uma API REST.

## Como rodar a API

Requisitos: Python 3.10+.

```bash
cd backend

# 1. criar e ativar o ambiente virtual
python -m venv .venv
.venv\Scripts\activate          # Windows (PowerShell/CMD)
# source .venv/bin/activate     # Linux/macOS

# 2. instalar as dependências
pip install -r requirements.txt

# 3. subir o servidor
python -m uvicorn app.main:app --reload
```

- API: http://localhost:8000
- **Swagger (documentação interativa): http://localhost:8000/docs**
- OpenAPI JSON: http://localhost:8000/openapi.json

O banco SQLite (`backend/investidor.db`) é criado automaticamente na primeira execução.

### Usar com o app Android
- **Emulador:** o app acessa a API em `http://10.0.2.2:8000`.
- **Celular físico:** suba a API acessível na rede com `python -m uvicorn app.main:app --host 0.0.0.0` e use o IP do PC (celular e PC na mesma rede Wi-Fi).

### Testes
```bash
cd backend
python -m pytest -q
```

Documentação completa dos endpoints em [docs/API.md](docs/API.md).
