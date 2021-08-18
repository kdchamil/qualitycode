package com.chamil.qualitycoder.csvimport.v1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImportCommand<T> implements Command<T> {


    private final Validator validator;
    private final Store<T> store;
    private final DataSource dataSource;
    private final Mapper<T> mapper;

    public ImportCommand(Validator validator, Store<T> store, DataSource dataSource, Mapper<T> mapper) {
        this.validator = validator;
        this.store = store;
        this.dataSource = dataSource;
        this.mapper = mapper;
    }

    @Override
    public void execute() {
        Map<Integer, List<String>> errorList = new ConcurrentHashMap<>();

        //for each record in datasource
        for(Record r : dataSource) {
            List<String> errors = validator.validate(r);
            errorList.put(r.getId(), errors);
            T object = mapper.map(r);
            store.save(object);
        }
    }
}
