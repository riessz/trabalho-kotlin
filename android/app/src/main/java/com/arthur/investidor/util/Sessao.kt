package com.arthur.investidor.util

import android.content.Context
import com.arthur.investidor.data.model.Usuario

/** Guarda o usuário logado em SharedPreferences. */
class Sessao(context: Context) {

    private val prefs = context.getSharedPreferences("sessao", Context.MODE_PRIVATE)

    fun salvar(usuario: Usuario, lembrar: Boolean) {
        prefs.edit()
            .putInt(CHAVE_ID, usuario.id)
            .putString(CHAVE_NOME, usuario.nome)
            .putString(CHAVE_EMAIL, usuario.email)
            .putBoolean(CHAVE_LEMBRAR, lembrar)
            .apply()
    }

    fun usuarioId(): Int = prefs.getInt(CHAVE_ID, -1)

    fun nome(): String = prefs.getString(CHAVE_NOME, "") ?: ""

    fun email(): String = prefs.getString(CHAVE_EMAIL, "") ?: ""

    fun lembrar(): Boolean = prefs.getBoolean(CHAVE_LEMBRAR, false)

    fun logado(): Boolean = usuarioId() != -1

    /** Encerra a sessão mantendo nome e e-mail para preencher o login. */
    fun sair() = prefs.edit().remove(CHAVE_ID).putBoolean(CHAVE_LEMBRAR, false).apply()

    private companion object {
        const val CHAVE_ID = "usuario_id"
        const val CHAVE_NOME = "usuario_nome"
        const val CHAVE_EMAIL = "usuario_email"
        const val CHAVE_LEMBRAR = "lembrar"
    }
}
