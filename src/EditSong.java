package camo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 * Allows the user to edit song data.
 *
 * @author Chris Arderne
 * @version 3.0
 */
class EditSong {

   JDialog dialog;
   JTextField textFieldTitle;
   JTextField textFieldArtist;
   JTextField textFieldAlbum;
   JTextField textFieldGenre;
   /**
    * Whether or not OK was clicked.
    */
   boolean result = false;

   /**
    * Creates the {@link JDialog} with the specified {@link JFrame} as a parent.
    * @param parent The JFrame object to be the parent.
    */
   EditSong(JFrame parent) {
      dialog = new JDialog(parent, "Edit Song", true);
      dialog.setLocationByPlatform(true);
      dialog.setResizable(false);
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      JPanel panelTitle = new JPanel();
      JLabel labelTitle = new JLabel("Title:");
      labelTitle.setPreferredSize(new Dimension(50, 20));
      panelTitle.add(labelTitle);
      textFieldTitle = new JTextField();
      textFieldTitle.setPreferredSize(new Dimension(200, 20));
      textFieldTitle.addKeyListener(new KeyAdapter() {

         @Override
         public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
               result = true;
               dialog.dispose();
            }
         }
      });
      panelTitle.add(textFieldTitle);
      panel.add(panelTitle);

      JPanel panelArtist = new JPanel();
      JLabel labelArtist = new JLabel("Artist:");
      labelArtist.setPreferredSize(new Dimension(50, 20));
      panelArtist.add(labelArtist);
      textFieldArtist = new JTextField();
      textFieldArtist.setPreferredSize(new Dimension(200, 20));
      textFieldArtist.addKeyListener(new KeyAdapter() {

         @Override
         public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
               result = true;
               dialog.dispose();
            }
         }
      });
      panelArtist.add(textFieldArtist);
      panel.add(panelArtist);

      JPanel panelAlbum = new JPanel();
      JLabel labelAlbum = new JLabel("Album:");
      labelAlbum.setPreferredSize(new Dimension(50, 20));
      panelAlbum.add(labelAlbum);
      textFieldAlbum = new JTextField();
      textFieldAlbum.setPreferredSize(new Dimension(200, 20));
      textFieldAlbum.addKeyListener(new KeyAdapter() {

         @Override
         public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
               result = true;
               dialog.dispose();
            }
         }
      });
      panelAlbum.add(textFieldAlbum);
      panel.add(panelAlbum);

      JPanel panelGenre = new JPanel();
      JLabel labelGenre = new JLabel("Genre:");
      labelGenre.setPreferredSize(new Dimension(50, 20));
      panelGenre.add(labelGenre);
      textFieldGenre = new JTextField();
      textFieldGenre.setPreferredSize(new Dimension(200, 20));
      textFieldGenre.addKeyListener(new KeyAdapter() {

         @Override
         public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
               result = true;
               dialog.dispose();
            }
         }
      });
      panelGenre.add(textFieldGenre);
      panel.add(panelGenre);

      JPanel panelButtons = new JPanel();
      panelButtons.setLayout(new FlowLayout(FlowLayout.TRAILING));
      JButton buttonOK = new JButton("OK");
      buttonOK.setPreferredSize(new Dimension(75, 23));
      buttonOK.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            result = true;
            dialog.dispose();
         }
      });
      panelButtons.add(buttonOK);
      JButton buttonCancel = new JButton("Cancel");
      buttonCancel.setPreferredSize(new Dimension(75, 23));
      buttonCancel.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            result = false;
            dialog.dispose();
         }
      });
      panelButtons.add(buttonCancel);
      panel.add(panelButtons);

      dialog.add(panel);
      dialog.pack();
   }// default constructor

   /**
    * Sets the {@link JTextField} objects in the dialog to the specified values.
    * @param title The value for that JTextField.
    * @param artist The value for that JTextField.
    * @param album The value for that JTextField.
    * @param genre The value for that JTextField.
    */
   void setValues(String title, String artist, String album, String genre) {
      textFieldTitle.setText(title);
      textFieldArtist.setText(artist);
      textFieldAlbum.setText(album);
      textFieldGenre.setText(genre);
   }// setValues
}// EditSong