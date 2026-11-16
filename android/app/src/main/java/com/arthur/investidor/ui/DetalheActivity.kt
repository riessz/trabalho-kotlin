package com.arthur.investidor.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.arthur.investidor.R
import com.arthur.investidor.data.api.RetrofitClient
import com.arthur.investidor.data.api.executar
import com.arthur.investidor.data.model.Avaliacao
import com.arthur.investidor.data.model.ObservacaoRequest
import com.arthur.investidor.data.model.Pergunta
import com.arthur.investidor.databinding.ActivityDetalheBinding
import com.arthur.investidor.util.aplicarInsets
import com.arthur.investidor.util.corDoPerfil
import com.arthur.investidor.util.formatarData
import com.arthur.investidor.util.mostrarMensagem

class DetalheActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalheBinding
    private var avaliacaoId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalheBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.aplicarInsets()

        avaliacaoId = intent.getIntExtra(EXTRA_ID, -1)
        binding.btnVoltar.setOnClickListener { finish() }
        binding.btnSalvar.setOnClickListener { salvarObservacao() }
        carregar()
    }

    private fun carregar() {
        carregando(true)
        RetrofitClient.api.obterAvaliacao(avaliacaoId).executar(
            onErro = ::falhaAoCarregar,
            onSucesso = { avaliacao ->
                if (avaliacao == null) {
                    falhaAoCarregar(getString(R.string.erro_nao_encontrada))
                } else {
                    carregarPerguntas(avaliacao)
                }
            }
        )
    }

    private fun carregarPerguntas(avaliacao: Avaliacao) {
        RetrofitClient.api.listarPerguntas().executar(
            onErro = ::falhaAoCarregar,
            onSucesso = { perguntas ->
                carregando(false)
                mostrar(avaliacao, perguntas.orEmpty())
            }
        )
    }

    private fun falhaAoCarregar(mensagem: String) {
        carregando(false)
        binding.root.mostrarMensagem(mensagem, getString(R.string.tentar_novamente)) { carregar() }
    }

    private fun mostrar(avaliacao: Avaliacao, perguntas: List<Pergunta>) {
        val cor = ContextCompat.getColor(this, corDoPerfil(avaliacao.perfil))
        binding.tvPerfil.text = avaliacao.perfil
        binding.tvPerfil.setTextColor(cor)
        binding.tvInfo.text = getString(R.string.item_info, avaliacao.pontuacao, formatarData(avaliacao.criadoEm))
        binding.tvRecomendacao.text = avaliacao.recomendacao
        binding.etObservacao.setText(avaliacao.observacao.orEmpty())

        binding.tvRespostas.text = avaliacao.respostas.joinToString("\n\n") { resposta ->
            val pergunta = perguntas.find { it.id == resposta.perguntaId }
            val textoResposta = pergunta?.opcoes?.find { it.valor == resposta.valor }?.texto ?: "${resposta.valor}"
            getString(R.string.resposta_linha, pergunta?.texto ?: "Pergunta ${resposta.perguntaId}", textoResposta)
        }
    }

    private fun salvarObservacao() {
        val texto = binding.etObservacao.text.toString().trim().ifEmpty { null }
        carregando(true)
        RetrofitClient.api.atualizarObservacao(avaliacaoId, ObservacaoRequest(texto)).executar(
            onErro = {
                carregando(false)
                binding.root.mostrarMensagem(it, getString(R.string.tentar_novamente)) { salvarObservacao() }
            },
            onSucesso = {
                carregando(false)
                binding.root.mostrarMensagem(getString(R.string.salvo))
            }
        )
    }

    private fun carregando(ativo: Boolean) {
        binding.progresso.visibility = if (ativo) View.VISIBLE else View.GONE
        binding.btnSalvar.isEnabled = !ativo
    }

    companion object {
        const val EXTRA_ID = "avaliacao_id"
    }
}
