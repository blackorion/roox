package social.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import social.customers.partnermapping.PartnerMapping;
import social.customers.partnermapping.PartnerMappingsRepository;

import java.util.List;

@RestController
@PreAuthorize("@currentUserAccessByIdAndStateService.canAccessUser(principal, #customerId)")
@RequestMapping("/customers/{cid:[\\d]+}")
public class CustomerPartnerMappingsController {
    @Autowired
    CustomersRepository customersRepository;
    @Autowired
    PartnerMappingsRepository mappingsRepository;

    @ResponseBody
    @RequestMapping(value = "/partner-mappings", method = RequestMethod.GET)
    public List<PartnerMapping> index(@PathVariable("cid") Long customerId) {
        return customersRepository.findOne(customerId).mappingList;
    }

    @ResponseBody
    @RequestMapping(value = "/partner-mappings/{mapping-id}", method = RequestMethod.GET)
    public PartnerMapping show(@PathVariable("cid") Long customerId, @PathVariable("mapping-id") Long mappingId) {
        return mappingsRepository.findOne(mappingId);
    }

    @ResponseBody
    @RequestMapping(value = "/partner-mappings", method = RequestMethod.POST)
    public PartnerMapping save(@PathVariable("cid") Long customerId, @RequestBody PartnerMapping mapping) {
        mapping.customer = customersRepository.findOne(customerId);
        mapping = mappingsRepository.save(mapping);

        return mapping;
    }

    @ResponseBody
    @RequestMapping(value = "/partner-mappings/{mapping-id}", method = RequestMethod.PUT)
    public PartnerMapping update(@PathVariable("cid") Long customerId,
                                 @PathVariable("mapping-id") Long mappingId,
                                 @RequestBody PartnerMapping mappingData) {
        PartnerMapping mapping = mappingsRepository.findOne(mappingId);
        mapping.partnerId = mappingData.partnerId;
        mapping.accId = mappingData.accId;
        mapping.fio = mappingData.fio;
        mapping.gravatar = mappingData.gravatar;

        return mappingsRepository.save(mapping);
    }

    @RequestMapping(value = "/partner-mappings/{mapping-id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("cid") Long customerId, @PathVariable("mapping-id") Long mappingId) {
        mappingsRepository.delete(mappingId);
    }
}