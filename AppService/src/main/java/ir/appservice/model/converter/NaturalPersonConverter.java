package ir.appservice.model.converter;

import ir.appservice.model.entity.domain.NaturalPerson;
import ir.appservice.model.service.NaturalPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Component
public class NaturalPersonConverter implements Converter<NaturalPerson> {

    private final static Logger logger = LoggerFactory.getLogger(NaturalPersonConverter.class);

    private NaturalPersonService naturalPersonService;

    public NaturalPersonConverter(NaturalPersonService naturalPersonService) {
        this.naturalPersonService = naturalPersonService;
    }

    @Override
    public NaturalPerson getAsObject(FacesContext context, UIComponent component, String id) {

        logger.trace(String.format("Converting %s to naturalPerson...", id));
        try {
            NaturalPerson temp = naturalPersonService.get(Long.valueOf(id));
            logger.trace(String.format("NaturalPerson: %s", temp.getId()));
            return temp;
        } catch (Exception e) {
            logger.warn(String.format("NaturalPerson: %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, NaturalPerson naturalPerson) {
        logger.trace(String.format("Converting naturalPerson to %s ...", naturalPerson.getId().toString()));
        try {
            logger.trace(String.format("String: %s", naturalPerson.getId().toString()));
            return naturalPerson.getId().toString();
        } catch (Exception e) {
            logger.warn(String.format("String: %s", e.getMessage()));
            return "-1";
        }
    }
}
