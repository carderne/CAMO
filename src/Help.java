package camo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Displays help.
 *
 * @author Chris Arderne
 * @version 3.0
 */
public class Help {
   /**
    * Creates the {@link JDialog} with the specified {@link JFrame} as a parent.
    * @param parent The JFrame object to be the parent.
    */
   Help(JFrame parent, String help) {
      final JDialog dialog = new JDialog(parent, "Help", true);
      dialog.setLocationByPlatform(true);
      dialog.setResizable(false);
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


      JTextArea output = new JTextArea(help);
      output.setEditable(false);
      output.setLineWrap(true);
      output.setWrapStyleWord(true);


      JScrollPane scrollPane =
              new JScrollPane(output, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setPreferredSize(new Dimension(1000, 620));
      scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0)));
      panel.add(scrollPane);

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
   }// constructor
}// Help