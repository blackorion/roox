package social.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import social.customers.Customer;
import social.customers.partnermapping.PartnerMapping;
import social.customers.partnermapping.PartnerMappingsRepository;

import java.util.UUID;

@Service
public class PartnerMappingsUtilService {
    @Autowired
    private PartnerMappingsRepository mappingsRepository;

    public PartnerMapping createMapping(Customer customer) {
        PartnerMapping mapping = new PartnerMapping();
        mapping.customer = customer;
        mapping.accId = UUID.randomUUID().getLeastSignificantBits();

        return mappingsRepository.save(mapping);
    }

    public void clearDatabase() {
        mappingsRepository.deleteAll();
    }

    public PartnerMapping find(long id) {
        return mappingsRepository.findOne(id);
    }
}
