from typing import List

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from .. import models, schemas
from ..database import get_db
from ..scoring import classificar, ids_perguntas

router = APIRouter(prefix="/avaliacoes", tags=["Avaliações"])


def _buscar_ou_404(db: Session, avaliacao_id: int) -> models.Avaliacao:
    avaliacao = db.get(models.Avaliacao, avaliacao_id)
    if not avaliacao:
        raise HTTPException(status_code=404, detail="Avaliação não encontrada")
    return avaliacao


@router.post("", response_model=schemas.AvaliacaoOut, status_code=status.HTTP_201_CREATED)
def criar_avaliacao(dados: schemas.AvaliacaoCreate, db: Session = Depends(get_db)):
    if not db.get(models.Usuario, dados.usuario_id):
        raise HTTPException(status_code=404, detail="Usuário não encontrado")

    respondidas = sorted(r.pergunta_id for r in dados.respostas)
    if respondidas != sorted(ids_perguntas()):
        raise HTTPException(status_code=422, detail="Responda todas as perguntas, uma vez cada")

    pontuacao = sum(r.valor for r in dados.respostas)
    perfil, recomendacao = classificar(pontuacao)

    avaliacao = models.Avaliacao(
        usuario_id=dados.usuario_id,
        pontuacao=pontuacao,
        perfil=perfil,
        recomendacao=recomendacao,
        respostas=[models.Resposta(pergunta_id=r.pergunta_id, valor=r.valor) for r in dados.respostas],
    )
    db.add(avaliacao)
    db.commit()
    db.refresh(avaliacao)
    return avaliacao


@router.get("", response_model=List[schemas.AvaliacaoOut])
def listar_avaliacoes(usuario_id: int, db: Session = Depends(get_db)):
    return (
        db.query(models.Avaliacao)
        .filter_by(usuario_id=usuario_id)
        .order_by(models.Avaliacao.criado_em.desc(), models.Avaliacao.id.desc())
        .all()
    )


@router.get("/{avaliacao_id}", response_model=schemas.AvaliacaoOut)
def obter_avaliacao(avaliacao_id: int, db: Session = Depends(get_db)):
    return _buscar_ou_404(db, avaliacao_id)


@router.put("/{avaliacao_id}", response_model=schemas.AvaliacaoOut)
def atualizar_observacao(avaliacao_id: int, dados: schemas.AvaliacaoUpdate, db: Session = Depends(get_db)):
    avaliacao = _buscar_ou_404(db, avaliacao_id)
    avaliacao.observacao = dados.observacao
    db.commit()
    db.refresh(avaliacao)
    return avaliacao


@router.delete("/{avaliacao_id}", status_code=status.HTTP_204_NO_CONTENT)
def remover_avaliacao(avaliacao_id: int, db: Session = Depends(get_db)):
    db.delete(_buscar_ou_404(db, avaliacao_id))
    db.commit()
