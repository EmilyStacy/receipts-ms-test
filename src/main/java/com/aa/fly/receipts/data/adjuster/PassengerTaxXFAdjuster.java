package com.aa.fly.receipts.data.adjuster;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import com.aa.fly.receipts.domain.FareTaxesFees;
import com.aa.fly.receipts.domain.Tax;
import com.aa.fly.receipts.domain.TicketReceipt;
import com.aa.fly.receipts.service.DataAdjusterService;
import org.springframework.stereotype.Component;

@Component
public class PassengerTaxXFAdjuster implements DataAdjusterService {

	@Override
	public TicketReceipt adjust(TicketReceipt ticketReceipt) {
		FareTaxesFees fareTaxesFees = ticketReceipt.getPassengerDetails().get(0).getFareTaxesFees();

		if (fareTaxesFees != null && fareTaxesFees.getTaxes() != null && !fareTaxesFees.getTaxes().isEmpty()) {
			String baseFareCurrencyCode = fareTaxesFees.getBaseFareCurrencyCode();
			BigDecimal totalFareAmount = new BigDecimal(fareTaxesFees.getTotalFareAmount());
			BigDecimal baseFareAmount = new BigDecimal(fareTaxesFees.getBaseFareAmount());
			BigDecimal totalTaxAmount = totalFareAmount.subtract(baseFareAmount);
			fareTaxesFees.setTaxFareAmount(totalTaxAmount.toString());
			Set<Tax> taxes = fareTaxesFees.getTaxes();
			// XF tax amount always comes as USD, even though baseFareCurrency is not USD.
			// If baseFareCurrencyCode is not USD, merge all XFs into one entry
			// calculate the XF amount by adding the base fare, taxes with baseFareCurrency
			// and subtract the amount from totalFare.
			// We need to do this so all the line items add up to the total amount on the
			// receipt.
			long count = taxes.stream().filter(
					t -> !baseFareCurrencyCode.equals(t.getTaxCurrencyCode()) && "XF".equalsIgnoreCase(t.getTaxCode()))
					.count();

			if (count > 0) {
				double nonXFTaxAmountDouble = taxes.stream()
						.filter(t -> baseFareCurrencyCode.equals(t.getTaxCurrencyCode()))
						.mapToDouble(t -> Double.valueOf(t.getTaxAmount())).sum();
				BigDecimal nonXFTaxAmount = BigDecimal.valueOf(nonXFTaxAmountDouble);
				String xfAmount = (totalTaxAmount.subtract(nonXFTaxAmount)).setScale(2, RoundingMode.CEILING)
						.toString();
				Tax mergedXF = taxes.stream().filter(t -> "XF".equals(t.getTaxCode())).findFirst().orElse(new Tax());
				mergedXF.setTaxAmount(xfAmount);
				mergedXF.setTaxCurrencyCode(baseFareCurrencyCode);
				taxes.removeIf(t -> "XF".equals(t.getTaxCode()));
				taxes.add(mergedXF);
			}
		}

		return ticketReceipt;
	}

	/*
	 * @Override public PassengerDetail
	 * adjustTaxesWithOtherCurrencies(PassengerDetail passengerDetail) { if
	 * (passengerDetail == null || passengerDetail.getFareTaxesFees() == null)
	 * return passengerDetail;
	 * 
	 * FareTaxesFees fareTaxesFees = passengerDetail.getFareTaxesFees(); String
	 * baseFareCurrencyCode = fareTaxesFees.getBaseFareCurrencyCode(); BigDecimal
	 * totalFareAmount = new BigDecimal(fareTaxesFees.getTotalFareAmount());
	 * BigDecimal baseFareAmount = new
	 * BigDecimal(fareTaxesFees.getBaseFareAmount()); BigDecimal totalTaxAmount =
	 * totalFareAmount.subtract(baseFareAmount);
	 * fareTaxesFees.setTaxFareAmount(totalTaxAmount.toString());
	 * 
	 * Set<Tax> taxes = passengerDetail.getFareTaxesFees().getTaxes();
	 * 
	 * // XF tax amount always comes as USD, even though baseFareCurrency is not
	 * USD. If baseFareCurrencyCode is not USD, merge all XFs into one entry //
	 * calculate the XF amount by adding the base fare, taxes with baseFareCurrency
	 * and subtract the amount from totalFare. // We need to do this so all the line
	 * items add up to the total amount on the receipt. long count =
	 * taxes.stream().filter(t ->
	 * !baseFareCurrencyCode.equals(t.getTaxCurrencyCode()) &&
	 * "XF".equalsIgnoreCase(t.getTaxCode())).count();
	 * 
	 * if (count > 0) { double nonXFTaxAmountDouble = taxes.stream().filter(t ->
	 * baseFareCurrencyCode.equals(t.getTaxCurrencyCode())).mapToDouble(t ->
	 * Double.valueOf(t.getTaxAmount())).sum(); BigDecimal nonXFTaxAmount =
	 * BigDecimal.valueOf(nonXFTaxAmountDouble); String xfAmount =
	 * (totalTaxAmount.subtract(nonXFTaxAmount)).setScale(2,
	 * RoundingMode.CEILING).toString(); Tax mergedXF = taxes.stream().filter(t ->
	 * "XF".equals(t.getTaxCode())).findFirst().orElse(new Tax());
	 * mergedXF.setTaxAmount(xfAmount);
	 * mergedXF.setTaxCurrencyCode(baseFareCurrencyCode); taxes.removeIf(t ->
	 * "XF".equals(t.getTaxCode())); taxes.add(mergedXF); } return passengerDetail;
	 * }
	 */
}
