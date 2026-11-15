package com.arthur.investidor.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arthur.investidor.R
import com.arthur.investidor.databinding.ActivityHomeBinding
import com.arthur.investidor.util.Sessao
import com.arthur.investidor.util.aplicarInsets

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessao: Sessao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessao = Sessao(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.aplicarInsets()

        binding.tvBoasVindas.text = getString(R.string.home_boas_vindas, sessao.nome())
        binding.cardNovaAvaliacao.setOnClickListener {
            startActivity(Intent(this, QuestionarioActivity::class.java))
        }
        binding.btnSair.setOnClickListener { sair() }
    }

    private fun sair() {
        sessao.sair()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
