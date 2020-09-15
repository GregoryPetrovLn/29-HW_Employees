package telran.employes.services.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import telran.employes.dto.DepartmentSalary;
import telran.employes.dto.Employee;
import telran.employes.dto.MinMaxSalaryEmployees;
import telran.employes.dto.ReturnCodes;
import telran.employes.services.interfaces.EmployeeService;

public class EmployeeServiceMapsImpl implements EmployeeService {

	HashMap<Long, Employee> employees = new HashMap<>();

	TreeMap<Integer, List<Employee>> employeesSalary = new TreeMap<>(); // key - salary value, value - list of Employes
																		// receiving the
	// salary

	TreeMap<Integer, List<Employee>> employeesAge = new TreeMap<>(); // key - birth year, value - list of Employes born
																		// at the year
	HashMap<String, List<Employee>> employeesDepartment = new HashMap<>(); // key - department, value - list of
																			// Employees working at the department

	@SuppressWarnings("rawtypes")
	public void printEmployees() {
		for (Map.Entry entry : employeesDepartment.entrySet()) {
			System.out.println(entry.getValue());
		}

	}

	public void clearAll() {
		employees.clear();
		employeesSalary.clear();
		employeesAge.clear();
		employeesDepartment.clear();
	}

	public int emplMapSize(String param) {
		switch (param) {
		case "employees":
			return employees.size();

		case "salary":
			return employeesSalary.size();

		case "age":
			return employeesAge.size();

		case "department":
			return employeesDepartment.size();

		default:
			return -1;
		}

	}

	@Override
	public ReturnCodes addEmployee(Employee empl) {
		Employee res = employees.putIfAbsent(empl.getId(), empl);

		if (res != null)
			return ReturnCodes.EMPLOYEE_ALREADY_EXISTS;

		addEmployeeSalary(empl);
		addEmployeeAge(empl);
		addEmployeeDepartment(empl);

		return ReturnCodes.OK;
	}

	private void addEmployeeDepartment(Employee empl) {
		String department = empl.getDepartment();
		employeesDepartment.computeIfAbsent(department, n -> new ArrayList<>()).add(empl);
	}

	private void addEmployeeSalary(Employee empl) {
		int salary = empl.getSalary();
		employeesSalary.computeIfAbsent(salary, n -> new ArrayList<>()).add(empl);
	}

	private void addEmployeeAge(Employee empl) {
		int birthYear = empl.getBirthDate().getYear();
		employeesAge.computeIfAbsent(birthYear, n -> new ArrayList<>()).add(empl);
	}

	@Override
	public ReturnCodes removeEmployee(long id) {
		Employee empl = employees.remove(id);
		if (empl == null) {
			return ReturnCodes.EMPLOYEE_NOT_FOUND;
		}

		removeEmployeeAge(empl);
		removeEmployeeSalary(empl);
		removeEmployeeDepartment(empl);

		return ReturnCodes.OK;
	}

	private void removeEmployeeDepartment(Employee empl) {
		String department = empl.getDepartment();

		List<Employee> employeesList = employeesDepartment.get(department);

		employeesList.remove(empl);
		if (employeesList.isEmpty()) {
			employeesDepartment.remove(department);
		}

	}

	private void removeEmployeeSalary(Employee empl) {
		int salary = empl.getSalary();
		List<Employee> employeesList = employeesSalary.get(salary);
		employeesList.remove(empl);

		if (employeesList.isEmpty()) {
			employeesSalary.remove(salary);
		}
	}

	private void removeEmployeeAge(Employee empl) {
		int birthYear = empl.getBirthDate().getYear();
		List<Employee> employeesList = employeesAge.get(birthYear);
		employeesList.remove(empl);

		if (employeesList.isEmpty()) {
			employeesAge.remove(birthYear);
		}
	}

	@Override
	public Employee updateEmployee(long id, Employee newEmployee) {
		removeEmployee(id);
		addEmployee(newEmployee);

		return newEmployee;
	}

	@Override
	public Iterable<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		NavigableMap<Integer, List<Employee>> employeesSubmap = employeesAge.subMap(getBirthYear(ageTo), true,
				getBirthYear(ageFrom), true);

		return toCollectionEmployes(employeesSubmap.values());
	}

	private Iterable<Employee> toCollectionEmployes(Collection<List<Employee>> values) {
		List<Employee> result = new ArrayList<>();
		values.forEach(result::addAll);

		return result;
	}

	private Integer getBirthYear(int ageFrom) {
		return LocalDate.now().minusYears(ageFrom).getYear();
	}

	@Override
	public Iterable<Employee> getEmployeesByDepartment(String department) {
		return employeesDepartment.getOrDefault(department, new LinkedList<>());
	}

	@Override
	public Iterable<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		NavigableMap<Integer, List<Employee>> employeesSubMap = employeesSalary.subMap(salaryFrom, true, salaryTo,
				true);

		return toCollectionEmployes(employeesSubMap.values());
	}

	@Override
	public Employee getEmployee(long id) {
		return employees.get(id);
	}

	@Override
	public DepartmentSalary[] getDepartmentAvgSalaryDistribution() {
		Map<String, Double> avgSalaryByDepartments = employees
				.entrySet()
				.stream().map(x -> x.getValue())
				.collect(Collectors.groupingBy(Employee::getDepartment,
						Collectors.averagingDouble(Employee::getSalary)));
		
	
		
		List<DepartmentSalary> result =  avgSalaryByDepartments
				.entrySet()
				.stream()
				.map(e -> new DepartmentSalary(e.getKey(), e.getValue()))
				.sorted((f1, f2) -> Double.compare(f2.getAvgSalary(), f1.getAvgSalary()))
				.collect(Collectors.toList());

		return result.toArray(new DepartmentSalary[0]);
	}

	@Override
	public MinMaxSalaryEmployees[] getEmployeesBySalariesInterval(int interval) {
		
		//completely incomprehensible method 
		
		
		
		return null;
	}

}
