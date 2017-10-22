import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
/**
 * ViewGroupPanel displays the employees in an individually selectable group.
 * 
 * @author Crowell, Irene
 *
 */
public class ViewGroupPanel extends JPanel {
	/** The Group being displayed **/
	private static Group currentGroup;
	/** Shows the name of the group **/
	private static JLabel lblName;
	/** Drop Down menu for user to select which group to view **/
	private static JComboBox<String> comboBoxGroups;
	private static JScrollPane scrollPane;
	/** ArrayList of all the labels showing the group's employees' names **/
	private static ArrayList<JLabel> employeeLabels = new ArrayList<JLabel>();
	/**
	 * ArrayList of all the buttons allowing the user to delete an employee from
	 * the group
	 **/
	private static ArrayList<JButton> employeeButtons = new ArrayList<JButton>();
	/** A Subpanel that contains all the employeeLabels and employeeButtons **/
	private static JPanel panel;
	/**
	 * Iterator of where to add the next employeeLabel and employeeButton combo
	 * on panel
	 **/
	private static int panelRow = 0;

	/**
	 * Displays the GUI, unless there are no groups, in which case it redirects
	 * to the HomePanel
	 */
	public ViewGroupPanel() {

		currentGroup = Data.getGroups().get(0);
		lblName = new JLabel(currentGroup.getName());
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		JButton btnEditGroup = new JButton("Edit Group Name");
		btnEditGroup.addActionListener(new EditNameListener());

		comboBoxGroups = new JComboBox<String>(Data.getGroupNames().toArray(new String[Data.getGroupNames().size()]));
		comboBoxGroups.setSelectedIndex(0);
		comboBoxGroups.addActionListener(new ComboBoxListener());

		panel = new JPanel();
		panel.setLayout(new MigLayout("", "[15px,right][100px,left]", "[20px][]"));

		scrollPane = new JScrollPane(panel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JButton btnReturn = new JButton("Return to Home");
		btnReturn.addActionListener(new ReturnListener());

		JButton btnDeleteEmployeeGroup = new JButton("Delete Group");
		btnDeleteEmployeeGroup.addActionListener(new DeleteGroupListener());

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addGap(20)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addComponent(btnDeleteEmployeeGroup)
								.addPreferredGap(ComponentPlacement.RELATED, 315, Short.MAX_VALUE)
								.addComponent(btnReturn))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
								.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnEditGroup)
								.addPreferredGap(ComponentPlacement.RELATED, 135, Short.MAX_VALUE).addComponent(
										comboBoxGroups, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)))
				.addGap(30)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblName, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnEditGroup, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(comboBoxGroups, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)))
				.addGap(18).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
				.addGap(18).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDeleteEmployeeGroup).addComponent(btnReturn))
				.addGap(5)));
		setLayout(groupLayout);

		if (Data.getGroups().isEmpty()) {
			JOptionPane.showMessageDialog(Driver.getFrame(),
					"Error: You have not created any groups yet.\nYou are being returned to the home page.", "Error.",
					JOptionPane.PLAIN_MESSAGE);
			Driver.openPanel("Home");
		} else {
			displayEmployees();
		}
	}

	/**
	 * Changes the Group's name, unless a group with that name already exists,
	 * in which case it alerts the user to this and does not change.
	 * 
	 * @author Crowell, Irene
	 *
	 */
	private class EditNameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String s = (String) JOptionPane.showInputDialog(Driver.getFrame(), "Please enter the Group's New Name",
					"Edit Group Name", JOptionPane.PLAIN_MESSAGE, null, null, "");

			// If a string was returned, say so.
			if ((s != null) && (s.length() > 0)) {
				boolean nameTaken = false;
				for (String a : Data.getGroupNames()) {
					if (a.equals(s)) {
						nameTaken = true;
					}
				}
				if (nameTaken) {
					JOptionPane.showMessageDialog(Driver.getFrame(),
							"Error: There is already a group with this name, please enter a new name.", "Error.",
							JOptionPane.PLAIN_MESSAGE);
				} else {
					currentGroup.setName(s);
					comboBoxGroups.setModel(new DefaultComboBoxModel<String>(
							Data.getGroupNames().toArray(new String[Data.getGroupNames().size()])));
					comboBoxGroups.setSelectedItem(s);
					lblName.setText(currentGroup.getName());
				}
				return;
			}
		}
	}

	/**
	 * Changes the employees being displayed when a different group is selected
	 * 
	 * @author IreneC
	 *
	 */
	private class ComboBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			currentGroup = Data.getGroups().get(comboBoxGroups.getSelectedIndex());
			displayEmployees();
		}

	}

	/**
	 * Asks the user, and then deletes an employee from the group
	 * 
	 * @author Crowell, Irene
	 *
	 */
	private class DeleteEmployeeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			final JOptionPane optionPane = new JOptionPane(
					"Are you sure you want to permanently remove this employee from the group?",
					JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);

			final JDialog dialog = new JDialog(Driver.getFrame(), "Verify Removing Employee", true);
			dialog.setContentPane(optionPane);
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			optionPane.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					String prop = e.getPropertyName();
					if (dialog.isVisible() && (e.getSource() == optionPane)
							&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
						// If you were going to check something
						// before closing the window, you'd do
						// it here.
						dialog.setVisible(false);
					}
				}
			});
			dialog.pack();
			dialog.setVisible(true);

			int value = ((Integer) optionPane.getValue()).intValue();
			if (value == JOptionPane.YES_OPTION) {
				int toDelete = employeeButtons.indexOf(e.getSource());
				currentGroup.removeEmployee(toDelete);
				if (currentGroup.getEmployees().size() < 2) {
					Data.deleteGroup(currentGroup);
				}
				if (Data.getGroups().isEmpty()) {
					JOptionPane.showMessageDialog(Driver.getFrame(),
							"Error: You have not created any groups yet.\nYou are being returned to the home page.",
							"Error.", JOptionPane.PLAIN_MESSAGE);
					Driver.openPanel("Home");
				} else {
					displayEmployees();
				}
			}
		}

	}

	/**
	 * Asks the user, and then deletes the group being viewed, and refreshes the
	 * GUI to display the changes.
	 * 
	 * @author Crowell, Irene
	 *
	 */
	private class DeleteGroupListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			final JOptionPane optionPane = new JOptionPane("Are you sure you want to permanently delete this group?",
					JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);

			final JDialog dialog = new JDialog(Driver.getFrame(), "Verify Deleting Group", true);
			dialog.setContentPane(optionPane);
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			optionPane.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					String prop = e.getPropertyName();

					if (dialog.isVisible() && (e.getSource() == optionPane)
							&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
						// If you were going to check something
						// before closing the window, you'd do
						// it here.
						dialog.setVisible(false);
					}
				}
			});
			dialog.pack();
			dialog.setVisible(true);

			int value = ((Integer) optionPane.getValue()).intValue();
			if (value == JOptionPane.YES_OPTION) {
				Data.deleteGroup(currentGroup);
				if (Data.getGroups().isEmpty()) {
					JOptionPane.showMessageDialog(Driver.getFrame(),
							"Error: You have not created any groups yet.\nYou are being returned to the home page.",
							"Error.", JOptionPane.PLAIN_MESSAGE);
					Driver.openPanel("Home");
				} else {
					comboBoxGroups.setModel(new DefaultComboBoxModel<String>(
							Data.getGroupNames().toArray(new String[Data.getGroupNames().size()])));
					comboBoxGroups.setSelectedIndex(0);
					currentGroup = Data.getGroups().get(comboBoxGroups.getSelectedIndex());
				}
			}
		}
	}

	/**
	 * Returns to the HomePanel
	 * 
	 * @author Crowell, Irene
	 *
	 */
	private class ReturnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Driver.openPanel("Home");
		}
	}

	/**
	 * Redisplays all the employees in the group
	 */
	private void displayEmployees() {
		for (JLabel x : employeeLabels) {
			panel.remove(x);
		}
		for (JButton x : employeeButtons) {
			panel.remove(x);
		}
		employeeLabels.clear();
		employeeButtons.clear();
		for (Employee e : currentGroup.getEmployees()) {
			JLabel lblNewLabel = new JLabel(e.getName());
			employeeLabels.add(lblNewLabel);
			panel.add(lblNewLabel, "cell 0 " + panelRow);

			JButton btnRemoveEmployee = new JButton("Remove");
			employeeButtons.add(btnRemoveEmployee);
			panel.add(btnRemoveEmployee, "cell 1 " + panelRow);
			btnRemoveEmployee.addActionListener(new DeleteEmployeeListener());
			panelRow++;
		}
		lblName.setText(currentGroup.getName());
		comboBoxGroups.setSelectedItem(currentGroup.getName());
		panel.revalidate();
		panel.repaint();
		scrollPane.setViewportView(panel);
		scrollPane.revalidate();
		scrollPane.repaint();
	}

	/**
	 * Reloads the full GUI for any changes.
	 */
	public void refresh() {
		comboBoxGroups.setModel(new DefaultComboBoxModel<String>(
				Data.getGroupNames().toArray(new String[Data.getGroupNames().size()])));
		comboBoxGroups.setSelectedIndex(0);
		currentGroup = Data.getGroups().get(comboBoxGroups.getSelectedIndex());
		lblName.setText(currentGroup.getName());
		displayEmployees();

	}
}