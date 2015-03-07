@echo off

echo ==========================================================
echo                      PIXEL KINGDOM         [pub-Preview_5] 
echo ==========================================================

echo Starte PixelKingdom...
java -jar PixelKingdom.jar -XX:+UseFastAccessorMethods -XX:+BindGCTaskThreadsToCPUs -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy
echo.
echo.
echo Errors bitte an Entwickler weitergeben mit Informationen zur Ursache /Eine Issue auf unserem GitHub erstellen.
echo Danke; das Entwicklerteam.
echo.
pause