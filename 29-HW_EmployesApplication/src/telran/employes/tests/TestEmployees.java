package telran.employes.tests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.*;

import telran.employes.dto.*;
import telran.employes.services.impl.EmployeeServiceMapsImpl;
import telran.employes.services.interfaces.EmployeeService;

class TestEmployees {

	private static final String DEPARTMENT1 = "company1";
	private static final String DEPARTMENT2 = "company2";
	private static final String DEPARTMENT3 = "company3";


	private static int AGE1 = 40;
	private static int AGE2 = 45;

	private static int AGE3 = 50;
	private static int AGE4 = 55;

	private static int AGE5 = 60;
	private static int AGE6 = 65;

	private static int AGE7 = 20;
	private static int AGE8 = 25;

	private static final long ID1 = 1;
	private static final long ID2 = 2;

	private static final long ID3 = 3;
	private static final long ID4 = 4;

	private static final long ID5 = 5;
	private static final long ID6 = 6;

	private static final long ID7 = 7;
	private static final long ID8 = 8;

	private static final int SALARY1 = 1000;
	private static final int SALARY2 = 2000;

	private static final int SALARY3 = 3000;
	private static final int SALARY4 = 4000;

	private static final int SALARY5 = 5000;
	private static final int SALARY6 = 6000;

	private static final int SALARY7 = 7000;
	private static final int SALARY8 = 8000;

	private static final LocalDate BIRTH_DATE1 = LocalDate.ofYearDay(LocalDate.now().getYear() - AGE1, 1);
	private static final LocalDate BIRTH_DATE2 = LocalDate.ofYearDay(LocalDate.now().getYear() - AGE2, 1);

	private static final LocalDate BIRTH_DATE3 = LocalDate.ofYearDay(LocalDate.now().getYear() - AGE3, 1);
	private static final LocalDate BIRTH_DATE4 = LocalDate.ofYearDay(LocalDate.now().getYear() - AGE4, 1);

	private static final LocalDate BIRTH_DATE5 = LocalDate.ofYearDay(LocalDate.now().getYear() - AGE5, 1);
	private static final LocalDate BIRTH_DATE6 = LocalDate.ofYearDay(LocalDate.now().getYear() - AGE6, 1);

	private static final LocalDate BIRTH_DATE7 = LocalDate.ofYearDay(LocalDate.now().getYear() - AGE7, 1);
	private static final LocalDate BIRTH_DATE8 = LocalDate.ofYearDay(LocalDate.now().getYear() - AGE8, 1);

	Employee empl1 = new Employee(ID1, "name", BIRTH_DATE1, DEPARTMENT1, SALARY1);
	Employee empl2 = new Employee(ID2, "name", BIRTH_DATE2, DEPARTMENT1, SALARY2);
	Employee empl3 = new Employee(ID3, "name", BIRTH_DATE3, DEPARTMENT1, SALARY3);
	
	Employee empl4 = new Employee(ID4, "name", BIRTH_DATE4, DEPARTMENT2, SALARY4);
	Employee empl5 = new Employee(ID5, "name", BIRTH_DATE5, DEPARTMENT2, SALARY5);
	Employee empl6 = new Employee(ID6, "name", BIRTH_DATE6, DEPARTMENT2, SALARY6);
	
	Employee empl7 = new Employee(ID7, "name", BIRTH_DATE7, DEPARTMENT3, SALARY7);
	Employee empl8 = new Employee(ID8, "name", BIRTH_DATE8, DEPARTMENT3, SALARY8);

	Employee newEmployee = new Employee(123777, "name", BIRTH_DATE4, DEPARTMENT2, SALARY4);

	Employee employees[] = { empl1, empl2, empl3, empl4, empl5, empl6, empl7, empl8 };
	EmployeeService employeesService;

	@BeforeEach
	void setUp() throws Exception {
		employeesService = new EmployeeServiceMapsImpl();
		for (Employee empl : employees) {
			employeesService.addEmployee(empl);
		}
	}

	@Test
	void testAddEmployee() {
		assertEquals(ReturnCodes.EMPLOYEE_ALREADY_EXISTS, employeesService.addEmployee(empl1));
		assertEquals(ReturnCodes.OK, employeesService.addEmployee(newEmployee));
	}

	@Test
	void testRemoveEmployee() {
		assertEquals(ReturnCodes.EMPLOYEE_NOT_FOUND, employeesService.removeEmployee(77777));
		Employee expected[] = { empl2, empl3, empl4, empl5, empl6, empl7, empl8 };
		assertEquals(ReturnCodes.OK, employeesService.removeEmployee(ID1));
		testEmployees(expected, employeesService.getEmployeesBySalary(0, Integer.MAX_VALUE));

	}

	private void testEmployees(Employee[] expected, Iterable<Employee> employeesIter) {
		Employee[] actual = new Employee[expected.length];
		int ind = 0;
		for (Employee empl : employeesIter) {
			actual[ind++] = empl;
		}
		Arrays.sort(actual, (e1, e2) -> Long.compare(e1.getId(), e2.getId()));
		assertArrayEquals(expected, actual);

	}

	@Test
	void testGetEmployee() {
		assertEquals(empl1, employeesService.getEmployee(ID1));
		assertNull(employeesService.getEmployee(777777));
	}

	@Test
	void testGetEmployeesCompany() {
		Employee expected[] = { empl1, empl2,empl3 };
		Iterable<Employee> employeesEmpty = employeesService.getEmployeesByDepartment("company");
		assertFalse(employeesEmpty.iterator().hasNext());
		Iterable<Employee> employeesCompany1 = employeesService.getEmployeesByDepartment(DEPARTMENT1);
		testEmployees(expected, employeesCompany1);

	}

	@Test
	void testGetEmployeesAges() {
		Employee expected1[] = { empl1, empl2 };
		Employee expected2[] = { empl3, empl4 };
		Iterable<Employee> employeesEmpty = employeesService.getEmployeesByAge(100, 120);
		assertFalse(employeesEmpty.iterator().hasNext());
		testEmployees(expected1, employeesService.getEmployeesByAge(AGE1, AGE2));
		testEmployees(expected2, employeesService.getEmployeesByAge(AGE3, AGE4));

	}

	@Test
	void testGetEmployeesSalary() {
		Employee expected[] = { empl1, empl2 };
		Iterable<Employee> employeesEmpty = employeesService.getEmployeesBySalary(10000, 25000);
		assertFalse(employeesEmpty.iterator().hasNext());
		Iterable<Employee> employees1000_2500 = employeesService.getEmployeesBySalary(1000, 2500);
		testEmployees(expected, employees1000_2500);
	}

	@Test
	void testUpdateCompany() {
		assertEquals(empl1,
				employeesService.updateEmployee(ID1, new Employee(ID1, "name", BIRTH_DATE1, "company", SALARY1)));
		Employee empl = employeesService.getEmployee(ID1);

		assertEquals("company", empl.getDepartment());
	}

	@Test
	void testUpdateSalary() {
		int newSalary = 100000;
		assertEquals(empl1,
				employeesService.updateEmployee(ID1, new Employee(ID1, "name", BIRTH_DATE1, "company", newSalary)));
		Employee empl = employeesService.getEmployee(ID1);
		assertEquals(newSalary, empl.getSalary());
	}

	@Test
	void testGetDepartmentAvgSalaryDistribution() {
		DepartmentSalary avgSalary1 = new DepartmentSalary(DEPARTMENT1, 2000);
		DepartmentSalary avgSalary2 = new DepartmentSalary(DEPARTMENT2, 5000);
		DepartmentSalary avgSalary3 = new DepartmentSalary(DEPARTMENT3, 7500);
		DepartmentSalary excpectedArray[] = {avgSalary3,  avgSalary2, avgSalary1 };
		
		assertArrayEquals(excpectedArray, employeesService.getDepartmentAvgSalaryDistribution());
	}
	
	

	@Test
	void testGetEmployeesBySalariesInterval() {
		MinMaxSalaryEmployees[] expected = {
				new MinMaxSalaryEmployees(1000, 2000, Arrays.asList(empl1)),
				new MinMaxSalaryEmployees(2000, 3000, Arrays.asList(empl2)),
				new MinMaxSalaryEmployees(3000, 4000, Arrays.asList(empl3)),
				new MinMaxSalaryEmployees(4000, 5000, Arrays.asList(empl4))
		};
		
		MinMaxSalaryEmployees[] actual = employeesService.getEmployeesBySalariesInterval(1000);
		assertArrayEquals(expected, actual);

	}

}
