package com.example.calculator

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {
    private var cNumber = ""
    private val numberList = mutableListOf<Double>()
    private val operatorList = mutableListOf<Operators>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        number_0.setOnClickListener(addNumber("0"))
        number_00.setOnClickListener(addNumber("00"))
        number_1.setOnClickListener(addNumber("1"))
        number_2.setOnClickListener(addNumber("2"))
        number_3.setOnClickListener(addNumber("3"))
        number_4.setOnClickListener(addNumber("4"))
        number_5.setOnClickListener(addNumber("5"))
        number_6.setOnClickListener(addNumber("6"))
        number_7.setOnClickListener(addNumber("7"))
        number_8.setOnClickListener(addNumber("8"))
        number_9.setOnClickListener(addNumber("9"))

        point.setOnClickListener {
            if (!cNumber.contains('.')) {
                formula.text = "${formula.text}."
                cNumber = "$cNumber."
            }
        }

        percentage.setOnClickListener {
            equalOnClickListener().onClick(it)
            try {
                val n = cNumber.toDouble()
                cNumber = (n / 100).toString()
                formula.text = cNumber
            } catch (e: Exception) { formula.text = "Error" }
        }

        sign.setOnClickListener {
            equalOnClickListener().onClick(it)
            if (cNumber.first() == '-') {
                formula.text = formula.text.substring(1)
                cNumber = cNumber.substring(1)
            } else {
                formula.text = "-${formula.text}"
                cNumber = "-$cNumber"
            }
        }

        plus.setOnClickListener {
            formula.text = "${formula.text}+"
            addNumList(cNumber)
            operatorList.add(Operators.PLUS)
            cNumber = ""
        }
        minus.setOnClickListener {
            formula.text = "${formula.text}-"
            addNumList(cNumber)
            operatorList.add(Operators.MINUS)
            cNumber = ""
        }
        times.setOnClickListener {
            formula.text = "${formula.text}×"
            addNumList(cNumber)
            operatorList.add(Operators.TIMES)
            cNumber = ""
        }
        div.setOnClickListener {
            formula.text = "${formula.text}÷"
            addNumList(cNumber)
            operatorList.add(Operators.DIV)
            cNumber = ""
        }

        equal.setOnClickListener(equalOnClickListener())

        all_clear.setOnClickListener {
            formula.text = ""
            cNumber = ""
            numberList.clear()
            operatorList.clear()
        }
    }

    private fun addNumber(n: String) = View.OnClickListener {
        formula.text = "${formula.text}$n"
        cNumber = "$cNumber$n"
    }

    private fun equalOnClickListener() = View.OnClickListener {
        addNumList(cNumber)
        val result = calculate().let {
            if (it == it.toLong().toDouble()) "${it.toLong()}" else "$it"
        }
        formula.text = result
        cNumber = result
        numberList.clear()
        operatorList.clear()
    }

    private fun addNumList(str: String) {
        try {
            val n = str.toDouble()
            numberList.add(n)
        } catch (e: Exception) { formula.text = "Error" }
    }

    private fun calculate(): Double {
        var i = 0
        while (i < operatorList.size) {
            when (operatorList[i]) {
                Operators.MINUS -> {
                    operatorList[i] = Operators.PLUS
                    numberList[i + 1] = numberList[i + 1] * -1
                }
                Operators.TIMES -> {
                    numberList[i] = numberList[i] * numberList[i + 1]
                    numberList.removeAt(i + 1)
                    operatorList.removeAt(i)
                    i--
                }
                Operators.DIV -> {
                    numberList[i] = numberList[i] / numberList[i + 1]
                    numberList.removeAt(i + 1)
                    operatorList.removeAt(i)
                    i--
                }
                else -> {}
            }
            i++
        }
        return numberList.sum()
    }
}
