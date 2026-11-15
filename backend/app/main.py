from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import RedirectResponse

from . import models  # noqa: F401  (registra as tabelas)
from .database import Base, engine
from .routers import avaliacoes, perguntas, usuarios

Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="API Perfil de Investidor",
    description="Questionário que classifica o perfil de investidor e guarda o histórico.",
    version="1.0.0",
)

app.add_middleware(
    CORSMiddleware, allow_origins=["*"], allow_methods=["*"], allow_headers=["*"]
)



@app.get("/", include_in_schema=False)
def raiz():
    return RedirectResponse(url="/docs")


app.include_router(usuarios.router)
app.include_router(perguntas.router)
app.include_router(avaliacoes.router)
