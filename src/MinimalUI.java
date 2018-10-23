package camo;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javazoom.jlgui.basicplayer.*;

/**
 * The minimal GUI for CAMO.
 * Contains only the playback controls, volume control and progressBar display.
 * @author Chris Arderne
 * @version 3.0
 */
class MinimalUI extends MainUI {

   /**
    * The main dialog window.
    */
   JDialog dialog;
   /**
    * The main panel that holds all the components.
    */
   private JPanel panel;
   /**
    * The button for playing and pausing.
    */
   private JButton buttonPlayPause;
   /**
    * The icon for the volume slider.
    */
   private JLabel labelVolume;
   /**
    * The volume slider.
    */
   JSlider sliderVolume;
   /**
    * Displays the tme played for the playing song.
    */
   private JLabel labelTimePlayed;
   /**
    * Displays song progress graphically.
    */
   private JProgressBar progressBar;
   /**
    * Displays the tme remaining for the playing song.
    */
   private JLabel labelTimeRemaining;
   /**
    * The value used to calculate the time remaining and time played.
    */
   private long time;

   /**
    * Creates the {@link JDialog} with the specified {@link JFrame} as a parent.
    * @param parent The JFrame object to be the parent.
    */
   MinimalUI(JFrame parent, String timePlayed, int progressMaximum, int progress,
           String timeRemaining) {
      dialog = new JDialog(parent, parent.getTitle(), true);
      dialog.setLocationByPlatform(true);
      dialog.setIconImage(new ImageIcon("src" + File.separator + "camo" + File.separator +
              "resources" + File.separator + "icon.png").getImage());
      dialog.setResizable(false);
      panel = new JPanel();

      /*
       * Skip backwards one track.
       */
      JButton buttonBackwards = new JButton();
      buttonBackwards.setIcon(new ImageIcon("src" + File.separator + "camo" + File.separator +
              "resources" + File.separator + "backwards.png"));
      buttonBackwards.setPreferredSize(new Dimension(35, 20));
      buttonBackwards.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            if (playingPlaylist != -1 && playingSong != -1) {
               if (library.getPlaylist(playingPlaylist).skipBack(playingSong,
                       ((double) sliderVolume.getValue()) / 100)) {
                  playingSong--;
                  dialog.setTitle("CAMO :: " + library.getPlaylist(playingPlaylist).getSong(
                          playingSong).getField(Song.ARTIST) + " - " + library.getPlaylist(
                          playingPlaylist).getSong(playingSong).getField(Song.TITLE));
                  nowPlaying.setText(library.getPlaylist(playingPlaylist).getSong(playingSong).
                          getField(Song.ARTIST) + " - " + library.getPlaylist(playingPlaylist).
                          getSong(playingSong).getField(Song.TITLE));
                  frame.setTitle("CAMO :: " + library.getPlaylist(playingPlaylist).getSong(
                          playingSong).getField(Song.ARTIST) + " - " + library.getPlaylist(
                          playingPlaylist).getSong(playingSong).getField(Song.TITLE));
               } else {
                  playingSong = -1;
                  playingPlaylist = -1;
                  dialog.setTitle("CAMO");
                  nowPlaying.setText("Nothing Playing");
                  frame.setTitle("CAMO");
               }
            }
         }
      });
      panel.add(buttonBackwards);

      /*
       * Play the selected track or resume/pause playback.
       */
      buttonPlayPause = new JButton();
      buttonPlayPause.setIcon(new ImageIcon("src" + File.separator + "camo" + File.separator +
              "resources" + File.separator + "play.png"));
      buttonPlayPause.setPreferredSize(new Dimension(40, 25));
      buttonPlayPause.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            play();
         }
      });
      panel.add(buttonPlayPause);

      /*
       * Skip forwards one track.
       */
      JButton buttonForwards = new JButton();
      buttonForwards.setIcon(new ImageIcon("src" + File.separator + "camo" + File.separator +
              "resources" + File.separator + "forwards.png"));
      buttonForwards.setPreferredSize(new Dimension(35, 20));
      buttonForwards.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            if (playingPlaylist != -1 && playingSong != -1) {
               if (library.getPlaylist(playingPlaylist).skipForwards(playingSong,
                       ((double) sliderVolume.getValue()) / 100)) {
                  playingSong++;
                  dialog.setTitle("CAMO :: " + library.getPlaylist(playingPlaylist).getSong(
                          playingSong).getField(Song.ARTIST) + " - " + library.getPlaylist(
                          playingPlaylist).getSong(playingSong).getField(Song.TITLE));
                  nowPlaying.setText(library.getPlaylist(playingPlaylist).getSong(playingSong).
                          getField(Song.ARTIST) + " - " + library.getPlaylist(playingPlaylist).
                          getSong(playingSong).getField(Song.TITLE));
                  frame.setTitle("CAMO :: " + library.getPlaylist(playingPlaylist).getSong(
                          playingSong).getField(Song.ARTIST) + " - " + library.getPlaylist(
                          playingPlaylist).getSong(playingSong).getField(Song.TITLE));
               } else {
                  playingSong = -1;
                  playingPlaylist = -1;
                  dialog.setTitle("CAMO");
                  nowPlaying.setText("Nothing Playing");
                  frame.setTitle("CAMO");
               }
            }
         }
      });
      panel.add(buttonForwards);

      JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
      separator.setPreferredSize(new Dimension(1, 25));
      panel.add(separator);

      /*
       * Volume icon to indicate approximate volume level.
       */
      labelVolume = new JLabel();
      labelVolume.setIcon(new ImageIcon("src" + File.separator + "camo" + File.separator +
              "resources" + File.separator + "volume_mid.png"));
      panel.add(labelVolume);

      /*
       * Slider to select volume level.
       */
      sliderVolume = new JSlider(0, 100, 50);
      sliderVolume.setPreferredSize(new Dimension(100, 20));
      sliderVolume.addChangeListener(new ChangeListener() {

         @Override
         public void stateChanged(ChangeEvent evt) {
            volumeChanged(sliderVolume, labelVolume);
         }
      });
      panel.add(sliderVolume);

      separator = new JSeparator(SwingConstants.VERTICAL);
      separator.setPreferredSize(new Dimension(1, 25));
      panel.add(separator);

      /*
       * Displays for how long the current track has microseconds.
       */
      labelTimePlayed = new JLabel("");
      labelTimePlayed.setFont(new Font("Tahoma", 0, 11));
      labelTimePlayed.setHorizontalAlignment(SwingConstants.TRAILING);
      labelTimePlayed.setPreferredSize(new Dimension(35, 15));
      panel.add(labelTimePlayed);

      /*
       * Graphically displays track progressBar.
       */
      progressBar = new JProgressBar();
      progressBar.setValue(0);
      progressBar.setPreferredSize(new Dimension(100, 15));
      panel.add(progressBar);

      /*
       * Displays the remaining time of the track.
       */
      labelTimeRemaining = new JLabel("");
      labelTimeRemaining.setFont(new Font("Tahoma", 0, 11));
      labelTimeRemaining.setHorizontalAlignment(SwingConstants.LEADING);
      labelTimeRemaining.setPreferredSize(new Dimension(35, 15));
      panel.add(labelTimeRemaining);

      separator = new JSeparator(SwingConstants.VERTICAL);
      separator.setPreferredSize(new Dimension(1, 25));
      panel.add(separator);

      if (Player.player.getStatus() == BasicPlayer.PLAYING) {
         buttonPlayPause.setIcon(new ImageIcon("src" + File.separator + "camo" +
                 File.separator + "resources" + File.separator + "pause.png"));
      }
      labelTimePlayed.setText(timePlayed);
      progressBar.setMaximum(progressMaximum);
      progressBar.setValue(progress);
      labelTimeRemaining.setText(timeRemaining);
      if (titleList.getSelectedIndex() != -1) {
         time = library.getPlaylist(playlistList.getSelectedIndex()).getSong(
                 titleList.getSelectedIndex()).getDuration();
      }
      createPlayListener();

      dialog.add(panel);
      dialog.pack();
   }// default constructor

   /**
    * Creates a listener for the Player class so that GUI elements can update as a track plays.
    */
   private void createPlayListener() {
      Player.player.addBasicPlayerListener(new BasicPlayerListener() {

         /**
          * When the track is first opened.
          * Sets the labelTimePlayed and labelTimeRemaining values.
          * Sets the progressBar bar.
          * Displays track metadata in nowPlaying and in the title bar.
          */
         @Override
         public void opened(Object stream, Map properties) {
            String timeRemaining = (int) time / 60000000 + ":" + (time / 1000000) % 60;
            if (timeRemaining.substring(timeRemaining.indexOf(":")).length() == 2) {
               timeRemaining = (int) time / 60000000 + ":0" + ((int) time / 1000000) % 60;
            }

            labelTimePlayed.setText("0:00");
            labelTimeRemaining.setText("-" + timeRemaining);
            progressBar.setMaximum(((Long) properties.get("duration")).intValue());
            dialog.setTitle("CAMO :: " +
                    library.getPlaylist(playlistList.getSelectedIndex()).getSong(
                    titleList.getSelectedIndex()).getField(Song.ARTIST)
                    + " - " + library.getPlaylist(
                    playlistList.getSelectedIndex()).getSong(titleList.getSelectedIndex()).
                    getField(Song.TITLE));

         }

         /**
          * Updates progressBar, labelTimePlayed and labelTimeRemaining as the song progresses.
          */
         @Override
         public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
            String timePlayed = (int) microseconds / 60000000 + ":" + (microseconds / 1000000) % 60;
            if (timePlayed.substring(timePlayed.indexOf(":")).length() == 2) {
               timePlayed = (int) microseconds / 60000000 + ":0" + ((int) microseconds / 1000000) %
                       60;
            }

            String timeRemaining = (int) (time - microseconds) / 60000000 + ":" + ((time -
                    microseconds) / 1000000) % 60;
            if (timeRemaining.substring(timeRemaining.indexOf(":")).length() == 2) {
               timeRemaining = (int) (time - microseconds) / 60000000 + ":0" + ((int) (time -
                       microseconds) / 1000000) % 60;
            }

            labelTimePlayed.setText(timePlayed);
            labelTimeRemaining.setText("-" + timeRemaining);
            progressBar.setValue((int) microseconds);
         }

         /**
          * Plays the next track when the current one ends.
          * Sets the play/pause icon depending on current playback state.
          */
         @Override
         public void stateUpdated(BasicPlayerEvent event) {
            if (event.getCode() == javazoom.jlgui.basicplayer.BasicPlayerEvent.EOM) {
               library.getPlaylist(playingPlaylist).skipForwards(playingSong,
                       ((double) sliderVolume.getValue()) / 100);
            } else if (event.getCode() == javazoom.jlgui.basicplayer.BasicPlayerEvent.RESUMED) {
               buttonPlayPause.setIcon(new ImageIcon("src" + File.separator + "camo" +
                       File.separator + "resources" + File.separator + "pause.png"));
            } else if (event.getCode() == javazoom.jlgui.basicplayer.BasicPlayerEvent.PAUSED) {
               buttonPlayPause.setIcon(new ImageIcon("src" + File.separator + "camo" +
                       File.separator + "resources" + File.separator + "play.png"));
            } else if (event.getCode() == javazoom.jlgui.basicplayer.BasicPlayerEvent.OPENED) {
               buttonPlayPause.setIcon(new ImageIcon("src" + File.separator + "camo" +
                       File.separator + "resources" + File.separator + "pause.png"));
            } else if (event.getCode() == javazoom.jlgui.basicplayer.BasicPlayerEvent.STOPPED) {
               buttonPlayPause.setIcon(new ImageIcon("src" + File.separator + "camo" +
                       File.separator + "resources" + File.separator + "play.png"));
               dialog.setTitle("CAMO");
               labelTimePlayed.setText("");
               labelTimeRemaining.setText("");
               progressBar.setValue(0);
            }
         }

         /**
          * Not implemented.
          */
         @Override
         public void setController(BasicController controller) {
         }
      });
   }// createPlayListener
}// MinimalUI