# AlertaJá Kotlin — Clima Real

## O que mudou
- Aba **Clima** adicionada à navegação.
- Consulta real via Open-Meteo: geocodificação da cidade, temperatura, umidade, vento, chance de chuva por hora e previsão de 7 dias.
- Requisição em coroutine com Dispatchers.IO, timeout e tratamento de falhas.
- Permissão INTERNET no AndroidManifest.xml.
- Dados comunitários do Radar continuam separados da previsão meteorológica.

## Como rodar
Abra esta pasta no Android Studio, aguarde Gradle Sync, selecione emulador com acesso à internet e clique Run. Emulador sem internet não poderá obter dados. Necessário Android SDK 35 e JDK 17. O projeto mantém as dependências da versão anterior.

## Limitações
A cidade é escolhida por texto, não por GPS. A API retorna previsão para as coordenadas centrais da cidade encontrada; não há promessa de precisão por rua. Previsões não são alertas oficiais. O serviço pode ficar indisponível. Projeto-fonte não foi compilado neste ambiente.

Serviço de dados: https://open-meteo.com/ (consulte condições de uso e atribuição para eventual distribuição comercial).
