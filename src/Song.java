package camo;

import java.io.File;
import java.util.Map;

/**
 * This class holds the data for each song and includes methods for accessing and altering that data.
 * All playback functions are included.
 *
 * @author Chris Arderne
 * @version 3.0
 */
class Song {
   /**
    * This song's title.
    */
   private String title;
   /**
    * This song's artist.
    */
   private String artist;
   /**
    * This song's album.
    */
   private String album;
   /**
    * This song's genre.
    */
   private String genre;
   /**
    * This song's duration.
    */
   private Long duration;
   /**
    * This song's file path.
    */
   private File file;
   /**
    * The static int to return the title value from the getField method.
    */
   static final int TITLE = 0;
   /**
    * The static int to return the artist value from the getField method.
    */
   static final int ARTIST = 1;
   /**
    * The static int to return the album value from the getField method.
    */
   static final int ALBUM = 2;
   /**
    * The static int to return the genre value from the getField method.
    */
   static final int GENRE = 3;
   /**
    * The default static int to return the artist value from the getField method.
    */
   static final int DEFAULT = 1;

   /**
    * Creates a Song object with metadata from the specified Map object and a
    * pointer to the specified File object.
    * @param properties A {@link java.util.Map} object with the song metadata.
    * @param file A pointer to the relevant song file.
    */
   Song(Map properties, File file) {
      this((String) properties.get("title"),
              (String) properties.get("author"),
              (String) properties.get("album"),
              (String) properties.get("mp3.id3tag.genre"),
              (Long) properties.get("duration"),
              file);
   }// constructor

   /**
    * Creates a Song object with all the specified parameters.
    * @param title
    * @param artist
    * @param album
    * @param genre
    * @param duration
    * @param file A pointer to the relevant song file.
    */
   Song(String title, String artist, String album, String genre, long duration, File file) {
      if (title == null || title.equals("") || title.length() < 1) {
         title = "Unknown Title";
      }
      this.title = title;

      if (artist == null || artist.equals("") || artist.length() < 1) {
         artist = "Unknown Artist";
      }
      this.artist = artist;

      if (album == null || album.equals("") || album.length() < 1) {
         album = "Unknown Album";
      }
      this.album = album;

      if (genre == null || genre.equals("") || genre.length() < 1) {
         genre = "Unknown Genre";
      }
      this.genre = genre;

      this.duration = new Long(duration);
      this.file = file;
   }// constructor

   /**
    * Plays this song.
    * @param volume The double value for the volume of playback.
    * @return True if the operation executes successfully, false otherwise.
    */
   boolean play(double volume) {
      if (Player.initialise(file)) {
         Player.play();
         Player.setVolume(volume);
         return true;
      } else {
         return false;
      }
   }// play

   /**
    * Edit the metadata for this song.
    * @param title
    * @param artist
    * @param album
    * @param genre
    */
   void edit(String title, String artist, String album, String genre) {
      this.title = title;
      this.artist = artist;
      this.album = album;
      this.genre = genre;
   }// edit

   /**
    * Gets a value based on the specified int.
    * @param field An int representing which value should be returned.
    * @return The value corresponding to the inputted int.
    */
   String getField(int field) {
      if (field == TITLE) {
         return title;
      } else if (field == ARTIST) {
         return artist;
      } else if (field == ALBUM) {
         return album;
      } else if (field == GENRE) {
         return genre;
      } else {
         return artist;
      }
   }// getField

   /**
    * Gets the metadata for output to a playlist file.
    * @return The metadata for this song formatted for exporting to a playlist file.
    */
   String getLineForExport() {
      return title + "\t" + artist + "\t" +album + "\t" +
             genre + "\t" + duration + "\t" + file;
   }// getLineForExport

   /**
    * Gets the metadata for search functions.
    * @return The metadata for this song formatted
    * for search purposes - the file and duration attributes are excluded.
    */
   String getLineForSearch() {
      return title + artist + album + genre;
   }// getLineForSearch

   /**
    * Formats the duration for display in mm:ss format.
    *@return The value of this song's duration in a displayable format.
    */
   String formattedDuration() {
      String formattedDuration = duration.intValue() /
              60000000 + ":" + (duration.intValue() / 1000000) % 60;
      if (formattedDuration.substring(formattedDuration.indexOf(":")).length() == 2) {
         formattedDuration = duration.intValue() /
                 60000000 + ":0" + (duration.intValue() / 1000000) % 60;
      }

      if (formattedDuration.length() < 5) {
         formattedDuration = " " + formattedDuration;
      }
      return formattedDuration;
   }//formatDuration

   /**
    * Gets this song's file path.
    * @return The {@link java.io.File} of this song.
    */
   File getFile() {
      return file;
   }// getFile

   /**
    * Gets the duration of this song in microseconds.
    * @return The duration in milliseconds of this song.
    */
   Long getDuration() {
      return duration;
   }// getDuration
}// Song