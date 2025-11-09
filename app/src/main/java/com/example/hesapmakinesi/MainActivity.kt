package com.example.hesapmakinesi
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
// Burada xml dosyasıyla bi köprü oluşturuyoruz
import com.example.hesapmakinesi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // 🚪 AKTİVİTE YAŞAM DÖNGÜSÜ: onCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // xml'i canlandırıyor buton gibi değişkenleri uygulamada oluşturuyor
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Başlangıçta gözükecek metini ayarlıyoruz. daha önce sıfır yazmamıza rağmen tekrar yazıyoruz
        binding.sonuc.text = "0"

        // Tüm ID'leri buraya ekliyoruz ki tek tek yazmayalım.
        val buttonIds = arrayOf(
            binding.button0.id, binding.button1.id, binding.button2.id, binding.button3.id,
            binding.button4.id, binding.button5.id, binding.button6.id, binding.button7.id,
            binding.button8.id, binding.button9.id, binding.buttonDot.id,
            binding.buttonAdd.id, binding.buttonSubtract.id, binding.buttonMultiply.id, binding.buttonDivide.id
        )

        for (id in buttonIds) {
            //burada o anki butonu buluyoruz (findViewByID ile) sonra aktif hale getiriyoruz yani bağlıyoruz
            val button = binding.root.findViewById<Button>(id)
            button.setOnClickListener(this::onButtonClick)
        }

        //AC butonu için tıklayınca 0 a bağlamamız lazım
        binding.buttonAc.setOnClickListener {
            binding.sonuc.text = "0"
        }

        // DEL Butonu (Silme)
        binding.buttonDel.setOnClickListener {
            val currentText = binding.sonuc.text.toString()
            if (currentText.length > 1) {
                // Son karakteri siliyoruz
                binding.sonuc.text = currentText.dropLast(1)
            } else {
                // Sadece 1 karakter varsa veya boşsa "0" yap
                binding.sonuc.text = "0"
            }
        }

        // Eşittir Butonu (Hesaplama)
        binding.buttonEquals.setOnClickListener {
            val ifade = binding.sonuc.text.toString()
            val sonuc = hesapla(ifade)

            binding.sonuc.text = sonuc
        }
    }

    private fun onButtonClick(view: View) {
        // Tıklanan butonun metnini al
        val buttonText = (view as Button).text.toString()

        appendToInput(buttonText)
    }

    private fun appendToInput(value: String) {
        val currentText = binding.sonuc.text.toString()
        // 1. Başlangıç "0" kontrolü

        if (currentText == "0" && value.matches(Regex("[0-9]"))) {
            binding.sonuc.text = value
        } else {
            binding.sonuc.append(value)
        }

    }
    private fun hesapla(ifade: String): String {
        if (ifade.isEmpty() || !ifade.contains(Regex("[+\\-*/]"))) {
            return ifade
        }


        try {
            val sayilar = ifade.split(Regex("[+\\-]"))
            val operatorler = ifade.filter { it == '+' || it == '-' }

            if (sayilar.size < 2 || operatorler.isEmpty()) {
                return ifade
            }


            var sonuc = sayilar[0].trim().toDouble()


            for (i in 1 until sayilar.size) {
                val sayi = sayilar[i].trim().toDouble()
                val operator = operatorler[i - 1]

                when (operator) {
                    '+' -> sonuc += sayi
                    '-' -> sonuc -= sayi
                }
            }

            return if (sonuc % 1.0 == 0.0) {
                sonuc.toLong().toString()
            } else {
                sonuc.toString()
            }

        } catch (e: Exception) {
            return "HATA"
        }
    }
}
