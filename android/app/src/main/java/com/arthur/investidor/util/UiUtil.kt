package com.arthur.investidor.util

import android.view.View
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
