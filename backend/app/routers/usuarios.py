from fastapi import APIRouter, Depends, HTTPException, Response, status
from sqlalchemy.orm import Session

from .. import models, schemas
from ..database import get_db

router = APIRouter(prefix="/usuarios", tags=["Usuários"])


@router.post("", response_model=schemas.UsuarioOut, status_code=status.HTTP_201_CREATED)
def criar_usuario(dados: schemas.UsuarioCreate, response: Response, db: Session = Depends(get_db)):
    existente = db.query(models.Usuario).filter_by(email=dados.email).first()
    if existente:
        response.status_code = status.HTTP_200_OK
        return existente
    usuario = models.Usuario(nome=dados.nome, email=dados.email)
    db.add(usuario)
    db.commit()
    db.refresh(usuario)
    return usuario


@router.get("/{usuario_id}", response_model=schemas.UsuarioOut)
def obter_usuario(usuario_id: int, db: Session = Depends(get_db)):
    usuario = db.get(models.Usuario, usuario_id)
    if not usuario:
        raise HTTPException(status_code=404, detail="Usuário não encontrado")
    return usuario
