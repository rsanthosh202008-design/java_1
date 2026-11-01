package streamify;
import java.io.*;import java.nio.file.*;import java.util.*;
public class LibraryStore {
 private final Path dir=Path.of("data");
 public void save(String name,Object value){try{Files.createDirectories(dir);try(ObjectOutputStream o=new ObjectOutputStream(Files.newOutputStream(dir.resolve(name)))){o.writeObject(value);}}catch(IOException e){System.err.println("Save failed: "+e.getMessage());}}
 @SuppressWarnings("unchecked") public <T>T load(String name,T fallback){Path p=dir.resolve(name);if(!Files.exists(p))return fallback;try(ObjectInputStream i=new ObjectInputStream(Files.newInputStream(p))){return (T)i.readObject();}catch(IOException|ClassNotFoundException e){System.err.println("Load failed for "+p+": "+e.getMessage());return fallback;}}
}
