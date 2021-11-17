package kg.itacademy.util;

public interface VariableValidation<T> {
    void validateLengthVariables(T t);
    void validateLengthVariablesForUpdate(T t);
    void validateVariablesForNullOrIsEmpty(T t);
    void validateVariablesForNullOrIsEmptyUpdate(T t);
}
