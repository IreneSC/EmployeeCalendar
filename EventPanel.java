import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout.*;

@SuppressWarnings("serial")
/**
 * EventPanel displays the information for an event, including the date, name,
 * description, and a button to delete the event. It is used as a subpanel on
 * the ViewEmployeePanel
 * @author IreneC
 *
 */
public class EventPanel extends JPanel {
	Event event;
	public EventPanel(Event event) {
		this.event=event;
		createGUI();
	}
	/**
	 * Deletes the event being displayed on the panel, and has ViewEmployeePanel refresh
	 * @author Crowell, Irene
	 */
	private class DeleteListener implements ActionListener{
		public void actionPerformed(ActionEvent e) 
		{  
			final JOptionPane optionPane = new JOptionPane(
					"Are you sure you want to permanently delete this event?",
					JOptionPane.QUESTION_MESSAGE,
					JOptionPane.YES_NO_OPTION);

			final JDialog dialog = new JDialog(Driver.getFrame(), 
					"Verify Deleting Event",
					true);
			dialog.setContentPane(optionPane);
			dialog.setDefaultCloseOperation(
					JDialog.DO_NOTHING_ON_CLOSE);
			optionPane.addPropertyChangeListener(
					new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent e) {
							String prop = e.getPropertyName();

							if (dialog.isVisible() 
									&& (e.getSource() == optionPane)
									&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
								dialog.setVisible(false);
							}
						}
					});
			dialog.pack();
			dialog.setVisible(true);

			int value = ((Integer)optionPane.getValue()).intValue();
			if (value == JOptionPane.YES_OPTION) {
				event.getEmployee().deleteEvent(event);
				ViewEmployeePanel.refresh();
			}

		}
	}
	private void createGUI(){
		JLabel lblName = new JLabel(event.getName());

		JLabel lblDate = new JLabel(event.getFormattedDate());

		JTextArea Description = new JTextArea(event.getDescription());

		Description.setEditable(false);
		Description.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(Description);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new DeleteListener());
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(2)
					.addComponent(lblDate, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnDelete)
					.addGap(35))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(9)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblName)
							.addComponent(lblDate))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 90, 90)
							.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)))
					.addGap(10))
		);
		setLayout(groupLayout);
	}
}
