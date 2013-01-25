// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.DirectoryStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import java.net.URI;

import java.util.ArrayList;

public class LocalFileAttributes {
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	public BasicFileAttributes basicAttributes= null;
	public ArrayList<Path> directoryContent= null;
	public boolean isOK= false;
	public LocalFileAttributes(URI uri) {
		try {
			Path path= fileSystem.provider().getPath(uri);
			basicAttributes= Files.readAttributes(path,BasicFileAttributes.class);
			if (basicAttributes.isDirectory()) {
				directoryContent= new ArrayList<Path>();
				DirectoryStream<Path> stream;
				stream= Files.newDirectoryStream(path);
				for (Path entry: stream) {
					directoryContent.add(entry);
				}
			};
			isOK= true;
		} catch (Throwable e) {
			isOK= false;
		}
	}
	public boolean wereChanged(LocalFileAttributes recentAttributes) {
		if (isOK) {
			if (recentAttributes.isOK) {
				if (basicAttributesWereChanged(recentAttributes.basicAttributes)) {
					return true;
				};
				ArrayList<Path> recentDirectoryContent= recentAttributes.directoryContent;
				if (directoryContent == null) {
					if (recentDirectoryContent == null) {
						return false;
					} else {
						return true;
					}
				} else {
					if (recentDirectoryContent == null) {
						return true;
					} else {
						return directoryContentWasChanged(recentDirectoryContent);
					}
				}
			} else {
				return true;
			}
		} else {
			if (recentAttributes.isOK) {
				return true;
			} else {
				return false;
			}
		}
	}
	protected boolean basicAttributesWereChanged(BasicFileAttributes recentAttributes) {
		// Returns the creation time.
		// System.out.printf("(1)LocalFileAttributes\n");
		FileTime time= basicAttributes.creationTime();
		if (time.compareTo(recentAttributes.creationTime()) != 0) {
			return true;
		};
		// System.out.printf("(2)LocalFileAttributes\n");
		// Returns an object that uniquely identifies the given file,
		// or null if a file key is not available.
		Object key1= basicAttributes.fileKey();
		Object key2= recentAttributes.fileKey();
		if (key1 != null) {
			if (key2 != null) {
				if (!key1.equals(key2)) {
					return true;
				}
			} else {
				return true;
			}
		} else {
			if (key2 != null) {
				return true;
			}
		};
		// System.out.printf("(3)LocalFileAttributes\n");
		// Tells whether the file is a directory.
		if (basicAttributes.isDirectory() != recentAttributes.isDirectory()) {
			return true;
		};
		// System.out.printf("(4)LocalFileAttributes\n");
		// Tells whether the file is something other than a regular
		// file, directory, or symbolic link.
		if (basicAttributes.isOther() != recentAttributes.isOther()) {
			return true;
		};
		// System.out.printf("(5)LocalFileAttributes\n");
		// Tells whether the file is a regular file with opaque content.
		if (basicAttributes.isRegularFile() != recentAttributes.isRegularFile()) {
			return true;
		};
		// System.out.printf("(6)LocalFileAttributes\n");
		// Tells whether the file is a symbolic link.
		if (basicAttributes.isSymbolicLink() != recentAttributes.isSymbolicLink()) {
			return true;
		};
		// System.out.printf("(7)LocalFileAttributes\n");
		// Returns the time of last access.
		if (basicAttributes.lastAccessTime().compareTo(recentAttributes.lastAccessTime()) != 0) {
			return true;
		};
		// System.out.printf("(8)LocalFileAttributes\n");
		// Returns the time of last modification.
		if (basicAttributes.lastModifiedTime().compareTo(recentAttributes.lastModifiedTime()) != 0) {
			return true;
		};
		// System.out.printf("(9)LocalFileAttributes\n");
		// Returns the size of the file (in bytes).
		if (basicAttributes.size() != recentAttributes.size()) {
			return true;
		};
		// System.out.printf("(FALSE)LocalFileAttributes\n");
		return false;
	}
	protected boolean directoryContentWasChanged(ArrayList<Path> recentContent) {
		int size= directoryContent.size();
		if (size == recentContent.size()) {
			for (int n=0; n < size; n++) {
				// System.out.printf("1) %s\n",directoryContent.get(n));
				// System.out.printf("2) %s\n",recentContent.get(n));
				// System.out.printf("3) %s\n",directoryContent.get(n).compareTo(recentContent.get(n)));
				if (directoryContent.get(n).compareTo(recentContent.get(n)) != 0) {
					return true;
				}
			};
			return false;
		} else {
			return true;
		}
	}
}
