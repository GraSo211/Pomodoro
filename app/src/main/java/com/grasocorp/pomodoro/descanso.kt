package com.grasocorp.pomodoro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.grasocorp.pomodoro.databinding.ActivityDescansoBinding

class descanso : AppCompatActivity() {
    private lateinit var binding: ActivityDescansoBinding
    private lateinit var timer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescansoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        timer = object : CountDownTimer(300_000, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                val tiempoSegundos = millisUntilFinished / 1000
                val tiempoSegundosPrint = (tiempoSegundos % 60)
                val tiempoMinutos = (tiempoSegundos / 60)
                binding.barraCircular.progress = tiempoSegundos.toInt()
                binding.tvCronometroDesc.setText(
                    tiempoMinutos.toString()
                        .padStart(2, ('0')) + ":" +
                            tiempoSegundosPrint.toString().padStart(2, '0'
                    )
                )
            }
            override fun onFinish() {
                pomodoro()
            }
        }
        timer.start()
        binding.btnParar.setOnClickListener{
            parar()
        }
    }

    private fun parar(){
        timer.cancel()
        pomodoro()
    }

    fun pomodoro(){
        val intento : Intent = Intent(this, MainActivity::class.java)
        startActivity(intento)
    }
}

