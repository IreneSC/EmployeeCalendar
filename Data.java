import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * Stores all employees and groups, as well as loads and saves them to an excel file
 * on the desktop at the beginning and end of the program respectively.
 * 
 * @author Crowell, Irene
 *
 */
public class Data
{
	private static ArrayList<Employee> employees = new ArrayList<Employee>();
	private static ArrayList<Group> groups = new ArrayList<Group>();
	private static final String FILE_PATH = System.getProperty("user.home") + "/Desktop/EmployeeCalendarSaveFile.xlsx";
	
	public Data(){}
	
	/**
	 * Creates an excel document (.xlsx) with a sheet for each employee and group
	 * containing the data for each of them, and then saves this file (or overwrites
	 * the old one).
	 * @throws IOException
	 */

	public static void saveData() throws IOException{
		Workbook workbook = new XSSFWorkbook();
		for(Employee employee: employees){
			Sheet sheet = workbook.createSheet("Employee - "+employee.getName());
			int rowIndex = 0;
			int cellIndex = 0;
			Row row1 = sheet.createRow(rowIndex++);
			
			row1.createCell(cellIndex++).setCellValue("Employee:");
			row1.createCell(cellIndex++).setCellValue(employee.getName());
			
			cellIndex=0;
			for(Event event: employee.getEvents()){
				Row row2 = sheet.createRow(rowIndex++);
				row2.createCell(cellIndex++).setCellValue(event.getFormattedDate());
				row2.createCell(cellIndex++).setCellValue(event.getName());
				row2.createCell(cellIndex++).setCellValue(event.getDescription());
				cellIndex=0;
			}
		}
		for(Group group: groups){
			Sheet sheet;
			sheet = workbook.createSheet("Group - "+group.getName());
			int rowIndex = 0;
			int cellIndex = 0;
			Row row1 = sheet.createRow(rowIndex++);
			
			row1.createCell(cellIndex++).setCellValue("Group:");
			row1.createCell(cellIndex++).setCellValue(group.getName());
			cellIndex=0;
			for(Employee employee: group.getEmployees()){
				Row row2 = sheet.createRow(rowIndex++);
				row2.createCell(cellIndex).setCellValue(employee.getName());
			}
		}
		try {
            FileOutputStream out = new FileOutputStream(FILE_PATH);
            workbook.write(out);
            out.close();
            workbook.close();

        } catch (FileNotFoundException e) {
        	File f = new File(FILE_PATH);

        	f.getParentFile().mkdirs(); 
        	f.createNewFile();
        	FileOutputStream out = new FileOutputStream(FILE_PATH);
            workbook.write(out);
            out.close();
            workbook.close();
        }
	}
	/**
	 * Locates the previous excel save data if available, and creates the necessary employees and groups
	 * it specifies.
	 */
	public static void loadData(){
		FileInputStream in = null;
        try {
            in = new FileInputStream(FILE_PATH);
            
            Workbook workbook = new XSSFWorkbook(in);

            int numberOfSheets = workbook.getNumberOfSheets();
            
            for(int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                
                if(sheet.getRow(0).getCell(0).getStringCellValue().equals("Employee:")){
                	Employee employee = new Employee(sheet.getRow(0).getCell(1).getStringCellValue());
                	Iterator<Row> rowIterator = sheet.iterator();
                	rowIterator.next();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        String[] date = row.getCell(0).getStringCellValue().split("/");
                        String name = row.getCell(1).getStringCellValue();
                        String desc = row.getCell(2).getStringCellValue();
                        
                        employee.addEvent(name, desc, Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                        		Integer.parseInt(date[2]));
                    }
                    employees.add(employee);
                }
                else if(sheet.getRow(0).getCell(0).getStringCellValue().equals("Group:")){
                	Iterator<Row> rowIterator = sheet.iterator();
                	rowIterator.next();
                	ArrayList<Employee> e = new ArrayList<Employee>();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        Employee emp = new Employee(row.getCell(0).getStringCellValue());
                        e.add(emp);
                    }
                    Group group = new Group(sheet.getRow(0).getCell(1).getStringCellValue(), e);
                    groups.add(group);
                }
                
            }

            in.close();
            workbook.close();

        } catch (IOException e) {
        	//do nothing -- no data exists
        }
	}
	public static ArrayList<Employee> getEmployees()
	{
		return employees;
	}
	public static Employee getEmployeeByIndex(int employeeIndex){
		return employees.get(employeeIndex);
	}
	public static ArrayList<Group> getGroups()
	{
		return groups;
	}
	public static Group getGroupByIndex(int groupIndex){
		return groups.get(groupIndex);
	}
	public static void addEmployee(String name){
		employees.add(new Employee(name));
	}
	public static ArrayList<String> getEmployeeNames(){
		ArrayList<String> names = new ArrayList<String>();
		for(Employee e : employees){
			names.add(e.getName());
		}
		return names;
	}
	public static ArrayList<String> getGroupNames(){
		ArrayList<String> names = new ArrayList<String>();
		for(Group g :  groups){
			names.add(g.getName());
		}
		return names;
	}
	/**
	 * Create a new group
	 * @param name			the name of the group
	 * @param employees		the employees belonging to this group
	 */
	public static void addGroup(String name, ArrayList<Employee> employees) {
		groups.add(new Group(name, employees));
		
	}
	/**
	 * Removes an employee from any groups it is in, and then deletes it.
	 * @param e the employee to remove
	 */
	public static void deleteEmployee(Employee e){
		int numGroups = groups.size();
		Iterator<Group> groupIt = groups.iterator();
		while(groupIt.hasNext()){
			Group g = groupIt.next();
			g.removeEmployee(e);
			if(g.getEmployees().size()<2){
				groupIt.remove();
			}
		}
		if(groups.size()<numGroups){
			JOptionPane.showMessageDialog(Driver.getFrame(),
					"Groups with fewer than 2 members have been deleted",
					"Please Note:",
					JOptionPane.PLAIN_MESSAGE);
		}
		employees.remove(e);
	}
	/**
	 * removes a group
	 * @param g the group to remove
	 */
	public static void deleteGroup(Group g) {
		groups.remove(g);
		
	}

}