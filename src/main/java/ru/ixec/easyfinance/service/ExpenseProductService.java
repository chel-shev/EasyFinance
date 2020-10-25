package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.ExpenseCategory;
import ru.ixec.easyfinance.entity.ExpenseProduct;
import ru.ixec.easyfinance.repositories.ExpenseProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseProductService {

    private final ExpenseProductRepository epr;
    private final ExpenseCategoryService ecs;

    public ExpenseProduct getProduct(String productName) {
        Optional<ExpenseProduct> epo = epr.findByName(productName);
        if (epo.isPresent())
            return epo.get();
        else {
            ExpenseCategory ec = ecs.getCategory(productName);
            return save(new ExpenseProduct(productName, ec));
        }
    }

    public ExpenseProduct save(ExpenseProduct ep) {
        return epr.save(ep);
    }

    public static List<String> prepareName(String productName, boolean size) {
        String[] nameArray = productName.split(" ");
        List<String> resultList = new ArrayList<>();
        for (String name : nameArray) {
            if (!name.matches("([^aA-zZаА-яЯ]*[\\d]*)*")) {
                if (name.matches("[\\d]+[aA-zZаА-яЯ]+[^aA-zZаА-яЯ]*") && !size) continue;
                resultList.add(name);
            }
        }
        return resultList;
    }

    public static String getCleanName(String productName) {
        return String.join(" ", ExpenseProductService.prepareName(productName, true));
    }
}