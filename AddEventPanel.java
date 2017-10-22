import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import net.miginfocom.swing.MigLayout;
import java.util.Calendar;

@SuppressWarnings("serial") 
/**
 * AddEventPanel is a JPanel that displays a GUI for the user to create
 * an event for an employee or group of employees
 * 
 * @author      Crowell, Irene
 */
public class AddEventPanel extends JPanel {
	private JLabel eventType = new JLabel("Employee:");
	private JTextField eventNameTextField;
	private JComboBox<String> subjectComboBox = new JComboBox<String>();
	private JComboBox<String> monthComboBox;
	private JComboBox<String> dayComboBox;
	private JComboBox<String> yearComboBox;
	private JRadioButton rdbtnEmployeeEvent;
	private JRadioButton rdbtnGroupEvent;
	private JTextPane eventDescriptionTextPane;
	private JButton btnCancel;
	private JButton btnSave;
	/**True if event is for employee, false if for group**/
	private boolean employeeMode = true;
	
	/**
	 * Create the GUI for the Panel, defaulting to employee mode with the first employee selected.
	 * <p>
	 * Initializes the ComboBoxes with default values (All employees, 12 months, 31 days, 5 years
	 * starting at current year. Adds all components, including local variable labels, into the
	 * GUI. Adds actionListeners to the buttons.
	 */
	public AddEventPanel() {
		subjectComboBox.setModel(new DefaultComboBoxModel<String>(Data.getEmployeeNames().
				toArray(new String[Data.getEmployeeNames().size()])));

		JLabel lblAddEvent = new JLabel("Add Event");
		lblAddEvent.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddEvent.setVerticalAlignment(SwingConstants.TOP);
		lblAddEvent.setFont(new Font("Tahoma", Font.BOLD, 20));

		eventType.setHorizontalAlignment(SwingConstants.TRAILING);
		eventType.setLabelFor(subjectComboBox);

		Box horizontalBox = Box.createHorizontalBox();

		JLabel lblEventName = new JLabel("Event Name:");

		eventNameTextField = new JTextField();
		lblEventName.setLabelFor(eventNameTextField);
		eventNameTextField.setColumns(10);

		JLabel lblEventDate = new JLabel("Event Date:");
		lblEventDate.setHorizontalAlignment(SwingConstants.TRAILING);

		monthComboBox = new JComboBox<String>();
		monthComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"January", 
				"February", "March", "April", "May", "June", "July", "August", "September", 
				"October", "November", "December"}));

		dayComboBox = new JComboBox<String>();
		dayComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2", "3", 
				"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", 
				"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));

		String[] years= new String[5];
		Calendar rightNow = Calendar.getInstance();
		for(int i =0; i<5; i++){
			int year = rightNow.get(Calendar.YEAR)+i;
			years[i]=""+year;
		}
		yearComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(years));
		yearComboBox.setEditable(true);

		JLabel lblEventDescription = new JLabel("Event Description:");
		lblEventDescription.setHorizontalAlignment(SwingConstants.LEFT);

		eventDescriptionTextPane = new JTextPane();

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue_1);

		rdbtnEmployeeEvent = new JRadioButton("Employee Event");
		horizontalBox.add(rdbtnEmployeeEvent);
		rdbtnEmployeeEvent.setVerticalAlignment(SwingConstants.TOP);
		rdbtnEmployeeEvent.setSelected(true);
		rdbtnEmployeeEvent.addActionListener(new rdbtnListener(true));
		rdbtnGroupEvent = new JRadioButton("Group Event");
		rdbtnGroupEvent.addActionListener(new rdbtnListener(false));
		horizontalBox.add(rdbtnGroupEvent);

		ButtonGroup rdbtnGroup = new ButtonGroup();
		rdbtnGroup.add(rdbtnGroupEvent);
		rdbtnGroup.add(rdbtnEmployeeEvent);

		setLayout(new MigLayout("", "[93px][16px][80px][16px][37px][16px][103px]",
				"[25px][23px][20px][20px][20px][92px][23px]"));
		add(lblAddEvent, "cell 0 0 7 1,growx,aligny top");
		add(lblEventDescription, "cell 0 5,alignx right,aligny top");
		add(lblEventName, "cell 0 3,alignx right,growy");
		add(eventType, "cell 0 2,alignx right,growy");
		add(lblEventDate, "cell 0 4,alignx right,aligny center");
		add(eventNameTextField, "cell 2 3 5 1,growx,aligny top");
		add(subjectComboBox, "cell 2 2 5 1,growx,aligny top");
		add(monthComboBox, "cell 2 4,growx,aligny top");
		add(dayComboBox, "cell 4 4,alignx left,aligny top");
		add(yearComboBox, "cell 6 4,growx,aligny top");
		add(horizontalBox, "cell 0 1 7 1,growx,aligny top");

		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		add(eventDescriptionTextPane, "cell 2 5 5 1,grow");

		btnCancel = new JButton("Cancel");
		add(btnCancel, "cell 0 6,growx");
		btnCancel.addActionListener (new CancelListener());

		btnSave = new JButton("Save");
		add(btnSave, "cell 6 6,alignx right,aligny top");
		btnSave.addActionListener (new SaveListener());

	}
	/**
	 * rdbtnListener changes the GUI to be in employeeMode or not (group mode). 
	 * rdbtnListener is used by rdbtnGroupEvent and rdbtnEmployeeEvent.
	 * 
	 * @author Crowell, Irene
	 *
	 */
	private class rdbtnListener implements ActionListener {  
		boolean Mode;
		public rdbtnListener(boolean mode){
			Mode = mode;
		}
		public void actionPerformed(ActionEvent e) 
		{  
			employeeMode = Mode;
			if(employeeMode){
				eventType.setText("Employee:");
				subjectComboBox.removeAllItems();
				subjectComboBox.setModel(new DefaultComboBoxModel<String>(
						Data.getEmployeeNames().toArray(new String[Data.getEmployeeNames().size()])));
			}
			else{
				if(Data.getGroups().isEmpty()){
					JOptionPane.showMessageDialog(Driver.getFrame(),
							"Error: You have not created any groups yet.",
							"Error.",
							JOptionPane.PLAIN_MESSAGE);
					employeeMode=true;
					rdbtnEmployeeEvent.setSelected(true);
				}
				else{
					eventType.setText("Group:");
					subjectComboBox.removeAllItems();
					subjectComboBox.setModel(new DefaultComboBoxModel<String>(
							Data.getGroupNames().toArray(new String[Data.getGroupNames().size()])));
				}
			}
		}
	}
	/**
	 * Save Listener adds the created event to the group or the employee selected, resets the GUI, 
	 * and returns to the Home Panel.
	 * 
	 * @author Crowell, Irene
	 *
	 */
	private class SaveListener implements ActionListener {  
		public void actionPerformed(ActionEvent e) 
		{  
			if(employeeMode){
				Employee emp = Data.getEmployeeByIndex(subjectComboBox.getSelectedIndex());
				emp.addEvent(eventNameTextField.getText(),
						eventDescriptionTextPane.getText(), monthComboBox.getSelectedIndex()+1, 
						dayComboBox.getSelectedIndex()+1, Integer.parseInt((String)
								yearComboBox.getSelectedItem()));
			}
			else{
				Group gr = Data.getGroupByIndex(subjectComboBox.getSelectedIndex());
				gr.addEvent(eventNameTextField.getText(),
						eventDescriptionTextPane.getText(), monthComboBox.getSelectedIndex()+1, 
						dayComboBox.getSelectedIndex()+1, Integer.parseInt((String)
								yearComboBox.getSelectedItem()));
			}
			refresh();

			Driver.openPanel("Home");
		}
	}
	/**
	 * CancelListener resets the GUI and returns to the Home Panel.
	 * 
	 * @author IreneC
	 *
	 */
	private class CancelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) 
		{
			refresh();
			Driver.openPanel("Home");
		}
	}
	/**
	 * Returns the GUI to employee mode, with the first employee and the
	 * default date selected, and the name and description boxes cleared.
	 */
	public void refresh(){
		employeeMode=true;
		eventType.setText("Employee:");
		subjectComboBox.setSelectedIndex(0);
		eventNameTextField.setText("");
		eventDescriptionTextPane.setText("");
		monthComboBox.setSelectedIndex(0);
		dayComboBox.setSelectedIndex(0);
		yearComboBox.setSelectedIndex(0);
		rdbtnEmployeeEvent.setSelected(true);
		subjectComboBox.removeAllItems();
		subjectComboBox.setModel(new DefaultComboBoxModel<String>(
				Data.getEmployeeNames().toArray(new String[Data.getEmployeeNames().size()])));
		subjectComboBox.revalidate();
		subjectComboBox.repaint();
	}
}

