package com.project.config;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

@Component
@NoArgsConstructor
public class ModelMapperProject extends ModelMapper {

    public  <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> this.map(element, targetClass))
                .collect(Collectors.toList());
    }

}