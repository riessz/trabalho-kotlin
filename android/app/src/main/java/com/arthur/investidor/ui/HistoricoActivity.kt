package com.arthur.investidor.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthur.investidor.R
import com.arthur.investidor.data.api.RetrofitClient
import com.arthur.investidor.data.api.executar
import com.arthur.investidor.data.model.Avaliacao
import com.arthur.investidor.databinding.ActivityHistoricoBinding
import com.arthur.investidor.util.Sessao
import com.arthur.investidor.util.aplicarInsets
import com.arthur.investidor.util.mostrarMensagem

class HistoricoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoBinding
    private lateinit var sessao: Sessao
    private var adapter: AvaliacaoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessao = Sessao(this)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.aplicarInsets()

        binding.rvAvaliacoes.layoutManager = LinearLayoutManager(this)
        binding.btnVoltar.setOnClickListener { finish() }
    }

    // Recarrega ao voltar do detalhe, para refletir alterações.
    override fun onResume() {
        super.onResume()
        carregar()
    }

    private fun carregar() {
        binding.progresso.visibility = View.VISIBLE
        RetrofitClient.api.listarAvaliacoes(sessao.usuarioId()).executar(
            onErro = {
                binding.progresso.visibility = View.GONE
                binding.root.mostrarMensagem(it, getString(R.string.tentar_novamente)) { carregar() }
            },
            onSucesso = { lista ->
                binding.progresso.visibility = View.GONE
                adapter = AvaliacaoAdapter(lista.orEmpty().toMutableList(), ::abrirDetalhe, ::confirmarExclusao)
                binding.rvAvaliacoes.adapter = adapter
                atualizarVazio()
            }
        )
    }

    private fun atualizarVazio() {
        val vazio = adapter?.estaVazio() ?: true
        binding.tvVazio.visibility = if (vazio) View.VISIBLE else View.GONE
    }

    private fun abrirDetalhe(avaliacao: Avaliacao) {
        startActivity(Intent(this, DetalheActivity::class.java).putExtra(DetalheActivity.EXTRA_ID, avaliacao.id))
    }

    private fun confirmarExclusao(avaliacao: Avaliacao) {
        AlertDialog.Builder(this)
            .setTitle(R.string.excluir_titulo)
            .setMessage(R.string.excluir_msg)
            .setPositiveButton(R.string.excluir) { _, _ -> excluir(avaliacao) }
            .setNegativeButton(R.string.cancelar, null)
            .show()
    }

    private fun excluir(avaliacao: Avaliacao) {
        RetrofitClient.api.removerAvaliacao(avaliacao.id).executar(
            onErro = { binding.root.mostrarMensagem(it) },
            onSucesso = {
                adapter?.remover(avaliacao)
                atualizarVazio()
                binding.root.mostrarMensagem(getString(R.string.excluida))
            }
        )
    }
}
