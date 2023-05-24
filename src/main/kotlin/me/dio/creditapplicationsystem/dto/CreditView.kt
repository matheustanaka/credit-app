package me.dio.creditapplicationsystem.dto

import me.dio.creditapplicationsystem.entity.Credit
import java.math.BigDecimal
import java.util.UUID

data class CreditView(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallment: Int,
    val status: me.dio.creditapplicationsystem.enums.Status,
    val emailCustomer: String?,
    val incomeCustomer: BigDecimal?
) {
    constructor(credit: Credit): this (
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallment = credit.numberOfInstallments,
        status = credit.status,
        emailCustomer = credit.customer?.email,
        incomeCustomer = credit.customer?.income,
    )
}
