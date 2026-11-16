package com.arthur.investidor.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arthur.investidor.R
import com.arthur.investidor.data.model.Avaliacao
import com.arthur.investidor.databinding.ItemAvaliacaoBinding
import com.arthur.investidor.util.corDoPerfil
import com.arthur.investidor.util.formatarData

/** Mostra uma avaliação por card; clique abre o detalhe e o botão exclui. */
class AvaliacaoAdapter(
    private val itens: MutableList<Avaliacao>,
    private val aoAbrir: (Avaliacao) -> Unit,
    private val aoExcluir: (Avaliacao) -> Unit
) : RecyclerView.Adapter<AvaliacaoAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemAvaliacaoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemAvaliacaoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = itens.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itens[position]
        val contexto = holder.itemView.context
        val cor = ContextCompat.getColor(contexto, corDoPerfil(item.perfil))
        with(holder.binding) {
            tvPerfil.text = item.perfil
            tvPerfil.setTextColor(cor)
            ivPerfil.setColorFilter(cor)
            tvInfo.text = contexto.getString(R.string.item_info, item.pontuacao, formatarData(item.criadoEm))
            root.setOnClickListener { aoAbrir(item) }
            btnExcluir.setOnClickListener { aoExcluir(item) }
        }
    }

    fun remover(item: Avaliacao) {
        val posicao = itens.indexOf(item)
        if (posicao >= 0) {
            itens.removeAt(posicao)
            notifyItemRemoved(posicao)
        }
    }

    fun estaVazio() = itens.isEmpty()
}
