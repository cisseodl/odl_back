# Script pour créer le ZIP de déploiement Elastic Beanstalk
# Inclut: JAR + Procfile + .ebextensions + .platform (OBLIGATOIRE pour client_max_body_size / 413)
# Usage: .\build-deploy-zip.ps1   (après mvn package)

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
$jarName = "awsodclearning.jar"
$jarPath = Join-Path $root "target" $jarName
$zipName = "odl-back-deploy.zip"
$zipPath = Join-Path $root $zipName

if (-not (Test-Path $jarPath)) {
    Write-Host "JAR introuvable. Lancez d'abord: mvnw.cmd clean package -DskipTests" -ForegroundColor Red
    exit 1
}

$tempDir = Join-Path $root "deploy-temp"
if (Test-Path $tempDir) { Remove-Item $tempDir -Recurse -Force }
New-Item -ItemType Directory -Path $tempDir | Out-Null

Copy-Item $jarPath (Join-Path $tempDir $jarName)
Copy-Item (Join-Path $root "Procfile") $tempDir
Copy-Item (Join-Path $root ".ebextensions") (Join-Path $tempDir ".ebextensions") -Recurse
Copy-Item (Join-Path $root ".platform") (Join-Path $tempDir ".platform") -Recurse

if (Test-Path $zipPath) { Remove-Item $zipPath -Force }
Compress-Archive -Path (Join-Path $tempDir "*") -DestinationPath $zipPath
Remove-Item $tempDir -Recurse -Force

Write-Host "OK: $zipPath" -ForegroundColor Green
Write-Host "Deployez ce ZIP sur Elastic Beanstalk (Upload and deploy) pour que la limite 500M soit appliquee."
