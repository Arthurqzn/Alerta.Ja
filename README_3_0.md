# AlertaJá 3.0 — Kotlin / Jetpack Compose

Aplicativo nativo Android com Jetpack Compose, Navigation Compose, ViewModel, Room e **novo Radar Urbano**.

## Novidades
- Quinta aba Radar com índice demonstrativo de atenção (alta=3, média=2, baixa=1), gráfico animado de 7 dias, bairros com ocorrências pendentes e orientações.
- Dashboard, cadastro, edição, exclusão, status, busca, filtros e insights da versão anterior preservados.
- Dados locais no Room; não são informações oficiais.

## Abrir no Android Studio
1. Extraia em `C:\Projetos\AlertaJaKotlin3` (fora do OneDrive).
2. Abra a pasta do projeto pelo Android Studio e aguarde o Gradle Sync.
3. Use JDK 17 e Android SDK compatível com o `build.gradle.kts`.
4. Se o Android Studio solicitar, configure o Gradle local: este pacote não inclui o binário `gradle-wrapper.jar`.
5. Execute **Build > Rebuild Project**, depois **Run**.

**Nota:** não foi possível compilar este projeto Android neste ambiente. Valide em emulador antes de entregar.
