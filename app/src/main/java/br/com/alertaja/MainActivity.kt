package br.com.alertaja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.alertaja.data.Occurrence
import br.com.alertaja.ui.*
import br.com.alertaja.viewmodel.OccurrenceViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val vm: OccurrenceViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AlertaTheme { MainApp(vm) } }
    }
}

@Composable
fun MainApp(vm: OccurrenceViewModel) {
    val nav = rememberNavController()
    val records by vm.occurrences.collectAsStateWithLifecycle()
    Scaffold(containerColor = Bg, bottomBar = {
        NavigationBar(containerColor = Surface) {
            listOf(Triple("home", "Início", Icons.Filled.Home), Triple("records", "Registros", Icons.Filled.List),
                Triple("new", "Registrar", Icons.Filled.AddCircle), Triple("insights", "Insights", Icons.Filled.Analytics), Triple("radar", "Radar", Icons.Filled.Warning), Triple("weather", "Clima", Icons.Filled.Cloud)).forEach { (route,label,icon) ->
                val selected = nav.currentBackStackEntry?.destination?.route == route
                NavigationBarItem(selected = selected, onClick = { nav.navigate(route) { launchSingleTop = true } },
                    icon = { Icon(icon, contentDescription = label) }, label = { Text(label) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Purple, selectedTextColor = Purple, indicatorColor = Purple.copy(alpha=.15f)))
            }
        }
    }) { pad ->
        NavHost(navController = nav, startDestination = "home", modifier = Modifier.padding(pad)) {
            composable("home") { Home(records, nav) }
            composable("records") { Records(records, vm, nav) }
            composable("new") { Editor(vm, nav, null) }
            composable("insights") { Insights(records) }
            composable("radar") { Radar(records) }
            composable("weather") { WeatherScreen() }
            composable("edit/{id}") { entry -> Editor(vm, nav, records.firstOrNull { it.id == entry.arguments?.getString("id")?.toLongOrNull() }) }
        }
    }
}

@Composable fun Heading(kicker: String, title: String, subtitle: String = "") {
    Text(kicker.uppercase(), color = Purple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(5.dp))
    Text(title, fontSize = 29.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
    if (subtitle.isNotBlank()) { Spacer(Modifier.height(5.dp)); Text(subtitle, color = Muted, fontSize = 13.sp) }
}
@Composable fun GlassCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(18.dp), content = content)
    }
}
@Composable fun Metric(label: String, value: String, tint: Color, modifier: Modifier = Modifier) {
    GlassCard(modifier) {
        Text(label, color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(10.dp))
        Text(value, color = tint, fontSize = 29.sp, fontWeight = FontWeight.Bold)
    }
}
@Composable fun Home(records: List<Occurrence>, nav: NavHostController) {
    val pending = records.count { it.status == "Pendente" }
    val solved = records.size - pending
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Heading("Urban intelligence", "AlertaJá 2.0", "Tecnologia cidadã para uma cidade mais segura") }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.Transparent), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.background(Brush.linearGradient(listOf(Color(0xFF372058), Color(0xFF4C1A31), Surface))).padding(22.dp)) {
                    Text("MONITORAMENTO COMUNITÁRIO", color = Color(0xFFE9C7FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Sua cidade. Sua voz.", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Registre riscos urbanos, acompanhe soluções e visualize indicadores locais.", color = Color.White.copy(alpha=.85f))
                    Spacer(Modifier.height(15.dp))
                    Button(onClick = { nav.navigate("new") }) { Icon(Icons.Filled.Add, null); Spacer(Modifier.width(6.dp)); Text("Nova ocorrência") }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Metric("Total", records.size.toString(), Purple, Modifier.weight(1f))
                Metric("Pendentes", pending.toString(), Red, Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Metric("Resolvidos", solved.toString(), Color(0xFF5EE6B4), Modifier.weight(1f))
                Metric("Bairros", records.map { it.neighborhood.lowercase() }.distinct().size.toString(), Color.White, Modifier.weight(1f))
            }
        }
        item { Text("Atividade recente", fontWeight = FontWeight.Bold, fontSize = 19.sp) }
        if (records.isEmpty()) item { EmptyState("Nenhuma ocorrência registrada. Comece criando seu primeiro alerta.") }
        items(records.take(3), key = { it.id }) { OccurrenceCard(it, onEdit = { nav.navigate("edit/${it.id}") }, onStatus = {}, onDelete = {}, showActions = false) }
        item { Text("ODS 11 • Cidades e comunidades sustentáveis", color = Muted, fontSize = 12.sp) }
    }
}

@Composable fun EmptyState(message: String) {
    GlassCard(Modifier.fillMaxWidth()) { Icon(Icons.Filled.Info, null, tint = Purple); Spacer(Modifier.height(8.dp)); Text(message, color = Muted) }
}

@Composable fun OccurrenceCard(item: Occurrence, onEdit: () -> Unit, onStatus: () -> Unit, onDelete: () -> Unit, showActions: Boolean = true) {
    var confirmDelete by remember { mutableStateOf(false) }
    if (confirmDelete) AlertDialog(onDismissRequest = { confirmDelete = false }, title = { Text("Excluir ocorrência?") },
        text = { Text("Essa ação não pode ser desfeita.") }, confirmButton = { TextButton(onClick = { confirmDelete = false; onDelete() }) { Text("Excluir", color = Red) } },
        dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancelar") } })
    GlassCard(Modifier.fillMaxWidth().animateContentSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(item.category.uppercase(), color = Purple, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text(item.severity, color = if (item.severity == "Alta") Red else Muted, fontSize = 12.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(item.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("${item.neighborhood} • ${SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(item.createdAt))}", color = Muted, fontSize = 12.sp)
        if (item.description.isNotBlank()) { Spacer(Modifier.height(8.dp)); Text(item.description, color = Muted, fontSize = 13.sp, maxLines = 3, overflow = TextOverflow.Ellipsis) }
        Spacer(Modifier.height(8.dp))
        Text("● ${item.status}", color = if (item.status == "Resolvido") Color(0xFF5EE6B4) else Red, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
        if (showActions) {
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TextButton(onClick = onEdit) { Text("Editar") }
                TextButton(onClick = onStatus) { Text(if (item.status == "Pendente") "Resolver" else "Reabrir") }
                TextButton(onClick = { confirmDelete = true }) { Text("Excluir", color = Red) }
            }
        }
    }
}

@Composable fun Records(records: List<Occurrence>, vm: OccurrenceViewModel, nav: NavHostController) {
    var search by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("Todas") }
    val visible = records.filter { (filter == "Todas" || it.status == filter) &&
        (it.title.contains(search, ignoreCase = true) || it.neighborhood.contains(search, ignoreCase = true) || it.category.contains(search, ignoreCase = true)) }
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Heading("Central de ocorrências", "Registros", "Pesquise, edite e acompanhe o andamento") }
        item { OutlinedTextField(value = search, onValueChange = { search = it }, label = { Text("Pesquisar título, bairro ou categoria") }, leadingIcon = { Icon(Icons.Filled.Search, null) }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                listOf("Todas", "Pendente", "Resolvido").forEach { status ->
                    FilterChip(selected = filter == status, onClick = { filter = status }, label = { Text(status) })
                }
            }
        }
        if (visible.isEmpty()) item { EmptyState("Nenhum registro encontrado para este filtro.") }
        items(visible, key = { it.id }) { item -> OccurrenceCard(item, { nav.navigate("edit/${item.id}") }, { vm.toggleStatus(item) }, { vm.delete(item) }) }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable fun Editor(vm: OccurrenceViewModel, nav: NavHostController, existing: Occurrence?) {
    var title by remember(existing?.id) { mutableStateOf(existing?.title ?: "") }
    var neighborhood by remember(existing?.id) { mutableStateOf(existing?.neighborhood ?: "") }
    var description by remember(existing?.id) { mutableStateOf(existing?.description ?: "") }
    var category by remember(existing?.id) { mutableStateOf(existing?.category ?: "Alagamento") }
    var severity by remember(existing?.id) { mutableStateOf(existing?.severity ?: "Média") }
    var error by remember { mutableStateOf(false) }
    var saved by remember { mutableStateOf(false) }
    val categories = listOf("Alagamento", "Buraco", "Iluminação", "Lixo", "Deslizamento", "Outro")
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
        item { Heading("Participação cidadã", if (existing == null) "Novo alerta" else "Editar alerta", "Descreva o problema observado na sua região") }
        item { OutlinedTextField(title, { title = it; error = false }, modifier = Modifier.fillMaxWidth(), label = { Text("Título *") }, isError = error && title.isBlank(), singleLine = true) }
        item { OutlinedTextField(neighborhood, { neighborhood = it; error = false }, modifier = Modifier.fillMaxWidth(), label = { Text("Bairro *") }, isError = error && neighborhood.isBlank(), singleLine = true) }
        item { Text("Categoria", color = Muted); FlowRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) { categories.forEach { c -> FilterChip(selected = category == c, onClick = { category = c }, label = { Text(c) }) } } }
        item { Text("Gravidade", color = Muted); Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { listOf("Baixa", "Média", "Alta").forEach { s -> FilterChip(selected = severity == s, onClick = { severity = s }, label = { Text(s) }) } } }
        item { OutlinedTextField(description, { description = it }, modifier = Modifier.fillMaxWidth().heightIn(min = 110.dp), label = { Text("Descrição do problema") }, minLines = 3) }
        if (error) item { Text("Preencha o título e o bairro para salvar.", color = Red) }
        if (saved) item { Text("Registro salvo com sucesso!", color = Color(0xFF5EE6B4)) }
        item {
            Button(modifier = Modifier.fillMaxWidth().height(52.dp), onClick = {
                if (title.isBlank() || neighborhood.isBlank()) { error = true } else {
                    vm.save(title, category, neighborhood, description, severity, existing)
                    saved = true
                    nav.navigate("records") { popUpTo("home") }
                }
            }) { Icon(Icons.Filled.Save, null); Spacer(Modifier.width(8.dp)); Text(if (existing == null) "Salvar ocorrência" else "Salvar alterações") }
        }
        item { Text("Os registros ficam salvos somente neste dispositivo (Room).", color = Muted, fontSize = 12.sp) }
    }
}

@Composable fun Insights(records: List<Occurrence>) {
    val categories = records.groupingBy { it.category }.eachCount().toList().sortedByDescending { it.second }
    val neighborhoods = records.groupingBy { it.neighborhood.trim() }.eachCount().toList().sortedByDescending { it.second }
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        item { Heading("Inteligência urbana", "Insights", "Indicadores calculados a partir dos seus registros") }
        item { Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Metric("Ocorrências", records.size.toString(), Purple, Modifier.weight(1f))
            Metric("Alta gravidade", records.count { it.severity == "Alta" }.toString(), Red, Modifier.weight(1f))
        } }
        item { Text("Ocorrências por categoria", fontSize = 19.sp, fontWeight = FontWeight.Bold) }
        if (categories.isEmpty()) item { EmptyState("Cadastre ocorrências para visualizar os gráficos.") }
        items(categories) { (label, count) -> BarRow(label, count, records.size, Purple) }
        item { Text("Bairros com mais registros", fontSize = 19.sp, fontWeight = FontWeight.Bold) }
        items(neighborhoods.take(6)) { (label, count) -> BarRow(label, count, records.size, Red) }
        item { GlassCard(Modifier.fillMaxWidth()) {
            Text("Como interpretar?", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(7.dp))
            Text("Esses indicadores refletem apenas registros locais feitos neste aplicativo. Não representam estatísticas oficiais, alertas meteorológicos ou previsões de risco.", color = Muted, fontSize = 13.sp)
        } }
    }
}
@Composable fun BarRow(label: String, count: Int, total: Int, color: Color) {
    val target = if (total == 0) 0f else count.toFloat() / total
    val progress by animateFloatAsState(targetValue = target, label = "progresso")
    GlassCard(Modifier.fillMaxWidth()) {
        Row { Text(label, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold); Text("$count", color = color) }
        Spacer(Modifier.height(10.dp))
        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(7.dp), color = color, trackColor = Color(0xFF302A3C))
    }
}


@Composable fun Radar(records: List<Occurrence>) {
    val pending = records.filter { it.status == "Pendente" }
    val high = pending.count { it.severity == "Alta" }
    val medium = pending.count { it.severity == "Média" }
    val low = pending.size - high - medium
    val score = if (records.isEmpty()) 0 else ((high * 3 + medium * 2 + low) * 100 / (records.size * 3)).coerceIn(0, 100)
    val neighborhoods = pending.groupingBy { it.neighborhood.trim() }.eachCount().toList().sortedByDescending { it.second }
    val today = java.util.Calendar.getInstance().apply { set(java.util.Calendar.HOUR_OF_DAY, 0); set(java.util.Calendar.MINUTE, 0); set(java.util.Calendar.SECOND, 0); set(java.util.Calendar.MILLISECOND, 0) }
    val days = (6 downTo 0).map { offset -> (today.clone() as java.util.Calendar).apply { add(java.util.Calendar.DAY_OF_YEAR, -offset) }.timeInMillis }
    val daily = days.map { start -> records.count { it.createdAt >= start && it.createdAt < start + 86400000L } }
    val peak = (daily.maxOrNull() ?: 0).coerceAtLeast(1)
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        item { Heading("Urban intelligence", "Radar urbano", "Análise dos relatos registrados neste aparelho") }
        item {
            GlassCard(Modifier.fillMaxWidth()) {
                Text("ÍNDICE DE ATENÇÃO • DEMONSTRATIVO", color = Purple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Text("$score/100", color = Red, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(8.dp))
                val progress by animateFloatAsState(targetValue = score / 100f, label = "indice")
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(9.dp), color = Red, trackColor = Color(0xFF322538))
                Spacer(Modifier.height(12.dp))
                Text("Soma ponderada de relatos pendentes (alta=3, média=2, baixa=1) sobre o máximo possível. Não é previsão nem medida de risco real.", color = Muted, fontSize = 12.sp)
            }
        }
        item { Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            Metric("Alta", high.toString(), Red, Modifier.weight(1f))
            Metric("Média", medium.toString(), Purple, Modifier.weight(1f))
            Metric("Baixa", low.toString(), Color(0xFF5EE6B4), Modifier.weight(1f))
        } }
        item {
            GlassCard(Modifier.fillMaxWidth()) {
                Text("Atividade • últimos 7 dias", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth().height(130.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Bottom) {
                    daily.forEachIndexed { i, value ->
                        val target = value.toFloat() / peak
                        val progress by animateFloatAsState(targetValue = target, label = "dia$i")
                        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                            Text("$value", color = Muted, fontSize = 11.sp)
                            Spacer(Modifier.height(5.dp))
                            Box(Modifier.fillMaxWidth().height((progress * 77 + 3).dp).background(if (i == 6) Red else Purple, RoundedCornerShape(7.dp)))
                            Spacer(Modifier.height(6.dp))
                            Text(SimpleDateFormat("dd/MM", Locale("pt", "BR")).format(Date(days[i])), color = Muted, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
        item { Text("Bairros com registros pendentes", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
        if (neighborhoods.isEmpty()) item { EmptyState("Nenhum registro pendente para analisar.") }
        items(neighborhoods.take(6)) { (label, count) -> BarRow(label, count, neighborhoods.first().second, Purple) }
        item { GlassCard(Modifier.fillMaxWidth()) {
            Text("Orientações preventivas", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Spacer(Modifier.height(9.dp))
            Text("• Não atravesse ruas alagadas.\n• Em emergência, procure os órgãos oficiais.\n• Não divulgue dados pessoais nos relatos.", color = Muted)
            Spacer(Modifier.height(10.dp))
            Text("Defesa Civil 199 • Bombeiros 193", color = Red, fontWeight = FontWeight.Bold)
        } }
        item { Text("Indicadores baseados exclusivamente em registros locais. Não são dados oficiais ou alertas meteorológicos.", color = Muted, fontSize = 11.sp) }
    }
}
