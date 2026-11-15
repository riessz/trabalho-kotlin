from typing import List

from fastapi import APIRouter

from .. import schemas
from ..scoring import listar_perguntas

router = APIRouter(prefix="/perguntas", tags=["Perguntas"])


@router.get("", response_model=List[schemas.PerguntaOut])
def listar():
    return listar_perguntas()
