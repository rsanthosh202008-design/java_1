package streamify;
import java.io.Serializable;
public class Media implements Serializable {
 private final String id,title,kind,genre,creator,duration;
 private int plays;
 public Media(String id,String title,String kind,String genre,String creator,String duration){this.id=id;this.title=title;this.kind=kind;this.genre=genre;this.creator=creator;this.duration=duration;}
 public String id(){return id;} public String title(){return title;} public String kind(){return kind;} public String genre(){return genre;} public String creator(){return creator;} public String duration(){return duration;}
 public int plays(){return plays;} public void played(){plays++;}
 public String toString(){return title+"  ·  "+creator;}
}
