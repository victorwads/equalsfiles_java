::Code
@echo off
SET ProgramName=EqualsFiles
SET PATH=%PATH%;%ProgramFiles%\WinRAR\
SET TPD=.\temppack
SET TPN=.\dist\tempPackage.zip
SET NAT=%TPD%\org\sqlite\native\
SET NAS=.\nbproject\finallibs\native\

::Clean
echo "Limpando"
rmdir /s/q %TPD% >nul 2>&1
del /s/q dist\%ProgramName%*_*.jar >nul 2>&1'
del /s/q dist\%ProgramName%*_*.txt >nul 2>&1
del /s/q TPN >nul 2>&1

echo "Criando Arquivos Temporarios"
::Create TempDir
mkdir %TPD%
::Extract Final Incomplete Lib
winrar x -o+ .\nbproject\finallibs\*.jar * %TPD%
::Extract Project
winrar x -o+ .\dist\*.jar * %TPD%
del .\dist\*.jar

::Win x86_64
echo "Gerando Windows x86_64"
xcopy /i/q/y/e %NAS%Windows\x86_64 %NAT%Windows\x86_64 >nul 2>&1
winrar a -ep1 %TPN% -r %TPD%\*
::copy %TPN% dist\%ProgramName%_Winx86_64_Whats.txt >nul 2>&1
move %TPN% dist\%ProgramName%_Win64.jar >nul 2>&1
rmdir /s /q %NAT%Windows\x86_64

echo "Criando Executavel"
.\nbproject\finallibs\launch4j\launch4jc.exe .\nbproject\finallibs\toExe.xml >nul 2>&1


::Win x86
::echo "Gerando Windows x86"
::xcopy /i/q/y/e %NAS%Windows\x86 %NAT%Windows\x86 >nul 2>&1
::winrar a -ep1 %TPN% -r %TPD%\*
::copy %TPN% dist\%ProgramName%_Winx86_Whats.txt >nul 2>&1
::move %TPN% dist\%ProgramName%_Winx86.jar >nul 2>&1
::rmdir /s /q %NAT%Windows

:: echo "Gerando Mac"
:: ::Mac
:: xcopy /i/q/y/e %NAS%Mac %NAT%Mac >nul 2>&1
:: winrar a -ep1 %TPN% -r %TPD%\*
:: copy %TPN% dist\%ProgramName%_Mac_Whats.txt >nul 2>&1
:: move %TPN% dist\%ProgramName%_Mac.jar >nul 2>&1
:: rmdir /s /q %NAT%Mac
:: ::Linux ALL
:: echo "Gerando Linux"
:: xcopy /i/q/y/e %NAS%Linux %NAT%Linux >nul 2>&1
:: winrar a -ep1 %TPN% -r %TPD%\*
:: copy %TPN% dist\%ProgramName%_Linux_Whats.txt >nul 2>&1
:: move %TPN% dist\%ProgramName%_Linux.jar >nul 2>&1
:: rmdir /s /q %NAT%Linux
:: ::FreeBSD
:: echo "Gerando FreeBSD"
:: xcopy /i/q/y/e %NAS%FreeBSD %NAT%FreeBSD >nul 2>&1
:: winrar a -ep1 %TPN% -r %TPD%\*
:: copy %TPN% dist\%ProgramName%_FreeBSD_Whats.txt >nul 2>&1
:: move %TPN% dist\%ProgramName%_FreeBSD.jar >nul 2>&1
:: rmdir /s /q %NAT%FreeBSD

rmdir /s /q %TPD%