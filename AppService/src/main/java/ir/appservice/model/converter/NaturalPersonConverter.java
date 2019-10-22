package ir.appservice.model.converter;

import ir.appservice.model.entity.domain.NaturalPerson;
import ir.appservice.model.repository.NaturalPersonRepository;
import org.springframework.stereotype.Component;

@Component
public class NaturalPersonConverter extends BaseConverter<NaturalPerson> {

    public NaturalPersonConverter(NaturalPersonRepository naturalPersonRepository) {
        super(NaturalPerson.class, naturalPersonRepository);
    }
}
