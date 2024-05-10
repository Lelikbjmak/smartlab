package com.innowise.smartlab.mapper;

import java.util.List;
import org.apache.commons.lang3.NotImplementedException;

public interface GenericMapper<E, D> {

  default D toDto(E entity) {
    throw new NotImplementedException("Method not implemented");
  }

  default E toEntity(D dto) {
    throw new NotImplementedException("Method not implemented");
  }

  default List<D> toDtoList(List<E> entityList) {
    throw new NotImplementedException("Method not implemented");
  }

  default List<E> toEntityList(List<D> dtoList) {
    throw new NotImplementedException("Method not implemented");
  }
}
