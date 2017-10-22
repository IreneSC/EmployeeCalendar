import java.util.Set;
import java.util.TreeSet;

/**
 * An Employee has a name and a set of events associated with it that can be
 * displayed.
 * 
 * @author Crowell, Irene
 *
 */
public class Employee {
	private String name;
	private Set<Event> events = new TreeSet<Event>();

	public Employee(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addEvent(String name, String description, int month, int day, int year) {
		this.events.add(new Event(this,name,description,month,day,year));
	}

	public void deleteEvent(Event event) {
		events.remove(event);
	}
}
