import javax.swing.*;

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.GroupLayout.*;
import javax.swing.LayoutStyle.*;
import java.util.*;

@SuppressWarnings("serial")
/**
 * ViewEmployeePanel displays the calendar for an individually selectable
 * employee, in chronological order. It utilizes EventPanel as a subpanel.
 * 
 * @author Crowell, Irene
 *
 */
public class ViewEmployeePanel extends JPanel {

	/** The employee currently being viewed **/
	private static Employee currentEmployee;
	/** Displays the name of the employee **/
	private static JLabel lblName;
	/** Container of all the EventPanels **/
	private static Box verticalBox;
	private static JScrollPane scrollPane;
	/** Drop down menu to select which employee to view **/
	private static JComboBox<String> comboBoxEmployees;
	/** Contains all the EventPanels being displayed **/
	private static ArrayList<EventPanel> eventPanels = new ArrayList<EventPanel>();

	/**
	 * Initializes the GUI, and if there are employees displays the first one, otherwise it
	 * returns to the HomePanel.
	 */
	public ViewEmployeePanel() {
		currentEmployee = Data.getEmployees().get(0);
		lblName = new JLabel(currentEmployee.getName());
		JButton btnEditName = new JButton("Edit Name");
		btnEditName.addActionListener(new EditListener());

		comboBoxEmployees = new JComboBox<String>(
				Data.getEmployeeNames().toArray(new String[Data.getEmployeeNames().size()]));
		comboBoxEmployees.setSelectedIndex(0);
		comboBoxEmployees.addActionListener(new ComboBoxListener());

		verticalBox = Box.createVerticalBox();

		scrollPane = new JScrollPane(verticalBox);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JButton btnReturn = new JButton("Return to Home");
		btnReturn.addActionListener(new ReturnListener());

		JButton btnDeleteEmployee = new JButton("Delete Employee");
		btnDeleteEmployee.addActionListener(new DeleteEmployeeListener());
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addGap(7)
										.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 107,
												GroupLayout.PREFERRED_SIZE)
										.addGap(4).addComponent(btnEditName).addGap(169).addComponent(comboBoxEmployees,
												GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(scrollPane,
										GroupLayout.PREFERRED_SIZE, 560, GroupLayout.PREFERRED_SIZE))
								.addGroup(Alignment.TRAILING,
										groupLayout.createSequentialGroup().addContainerGap()
										.addComponent(btnDeleteEmployee)
										.addPreferredGap(ComponentPlacement.RELATED, 302, Short.MAX_VALUE)
										.addComponent(btnReturn)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(12).addComponent(lblName))
						.addGroup(groupLayout.createSequentialGroup().addGap(7).addComponent(btnEditName))
						.addGroup(groupLayout.createSequentialGroup().addGap(9).addComponent(comboBoxEmployees,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addGap(18).addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
				.addGap(18).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnReturn)
						.addComponent(btnDeleteEmployee))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		setLayout(groupLayout);

		if (Data.getEmployees().isEmpty()) {
			JOptionPane.showMessageDialog(Driver.getFrame(),
					"Error: You have not created any employees yet.\nYou are being returned to the home page.",
					"Error.", JOptionPane.PLAIN_MESSAGE);
			Driver.openPanel("Home");
		} else {
			displayEvents();
		}
	}

	/**Is activated when the employee selection comboBox is changed, and reloads and redisplays the events**/
	private class ComboBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			displayEvents();

		}

	}
	/**
	 * Returns to the home menu
	 * @author Crowell, Irene
	 *
	 */
	private class ReturnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Driver.openPanel("Home");
		}
	}
	/**
	 * Deletes the employee being viewed and reloads the panel to display the remaining employees.
	 * @author Crowell, Irene
	 *
	 */
	private class DeleteEmployeeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			final JOptionPane optionPane = new JOptionPane("Are you sure you want to permanently delete this employee? They will be removed from all groups as well.",
					JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);

			final JDialog dialog = new JDialog(Driver.getFrame(), "Verify Deleting Employee", true);
			dialog.setContentPane(optionPane);
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			optionPane.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					String prop = e.getPropertyName();

					if (dialog.isVisible() && (e.getSource() == optionPane)
							&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
						dialog.setVisible(false);
					}
				}
			});
			dialog.pack();
			dialog.setVisible(true);

			int value = ((Integer) optionPane.getValue()).intValue();
			if (value == JOptionPane.YES_OPTION) {
				Data.deleteEmployee(currentEmployee);
				refresh();
			}
		}
	}
	/**
	 * Changes the employee's names and displays the change on the panel.
	 * @author IreneC
	 *
	 */
	private class EditListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String s = (String) JOptionPane.showInputDialog(Driver.getFrame(), "Please enter the Employee's New Name",
					"Edit Employee Name", JOptionPane.PLAIN_MESSAGE, null, null, "");
			if ((s != null) && (s.length() > 0)) {
				boolean nameTaken=false;
				for(String a: Data.getGroupNames()){
					if(a.equals(s)){
						nameTaken=true;
					}
				}
				if(nameTaken){
					JOptionPane.showMessageDialog(Driver.getFrame(),
							"Error: There is already an employee with this name, please enter a new name.",
							"Error.",
							JOptionPane.PLAIN_MESSAGE);
				}
				else{
					currentEmployee.setName(s);
					comboBoxEmployees
					.setModel(new DefaultComboBoxModel<String>(Data.getEmployeeNames().
							toArray(new String[Data.getEmployeeNames().size()])));
					comboBoxEmployees.setSelectedItem(s);
					lblName.setText(currentEmployee.getName());
				}
				return;
			}
		}
	}

	/**
	 * Displays any changes to the GUI. If all employees have been deleted,
	 * the user is returned to the HomePanel.
	 */
	public static void refresh() {

		if (Data.getEmployees().isEmpty()) {
			JOptionPane.showMessageDialog(Driver.getFrame(),
					"Error: You have not created any employees yet."
					+ "\nYou are being returned to the home page.",
					"Error.", JOptionPane.PLAIN_MESSAGE);
			Driver.openPanel("Home");
		} else {
			comboBoxEmployees.setModel(new DefaultComboBoxModel<String>((Data.getEmployeeNames().
					toArray(new String[Data.getEmployeeNames().size()]))));
			comboBoxEmployees.setSelectedIndex(0);
			currentEmployee = Data.getEmployees().get(comboBoxEmployees.getSelectedIndex());
			lblName.setText(currentEmployee.getName());
			displayEvents();
		}
	}
	/**
	 * Removes an event from the panel (does not delete the event itself)
	 * @param toDelete	the index of the event to remove
	 */
	public static void deleteEvent(int toDelete) {
		verticalBox.remove(eventPanels.get(toDelete));
		verticalBox.revalidate();
		verticalBox.repaint();
	}
	/**
	 * Refreshes the EventPanel with the events currently associated with the selected
	 * employee.
	 */
	private static void displayEvents() {
		currentEmployee = Data.getEmployees().get(comboBoxEmployees.getSelectedIndex());

		for (EventPanel x : eventPanels) {
			verticalBox.remove(x);
		}
		eventPanels.clear();

		for (Event x : currentEmployee.getEvents()) {
				eventPanels.add(x.getPanel());
				verticalBox.add(x.getPanel());
			}
		lblName.setText(currentEmployee.getName());
		comboBoxEmployees.setSelectedItem(currentEmployee.getName());
		verticalBox.revalidate();
		verticalBox.repaint();
	}
}
