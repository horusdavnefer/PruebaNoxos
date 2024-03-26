package com.david.pruebanoxos.activities.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.david.pruebanoxos.R
import com.david.pruebanoxos.activities.viewmodels.MainViewModel
import com.david.pruebanoxos.databinding.FragmentDetalleBinding
import com.david.pruebanoxos.objectsDTO.AnnulationDTO
import com.david.pruebanoxos.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.migration.OptionalInject
import java.util.Base64

@OptionalInject
@AndroidEntryPoint
class DetalleFragment : Fragment() {

    private var _binding: FragmentDetalleBinding? = null
    private val binding get() = _binding!!
    private val transactionViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObservers(){
        transactionViewModel.getTransactionsList()
        transactionViewModel.transactions.observe(viewLifecycleOwner) {data ->
            data?.run {
                this.data?.let { list ->
                    val transaction = list.find { transactionSelect ->
                        transactionSelect.id == transactionViewModel.transactionIdSelected
                    }
                    with(binding){
                        transaction?.let { select ->
                            idTransaction.text = Utils.concatString("Transacción ", select.id.toString())
                            cardNumber.text = Utils.concatString("Card: ", select.card.toString())
                            commerceNumber.text = Utils.concatString("Commerce: ", select.commerceCode.toString())
                            terminalNumber.text = Utils.concatString("Terminal: ", select.terminalCode.toString())
                            amountNumber.text = Utils.concatString("$ ", Utils.convertToDecimal(select.amount!!))
                            receiptNumber.text = Utils.concatString("Receipt: ", select.receiptId.toString())
                            rrnNumber.text = Utils.concatString("Rrn: ", select.rrn.toString())

                            val annulationDTO = AnnulationDTO(select.receiptId.toString(), select.rrn.toString())
                            annulationButton.setOnClickListener {
                                val concatString = "000123000ABC"
                                val encoder: Base64.Encoder = Base64.getEncoder()
                                val encoderString: String = encoder.encodeToString(concatString.encodeToByteArray())
                                val concatKey = Utils.concatString("Basic ", encoderString)
                                transactionViewModel.getAnnulation(concatKey, annulationDTO)
                                transactionViewModel.transactionAnnulation = true
                            }
                        }
                    }
                }
            }
        }

        transactionViewModel.annulation.observe(viewLifecycleOwner){ data ->
            data?.run {
                this.data?.let {
                    transactionViewModel.transactions.observe(viewLifecycleOwner) { data ->
                        if (transactionViewModel.transactionAnnulation != null){
                            data?.run {
                                this.data?.let { list ->
                                    val transaction = list.find { transactionSelect ->
                                        transactionSelect.id == transactionViewModel.transactionIdSelected
                                    }
                                    transaction?.let {
                                        it.annulation = true
                                        transactionViewModel.setTransactionUpdate(it)
                                        Toast.makeText(context, "Anulación Correcta", Toast.LENGTH_SHORT).show()
                                        findNavController().navigate(R.id.action_detailTransactionFragment_to_homeFragment)
                                        transactionViewModel.transactionAnnulation = null
                                    }
                                }
                            }
                        }
                    }
                    error?.let {
                        Toast.makeText(context, "Anulación Incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}