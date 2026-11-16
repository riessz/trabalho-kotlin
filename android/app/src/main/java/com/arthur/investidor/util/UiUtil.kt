package com.arthur.investidor.util

import android.view.View
import com.arthur.investidor.R
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

/** Evita que o conteúdo fique sob a barra de status/navegação (edge-to-edge). */
fun View.aplicarInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val barras = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        view.setPadding(barras.left, barras.top, barras.right, barras.bottom)
        insets
    }
}

/** Feedback visual padrão do app, com ação opcional (ex.: "Tentar novamente"). */
fun View.mostrarMensagem(texto: String, acao: String? = null, aoClicar: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, texto, Snackbar.LENGTH_LONG)
    if (acao != null && aoClicar != null) snackbar.setAction(acao) { aoClicar() }
    snackbar.show()
}

/** Cor associada a cada perfil (usada no resultado, histórico e detalhe). */
fun corDoPerfil(perfil: String) = when (perfil) {
    "Conservador" -> R.color.perfil_conservador
    "Moderado" -> R.color.perfil_moderado
    else -> R.color.perfil_arrojado
}

/** Converte "2026-11-15T20:05:11" em "15/11/2026 20:05". */
fun formatarData(iso: String): String {
    if (iso.length < 16) return iso
    val (ano, mes, dia) = iso.substring(0, 10).split("-")
    return "$dia/$mes/$ano ${iso.substring(11, 16)}"
}
