// (c) 2015 IRE RAS Alexei A. Morozov

package morozov.system.datum;

import morozov.built_in.*;
import morozov.run.*;
import morozov.system.datum.errors.*;
import morozov.terms.*;

import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.StandardWatchEventKinds;

public class UpdatesWatchHolder extends Thread {
	protected OpenedDataStore currentDataStore;
	// protected ActiveWorld currentProcess;
	protected AtomicBoolean stopThisThread= new AtomicBoolean(false);
	protected Path subdirectoryPath;
	protected WatchService watcher;
	protected WatchKey key;
	protected HashMap<Path,HashSet<UpdatesWatchTarget>> targets= new HashMap<Path,HashSet<UpdatesWatchTarget>>();
	//
	protected static final FileSystem fileSystem= FileSystems.getDefault();
	//
	public UpdatesWatchHolder(OpenedDataStore openedDataStore, Path path) {
		// currentProcess= process;
		setDaemon(true);
		currentDataStore= openedDataStore;
		subdirectoryPath= path;
		try {
			watcher= fileSystem.newWatchService();
		} catch (IOException e) {
			throw new CannotCreateWatchService();
		};
		try {
			// key= subdirectoryPath.register(watcher,StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);
			key= subdirectoryPath.register(watcher,StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			throw new CannotRegisterWatchService(subdirectoryPath.toString());
		}
	}
	//
	public void register(DataAbstraction database, LoadableContainer container, Path mainDataPath) {
		synchronized (targets) {
			HashSet<UpdatesWatchTarget> hash= targets.get(mainDataPath);
			if (hash==null) {
				hash= new HashSet<UpdatesWatchTarget>();
				targets.put(mainDataPath,hash);
			};
			hash.add(new UpdatesWatchTarget(database,container));
		};
	}
	//
	public void unregister(DataAbstraction database, Path mainDataPath) {
		if (database != null && mainDataPath != null) {
			synchronized (targets) {
				HashSet<UpdatesWatchTarget> hash= targets.get(mainDataPath);
				if (hash != null) {
					Iterator<UpdatesWatchTarget> targetsIterator= hash.iterator();
					while (targetsIterator.hasNext()) {
						UpdatesWatchTarget target= targetsIterator.next();
						if (target.world==database) {
							targetsIterator.remove();
						}
					}
				}
			}
		}
	}
	//
	public void run() {
		while (!stopThisThread.get()) {
			try {
				WatchKey key= watcher.take();
				synchronized (targets) {
					for (WatchEvent<?> event: key.pollEvents()) {
						WatchEvent.Kind kind= event.kind();
						if (kind == StandardWatchEventKinds.OVERFLOW) {
							// HashMap<Path,HashSet<DataAbstraction>> targetWorlds= new HashMap<Path,HashSet<DataAbstraction>>();
							Set<Path> targetsKeySet= targets.keySet();
							Iterator<Path> targetsKeySetIterator= targetsKeySet.iterator();
							while (targetsKeySetIterator.hasNext()) {
								Path currentPath= targetsKeySetIterator.next();
								sendMessage(currentPath);
							}
						} else {
							WatchEvent<Path> ev= cast(event);
							Path name= ev.context();
							Path child= subdirectoryPath.resolve(name);
							sendMessage(child);
						}
					}
				};
				key.reset();
			} catch (InterruptedException e) {
			} catch (ThreadDeath e) {
				return;
			}
		}
	}
	//
	protected void sendMessage(Path child) {
		HashSet<UpdatesWatchTarget> hash= targets.get(child);
		if (hash != null) {
			Iterator<UpdatesWatchTarget> targetsIterator= hash.iterator();
			while (targetsIterator.hasNext()) {
				UpdatesWatchTarget target= targetsIterator.next();
				LoadableContainer targetContainer= target.container;
				if (targetContainer != null) {
					targetContainer.reportExternalUpdate();
				} else {
					currentDataStore.reportExternalUpdate();
				};
				DataAbstraction targetWorld= target.world;
				long domainSignature= targetWorld.entry_s_Update_0();
				AsyncCall call= new AsyncCall(domainSignature,targetWorld,true,false,new Term[0],true);
				targetWorld.transmitAsyncCall(call,null);
			}
		}
	}
	//
	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>)event;
	}
}
