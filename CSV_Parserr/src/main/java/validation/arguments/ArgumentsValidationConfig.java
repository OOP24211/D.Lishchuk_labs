package validation.arguments;

import validation.validators.IValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class ArgumentsValidationConfig {
    private final Map<Integer, List<IValidator<Object>>> validatorsByIndex;

    private ArgumentsValidationConfig(Map<Integer, List<IValidator<Object>>> validatorsByIndex) {
        this.validatorsByIndex = Map.copyOf(validatorsByIndex);
    }

    public List<IValidator<Object>> validatorsFor(int index) {
        return validatorsByIndex.getOrDefault(index, List.of());
    }

    public Set<Integer> indices() {
        return validatorsByIndex.keySet();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<Integer, List<IValidator<Object>>> map = new HashMap<>();

        public Builder arg(int index, IValidator<?> validator) {
            return add(index, ValidatorAdapters.raw(validator));
        }

        private Builder add(int index, IValidator<Object> validator) {
            Objects.requireNonNull(validator, "validation");
            if (index < 0) {
                throw new IllegalArgumentException("index must be >= 0");
            }
            map.computeIfAbsent(index, __ -> new ArrayList<>()).add(validator);
            return this;
        }

        public ArgumentsValidationConfig build() {
            Map<Integer, List<IValidator<Object>>> out = new HashMap<>();
            for (var e : map.entrySet()) {
                out.put(e.getKey(), List.copyOf(e.getValue()));
            }
            return new ArgumentsValidationConfig(out);
        }
    }
}
