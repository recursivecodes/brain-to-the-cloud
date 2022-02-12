package codes.recursive.util;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.data.runtime.convert.DataTypeConverter;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.Optional;

@Singleton
public class BigDecimalToBooleanConverter implements DataTypeConverter<BigDecimal, Boolean> {
    @Override
    public Optional<Boolean> convert(BigDecimal value, Class<Boolean> targetType, ConversionContext context) {
        if(value.compareTo(new BigDecimal(1)) == 0) return Optional.of(Boolean.TRUE);
        return Optional.of(Boolean.FALSE);
    }
}
