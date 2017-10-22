import javax.swing.*;
import java.io.IOException;
/**
 * Displays the different JPanels, and handles switching between different GUI views.
 * 
 * @author Crowell, Irene
 */
public class Driver
{
	/**The displayed window**/
	private static JFrame frame;
	/**Always displayed, contains all possible panels**/
	private static JPanel panel = new JPanel();
	/**the panel to be displayed, while all others will be hidden**/
	private static JPanel currentPanel;
	/**implementation of the HomePanel**/
	private static JPanel home;
	/**implementation of the AddEventPanel**/
	private static JPanel addEvent;
	/**implementation of the AddGroupPanel**/
	private static JPanel addGroup;
	/**implementation of the ViewGroupPanel**/
	private static JPanel viewGroup;
	/**implementation of the ViewEmployeePanel**/
	private static JPanel viewEmployee;
	/**
	 * Loads the save file, and creates all panels that have the necessary
	 * prerequisites fulfilled (some need employees or groups to be available),
	 * as well as saves the date when the window is closed.
	 * <p>
	 * This avoids indexOutOfBounds exceptions, and prevents the panels from
	 * being created before they are useful.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		Data.loadData();
		createDefaultPanels();
		createOptionalPanels();
		currentPanel.setVisible(true);
		setFrameBehavior();
	}
	/**
	 * Hides the panel current being shown and displays the requested one, unless
	 * the requested panel does not meet the prerequisites (some need employees or
	 *  groups to be available)
	 * @param p	The name of the class to be displayed
	 */
	public static void openPanel(String p){
		currentPanel.setVisible(false);
		switch (p) {
		case "AddEventPanel":
			openEventPanel();
			break;
		case "ViewEmployeePanel":
			openEmployeePanel();
			break;
		case "ViewGroupPanel":
			openGroupPanel();
			break;
		case "AddGroupPanel":
			currentPanel = addGroup;
			((AddGroupPanel) addGroup).refresh();
			break;
		case "Home":
			currentPanel = home;
			break;
		default:
			break;
		}
		currentPanel.setVisible(true);
		panel.revalidate();
		panel.repaint();
		currentPanel.revalidate();
		currentPanel.repaint();
	}
	public static JFrame getFrame(){
		return frame;
	}
	
	private static void createDefaultPanels() {
		home = new HomePanel();
		panel.add(home);
		home.setVisible(false);
		currentPanel = home;
		
		addGroup = new AddGroupPanel();
		
		panel.add(addGroup);
		addGroup.setVisible(false);
	}
	private static void createOptionalPanels() {
		if(!Data.getGroups().isEmpty()){
    		viewGroup = new ViewGroupPanel();
    		viewGroup.setVisible(false);
    		panel.add(viewGroup);
    	}
    	if(!Data.getEmployees().isEmpty()){
    		viewEmployee = new ViewEmployeePanel();
			panel.add(viewEmployee);
			viewEmployee.setVisible(false);
    	}
    	if(!Data.getEmployees().isEmpty()){
    		addEvent = new AddEventPanel();
    		panel.add(addEvent);
    		addEvent.setVisible(false);
    	}
	}
	/**
	 * Sets size, title, and saving before closing behavior.
	 */
	private static void setFrameBehavior() {
		frame = new JFrame("Employee Calendars");
		frame.setSize(600, 400);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	try {
					Data.saveData();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	System.exit(0);
		    }});
		frame.setContentPane(panel);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	private static void openGroupPanel() {
		if(!Data.getGroups().isEmpty()){
			if(viewGroup==null){
				viewGroup = new ViewGroupPanel();
				panel.add(viewGroup);
			}
			currentPanel = viewGroup;
			((ViewGroupPanel) viewGroup).refresh();
		}
		else{
			JOptionPane.showMessageDialog(Driver.getFrame(),
				"Error: You have not created any groups yet.\nYou are being returned to the home page.",
				"Error.",
				JOptionPane.PLAIN_MESSAGE);
		}
	}
	private static void openEmployeePanel() {
		if(!Data.getEmployees().isEmpty()){
			if(viewEmployee==null){
				viewEmployee = new ViewEmployeePanel();
				panel.add(viewEmployee);
			}
			currentPanel = viewEmployee;
			ViewEmployeePanel.refresh();
		}
		else{
			JOptionPane.showMessageDialog(Driver.getFrame(),
				"Error: You have not created any employees yet.\nYou are being returned to the home page.",
				"Error.",
				JOptionPane.PLAIN_MESSAGE);
		}
	}
	private static void openEventPanel() {
		if(!Data.getEmployees().isEmpty()){
			if(addEvent==null){
				addEvent = new AddEventPanel();
				panel.add(addEvent);
			}
			currentPanel = addEvent;
			currentPanel.setVisible(true);
			((AddEventPanel) addEvent).refresh();
		}
		else{
			JOptionPane.showMessageDialog(Driver.getFrame(),
				"Error: You have not created any employees yet.\nYou are being returned to the home page.",
				"Error.",
				JOptionPane.PLAIN_MESSAGE);
		}
	}
}