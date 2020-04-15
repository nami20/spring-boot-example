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
    Key key = new Key("test", "myset", "employee");

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        Record record = asdClient.get(policy, key);
        Employee employee1 = new Employee();
        if(!record.getString("lastName").isEmpty()) {
            employee1.setId(record.getInt("id"));
            employee1.setEmailId(record.getString("lastName"));
            employee1.setLastName(record.getString("firstName"));
            employee1.setFirstName(record.getString("emailId"));
            list.add(employee1);
            return list;
        } else {
            return employeeRepository.findAll();
        }
    }

    @PostMapping("/employee")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        Record record = asdClient.get(policy, key);
        System.out.println(record);
        Bin Id = new Bin("id", employee.getId());
        Bin lastName = new Bin("lastName", employee.getLastName());
        Bin firstName = new Bin("firstName", employee.getFirstName());
        Bin emailId = new Bin("emailId", employee.getEmailId());
        if(!record.getString("lastName").isEmpty()) {
            asdClient.put(policy, key, lastName, firstName, emailId);
            System.out.println('q');
            System.out.println(asdClient.get(policy, key));
        } else  {
            System.out.println('c');
            asdClient.put(policy, key, lastName, firstName, emailId);
            System.out.println(asdClient.get(policy, key));
        }
        return employeeRepository.save(employee);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                   @Valid @RequestBody Employee employeeDetails){
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Employee employee1 = employee.get();
//        Employee employee = employeeRepository.findById(employeeId);
        employee1.setEmailId(employeeDetails.getEmailId());
        employee1.setLastName(employeeDetails.getLastName());
        employee1.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepository.save(employee1);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employee/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        Employee employee1 = employee.get();
        employeeRepository.delete(employee1);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}