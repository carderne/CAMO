package camo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

/**
 * Displays the logo and certain information to do with CAMO.
 *
 * @author Chris Arderne
 * @version 3.0
 */
class About {
   /**
    * Creates the {@link JDialog} with the specified {@link JFrame} as a parent.
    * @param parent The JFrame object to be the parent.
    */
   About(JFrame parent) {
      final JDialog dialog = new JDialog(parent, "About", true);
      dialog.setLocationByPlatform(true);
      dialog.setResizable(false);
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      JPanel panelContent = new JPanel();
      JLabel labelIcon = new JLabel(new javax.swing.ImageIcon("src" + File.separator + "camo" +
              File.separator + "resources" + File.separator + "icon_about.png"));
      panelContent.add(labelIcon);

      JPanel panelDetails = new JPanel();
      panelDetails.setLayout(new BoxLayout(panelDetails, BoxLayout.Y_AXIS));
      JLabel labelTitle = new JLabel("CAMO");
      labelTitle.setFont(labelTitle.getFont().deriveFont(labelTitle.getFont().getStyle() |
              java.awt.Font.BOLD, labelTitle.getFont().getSize() + 4));
      panelDetails.add(labelTitle);
      JLabel labelDescription = new JLabel("<html><br>A simple music organiser, with playlist and<br>" +
              "smart playlists functions, a powerful search<br>and basic playback functionality.");
      panelDetails.add(labelDescription);
      JLabel labelVersion = new JLabel("<html><br><b>Version:</b>\t3.0");
      panelDetails.add(labelVersion);
      JLabel labelCreator = new JLabel("<html><br><b>Creator:</b>\tChris Arderne");
      panelDetails.add(labelCreator);
      panelContent.add(panelDetails);
      panel.add(panelContent);

      JPanel panelButtons = new JPanel();
      panelButtons.setLayout(new FlowLayout(FlowLayout.TRAILING));
      JButton buttonOK = new JButton("OK");
      buttonOK.setPreferredSize(new Dimension(75, 23));
      buttonOK.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent evt) {
            dialog.dispose();
         }
      });
      panelButtons.add(buttonOK);
      panel.add(panelButtons);

      dialog.add(panel);
      dialog.pack();
      dialog.setVisible(true);
   }// default constructor
}//About