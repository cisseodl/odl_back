# Script de déploiement sur Elastic Beanstalk
# Usage: .\deploy-to-eb.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Déploiement sur Elastic Beanstalk" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Vérifier que le JAR existe
$jarPath = "target\awsodclearning.jar"
if (-not (Test-Path $jarPath)) {
    Write-Host "ERREUR: Le fichier JAR n'existe pas: $jarPath" -ForegroundColor Red
    Write-Host "Exécutez d'abord: mvn clean package -DskipTests" -ForegroundColor Yellow
    exit 1
}

Write-Host "✓ JAR trouvé: $jarPath" -ForegroundColor Green
Write-Host ""

# Vérifier la taille du JAR
$jarSize = (Get-Item $jarPath).Length / 1MB
Write-Host "Taille du JAR: $([math]::Round($jarSize, 2)) MB" -ForegroundColor Cyan
Write-Host ""

# Vérifier si EB CLI est installé
$ebInstalled = Get-Command eb -ErrorAction SilentlyContinue
if (-not $ebInstalled) {
    Write-Host "EB CLI n'est pas installé." -ForegroundColor Yellow
    Write-Host "Options:" -ForegroundColor Yellow
    Write-Host "1. Installer EB CLI: pip install awsebcli" -ForegroundColor Yellow
    Write-Host "2. Utiliser la console AWS pour téléverser le JAR manuellement" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Le JAR est prêt pour le téléversement manuel:" -ForegroundColor Cyan
    Write-Host "  Chemin: $((Get-Item $jarPath).FullName)" -ForegroundColor White
    Write-Host ""
    Write-Host "Instructions:" -ForegroundColor Cyan
    Write-Host "1. Allez sur https://console.aws.amazon.com/elasticbeanstalk" -ForegroundColor White
    Write-Host "2. Sélectionnez votre environnement: ODC-Learning-Backend-env" -ForegroundColor White
    Write-Host "3. Cliquez sur 'Téléverser et déployer'" -ForegroundColor White
    Write-Host "4. Sélectionnez le fichier: $jarPath" -ForegroundColor White
    Write-Host "5. Entrez un label de version (ex: v1.0.0-2026-01-10)" -ForegroundColor White
    Write-Host "6. Cliquez sur 'Déployer'" -ForegroundColor White
    exit 0
}

Write-Host "✓ EB CLI est installé" -ForegroundColor Green
Write-Host ""

# Vérifier si l'environnement est initialisé
if (-not (Test-Path ".elasticbeanstalk")) {
    Write-Host "Initialisation de Elastic Beanstalk..." -ForegroundColor Yellow
    Write-Host "Exécutez: eb init" -ForegroundColor Yellow
    Write-Host "Puis: eb use ODC-Learning-Backend-env" -ForegroundColor Yellow
    exit 1
}

Write-Host "✓ Environnement Elastic Beanstalk initialisé" -ForegroundColor Green
Write-Host ""

# Déployer
Write-Host "Déploiement en cours..." -ForegroundColor Cyan
Write-Host ""

try {
    eb deploy
    Write-Host ""
    Write-Host "✓ Déploiement réussi!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Vérifiez le statut avec: eb status" -ForegroundColor Cyan
    Write-Host "Voir les logs avec: eb logs" -ForegroundColor Cyan
} catch {
    Write-Host ""
    Write-Host "ERREUR lors du déploiement:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host ""
    Write-Host "Alternative: Utilisez la console AWS pour téléverser le JAR manuellement" -ForegroundColor Yellow
    exit 1
}
