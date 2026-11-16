package com.arthur.investidor.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.arthur.investidor.R
import com.arthur.investidor.databinding.ActivityResultadoBinding
import com.arthur.investidor.util.aplicarInsets
import com.arthur.investidor.util.corDoPerfil
import com.arthur.investidor.util.mostrarMensagem

class ResultadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.aplicarInsets()

        val perfil = intent.getStringExtra(EXTRA_PERFIL).orEmpty()
        val pontuacao = intent.getIntExtra(EXTRA_PONTUACAO, 0)
        val recomendacao = intent.getStringExtra(EXTRA_RECOMENDACAO).orEmpty()

        val cor = ContextCompat.getColor(this, corDoPerfil(perfil))
        binding.tvPerfil.text = perfil
        binding.tvPerfil.setTextColor(cor)
        binding.ivPerfil.setColorFilter(cor)
        binding.tvPontuacao.text = getString(R.string.pontuacao, pontuacao)
        binding.tvRecomendacao.text = recomendacao

        binding.btnCompartilhar.setOnClickListener { compartilhar(perfil, pontuacao, recomendacao) }
        binding.btnSaibaMais.setOnClickListener { abrirSite() }
        binding.btnVoltar.setOnClickListener { voltarAoInicio() }
    }

    /** Intent implícita: o Android mostra os apps que sabem compartilhar texto. */
    private fun compartilhar(perfil: String, pontuacao: Int, recomendacao: String) {
        val envio = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.texto_compartilhar, perfil, pontuacao, recomendacao))
        }
        iniciar(Intent.createChooser(envio, getString(R.string.compartilhar)))
    }

    /** Intent implícita: abre a página no navegador do aparelho. */
    private fun abrirSite() {
        iniciar(Intent(Intent.ACTION_VIEW, Uri.parse(URL_EDUCACAO_FINANCEIRA)))
    }

    private fun iniciar(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            binding.root.mostrarMensagem(getString(R.string.erro_abrir))
        }
    }

    private fun voltarAoInicio() {
        val home = Intent(this, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(home)
        finish()
    }

    companion object {
        const val EXTRA_PERFIL = "perfil"
        const val EXTRA_PONTUACAO = "pontuacao"
        const val EXTRA_RECOMENDACAO = "recomendacao"
        private const val URL_EDUCACAO_FINANCEIRA = "https://www.gov.br/investidor/pt-br"
    }
}
