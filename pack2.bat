cls
erase /s *.bak *.obj *.exe *.map more.000
erase /s morozov.jar
jar cf morozov.jar morozov/built_in/*.class morozov/classes/*.class morozov/classes/errors/*.class morozov/domains/*.class morozov/domains/errors/*.class morozov/domains/signals/*.class morozov/run/*.class morozov/run/errors/*.class morozov/syntax/*.class morozov/syntax/errors/*.class morozov/syntax/scanner/*.class morozov/syntax/scanner/errors/*.class morozov/syntax/scanner/signals/*.class morozov/system/*.class morozov/system/checker/*.class morozov/system/checker/errors/*.class morozov/system/checker/signals/*.class morozov/system/errors/*.class morozov/system/files/*.class morozov/system/files/errors/*.class morozov/system/gui/*.class morozov/system/gui/dialogs/*.class morozov/system/gui/dialogs/errors/*.class morozov/system/gui/dialogs/scalable/*.class morozov/system/gui/dialogs/scalable/common/*.class morozov/system/gui/dialogs/scalable/metal/*.class morozov/system/gui/dialogs/scalable/windows/*.class morozov/system/gui/dialogs/signals/*.class morozov/system/gui/dialogs/special/*.class morozov/system/gui/errors/*.class morozov/system/gui/reports/*.class morozov/system/gui/reports/errors/*.class morozov/system/gui/reports/signals/*.class morozov/system/gui/sadt/*.class morozov/system/gui/sadt/errors/*.class morozov/system/gui/sadt/signals/*.class morozov/system/gui/signals/*.class morozov/system/gui/space2d/*.class morozov/system/gui/space2d/errors/*.class morozov/system/gui/space3d/*.class morozov/system/gui/space3d/errors/*.class morozov/system/gui/space3d/signals/*.class morozov/system/indices/*.class morozov/system/indices/errors/*.class morozov/system/records/*.class morozov/system/records/errors/*.class morozov/system/signals/*.class morozov/terms/*.class morozov/terms/errors/*.class morozov/terms/signals/*.class morozov/tests/*.class
jar -i morozov.jar