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
class Playlist {

   /**
    * The songs in the 'Library' playlist.
    */
   static ArrayList<Song> mainSongs = new ArrayList<Song>();
   /**
    * All the songs in this playlist.
    */
   ArrayList<Song> storeSongs = new ArrayList<Song>();
   /**
    * The songs in this playlist after searches etc.
    */
   ArrayList<Song> displaySongs = new ArrayList<Song>();
   /**
    * This playlist's name.
    */
   String name;
   /**
    * The field by which to sort this playlist.
    */
   int sortBy = Song.DEFAULT;
   /**
    * Stores the selected index in this playlist so that highlighted songs are remembered.
    */
   int[] selectedIndices = new int[0];

   /**
    * Creates a new Playlist object with the specified parameters.
    * Calculates the ID based on the static idCounter field.
    * @param name The name for this playlist.
    */
   Playlist(String name) {
      if (name.equals("")) {
         name = "new playlist";
      }
      this.name = name;
   }// constructor

   /**
    * Gets the song at the specified index from this playlist's temporary storage.
    * @param index The index for the song that is to be retrieved.
    * @return The {@link camo.Song} at the specified index.
    */
   Song getSong(int index) {
      return displaySongs.get(index);
   }// getSong

   /**
    * Gets the song at the specified index from this playlist's main storage.
    * @param index  The index for the song that is to be retrieved.
    * @return The {@link java.util.ArrayList} containing the {@link camo.Song} objects.
    */
   Song getStoreSong(int index) {
      return storeSongs.get(index);
   }// getSong

   /**
    * Gets the total number of songs in this playlist.
    * @return The size of the storeSongs ArrayList.
    */
   int getSize() {
      return storeSongs.size();
   }// getSize

   /**
    * Add the specified {@link camo.Song} object to the playlist.
    * @param song The (@link camo.Song} object to be added.
    */
   void addSong(Song song) {
      storeSongs.add(song);
   }// addSong

   /**
    * Removes the specified indices from the playlist.
    * @param indices The array containing the indices to be removed.
    */
   void removeSongs(int[] indices) {
      for (int i = 0; i < indices.length; i++) {
         storeSongs.remove(displaySongs.get(indices[i]));
      }
   }// removeSongs

   /**
    * Removes the specified {@link camo.Song} object.
    * @param index The index for the Song object to be removed.
    */
   void removeSong(int index) {
      storeSongs.remove(index);
   }// removeSong

   /**
    * Creates a String array of this playlist to be displayed in {@link camo.MainUI} object.
    * @param sortBy The int representing the field by which to sort the storeSongs.
    * @param query A search term if a search is being run, can be blank otherwise.
    * @return The songs to be displayed in {@link camo.MainUI}.
    */
   synchronized ArrayList<Song> displaySongs(int sortBy, String query) {
      ArrayList<Integer> indices = new ArrayList<Integer>();
      for (int i = 0; i < storeSongs.size(); i++) {
         // Creates an array of Strings based on each term in the search query,
         // split up around 'space' characters.
         String[] terms = Pattern.compile("\\s").split(query);
         boolean success = false;

         for (int k = 0; k < terms.length; k++) {
            // Finds any instances of the search query in each song's metadata.
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
    * Skips back one place from the currently playing song.
    * @param playingSong The currently playong song.
    * @param volume The volume with which to play the song.
    * @return True if the skip successfully executes, false otherwise.
    */
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
   boolean skipForwards(int playingSong, double volume) {
      Player.stop();
      if (playingSong + 1 < displaySongs.size()) {
         return displaySongs.get(playingSong + 1).play(volume);
      } else {
         return false;
      }
   }// skipForwards

   /**
    * Exports this playlist to the specified destinaion.
    * @param destination The location to save this playlist.
    */
   void exportPlaylist(File destination) {
      try {
         BufferedWriter export = new BufferedWriter(new FileWriter(destination));
         export.write("#CAMO List: " + name);

         for (int i = 0; i < storeSongs.size(); i++) {
            export.newLine();
            export.write(storeSongs.get(i).getLineForExport());
         }

         export.close();
      } catch (IOException ex) {
         Logger.getLogger(Playlist.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// exportPlaylist

   /**
    * Gets this playlist's name.
    * @return This playlist's name.
    */
   String getName() {
      return name;
   }// getName

   /**
    * Gets this playlist's name, formatted for display in {@link camo.MainUI}.
    * @return This playlist's name.
    */
   String getNameForDisplay() {
      if (name.length() > 24) {
         return name.substring(0, 22) + "...";
      } else {
         return name;
      }
   }// getName

   /**
    * Gets the total time of this playlist to display in the status bar.
    * @return The time in decimal format.
    */
   String getTotalTime(ArrayList<Song> songs) {
      double time = 0;
      for (int i = 0; i < songs.size(); i++) {
         time += songs.get(i).getDuration();
      }

      String formattedDuration;

      // If the total time is less than 60 seconds.
      if (time / 1000000 < 60) {
         formattedDuration = (time / 1000000) + " seconds";
         // If the total time is less than 60 minutes.
      } else if (((time / 1000000) / 60) < 60) {
         formattedDuration = ((time / 1000000) / 60) + "";
         if (!formattedDuration.equals("1.0")) {
            formattedDuration = formattedDuration.substring(0,
                    formattedDuration.lastIndexOf('.') + 2) + " minutes";
         } else {
            formattedDuration = formattedDuration.substring(0,
                    formattedDuration.lastIndexOf('.') + 2) + " minute";
         }
         // If the total time is less than 24 hours.
      } else if ((((time / 1000000) / 60) / 60) < 24) {
         formattedDuration = (((time / 1000000) / 60) / 60) + "";
         if (!formattedDuration.equals("1.0")) {
            formattedDuration = formattedDuration.substring(0,
                    formattedDuration.lastIndexOf('.') + 2) + " hours";
         } else {
            formattedDuration = formattedDuration.substring(0,
                    formattedDuration.lastIndexOf('.') + 2) + " hour";
         }
         
      } else {
         formattedDuration = ((((time / 1000000) / 60) / 60) / 24) + "";
         if (!formattedDuration.equals("1.0")) {
            formattedDuration = formattedDuration.substring(0,
                    formattedDuration.lastIndexOf('.') + 2) + " days";
         } else {
            formattedDuration = formattedDuration.substring(0,
                    formattedDuration.lastIndexOf('.') + 2) + " day";
         }
      }
      return formattedDuration;
   }// getTotalTime

   /**
    * Edits the playlist with the specified values.
    * @param index This Playlist's index so that the playlist file can be deleted.
    * @param name The new name for this playlist.
    */
   void edit(int index, String name) {
      File delete = new File("lists" + File.separator + index + " " + name + ".txt");
      delete.delete();

      this.name = name;
   }// edit
}// Playlist