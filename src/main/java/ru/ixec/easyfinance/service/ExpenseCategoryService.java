package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.ExpenseCategoryEntity;
import ru.ixec.easyfinance.entity.StatisticCategoryEntity;
import ru.ixec.easyfinance.repositories.ExpenseCategoryRepository;
import ru.ixec.easyfinance.repositories.StatisticCategoryRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {

    private final StatisticCategoryRepository scr;
    private final ExpenseCategoryRepository ecr;

    @PostConstruct
    void init() {
        List<String> cat = new ArrayList<>() {{
//            add("Автотранспорт");
//            add("Бизнес, проекты");
//            add("Бытовая техника");
//            add("Долги, кредиты");
//            add("Дом, квартира, дача");
//            add("Домашние животные");
//            add("Ипотека");
//            add("Кафе и рестораны");
//            add("Коммунальные платежи");
//            add("Красота и здоровье");
//            add("Медицина, аптека");
//            add("Мобильная связь, интернет, ТВ, телефон");
//            add("Финансовые операции");
//            add("Образование");
//            add("Общественный транспорт");
//            add("Одежда и обувь");
//            add("Отпуск, путешествия");
//            add("Мультимедиа");
//            add("Гипермаркет");
//            add("Развлечения и праздники");
//            add("Семья и дети");
//            add("Строительство и ремонт");
//            add("Хобби и увлечения");
//            add("Штрафы, налоги, комиссии");
//            add("Другое");
//            add("Я не помню");
//            add("Благотворительность");
//            add("АЗС");
//            add("Прочие расходы");
//            add("Снятие наличных");
        }};
        cat.forEach(this::save);
    }

    public ExpenseCategoryEntity getCategory(String productName) {
        Iterable<StatisticCategoryEntity> sc = scr.findAll(Sort.by(Sort.Direction.ASC, "singleName", "frequency"));
        TreeMap<Long, StatisticCategoryEntity> catTable = new TreeMap<>();
        List<String> prepList = ExpenseProductService.prepareName(productName, false);
        sc.forEach(e -> {
            if (prepList.contains(e.getSingleName())) catTable.put(e.getFrequency(), e);
        });
        return catTable.isEmpty() ? null : catTable.firstEntry().getValue().getExpenseCategory();
    }

    public ExpenseCategoryEntity save(String name) {
        return ecr.save(new ExpenseCategoryEntity(name));
    }
}