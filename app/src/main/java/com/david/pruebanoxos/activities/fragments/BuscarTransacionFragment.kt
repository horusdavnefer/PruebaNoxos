package com.david.pruebanoxos.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.david.pruebanoxos.R
import com.david.pruebanoxos.activities.viewmodels.MainViewModel
import com.david.pruebanoxos.adapters.TransacionAdapter
import com.david.pruebanoxos.db.entities.TransaccionEntity
import com.david.pruebanoxos.utils.TransaccionInflater
import com.david.pruebanoxos.utils.Utils
import androidx.core.widget.addTextChangedListener
import com.david.pruebanoxos.databinding.FragmentBuscarBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.migration.OptionalInject

@OptionalInject
@AndroidEntryPoint
class BuscarTransacionFragment : Fragment() {

    private var _binding: FragmentBuscarBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TransacionAdapter<TransaccionEntity>
    private val transactionViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuscarBinding.inflate(inflater, container, false)
        return  binding.root
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
                this.data?.let { list ->
                    val transactionApproved = list.filter {
                        it.annulation == false
                    }
                    initBenefitAdapter(transactionApproved)
                    binding.filterTransaction.addTextChangedListener { filter ->
                        val listFilterTransaction =
                            transactionApproved.filter { listTransaction ->
                                listTransaction.receiptId!!.lowercase().contains(filter.toString())
                            }
                        adapter.updateListTransactions(listFilterTransaction)
                        if (listFilterTransaction.isEmpty()) {
                            binding.emptyItemsTextFilter.visibility = View.VISIBLE
                            binding.transactionRecyclerFilter.visibility = View.GONE
                        } else{
                            binding.emptyItemsTextFilter.visibility = View.GONE
                            binding.transactionRecyclerFilter.visibility = View.VISIBLE
                        }
                    }
                }
                error?.let {
                    binding.transactionRecyclerFilter.visibility = View.GONE
                    binding.emptyItemsTextFilter.visibility = View.VISIBLE
                    Toast.makeText(context, "no hay datos ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Funcion que inicializa el adaptador de los beneficios
     */
    private fun initBenefitAdapter(transactions: List<TransaccionEntity>) {
        adapter = TransacionAdapter(
            dataset = transactions,
            itemViewFactory = {
                TransaccionInflater(this.requireContext())
            }
        ) { view, data, _ ->
            setData(view as TransaccionInflater, data)
        }
        binding.transactionRecyclerFilter.adapter = adapter
        binding.transactionRecyclerFilter.layoutManager = GridLayoutManager(this.context, 1)
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

            transactionItemInflater.setOnClickListener {
                transactionViewModel.transactionIdSelected = data.id
                //Se asegura que realmente se haya asignado un beneficio antes de lanzar el fragmento que muestra el detalle
                transactionViewModel.transactionIdSelected?.let {
                    findNavController().navigate(R.id.action_browseTransactionFragment_to_detailTransactionFragment)
                }
            }
        }
    }
}