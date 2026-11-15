package com.arthur.investidor.data.api

import com.arthur.investidor.data.model.Avaliacao
import com.arthur.investidor.data.model.AvaliacaoRequest
import com.arthur.investidor.data.model.ObservacaoRequest
import com.arthur.investidor.data.model.Pergunta
import com.arthur.investidor.data.model.Usuario
import com.arthur.investidor.data.model.UsuarioRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/** Um método por endpoint da API (veja docs/API.md). */
interface ApiService {

    @POST("usuarios")
    fun criarUsuario(@Body dados: UsuarioRequest): Call<Usuario>

    @GET("perguntas")
    fun listarPerguntas(): Call<List<Pergunta>>

    @POST("avaliacoes")
    fun criarAvaliacao(@Body dados: AvaliacaoRequest): Call<Avaliacao>

    @GET("avaliacoes")
    fun listarAvaliacoes(@Query("usuario_id") usuarioId: Int): Call<List<Avaliacao>>

    @GET("avaliacoes/{id}")
    fun obterAvaliacao(@Path("id") id: Int): Call<Avaliacao>

    @PUT("avaliacoes/{id}")
    fun atualizarObservacao(@Path("id") id: Int, @Body dados: ObservacaoRequest): Call<Avaliacao>

    @DELETE("avaliacoes/{id}")
    fun removerAvaliacao(@Path("id") id: Int): Call<Void>
}
