$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$target = Join-Path $root '.gradle-portable\gradle-8.9\bin\gradle.bat'
if (Test-Path $target) { Write-Host 'Gradle 8.9 pronto.'; exit 0 }
$dir = Join-Path $root '.gradle-portable'
New-Item -ItemType Directory -Force -Path $dir | Out-Null
$zip = Join-Path $dir 'gradle-8.9-bin.zip'
Write-Host 'Baixando Gradle 8.9 (necessita internet)...'
Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-8.9-bin.zip' -OutFile $zip
Expand-Archive -Path $zip -DestinationPath $dir -Force
Remove-Item $zip -Force
if (-not (Test-Path $target)) { throw 'Gradle nao encontrado apos extracao.' }
Write-Host 'Gradle pronto.'
