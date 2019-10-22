package ir.appservice.model.converter;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public abstract class BaseConverter<T extends BaseEntity> implements Converter<T> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private CrudRepository crudRepository;
    private Class<T> clazz;

    public BaseConverter(Class<T> clazz, CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
        this.clazz = clazz;
    }

    @Override
    @Transactional
    public T getAsObject(FacesContext context, UIComponent component, String id) {
//        component.getAttributes().entrySet().stream().forEach(entry -> logger.warn("{}:{}", entry.getKey(), entry.getValue()));
        T temp = null;
        try {
            temp = (T) crudRepository.getById(id);
            logger.trace("To {}: {} -> {}", clazz.getSimpleName(), id, temp);
        } catch (Exception e) {
            logger.error("To {}: {} -> {}\n{}", clazz.getSimpleName(), id, temp, e.getMessage());
        } finally {
            return temp;
        }
    }

    @Override
    @Transactional
    public String getAsString(FacesContext context, UIComponent component, T item) {
        String asString = "";
        try {
            asString = item.getId();
            logger.trace("{} To String {} -> {}", clazz.getSimpleName(), item, asString);
        } catch (Exception e) {
            logger.error("{} To String {} -> {}\n{}", clazz.getSimpleName(), item, asString,
                    e.getMessage());
        } finally {
            return asString;
        }
    }
}
