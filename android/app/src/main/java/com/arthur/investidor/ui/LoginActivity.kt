package com.arthur.investidor.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arthur.investidor.R
import com.arthur.investidor.data.api.RetrofitClient
import com.arthur.investidor.data.api.executar
import com.arthur.investidor.data.model.UsuarioRequest
import com.arthur.investidor.databinding.ActivityLoginBinding
import com.arthur.investidor.util.Sessao
import com.arthur.investidor.util.aplicarInsets
import com.arthur.investidor.util.mostrarMensagem

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessao: Sessao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessao = Sessao(this)

        if (sessao.logado() && sessao.lembrar()) {
            abrirHome()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.aplicarInsets()

        binding.etNome.setText(sessao.nome())
        binding.etEmail.setText(sessao.email())
        binding.btnEntrar.setOnClickListener { entrar() }
    }

    private fun entrar() {
        val nome = binding.etNome.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        binding.tilNome.error = if (nome.isEmpty()) getString(R.string.erro_nome) else null
        binding.tilEmail.error =
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) getString(R.string.erro_email) else null
        if (binding.tilNome.error != null || binding.tilEmail.error != null) return

        carregando(true)
        RetrofitClient.api.criarUsuario(UsuarioRequest(nome, email)).executar(
            onErro = {
                carregando(false)
                binding.root.mostrarMensagem(it)
            },
            onSucesso = { usuario ->
                usuario?.let {
                    sessao.salvar(it, binding.swLembrar.isChecked)
                    abrirHome()
                }
            }
        )
    }

    private fun carregando(ativo: Boolean) {
        binding.progresso.visibility = if (ativo) View.VISIBLE else View.GONE
        binding.btnEntrar.isEnabled = !ativo
    }

    private fun abrirHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
