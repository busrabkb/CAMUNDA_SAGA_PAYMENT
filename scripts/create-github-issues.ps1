# Creates GitHub issues TASK-01..08 per CREATE_TASK_PROMPT format.
# Prerequisites: gh auth status must succeed.
# Usage: .\scripts\create-github-issues.ps1

$ErrorActionPreference = "Stop"
$repo = "busrabkb/CAMUNDA_SAGA_PAYMENT"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$issuesDir = Join-Path $scriptDir "issues"

$tasks = @(
    @{ Title = "TASK-01 - Docker Camunda ve Flyway ayaga kalkma hatasini gider"; BodyFile = "task-01.md" },
    @{ Title = "TASK-02 - process_completions veritabani migration"; BodyFile = "task-02.md" },
    @{ Title = "TASK-03 - ProcessCompletion persistence model tanimi"; BodyFile = "task-03.md" },
    @{ Title = "TASK-04 - ProcessCompletion repository katmani"; BodyFile = "task-04.md" },
    @{ Title = "TASK-05 - CompletionService is mantigi"; BodyFile = "task-05.md" },
    @{ Title = "TASK-06 - BPMN Record Completion service task ve delegate"; BodyFile = "task-06.md" },
    @{ Title = "TASK-07 - Order API completion bilgisini goster"; BodyFile = "task-07.md" },
    @{ Title = "TASK-08 - Completion feature entegrasyon testleri"; BodyFile = "task-08.md" }
)

Write-Host "Checking gh auth..."
gh auth status

Write-Host ""
Write-Host "Creating 8 issues in $repo ..."
Write-Host "Analysis doc: scripts/REQUIREMENT_ANALYSIS.md"
Write-Host ""

foreach ($task in $tasks) {
    $bodyPath = Join-Path $issuesDir $task.BodyFile
    Write-Host "Creating: $($task.Title)"
    gh issue create --repo $repo --title $task.Title --body-file $bodyPath
}

Write-Host ""
Write-Host "Done. Open issues:"
gh issue list --repo $repo --limit 20
