package camo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Allows the user to create a new smart playlist or edit an existing one.
 *
 * @author Chris Arderne
 * @version 3.0
 */
class NewSmartPlaylist {

   JDialog dialog;
   JTextField textFieldName;
   JComboBox comboBoxCategory;
   JTextField textFieldRule;
   /**
    * Whether or not OK was clicked.
    */
   boolean result = false;

   /**
    * Creates the {@link JDialog} with the specified {@link JFrame} as a parent.
    * @param parent The JFrame object to be the parent.
    */
   NewSmartPlaylist(JFrame parent) {
      dialog = new JDialog(parent, "New Smart Playlist", true);
      dialog.setLocationByPlatform(true);
      dialog.setResizable(false);
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      JPanel panelName = new JPanel();
      panelName.setLayout(new FlowLayout(FlowLayout.LEADING));
      JLabel labelName = new JLabel("Name:");
      labelName.setPreferredSize(new Dimension(50, 20));
      panelName.add(labelName);
      textFieldName = new JTextField();
      textFieldName.setPreferredSize(new Dimension(200, 20));
      panelName.add(textFieldName);
      panel.add(panelName);

      JPanel panelMessage = new JPanel();
      panelMessage.setLayout(new FlowLayout(FlowLayout.LEADING));
      JLabel labelMessage = new JLabel("<html><br>Match the following rule:");
      panelMessage.add(labelMessage);
      panel.add(panelMessage);

      JPanel panelRule = new JPanel();
      panelRule.setLayout(new FlowLayout(FlowLayout.LEADING));
      comboBoxCategory = new JComboBox();
      comboBoxCategory.setModel(new DefaultComboBoxModel(
              new String[]{"Title", "Artist", "Album", "Genre"}));
      comboBoxCategory.setPreferredSize(new Dimension(100, 20));
      panelRule.add(comboBoxCategory);
      JLabel operator = new JLabel("is");
      operator.setHorizontalAlignment(SwingConstants.CENTER);
      operator.setPreferredSize(new Dimension(50, 20));
      panelRule.add(operator);
      textFieldRule = new JTextField();
      textFieldRule.setPreferredSize(new Dimension(200, 20));
      panelRule.add(textFieldRule);
      panel.add(panelRule);

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
    * @param name The value for that JTextField.
    * @param category The value for that JTextField.
    * @param rule The value for that JTextField.
    */
   void setValues(String name, int category, String rule) {
      textFieldName.setText(name);
      comboBoxCategory.setSelectedIndex(category);
      textFieldRule.setText(rule);
   }// setValues
}// NewSmartPlaylist