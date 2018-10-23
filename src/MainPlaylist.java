package camo;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.regex.Pattern;

/**
 * Contains all the primary playlist containing all storeSongs in the library.
 *
 * @author Chris Arderne
 * @version 3.0
 */
class MainPlaylist extends Playlist {

   /**
    * This playlist always has the name 'Library'.
    */
   MainPlaylist() {
      super("Library");
   }// constructor

   /**
    * Gets the song at the specified index.
    * @param index The index for the song to be returned.
    * @return The {@link camo.Song} at the specified index.
    */
   @Override
   Song getSong(int index) {
      return displaySongs.get(index);
   }// getSong

   /**
    * Gets the size of the 'Library' playlist.
    * @return The size of the mainSongs ArrayList.
    */
   @Override
   int getSize() {
      return mainSongs.size();
   }// getSize

   /**
    * Add the specified {@link camo.Song} object to the playlist.
    * @param song The (@link camo.Song} object to be added.
    */
   @Override
   void addSong(Song song) {
      mainSongs.add(song);
   }// addSong

   /**
    * Removes the specified indices from the playlist.
    * @param indices The array containing the indices to be removed.
    */
   @Override
   void removeSongs(int[] indices) {
      for (int i = 0; i < indices.length; i++) {
         mainSongs.remove(displaySongs.get(indices[i]));
      }
   }// removeSongs

   /**
    * Creates a String array of this playlist to be displayed in {@link camo.MainUI} object.
    * @param sortBy The int representing the field by which to sort the storeSongs.
    * @param query A search term if a search is being run, can be blank otherwise.
    * @return The songs to be displayed in {@link camo.MainUI}.
    */
   @Override
   synchronized ArrayList<Song> displaySongs(int sortBy, String query) {
      ArrayList<Integer> indices = new ArrayList<Integer>();
      for (int i = 0; i < mainSongs.size(); i++) {
         // Creates an array of Strings based on each term in the search query,
         // split up around 'space' characters.
         String[] terms = Pattern.compile("\\s").split(query);
         boolean success = false;

         for (int k = 0; k < terms.length; k++) {
            // Finds any instances of the search query in each song's metadata.
            if (mainSongs.get(i).getLineForSearch()
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
         displaySongs.add(mainSongs.get(indices.get(i)));
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
         export.write("#CAMO List: " + name);

         for (int i = 0; i < mainSongs.size(); i++) {
            export.newLine();
            export.write(mainSongs.get(i).getLineForExport());
         }

         export.close();
      } catch (IOException ex) {
         Logger.getLogger(MainPlaylist.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// exportPlaylist
}// MainPlaylist