@cls
@erase *.bak *.obj *.map *.class *.jar more.000
@rem @FOR %%i IN (*.java) DO javac -Xlint:unchecked %%i
@FOR %%i IN (*.java) DO javac %%i
@rem jar cf system.jar *.class
@rem erase *.class
