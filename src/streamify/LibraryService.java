package streamify;
import java.util.*;
public class LibraryService {
 private final List<Media> catalog=List.of(
  new Media("1","Midnight City","Song","Electronic","M83","4:03"),
  new Media("2","Golden Hour","Song","Indie","JVKE","3:29"),
  new Media("3","Ocean Eyes","Song","Pop","Billie Eilish","3:20"),
  new Media("4","Monsoon Drive","Song","Indian Pop","Aarav","3:51"),
  new Media("5","Beyond Earth","Series","Sci-Fi","Studio Nova","42 min"),
  new Media("6","The Last Orbit","Movie","Sci-Fi","R. Sen","1h 48m"),
  new Media("7","Hidden Valley","Movie","Adventure","K. Rao","1h 56m"),
  new Media("8","Quiet Morning","Song","Acoustic","Mira","3:12"),
  new Media("9","City Stories","Series","Drama","North Films","38 min"),
  new Media("10","Neon Dreams","Song","Synthwave","The Signals","4:17"),
  new Media("11","The Signal","Episode","Sci-Fi","Studio Nova","42 min"),
  new Media("12","First Light","Episode","Drama","North Films","38 min"),
  new Media("13","The Long Way Home","Episode","Adventure","K. Rao","36 min"));
 public List<Media> all(){return catalog;}
 public List<Media> search(String q){String s=q==null?"":q.toLowerCase(Locale.ROOT).trim(); if(s.isEmpty())return catalog; return catalog.stream().filter(m->(m.title()+" "+m.genre()+" "+m.creator()+" "+m.kind()).toLowerCase(Locale.ROOT).contains(s)).toList();}
 public List<Media> recommendations(Media m){return catalog.stream().filter(x->!x.id().equals(m.id())&&x.genre().equalsIgnoreCase(m.genre())).sorted(Comparator.comparingInt(Media::plays).reversed()).limit(4).toList();}
}
