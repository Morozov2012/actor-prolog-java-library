cls
erase /s *.bak *.obj *.exe *.map more.000
erase /s morozov.jar
jar cf morozov.jar morozov/built_in/*.class morozov/domains/*.class morozov/classes/*.class morozov/run/*.class morozov/syntax/*.class morozov/syntax/scanner/*.class morozov/system/*.class morozov/system/checker/*.class morozov/system/files/*.class morozov/system/gui/*.class morozov/system/gui/sadt/*.class morozov/system/gui/space3d/*.class morozov/system/gui/dialogs/*.class morozov/system/gui/dialogs/scalable/*.class morozov/system/gui/dialogs/scalable/common/*.class morozov/system/gui/dialogs/scalable/metal/*.class morozov/system/gui/dialogs/scalable/windows/*.class morozov/system/gui/dialogs/special/*.class morozov/system/gui/reports/*.class morozov/system/indices/*.class morozov/system/records/*.class morozov/terms/*.class morozov/tests/*.class
jar -i morozov.jar
