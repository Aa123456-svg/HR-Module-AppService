package ir.appservice.model.service;

import ir.appservice.model.entity.domain.NaturalPerson;
import ir.appservice.model.repository.NaturalPersonRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class NaturalPersonService extends CrudService<NaturalPerson> {

    private NaturalPersonRepository naturalPersonRepository;

    public NaturalPersonService(NaturalPersonRepository naturalPersonRepository) {
        super(naturalPersonRepository);
        this.naturalPersonRepository = naturalPersonRepository;
    }
}
