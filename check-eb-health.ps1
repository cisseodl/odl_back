# Script pour vérifier la santé de l'environnement Elastic Beanstalk
# Usage: .\check-eb-health.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Vérification de la Santé Elastic Beanstalk" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Vérifier si EB CLI est installé
$ebInstalled = Get-Command eb -ErrorAction SilentlyContinue
if (-not $ebInstalled) {
    Write-Host "EB CLI n'est pas installé." -ForegroundColor Yellow
    Write-Host "Installez-le avec: pip install awsebcli" -ForegroundColor Yellow
    exit 1
}

Write-Host "✓ EB CLI est installé" -ForegroundColor Green
Write-Host ""

# Vérifier si l'environnement est initialisé
if (-not (Test-Path ".elasticbeanstalk")) {
    Write-Host "ERREUR: Environnement Elastic Beanstalk non initialisé" -ForegroundColor Red
    Write-Host "Exécutez d'abord: eb init" -ForegroundColor Yellow
    exit 1
}

Write-Host "Vérification du statut de l'environnement..." -ForegroundColor Cyan
Write-Host ""

# Afficher le statut
try {
    $statusOutput = eb status 2>&1
    Write-Host $statusOutput
    Write-Host ""
} catch {
    Write-Host "ERREUR lors de la récupération du statut:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

Write-Host "Vérification de la santé..." -ForegroundColor Cyan
Write-Host ""

# Afficher la santé
try {
    $healthOutput = eb health --refresh 2>&1
    Write-Host $healthOutput
    Write-Host ""
} catch {
    Write-Host "ERREUR lors de la vérification de la santé:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Commandes utiles:" -ForegroundColor Cyan
Write-Host "  eb logs          - Voir les logs" -ForegroundColor White
Write-Host "  eb logs --stream - Voir les logs en temps réel" -ForegroundColor White
Write-Host "  eb events        - Voir les événements récents" -ForegroundColor White
Write-Host "  eb console       - Ouvrir la console AWS" -ForegroundColor White
Write-Host "========================================" -ForegroundColor Cyan
