package com.example.tipcalculator

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import android.text.TextWatcher
import android.text.Editable

class MainActivity : AppCompatActivity() {
    private lateinit var baseAmountEditText: EditText
    private lateinit var tipSeekBar: SeekBar
    private lateinit var tipAmountEditText: EditText
    private lateinit var totalAmountEditText: EditText
    private lateinit var partySizeSpinner: Spinner
    private var currentPartySize = 1 // Default party size
    private val decimalFormat = DecimalFormat("#.##") // For formatting the decimal numbers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        baseAmountEditText = findViewById(R.id.editTextNumberDecimal2)
        tipSeekBar = findViewById(R.id.seekBar)
        tipAmountEditText = findViewById(R.id.editTextNumberDecimal)
        totalAmountEditText = findViewById(R.id.editTextNumberDecimal3)
        partySizeSpinner = findViewById(R.id.spinner)

        // Set up the ArrayAdapter for the Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.party_size_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            partySizeSpinner.adapter = adapter
        }

        // Set Spinner item selection listener
        partySizeSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                currentPartySize = parent.getItemAtPosition(position).toString().toInt()
                calculateTipAndTotal()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                // Interface callback
            }
        }

        // SeekBar change listener
        tipSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
        baseAmountEditText.addTextChangedListener(object : TextWatcher {
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
        val baseAmount = baseAmountEditText.text.toString().toDoubleOrNull() ?: return
        val tipPercentage = tipSeekBar.progress
        val tipAmount = baseAmount * (tipPercentage / 100.0)
        val totalAmount = baseAmount + tipAmount

        // Divide by the current party size
        val tipPerPerson = tipAmount / currentPartySize
        val totalPerPerson = totalAmount / currentPartySize

        // Update the tip and total EditTexts with the formatted values per person
        tipAmountEditText.setText(decimalFormat.format(tipPerPerson))
        totalAmountEditText.setText(decimalFormat.format(totalPerPerson))
    }
}
