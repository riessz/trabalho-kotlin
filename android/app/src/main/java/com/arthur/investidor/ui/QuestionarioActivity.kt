package com.arthur.investidor.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.arthur.investidor.R
import com.arthur.investidor.data.api.RetrofitClient
import com.arthur.investidor.data.api.executar
import com.arthur.investidor.data.model.Avaliacao
import com.arthur.investidor.data.model.AvaliacaoRequest
import com.arthur.investidor.data.model.Pergunta
import com.arthur.investidor.data.model.Resposta
import com.arthur.investidor.databinding.ActivityQuestionarioBinding
import com.arthur.investidor.util.Sessao
import com.arthur.investidor.util.aplicarInsets
import com.arthur.investidor.util.mostrarMensagem

class QuestionarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionarioBinding
    private lateinit var sessao: Sessao

    private var perguntas: List<Pergunta> = emptyList()
    private var indice = 0
    private val respostas = mutableListOf<Resposta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessao = Sessao(this)
        binding = ActivityQuestionarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.aplicarInsets()

        binding.rgOpcoes.setOnCheckedChangeListener { _, _ -> binding.btnAvancar.isEnabled = true }
        binding.btnAvancar.setOnClickListener { avancar() }
        carregarPerguntas()
    }

    private fun carregarPerguntas() {
        carregando(true)
        RetrofitClient.api.listarPerguntas().executar(
            onErro = {
                carregando(false)
                binding.root.mostrarMensagem(it, getString(R.string.tentar_novamente)) { carregarPerguntas() }
            },
            onSucesso = { lista ->
                carregando(false)
                perguntas = lista.orEmpty()
                if (perguntas.isNotEmpty()) mostrarPergunta()
            }
        )
    }

    private fun mostrarPergunta() {
        val pergunta = perguntas[indice]
        binding.tvContador.text = getString(R.string.pergunta_contador, indice + 1, perguntas.size)
        binding.progressoPerguntas.max = perguntas.size
        binding.progressoPerguntas.progress = indice + 1
        binding.tvPergunta.text = pergunta.texto

        binding.rgOpcoes.removeAllViews()
        pergunta.opcoes.forEach { opcao ->
            val radio = RadioButton(this).apply {
                id = View.generateViewId()
                text = opcao.texto
                tag = opcao.valor
                textSize = 16f
                setPadding(8, 24, 8, 24)
            }
            binding.rgOpcoes.addView(radio)
        }

        binding.btnAvancar.isEnabled = false
        binding.btnAvancar.text = getString(if (indice == perguntas.lastIndex) R.string.finalizar else R.string.proxima)
    }

    private fun avancar() {
        val marcado = findViewById<RadioButton>(binding.rgOpcoes.checkedRadioButtonId)
        if (marcado == null) {
            binding.root.mostrarMensagem(getString(R.string.selecione_resposta))
            return
        }
        respostas.add(Resposta(perguntas[indice].id, marcado.tag as Int))

        if (indice < perguntas.lastIndex) {
            indice++
            mostrarPergunta()
        } else {
            enviar()
        }
    }

    private fun enviar() {
        carregando(true)
        val dados = AvaliacaoRequest(sessao.usuarioId(), respostas)
        RetrofitClient.api.criarAvaliacao(dados).executar(
            onErro = {
                carregando(false)
                binding.root.mostrarMensagem(it, getString(R.string.tentar_novamente)) { enviar() }
            },
            onSucesso = { avaliacao -> avaliacao?.let { abrirResultado(it) } }
        )
    }

    private fun abrirResultado(avaliacao: Avaliacao) {
        val intent = Intent(this, ResultadoActivity::class.java).apply {
            putExtra(ResultadoActivity.EXTRA_PERFIL, avaliacao.perfil)
            putExtra(ResultadoActivity.EXTRA_PONTUACAO, avaliacao.pontuacao)
            putExtra(ResultadoActivity.EXTRA_RECOMENDACAO, avaliacao.recomendacao)
        }
        startActivity(intent)
        finish()
    }

    private fun carregando(ativo: Boolean) {
        binding.progresso.visibility = if (ativo) View.VISIBLE else View.GONE
        binding.btnAvancar.isEnabled = !ativo && binding.rgOpcoes.checkedRadioButtonId != -1
    }
}
