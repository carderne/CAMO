package camo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import javazoom.jlgui.basicplayer.*;
import java.io.File;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

/**
 * The primary graphical user interface for CAMO.
 * Consists of a frame with panels for the various sections of the display.
 * Logical code has been completely excluded from this class,
 * except from situation where it deals directly with GUI components.
 * @author Chris Arderne
 * @version 3.0
 */
class MainUI {

   /**
    * The top-level component holding every other component.
    */
   static JFrame frame;
   /**
    * The JMenuBar that holds all of the menus.
    */
   private static JMenuBar mBar;
   /**
    * This JMenu holds the following items:
    * Add File to Library...
    * Add Folder to Library...
    * Exit
    */
   private static JMenu mFile;
   /**
    * This JMenu holds the following items:
    * Remove Duplicates
    * Preferences
    */
   private static JMenu mEdit;
   /**
    * This JMenu holds the following items:
    * Play
    * Increase Volume
    * Decrease Volume
    * Switch To Minimal
    */
   private static JMenu mPlayer;
   /**
    * This JMenu holds the following items:
    * Import Playlist...
    * Export Playlist...
    * New Playlist...
    * New Smart Playlist...
    * Edit Selected Playlist...
    */
   private static JMenu mPlaylists;
   /**
    * This JMenu holds the following items:
    * About
    */
   private static JMenu mHelp;
   /**
    * Creates a JFileChooser object and allows the user to
    * select a wav or mp3 file to import.
    */
   private static JMenuItem miAddFile;
   /**
    * Creates a JFileChooser object and allows
    * the user to select a folder to import.
    */
   private static JMenuItem miAddFolder;
   /**
    * Saves all playlists and exits the runtime.
    */
   private static JMenuItem miExit;
   /**
    * Scans for and removes duplicate tracks.
    */
   private static JMenuItem miDuplicates;
   /**
    * Opens the preferences dialog.
    */
   private static JMenuItem miPreferences;
   /**
    * Play the selected file or resume/pause playback..
    */
   private static JMenuItem miPlayPause;
   /**
    * Increase playback volume.
    */
   private static JMenuItem miIncrVolume;
   /**
    * Decrease playback volume.
    */
   private static JMenuItem miDecrVolume;
   /**
    * Switch to the minimal GUI.
    */
   private static JMenuItem miMinimal;
   /**
    * Creates a JFileChooser object for
    * the user to select a playlist file to import.
    */
   private static JMenuItem miImportPlaylist;
   /**
    * Creates a JFileChooser object for
    * the user to save to a playlist file.
    */
   private static JMenuItem miExportPlaylist;
   /**
    * Opens a dialog for the user to specify the name
    * for a new playlist that will contain the selected tracks.
    */
   private static JMenuItem miNewPlaylist;
   /**
    * Opens a dialog for the user to specify
    * the name and rules for a new smart playlist.
    */
   private static JMenuItem miNewSmartPlaylist;
   /*
    * Opens a dialog for the user to edit the selected playlist.
    */
   private static JMenuItem miEditPlaylist;
   /**
    * Opens a dialog displaying help.
    */
   private static JMenuItem miHelp;
   /**
    * Opens a dialog displaying the logo and information about CAMO.
    */
   private static JMenuItem miAbout;
   /**
    * Play the selected track or resume/pause playback.
    */
   private static JButton playPause;
   /**
    * Volume icon that changes depending on the current volume level.
    */
   private static JLabel volumeIcon;
   /**
    * Set the volume for playback.
    */
   private static JSlider volumeSlider;
   /**
    * Displays for how long the current track has microseconds.
    */
   private static JLabel labelTimePlayed;
   /**
    * Graphically display song progressBar.
    */
   private static JProgressBar progressBar;
   /**
    * Displays the remaining time of the track
    */
   private static JLabel labelTimeRemaining;
   /**
    * Allows the user to input a search term.
    */
   private static JTextField search;
   /*
    * The heading for playlistList.
    */
   private static JLabel playlistHeader;
   /**
    * Displays all playlists.
    */
   static JList playlistList;
   /**
    * Sorts data when clicked.
    */
   private static JButton titleHeader;
   /**
    * Displays all title values.
    */
   static JList titleList;
   /**
    * Displays all time values.
    */
   private static JList timeList;
   /**
    * Sorts data when clicked.
    */
   private static JButton artistHeader;
   /**
    * Displays all tiartistle values.
    */
   private static JList artistList;
   /**
    * Sorts data when clicked.
    */
   private static JButton albumHeader;
   /**
    * Displays all album values.
    */
   private static JList albumList;
   /**
    * Sorts data when clicked.
    */
   private static JButton genreHeader;
   /**
    * Displays all genre values.
    */
   private static JList genreList;
   /**
    * Status bar information for currently playing song.
    */
   static JLabel nowPlaying;
   /**
    * Status bar information for number of songs in selected playlist.
    */
   private static JLabel numSongs;
   /**
    * Used to run resource-intensive operations in a separate thread2.
    */
   private static Thread thread2;
   /**
    * Contains the entire music library and all associated information.
    */
   static Library library;
   /**
    * Stores the search query with which
    * the currently displayed playlist is being filtered.
    */
   private static String query;
   /**
    * Temporary storage for copying Song objects from playlist to playlist.
    */
   private static ArrayList<Song> copyPasteSongs;
   /**
    * The index of the playlist that contains the currently playing song.
    */
   static int playingPlaylist;
   /**
    * The index of the song that is currently playing,
    * relative to the playlist specified in playingPlaylist
    */
   static int playingSong;
   /**
    * User-customisable, stored in CAMO.cfg -
    * determines whether to display now-playing information in the title bar.
    */
   private static boolean nowPlayingInTitle;
   /**
    * User-customisable, stored in CAMO.cfg.
    * Any of the following fields from Preferences: RED, GREEN or BLUE
    */
   private static int colourScheme;

   /**
    * Initialises necessary objects and calls the
    * createFrame and createPlayListener methods.
    * @param args Unused command line arguments.
    */
   public static void main(String[] args) {
      // Schedule a job for the event-dispatching thread:
      // creating and showing this application's GUI.
      EventQueue.invokeLater(new Runnable() {

         @Override
         public void run() {
            try {
               // Sets the look and feel to the OS look and feel.
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
               // This form of error reporting is used throughout CAMO,
               // as it allows the source of errors to be easily traced.
               Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
            }

            library = new Library();

            // Imports the system preferences from
            // the Scanner object provided by the restore method.
            Scanner fileScanner = library.restore();
            if (fileScanner != null) {
               nowPlayingInTitle = Boolean.valueOf(fileScanner.nextLine());
               colourScheme = Integer.parseInt(fileScanner.nextLine());
            } else {
               // If no settings file is found, default to true.
               nowPlayingInTitle = true;
               // If no settings file found, default to Preferences.GREEN.
               colourScheme = Preferences.GREEN;
            }

            createFrame();
            // This displays all playlists in the left JList.
            playlistList.setListData(library.displayPlaylists());

            // Sets initial song outputs.
            setListData(library.getPlaylist(0).displaySongs(
                    library.getPlaylist(0).sortBy, query = ""));

            // Sets the playlists to the default 'Library'.
            playlistList.setSelectedIndex(0);
            playingPlaylist = -1;
            playingSong = -1;

            // Creates the playback listener to handle events such
            // as song progress and new songs.
            createPlayListener();
         }
      });
   }// main

   /**
    * Creates the {@link javax.swing.JFrame} object that forms
    * the top-level component of this class.
    */
   private static void createFrame() {
      frame = new JFrame("CAMO");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // Set to be un-resizable to make the coding easier.
      //frame.setResizable(false);
      frame.setIconImage(new ImageIcon("src" + File.separator + "camo"
              + File.separator + "resources" + File.separator + "icon.png").getImage());
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.addWindowListener(new WindowAdapter() {

         @Override
         public void windowClosed(WindowEvent evt) {
            // This is fired when the main window is closed.
            exit();
         }
      });

      frame.setJMenuBar(createMenuBar());
      frame.getContentPane().add(createPanel());
      frame.pack();
      frame.setVisible(true);
   }// createFrame

   /**
    * Creates the {@link javax.swing.JMenuBar} for this UI.
    */
   static JMenuBar createMenuBar() {
      mBar = new JMenuBar();
      mFile = new JMenu("File");

      miAddFile = new JMenuItem("Add File to Library...");
      miAddFile.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            addFileToLibrary();
         }
      });
      mFile.add(miAddFile);

      miAddFolder = new JMenuItem("Add Folder to Library...");
      miAddFolder.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            playlistList.setSelectedIndex(0);
            // Creates a new thread2 in which to run the library importing.
            thread2 = new Thread(new Runnable() {

               @Override
               public void run() {
                  addFolderToLibrary();
               }
            });
            thread2.start();
         }
      });
      mFile.add(miAddFolder);
      mFile.addSeparator();

      miExit = new JMenuItem("Exit");
      miExit.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            exit();
         }
      });
      mFile.add(miExit);
      mBar.add(mFile);

      mEdit = new JMenu("Edit");

      miDuplicates = new JMenuItem("Remove Duplicates");
      miDuplicates.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            removeDuplicates();
         }
      });
      mEdit.add(miDuplicates);
      mEdit.addSeparator();

      miPreferences = new JMenuItem("Preferences...");
      miPreferences.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            preferences();
         }
      });
      mEdit.add(miPreferences);
      mBar.add(mEdit);

      mPlayer = new JMenu("Player");

      miPlayPause = new JMenuItem("Play");
      miPlayPause.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            play();
         }
      });
      mPlayer.add(miPlayPause);

      miIncrVolume = new JMenuItem("Increase Volume");
      miIncrVolume.setAccelerator(KeyStroke.getKeyStroke(
              KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK));
      miIncrVolume.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            increaseVolume();
         }
      });
      mPlayer.add(miIncrVolume);

      miDecrVolume = new JMenuItem("Decrease Volume");
      miDecrVolume.setAccelerator(KeyStroke.getKeyStroke(
              KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));
      miDecrVolume.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            decreaseVolume();
         }
      });
      mPlayer.add(miDecrVolume);
      mPlayer.addSeparator();

      miMinimal = new JMenuItem("Switch to Minimal");
      miMinimal.setAccelerator(KeyStroke.getKeyStroke(
              KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK));
      miMinimal.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            switchToMinimal();
         }
      });
      mPlayer.add(miMinimal);
      mBar.add(mPlayer);

      mPlaylists = new JMenu("Playlists");

      miImportPlaylist = new JMenuItem("Import Playlist...");
      miImportPlaylist.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            importPlaylist();
         }
      });
      mPlaylists.add(miImportPlaylist);

      miExportPlaylist = new JMenuItem("Export Playlist...");
      miExportPlaylist.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            exportPlaylist();
         }
      });
      mPlaylists.add(miExportPlaylist);
      mPlaylists.addSeparator();

      miNewPlaylist = new JMenuItem("New Playlist...");
      miNewPlaylist.setAccelerator(KeyStroke.getKeyStroke(
              KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
      miNewPlaylist.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            newPlaylist();
         }
      });
      mPlaylists.add(miNewPlaylist);

      miNewSmartPlaylist = new JMenuItem("New Smart Playlist...");
      miNewSmartPlaylist.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
              InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
      miNewSmartPlaylist.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            newSmartPlaylist();
         }
      });
      mPlaylists.add(miNewSmartPlaylist);

      miEditPlaylist = new JMenuItem("Edit Selected Playlist...");
      miEditPlaylist.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            editSelectedPlaylist();
         }
      });
      mPlaylists.add(miEditPlaylist);
      mBar.add(mPlaylists);

      mHelp = new JMenu("Help");

      miHelp = new JMenuItem("Help");
      miHelp.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            help();
         }
      });
      mHelp.add(miHelp);

      miAbout = new JMenuItem("About");
      miAbout.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            about();
         }
      });
      mHelp.add(miAbout);

      mBar.add(mHelp);
      return mBar;
   }// createMenuBar

   /**
    * Creates all the content, besides the menu bar,
    * that is to be held in the main frame.
    */
   private static JPanel createPanel() {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      // Skip backwards one track.
      JButton skipBack = new JButton();
      skipBack.setIcon(new ImageIcon("src" + File.separator + "camo"
              + File.separator + "resources" + File.separator + "backwards.png"));
      skipBack.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            // If there is no song playing.
            if (playingPlaylist != -1 && playingSong != -1) {
               // If the playback successfully skips back one track.
               if (library.getPlaylist(playingPlaylist).skipBack(
                       playingSong, ((double) volumeSlider.getValue()) / 100)) {
                  playingSong--;
                  nowPlaying.setText(" " + library.getPlaylist(playingPlaylist).
                          getSong(playingSong).getField(Song.ARTIST) + " - " + library.getPlaylist(
                          playingPlaylist).getSong(playingSong).getField(Song.TITLE));
                  if (nowPlayingInTitle) {
                     frame.setTitle("CAMO :: " + library.getPlaylist(
                             playingPlaylist).getSong(playingSong).
                             getField(Song.ARTIST) + " - " + library.getPlaylist(
                             playingPlaylist).getSong(playingSong).getField(Song.TITLE));
                  }
               } else {
                  playingSong = -1;
                  playingPlaylist = -1;
                  nowPlaying.setText(" " + "Nothing Playing");
                  frame.setTitle("CAMO");
               }
            }
         }
      });


      // Play the selected track or resume/pause playback.
      playPause = new JButton();
      playPause.setIcon(new ImageIcon("src" + File.separator + "camo" +
              File.separator + "resources" + File.separator + "play.png"));
      playPause.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            play();
         }
      });

      // Skip forwards one track.
      JButton skipForwards = new JButton();
      skipForwards.setIcon(new ImageIcon("src" + File.separator + "camo" +
              File.separator + "resources" + File.separator + "forwards.png"));
      skipForwards.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            // If nothing is playing.
            if (playingPlaylist != -1 && playingSong != -1) {
               // If the playback successfully skips back one track.
               if (library.getPlaylist(playingPlaylist).skipForwards(
                       playingSong, ((double) volumeSlider.getValue()) / 100)) {
                  playingSong++;
                  nowPlaying.setText(" " + library.getPlaylist(playingPlaylist).
                          getSong(playingSong).getField(Song.ARTIST) + " - " + library.getPlaylist(
                          playingPlaylist).getSong(playingSong).getField(Song.TITLE));
                  if (nowPlayingInTitle) {
                     frame.setTitle("CAMO :: " + library.getPlaylist(
                             playingPlaylist).getSong(playingSong).
                             getField(Song.ARTIST) + " - " + library.getPlaylist(
                             playingPlaylist).getSong(playingSong).getField(Song.TITLE));
                  }
               } else {
                  playingSong = -1;
                  playingPlaylist = -1;
                  nowPlaying.setText(" " + "Nothing Playing");
                  frame.setTitle("CAMO");
               }
            }
         }
      });

      // Volume icon to indicate approximate volume level.
      volumeIcon = new JLabel();
      volumeIcon.setIcon(new ImageIcon("src" + File.separator + "camo" +
              File.separator + "resources" + File.separator + "volume_mid.png"));

      // Slider to select volume level.
      volumeSlider = new JSlider(0, 100, 50);
      volumeSlider.addChangeListener(new ChangeListener() {

         @Override
         public void stateChanged(ChangeEvent evt) {
            // Fires whenever the slider's value is changed.
            volumeChanged(volumeSlider, volumeIcon);
         }
      });

      // Displays for how long the current track has microseconds.
      labelTimePlayed = new JLabel("");
      labelTimePlayed.setFont(new Font("Tahoma", 0, 11));
      labelTimePlayed.setHorizontalAlignment(SwingConstants.TRAILING);

      // Graphically displays track progressBar.
      progressBar = new JProgressBar();
      progressBar.setValue(0);

      // Displays the remaining time of the track.
      labelTimeRemaining = new JLabel("");
      labelTimeRemaining.setFont(new Font("Tahoma", 0, 11));
      labelTimeRemaining.setHorizontalAlignment(SwingConstants.LEADING);

      // Allows the user to input a search term.
      search = new JTextField(" Search...");
      search.setForeground(new Color(155, 155, 155));
      search.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

      search.getDocument().addDocumentListener(new DocumentListener() {

         @Override
         public void insertUpdate(DocumentEvent evt) {
            // Fired whenever text is entered.
            search(evt);
         }

         @Override
         public void removeUpdate(DocumentEvent evt) {
            // Fired whenever text is removed.
            search(evt);
         }

         @Override
         public void changedUpdate(DocumentEvent evt) {
         }
      });

      search.addFocusListener(new FocusAdapter() {

         @Override
         public void focusGained(FocusEvent evt) {
            search.setText("");
            // Set the foreground colour to black.
            search.setForeground(new Color(0, 0, 0));
            // Set the border colour to blue.
            search.setBorder(new LineBorder(new Color(102, 102, 255), 1,
                    true));
         }

         @Override
         public void focusLost(FocusEvent evt) {
            // If no text has been entered.
            if (search.getText().equals("")) {
               search.setText(" Search...");
            }
            // Set the foreground colour to grey.
            search.setForeground(new Color(155, 155, 155));
            // Set the border colour to black.
            search.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
         }
      });

      // The heading for playlistList.
      playlistHeader = new JLabel("   PLAYLISTS");
      playlistHeader.setHorizontalAlignment(JLabel.LEADING);
      playlistHeader.setOpaque(true);
      playlistHeader.setFont(new Font("Tahoma", 1, 11));

      // Sets the colour based on the colourScheme int.
      if (colourScheme == Preferences.RED) {
         playlistHeader.setBackground(new Color(226, 209, 215));
      } else if (colourScheme == Preferences.GREEN) {
         playlistHeader.setBackground(new Color(209, 226, 215));
      } else if (colourScheme == Preferences.BLUE) {
         playlistHeader.setBackground(new Color(209, 215, 226));
      }

      // Displays all the playlists and the main library.
      // Includes a right-click context menu.
      playlistList = new JList();
      playlistList.setFont(new Font("Courier New", 0, 12));

      // Sets the background colour based on the colourScheme int.
      if (colourScheme == Preferences.RED) {
         playlistList.setBackground(new Color(226, 209, 215));
      } else if (colourScheme == Preferences.GREEN) {
         playlistList.setBackground(new Color(209, 226, 215));
      } else if (colourScheme == Preferences.BLUE) {
         playlistList.setBackground(new Color(209, 215, 226));
      }

      // Sets the selection background colour based on the colourScheme int.
      if (colourScheme == Preferences.RED) {
         playlistList.setSelectionBackground(new Color(150, 120, 120));
      } else if (colourScheme == Preferences.GREEN) {
         playlistList.setSelectionBackground(new Color(120, 150, 120));
      } else if (colourScheme == Preferences.BLUE) {
         playlistList.setSelectionBackground(new Color(120, 120, 150));
      }

      playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      playlistList.addMouseListener(new MouseAdapter() {

         @Override
         public void mousePressed(MouseEvent evt) {
            if (playlistList.getSelectedIndex() == -1) {
               playlistList.setSelectedIndex(0);
            }

            // If this event is a normal left click.
            if (evt.getButton() == MouseEvent.BUTTON1) {
               search.setText(" Search...");
               query = "";
               titleHeader.setText("Title");
               artistHeader.setText("Artist");
               albumHeader.setText("Album");
               genreHeader.setText("Genre");

               setListData(library.getPlaylist(playlistList.getSelectedIndex()).displaySongs(
                       library.getPlaylist(playlistList.getSelectedIndex()).sortBy, query));

               // Restores the selected index for this playlist if there is one.
               if (library.getPlaylist(playlistList.getSelectedIndex()).selectedIndices.length != -1
                       && library.getPlaylist(playlistList.getSelectedIndex()).selectedIndices.length
                       < library.getPlaylist(playlistList.getSelectedIndex()).getSize()) {
                  synchroniseListSelections(
                          library.getPlaylist(playlistList.getSelectedIndex()).selectedIndices);
               }
            }

            // If this event is a right click.
            if (evt.getButton() == MouseEvent.BUTTON3) {
               search.setText(" Search...");
               playlistList.setSelectedIndex(playlistList.locationToIndex(evt.getPoint()));

               setListData(library.getPlaylist(playlistList.getSelectedIndex()).displaySongs(
                       library.getPlaylist(playlistList.getSelectedIndex()).sortBy, query = ""));

               JPopupMenu playlistsPopup = new JPopupMenu();

               JMenuItem popupNewPlaylist = new JMenuItem("New Playlist...");
               popupNewPlaylist.addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent evt) {
                     newPlaylist();
                  }
               });
               playlistsPopup.add(popupNewPlaylist);

               JMenuItem popupEditPlaylist = new JMenuItem("Edit Selected Playlist");
               popupEditPlaylist.addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent evt) {
                     editSelectedPlaylist();
                  }
               });
               playlistsPopup.add(popupEditPlaylist);

               JMenuItem popupExportPlaylist = new JMenuItem("Export Playlist...");
               popupExportPlaylist.addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent evt) {
                     exportPlaylist();
                  }
               });
               playlistsPopup.add(popupExportPlaylist);

               // Pastes the copied item(s) into the selected playlist.
               JMenuItem popupPasteLeft = new JMenuItem("Paste");
               popupPasteLeft.addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent evt) {
                     paste();
                  }
               });
               playlistsPopup.add(popupPasteLeft);

               JMenuItem popupDeletePlaylist = new JMenuItem("Delete");
               popupDeletePlaylist.addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent evt) {
                     deletePlaylist();
                  }
               });
               playlistsPopup.add(popupDeletePlaylist);
               playlistsPopup.show(playlistList, evt.getX(), evt.getY());
            }
         }
      });

      playlistList.addKeyListener(new KeyListener() {

         @Override
         public void keyTyped(KeyEvent evt) {
         }

         @Override
         public void keyPressed(KeyEvent evt) {
            // If the delete key is pressed.
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
               deletePlaylist();
            } else if (KeyStroke.getKeyStrokeForEvent(evt) == KeyStroke.getKeyStroke(
                    KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK)) {
               paste();
            }
         }

         @Override
         public void keyReleased(KeyEvent evt) {
         }
      });

      playlistList.addFocusListener(new FocusAdapter() {

         @Override
         public void focusGained(FocusEvent evt) {
            if (colourScheme == Preferences.RED) {
               playlistList.setSelectionBackground(new Color(150, 120, 120));
            } else if (colourScheme == Preferences.GREEN) {
               playlistList.setSelectionBackground(new Color(120, 150, 120));
            } else if (colourScheme == Preferences.BLUE) {
               playlistList.setSelectionBackground(new Color(120, 120, 150));
            }
         }

         @Override
         public void focusLost(FocusEvent evt) {
            if (colourScheme == Preferences.RED) {
               playlistList.setSelectionBackground(new Color(180, 150, 150));
            } else if (colourScheme == Preferences.GREEN) {
               playlistList.setSelectionBackground(new Color(150, 180, 150));
            } else if (colourScheme == Preferences.BLUE) {
               playlistList.setSelectionBackground(new Color(150, 150, 180));
            }
         }
      });

      // Allows the JList to scroll.
      JScrollPane playlistScroll =
              new JScrollPane(playlistList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      playlistScroll.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
      playlistScroll.setColumnHeaderView(playlistHeader);

      // Header button for title display. Sorts when clicked.
      titleHeader = new JButton("Title");
      titleHeader.setFont(new Font("Arial", 1, 11));
      titleHeader.setHorizontalAlignment(JLabel.LEADING);
      titleHeader.setMinimumSize(new Dimension(100, 15));
      titleHeader.setPreferredSize(new Dimension(250, 15));
      titleHeader.setMaximumSize(new Dimension(400, 15));
      titleHeader.addActionListener(getHeaderActionListener(titleHeader, Song.TITLE));

      // Displays all song titles.
      titleList = new JList();
      titleList.setFont(new Font("Courier New", 0, 12));
      setColour(titleList);
      titleList.addMouseMotionListener(getMouseMotionListener(titleList));
      titleList.addMouseListener(getMouseAdapter(titleList));
      titleList.addKeyListener(getKeyListener(titleList));
      titleList.addFocusListener(getFocusAdapter());

      // Allows the JList to scroll.
      JScrollPane titleScroll = new JScrollPane(titleList,
              ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      titleScroll.setColumnHeaderView(titleHeader);
      titleScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 0, 0)));

      // Header button for time display. Sorts when clicked.
      JButton timeHeader = new JButton("Time");
      timeHeader.setFont(new Font("Arial", 1, 11));
      timeHeader.setHorizontalAlignment(JLabel.LEADING);
      timeHeader.setMinimumSize(new Dimension(62, 15));
      timeHeader.setPreferredSize(new Dimension(62, 15));
      timeHeader.setMaximumSize(new Dimension(62, 15));

      // Displays all song times.
      timeList = new JList();
      timeList.setFont(new Font("Courier New", 0, 12));
      setColour(timeList);
      timeList.addMouseMotionListener(getMouseMotionListener(timeList));
      timeList.addMouseListener(getMouseAdapter(timeList));
      timeList.addKeyListener(getKeyListener(timeList));
      timeList.addFocusListener(getFocusAdapter());

      // Allows the JList to scroll.
      JScrollPane timeScroll = new JScrollPane(timeList,
              ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      timeScroll.setColumnHeaderView(timeHeader);
      timeScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 0, 0)));

      // Header button for artist display. Sorts when clicked.
      artistHeader = new JButton("Artist");
      artistHeader.setFont(new Font("Arial", 1, 11));
      artistHeader.setHorizontalAlignment(JLabel.LEADING);
      artistHeader.setMinimumSize(new Dimension(100, 15));
      artistHeader.setPreferredSize(new Dimension(250, 15));
      artistHeader.setMaximumSize(new Dimension(400, 15));
      artistHeader.addActionListener(getHeaderActionListener(artistHeader, Song.ARTIST));

      // Displays all song artist.
      artistList = new JList();
      artistList.setFont(new Font("Courier New", 0, 12));
      setColour(artistList);
      artistList.addMouseMotionListener(getMouseMotionListener(artistList));
      artistList.addMouseListener(getMouseAdapter(artistList));
      artistList.addKeyListener(getKeyListener(artistList));
      artistList.addFocusListener(getFocusAdapter());

      // Allows the JList to scroll.
      JScrollPane artistScroll = new JScrollPane(artistList,
              ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      artistScroll.setColumnHeaderView(artistHeader);
      artistScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 0, 0)));

      // Header button for album display. Sorts when clicked.
      albumHeader = new JButton("Album");
      albumHeader.setFont(new Font("Arial", 1, 11));
      albumHeader.setHorizontalAlignment(JLabel.LEADING);
      albumHeader.setMinimumSize(new Dimension(100, 15));
      albumHeader.setPreferredSize(new Dimension(250, 15));
      albumHeader.setMaximumSize(new Dimension(400, 15));
      albumHeader.addActionListener(getHeaderActionListener(albumHeader, Song.ALBUM));

      // Displays all song albums.
      albumList = new JList();
      albumList.setFont(new Font("Courier New", 0, 12));
      setColour(albumList);
      albumList.addMouseMotionListener(getMouseMotionListener(albumList));
      albumList.addMouseListener(getMouseAdapter(albumList));
      albumList.addKeyListener(getKeyListener(albumList));
      albumList.addFocusListener(getFocusAdapter());

      // Allows the JList to scroll.
      JScrollPane albumScroll = new JScrollPane(albumList,
              ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      albumScroll.setColumnHeaderView(albumHeader);
      albumScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 0, 0)));

      // Header button for genre display. Sorts when clicked.
      genreHeader = new JButton("Genre");
      genreHeader.setFont(new Font("Arial", 1, 11));
      genreHeader.setHorizontalAlignment(JLabel.LEADING);
      genreHeader.setMinimumSize(new Dimension(100, 15));
      genreHeader.setPreferredSize(new Dimension(250, 15));
      genreHeader.setMaximumSize(new Dimension(400, 15));
      genreHeader.addActionListener(getHeaderActionListener(genreHeader, Song.GENRE));

      // Displays all song genres.
      genreList = new JList();
      genreList.setFont(new Font("Courier New", 0, 12));
      setColour(genreList);
      genreList.addMouseMotionListener(getMouseMotionListener(genreList));
      genreList.addMouseListener(getMouseAdapter(genreList));
      genreList.addKeyListener(getKeyListener(genreList));
      genreList.addFocusListener(getFocusAdapter());

      // Allows the JList to scroll.
      JScrollPane genreScroll = new JScrollPane(genreList,
              ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      genreScroll.setColumnHeaderView(genreHeader);
      genreScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 0, 0)));

      // All the song display JLists will share a scroll bar on the far right.
      titleScroll.setVerticalScrollBar(genreScroll.getVerticalScrollBar());
      timeScroll.setVerticalScrollBar(genreScroll.getVerticalScrollBar());
      artistScroll.setVerticalScrollBar(genreScroll.getVerticalScrollBar());
      albumScroll.setVerticalScrollBar(genreScroll.getVerticalScrollBar());
      genreScroll.setVerticalScrollBar(genreScroll.getVerticalScrollBar());

      // Status bar information for currently playing song.
      nowPlaying = new JLabel(" Nothing Playing");

      // Status bar information for number of songs in selected playlist.
      numSongs = new JLabel("0 songs");
      numSongs.setHorizontalAlignment(SwingConstants.CENTER);

      // The height and width, in pixels of the current desktop environment.
      int height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
      int width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
      
      GroupLayout panelLayout = new GroupLayout(panel);
      panel.setLayout(panelLayout);

      panelLayout.setHorizontalGroup(panelLayout.createParallelGroup()
         .addGroup(panelLayout.createSequentialGroup()
            .addComponent(skipBack, 35, 35, 35)
            .addComponent(true, playPause, 40, 40, 40)
            .addComponent(skipForwards, 35, 35, 35)
            .addGap(20)
            .addComponent(volumeIcon)
            .addComponent(volumeSlider, 100, 100, 100)
            .addGap(20)
            .addComponent(labelTimePlayed, 35, 35, 35)
            .addGap(10)
            .addComponent(progressBar, 100, width - 580, 7000)
            .addGap(10)
            .addComponent(labelTimeRemaining, 35, 35, 35)
            .addGap(20, 50, 80)
            .addComponent(search, 140, 140, 140)
            .addGap(10))
         .addGroup(panelLayout.createSequentialGroup()
            .addComponent(playlistScroll, 100, (width - 82) / 5, (width - 82) / 5)
            .addComponent(titleScroll, 100, (width - 82) / 5, 1000)
            .addComponent(timeScroll, 62, 62, 62)
            .addComponent(artistScroll, 100, (width - 82) / 5, 1000)
            .addComponent(albumScroll, 100, (width - 82) / 5, 1000)
            .addComponent(genreScroll, 100, (width - 82) / 5, 1000))
         .addGroup(panelLayout.createSequentialGroup()
            .addComponent(nowPlaying, 200, 200, 200)
            .addComponent(numSongs, 462, width - 220, 4062)));

      panelLayout.setVerticalGroup(panelLayout.createSequentialGroup()
         .addGap(5).addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(skipBack, 20, 20, 20)
            .addComponent(playPause, 25, 25, 25)
            .addComponent(skipForwards, 20, 20, 20)
            .addComponent(volumeIcon)
            .addComponent(volumeSlider, 20, 20, 20)
            .addComponent(labelTimePlayed, 15, 15, 15)
            .addComponent(progressBar, 15, 15, 15)
            .addComponent(labelTimeRemaining, 15, 15, 15)
            .addComponent(search, 20, 20, 20))
         .addGap(5)
         .addGroup(panelLayout.createParallelGroup()
            .addComponent(playlistScroll, 0, height - 200, height)
            .addComponent(titleScroll, 0, height - 200, height)
            .addComponent(timeScroll, 0, height - 200, height)
            .addComponent(artistScroll, 0, height - 200, height)
            .addComponent(albumScroll, 0, height - 200, height)
            .addComponent(genreScroll, 0, height - 200, height))
         .addGroup(panelLayout.createParallelGroup()
            .addComponent(nowPlaying, 15, 15, 15)
            .addComponent(numSongs, 15, 15, 15)));

      return panel;
   }// createPanel

   /**
    * Displays a {@link javax.swing.JFileChooser} and imports the selected song files.
    */
   private static void addFileToLibrary() {
      JFileChooser browser = new JFileChooser();
      browser.setFileHidingEnabled(false);
      browser.setMultiSelectionEnabled(true);
      // Sets the browser to only allow mp3 and wav files.
      browser.setFileFilter(new FileNameExtensionFilter("Music file", "mp3", "wav"));
      browser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      if (browser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
         library.addFiles(browser.getSelectedFiles());

         if (playlistList.getSelectedIndex() != 0) {
            query = "";
         }

         playlistList.setListData(library.displayPlaylists());
         playlistList.setSelectedIndex(0);

         setListData(library.getPlaylist(0).displaySongs(library.getPlaylist(0).sortBy, query));
      }
   }// addFileToLibrary

   /**
    * Displays a {@link javax.swing.JFileChooser} and imports the selected folders.
    */
   private static void addFolderToLibrary() {
      final JFileChooser browser = new JFileChooser();
      browser.setFileHidingEnabled(false);
      browser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      if (browser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {

         // Sets the progressBar bar to indeterminate to show the user that
         // something is taking place in the background.
         progressBar.setIndeterminate(true);

         miDuplicates.setEnabled(false);
         miAddFile.setEnabled(false);
         miAddFolder.setEnabled(false);
         miImportPlaylist.setEnabled(false);
         miExportPlaylist.setEnabled(false);
         miNewPlaylist.setEnabled(false);
         miNewSmartPlaylist.setEnabled(false);
         miEditPlaylist.setEnabled(false);
         search.setEnabled(false);
         playlistList.setEnabled(false);
         titleList.setEnabled(false);
         timeList.setEnabled(false);
         artistList.setEnabled(false);
         albumList.setEnabled(false);
         genreList.setEnabled(false);

         library.addDirectory(browser.getSelectedFile());

         miDuplicates.setEnabled(true);
         miAddFile.setEnabled(true);
         miAddFolder.setEnabled(true);
         miImportPlaylist.setEnabled(true);
         miExportPlaylist.setEnabled(true);
         miNewPlaylist.setEnabled(true);
         miNewSmartPlaylist.setEnabled(true);
         miEditPlaylist.setEnabled(true);
         search.setEnabled(true);
         playlistList.setEnabled(true);
         titleList.setEnabled(true);
         timeList.setEnabled(true);
         artistList.setEnabled(true);
         albumList.setEnabled(true);
         genreList.setEnabled(true);

         if (playlistList.getSelectedIndex() != 0) {
            query = "";
         }

         playlistList.setListData(library.displayPlaylists());
         playlistList.setSelectedIndex(0);
         // Returns the progressBar bar to its normal status.
         progressBar.setIndeterminate(false);

         try {
            setListData(library.getPlaylist(0).displaySongs(library.getPlaylist(0).sortBy, query));
            // Ends thread2.
            thread2.join();
         } catch (InterruptedException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }// addFolderToLibrary

   /**
    * Saves all persistent data and exits the JRE.
    */
   private static void exit() {
      library.save(nowPlayingInTitle, colourScheme);
      System.exit(0);
   }// exit

   /**
    * Scans for and removes duplicates.
    */
   private static void removeDuplicates() {
      library.removeSongs(library.removeDuplicates());

      if (playlistList.getSelectedIndex() != 0) {
         query = "";
      }

      setListData(library.getPlaylist(0).displaySongs(library.getPlaylist(0).sortBy, query));
   }// removeDuplicates

   /**
    * Displays the preferences dialog window.
    */
   private static void preferences() {
      Preferences p = new Preferences(frame);
      p.checkBoxTitleDisplay.setSelected(nowPlayingInTitle);

      if (colourScheme == Preferences.RED) {
         p.radioButtonColourThemeRed.setSelected(true);
      } else if (colourScheme == Preferences.GREEN) {
         p.radioButtonColourThemeGreen.setSelected(true);
      } else if (colourScheme == Preferences.BLUE) {
         p.radioButtonColourThemeBlue.setSelected(true);
      }

      p.dialog.setVisible(true);
      if (p.result) {

         if (p.checkBoxTitleDisplay.isSelected()) {
            nowPlayingInTitle = true;
            if (playingSong != -1) {
               frame.setTitle("CAMO :: " + library.getPlaylist(
                       playingPlaylist).getSong(playingSong).getField(
                       Song.ARTIST) + " - " + library.getPlaylist(
                       playingPlaylist).getSong(playingSong).getField(
                       Song.TITLE));
            }
         } else {
            nowPlayingInTitle = false;
            frame.setTitle("CAMO");
         }

         if (p.radioButtonColourThemeRed.isSelected()) {
            colourScheme = Preferences.RED;
            playlistList.setBackground(new Color(226, 209, 215));
            playlistList.setSelectionBackground(new Color(180, 150, 150));
            playlistHeader.setBackground(new Color(226, 209, 215));
         } else if (p.radioButtonColourThemeGreen.isSelected()) {
            colourScheme = Preferences.GREEN;
            playlistList.setBackground(new Color(209, 226, 215));
            playlistList.setSelectionBackground(new Color(150, 180, 150));
            playlistHeader.setBackground(new Color(209, 226, 215));
         } else if (p.radioButtonColourThemeBlue.isSelected()) {
            colourScheme = Preferences.BLUE;
            playlistList.setBackground(new Color(209, 215, 226));
            playlistList.setSelectionBackground(new Color(150, 150, 180));
            playlistHeader.setBackground(new Color(209, 215, 226));
         }
      }
   }// preferences

   /**
    * Plays the selected song.
    */
   static void play() {
      if (Player.player.getStatus() == BasicPlayer.PLAYING) {
         Player.pause();
      } else if (Player.player.getStatus() == BasicPlayer.PAUSED) {
         Player.resume();
      } else if (titleList.getSelectedIndex() != -1 && (Player.player.getStatus() ==
              BasicPlayer.UNKNOWN ||Player.player.getStatus() == BasicPlayer.STOPPED)) {
         playingPlaylist = playlistList.getSelectedIndex();
         playingSong = titleList.getSelectedIndex();

         if (library.getPlaylist(playlistList.getSelectedIndex())
                 .getSong(titleList.getSelectedIndex())
                 .play(((double) volumeSlider.getValue()) / 100)) {
            nowPlaying.setText(" " + library.getPlaylist(
                    playlistList.getSelectedIndex()).getSong(titleList.getSelectedIndex()).
                    getField(Song.ARTIST) + " - " +
                    library.getPlaylist(playlistList.getSelectedIndex()).
                    getSong(titleList.getSelectedIndex()).getField(Song.TITLE));
            if (nowPlayingInTitle) {
               frame.setTitle("CAMO :: " + library.getPlaylist(playlistList.getSelectedIndex()).
                       getSong(titleList.getSelectedIndex()).getField(Song.ARTIST) + " - " +
                       library.getPlaylist(playlistList.getSelectedIndex()).
                       getSong(titleList.getSelectedIndex()).getField(Song.TITLE));
            }
         } else {
            // If playback of the song failed.
            nowPlaying.setText(" " + "Incompatible song file or file not found");
         }
      }
   }// play

   /**
    * Increments the volume level by 5.
    */
   private static void increaseVolume() {
      volumeSlider.setValue(volumeSlider.getValue() + 5);
   }// increaseVolume

   /**
    * Decrements the volume level by 5.
    */
   private static void decreaseVolume() {
      volumeSlider.setValue(volumeSlider.getValue() - 5);
   }// decreaseVolume

   /**
    * Replaces the standard window with a smaller 'minimal' version, with only playback controls.
    */
   private static void switchToMinimal() {
      frame.setVisible(false);
      MinimalUI minimal = new MinimalUI(frame, labelTimePlayed.getText(),
              progressBar.getMaximum(), progressBar.getValue(), labelTimeRemaining.getText());
      minimal.sliderVolume.setValue(volumeSlider.getValue());
      minimal.dialog.setVisible(true);
      volumeSlider.setValue(minimal.sliderVolume.getValue());
      // Program returns to the main window when the 'minimal' version is exited.
      frame.setVisible(true);
   }// switchToMinimal

   /**
    * Displays a {@link javax.swing.JFileChooser} and imports the selected playlist.
    */
   private static void importPlaylist() {
      JFileChooser browser = new JFileChooser();
      browser.setFileHidingEnabled(false);
      browser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
      browser.setFileFilter(new FileNameExtensionFilter("Text", "txt"));
      if (browser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
         if (browser.getName(browser.getSelectedFile()).endsWith(".txt")) {
            library.importPlaylist(browser.getSelectedFile());
            setListData(library.getPlaylist(
                    playlistList.getSelectedIndex()).displaySongs(library.getPlaylist(
                    playlistList.getSelectedIndex()).sortBy, query));
         }
      }
   }// importPlaylist

   /**
    * Displays a {@link javax.swing.JFileChooser} and exports the selected playlist.
    */
   private static void exportPlaylist() {
      JFileChooser browser = new JFileChooser();
      browser.setFileHidingEnabled(false);
      browser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
      browser.setFileFilter(new FileNameExtensionFilter("Text", "txt"));
      if (browser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
         File selectedFile = browser.getSelectedFile();
         if (!selectedFile.toString().endsWith(".txt")) {
            selectedFile = new File(selectedFile.toString() + ".txt");
         }
         library.getPlaylist(playlistList.getSelectedIndex()).exportPlaylist(selectedFile);
      }
   }// exportPlaylist

   /**
    * Displays the {@link camo.NewPlaylist} dialog to create a new playlist.
    */
   private static void newPlaylist() {
      NewPlaylist np = new NewPlaylist(frame);
      np.dialog.setVisible(true);
      if (np.result) {
         library.addPlaylist(playlistList.getSelectedIndex(), np.textFieldName.getText());
         setListData(library.getPlaylist(library.getListsSize() - 1).
                 displaySongs(library.getPlaylist(library.getListsSize() - 1).sortBy, query = ""));
         playlistList.setListData(library.displayPlaylists());
         playlistList.setSelectedIndex(library.getListsSize() - 1);
      }
   }// newPlaylist

   /**
    * Displays the {@link camo.NewSmartPlaylist} dialog to create a new playlist.
    */
   private static void newSmartPlaylist() {
      NewSmartPlaylist nsp = new NewSmartPlaylist(frame);
      nsp.dialog.setVisible(true);
      if (nsp.result) {
         library.addSmartPlaylist(nsp.textFieldName.getText(),
                 nsp.comboBoxCategory.getSelectedIndex(),
                 nsp.textFieldRule.getText());
         setListData(library.getPlaylist(library.getListsSize() - 1).displaySongs(
                 library.getPlaylist(library.getListsSize() - 1).sortBy, query = ""));
         playlistList.setListData(library.displayPlaylists());
         playlistList.setSelectedIndex(library.getListsSize() - 1);
      }
   }// newSmartPlaylist

   /**
    * Displays the appropriate dialog to edit an existing playlist.
    */
   private static void editSelectedPlaylist() {
      int index = playlistList.getSelectedIndex();
      // If the selected playlist is a SmartPlaylist object.
      if (library.getPlaylist(playlistList.getSelectedIndex()) instanceof SmartPlaylist) {
         NewSmartPlaylist nsp = new NewSmartPlaylist(frame);
         nsp.setValues(((SmartPlaylist) library.getPlaylist(
                 playlistList.getSelectedIndex())).getName(), ((SmartPlaylist) library.getPlaylist(
                 playlistList.getSelectedIndex())).getCategory(),
                 ((SmartPlaylist) library.getPlaylist(playlistList.getSelectedIndex())).getRule());
         nsp.dialog.setVisible(true);

         if (nsp.result) {
            ((SmartPlaylist) library.getPlaylist(playlistList.getSelectedIndex())).edit(
                    playlistList.getSelectedIndex(),
                    nsp.textFieldName.getText(),
                    nsp.comboBoxCategory.getSelectedIndex(),
                    nsp.textFieldRule.getText());
            int selectedPlaylist = playlistList.getSelectedIndex();
            playlistList.setListData(library.displayPlaylists());
            playlistList.setSelectedIndex(selectedPlaylist);
            setListData(library.getPlaylist(
                    playlistList.getSelectedIndex()).displaySongs(library.getPlaylist(
                    playlistList.getSelectedIndex()).sortBy, query));
         }
      } else if (playlistList.getSelectedIndex() != 0) {
         NewPlaylist np = new NewPlaylist(frame);
         np.setValues(library.getPlaylist(playlistList.getSelectedIndex()).getName());
         np.dialog.setVisible(true);
         if (np.result) {
            library.getPlaylist(playlistList.getSelectedIndex())
                    .edit(playlistList.getSelectedIndex(), np.textFieldName.getText());
         }
         playlistList.setListData(library.displayPlaylists());
      }
      playlistList.setSelectedIndex(index);
   }// editSelectedPlaylist

   /**
    * Displays the {@link camo.Help} dialog.
    */
   private static void help() {
      new Help(frame, library.loadHelp());
   }// about

   /**
    * Displays the {@link camo.About} dialog.
    */
   private static void about() {
      new About(frame);
   }// about

   /**
    * Fired when the volume changes to set the volume level indicator icon.
    * @param sliderVolume The volume level.
    * @param labelVolume The label to be modified: this is used so
    * that {@link camo.MinimalUI} can also use this method.
    */
   static void volumeChanged(JSlider sliderVolume, JLabel labelVolume) {
      Player.setVolume(((double) sliderVolume.getValue()) / 100);
      if (sliderVolume.getValue() == 0) {
         labelVolume.setIcon(new ImageIcon("src" + File.separator + "camo" +
                 File.separator + "resources" + File.separator + "volume_mute.png"));
      } else if (sliderVolume.getValue() < 20) {
         labelVolume.setIcon(new ImageIcon("src" + File.separator + "camo" +
                 File.separator + "resources" +
                 File.separator + "volume_min.png"));
      } else if (sliderVolume.getValue() < 70) {
         labelVolume.setIcon(new ImageIcon("src" + File.separator + "camo" +
                 File.separator + "resources" +
                 File.separator + "volume_mid.png"));
      } else {
         labelVolume.setIcon(new ImageIcon("src" + File.separator + "camo" +
                 File.separator + "resources" +
                 File.separator + "volume_max.png"));
      }
   }// volumeChanged

   /**
    * Runs a search.
    * @param evt The event containing the information pertaining to the search.
    */
   private static void search(DocumentEvent evt) {
      try {
         if (!(evt.getDocument().getText(0, evt.getDocument().getLength()).equals(" Search..."))) {
            query = evt.getDocument().getText(0, evt.getDocument().getLength());

            setListData(library.getPlaylist(
                    playlistList.getSelectedIndex()).displaySongs(library.getPlaylist(
                    playlistList.getSelectedIndex()).sortBy, query));
         }
      } catch (BadLocationException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// search

   /**
    * Sorts song output based on which header JButton has been clicked.
    * @param header The JButton that has been clicked.
    * @param sortInt The int representing the field by which to sort.
    */
   private static void sortSongs(JButton header, int sortInt) {
      setListData(library.getPlaylist(playlistList.getSelectedIndex()).displaySongs(
              library.getPlaylist(playlistList.getSelectedIndex()).sortBy = sortInt, query));

      titleHeader.setText("Title");
      artistHeader.setText("Artist");
      albumHeader.setText("Album");
      genreHeader.setText("Genre");

      header.setText(header.getText() + "                                                     ▲");
   }

   /**
    * Copies the selected song(s) to the program's clipboard.
    */
   private static void copy() {
      copyPasteSongs = new ArrayList<Song>();
      for (int i = 0; i < titleList.getSelectedIndices().length; i++) {
         copyPasteSongs.add(library.getPlaylist(playlistList.getSelectedIndex()).
                 getSong(titleList.getSelectedIndices()[i]));
      }
   }// copy

   /**
    * Pastes the song(s) from the program's clipboard into the selected playlist.
    */
   private static void paste() {
      if (copyPasteSongs != null) {
         outer:
         for (int i = 0; i < copyPasteSongs.size(); i++) {
            for (int j = 0; j < library.getPlaylist(playlistList.getSelectedIndex()).getSize(); j++) {
               if (library.getPlaylist(playlistList.getSelectedIndex()).
                       getStoreSong(j).getFile().equals(copyPasteSongs.get(i).getFile())) {
                  continue outer;
               }
            }
            library.getPlaylist(playlistList.getSelectedIndex()).addSong(copyPasteSongs.get(i));
         }

         setListData(library.getPlaylist(playlistList.getSelectedIndex()).displaySongs(
                 library.getPlaylist(playlistList.getSelectedIndex()).sortBy, query));
      }
   }// paste

   /**
    * Deletes the selected song.
    */
   private static void deleteSong() {
      if (titleList.getSelectedIndex() != -1) {
         if (playlistList.getSelectedIndex() == 0) {
            int index = titleList.getSelectedIndex();
            for (int i = 0; i < titleList.getSelectedIndices().length; i++) {
               if (titleList.getSelectedIndices()[i] == playingSong) {
                  Player.stop();
               }
            }
            library.removeSongs(titleList.getSelectedIndices());

            setListData(library.getPlaylist(
                    playlistList.getSelectedIndex()).displaySongs(library.getPlaylist(
                    playlistList.getSelectedIndex()).sortBy, query));

            synchroniseListSelections(new int[]{index - 1});
         } else if (playlistList.getSelectedIndex() > 0) {
            int index = titleList.getSelectedIndex();
            if (playlistList.getSelectedIndex() == playingPlaylist) {
               for (int i = 0; i < titleList.getSelectedIndices().length; i++) {
                  if (titleList.getSelectedIndices()[i] == playingSong) {
                     Player.stop();
                  }
               }
            }
            library.getPlaylist(playlistList.getSelectedIndex())
                    .removeSongs(titleList.getSelectedIndices());

            setListData(library.getPlaylist(
                    playlistList.getSelectedIndex()).displaySongs(library.getPlaylist(
                    playlistList.getSelectedIndex()).sortBy, query));

            titleList.setSelectedIndex(index - 1);
         }
      }
   }// deleteSong

   /**
    * Deletes the selected playlist.
    */
   private static void deletePlaylist() {
      if (playlistList.getSelectedIndex() > 0) {
         int index = playlistList.getSelectedIndex();
         Player.stop();
         library.removePlaylist(playlistList.getSelectedIndex());
         playlistList.setListData(library.displayPlaylists());
         playlistList.setSelectedIndex(index - 1);

         setListData(library.getPlaylist(playlistList.getSelectedIndex()).displaySongs(
                 library.getPlaylist(playlistList.getSelectedIndex()).sortBy, query = ""));
      }
   }// deletePlaylist

   /**
    * Displays the {@link camo.EditSong} dialog to edit the selected song.
    */
   private static void editSelectedSong() {
      EditSong es = new EditSong(frame);
      es.setValues(
              library.getPlaylist(playlistList.getSelectedIndex())
              .getSong(titleList.getSelectedIndex()).getField(Song.TITLE),
              library.getPlaylist(playlistList.getSelectedIndex())
              .getSong(titleList.getSelectedIndex()).getField(Song.ARTIST),
              library.getPlaylist(playlistList.getSelectedIndex())
              .getSong(titleList.getSelectedIndex()).getField(Song.ALBUM),
              library.getPlaylist(playlistList.getSelectedIndex())
              .getSong(titleList.getSelectedIndex()).getField(Song.GENRE));
      es.dialog.setVisible(true);
      if (es.result) {
         library.getPlaylist(playlistList.getSelectedIndex())
                 .getSong(titleList.getSelectedIndex()).edit(
                 es.textFieldTitle.getText(),
                 es.textFieldArtist.getText(),
                 es.textFieldAlbum.getText(),
                 es.textFieldGenre.getText());
      }

      setListData(library.getPlaylist(playlistList.getSelectedIndex()).displaySongs(
              library.getPlaylist(playlistList.getSelectedIndex()).sortBy, query));
   }// editSelectedSong

   /**
    * Creates a listener for the Player class so that GUI elements can update as a track plays.
    */
   private static void createPlayListener() {
      Player.player.addBasicPlayerListener(new BasicPlayerListener() {

         Long time;

         // When the track is first opened. Sets the labelTimePlayed and labelTimeRemaining values.
         // Sets the progressBar bar. Displays track metadata in nowPlaying and in the title bar.
         @Override
         public void opened(Object stream, Map properties) {
            time = (Long) properties.get("duration");

            String timeRemaining = time / 60000000 + ":" + (time / 1000000) % 60;
            if (timeRemaining.substring(timeRemaining.indexOf(":")).length() == 2) {
               timeRemaining = time / 60000000 + ":0" + (time / 1000000) % 60;
            }

            labelTimePlayed.setText("0:00");
            labelTimeRemaining.setText("-" + timeRemaining);
            progressBar.setMaximum(((Long) properties.get("duration")).intValue());
         }// opened

         // Updates progressBar, labelTimePlayed and labelTimeRemaining as the song progresses.
         @Override
         public void progress(int bytesRead, long played, byte[] pcmdata, Map properties) {
            // Formats the microsecond value into a mm:ss display.
            String timePlayed = played / 60000000 + ":" + (played / 1000000) % 60;
            if (timePlayed.substring(timePlayed.indexOf(":")).length() == 2) {
               timePlayed = played / 60000000 + ":0" + (played / 1000000) % 60;
            }

            String timeRemaining = (time - played) / 60000000 +
                    ":" + ((time - played) / 1000000) % 60;
            if (timeRemaining.substring(timeRemaining.indexOf(":")).length() == 2) {
               timeRemaining = (time - played) / 60000000 + ":0" + ((time - played) / 1000000) % 60;
            }

            labelTimePlayed.setText(timePlayed);
            labelTimeRemaining.setText("-" + timeRemaining);
            progressBar.setValue(((Long) played).intValue());
         }// progress

         // Plays the next track when the current one ends.
         // Sets the play/pause icon depending on current playback state.
         @Override
         public void stateUpdated(BasicPlayerEvent event) {
            if (event.getCode() == javazoom.jlgui.basicplayer.BasicPlayerEvent.EOM) {
               // If something is playing.
               if (playingPlaylist != -1 && playingSong != -1) {
                  // If playack succesfully skips forwards.
                  if (library.getPlaylist(playingPlaylist).skipForwards(playingSong,
                          ((double) volumeSlider.getValue()) / 100)) {
                     playingSong++;
                     nowPlaying.setText(" " + library.getPlaylist(playingPlaylist)
                             .getSong(playingSong).
                             getField(Song.ARTIST) + " - " + library.getPlaylist(
                             playingPlaylist).getSong(playingSong).getField(Song.TITLE));
                     if (nowPlayingInTitle) {
                        frame.setTitle("CAMO :: " + library.getPlaylist(
                                playingPlaylist).getSong(playingSong).
                                getField(Song.ARTIST) + " - " + library.getPlaylist(
                                playingPlaylist).getSong(playingSong).getField(Song.TITLE));
                     }
                  } else {
                     playingSong = -1;
                     playingPlaylist = -1;
                     nowPlaying.setText(" " + "Nothing Playing");
                     frame.setTitle("CAMO");
                  }
               }
            } else if (event.getCode() == javazoom.jlgui.basicplayer.BasicPlayerEvent.RESUMED) {
               playPause.setIcon(new ImageIcon("src" + File.separator + "camo" + File.separator +
                       "resources" + File.separator + "pause.png"));
               miPlayPause.setText("Pause");
            } else if (event.getCode() == javazoom.jlgui.basicplayer.BasicPlayerEvent.PAUSED) {
               playPause.setIcon(new ImageIcon("src" + File.separator +
                       "camo" + File.separator + "resources" + File.separator + "play.png"));
               miPlayPause.setText("Resume");
            } else if (event.getCode() == javazoom.jlgui.basicplayer.BasicPlayerEvent.OPENED) {
               playPause.setIcon(new ImageIcon("src" + File.separator +
                       "camo" + File.separator + "resources" + File.separator + "pause.png"));
               miPlayPause.setText("Pause");
            } else if (event.getCode() == javazoom.jlgui.basicplayer.BasicPlayerEvent.STOPPED) {
               playPause.setIcon(new ImageIcon("src" + File.separator +
                       "camo" + File.separator + "resources" + File.separator + "play.png"));
               miPlayPause.setText("Play");
               nowPlaying.setText(" " + "Nothing Playing");
               frame.setTitle("CAMO");
               labelTimePlayed.setText("");
               labelTimeRemaining.setText("");
               progressBar.setValue(0);
            }
         }// stateUpdated

         @Override
         public void setController(BasicController controller) {
         }// setController
      });
   }// createPlayListener

   /**
    * Sets the colour for each of the song output JLists, based on the colourScheme value.
    * @param list The specific JList.
    */
   private static void setColour(JList list) {
      if (colourScheme == Preferences.RED) {
         list.setSelectionBackground(new Color(180, 150, 150));
      } else if (colourScheme == Preferences.GREEN) {
         list.setSelectionBackground(new Color(150, 180, 150));
      } else if (colourScheme == Preferences.BLUE) {
         list.setSelectionBackground(new Color(150, 150, 180));
      }
   }// setColour

   /**
    * Creates a MouseAdapter for each of the song output JLists.
    * @param list The specific JList.
    * @return
    */
   private static MouseAdapter getMouseAdapter(final JList list) {
      return new MouseAdapter() {

         @Override
         public void mousePressed(MouseEvent evt) {
            synchroniseListSelections(list.getSelectedIndices());
            // If the event is a double click.
            if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
               Player.stop();
               play();
            }

            // If it is a right click in a playlist NOT containing songs.
            if (evt.getButton() == MouseEvent.BUTTON3 &&
                    list.locationToIndex(evt.getPoint()) == -1) {
               list.requestFocusInWindow();

               JPopupMenu songsListPopup = new JPopupMenu();

               // Pastes the copied item(s) into the selected playlist.
               JMenuItem popupPaste = new JMenuItem("Paste");
               popupPaste.setAccelerator(
                       KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
               popupPaste.addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent evt) {
                     paste();
                  }
               });
               songsListPopup.add(popupPaste);
               songsListPopup.show(list, evt.getX(), evt.getY());
            }


            // If it is a right click in a playlist containing songs.
            if (evt.getButton() == MouseEvent.BUTTON3 && list.locationToIndex(evt.getPoint()) != -1) {

               boolean rightClickIsPartOfSelection = false;
               for (int i = 0; i < list.getSelectedIndices().length; i++) {
                  // If the mouse position is inside an existing selection.
                  if (list.getSelectedIndices()[i] == list.locationToIndex(evt.getPoint())) {
                     rightClickIsPartOfSelection = true;
                  }
               }

               if (!rightClickIsPartOfSelection) {
                  // The mouse is outside any existing selections,
                  // so a new selection is made.
                  list.setSelectedIndex(list.locationToIndex(evt.getPoint()));
               }

               synchroniseListSelections(list.getSelectedIndices());

               for (int i = 0; i < list.getSelectedIndices().length; i++) {
                  if (list.getSelectedIndices()[i] == list.locationToIndex(evt.getPoint())) {
                     break;
                  } else if (i == list.getSelectedIndices().length - 1 &&
                          list.getSelectedIndices()[i] !=
                          list.locationToIndex(evt.getPoint())) {
                     list.setSelectedIndex(list.locationToIndex(evt.getPoint()));
                  }
               }

               JPopupMenu songsListPopup = new JPopupMenu();
               // The sub-menu holding the list of playlists
               // to which the selected song(s) can be added.
               final JMenu popupAddToPlaylist = new JMenu("Add to Playlist");
               String[] playlists = library.displayPlaylists();
               // One menuitem for each playlist.
               JMenuItem[] menus = new JMenuItem[playlists.length];

               for (int i = 1; i < playlists.length; i++) {
                  // Checks to ensure that the current playlist is not added.
                  if (i != playlistList.getSelectedIndex()) {
                     menus[i] = new JMenuItem(playlists[i]);
                     menus[i].addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent evt) {

                           // This is the amount to add to the index from the JMenu
                           // to get the correct playlist. If the selected playlist
                           // is not Library, there are two spaces to shift because
                           // both Library and that playlist are being excluded.
                           int shift = 2;
                           if (playlistList.getSelectedIndex() == 0) {
                              shift = 1;
                           }
                           for (int i = 0; i < list.getSelectedIndices().length; i++) {
                              library.getPlaylist(popupAddToPlaylist.getPopupMenu().
                                      getComponentIndex((Component) evt.getSource()) +
                                      shift).addSong(library.getPlaylist(
                                      playlistList.getSelectedIndex()).
                                      getSong(list.getSelectedIndices()[i]));
                           }
                        }
                     });
                     popupAddToPlaylist.add(menus[i]);
                  }
               }

               songsListPopup.add(popupAddToPlaylist);

               JMenuItem popupEditSong = new JMenuItem("Edit Selected Song");
               popupEditSong.addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent evt) {
                     editSelectedSong();
                  }
               });
               songsListPopup.add(popupEditSong);

               // Copies the selected track(s) to be pasted into a playlist.
               JMenuItem popupCopy = new JMenuItem("Copy");
               popupCopy.addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent evt) {
                     copy();
                  }
               });
               songsListPopup.add(popupCopy);

               // Pastes the copied item(s) into the selected playlist.
               JMenuItem popupPasteRight = new JMenuItem("Paste");
               popupPasteRight.addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent evt) {
                     paste();
                  }
               });
               songsListPopup.add(popupPasteRight);

               JMenuItem popupDeleteSong = new JMenuItem("Delete");
               popupDeleteSong.addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent evt) {
                     deleteSong();
                  }
               });
               songsListPopup.add(popupDeleteSong);
               songsListPopup.show(list, evt.getX(), evt.getY());
               list.requestFocusInWindow();
            }
            library.getPlaylist(playlistList.getSelectedIndex()).selectedIndices =
                    list.getSelectedIndices();
         }
      };
   }// getMouseListener

   /**
    * Creates a MouseMotionAdapter for each of the JLists, to handle mouse drags.
    * @param list The specific JList.
    * @return The MouseMotionAdapter.
    */
   private static MouseMotionAdapter getMouseMotionListener(final JList list) {
      return new MouseMotionAdapter() {

         @Override
         public void mouseDragged(MouseEvent evt) {
            synchroniseListSelections(list.getSelectedIndices());
         }
      };
   }// getMouseMotionListener

   /**
    * Creates a KeyListener for each of the song output JLists.
    * @param list The JList.
    * @return The KeyListener.
    */
   private static KeyListener getKeyListener(final JList list) {
      return new KeyListener() {

         @Override
         public void keyTyped(KeyEvent evt) {
         }

         @Override
         public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
               if (list != titleList) {
                  titleList.setSelectedIndex(list.getSelectedIndex() + 1);
               }
               if (list != timeList) {
                  timeList.setSelectedIndex(list.getSelectedIndex() + 1);
               }
               if (list != artistList) {
                  artistList.setSelectedIndex(list.getSelectedIndex() + 1);
               }
               if (list != albumList) {
                  albumList.setSelectedIndex(list.getSelectedIndex() + 1);
               }
               if (list != genreList) {
                  genreList.setSelectedIndex(list.getSelectedIndex() + 1);
               }
            } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
               if (list != titleList) {
                  titleList.setSelectedIndex(list.getSelectedIndex() - 1);
               }
               if (list != timeList) {
                  timeList.setSelectedIndex(list.getSelectedIndex() - 1);
               }
               if (list != artistList) {
                  artistList.setSelectedIndex(list.getSelectedIndex() - 1);
               }
               if (list != albumList) {
                  albumList.setSelectedIndex(list.getSelectedIndex() - 1);
               }
               if (list != genreList) {
                  genreList.setSelectedIndex(list.getSelectedIndex() - 1);
               }
            } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
               Player.stop();
               play();
            } else if (KeyStroke.getKeyStrokeForEvent(evt) == KeyStroke.getKeyStroke(
                    KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK)) {
               if (list.getModel().getSize() > 0) {
                  list.addSelectionInterval(0, list.getModel().getSize() - 1);
                  synchroniseListSelections(list.getSelectedIndices());
               }
            } // If the delete key is pressed.
            else if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
               deleteSong();
            } else if (KeyStroke.getKeyStrokeForEvent(evt) == KeyStroke.getKeyStroke(
                    KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK)) {
               paste();
            } else if (KeyStroke.getKeyStrokeForEvent(evt) == KeyStroke.getKeyStroke(
                    KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK)) {
               copy();
            }
         }

         @Override
         public void keyReleased(KeyEvent evt) {
         }
      };
   }

   /**
    * Creates a FocusAdapter for each of the song output JLists.
    * @return The FocusAdapter.
    */
   private static FocusAdapter getFocusAdapter() {
      return new FocusAdapter() {

         @Override
         public void focusGained(FocusEvent evt) {
            if (colourScheme == Preferences.RED) {
               titleList.setSelectionBackground(new Color(150, 120, 120));
               timeList.setSelectionBackground(new Color(150, 120, 120));
               artistList.setSelectionBackground(new Color(150, 120, 120));
               albumList.setSelectionBackground(new Color(150, 120, 120));
               genreList.setSelectionBackground(new Color(150, 120, 120));
            } else if (colourScheme == Preferences.GREEN) {
               titleList.setSelectionBackground(new Color(120, 150, 120));
               timeList.setSelectionBackground(new Color(120, 150, 120));
               artistList.setSelectionBackground(new Color(120, 150, 120));
               albumList.setSelectionBackground(new Color(120, 150, 120));
               genreList.setSelectionBackground(new Color(120, 150, 120));
            } else if (colourScheme == Preferences.BLUE) {
               titleList.setSelectionBackground(new Color(120, 120, 150));
               timeList.setSelectionBackground(new Color(120, 120, 150));
               artistList.setSelectionBackground(new Color(120, 120, 150));
               albumList.setSelectionBackground(new Color(120, 120, 150));
               genreList.setSelectionBackground(new Color(120, 120, 150));
            }
            titleList.setSelectionForeground(new Color(255, 255, 255));
            timeList.setSelectionForeground(new Color(255, 255, 255));
            artistList.setSelectionForeground(new Color(255, 255, 255));
            albumList.setSelectionForeground(new Color(255, 255, 255));
            genreList.setSelectionForeground(new Color(255, 255, 255));
         }

         @Override
         public void focusLost(FocusEvent evt) {
            titleList.setSelectionBackground(new Color(200, 200, 200));
            titleList.setSelectionForeground(new Color(0, 0, 0));
            timeList.setSelectionBackground(new Color(200, 200, 200));
            timeList.setSelectionForeground(new Color(0, 0, 0));
            artistList.setSelectionBackground(new Color(200, 200, 200));
            artistList.setSelectionForeground(new Color(0, 0, 0));
            albumList.setSelectionBackground(new Color(200, 200, 200));
            albumList.setSelectionForeground(new Color(0, 0, 0));
            genreList.setSelectionBackground(new Color(200, 200, 200));
            genreList.setSelectionForeground(new Color(0, 0, 0));
         }
      };
   }// getFocusAdapter

   /**
    * Updates the contents of all the song display JLists.
    * @param songs The {@link ArrayList} of Song objects to be displayed.
    */
   private static void setListData(ArrayList<Song> songs) {
      String[] titleArray = new String[songs.size()];
      String[] timeArray = new String[songs.size()];
      String[] artistArray = new String[songs.size()];
      String[] albumArray = new String[songs.size()];
      String[] genreArray = new String[songs.size()];

      for (int i = 0; i < songs.size(); i++) {
         titleArray[i] = songs.get(i).getField(Song.TITLE);
         timeArray[i] = songs.get(i).formattedDuration();
         artistArray[i] = songs.get(i).getField(Song.ARTIST);
         albumArray[i] = songs.get(i).getField(Song.ALBUM);
         genreArray[i] = songs.get(i).getField(Song.GENRE);
      }

      titleList.setListData(titleArray);
      timeList.setListData(timeArray);
      artistList.setListData(artistArray);
      albumList.setListData(albumArray);
      genreList.setListData(genreArray);
      numSongs.setText(songs.size() + " songs, " + library.getPlaylist(0).getTotalTime(songs));
   }// setListData

   /**
    * Ensures that all the JLists have the same selected indices.
    * @param indices The array of indices that must be applied to all the JLists.
    */
   private static void synchroniseListSelections(int[] indices) {
      titleList.setSelectedIndices(indices);
      timeList.setSelectedIndices(indices);
      artistList.setSelectedIndices(indices);
      albumList.setSelectedIndices(indices);
      genreList.setSelectedIndices(indices);
   }// synchroniseListSelections

   /**
    *
    * @param header The {@link JButton} of this ActionListener.
    * @param sortInt The int value that decides by which field to sort data.
    * @return The {@link ActionListener}.
    */
   private static ActionListener getHeaderActionListener(final JButton header, final int sortInt) {
      return new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            sortSongs(header, sortInt);
         }
      };
   }// getHeaderActionListener
}// MainUI