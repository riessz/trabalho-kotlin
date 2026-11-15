from datetime import datetime
from typing import List, Optional

from pydantic import BaseModel, ConfigDict, EmailStr, Field


class UsuarioCreate(BaseModel):
    nome: str = Field(min_length=1)
    email: EmailStr


class UsuarioOut(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    nome: str
    email: str


class Opcao(BaseModel):
    valor: int
    texto: str


class PerguntaOut(BaseModel):
    id: int
    texto: str
    opcoes: List[Opcao]


class RespostaIn(BaseModel):
    pergunta_id: int
    valor: int = Field(ge=1, le=4)


class RespostaOut(RespostaIn):
    model_config = ConfigDict(from_attributes=True)


class AvaliacaoCreate(BaseModel):
    usuario_id: int
    respostas: List[RespostaIn]


class AvaliacaoUpdate(BaseModel):
    observacao: Optional[str] = None


class AvaliacaoOut(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    usuario_id: int
    pontuacao: int
    perfil: str
    recomendacao: str
    observacao: Optional[str]
    criado_em: datetime
    respostas: List[RespostaOut]
