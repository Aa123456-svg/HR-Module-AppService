package ir.appservice.configuration.initializer;


import ir.appservice.model.entity.application.Account;
import ir.appservice.model.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@Configuration
public class TestInitializing implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TestInitializing.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AccountService accountService;

    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    @Override
    public void afterPropertiesSet() throws Exception {
        Account account = accountService.lazyGet("XXX", "roles.panels", "");
        logger.trace("Account: {}", account);
        logger.trace("Roles: {}", account.getRoles());
        account.getRoles().forEach(role -> {
            logger.trace("panel: {}", role.getPanels());
        });

//        f2();
    }

    private void f2() {
        logger.trace("f2");
        EntityGraph<Account> graph = entityManager.createEntityGraph(Account.class);
        Subgraph roleGraph = graph.addSubgraph("roles");
        roleGraph.addSubgraph("panels");
//        roleGraph.addAttributeNodes("panels");

        Map<String, Object> hints = new HashMap();
        hints.put("javax.persistence.loadgraph", graph);
        logger.trace("Graph: {}", graph.getAttributeNodes());

        Account account = entityManager.find(Account.class, UUID.fromString("XXX"), hints);
        logger.trace("Account: {}", account);
        logger.trace("Roles: {}", account.getRoles());
        account.getRoles().forEach(role -> logger.trace("panel: {}", role.getPanels()));
    }

    //
//    private void initializeApplication() {
//        if (account != null && !accountService.existsByAccountNameIgnoreCase(account.getAccountName())) {
//            account.getRoles().stream().forEach(role -> logger.trace("Account Access Panel By " +
//                    "Role: {}=> {}", role.getDisplayName(), role.getPanels()));
//
//            account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
//            account.setAvatar(account.getNaturalPerson().getAvatar());
////            account = accountService.add(account);
//            logger.trace(String.format("New account %s was added.", account.getAccountName()));
//
//
//            List<Class> entityClasses = new ArrayList<>();
//            ClassPathScanningCandidateComponentProvider scanner =
//                    new ClassPathScanningCandidateComponentProvider(false);
//            scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
//            for (BeanDefinition bd : scanner.findCandidateComponents(BaseEntity.class.getPackage().getName())) {
//                try {
//                    entityClasses.add(Class.forName(bd.getBeanClassName()));
//                    logger.trace("Entity: {}", Class.forName(bd.getBeanClassName()).getName());
//                } catch (ClassNotFoundException e) {
//                    logger.error("Class not found for Entity: {}", bd.getBeanClassName());
//                }
//            }
//
//            try {
//                JAXBContext jaxbContext = JAXBContext.newInstance(entityClasses.toArray(new Class[]{}));
//                marshaller = jaxbContext.createMarshaller();
//                logger.trace("Account: {}", account);
//                marshaller.marshal(account, new StreamResult(initializer.getFile()));
////            unmarshaller = jaxbContext.createUnmarshaller();
////            account =
////                    (Account) unmarshaller.unmarshal(feeder.getFile());
////            accountService.add(account);
//            } catch (JAXBException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage());
//            } catch (IOException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage());
//            }
//
//        }
//    }
}
