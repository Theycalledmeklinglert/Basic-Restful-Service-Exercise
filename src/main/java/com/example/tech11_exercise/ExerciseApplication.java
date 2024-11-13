package com.example.tech11_exercise;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class ExerciseApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(Service.class);
        classes.add(ExceptionMappers.JsonParseExceptionMapper.class);
        classes.add(ExceptionMappers.ValidationExceptionMapper.class);
        return classes;
    }
}