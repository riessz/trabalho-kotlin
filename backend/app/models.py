from datetime import datetime

from sqlalchemy import Column, DateTime, ForeignKey, Integer, String
from sqlalchemy.orm import relationship

from .database import Base


class Usuario(Base):
    __tablename__ = "usuarios"

    id = Column(Integer, primary_key=True, index=True)
    nome = Column(String, nullable=False)
    email = Column(String, unique=True, nullable=False)

    avaliacoes = relationship("Avaliacao", back_populates="usuario")


class Avaliacao(Base):
    __tablename__ = "avaliacoes"

    id = Column(Integer, primary_key=True, index=True)
    usuario_id = Column(Integer, ForeignKey("usuarios.id"), nullable=False)
    pontuacao = Column(Integer, nullable=False)
    perfil = Column(String, nullable=False)
    recomendacao = Column(String, nullable=False)
    observacao = Column(String, nullable=True)
    criado_em = Column(DateTime, default=datetime.now)

    usuario = relationship("Usuario", back_populates="avaliacoes")
    respostas = relationship(
        "Resposta", back_populates="avaliacao", cascade="all, delete-orphan"
    )


class Resposta(Base):
    __tablename__ = "respostas"

    id = Column(Integer, primary_key=True, index=True)
    avaliacao_id = Column(Integer, ForeignKey("avaliacoes.id"), nullable=False)
    pergunta_id = Column(Integer, nullable=False)
    valor = Column(Integer, nullable=False)

    avaliacao = relationship("Avaliacao", back_populates="respostas")
