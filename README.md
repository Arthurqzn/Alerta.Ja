# AlertaJá — Urban Intelligence | Global Solution 2026/2

**Disciplina:** Android Kotlin Developer — FIAP  
**Professor:** Ewerton Luiz de Lima Carreira  
**Desafio:** CitizenTech — tecnologia que melhora a vida das pessoas

## Integrantes
Arthur Queiroz Nascimento
Felipe Gasparetto Ohara Sato
Gabriel de lima
RM555205
RM554315
RM99659


> Confirme nome e RM antes da entrega. Se houver outros integrantes, acrescente nome completo e RM de cada um (limite de 3 integrantes).

## 1. Problema e público beneficiado
Problemas urbanos como alagamentos, buracos, iluminação precária e descarte irregular de resíduos afetam o cotidiano dos moradores. Muitas vezes, os relatos ficam dispersos e difíceis de acompanhar.

O **AlertaJá** é um protótipo acadêmico para **moradores e lideranças comunitárias** registrarem, consultarem e acompanharem ocorrências urbanas em seu dispositivo. O app **não envia solicitações à prefeitura** nem substitui alertas oficiais de emergência.

## 2. Proposta de solução
Centralizar registros de problemas urbanos com título, bairro, categoria, gravidade, descrição e situação, permitindo acompanhamento por meio de uma lista, indicadores e gráficos. A interface utiliza preto, roxo e vermelho para facilitar a leitura dos dados.

## 3. Objetivo de Desenvolvimento Sustentável (ODS)
**ODS 11 — Cidades e Comunidades Sustentáveis.** Ao organizar informações sobre problemas urbanos e apoiar a observação do território, o AlertaJá estimula a participação cidadã e a atenção à qualidade de vida nas comunidades.

## 4. Funcionalidades
- **Painel:** indicadores de registros, pendências, resoluções e bairros.
- **Cadastro:** criar ocorrência com título, bairro, categoria, gravidade e descrição.
- **Validação:** título e bairro obrigatórios.
- **Registros:** pesquisar, filtrar e consultar ocorrências.
- **Ação adicional:** editar, excluir com confirmação e alterar status.
- **Insights e Radar:** gráficos e indicadores derivados dos registros locais.
- **Persistência:** registros armazenados no dispositivo por Room e disponíveis após reabrir o app.
- **Clima:** consulta de dados meteorológicos por cidade usando API pública Open-Meteo, com internet; **não é previsão GPS exata nem aviso oficial**.

## 5. Tecnologias utilizadas
- Kotlin e Android SDK
- Jetpack Compose / Material 3 — interface
- Navigation Compose — navegação
- ViewModel, StateFlow e Coroutines — estado e atualização da interface
- Room — persistência local
- Gradle Kotlin DSL — compilação
- Open-Meteo — dados meteorológicos públicos (funcionalidade complementar)

## 6. Como executar no Android Studio
1. Extraia o projeto ZIP em uma pasta local, por exemplo `C:\Projetos\AlertaJaKotlin`.
2. No Android Studio, escolha **File > Open** e selecione a pasta que contém `settings.gradle.kts`.
3. Configure **JDK 17** e instale o **Android SDK API 35** pelo SDK Manager.
4. Aguarde a sincronização do Gradle e o download das dependências.
5. Escolha um emulador Android (API 26 ou superior) ou celular com depuração USB.
6. Clique em **Run ▶**. Para testar Clima, o dispositivo precisa de internet.

**Atenção ao Gradle:** o ZIP original do projeto não incluía `gradle-wrapper.jar`. Se o Android Studio solicitar o wrapper, consulte `INICIAR_NO_WINDOWS.bat` e `scripts/preparar-gradle.ps1`, que preparam o Gradle portátil, ou configure uma instalação local do Gradle 8.9. Não marque a compilação como comprovada antes de executá-la no seu computador.

## 7. Fluxo principal para demonstrar ao professor
1. Abra **Painel** e mostre os indicadores.
2. Acesse **Registrar** e tente salvar com título vazio para demonstrar a validação.
3. Preencha o formulário e salve uma ocorrência real de teste.
4. Abra **Registros** e localize o registro.
5. Edite ou altere o status; mostre o resultado na lista e no painel.
6. **Feche completamente o aplicativo** e abra novamente; mostre o mesmo registro persistido.
7. Mostre os gráficos em **Insights/Radar** e, opcionalmente, a aba **Clima**.

## 8. Evidências visuais obrigatórias

> **As imagens devem ser capturas reais do aplicativo executando no Android Studio/emulador ou celular.** Não substitua por imagens geradas ou protótipos.

### Imagem 1 — Tela inicial / dashboard
<img width="372" height="756" alt="image" src="https://github.com/user-attachments/assets/bdc5b19f-a633-4271-84db-1420f50d1a3a" />


**O que comprova:** identidade visual, contextualização e acesso às funcionalidades.

### Imagem 2 — Registro criado
<img width="357" height="758" alt="image" src="https://github.com/user-attachments/assets/a5f9ca95-165e-4c4c-a290-7445096ce365" />


**O que comprova:** cadastro funcional e atualização da interface.

### Imagem 3 — Ação adicional
<img width="358" height="757" alt="image" src="https://github.com/user-attachments/assets/29360830-ac8f-4a3e-852a-a17f342a9e6e" />


**O que comprova:** edição, alteração de status ou exclusão funcional.

### Imagem 5 — Persistência após reabrir
<img width="352" height="202" alt="image" src="https://github.com/user-attachments/assets/8613d728-a5f9-471b-9059-d143d802158f" />


**O que comprova:** registro mantido após fechar completamente e reabrir o aplicativo, com Room.

### Imagem 6 — Insights / gráficos (complementar)
<img width="360" height="763" alt="image" src="https://github.com/user-attachments/assets/e7dca847-77b2-4258-912f-833cba8f447f" />

**O que mostra:** indicadores calculados a partir dos registros.

### Imagem 7 — Clima (complementar)
<img width="377" height="756" alt="image" src="https://github.com/user-attachments/assets/efa69e54-efd9-4296-a9f6-5031e0b80b2b" />

**O que mostra:** consulta meteorológica com conexão ativa.

**IMPORTANTE:** As referências acima só aparecerão como imagens depois que você salvar os PNGs com esses nomes dentro da pasta `evidencias`. Antes disso, os links ficarão sem imagem — não envie assim ao professor.

## 9. Como tirar e inserir as imagens
1. Execute o aplicativo no emulador Android.
2. Navegue até a tela desejada e deixe o conteúdo legível.
3. No emulador, clique no ícone **Take Screenshot** (câmera) da barra lateral; ou use **Logcat / Device Manager** conforme a versão do Android Studio.
4. Salve o PNG no computador.
5. Renomeie **exatamente** como a legenda correspondente acima (`01-dashboard.png`, `02-cadastro.png` etc.).
6. Copie para a pasta **`evidencias`**, ao lado deste README.
7. Abra `README.md` no VS Code e pressione **Ctrl+Shift+V** para visualizar a prévia Markdown. Confirme que todas as imagens aparecem.
8. Para a imagem de persistência: feche o app por completo, abra novamente e só então capture a tela.
9. Não capture informações pessoais ou localização residencial precisa sem necessidade.

### Estrutura esperada
```text
AlertaJa_Kotlin_Entrega_FIAP/
├── README.md
├── app/
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── evidencias/
    ├── 01-dashboard.png
    ├── 02-cadastro.png
    ├── 03-registro-criado.png
    ├── 04-acao-adicional.png
    ├── 05-persistencia-room.png
    ├── 06-insights.png
    └── 07-clima.png
```

## 10. Entrega FIAP
Conforme o enunciado da disciplina, entregar até **19/11/2026 às 23h59 (horário de Brasília)**:
1. **Projeto Android completo em `.zip`**, sem pastas `build`, caches ou arquivos locais desnecessários.
2. **README.md** com problema, público, proposta, ODS, funcionalidades, tecnologias, instruções, integrantes/RMs e capturas reais.
3. **Vídeo pitch**, conforme as orientações institucionais da Global Solution.

**Checklist final:** [ ] Projeto compila  [ ] Cadastro funciona  [ ] Ação adicional funciona  [ ] Room persiste  [ ] Imagens reais inseridas  [ ] Nome/RM conferidos  [ ] Pitch gravado

> A documentação descreve o comportamento esperado do projeto. Confirme tudo em execução antes de entregar; a nota é definida pelo professor.
