"""Perguntas do questionário e regra de pontuação do perfil de investidor."""

VALORES = [1, 2, 3, 4]

PERGUNTAS = [
    {"id": 1, "texto": "Por quanto tempo pretende manter seus investimentos?",
     "opcoes": ["Menos de 1 ano", "1 a 3 anos", "3 a 5 anos", "Mais de 5 anos"]},
    {"id": 2, "texto": "Qual é o seu principal objetivo ao investir?",
     "opcoes": ["Preservar o dinheiro", "Renda complementar", "Crescer o patrimônio", "Maximizar ganhos"]},
    {"id": 3, "texto": "Qual é a sua experiência com investimentos?",
     "opcoes": ["Nenhuma", "Só poupança", "Renda fixa e fundos", "Ações e derivativos"]},
    {"id": 4, "texto": "Se seu investimento cair 20% em um mês, o que faria?",
     "opcoes": ["Resgataria tudo", "Resgataria parte", "Manteria", "Compraria mais"]},
    {"id": 5, "texto": "Possui reserva de emergência?",
     "opcoes": ["Não", "Menos de 3 meses", "De 3 a 6 meses", "Mais de 6 meses"]},
    {"id": 6, "texto": "Que parte da sua renda mensal pretende investir?",
     "opcoes": ["Menos de 5%", "5% a 15%", "15% a 30%", "Mais de 30%"]},
    {"id": 7, "texto": "Qual perda máxima você aceitaria em um ano?",
     "opcoes": ["Nenhuma", "Até 5%", "Até 15%", "Mais de 15%"]},
    {"id": 8, "texto": "Como avalia seu conhecimento do mercado financeiro?",
     "opcoes": ["Baixo", "Básico", "Bom", "Avançado"]},
]

# (pontuação máxima do perfil, nome, recomendação de alocação)
PERFIS = [
    (15, "Conservador", "70% renda fixa, 20% fundos, 10% ações"),
    (24, "Moderado", "50% renda fixa, 30% fundos, 20% ações"),
    (32, "Arrojado", "20% renda fixa, 30% fundos, 50% ações"),
]


def listar_perguntas():
    return [
        {
            "id": p["id"],
            "texto": p["texto"],
            "opcoes": [{"valor": v, "texto": t} for v, t in zip(VALORES, p["opcoes"])],
        }
        for p in PERGUNTAS
    ]


def ids_perguntas():
    return {p["id"] for p in PERGUNTAS}


def classificar(pontuacao: int):
    """Retorna (perfil, recomendacao) para a pontuação total (8-32)."""
    for limite, perfil, recomendacao in PERFIS:
        if pontuacao <= limite:
            return perfil, recomendacao
    return PERFIS[-1][1], PERFIS[-1][2]
