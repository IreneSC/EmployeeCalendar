import javax.swing.*; 
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import net.miginfocom.swing.MigLayout; 

@SuppressWarnings("serial")
/**
 * AddGrouptPanel is a JPanel that displays a GUI for the user to create
 * an a group of employees with a given name.
 * 
 * @author      Crowell, Irene	
 */
public class AddGroupPanel extends JPanel  
{
	/**Array of each drop down menu of all employees so they can be added to the group**/
	private ArrayList<JComboBox<String>> memberSelection = new ArrayList<JComboBox<String>>();
	/**Includes all the employee names, for use with the memberSelection ComboBox**/
	private ArrayList<String> employeeNames;
	/**Allows the user to input a name for the group**/
	private JTextField textFieldGroupName;
	/**A subpanel for all of the employee ComboBoxes**/
	private JPanel panel = new JPanel();
	/**Iterator of where to add the next ComboBox on panel**/
	private int panelRow=0;
	/**Saves the group, resets,  and exits**/
	JButton btnCreateGroup;
	/**Resets and exits the panel**/
	JButton btnCancel;
	/**Adds another ComboBox for an additional employee to be selected**/
	JButton btnAddMember;
	/**
	 * Create the GUI for the Panel, defaulting to employee mode with the first employee selected.
	 * <p>
	 * Initializes the ComboBoxes with default values (All employees, 12 months, 31 days, 5 years
	 * starting at current year. Adds all components, including local variable labels, into the
	 * GUI. Adds actionListeners to the buttons.
	 */
    public AddGroupPanel() 
    {
    	
    	textFieldGroupName = new JTextField();
    	textFieldGroupName.setColumns(10);
    	JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
    	btnCreateGroup = new JButton("Create Group");
    	btnCreateGroup.addActionListener(new SaveListener());
    	
    	btnCancel = new JButton("Cancel");
    	btnCancel.addActionListener(new CancelListener());
    	
    	JLabel label = new JLabel("Group Name:");
    	
    	JLabel label_1 = new JLabel("Members:");
    	
    	btnAddMember = new JButton("Add Employee");
    	btnAddMember.addActionListener(new AddMemberListener());
    	
    	panel.setLayout(new MigLayout("", "[200px][]", "[20px][]"));
    	
    	employeeNames = new ArrayList<String>();
    	employeeNames.add("");
    	for(Object n: Data.getEmployeeNames().toArray()){
    		employeeNames.add((String)n);
    	}
    	
    	JComboBox<String> comboBox = new JComboBox<String>((String[]) employeeNames.
    			toArray(new String[employeeNames.size()]));
    	panel.add(comboBox,"cell 0 " + panelRow +",growx,aligny top");
    	panelRow++;
    	memberSelection.add(comboBox);
    	JComboBox<String> comboBox_1 = new JComboBox<String>((String[]) employeeNames.
    			toArray(new String[employeeNames.size()]));
    	panel.add(comboBox_1,"cell 0 " + panelRow +",growx,aligny top");
    	panelRow++;
    	memberSelection.add(comboBox_1);
    	
    	GroupLayout groupLayout = new GroupLayout(this);
    	groupLayout.setHorizontalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(groupLayout.createSequentialGroup()
    				.addGap(21)
    				.addComponent(btnCancel)
    				.addPreferredGap(ComponentPlacement.RELATED, 379, Short.MAX_VALUE)
    				.addComponent(btnCreateGroup)
    				.addGap(18))
    			.addGroup(groupLayout.createSequentialGroup()
    				.addGap(154)
    				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
    					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
    					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
    						.addGroup(groupLayout.createSequentialGroup()
    							.addComponent(label, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
    							.addGap(33)
    							.addComponent(textFieldGroupName))
    						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
    							.addComponent(btnAddMember, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
    							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 259, GroupLayout.PREFERRED_SIZE))))
    				.addContainerGap(167, Short.MAX_VALUE))
    	);
    	groupLayout.setVerticalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
    			.addGroup(groupLayout.createSequentialGroup()
    				.addContainerGap()
    				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
    					.addGroup(groupLayout.createSequentialGroup()
    						.addGap(3)
    						.addComponent(label)
    						.addGap(19)
    						.addComponent(label_1))
    					.addComponent(textFieldGroupName, GroupLayout.PREFERRED_SIZE, 
    							GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
    				.addPreferredGap(ComponentPlacement.RELATED)
    				.addComponent(btnAddMember)
    				.addGap(67))
    			.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
    				.addContainerGap(319, Short.MAX_VALUE)
    				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
    					.addComponent(btnCancel)
    					.addComponent(btnCreateGroup))
    				.addContainerGap())
    	);
    	setLayout(groupLayout);

        
    } 
    /**
     * Adds another ComboBox to the subpanel, for another employee to be selected
     * for the group.
     * @author Crowell, Irene
     *
     */
    private class AddMemberListener implements ActionListener{
		public void actionPerformed(ActionEvent e) 
		{  

			JComboBox<String> comboBox = new JComboBox<String>(
					employeeNames.toArray(new String[employeeNames.size()]));
	    	panel.add(comboBox,"cell 0 " + panelRow +",growx,aligny top");
	    	panelRow++;
	    	memberSelection.add(comboBox);
	    	panel.revalidate();
	    	panel.repaint();
		}
    }
    /**
     * Creates the group with the employee's selections (minus blank and
     * duplicate employees), resets the GUI, and returns to the Home Panel.
     * If the user has not selected at least 2 members and set a unique 
     * group name, it prompts them to do so and does nothing.
     * 
     * @author Crowell, Irene
     *
     */
    private class SaveListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		ArrayList<Employee> employees = new ArrayList<Employee>();
    		ArrayList<Integer> usedEmployees = new ArrayList<Integer>();
    		for(JComboBox<String> c : memberSelection){
    			int index =c.getSelectedIndex()-1;
    			if(index>=0 && (!usedEmployees.contains(index))){
    				employees.add(Data.getEmployees().get(index));
    				usedEmployees.add(index);
    			}
    		}
    		boolean nameTaken=false;
    		for(String s: Data.getGroupNames()){
    			if(s.equals(textFieldGroupName.getText())){
    				nameTaken=true;
    			}
    		}
    		if(nameTaken){
    			JOptionPane.showMessageDialog(Driver.getFrame(),
    					"Error: There is already a group with this name, please enter a new name.",
    					"Error.",
    					JOptionPane.PLAIN_MESSAGE);
    		}
    		else if(textFieldGroupName.getText().isEmpty()){
    			JOptionPane.showMessageDialog(Driver.getFrame(),
    					"Error: Please enter a name for this Group.",
    					"Error.",
    					JOptionPane.PLAIN_MESSAGE);
    		}
    		else if(employees.size()<2){
    			JOptionPane.showMessageDialog(Driver.getFrame(),
    					"Error: Please add at least 2 members to this group",
    					"Error.",
    					JOptionPane.PLAIN_MESSAGE);
    		}
    		else{
    			Data.addGroup(textFieldGroupName.getText(), employees);
    			refresh();
    	    	Driver.openPanel("Home");
    		}
    	}
    }
    /**
     * Resets the GUI and returns to the Home Panel
     * 
     * @author Crowell, Irene
     *
     */
    private class CancelListener implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		refresh();
        	Driver.openPanel("Home");
    	}
    }
    /**
     * Resets the GUI by clearing all input and leaving only two employee selection
     * ComboBoxes.
     */
	public void refresh() {
		textFieldGroupName.setText("");
		
		for(JComboBox<String> c: memberSelection){
			panel.remove(c);
		}
		memberSelection.clear();
		employeeNames = new ArrayList<String>();
    	employeeNames.add("");
    	for(Object n: Data.getEmployeeNames().toArray()){
    		employeeNames.add((String)n);
    	}
		panelRow=0;
		JComboBox<String> comboBox = new JComboBox<String>((String[]) employeeNames.
				toArray(new String[employeeNames.size()]));
    	panel.add(comboBox,"cell 0 " + panelRow +",growx,aligny top");
    	panelRow++;
    	memberSelection.add(comboBox);
    	JComboBox<String> comboBox_1 = new JComboBox<String>((String[]) employeeNames.
    			toArray(new String[employeeNames.size()]));
    	panel.add(comboBox_1,"cell 0 " + panelRow +",growx,aligny top");
    	panelRow++;
    	memberSelection.add(comboBox_1);
    	
    	panel.revalidate();
    	panel.repaint();
	}
}