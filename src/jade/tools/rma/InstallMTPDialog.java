/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/
 
package jade.tools.rma;

/** 
 *
 * @author Giovanni Rimassa - Universita` di Parma
 * @version $Date$ $Revision$
 */
class InstallMTPDialog extends javax.swing.JDialog {

  /** Creates new form InstallMTPDialog */
  public InstallMTPDialog(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    pack();
  }

  public void reset(String[] containers, String defaultContainer) {
    java.awt.Window owner = getOwner();
    setLocation(owner.getX() + (owner.getWidth() - getWidth()) / 2, owner.getY() + (owner.getHeight() - getHeight()) / 2);
    classField.setText(null);
    addressField.setText(null);
    containerList.removeAllElements();
    for(int i = 0; i < containers.length; i++)
      containerList.addElement(containers[i]);
    containerList.setSelectedItem(defaultContainer);
  }

  public String getAddress() {
    String s = addressField.getText();
    if(s.length() == 0)
      return null;
    else
      return s;
  }

  public String getClassName() {
    String s = classField.getText();
    if(s.length() == 0)
      return null;
    else
      return s;
  }

  public String getContainer() {
    return (String)containerChoice.getSelectedItem();
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the FormEditor.
   */
  private void initComponents() {//GEN-BEGIN:initComponents
    topPanel = new javax.swing.JPanel();
    containerLabel = new javax.swing.JLabel();

    // The list is initialized with a long string, to force a wide enough JComboBox
    containerList = new javax.swing.DefaultComboBoxModel(new String[] { "              " });

    containerChoice = new javax.swing.JComboBox(containerList);
    classLabel = new javax.swing.JLabel();
    classField = new javax.swing.JTextField();
    addressLabel = new javax.swing.JLabel();
    addressField = new javax.swing.JTextField();
    bottomPanel = new javax.swing.JPanel();
    buttonOK = new javax.swing.JButton();
    buttonCancel = new javax.swing.JButton();
    setResizable(false);
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Install a new MTP");
    setModal(true);
    setName("installMTPDlg");
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        closeDialog(evt);
      }
    }
    );

    topPanel.setLayout(new java.awt.GridLayout(3, 2, 10, 2));
    topPanel.setBorder(new javax.swing.border.CompoundBorder(
      new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)),
      new javax.swing.border.CompoundBorder(
        new javax.swing.border.BevelBorder(1),
	new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 2, 2, 2)))
      )
    );

    containerLabel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 2, 2, 2)));
    containerLabel.setText("Container:");
    containerLabel.setFont(new java.awt.Font("Dialog", 1, 12));
  
    topPanel.add(containerLabel);
  
    containerChoice.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 2, 2, 2)));
    containerChoice.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
  
    topPanel.add(containerChoice);
  
    classLabel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 2, 2, 2)));
    classLabel.setText("Class Name:");
    classLabel.setFont(new java.awt.Font("Dialog", 1, 12));
  
    topPanel.add(classLabel);
  
    classField.setToolTipText("Write here the name of the class implementing the MTP");
  
    topPanel.add(classField);
  
    addressLabel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 2, 2, 2)));
    addressLabel.setText("Address:");
    addressLabel.setFont(new java.awt.Font("Dialog", 1, 12));
  
    topPanel.add(addressLabel);
  
    addressField.setToolTipText("Write here the MTP address, if needed");
  
    topPanel.add(addressField);
  
    getContentPane().add(topPanel, java.awt.BorderLayout.CENTER);

    bottomPanel.setLayout(new java.awt.GridLayout(1, 2, 10, 0));
    bottomPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));

      buttonOK.setText("OK");
      buttonOK.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          buttonOKActionPerformed(evt);
        }
      }
      );
  
      bottomPanel.add(buttonOK);
  
      buttonCancel.setText("Cancel");
      buttonCancel.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          buttonCancelActionPerformed(evt);
        }
      }
      );
  
      bottomPanel.add(buttonCancel);
  

    getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

  }//GEN-END:initComponents

  private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
// Add your handling code here:
    confirmed = false;
    destroy();
  }//GEN-LAST:event_buttonCancelActionPerformed

  private void buttonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKActionPerformed
// Add your handling code here:
    confirmed = true;
    String cls = classField.getText();
    if(cls.length() == 0)
	javax.swing.JOptionPane.showMessageDialog(getParent(), "The class name for the MTP must be present", "Error during MTP installation.", javax.swing.JOptionPane.ERROR_MESSAGE);
    else
      destroy();
  }//GEN-LAST:event_buttonOKActionPerformed

  /** Closes the dialog */
  private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
    confirmed = false;
    destroy();
  }//GEN-LAST:event_closeDialog

  private void destroy() {
    setVisible(false);
    dispose();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel topPanel;
  private javax.swing.JLabel containerLabel;
  private javax.swing.DefaultComboBoxModel containerList;
  private javax.swing.JComboBox containerChoice;
  private javax.swing.JLabel classLabel;
  private javax.swing.JTextField classField;
  private javax.swing.JLabel addressLabel;
  private javax.swing.JTextField addressField;
  private javax.swing.JPanel bottomPanel;
  private javax.swing.JButton buttonOK;
  private javax.swing.JButton buttonCancel;
  // End of variables declaration//GEN-END:variables

  private boolean confirmed = false;

}
