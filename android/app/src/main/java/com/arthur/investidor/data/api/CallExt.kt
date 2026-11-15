package com.arthur.investidor.data.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Executa a chamada em segundo plano e devolve o resultado na thread principal,
 * evitando repetir o mesmo Callback em todas as telas.
 */
fun <T> Call<T>.executar(onErro: (String) -> Unit, onSucesso: (T?) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                onSucesso(response.body())
            } else {
                onErro("A API respondeu com erro ${response.code()}")
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onErro("Sem conexão com a API. Verifique se o servidor está rodando.")
        }
    })
}
