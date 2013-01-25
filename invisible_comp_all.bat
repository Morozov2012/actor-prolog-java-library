@cls
@erase /s *.bak *.obj *.exe *.map *.class more.000
@cd morozov\built_in
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd morozov\classes
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd morozov\domains
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd morozov\run
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd morozov\syntax
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd morozov\syntax\scanner
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd morozov\system
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd morozov\system\checker
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd morozov\system\files
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd morozov\system\gui
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd morozov\system\gui\sadt
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd ..
@cd morozov\system\gui\space3d
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd ..
@cd morozov\system\gui\dialogs
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd ..
@cd morozov\system\gui\dialogs\scalable
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd ..
@cd ..
@cd morozov\system\gui\dialogs\scalable\common
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd ..
@cd ..
@cd ..
@cd morozov\system\gui\dialogs\scalable\metal
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd ..
@cd ..
@cd ..
@cd morozov\system\gui\dialogs\scalable\windows
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd ..
@cd ..
@cd ..
@cd morozov\system\gui\dialogs\special
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd ..
@cd ..
@cd morozov\system\gui\reports
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd ..
@cd morozov\system\indices
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd morozov\system\records
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd ..
@cd morozov\terms
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
@cd morozov\tests
@FOR %%i IN (*.java) DO @javac -Xlint:fallthrough %%i
@cd ..
@cd ..
