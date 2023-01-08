package com.grasocorp.pomodoro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.work.WorkManager
import com.grasocorp.pomodoro.databinding.ActivityMainBinding

private var iniciado = false
private var pausado = false
private var minutos= ""
private var segundos = ""
private var tiempoCron = 0.toLong()

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WorkManager.getInstance(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIniciar.setOnClickListener {
            iniciar()
        }

        binding.btnPausar.setOnClickListener {
            pausar()
        }

        binding.btnParar.setOnClickListener {
            parar()
        }
    }

    private fun cronometro(tiempo: Long) {
        timer = object : CountDownTimer(tiempo, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                val tiempoSegundos = millisUntilFinished / 1000
                val tiempoSegundosPrint = (tiempoSegundos % 60)
                val tiempoMinutos = (tiempoSegundos / 60)
                binding.barraCircular.progress = tiempoSegundos.toInt()
                binding.tvCronometro.setText(tiempoMinutos.toString().padStart(2,('0')) + ":"
                        + tiempoSegundosPrint.toString().padStart(2,'0'))
                if(tiempoMinutos.toInt() == 24){
                    binding.tvEsfuerzo.text = "A Concentrarse!"
                }else if(tiempoMinutos.toInt()== 20){
                    binding.tvEsfuerzo.text = "Aun Falta Mucho!"
                }else if(tiempoMinutos.toInt()== 10){
                    binding.tvEsfuerzo.text = "Ya Casi!"
                }else if(tiempoMinutos.toInt()== 5){
                    binding.tvEsfuerzo.text = "Ultimo Esfuerzo!"
                }
            }
            override fun onFinish() {
                iniciado = false
                pausado= true
                descanso()
            }
        }
        timer.start()
    }

    private fun iniciar(){
        if(!iniciado){
            if(segundos != "" ){
                val tiempo = ((minutos.toInt() * 60 )+ segundos.toInt())*1000
                tiempoCron = tiempo.toLong()
            }else {
                tiempoCron = 1500_000
            }
            cronometro(tiempoCron)
            iniciado = true
            pausado = false
            minutos= ""
            segundos= ""
            binding.btnIniciar.visibility = View.INVISIBLE
            binding.btnPausar.visibility = View.VISIBLE
            binding.tvIniciar.text = "Pausar"
        }
    }

    private fun pausar(){
        if(!pausado){
            val tiempoPausa = binding.tvCronometro.text.toString()
            var dosPuntos = false
            for(c in tiempoPausa){
                if( c != ':' && !dosPuntos){
                    minutos += c

                }else if(c != ':' && dosPuntos){
                    segundos += c
                }else{
                    dosPuntos = true
                }
            }
            timer.cancel()
            iniciado = false
            pausado = true
            binding.btnIniciar.visibility = View.VISIBLE
            binding.btnPausar.visibility = View.INVISIBLE
            binding.tvIniciar.text = "Iniciar"
        }
    }

    private fun parar(){
        if(pausado){
            timer.cancel()
            binding.tvCronometro.text = "25:00"
            iniciado = false
            minutos=""
            segundos=""
            binding.btnIniciar.visibility = View.VISIBLE
            binding.btnPausar.visibility = View.INVISIBLE
            binding.tvEsfuerzo.text = ""
            binding.barraCircular.progress = 1500
        }
    }

    fun descanso(){
        val intento : Intent = Intent(this, descanso::class.java)
        startActivity(intento)
    }
}

