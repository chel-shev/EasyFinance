package ru.ixec.easyfinance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.ixec.easyfinance.entity.ExpenseCategory;
import ru.ixec.easyfinance.entity.StatisticCategory;
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
    void init(){
        List<String> cat = new ArrayList<>(){{
            add("Алкоголь крепкий");
            add("Бакалея");
            add("Вино и шампанское");
            add("Горячие напитки");
            add("Диетическое и здоровое питание");
            add("Замороженные продукты");
            add("Икра и рыбная гастрономия");
            add("Колбасные изделия и мясные деликатесы");
            add("Кондитерские изделия длительного хранения");
            add("Кондитерские изделия короткого срока хранения");
            add("Консервация");
            add("Масла растительные, кетчупы, соусы");
            add("Масло, майонез");
            add("Молочные продукты");
            add("Мороженое");
            add("Мясо");
            add("Напитки");
            add("Овощи");
            add("Овощи и плоды приготовленные");
            add("Орехи, сухофрукты");
            add("Пиво и напитки слабоалкогольные");
            add("Пищевые ингредиенты");
            add("Продукты для детского питания");
            add("Прочая гастрономия");
            add("Птица");
            add("Рыба и морепродукты замороженные");
            add("Рыба и морепродукты охлажденные");
            add("Сыры");
            add("Табак");
            add("Товары для животных");
            add("Фрукты, ягоды");
            add("Хлебобулочные изделия, поставщик");
            add("Яйцо");
            add("Автотовары");
            add("Аксессуары");
            add("Аудио-видео техника");
            add("Бутик-косметика");
            add("Бытовая техника");
            add("Бытовая химия");
            add("Галантерея");
            add("Гигиена");
            add("Детская одежда");
            add("Женская одежда");
            add("Игрушки");
            add("Интерьер");
            add("Канцтовары");
            add("Косметика");
            add("Мебель для дома");
            add("Мужская одежда");
            add("Мультимедиа");
            add("Обувь");
            add("Полиграфия");
            add("Порядок в доме");
            add("Посуда");
            add("Праздничные товары");
            add("Текстиль");
            add("Техника для офиса");
            add("Товары для новорожденных");
            add("Товары для отдыха");
            add("Товары для ремонта");
            add("Товары для сада, дачи");
            add("Товары для спорта");
            add("Упаковочные пакеты и сумки для клиентов");
            add("Электротовары");
            add("Кулинария готовая");
            add("Полуфабрикаты собственного производства");
            add("Сырье для производства");
            add("Хлеб и кондитерские изделия, пекарня");
        }};
        cat.forEach(this::save);

    }

    public ExpenseCategory getCategory(String productName) {
        Iterable<StatisticCategory> sc = scr.findAll(Sort.by(Sort.Direction.ASC, "singleName", "frequency"));
        TreeMap<Long, StatisticCategory> catTable = new TreeMap<>();
        List<String> prepList = ExpenseProductService.prepareName(productName, false);
        sc.forEach(e -> {
            if (prepList.contains(e.getSingleName())) catTable.put(e.getFrequency(), e);
        });
        return catTable.isEmpty() ? null : catTable.firstEntry().getValue().getExpenseCategory();
    }

    public ExpenseCategory save(String name){
        return ecr.save(new ExpenseCategory(name));
    }
}