package ru.ixec.easyfinance.bot.receipt;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ixec.easyfinance.entity.ExpenseEntity;
import ru.ixec.easyfinance.entity.ExpenseProductEntity;
import ru.ixec.easyfinance.service.ExpenseProductService;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.isNull;

/**
 * Парсинг qr кода, из чека
 * Вид кода: t=20200517T1429&s=2711.51&fn=9251440300024356&i=108404&fp=357526947&n=1
 */
@Data
@Component
public class Receipt {

    @Value("${easy.finance.receipt.api.url}")
    private String URL_API;

    @Value("${easy.finance.receipt.api.token}")
    private String TOKEN_API;

    /**
     * Номер ФН (Фискальный Номер) — 16-значный номер. Например 8710000100518392
     */
    private String FN;

    /**
     * Номер ФПД (Фискальный Признак Документа, также известный как ФП) — до 10 знаков. Например 3522207165
     */
    private String FP;

    /**
     * Номер ФД (Фискальный документ) — до 10 знаков. Например 54812
     */
    private String FD;

    /**
     * Дата — дата с чека. Формат может отличаться.
     */
    private String T;

    /**
     * Сумма — сумма с чека в копейках
     */
    private String S;

    /**
     * Вид кассового чека.
     * В чеке помечается как
     * n=1 (приход)
     * n=2 (возврат прихода)
     */
    private String N;

    public List<ExpenseEntity> getExpenses() throws JSONException {
        Webb webb = Webb.create();
        Response<JSONObject> response = webb.post(URL_API)
                .params(getParams())
                .ensureSuccess()
                .asJsonObject();
        return createItems(response.getBody());
    }

    public List<ExpenseEntity> createItems(JSONObject jsonReceipt) throws JSONException {
        JSONObject receipt = jsonReceipt.getJSONObject("data").getJSONObject("json");
        LocalDateTime dateTime = LocalDateTime.parse((String) receipt.get("dateTime"));
        List<ExpenseEntity> expenseEntityList = new ArrayList<>();
        JSONArray jsonItems = receipt.getJSONArray("items");
        for (Object itemObject : jsonItems) {
            ExpenseEntity expenseEntity = getExpenseFromItem((JSONObject) itemObject, dateTime);
            expenseEntityList.add(expenseEntity);
        }
        return expenseEntityList;
    }

    public void setQR(String qr) {
        String[] qr_data = qr.split("&");
        Arrays.stream(qr_data).forEach(data -> {
            String value = getValue(data);
            if (data.startsWith("t")) setT(value);
            if (data.startsWith("s")) setS(value);
            if (data.startsWith("fn")) setFN(value);
            if (data.startsWith("i")) setFD(value);
            if (data.startsWith("fp")) setFP(value);
            if (data.startsWith("n")) setN(value);
        });
        if (isInvalid())
            throw new NullPointerException();
    }

    private ExpenseEntity getExpenseFromItem(JSONObject item, LocalDateTime dateTime) throws JSONException {
        Long price = item.getLong("price");
        Long sum = item.getLong("sum");
        Double quantity = item.getDouble("quantity");
        String name = ExpenseProductService.getCleanName(item.getString("name"));
        ExpenseProductEntity expenseProductEntity = new ExpenseProductEntity(name, null);
        return new ExpenseEntity(dateTime, price, sum, quantity, expenseProductEntity);
    }

    private Map<String, Object> getParams() {
        return new HashMap<>() {{
            put("t", T);
            put("s", S);
            put("fn", FN);
            put("fd", FD);
            put("fp", FP);
            put("n", N);
            put("qr", "1");
            put("token", TOKEN_API);
        }};
    }

    /**
     * @param data - строка типа: s=2711.51
     * @return value s=2711.51 -> 2711.51
     */
    private String getValue(String data) {
        return data.contains("=") ? data.split("=")[1] : "";
    }

    public boolean isInvalid() {
        return isNull(FP) || isNull(N) || isNull(S) || isNull(FD) || isNull(FN) || isNull(T);
    }

    public void clear() {
        FP = null;
        N = null;
        S = null;
        FD = null;
        FN = null;
        T = null;
    }
}