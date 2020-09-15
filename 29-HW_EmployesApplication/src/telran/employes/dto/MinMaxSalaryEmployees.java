package telran.employes.dto;

import java.util.List;

public class MinMaxSalaryEmployees {
	int minSalary;
	int maxSalary;
	List<Employee> employees;
	
	
	
	
	public MinMaxSalaryEmployees(int minSalary, int maxSalary, List<Employee> employees) {
		super();
		this.minSalary = minSalary;
		this.maxSalary = maxSalary;
		this.employees = employees;
	}
	
	


	public int getMinSalary() {
		return minSalary;
	}




	public int getMaxSalary() {
		return maxSalary;
	}




	public List<Employee> getEmployees() {
		return employees;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((employees == null) ? 0 : employees.hashCode());
		result = prime * result + maxSalary;
		result = prime * result + minSalary;
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MinMaxSalaryEmployees other = (MinMaxSalaryEmployees) obj;
		if (employees == null) {
			if (other.employees != null)
				return false;
		} else if (!employees.equals(other.employees))
			return false;
		if (maxSalary != other.maxSalary)
			return false;
		if (minSalary != other.minSalary)
			return false;
		return true;
	}
	
	

}
