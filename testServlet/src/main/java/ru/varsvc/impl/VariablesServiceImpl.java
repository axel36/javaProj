package ru.mbtc.varsvc.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import ru.mbtc.varsvc.VariablesService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class VariablesServiceImpl implements VariablesService {
    private final DataSource dataSource;

    @Inject
    VariablesServiceImpl(@Named("mainDB") DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Map<String, String> apply(long fid) {
        final Map<String, String> result = new HashMap<>();
        String s = Long.toString(fid);
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result.put("Deep_overdue_loans_cnt", "Deep_overdue_loans_cnt"+s);
        result.put("Overdue_loans_cnt", "Overdue_loans_cnt"+s);
        result.put("Credit_with_due_rate", "Credit_with_due_rate"+s);
        result.put("Bad_debt_cnt", "Bad_debt_cnt"+s);
        result.put("Bad_pmt_cnt", "Bad_pmt_cnt"+s);
        result.put("Good_pmt_cnt", "Good_pmt_cnt"+s);
        result.put("Paid_sum", "Paid_sum"+s);
        result.put("Credit_sum_avg", "Credit_sum_avg"+s);
        result.put("Outst_sum", "Outst_sum"+s);
        result.put("Outst_avg_4year_sum", "Outst_avg_4year_sum"+s);
        return result;
    }

}
