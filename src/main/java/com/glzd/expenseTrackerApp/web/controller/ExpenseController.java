package com.glzd.expenseTrackerApp.web.controller;

import com.glzd.expenseTrackerApp.business.model.Expense;
import com.glzd.expenseTrackerApp.business.model.ExpenseType;
import com.glzd.expenseTrackerApp.business.services.serviceExpenses;
import com.glzd.expenseTrackerApp.business.services.ExpenseTypeService;
import com.glzd.expenseTrackerApp.business.exceptions.ExpenseException;
import com.glzd.expenseTrackerApp.web.helpers.Helpers;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Month;

@Controller
@RequestMapping
public class ExpenseController {

    

    private final serviceExpenses serviceExpenses;
    private final ExpenseTypeService expenseTypeService;
    private static final int PAGE_SIZE = 8; 

    public ExpenseController(serviceExpenses serviceExpenses, ExpenseTypeService expenseTypeService) {
        this.serviceExpenses = serviceExpenses;
        this.expenseTypeService = expenseTypeService;
    }


    
    @ModelAttribute("totalAmount")
    public BigDecimal getTotalAmount(){
        Iterable<Expense> expenses = serviceExpenses.findAll();
        return serviceExpenses.getTotalAmount(expenses);
    }


    
    @ModelAttribute("expenses")
    public Page<Expense> getExpenses(@PageableDefault(size = PAGE_SIZE) Pageable page){
        return serviceExpenses.findAll(page);
    }

    @GetMapping("/expenses")
    public String showExpenses(){
        return "/expenses";
    }

    @ModelAttribute("expenseTypes")
    public Iterable<ExpenseType> getExpenseTypes() {
        return expenseTypeService.findAll();
    }

    @ModelAttribute
    public Expense getExpense(){
        return new Expense();
    }

    @ModelAttribute
    public ExpenseType getExpenseType(){
        return new ExpenseType();
    }

    @GetMapping("/newExpenseType")
    public String showExpenseTypes(){
        return "/newExpenseType";
    }


  
    @PostMapping("/newExpenseType")
    public String addExpenseType(@Valid ExpenseType expenseType, Errors errors, Model model){
        if (errors.hasErrors()) {
            return "newExpenseType";
        }
        try {
            expenseTypeService.save(expenseType);
        } catch (ExpenseException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "newExpenseType";
        }
        return "redirect:/newExpenseType";
    }


    @PostMapping(value = "expenses/delete/individual/{id}")
    public String deleteExpense(@PathVariable("id") Long id){
        serviceExpenses.deleteById(id);
        return "redirect:/expenses";
    }

  
    @PostMapping(value = "delete/{id}")
    public String deleteExpenseType(@PathVariable("id") Long id){
        expenseTypeService.deleteById(id);
        return "redirect:/newExpenseType";
    }


    @PostMapping("/AddExpense")
    public String addExpense(@Valid Expense expense, Errors errors){
        if (errors.hasErrors()) {
            return "expenses"; 
        }
        serviceExpenses.save(expense);

        return "redirect:/expenses";
    }



    @GetMapping("/update/{id}")
    public String showUpdateExpenseForm(@PathVariable String id, Model model) {
        Long longId = Long.parseLong(id);

        Expense expense = serviceExpenses.findById(longId);
        model.addAttribute("expense", expense);

        return "updateExpense";


        
    }

    @PostMapping("/update")
    public String updateExpense(@Valid Expense expense, Errors errors) {
        if (errors.hasErrors()) {
            return "updateExpense";
        }
        serviceExpenses.save(expense); 
        return "redirect:/expenses";
    }


    @GetMapping("/expenses/filter")
    public String showFilteredExpenses(@RequestParam(name = "year", required = false) Integer year,
                                       @RequestParam(name = "month", required = false) Month month,
                                       @RequestParam(name = "expenseTypeFilter", required = false) String expenseType,
                                       Model model, @PageableDefault(size = PAGE_SIZE) Pageable page) {

        Page<Expense> expenses;
        String monthToDisplay = null;
        String yearToDisplay = null;

        if (year != null && month != null && expenseType != null && !expenseType.isEmpty()) {
            expenses = serviceExpenses.getExpensesByYearMonthAndType(year, month, expenseType, page);
            monthToDisplay = Helpers.toNewCaseVal(month.toString());
            yearToDisplay = year.toString();
        }
        else if (year != null && month != null) {
            expenses = serviceExpenses.getExpensesByYearMonth(year, month, page);
            monthToDisplay = Helpers.toNewCaseVal(month.toString());
            yearToDisplay = year.toString();
        }
        else if (expenseType != null && !expenseType.isEmpty()) {
            expenses = serviceExpenses.getExpensesByType(expenseType, page);
        }
        else {
            expenses = serviceExpenses.findAll(page);
        }

        model.addAttribute("expenses", expenses);
        model.addAttribute("month", monthToDisplay);
        model.addAttribute("year", yearToDisplay);
        model.addAttribute("expenseType", expenseType);

        return "expenses";
    }


    @GetMapping("/downloadExpenses")
    public ResponseEntity<Resource> downloadExpenses() {
        Iterable<Expense> expenses = serviceExpenses.findAll();

        String csvData = serviceExpenses.convertToCSV(expenses);

        ByteArrayResource resource = new ByteArrayResource(csvData.getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=expenses.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource);
    }



}
