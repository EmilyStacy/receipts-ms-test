package com.aa.fly.receipts.data.adjuster;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.aa.fly.receipts.domain.PassengerDetail;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.DataAdjusterService;
import org.springframework.stereotype.Component;

@Component
public class PassengerTotalAdjuster implements DataAdjusterService {

	@Override
	public TicketReceipt adjust(TicketReceipt ticketReceipt) {

		PassengerDetail passengerDetail = ticketReceipt.getPassengerDetails().get(0);
		
        BigDecimal passengerTotalAmount = new BigDecimal("0");

        for (int i = 0; i < passengerDetail.getFormOfPayments().size(); i++) {
            final String fopAmount = passengerDetail.getFormOfPayments().get(i).getFopAmount();
            final String fopCurrencyCode = passengerDetail.getFormOfPayments().get(i).getFopCurrencyCode();
            if (fopAmount != null && !fopCurrencyCode.equalsIgnoreCase("X")) {
                passengerTotalAmount = passengerTotalAmount.add(new BigDecimal(fopAmount)).setScale(2, RoundingMode.CEILING);
            }
        }

        // in case of even exchange, return totalFareAmount as passengerTotalAmount
        if (passengerTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
            passengerTotalAmount = BigDecimal.valueOf(Double.valueOf(passengerDetail.getFareTaxesFees().getTotalFareAmount())).setScale(2, RoundingMode.CEILING);
        }
        
        passengerDetail.setPassengerTotalAmount(passengerTotalAmount.toString());
        this.setShowPassengerTotal(passengerDetail);

		return ticketReceipt;
	}
	
    private void setShowPassengerTotal(PassengerDetail passengerDetail) {
        passengerDetail.setShowPassengerTotal(true);
        String baseFareCurrencyCode = passengerDetail.getFareTaxesFees().getBaseFareCurrencyCode();
        passengerDetail.getFormOfPayments().forEach(formOfPayment -> formOfPayment.getAncillaries().stream().filter(ancillary -> !baseFareCurrencyCode.equals(ancillary.getAnclryPriceCurrencyCode()))
                .forEach(t -> passengerDetail.setShowPassengerTotal(false)));
    }	
}
