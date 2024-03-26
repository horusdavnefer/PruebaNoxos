package com.david.pruebanoxos.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.RadioGroup
import com.david.pruebanoxos.adapters.TransacionAdapter
import com.david.pruebanoxos.databinding.ItemTransactionBinding

class TransaccionInflater (
    context: Context
): RadioGroup(context){
    val binding: ItemTransactionBinding = ItemTransactionBinding.inflate(
        LayoutInflater.from(context), this, true
    )
}