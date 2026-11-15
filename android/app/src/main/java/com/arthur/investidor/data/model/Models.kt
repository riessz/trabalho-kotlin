package com.arthur.investidor.data.model

import com.google.gson.annotations.SerializedName

data class UsuarioRequest(val nome: String, val email: String)

data class Usuario(val id: Int, val nome: String, val email: String)

data class Opcao(val valor: Int, val texto: String)

data class Pergunta(val id: Int, val texto: String, val opcoes: List<Opcao>)

data class Resposta(
    @SerializedName("pergunta_id") val perguntaId: Int,
    val valor: Int
)

data class AvaliacaoRequest(
    @SerializedName("usuario_id") val usuarioId: Int,
    val respostas: List<Resposta>
)

data class ObservacaoRequest(val observacao: String?)

data class Avaliacao(
    val id: Int,
    @SerializedName("usuario_id") val usuarioId: Int,
    val pontuacao: Int,
    val perfil: String,
    val recomendacao: String,
    val observacao: String?,
    @SerializedName("criado_em") val criadoEm: String,
    val respostas: List<Resposta>
)
