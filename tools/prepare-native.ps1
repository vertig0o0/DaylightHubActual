$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path -Parent $PSScriptRoot
$toolsDir = Join-Path $taskRoot '.tools'
$nativeDir = Join-Path $taskRoot '.native/llama.cpp'
New-Item -ItemType Directory -Force $toolsDir | Out-Null
$revision = '889f2ec96deacbc2d8a9a67dbe181eaa43a2fb3b'

function Get-PinnedArchive([string]$Url, [string]$Name, [string]$Hash, [string]$Destination) {
    $archive = Join-Path $toolsDir $Name
    if (!(Test-Path -LiteralPath $archive)) { Invoke-WebRequest -Uri $Url -OutFile $archive -TimeoutSec 300 }
    if ((Get-FileHash -LiteralPath $archive -Algorithm SHA256).Hash -ne $Hash) {
        throw "Checksum mismatch for $Name. Do not extract this file."
    }
    Expand-Archive -LiteralPath $archive -DestinationPath $Destination -Force
}
if (!(Test-Path -LiteralPath (Join-Path $nativeDir '.git'))) {
    New-Item -ItemType Directory -Force $nativeDir | Out-Null
    git -C $nativeDir init
    git -C $nativeDir remote add origin https://github.com/ggml-org/llama.cpp.git
}
$currentRevision = git -C $nativeDir rev-parse HEAD 2>$null
if ($currentRevision -ne $revision) {
    git -C $nativeDir fetch --depth 1 origin $revision
    if ($LASTEXITCODE -ne 0) { throw 'Could not fetch pinned llama.cpp.' }
    git -C $nativeDir checkout --detach $revision
    if ($LASTEXITCODE -ne 0) { throw 'Could not check out pinned llama.cpp.' }
}
$archiveDir = Join-Path $taskRoot '.native/libarchive'
$archiveRevision = '0c48412be2d94afe1859a807ea7896a18d8900bd' # libarchive 3.8.10
if (!(Test-Path -LiteralPath (Join-Path $archiveDir '.git'))) {
    New-Item -ItemType Directory -Force $archiveDir | Out-Null
    git -C $archiveDir init
    git -C $archiveDir remote add origin https://github.com/libarchive/libarchive.git
}
$archiveHead = git -C $archiveDir rev-parse HEAD 2>$null
if ($archiveHead -ne $archiveRevision) {
    git -C $archiveDir fetch --depth 1 origin $archiveRevision
    if ($LASTEXITCODE -ne 0) { throw 'Could not fetch pinned libarchive.' }
    git -C $archiveDir checkout --detach $archiveRevision
    if ($LASTEXITCODE -ne 0) { throw 'Could not check out pinned libarchive.' }
}
if (!(Test-Path -LiteralPath (Join-Path $toolsDir 'android-ndk-r30/source.properties'))) {
    Get-PinnedArchive 'https://dl.google.com/android/repository/android-ndk-r30-windows.zip' 'ndk.zip' 'B830098AAF18B67A42EB831C404E15E5F2990A474F054AC145B0BC957AC6D729' $toolsDir
}
$cmakeDir = Join-Path $toolsDir 'cmake-3.31.6-windows-x86_64'
if (!(Test-Path -LiteralPath (Join-Path $cmakeDir 'bin/cmake.exe'))) {
    Get-PinnedArchive 'https://github.com/Kitware/CMake/releases/download/v3.31.6/cmake-3.31.6-windows-x86_64.zip' 'cmake.zip' 'D163CD3AB4959B0A53FA8988F2DDBD2E6C501658201E6A154386BAD9DBE4F836' $toolsDir
}
if (!(Test-Path -LiteralPath (Join-Path $cmakeDir 'bin/ninja.exe'))) {
    Get-PinnedArchive 'https://github.com/ninja-build/ninja/releases/download/v1.12.1/ninja-win.zip' 'ninja.zip' 'F550FEC705B6D6FF58F2DB3C374C2277A37691678D6ABA463ADCBB129108467A' (Join-Path $cmakeDir 'bin')
}
$sdkDir = $env:ANDROID_HOME
if (!$sdkDir) { $sdkDir = Join-Path $env:LOCALAPPDATA 'Android/Sdk' }
if (!(Test-Path -LiteralPath $sdkDir)) { throw 'Install the Android SDK with Android Studio first.' }
$propertiesLines = @(
    ('sdk.dir=' + $sdkDir.Replace('\','/').Replace(':','\:'))
    ('cmake.dir=' + $cmakeDir.Replace('\','/').Replace(':','\:'))
)
[IO.File]::WriteAllLines((Join-Path $taskRoot 'local.properties'), [string[]]$propertiesLines)
Write-Output 'Native source and build tools are ready. Use Android Studio JBR (JDK 17+) and build with gradlew.bat.'
