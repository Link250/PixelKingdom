@echo off
:begin
echo ==========================================================
echo                      PIXEL KINGDOM         [pub-Preview_3] 
echo ==========================================================
echo.
echo                       (1) Normal
echo                       (2) Debug
echo.
set /p op=Zum Spielen 1 oder 2 eingeben oder (0) zum Beenden: 
if "%op%"=="1" goto op1
if "%op%"=="2" goto op2
if "%op%"=="0" goto exit

echo.
echo Bitte waehle eine gueltige Option:
echo.
goto begin


:op1
echo.
echo Starte PixelKingdom normal...
start .\PixelKingdom.exe
pause
cls
goto begin


:op2
echo.
echo Starte PixelKingdom Debug...
java -jar PixelKingdom.jar -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -XX:+CICompilerCountPerCPU
echo.
pause
echo.
echo Errors bitte an Entwickler weitergeben mit Informationen zur Ursache.
echo Danke; das Entwicklerteam.
echo.
goto begin


:exit
@exit