param(
    [int]$AppPort = 8080,
    [int]$DbHostPort = 3308,
    [string]$ProjectName = "smartclinic",
    [string]$AdminEmail = "admin@smartclinic.com",
    [string]$AdminPassword = "admin123"
)

$ErrorActionPreference = "Stop"

$env:APP_PORT = "$AppPort"
$env:DB_HOST_PORT = "$DbHostPort"
$env:DB_PASSWORD = if ($env:DB_PASSWORD) { $env:DB_PASSWORD } else { "rootpassword" }

docker compose -p $ProjectName config | Out-Null
docker compose -p $ProjectName up --build -d

$loginUrl = "http://localhost:$AppPort/login"
$ready = $false
for ($i = 1; $i -le 60; $i++) {
    try {
        $response = Invoke-WebRequest -Uri $loginUrl -UseBasicParsing -TimeoutSec 3
        if ($response.StatusCode -eq 200) {
            $ready = $true
            break
        }
    } catch {
        Start-Sleep -Seconds 3
    }
}

docker compose -p $ProjectName ps

if (-not $ready) {
    docker compose -p $ProjectName logs --tail=120 app
    throw "SmartClinic did not become ready at $loginUrl"
}

$loginPage = Invoke-WebRequest -Uri $loginUrl -SessionVariable session -UseBasicParsing -TimeoutSec 5
$csrfMatch = [regex]::Match($loginPage.Content, '<meta\s+name="_csrf"\s+content="([^"]+)"')
if (-not $csrfMatch.Success) {
    throw "CSRF token was not found on the login page"
}

$loginBody = @{
    username = $AdminEmail
    password = $AdminPassword
    _csrf = $csrfMatch.Groups[1].Value
}

Invoke-WebRequest -Uri "http://localhost:$AppPort/authenticateTheUser" -Method Post -Body $loginBody -WebSession $session -UseBasicParsing -TimeoutSec 10 | Out-Null
$adminDashboard = Invoke-WebRequest -Uri "http://localhost:$AppPort/admin/dashboard" -WebSession $session -UseBasicParsing -TimeoutSec 10
if ($adminDashboard.StatusCode -ne 200 -or $adminDashboard.Content -notmatch "Admin Dashboard") {
    throw "Default admin login did not reach the admin dashboard"
}

Write-Output "SmartClinic is ready: $loginUrl"
Write-Output "Verified admin login: $AdminEmail / $AdminPassword"
