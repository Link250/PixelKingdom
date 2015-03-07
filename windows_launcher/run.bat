@echo off

echo ==========================================================
echo                      PIXEL KINGDOM         [pub-Preview_4] 
echo ==========================================================

echo Starte PixelKingdom...
java -jar PixelKingdom.jar -XX:+UseFastAccessorMethods -XX:+BindGCTaskThreadsToCPUs -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -XX:+CICompilerCountPerCPU
echo.
echo.
echo Errors bitte an Entwickler weitergeben mit Informationen zur Ursache.
echo Danke; das Entwicklerteam.
echo.
pause