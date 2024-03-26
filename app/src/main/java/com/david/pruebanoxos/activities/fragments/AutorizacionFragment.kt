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
import com.david.pruebanoxos.databinding.FragmentAutorizacionBinding
import com.david.pruebanoxos.objectsDTO.AuthorizationDTO
import com.david.pruebanoxos.objectsDTO.TransactionDTO
import com.david.pruebanoxos.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.util.Base64
import java.util.UUID

@AndroidEntryPoint
class AutorizacionFragment : Fragment() {

    private var _binding: FragmentAutorizacionBinding? = null
    private val binding get() = _binding!!

    private val transactionViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAutorizacionBinding.inflate(inflater, container, false)
        return  binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtonLayer()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initButtonLayer(){
        with(binding){
            approvedButton.setOnClickListener {
                if (commerceCodeInput.text.isNotEmpty() && terminalCodeInput.text.isNotEmpty() &&
                    amountInput.text.isNotEmpty() && cardInput.text.isNotEmpty()){
                    val concatString = "000123000ABC"
                    val encoder: Base64.Encoder = Base64.getEncoder()
                    val encoderString: String = encoder.encodeToString(concatString.encodeToByteArray())
                    val concatKey = Utils.concatString("Basic ", encoderString)
                    val myUuid = UUID.randomUUID()
                    val myUuidAsString = myUuid.toString()
                    val amountNumber = amountInput.text.toString()
                    val newAmount = String.format(amountNumber).replace(".","")
                    val authorizationDTO = AuthorizationDTO(id = myUuidAsString, commerceCode = commerceCodeInput.text.toString(),
                        terminalCode = terminalCodeInput.text.toString() ,amount = newAmount, card = cardInput.text.toString())
                    transactionViewModel.getAuthorization(concatKey, authorizationDTO)
                    transactionViewModel.setTransactionRoom( TransactionDTO(idTransaction = myUuidAsString, commerceCode = commerceCodeInput.text.toString(),
                        terminalCode = terminalCodeInput.text.toString() ,amount = newAmount, card = cardInput.text.toString(), authorization = true,
                        annulation = false, receiptId = null, rrn = null))
                }else{
                    Toast.makeText(context, "Falta información en el Formulario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Funcion que inicializa el observador
     */
    private fun initObservers(){
        transactionViewModel.authorization.observe(viewLifecycleOwner) { data ->
            data?.run {
                this.data?.let {
                    if (transactionViewModel.transactionDTO.authorization){
                        transactionViewModel.transactionDTO.receiptId = it.receiptId
                        transactionViewModel.transactionDTO.rrn = it.rrn
                        val transactionRoom = transactionViewModel.transactionDTO
                        transactionViewModel.setTransactionInsert(transactionRoom)
                        Toast.makeText(context, "Transacción Autorizada", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_authorizationFragment_to_homeFragment)
                        transactionViewModel.transactionDTO = TransactionDTO(idTransaction = "", commerceCode = "",
                            terminalCode = "" ,amount = "", card = "", authorization = false,
                            annulation = false, receiptId = null, rrn = null)
                    }
                }
                error?.let {
                    Toast.makeText(context, "Transacción No Autorizada - Revise los datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}