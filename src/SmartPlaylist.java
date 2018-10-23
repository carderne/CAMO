package camo;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.*;
import java.util.regex.Pattern;

/**
 * This class holds the data for each playlist and includes methods
 * for accessing and altering that data.
 *
 * @author Chris Arderne
 * @version 3.0
 */
class SmartPlaylist extends Playlist {

   /**
    * The category on which to base the rule, i.e. title, artist, album or genre.
    */
   private int category;
   /**
    * The rule term.
    */
   private String rule;

   /**
    * Creates a new SmartPlaylist object with the specified parameters.
    * @param name The name for this playlist.
    * @param category The rule category for this playlist.
    * @param rule The rule phrase for this playlist.
    */
   SmartPlaylist(String name, int category, String rule) {
      super(name);
      this.category = category;
      this.rule = rule;
   }// constructor

   /**
    * Creates a String array of this playlist to be displayed in {@link camo.MainUI} object.
    * @param sortBy The int representing the field by which to sort the storeSongs.
    * @param query A search term if a search is being run, can be blank otherwise.
    * @return The songs to be displayed in {@link camo.MainUI}.
    */
   @Override
   synchronized ArrayList<Song> displaySongs(int sortBy, String query) {
      storeSongs.clear();
      for (int i = 0; i < mainSongs.size(); i++) {
         // If the song at i matches this smart playlist's rule.
         if (mainSongs.get(i).getField(category).equalsIgnoreCase(rule)) {
            storeSongs.add(mainSongs.get(i));
         }
      }

      ArrayList<Integer> indices = new ArrayList<Integer>();
      for (int i = 0; i < storeSongs.size(); i++) {
         String[] terms = Pattern.compile("\\s").split(query);
         boolean success = false;

         for (int k = 0; k < terms.length; k++) {
            if (storeSongs.get(i).getLineForSearch()
                    .toLowerCase().indexOf(terms[k].toLowerCase()) != -1) {
               success = true;
            } else {
               success = false;
               // Stop the inner for loop.
               break;
            }
         }
         if (success) {
            // If the check above was true, add the song index at i to the results.
            indices.add(i);
         }
      }

      displaySongs.clear();
      for (int i = 0; i < indices.size(); i++) {
         displaySongs.add(storeSongs.get(indices.get(i)));
      }

      // Sorts ouput based on the sortBy parameter.
      Song temp;
      for (int i = 0; i < displaySongs.size(); i++) {
         for (int j = i + 1; j < displaySongs.size(); j++) {
            if (displaySongs.get(i).getField(sortBy)
                    .compareTo(displaySongs.get(j).getField(sortBy)) > 0) {
               temp = displaySongs.get(i);
               displaySongs.set(i, displaySongs.get(j));
               displaySongs.set(j, temp);
            }
         }
      }

      return displaySongs;
   }// displaySongs

   /**
    * Exports this playlist to the specified destinaion.
    * @param destination The location to save this playlist.
    */
   @Override
   void exportPlaylist(File destination) {
      try {
         BufferedWriter export = new BufferedWriter(new FileWriter(destination));

         export.write("#CAMO SmartList: " + name);
         export.newLine();
         export.write("" + category);
         export.newLine();
         export.write("" + rule);

         export.close();
      } catch (IOException ex) {
         Logger.getLogger(SmartPlaylist.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// exportPlaylist

   /**
    * @return The category for this playlist.
    */
   int getCategory() {
      return category;
   }// getCategory

   /**
    * Edits this playlist based on the specified values.
    * @param name The new name for this SmartPlaylist.
    * @param category The new category for this SmartPlaylist.
    * @param rule The new rule for this SmartPlaylist.
    */
   void edit(int index, String name, int category, String rule) {
      File delete = new File("lists" + File.separator + index + " " + name + ".txt");
      delete.delete();

      this.name = name;
      this.category = category;
      this.rule = rule;
   }// edit

   /**
    * Skips back one place from the currently playing song.
    * @param playingSong The currently playong song.
    * @param volume The volume with which to play the song.
    * @return True if the skip successfully executes, false otherwise.
    */
   @Override
   boolean skipBack(int playingSong, double volume) {
      Player.stop();
      if (playingSong - 1 > -1) {
         return displaySongs.get(playingSong - 1).play(volume);
      } else {
         return false;
      }

   }// skipBack

   /**
    * Skips forward one place from the currently playing song.
    * @param playingSong The currently playing song.
    * @param volume The volume with which to play the song.
    * @return True if the skip successfully executes, false otherwise.
    */
   @Override
   boolean skipForwards(int playingSong, double volume) {
      Player.stop();
      if (playingSong + 1 < displaySongs.size()) {
         return displaySongs.get(playingSong + 1).play(volume);
      } else {
         return false;
      }
   }// skipForwards

   /**
    * Gets the rule term for this playlist.
    * @return The rule phrase for this playlist.
    */
   String getRule() {
      return rule;
   }// getRule
}// SmartPlaylist