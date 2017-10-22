import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 

@SuppressWarnings("serial")
/**
 * HomePanel Displays a GUI with several options for the user to pick from.
 * All are redirected to Driver for the correct Panel to be displayed, except
 * "Add Employee", which pops up a dialog box for the name to be entered.
 * @author Crowell, Irene
 *
 */
public class HomePanel extends JPanel  
{  
	/**
	 * Sets up the Panel layout and adds the buttons with ActionListeners
	 */
	public HomePanel() 
	{   
		setLayout(new GridBagLayout()); 
		GridBagConstraints c = new GridBagConstraints();


		JLabel label = new JLabel("Choose An Option"); 
		label.setFont(new Font("Sans", Font.BOLD, 20));

		c.ipady = 0;       
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10,0,10,0);  
		c.gridx = 0;       
		c.gridwidth = 3;   
		c.gridy = 0; 
		add(label, c);   



		JButton addEventButt = new JButton("Add Event");

		c.anchor = GridBagConstraints.CENTER;
		c.ipady = 0;
		c.gridx = 2;
		c.gridwidth = 1;
		c.gridy = 1;
		addEventButt.addActionListener(new SwitchPanelListener("AddEventPanel"));
		add(addEventButt, c);

		JPanel ButtonSubPanel = new JPanel();
		ButtonSubPanel.setLayout(new GridLayout(2,2)); 


		JButton addEmployeeButt = new JButton("Add Employee");
		addEmployeeButt.addActionListener(new AddEmployeeListener());
		ButtonSubPanel.add(addEmployeeButt); 

		JButton viewEmployeeButt = new JButton("View Employee"); 
		viewEmployeeButt.addActionListener(new SwitchPanelListener("ViewEmployeePanel")); 
		ButtonSubPanel.add(viewEmployeeButt); 

		JButton addGroupButt = new JButton("Add Group");
		addGroupButt.addActionListener(new SwitchPanelListener("AddGroupPanel")); 
		ButtonSubPanel.add(addGroupButt); 

		JButton viewGroupButt = new JButton("View Group");
		viewGroupButt.addActionListener(new SwitchPanelListener("ViewGroupPanel")); 
		ButtonSubPanel.add(viewGroupButt); 

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(10,0,0,0);
		c.gridx = 0;
		c.gridwidth = 3;
		c.gridy = 2;
		add(ButtonSubPanel, c);        
	} 
	/**
	 * All the buttons except Add Employee use this Listener. It is constructed
	 * with the panel that should be opened when the button is clicked.
	 * @author Crowell Irene
	 *
	 */
	private class SwitchPanelListener implements ActionListener 
	{  
		/**The name of the panel to open when the button is clicked**/
		String panel;
		/**
		 * Sets the panel that will be opened.
		 * @param p	the panel to open on the button's click
		 */
		public SwitchPanelListener(String p){
			panel=p;
		}
		/**
		 * Opens the panel associated with the button
		 */
		public void actionPerformed(ActionEvent e) 
		{  
			Driver.openPanel(panel);
		} 
	}
	/**
	 * Displays a dialog prompting for the user to input a name for the new employee.
	 * The name is rejected if an employee with the same name exists.
	 * @author Crowell, Irene
	 *
	 */
	private class AddEmployeeListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String s = (String)JOptionPane.showInputDialog(
					Driver.getFrame(),
					"Please enter the Employee's Name",
					"Add Employee",
					JOptionPane.PLAIN_MESSAGE,
					null,
					null,
					"");
			if ((s != null) && (s.length() > 0)) {
				boolean nameTaken=false;
				for(String a: Data.getEmployeeNames()){
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
					Data.addEmployee(s);
				}
				return;
			}
		}
	}
}