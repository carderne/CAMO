package camo;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 * Contains all primary functions related to the music library.
 *
 * @author Chris Arderne
 * @version 3.0
 */
class Library {

   /**
    * Stores all of the Playlist objects.
    */
   private ArrayList<Playlist> lists = new ArrayList<Playlist>();

   /**
    * @param index The index for the {@link camo.Playlist} to be returned.
    * @return The {@link camo.Playlist} at the specified index.
    */
   Playlist getPlaylist(int index) {
      return lists.get(index);
   }// getPlaylist

   /**
    * Returns the number of playlists.
    * @return The size of the lists ArrayList.
    */
   int getListsSize() {
      return lists.size();
   }// getListsSize

   /**
    * Creates the 'Library' playlist and imports all playlist information.
    * @return The fileScanner used to read in the preferences.
    */
   Scanner restore() {
      Scanner fileScanner = null;
      try {
         fileScanner = new Scanner(new File("config.cfg"));
         if (fileScanner.hasNext()) {
            // The configuration file must start with this line.
            if (!fileScanner.nextLine().startsWith("#CAMO Configuration File")) {
               fileScanner = null;
            }
         }
      } catch (FileNotFoundException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }

      // This creates the main 'Library' playlist.
      if (lists.size() == 0) {
         lists.add(new MainPlaylist());
      }

      File dir = new File("lists" + File.separator);
      File[] files = dir.listFiles();
      if (files != null) {
         if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
               importPlaylist(files[i]);
            }
         }
      }
      return fileScanner;
   }// restore

   /**
    * Saves all playlist data.
    * @param nowPlayingInTitle
    * @param colourScheme
    */
   void save(boolean nowPlayingInTitle, int colourScheme) {
      try {
         BufferedWriter export = new BufferedWriter(new FileWriter(new File("config.cfg")));
         export.write("#CAMO Configuration File");
         export.newLine();
         export.write(String.valueOf(nowPlayingInTitle));
         export.newLine();
         export.write(String.valueOf(colourScheme));
         export.close();
      } catch (IOException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }

      // Delete all existing *.cfg playlist files so
      // that duplicates are not made.
      File[] filesToDelete = new File("lists" + File.separator).listFiles();
      for (int i = 0; i < filesToDelete.length; i++) {
         filesToDelete[i].delete();
      }

      for (int i = 0; i < lists.size(); i++) {
         lists.get(i).exportPlaylist(new File("lists" + File.separator + i +
                 " " + lists.get(i).getName() + ".cfg"));
      }
   }// save

   /**
    * Adds the specified music file to the library.
    * @param files The music file to be added.
    */
   void addFiles(File[] files) {
      try {
         for (int i = 0; i < files.length; i++) {
            lists.get(0).addSong(new Song(((TAudioFileFormat) AudioSystem.getAudioFileFormat(
                    files[i])).properties(), files[i]));
         }
      } catch (UnsupportedAudioFileException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// addFiles

   /**
    * Add the specified directory to the library.
    * @param directory The directory to be added.
    */
   synchronized void addDirectory(File directory) {
      File[] files = directory.listFiles();
      for (int i = 0; i < files.length; i++) {
         try {
            if (files[i].isDirectory()) {
               // If the path is a directory, pass it again to this method,
               // so that subdirectories are included.
               addDirectory(files[i]);
            } else if (files[i].toString().endsWith("mp3")) {
               // Set to only allow mp3 files.
               lists.get(0).addSong(new Song(((TAudioFileFormat) AudioSystem.getAudioFileFormat(
                       files[i])).properties(), files[i]));
            }
         } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }// addDirectory

   /**
    * Imports a playlist file into the library.
    * @param file The playlist file to be imported.
    */
   void importPlaylist(File file) {
      try {
         Scanner fileScanner = new Scanner(file);
         if (fileScanner.hasNext()) {
            String firstLine = fileScanner.nextLine();
            // If the selected playlist is a 'Library' playlist.
            if (firstLine.startsWith("#CAMO List: Library")) {
               while (fileScanner.hasNext()) {
                  Scanner lineScanner = new Scanner(fileScanner.nextLine()).useDelimiter("\t");
                  lists.get(0).addSong(new Song(lineScanner.next(),
                          lineScanner.next(),
                          lineScanner.next(),
                          lineScanner.next(),
                          lineScanner.nextLong(),
                          new File(lineScanner.next())));
                  lineScanner.close();
               }
               // If the selected playlist is a regular playlist.
            } else if (firstLine.startsWith("#CAMO List: ")) {
               lists.add(new Playlist(firstLine.substring(12)));
               while (fileScanner.hasNext()) {
                  Scanner lineScanner = new Scanner(fileScanner.nextLine()).useDelimiter("\t");
                  lists.get(lists.size() - 1).addSong(new Song(lineScanner.next(),
                          lineScanner.next(),
                          lineScanner.next(),
                          lineScanner.next(),
                          lineScanner.nextLong(),
                          new File(lineScanner.next())));
                  lineScanner.close();
               }
               // If the selected playlist is a smart playlist.
            } else if (firstLine.startsWith("#CAMO SmartList: ")) {
               lists.add(new SmartPlaylist(firstLine.substring(17),
                       Integer.parseInt(fileScanner.nextLine()),
                       fileScanner.nextLine()));
            }
         }
         fileScanner.close();
      } catch (java.io.FileNotFoundException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// importPlaylist

   /**
    * Creates a Playlist object with the specified parameters.
    * @param source The playlist from which to add tracks.
    * @param name The name for the new playlist.
    */
   void addPlaylist(int source, String name) {
      lists.add(new Playlist(name));
   }// addPlaylist

   /**
    * Creates a SmartPlaylist object with the specified parameters.
    * @param name he name for the new playlist.
    * @param category The int representing the category for the rule.
    * @param rule The rule phrase.
    */
   void addSmartPlaylist(String name, int category, String rule) {
      lists.add(new SmartPlaylist(name, category, rule));
   }// addSmartPlaylist

   /**
    * Compiles an array of Strings holding the playlist names.
    * @return A list of playlists to be displayed in {@link camo.MainUI}.
    */
   String[] displayPlaylists() {
      String[] display = new String[lists.size()];

      for (int i = 0; i < lists.size(); i++) {
         display[i] = "   " + lists.get(i).getNameForDisplay();
      }
      return display;
   }// showPlaylists

   /**
    * Removes the playlist at the specified index.
    * @param index The index for the Playlist to be deleted.
    */
   void removePlaylist(int index) {
      lists.remove(index);
   }// removePlaylist

   /**
    * Scans for and removes duplicate tracks.
    * @return The indices for the tracks to be removed.
    */
   int[] removeDuplicates() {
      int[] nums = new int[lists.get(0).getSize()];
      int numsLength = 0;

      for (int i = 0; i < lists.get(0).getSize(); i++) {
         for (int j = i + 1; j < lists.get(0).getSize(); j++) {
            // If two songs have the same file -
            // other metadata is NOT compared.
            if (lists.get(0).getSong(i).getFile().equals(lists.get(0).getSong(j).getFile())) {
               nums[numsLength++] = j;
            }
         }
      }

      nums = Arrays.copyOf(nums, numsLength);
      Arrays.sort(nums);

      return nums;
   }// removeDuplicates

   /**
    * Removes the specified indices from the playlist/library.
    * @param indices The indices to be removed.
    */
   void removeSongs(int[] indices) {
      // This first removes the songs from each playlist.
      // Outermost for loop iterates through the playlist.
      for (int i = 1; i < lists.size(); i++) {
         // Iterates through all the songs in the current playlist.
         for (int j = lists.get(i).getSize() - 1; j >= 0; j--) {
            // Iterates through the indices that are to be removed.
            for (int k = 0; k < indices.length; k++) {
               // If any of the songs in any of the playlists is
               // equal to a song that is to be removed from the Library.
               if (lists.get(i).getStoreSong(j).getFile()
                       .equals(lists.get(0).getSong(indices[k]).getFile())) {
                  lists.get(i).removeSong(j);
                  break;
               }
            }
         }
      }

      // Now the songs are removed from the main Library.
      lists.get(0).removeSongs(indices);
   }// removeSongs

   /**
    * Loads the help text from help.cfg.
    * @return The help text.
    */
   String loadHelp() {
      String help = "";
      try {
         Scanner helpScanner = new Scanner(new File("help.cfg"));
         while (helpScanner.hasNext()) {
            help += helpScanner.nextLine() + "\n";
         }
      } catch (FileNotFoundException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }
      return help;
   }
}// Library