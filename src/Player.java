package camo;

import java.io.File;
import java.util.logging.*;
import javazoom.jlgui.basicplayer.*;

/**
 * Allows the playback of music files.
 *
 * @author Chris Arderne
 * @version 3.0
 */
public class Player {

   /**
    * The BasicPlayer object that is used to playback music files.
    */
   static BasicPlayer player = new BasicPlayer();

   /**
    * Opens the specified music file.
    * @param musicFile The file to be opened by the BasicPlayer object.
    * @return True if the object is initialised succesfully, false otherwise.
    */
   static boolean initialise(File musicFile) {
      try {
         player.open(musicFile);
         return true;
      } catch (BasicPlayerException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
         return false;
      }
   }// initialise

   /**
    * Plays the file opened in the BasicPlayer object.
    */
   static void play() {
      try {
         player.play();
      } catch (BasicPlayerException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// play

   /**
    * Resumes playback if it has been paused.
    */
   static void resume() {
      try {
         player.resume();
      } catch (BasicPlayerException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// resume

   /**
    * Pauses playback.
    */
   static void pause() {
      try {
         player.pause();
      } catch (BasicPlayerException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// pause

   /**
    * Stops playback and allows the BasicPlayer object to open another music file.
    */
   static void stop() {
      try {
         player.stop();
      } catch (BasicPlayerException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// stop

   /**
    * Sets the volume of the BasicPlayer object to the specified value.
    * @param level The volume to be set, from 0 - 1.
    */
   static void setVolume(double level) {
      try {
         player.setGain(level);
      } catch (BasicPlayerException ex) {
         Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
      }
   }// setVolume
}// Player