package me.dio.creditapplicationsystem.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import io.mockk.verify
import me.dio.creditapplicationsystem.entity.Address
import me.dio.creditapplicationsystem.entity.Credit
import me.dio.creditapplicationsystem.entity.Customer
import me.dio.creditapplicationsystem.exception.BusinessException
import me.dio.creditapplicationsystem.repository.CreditRepository
import me.dio.creditapplicationsystem.service.implementation.CreditService
import me.dio.creditapplicationsystem.service.implementation.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK
    lateinit var creditRepository: CreditRepository

    @MockK
    lateinit var customerService: CustomerService

    @InjectMockKs
    lateinit var creditService: CreditService

    private lateinit var customer: Customer


    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        customer = CustomerServiceTest.buildCustomer()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Should save credit`() {
        //given
        val credit: Credit = buildCredit()
        val customerId: Long = 1L

        every { customerService.findById(customerId) } returns credit.customer!!
        every { creditRepository.save(credit) } returns credit
        //when
        val actual: Credit = this.creditService.save(credit)
        //then
        verify(exactly = 1) { customerService.findById(customerId) }
        verify(exactly = 1) { creditRepository.save(credit) }

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(credit)
    }


    @Test
    fun `Should not save credit with first installment date more than 3 months`() {
        val customerId: Long =  1L
        every { customerService.findById(customerId) } returns customer
        val credit = buildCredit(
            dayFirstInstallment = LocalDate.now().plusMonths(4L),
            customer = customer
        )

        val exception = assertThrows<BusinessException> {
            creditService.save(credit)
        }

        Assertions.assertThat(exception.message).isEqualTo("Invalid installment date")
        verify(exactly = 0) { creditRepository.save(any()) }
    }


    companion object {
        private fun buildCredit(
            creditValue: BigDecimal = BigDecimal.valueOf(100.0),
            dayFirstInstallment: LocalDate = LocalDate.now().plusMonths(2L),
            numberOfInstallments: Int = 15,
            customer: Customer = CustomerServiceTest.buildCustomer()
        ): Credit = Credit(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            customer = customer
        )
    }
}