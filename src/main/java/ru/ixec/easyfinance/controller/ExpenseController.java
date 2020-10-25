package ru.ixec.easyfinance.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.ixec.easyfinance.dto.ExpenseWrapper;
import ru.ixec.easyfinance.entity.Expense;
import ru.ixec.easyfinance.service.ExpenseService;

@RequestMapping("/client/{client.name}/expense")
@RestController
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @CrossOrigin("http://localhost:3000")
    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Expense> getExpenses() {
        return expenseService.get();
    }


    @CrossOrigin("http://localhost:3000")
    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Expense get(@PathVariable Long id) {
        return expenseService.getById(id);
    }


    @CrossOrigin(value = "http://localhost:3000")
    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Expense save(@RequestBody Expense expense) {
        return expenseService.save(expense);
    }


    @CrossOrigin("http://localhost:3000")
    @ResponseBody
    @GetMapping(value = "/product/{product}/category")
    public String checkProduct(@PathVariable String product) {
        return "expenseService.checkProduct(product);";
    }


    @CrossOrigin(value = "http://localhost:3000")
    @ResponseBody
    @PostMapping(value = "/confirm", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void confirmProduct(@RequestBody ExpenseWrapper expenses){
        expenseService.updateConfirmed(expenses.getExpenses());
    }
}
