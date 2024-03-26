package com.david.pruebanoxos.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.david.pruebanoxos.activities.viewmodels.MainViewModel
import com.david.pruebanoxos.adapters.TransacionAdapter
import com.david.pruebanoxos.databinding.FragmentTransaccionesBinding
import com.david.pruebanoxos.db.entities.TransaccionEntity
import com.david.pruebanoxos.utils.TransaccionInflater
import com.david.pruebanoxos.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.migration.OptionalInject

@OptionalInject
@AndroidEntryPoint
class TransaccionesFragment : Fragment() {

    private var _binding: FragmentTransaccionesBinding? = null
    private val binding get() = _binding!!
    private val transactionViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransaccionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Funcion que inicializa el observador
     */
    private fun initObservers(){
        transactionViewModel.getTransactionsList()
        transactionViewModel.transactions.observe(viewLifecycleOwner) {data ->
            data?.run {
                this.data?.let {
                    initBenefitAdapter(it)
                    binding.emptyItemsText.visibility = View.GONE
                }
                error?.let {
                    binding.transactionRecycler.visibility = View.GONE
                    binding.emptyItemsText.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * Funcion que inicializa el adaptador de los beneficios
     */
    private fun initBenefitAdapter(transactions: List<TransaccionEntity>) {
        binding.transactionRecycler.adapter = TransacionAdapter(
            dataset = transactions,
            itemViewFactory = {
                TransaccionInflater(this.requireContext())
            }
        ) { view, data, _ ->
            setData(view as TransaccionInflater, data)
        }
        binding.transactionRecycler.layoutManager = GridLayoutManager(this.context, 1)
    }

    /**
     * Asigna los datos a la vista
     */
    private fun setData(transactionItemInflater: TransaccionInflater, data: TransaccionEntity){
        with((transactionItemInflater).binding) {
            idTransaction.text = Utils.concatString("Transacción ", data.id.toString())
            cardNumber.text = Utils.concatString("Card: ", data.card.toString())
            commerceNumber.text = Utils.concatString("Commerce: ", data.commerceCode.toString())
            terminalNumber.text = Utils.concatString("Terminal: ", data.terminalCode.toString())
            amountNumber.text = Utils.concatString("$ ", Utils.convertToDecimal(data.amount!!))
            if(data.annulation == true){
                approved.text = "ANULADA"
            }else {
                approved.text = "APROBADA"
            }
        }
    }


}