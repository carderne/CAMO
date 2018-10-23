package camo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Allows the user to select personal options for the program.
 *
 * @author Chris Arderne
 * @version 3.0
 */
class Preferences {

   JDialog dialog;
   JCheckBox checkBoxTitleDisplay;
   JRadioButton radioButtonColourThemeRed;
   JRadioButton radioButtonColourThemeGreen;
   JRadioButton radioButtonColourThemeBlue;
   /**
    * Whether or not OK was clicked.
    */
   boolean result = false;
   static int RED = 1;
   static int GREEN = 2;
   static int BLUE = 3;

   /**
    * Creates the {@link JDialog} with the specified {@link JFrame} as a parent.
    * @param parent The JFrame object to be the parent.
    */
   Preferences(JFrame parent) {
      dialog = new JDialog(parent, "Preferences", true);
      dialog.setLocationByPlatform(true);
      dialog.setResizable(false);
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      JPanel panelTitleDisplay = new JPanel();

      checkBoxTitleDisplay = new JCheckBox("Display now playing information in the title bar?");
      panelTitleDisplay.add(checkBoxTitleDisplay);
      panel.add(panelTitleDisplay);

      JPanel panelLabel = new JPanel();
      panelLabel.setLayout(new FlowLayout(FlowLayout.LEADING));
      JLabel labelColourTheme = new JLabel("Please select a colour theme:");
      panelLabel.add(labelColourTheme);
      panel.add(panelLabel);

      JPanel panelColourTheme = new JPanel();
      panelColourTheme.setLayout(new FlowLayout(FlowLayout.LEADING));

      radioButtonColourThemeRed = new JRadioButton("Red");
      radioButtonColourThemeGreen = new JRadioButton("Green");
      radioButtonColourThemeBlue = new JRadioButton("Blue");

      ButtonGroup buttonGroupColourTheme = new ButtonGroup();
      buttonGroupColourTheme.add(radioButtonColourThemeRed);
      buttonGroupColourTheme.add(radioButtonColourThemeGreen);
      buttonGroupColourTheme.add(radioButtonColourThemeBlue);

      panelColourTheme.add(radioButtonColourThemeRed);
      panelColourTheme.add(radioButtonColourThemeGreen);
      panelColourTheme.add(radioButtonColourThemeBlue);

      panel.add(panelColourTheme);

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
}// NewPlaylist