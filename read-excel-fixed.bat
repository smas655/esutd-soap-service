@echo off
powershell -Command "& {
    Add-Type -AssemblyName System.IO.Compression.FileSystem
    Add-Type -AssemblyName System.Windows.Forms
    [void][System.Reflection.Assembly]::LoadWithPartialName('Microsoft.Office.Interop.Excel')
    
    try {
        $excel = New-Object -ComObject Excel.Application
        $excel.Visible = $false
        $workbook = $excel.Workbooks.Open('C:\Users\Asus Vivobook\Desktop\andersen\references\D_COUNTRY.xlsx')
        $sheet = $workbook.Sheets.Item(1)
        $usedRange = $sheet.UsedRange
        $rowCount = $usedRange.Rows.Count
        $colCount = $usedRange.Columns.Count
        
        Write-Host 'Excel file info:'
        Write-Host '--------------'
        Write-Host 'Total rows:' $rowCount
        Write-Host 'Total columns:' $colCount
        Write-Host ''
        
        Write-Host 'Column headers:'
        Write-Host '--------------'
        for ($col = 1; $col -le $colCount; $col++) {
            Write-Host $col': ' $sheet.Cells.Item(1, $col).Text
        }
        Write-Host ''
        
        Write-Host 'Sample data (row 2):'
        Write-Host '--------------'
        for ($col = 1; $col -le $colCount; $col++) {
            Write-Host $sheet.Cells.Item(1, $col).Text': ' $sheet.Cells.Item(2, $col).Text
        }
    }
    catch {
        Write-Host 'Error:' $_.Exception.Message
    }
    finally {
        if ($workbook) { $workbook.Close($false) }
        if ($excel) { $excel.Quit() }
        if ($sheet) { [System.Runtime.Interopservices.Marshal]::ReleaseComObject($sheet) | Out-Null }
        if ($workbook) { [System.Runtime.Interopservices.Marshal]::ReleaseComObject($workbook) | Out-Null }
        if ($excel) { [System.Runtime.Interopservices.Marshal]::ReleaseComObject($excel) | Out-Null }
        [System.GC]::Collect()
        [System.GC]::WaitForPendingFinalizers()
    }
}" 