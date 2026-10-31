param([string]$NodePath = $env:CODEX_MCP_NODE_PATH)
$ErrorActionPreference = 'Stop'
$readerRoot = Join-Path (Split-Path -Parent $PSScriptRoot) 'reader'
if (!$NodePath) { $NodePath = (Get-Command node.exe -ErrorAction Stop).Source }
$readerNodeDirectory = Split-Path -Parent $NodePath
$env:PATH = $readerNodeDirectory + ';' + $env:PATH
$env:COREPACK_HOME = Join-Path (Split-Path -Parent $PSScriptRoot) '.tools/corepack'
$env:COREPACK_ENABLE_DOWNLOAD_PROMPT = '0'
Push-Location $readerRoot
try {
    & (Join-Path $readerNodeDirectory 'corepack.cmd') pnpm@10.20.0 install --frozen-lockfile
    if ($LASTEXITCODE -ne 0) { throw 'Reader dependency installation failed.' }
    & $NodePath build.mjs
    if ($LASTEXITCODE -ne 0) { throw 'Reader bundling failed.' }
} finally { Pop-Location }
