# AlertaJá 2.0 — Urban Intelligence (Kotlin nativo)

**Global Solution 2026/2 — FIAP — Android Kotlin Developer — CitizenTech**

## Integrantes
- **Nome completo:** PREENCHER
- **RM:** PREENCHER

## Problema e público
Moradores frequentemente encontram problemas urbanos como alagamentos, buracos, iluminação insuficiente e descarte irregular de lixo, sem uma maneira simples de organizar seus próprios relatos e acompanhar o andamento. O AlertaJá é um **protótipo acadêmico** voltado a moradores e lideranças comunitárias para **registrar e acompanhar ocorrências localmente**. Não encaminha registros à prefeitura e não oferece avisos oficiais.

## ODS
**ODS 11 — Cidades e Comunidades Sustentáveis.** A proposta contribui para a observação e organização de problemas urbanos, promovendo participação cidadã e informação comunitária.

## Funcionalidades
- Dashboard com total, pendentes, resolvidos e bairros distintos.
- Cadastro de ocorrências com título, bairro, categoria, gravidade e descrição.
- Validação de título e bairro obrigatórios.
- Lista com pesquisa e filtros por status.
- Edição, exclusão com confirmação e alteração de status (Pendente / Resolvido).
- Indicadores por categoria e bairro, com barras animadas.
- Persistência local com **Room**; estado reativo com **ViewModel + StateFlow**.
- Navegação entre Início, Registros, Registrar, Insights e Editar com **Navigation Compose**.
- Interface **Jetpack Compose / Material 3** com tema preto, roxo e vermelho.

## Tecnologias
Kotlin, Jetpack Compose, Material 3, Navigation Compose, Android ViewModel, StateFlow, Room, Coroutines, Gradle Kotlin DSL.

## Como executar no Android Studio
1. Descompacte o ZIP em uma pasta local simples (por exemplo `C:\Projetos\AlertaJaKotlin`).
2. Abra o **Android Studio** > **Open** e selecione a pasta do projeto.
3. Configure **JDK 17** e instale o Android SDK **API 35** no SDK Manager.
4. Aguarde o **Gradle Sync** e aceite os downloads de dependências.
5. Escolha um emulador Android (API 26 ou superior) ou celular com depuração USB e clique em **Run app**.
6. O projeto usa Gradle 8.9 / Android Gradle Plugin 8.7.3. Este ZIP inclui um **inicializador Windows** (`INICIAR_NO_WINDOWS.bat`) que baixa o Gradle 8.9 quando necessário e compila o APK. Para o Android Studio, configure **Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle distribution > Local installation**, apontando para `.gradle-portable/gradle-8.9` depois de executar o inicializador, caso a sincronização solicite o wrapper. O arquivo `gradle-wrapper.jar` não acompanha o ZIP.

## VS Code
Instale as extensões **Kotlin** e **Gradle for Java**, além de Android SDK e JDK 17. Abra a pasta e execute `INICIAR_NO_WINDOWS.bat` para baixar Gradle e gerar o APK; em seguida use o Android Studio para instalar/rodar no emulador. Para desenvolver e executar apps nativos Compose, **Android Studio é o caminho recomendado**.

## Fluxo principal de demonstração
1. Abra a tela inicial e observe os indicadores zerados.
2. Vá a **Registrar** e tente salvar sem título/bairro: o formulário deverá impedir.
3. Cadastre uma ocorrência válida.
4. Confira-a em **Registros** e **Insights**.
5. Altere o status para **Resolvido**, edite o registro e confira os indicadores.
6. Feche o app completamente, abra novamente e confirme que o registro continua lá (Room).
7. Demonstre a exclusão com confirmação.

## Evidências obrigatórias para a entrega
**Ainda precisam ser produzidas em execução real no seu computador:**
- `evidencias/01-home.png`: dashboard aberto.
- `evidencias/02-cadastro.png`: formulário preenchido.
- `evidencias/03-registro.png`: registro na lista.
- `evidencias/04-acao-adicional.png`: alteração de status ou edição.
- `evidencias/05-persistencia.png`: registro presente após fechar e reabrir.
- Vídeo pitch conforme orientações institucionais da FIAP.

> **Nota:** O código foi preparado como projeto Android, mas não foi compilado em emulador neste ambiente. É necessário executar o Gradle Sync e testar no Android Studio antes de entregar. A nota depende da avaliação do professor. Não inclua dados fictícios como evidência real.

## Correção de compilação (06/11/2026)
- Adicionada a anotação `@OptIn(ExperimentalLayoutApi::class)` ao formulário que utiliza `FlowRow`, corrigindo o erro do compilador Kotlin na linha 193 (`The API of this layout is experimental`).
- Esta correção é específica para o erro enviado; a compilação completa ainda precisa ser validada no Android Studio.
