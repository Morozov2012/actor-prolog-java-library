// (c) 2012 IRE RAS Alexei A. Morozov

package morozov.system.checker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.DirectoryStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import java.net.URI;

import java.util.ArrayList;

public class LocalFileAttributes {
	//
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	protected BasicFileAttributes basicAttributes= null;
	protected ArrayList<Path> directoryContent= null;
	protected boolean isOK= false;
	//
	public LocalFileAttributes(URI uri) {
		try {
			Path path= Paths.get(uri);
			basicAttributes= Files.readAttributes(path,BasicFileAttributes.class);
			if (basicAttributes.isDirectory()) {
				directoryContent= new ArrayList<>();
				DirectoryStream<Path> stream= Files.newDirectoryStream(path);
				try {
					for (Path entry: stream) {
						directoryContent.add(entry);
					}
				} finally {
					try {
						stream.close();
					} catch (Throwable e) {
					}
				}
			};
			isOK= true;
		} catch (Throwable e) {
			isOK= false;
		}
	}
	//
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
		// Returns the creation time:
		FileTime time= basicAttributes.creationTime();
		if (time.compareTo(recentAttributes.creationTime()) != 0) {
			return true;
		};
		// Returns an object that uniquely identifies the given file,
		// or null if a file key is not available:
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
		// Tells whether the file is a directory:
		if (basicAttributes.isDirectory() != recentAttributes.isDirectory()) {
			return true;
		};
		// Tells whether the file is something other than a regular
		// file, directory, or symbolic link:
		if (basicAttributes.isOther() != recentAttributes.isOther()) {
			return true;
		};
		// Tells whether the file is a regular file with opaque content:
		if (basicAttributes.isRegularFile() != recentAttributes.isRegularFile()) {
			return true;
		};
		// Tells whether the file is a symbolic link:
		if (basicAttributes.isSymbolicLink() != recentAttributes.isSymbolicLink()) {
			return true;
		};
		// Returns the time of last access:
		if (basicAttributes.lastAccessTime().compareTo(recentAttributes.lastAccessTime()) != 0) {
			return true;
		};
		// Returns the time of last modification:
		if (basicAttributes.lastModifiedTime().compareTo(recentAttributes.lastModifiedTime()) != 0) {
			return true;
		};
		// Returns the size of the file (in bytes):
		if (basicAttributes.size() != recentAttributes.size()) {
			return true;
		};
		return false;
	}
	protected boolean directoryContentWasChanged(ArrayList<Path> recentContent) {
		int size= directoryContent.size();
		if (size == recentContent.size()) {
			for (int n=0; n < size; n++) {
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
