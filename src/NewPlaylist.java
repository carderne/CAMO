package camo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 * Allows the user to create a new playlist or edit an existing one.
 *
 * @author Chris Arderne
 * @version 3.0
 */
class NewPlaylist {

   JDialog dialog;
   JTextField textFieldName;
   /**
    * Whether or not OK was clicked.
    */
   boolean result = false;

   /**
    * Creates the {@link JDialog} with the specified {@link JFrame} as a parent.
    * @param parent The JFrame object to be the parent.
    */
   NewPlaylist(JFrame parent) {
      dialog = new JDialog(parent, "New Playlist", true);
      dialog.setLocationByPlatform(true);
      dialog.setResizable(false);
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      JPanel panelName = new JPanel();
      JLabel labelName = new JLabel("Name:");
      labelName.setPreferredSize(new Dimension(50, 20));
      panelName.add(labelName);
      textFieldName = new JTextField();
      textFieldName.setPreferredSize(new Dimension(200, 20));
      textFieldName.addKeyListener(new KeyAdapter() {

         @Override
         public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
               result = true;
               dialog.dispose();
            }
         }
      });
      panelName.add(textFieldName);
      panel.add(panelName);

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
    * Sets the {@link JTextField} object in the dialog to the specified value.
    * @param name The value for that JTextField.
    */
   void setValues(String name) {
      textFieldName.setText(name);
   }// setValues
}// NewPlaylist