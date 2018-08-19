package ru.mbtc.varsvc;

import com.google.inject.ImplementedBy;
import ru.mbtc.varsvc.impl.VariablesServiceImpl;

import java.util.Map;


public interface VariablesService {
    Map<String,String> apply(long fid);
}
