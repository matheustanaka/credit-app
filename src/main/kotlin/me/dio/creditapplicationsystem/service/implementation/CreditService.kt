package me.dio.creditapplicationsystem.service.implementation

import me.dio.creditapplicationsystem.entity.Credit
import me.dio.creditapplicationsystem.exception.BusinessException
import me.dio.creditapplicationsystem.repository.CreditRepository
import me.dio.creditapplicationsystem.service.ICreditService
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.time.LocalDate
import java.util.*

@Service
class CreditService(private val creditRepository: CreditRepository, private val customerService: CustomerService): ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply { customer = customerService.findById(credit.customer?.id!!) }
        validateFirstInstallmentDate(credit.dayFirstInstallment)
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> {
        return this.creditRepository.findAllByCustomerId(customerId)
    }

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit = (this.creditRepository.findByCreditCode(creditCode) ?: throw BusinessException("It was not possible to find the CreditCode $creditCode"))
        return if(credit.customer?.id == customerId) credit else throw IllegalArgumentException("Please, contact application admin")
    }

    private fun validateFirstInstallmentDate(firstInstallmentDate: LocalDate) {
        val threeMonthsFromNow = LocalDate.now().plusMonths(3)
        if (firstInstallmentDate.isAfter(threeMonthsFromNow)) {
            throw BusinessException("Installment date should be 3 months after the current day.")
        }
    }

}