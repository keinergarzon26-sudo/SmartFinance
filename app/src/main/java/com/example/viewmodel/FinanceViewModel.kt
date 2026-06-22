package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.FinanceApplication
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as FinanceApplication).repository

    // Expose flows from Repository with stateIn
    val incomes = repository.incomes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val expenses = repository.expenses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val debts = repository.debts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val savings = repository.savings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val goals = repository.goals.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val budgets = repository.budgets.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // SharedPreferences to track if pre-populate occurred
    private val prefs = application.getSharedPreferences("finance_prefs", Context.MODE_PRIVATE)

    private fun isPrepopulated(): Boolean {
        return prefs.getBoolean("prepopulated", false)
    }

    private fun setPrepopulated(value: Boolean) {
        prefs.edit().putBoolean("prepopulated", value).apply()
    }

    // UI State for Active screen selector
    private val _currentTab = MutableStateFlow(0) // 0: Dashboard, 1: Trackers, 2: Budgets & Goals, 3: Indicators
    val currentTab = _currentTab.asStateFlow()

    fun selectTab(index: Int) {
        _currentTab.value = index
    }

    // Design Settings using SharedPreferences persistence
    private val _themeIndex = MutableStateFlow(prefs.getInt("theme_index", 0))
    val themeIndex = _themeIndex.asStateFlow()

    private val _templateIndex = MutableStateFlow(prefs.getInt("template_index", 0))
    val templateIndex = _templateIndex.asStateFlow()

    fun setThemeIndex(index: Int) {
        _themeIndex.value = index
        prefs.edit().putInt("theme_index", index).apply()
    }

    fun setTemplateIndex(index: Int) {
        _templateIndex.value = index
        prefs.edit().putInt("template_index", index).apply()
    }

    init {
        // Pre-populate database with beautiful mock values IF empty
        viewModelScope.launch {
            combine(incomes, expenses, budgets) { inc, exp, bud ->
                Triple(inc, exp, bud)
            }.first().let { (inc, exp, bud) ->
                if (inc.isEmpty() && exp.isEmpty() && bud.isEmpty()) {
                    if (!isPrepopulated()) {
                        prePopulateData()
                        setPrepopulated(true)
                    }
                } else {
                    setPrepopulated(true)
                }
            }
        }
    }

    private suspend fun prePopulateData() {
        // 1. Core Budgets
        repository.insertBudget(Budget(category = "Vivienda", limitAmount = 1400.0))
        repository.insertBudget(Budget(category = "Comida", limitAmount = 450.0))
        repository.insertBudget(Budget(category = "Transporte", limitAmount = 300.0))
        repository.insertBudget(Budget(category = "Servicios", limitAmount = 200.0))
        repository.insertBudget(Budget(category = "Entretenimiento", limitAmount = 150.0))
        repository.insertBudget(Budget(category = "Compras", limitAmount = 250.0))
        repository.insertBudget(Budget(category = "Otros", limitAmount = 100.0))

        // 2. Sample Incomes
        repository.insertIncome(Income(source = "Salario Mensual", amount = 3400.0, category = "Salario"))
        repository.insertIncome(Income(source = "Proyecto Independiente", amount = 950.0, category = "Independiente"))
        repository.insertIncome(Income(source = "Dividendos de Acciones", amount = 150.0, category = "Inversiones"))

        // 3. Sample Expenses
        repository.insertExpense(Expense(title = "Alquiler de Apartamento", amount = 1200.0, category = "Vivienda"))
        repository.insertExpense(Expense(title = "Supermercado Semanal", amount = 210.0, category = "Comida"))
        repository.insertExpense(Expense(title = "Electricidad e Internet", amount = 160.0, category = "Servicios"))
        repository.insertExpense(Expense(title = "Cena y Cine", amount = 90.0, category = "Entretenimiento"))
        repository.insertExpense(Expense(title = "Pase de Metro y Gasolina", amount = 85.0, category = "Transporte"))
        repository.insertExpense(Expense(title = "Zapatos para Correr", amount = 120.0, category = "Compras"))
        repository.insertExpense(Expense(title = "Servicio de Lavandería", amount = 45.0, category = "Otros"))

        // 4. Sample Debt Records
        repository.insertDebt(Debt(creditor = "Préstamo Estudiantil", originalAmount = 12000.0, currentAmount = 5600.0, interestRate = 4.2, dueDate = "1 de Julio", monthlyPayment = 220.0))
        repository.insertDebt(Debt(creditor = "Tarjeta de Crédito", originalAmount = 3000.0, currentAmount = 850.0, interestRate = 17.5, dueDate = "14 de Julio", monthlyPayment = 110.0))

        // 5. Sample Financial Goals
        repository.insertGoal(Goal(name = "Fondo de Emergencia", targetAmount = 6000.0, currentAmount = 4000.0, targetDate = "2026-11-20"))
        repository.insertGoal(Goal(name = "Viaje de Verano", targetAmount = 3500.0, currentAmount = 1050.0, targetDate = "2027-06-15"))

        // 6. Savings Deposits
        repository.insertSavings(Savings(description = "Reserva de Emergencia", amount = 3000.0))
        repository.insertSavings(Savings(description = "Ahorro para Vuelos de Vacaciones", amount = 1000.0))
    }

    // CRUD - Incomes
    fun addIncome(source: String, amount: Double, category: String, date: Long = System.currentTimeMillis()) {
        viewModelScope.launch {
            repository.insertIncome(Income(source = source, amount = amount, category = category, date = date))
        }
    }

    fun updateIncome(income: Income) {
        viewModelScope.launch {
            repository.insertIncome(income)
        }
    }

    fun deleteIncome(income: Income) {
        viewModelScope.launch {
            repository.deleteIncome(income)
        }
    }

    // CRUD - Expenses
    fun addExpense(title: String, amount: Double, category: String, date: Long = System.currentTimeMillis()) {
        viewModelScope.launch {
            repository.insertExpense(Expense(title = title, amount = amount, category = category, date = date))
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    // CRUD - Debts
    fun addDebt(creditor: String, originalAmount: Double, currentAmount: Double, interestRate: Double, dueDate: String, monthlyPayment: Double) {
        viewModelScope.launch {
            repository.insertDebt(Debt(
                creditor = creditor,
                originalAmount = originalAmount,
                currentAmount = currentAmount,
                interestRate = interestRate,
                dueDate = dueDate,
                monthlyPayment = monthlyPayment
            ))
        }
    }

    fun updateDebt(debt: Debt) {
        viewModelScope.launch {
            repository.insertDebt(debt)
        }
    }

    fun deleteDebt(debt: Debt) {
        viewModelScope.launch {
            repository.deleteDebt(debt)
        }
    }

    // CRUD - Savings
    fun addSavings(description: String, amount: Double, date: Long = System.currentTimeMillis()) {
        viewModelScope.launch {
            repository.insertSavings(Savings(description = description, amount = amount, date = date))
        }
    }

    fun updateSavings(savings: Savings) {
        viewModelScope.launch {
            repository.insertSavings(savings)
        }
    }

    fun deleteSavings(savings: Savings) {
        viewModelScope.launch {
            repository.deleteSavings(savings)
        }
    }

    // CRUD - Goals
    fun addGoal(name: String, targetAmount: Double, currentAmount: Double, targetDate: String) {
        viewModelScope.launch {
            repository.insertGoal(Goal(name = name, targetAmount = targetAmount, currentAmount = currentAmount, targetDate = targetDate))
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            repository.insertGoal(goal)
        }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }

    // CRUD - Budgets
    fun addBudget(category: String, limitAmount: Double) {
        viewModelScope.launch {
            repository.insertBudget(Budget(category = category, limitAmount = limitAmount))
        }
    }

    fun updateBudget(budget: Budget) {
        viewModelScope.launch {
            repository.insertBudget(budget)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            setPrepopulated(true)
            repository.clearAllData()
        }
    }
}

// Simple Factory for creating ViewModel with application context
class FinanceViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FinanceViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
