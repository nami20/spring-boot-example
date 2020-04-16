package net.gradle.com.springbootexpample.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.Optional;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.aerospike.client.AerospikeClient;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.Value;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.gradle.com.springbootexpample.model.Employee;
import net.gradle.com.springbootexpample.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    AerospikeClient asdClient = new AerospikeClient("172.28.128.3", 3000);
    WritePolicy policy = new WritePolicy();

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @PostMapping("/employee")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        Employee employee1 = employeeRepository.save(employee);
        setASDClientEmployee(employee1);
        return employee1;
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployeeById(@PathVariable(value = "id") Long employeeId) {
        Key key = new Key("test", "employee", employeeId);
        Record record = asdClient.get(policy, key);
        Employee employee1 = new Employee();
        System.out.println(asdClient.get(policy, key));
        if(record!= null && record.getInt("id")!= 0) {
            employee1.setId(record.getInt("id"));
            employee1.setEmailId(record.getString("lastName"));
            employee1.setLastName(record.getString("firstName"));
            employee1.setFirstName(record.getString("emailId"));
            return employee1;
        } else {
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            Employee employeeDetails = employee.get();
            setASDClientEmployee(employeeDetails);
            return employeeDetails;
        }
    }

    private void setASDClientEmployee(Employee employee) {
        Key key = new Key("test", "employee", employee.getId());
        Bin Id = new Bin("id", employee.getId());
        Bin lastName = new Bin("lastName", employee.getLastName());
        Bin firstName = new Bin("firstName", employee.getFirstName());
        Bin emailId = new Bin("emailId", employee.getEmailId());
        asdClient.put(policy, key, Id, lastName, firstName, emailId);
        System.out.println(asdClient.get(policy, key));
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                   @Valid @RequestBody Employee employeeDetails){
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Employee employee1 = employee.get();
        employee1.setEmailId(employeeDetails.getEmailId());
        employee1.setLastName(employeeDetails.getLastName());
        employee1.setFirstName(employeeDetails.getFirstName());
        setASDClientEmployee(employee1);
        final Employee updatedEmployee = employeeRepository.save(employee1);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employee/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) {
        Key key = new Key("test", "employee", employeeId);
        Record record = asdClient.get(policy, key);
        if(record!= null && record.getInt("id") != 0) {
            asdClient.delete(policy, key);
        }
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Employee employee1 = employee.get();
        employeeRepository.delete(employee1);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}