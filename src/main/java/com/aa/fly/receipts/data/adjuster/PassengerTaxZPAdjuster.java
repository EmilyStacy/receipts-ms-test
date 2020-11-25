package com.aa.fly.receipts.data.adjuster;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.Tax;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.DataAdjusterService;
import org.springframework.stereotype.Component;

@Component
public class PassengerTaxZPAdjuster implements DataAdjusterService {

	@Override
	public TicketReceipt adjust(TicketReceipt ticketReceipt) {
		
		FareTaxesFees fareTaxesFees = ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees();
				
        if (fareTaxesFees != null && fareTaxesFees.getTaxes() != null && !fareTaxesFees.getTaxes().isEmpty()) {
            Set<Tax> zpTaxes = fareTaxesFees.getTaxes().stream().filter(tax -> "ZP".equals(tax.getTaxCode())).collect(Collectors.toSet()); // get all ZP tax line items
            if (zpTaxes.size() > 1) {
                Optional<Tax> maxZPTaxLineItem = zpTaxes.stream().max(Comparator.comparing(Tax::getTaxAmountDouble)); // get the line item with maximum tax amount
                Double subTotal = zpTaxes.stream().filter(tax -> !tax.getTaxCodeSequenceId().equals(maxZPTaxLineItem.get().getTaxCodeSequenceId()))
                        .mapToDouble(Tax::getTaxAmountDouble).sum(); // calculate the sum of remaining items
                
                if (maxZPTaxLineItem.get().getTaxAmountDouble().equals(subTotal)) {
                	
                    // maxZPTaxLineItem is the subtotal item of all remaining ZPs. Keep the subtotal item and remove all ZP line items.
                    Set<Tax> nonZPTaxes = fareTaxesFees.getTaxes().stream().filter(tax -> !"ZP".equals(tax.getTaxCode())).collect(Collectors.toSet());
                    fareTaxesFees.setTaxes(nonZPTaxes);
                    fareTaxesFees.getTaxes().add(maxZPTaxLineItem.get());
                }
            }
        }
		return ticketReceipt;
	}
}
