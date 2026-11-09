package br.com.alertaja

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alertaja.ui.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

private data class ForecastDay(val date: String, val max: Double, val min: Double, val rain: Int, val code: Int)
private data class WeatherResult(val city: String, val temperature: Double, val humidity: Int, val wind: Double, val code: Int, val rain: Int, val days: List<ForecastDay>)

private fun condition(code: Int): String = when (code) {
    0 -> "Céu limpo"
    1, 2, 3 -> "Parcialmente nublado"
    45, 48 -> "Nevoeiro"
    51, 53, 55, 56, 57 -> "Garoa"
    61, 63, 65, 66, 67, 80, 81, 82 -> "Chuva"
    71, 73, 75, 77, 85, 86 -> "Neve"
    95, 96, 99 -> "Trovoadas"
    else -> "Condição não identificada"
}

private fun readJson(address: String): JSONObject {
    val conn = URL(address).openConnection() as HttpURLConnection
    conn.connectTimeout = 12000
    conn.readTimeout = 12000
    conn.setRequestProperty("Accept", "application/json")
    return try {
        if (conn.responseCode !in 200..299) error("Serviço meteorológico indisponível (${conn.responseCode})")
        JSONObject(conn.inputStream.bufferedReader().use { it.readText() })
    } finally { conn.disconnect() }
}

private suspend fun fetchWeather(query: String): WeatherResult = withContext(Dispatchers.IO) {
    val name = URLEncoder.encode(query.trim(), "UTF-8")
    val geo = readJson("https://geocoding-api.open-meteo.com/v1/search?name=$name&count=10&language=pt&format=json")
    val matches = geo.optJSONArray("results") ?: error("Cidade não encontrada. Informe cidade e estado, por exemplo: São Paulo")
    val first = (0 until matches.length()).map { matches.getJSONObject(it) }
        .firstOrNull { it.optString("country_code") == "BR" } ?: matches.getJSONObject(0)
    val lat = first.getDouble("latitude")
    val lon = first.getDouble("longitude")
    val city = "${first.getString("name")} • ${first.optString("admin1", first.optString("country"))}"
    val url = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&hourly=precipitation_probability&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max&timezone=auto&forecast_days=7"
    val data = readJson(url)
    val current = data.getJSONObject("current")
    val hourly = data.getJSONObject("hourly")
    val times = hourly.getJSONArray("time")
    val probabilities = hourly.getJSONArray("precipitation_probability")
    val now = current.getString("time")
    val index = (0 until times.length()).firstOrNull { times.getString(it) >= now } ?: 0
    val daily = data.getJSONObject("daily")
    val dates = daily.getJSONArray("time")
    val highs = daily.getJSONArray("temperature_2m_max")
    val lows = daily.getJSONArray("temperature_2m_min")
    val rain = daily.getJSONArray("precipitation_probability_max")
    val codes = daily.getJSONArray("weather_code")
    WeatherResult(city, current.getDouble("temperature_2m"), current.getInt("relative_humidity_2m"),
        current.getDouble("wind_speed_10m"), current.getInt("weather_code"), probabilities.optInt(index),
        (0 until dates.length()).map { ForecastDay(dates.getString(it), highs.getDouble(it), lows.getDouble(it), rain.optInt(it), codes.getInt(it)) })
}

@Composable
fun WeatherScreen() {
    var city by remember { mutableStateOf("São Paulo") }
    var requestedCity by remember { mutableStateOf("São Paulo") }
    var refresh by remember { mutableIntStateOf(0) }
    var result by remember { mutableStateOf<WeatherResult?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(requestedCity, refresh) {
        loading = true
        error = null
        try { result = fetchWeather(requestedCity) }
        catch (e: Exception) { result = null; error = e.localizedMessage ?: "Não foi possível consultar o clima" }
        finally { loading = false }
    }
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Heading("Meteorologia em tempo real", "Central do Clima", "Previsão por cidade • Fonte: Open-Meteo") }
        item {
            OutlinedTextField(value = city, onValueChange = { city = it }, modifier = Modifier.fillMaxWidth(),
                label = { Text("Digite sua cidade") }, singleLine = true, leadingIcon = { Icon(Icons.Filled.Search, null) })
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { requestedCity = city.trim(); refresh++ }, enabled = city.isNotBlank()) { Text("Consultar previsão") }
                OutlinedButton(onClick = { refresh++ }) { Icon(Icons.Filled.Refresh, null); Spacer(Modifier.width(5.dp)); Text("Atualizar") }
            }
        }
        if (loading) item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Purple) }
        if (error != null) item { GlassCard(Modifier.fillMaxWidth()) { Text("Não foi possível obter os dados", color = Red, fontWeight = FontWeight.Bold); Text(error.orEmpty(), color = Muted); Text("Verifique a internet e tente novamente.", color = Muted) } }
        result?.let { weather ->
            item {
                GlassCard(Modifier.fillMaxWidth()) {
                    Text(weather.city, color = Purple, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Row {
                        Icon(Icons.Filled.Cloud, contentDescription = null, tint = Purple, modifier = Modifier.size(45.dp))
                        Spacer(Modifier.width(12.dp))
                        Text(String.format(Locale("pt", "BR"), "%.1f°C", weather.temperature), fontSize = 35.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Text(condition(weather.code), color = Muted)
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Metric("Chuva agora*", "${weather.rain}%", Purple, Modifier.weight(1f))
                    Metric("Umidade", "${weather.humidity}%", Red, Modifier.weight(1f))
                }
            }
            item { GlassCard(Modifier.fillMaxWidth()) { Text("Vento", color = Muted); Text(String.format(Locale("pt", "BR"), "%.1f km/h", weather.wind), fontSize = 24.sp, fontWeight = FontWeight.Bold) } }
            item { Text("Próximos 7 dias", fontSize = 21.sp, fontWeight = FontWeight.Bold) }
            items(weather.days) { day ->
                GlassCard(Modifier.fillMaxWidth()) {
                    Text(day.date, color = Purple, fontWeight = FontWeight.Bold)
                    Text(condition(day.code))
                    Text(String.format(Locale("pt", "BR"), "Máx. %.0f°C  •  Mín. %.0f°C  •  Chuva até %d%%", day.max, day.min, day.rain), color = Muted)
                }
            }
            item { Text("* Probabilidade de precipitação na hora consultada, não volume de chuva. Previsões são estimativas e podem mudar. Fonte: Open-Meteo. Dados de ocorrências são comunitários e independentes do serviço meteorológico.", color = Muted, fontSize = 12.sp) }
        }
    }
}
