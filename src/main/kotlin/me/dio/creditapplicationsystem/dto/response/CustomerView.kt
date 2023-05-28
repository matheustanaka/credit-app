package me.dio.creditapplicationsystem.dto.response

import me.dio.creditapplicationsystem.entity.Customer
import java.math.BigDecimal

data class CustomerView(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val email: String,
    val income: BigDecimal,
    val zipCode: String,
    val street: String,
    val id: Long?
) {
    constructor(customer: Customer): this (
        firstName = customer.firstName,
        lastName = customer.lastName,
        email = customer.email,
        cpf = customer.cpf,
        income = customer.income,
        zipCode = customer.address.zipCode,
        street = customer.address.street,
        id = customer.id

    )
}
