package streamify;
import javax.swing.*;import javax.swing.border.*;import javax.swing.event.*;import java.awt.*;import java.util.*;
public class Main {
 static final Color BG=new Color(15,17,24), PANEL=new Color(25,29,39), CARD=new Color(34,39,52), TEXT=new Color(238,240,247), MUTED=new Color(157,165,183), ACCENT=new Color(139,92,246), GREEN=new Color(46,204,153);
 LibraryService service=new LibraryService(); LibraryStore store=new LibraryStore();
 java.util.List<Media> favorites=store.load("favorites.bin",new ArrayList<>());
 java.util.List<Media> playlist=store.load("playlist.bin",new ArrayList<>());
 java.util.List<Media> history=store.load("history.bin",new ArrayList<>());
 DefaultListModel<Media> model=new DefaultListModel<>(); JList<Media> list=new JList<>(model);
 JLabel now=new JLabel("Choose something to play"), planLabel=new JLabel("FREE PLAN"),section=new JLabel("Made for you"); JTextField search=new JTextField();
 JButton playlistAction,favoriteAction,toggle;
 java.util.List<Media> visibleItems;
 Media current; int skips=0; boolean premium=false,playing=false;
 public static void main(String[]a){SwingUtilities.invokeLater(()->new Main().show());}
 void show(){
  try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception ignored){}
  JFrame f=new JFrame("Streamify Pro");f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);f.setSize(1120,720);f.setMinimumSize(new Dimension(900,600));f.setLocationRelativeTo(null);
  JPanel root=new JPanel(new BorderLayout(0,0));root.setBackground(BG);
  JPanel side=new JPanel();side.setBackground(PANEL);side.setPreferredSize(new Dimension(205,1));side.setLayout(new BoxLayout(side,BoxLayout.Y_AXIS));side.setBorder(new EmptyBorder(24,16,20,16));
  JLabel brand=new JLabel("♫  STREAMIFY");brand.setForeground(TEXT);brand.setFont(new Font("SansSerif",Font.BOLD,21));brand.setBorder(new EmptyBorder(0,0,28,0));side.add(brand);
  nav(side,"⌂   Home",()->showCatalog(service.all()));nav(side,"◉   Discover",()->showCatalog(service.all()));nav(side,"♥   Favorites",()->showCatalog(favorites));nav(side,"▤   My Playlist",()->showCatalog(playlist));nav(side,"◷   Recently Played",()->showCatalog(history));
  side.add(Box.createVerticalGlue());JButton upgrade=button("Upgrade to Premium");upgrade.addActionListener(e->{premium=!premium;planLabel.setText(premium?"PREMIUM PLAN":"FREE PLAN");upgrade.setText(premium?"Premium Enabled ✓":"Upgrade to Premium");});side.add(upgrade);
  JPanel main=new JPanel(new BorderLayout(18,16));main.setBackground(BG);main.setBorder(new EmptyBorder(24,26,16,26));
  JPanel header=new JPanel(new BorderLayout(16,0));header.setOpaque(false);
  JPanel welcome=new JPanel();welcome.setOpaque(false);welcome.setLayout(new BoxLayout(welcome,BoxLayout.Y_AXIS));JLabel h=new JLabel("Good evening");h.setForeground(TEXT);h.setFont(new Font("SansSerif",Font.BOLD,28));JLabel sub=new JLabel("Your soundtrack, your screen.");sub.setForeground(MUTED);welcome.add(h);welcome.add(Box.createVerticalStrut(5));welcome.add(sub);
  search.setPreferredSize(new Dimension(270,38));search.setBackground(CARD);search.setForeground(TEXT);search.setCaretColor(TEXT);search.setBorder(new CompoundBorder(new LineBorder(CARD,1,true),new EmptyBorder(8,12,8,12)));search.setToolTipText("Search title, genre or artist");
  search.getDocument().addDocumentListener(new DocumentListener(){
   public void insertUpdate(DocumentEvent e){searchCatalog();}
   public void removeUpdate(DocumentEvent e){searchCatalog();}
   public void changedUpdate(DocumentEvent e){searchCatalog();}
  });
  header.add(welcome,BorderLayout.WEST);header.add(search,BorderLayout.EAST);
  JPanel hero=new JPanel(new BorderLayout(12,0));hero.setBackground(new Color(48,35,85));hero.setBorder(new EmptyBorder(22,24,22,24));
  JPanel copy=new JPanel();copy.setOpaque(false);copy.setLayout(new BoxLayout(copy,BoxLayout.Y_AXIS));JLabel tag=new JLabel("FEATURED MIX");tag.setForeground(new Color(205,190,255));tag.setFont(new Font("SansSerif",Font.BOLD,11));JLabel title=new JLabel("<html>Late Night<br>Energy</html>");title.setForeground(Color.WHITE);title.setFont(new Font("SansSerif",Font.BOLD,29));JLabel desc=new JLabel("Electronic • Indie • Your next favorite");desc.setForeground(new Color(220,214,240));JButton listen=button("▶  Play mix");listen.addActionListener(e->{showCatalog(service.all());play(service.all().get(0));});copy.add(tag);copy.add(Box.createVerticalStrut(8));copy.add(title);copy.add(desc);copy.add(Box.createVerticalStrut(14));copy.add(listen);hero.add(copy,BorderLayout.WEST);
  JPanel content=new JPanel(new BorderLayout(0,10));content.setOpaque(false);section.setForeground(TEXT);section.setFont(new Font("SansSerif",Font.BOLD,19));
  list.setBackground(BG);list.setForeground(TEXT);list.setSelectionBackground(ACCENT);list.setFont(new Font("SansSerif",Font.PLAIN,14));list.setFixedCellHeight(42);list.setBorder(new EmptyBorder(3,3,3,3));
  list.addListSelectionListener(e->{if(!e.getValueIsAdjusting())updateActionLabels();});
  list.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){if(e.getClickCount()==2){Media m=list.getSelectedValue();if(m!=null)play(m);}}});
  JScrollPane scroll=new JScrollPane(list);scroll.setBorder(new LineBorder(CARD));scroll.getViewport().setBackground(BG);
  JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));actions.setOpaque(false);
  JButton play=button("▶ Play");playlistAction=button("+ Playlist");favoriteAction=button("♥ Favorite");JButton rec=button("✦ Similar");
  actions.add(play);actions.add(playlistAction);actions.add(favoriteAction);actions.add(rec);
  play.addActionListener(e->{Media m=list.getSelectedValue();if(m!=null)play(m);});
  playlistAction.addActionListener(e->toggleMembership(playlist,"playlist.bin"));
  favoriteAction.addActionListener(e->toggleMembership(favorites,"favorites.bin"));
  rec.addActionListener(e->{Media m=list.getSelectedValue();if(m!=null)showCatalog(service.recommendations(m));});
  content.add(section,BorderLayout.NORTH);content.add(scroll,BorderLayout.CENTER);content.add(actions,BorderLayout.SOUTH);
  JPanel center=new JPanel(new BorderLayout(0,16));center.setOpaque(false);center.add(hero,BorderLayout.NORTH);center.add(content,BorderLayout.CENTER);
  main.add(header,BorderLayout.NORTH);main.add(center,BorderLayout.CENTER);
  JPanel player=new JPanel(new BorderLayout(12,0));player.setBackground(PANEL);player.setBorder(new EmptyBorder(12,22,12,22));
  now.setForeground(TEXT);  JButton prev=button("◀");toggle=button("▶ / ❚❚");JButton next=button("▶▶");
  JPanel controls=new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));controls.setOpaque(false);controls.add(prev);controls.add(toggle);controls.add(next);
  prev.addActionListener(e->step(-1));next.addActionListener(e->step(1));toggle.addActionListener(e->{if(current!=null){playing=!playing;updatePlaybackStatus();}});
  player.add(now,BorderLayout.WEST);player.add(controls,BorderLayout.CENTER);player.add(planLabel,BorderLayout.EAST);planLabel.setForeground(MUTED);
  root.add(side,BorderLayout.WEST);root.add(main,BorderLayout.CENTER);root.add(player,BorderLayout.SOUTH);f.setContentPane(root);showCatalog(service.all());f.setVisible(true);
 }
 void nav(JPanel p,String text,Runnable r){JButton b=button(text);b.setHorizontalAlignment(SwingConstants.LEFT);b.setMaximumSize(new Dimension(190,42));b.addActionListener(e->r.run());p.add(b);p.add(Box.createVerticalStrut(5));}
 JButton button(String s){JButton b=new JButton(s);b.setUI(new javax.swing.plaf.basic.BasicButtonUI());b.setFocusPainted(false);b.setForeground(Color.WHITE);b.setBackground(Color.BLACK);b.setOpaque(true);b.setContentAreaFilled(true);b.setBorderPainted(false);b.setBorder(new EmptyBorder(9,14,9,14));b.setFont(new Font("SansSerif",Font.BOLD,12));return b;}
 void searchCatalog(){showCatalog(service.search(search.getText()));section.setText("Search results");}
 void showCatalog(java.util.List<Media> items){
  visibleItems=items;
  section.setText(items==favorites?"Favorites":items==playlist?"My Playlist":items==history?"Recently Played":items==service.all()?"Made for you":"Similar picks");
  model.clear();for(Media m:items)model.addElement(m);updateActionLabels();
 }
 boolean containsMedia(java.util.List<Media> items,Media media){return items.stream().anyMatch(item->item.id().equals(media.id()));}
 void toggleMembership(java.util.List<Media> items,String file){
  Media selected=list.getSelectedValue();if(selected==null)return;
  if(containsMedia(items,selected))items.removeIf(item->item.id().equals(selected.id()));else items.add(selected);
  store.save(file,items);
  if(visibleItems==items)showCatalog(items);
  else updateActionLabels();
 }
 void updateActionLabels(){
  if(playlistAction==null||favoriteAction==null)return;
  Media selected=list.getSelectedValue();boolean hasSelection=selected!=null;
  playlistAction.setEnabled(hasSelection);favoriteAction.setEnabled(hasSelection);
  playlistAction.setText(hasSelection&&containsMedia(playlist,selected)?"− Remove from Playlist":"+ Playlist");
  favoriteAction.setText(hasSelection&&containsMedia(favorites,selected)?"♥ Unfavorite":"♥ Favorite");
 }
 void updatePlaybackStatus(){
  now.setText((playing?"Playing: ":"Paused: ")+current.title()+"  ·  "+current.creator());
 }
 void play(Media m){current=m;m.played();playing=true;history.removeIf(item->item.id().equals(m.id()));history.add(0,m);if(history.size()>25)history.remove(history.size()-1);updatePlaybackStatus();persist();}
 void step(int dir){if(current==null){if(!service.all().isEmpty())play(service.all().get(0));return;}if(!premium&&skips>=3){JOptionPane.showMessageDialog(null,"Free plan allows 3 skips. Enable Premium to continue.");return;}skips++;int i=service.all().indexOf(current);if(i<0)i=0;play(service.all().get(Math.floorMod(i+dir,service.all().size())));}
 void persist(){store.save("favorites.bin",favorites);store.save("playlist.bin",playlist);store.save("history.bin",history);}
}
