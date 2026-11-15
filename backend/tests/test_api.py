import pytest
from fastapi.testclient import TestClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.pool import StaticPool

from app.database import Base, get_db
from app.main import app


@pytest.fixture()
def client():
    engine = create_engine(
        "sqlite://", connect_args={"check_same_thread": False}, poolclass=StaticPool
    )
    Base.metadata.create_all(bind=engine)
    Sessao = sessionmaker(bind=engine, autoflush=False)

    def override():
        db = Sessao()
        try:
            yield db
        finally:
            db.close()

    app.dependency_overrides[get_db] = override
    yield TestClient(app)
    app.dependency_overrides.clear()


def _usuario(client):
    return client.post("/usuarios", json={"nome": "Ana", "email": "ana@email.com"}).json()["id"]


def _respostas(client, valor):
    perguntas = client.get("/perguntas").json()
    return [{"pergunta_id": p["id"], "valor": valor} for p in perguntas]


def test_usuario_criado_e_reaproveitado_pelo_email(client):
    primeiro = client.post("/usuarios", json={"nome": "Ana", "email": "ana@email.com"})
    segundo = client.post("/usuarios", json={"nome": "Ana", "email": "ana@email.com"})
    assert primeiro.status_code == 201
    assert segundo.status_code == 200
    assert primeiro.json()["id"] == segundo.json()["id"]


def test_usuario_inexistente_retorna_404(client):
    assert client.get("/usuarios/999").status_code == 404


def test_lista_oito_perguntas_com_quatro_opcoes(client):
    perguntas = client.get("/perguntas").json()
    assert len(perguntas) == 8
    assert all(len(p["opcoes"]) == 4 for p in perguntas)


@pytest.mark.parametrize(
    "valor,perfil,pontuacao",
    [(1, "Conservador", 8), (2, "Moderado", 16), (3, "Moderado", 24), (4, "Arrojado", 32)],
)
def test_classificacao_do_perfil(client, valor, perfil, pontuacao):
    corpo = {"usuario_id": _usuario(client), "respostas": _respostas(client, valor)}
    resposta = client.post("/avaliacoes", json=corpo)
    assert resposta.status_code == 201
    assert resposta.json()["pontuacao"] == pontuacao
    assert resposta.json()["perfil"] == perfil


def test_avaliacao_incompleta_retorna_422(client):
    corpo = {"usuario_id": _usuario(client), "respostas": _respostas(client, 2)[:3]}
    assert client.post("/avaliacoes", json=corpo).status_code == 422


def test_avaliacao_com_usuario_inexistente_retorna_404(client):
    corpo = {"usuario_id": 999, "respostas": _respostas(client, 2)}
    assert client.post("/avaliacoes", json=corpo).status_code == 404


def test_fluxo_completo_historico_edicao_e_exclusao(client):
    uid = _usuario(client)
    criada = client.post(
        "/avaliacoes", json={"usuario_id": uid, "respostas": _respostas(client, 3)}
    ).json()

    historico = client.get(f"/avaliacoes?usuario_id={uid}").json()
    assert [a["id"] for a in historico] == [criada["id"]]

    editada = client.put(f"/avaliacoes/{criada['id']}", json={"observacao": "Refazer"})
    assert editada.json()["observacao"] == "Refazer"

    assert client.delete(f"/avaliacoes/{criada['id']}").status_code == 204
    assert client.get(f"/avaliacoes/{criada['id']}").status_code == 404
