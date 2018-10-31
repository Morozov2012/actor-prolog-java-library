// (c) 2018 IRE RAS Alexei A. Morozov

package morozov.system.frames.tools;

import java.io.ObjectStreamClass;
import java.lang.ClassNotFoundException;

public class SerialVersionChecker {
	public static void check(long serialVersionUID, String packageName, String className) {
		try {
			Class<?> thisClass= Class.forName(packageName+"."+className);
        	        ObjectStreamClass c= ObjectStreamClass.lookup(thisClass);
			String text1= String.format("%x",c.getSerialVersionUID());
			System.out.printf("*%s: serialVersionUID: %s\n",className,text1.toUpperCase());
			String text2= String.format("%x",serialVersionUID);
			System.out.printf("_%s: serialVersionUID: %s\n",className,text2.toUpperCase());
			if (text1.equals(text2)) {
				System.out.printf("%s: test O.K.\n",className);
			} else {
				System.out.printf("%s Error: %s <> %s\n",className,text1,text2);
			};
		} catch (ClassNotFoundException e) {
			System.out.printf("%s >>> ClassNotFoundException: %s\n",className,e);
		} catch (Throwable e) {
			System.out.printf("%s >>> Throwable: %s\n",className,e);
		}
	}
}
