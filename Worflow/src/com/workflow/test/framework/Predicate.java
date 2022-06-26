
package com.workflow.test.framework;

public interface Predicate<T> {
  boolean test(T input);
  @Override
  boolean equals(Object object);
}
