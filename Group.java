import java.util.ArrayList;

/**
 * A Group combines multiple employees
 * @author IreneC
 *
 */
public class Group
{
	/**The name of the group**/
	private String name;
	/**The employees in the group**/
	private ArrayList<Employee> members = new ArrayList<Employee>();
	
	/**
	 * Creates the group
	 * @param name		the name of the group
	 * @param members	the employees in the group
	 */
	public Group(String name, ArrayList<Employee> members){
		this.name = name;
		this.members=members;
	}
	/**
	 * Gets the name of the group
	 * @return the group's name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Get the group members
	 * @return the employees in the group
	 */
	public ArrayList<Employee> getEmployees() {
		return members;
	}
	/**
	 * Changes the name of the group
	 * @param name the name to change it to
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds an event to all the employees in the group
	 * @param event	the event to add
	 */
	public void addEvent(String name, String description, int month, int day, int year) {
		for(Employee x : members){
			x.addEvent(name,description,month,day,year);
		}
	}
	/**
	 * Removes an employee in the group
	 * @param index	the employee's position in the ArrayList
	 */
	public void removeEmployee(int index){
		members.remove(index);
	}
	public void removeEmployee(Employee e) {
		members.remove(e);
	}

}
