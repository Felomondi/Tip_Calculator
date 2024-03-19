package com.example.tipcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.tipcalculator.databinding.ActivityMainBinding
import java.text.DecimalFormat
import android.widget.SeekBar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentPartySize = 1 // Default party size
    private val decimalFormat = DecimalFormat("#.##") // For formatting the decimal numbers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the ArrayAdapter for the Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.party_size_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

        // Set Spinner item selection listener
        binding.spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                currentPartySize = parent.getItemAtPosition(position).toString().toInt()
                calculateTipAndTotal()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                // Interface callback
            }
        }

        // SeekBar change listener
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                calculateTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Not needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Not needed
            }
        })

        // Base amount EditText change listener
        binding.editTextNumberDecimal2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                calculateTipAndTotal()
            }
        })
    }

    private fun calculateTipAndTotal() {
        val baseAmount = binding.editTextNumberDecimal2.text.toString().toDoubleOrNull() ?: return
        val tipPercentage = binding.seekBar.progress
        val tipAmount = baseAmount * (tipPercentage / 100.0)
        val totalAmount = baseAmount + tipAmount

        // Divide by the current party size
        val tipPerPerson = tipAmount / currentPartySize
        val totalPerPerson = totalAmount / currentPartySize

        // Update the tip and total EditTexts with the formatted values per person
        binding.editTextNumberDecimal.setText(decimalFormat.format(tipPerPerson))
        binding.editTextNumberDecimal3.setText(decimalFormat.format(totalPerPerson))
    }
}
