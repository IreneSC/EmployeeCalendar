/**
 * An Event has a name, description, and date associated with it.
 * @author IreneC
 *
 */
public class Event implements Comparable<Event>
{
	private Employee employee;
	private String name;
	private String description;
	private int month;
	private int day;
	private int year;
	private EventPanel panel;

	/**
	 * constructs the event object and sets the value for the fields.
	 * @param name			the name of the event
	 * @param description	a description of the event
	 * @param month			the month (1-12) the event is on
	 * @param day			the day (1-31) the event is on
	 * @param year			the year the event is in
	 */
	public Event(Employee employee,String name, String description, int month, int day, int year)
	{
		this.employee=employee;
		this.name=name;
		this.description = description;
		this.month=month;
		this.day=day;
		this.year=year;
		panel = new EventPanel(this);
	}
	public String getName() {
		return name;
	}
	/**
	 * Gives the month formatted as MM/DDD/YY
	 * @return the String representing the date.
	 **/
	public String getFormattedDate(){
		return "" + month + "/" + day + "/" + year;
	}
	public String getDescription() {
		return description;
	}
	public int getMonth() {
		return month;
	}
	public int getDay() {
		return day;
	}
	public int getYear() {
		return year;
	}
	public Employee getEmployee(){
		return employee;
	}
	public EventPanel getPanel(){
		return panel;
	}
	public int compareTo(Event o){
		int time1 = this.getYear()*365+this.getMonth()*30+this.getDay();
    	int time2 = o.getYear()*365+o.getMonth()*30+o.getDay();
    	if(time1!=time2){
    		return  time1-time2;
    	}
    	return (this.getName()+this.getDescription()).compareTo(o.getName()+o.getDescription());
	}
}
