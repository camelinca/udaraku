package com.example.udaraku

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.udaraku.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputPredictActivity : AppCompatActivity() {

    private lateinit var etPm10: EditText
    private lateinit var etSo2: EditText
    private lateinit var etCo: EditText
    private lateinit var etO3: EditText
    private lateinit var etNo2: EditText
    private lateinit var etMax: EditText
    private lateinit var btnPrediksi: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_predict)

        etPm10 = findViewById(R.id.et_pm10)
        etSo2 = findViewById(R.id.et_so2)
        etCo = findViewById(R.id.et_co)
        etO3 = findViewById(R.id.et_o3)
        etNo2 = findViewById(R.id.et_no2)
        etMax = findViewById(R.id.et_max)
        btnPrediksi = findViewById(R.id.btn_prediksi)

        btnPrediksi.setOnClickListener {
            sendPredictionRequest()
        }
    }

    private fun sendPredictionRequest() {
        // Extract text inputs from EditText views
        val pm10 = etPm10.text.toString()
        val so2 = etSo2.text.toString()
        val co = etCo.text.toString()
        val o3 = etO3.text.toString()
        val no2 = etNo2.text.toString()
        val max_val = etMax.text.toString()

        if (pm10.isEmpty() || so2.isEmpty() || co.isEmpty() || o3.isEmpty() || no2.isEmpty() || max_val.isEmpty()) {
            Toast.makeText(this@InputPredictActivity, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        ApiClient.instance.predictAirQuality(pm10, so2, co, o3, no2, max_val)
            .enqueue(object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()?.get(0) ?: "Tidak ada respons"
                        showPredictionDialog(result)
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string() ?: "Tidak ada detail kesalahan"

                        Log.e("API Error", "Kode: $errorCode, Pesan: $errorMessage")
                        Toast.makeText(this@InputPredictActivity, "$errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    // Display error message if the API call failed
                    Log.e("API Error", "Kesalahan: ${t.message}")
                    Toast.makeText(this@InputPredictActivity, "Kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showPredictionDialog(result: String) {
        val message = when (result) {
            "BAIK" -> """
            Prediksi Sehat:
            Kualitas udara dalam kondisi aman dan tidak menimbulkan risiko bagi kesehatan masyarakat umum. 
            Petugas dapat mengedukasi masyarakat tentang pentingnya menjaga kebersihan lingkungan agar kualitas udara tetap baik. 
            Selain itu, lakukan pemantauan rutin terhadap kualitas udara untuk memastikan kondisi tetap stabil.
        """.trimIndent()

            "SEDANG" -> """
            Prediksi Sedang:
            Kualitas udara cukup baik tetapi dapat berdampak pada kelompok sensitif, seperti anak-anak, lansia, dan penderita penyakit pernapasan. 
            Petugas dapat memberikan pemberitahuan kepada masyarakat, khususnya kelompok sensitif, untuk mengurangi aktivitas di luar ruangan. 
            Selain itu, disarankan penggunaan masker untuk mengurangi paparan udara yang kurang sehat.
        """.trimIndent()

            "TIDAK SEHAT" -> """
            Prediksi Tidak Sehat:
            Kualitas udara buruk dan dapat menyebabkan gangguan kesehatan serius, terutama bagi kelompok rentan. 
            Petugas harus memberikan pengumuman darurat agar masyarakat tetap di dalam ruangan dan membagikan masker N95 jika memungkinkan. 
            Selain itu, hentikan aktivitas yang memperburuk kualitas udara, seperti pembakaran sampah atau aktivitas industri tanpa pengendalian emisi.
        """.trimIndent()

            else -> "Hasil prediksi tidak diketahui."
        }

        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Hasil Prediksi")
            .setMessage(message)
            .setPositiveButton("Tutup") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }
}