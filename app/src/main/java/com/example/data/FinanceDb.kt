package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ==========================================
// 1. ROOM ENTITIES (DATA MODELS)
// ==========================================

@Entity(tableName = "incomes")
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val source: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val category: String
)

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val category: String
)

@Entity(tableName = "debts")
data class Debt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val creditor: String,
    val originalAmount: Double,
    val currentAmount: Double,
    val interestRate: Double,
    val dueDate: String,
    val monthlyPayment: Double
)

@Entity(tableName = "savings")
data class Savings(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val associatedGoalId: Int? = null
)

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDate: String
)

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val limitAmount: Double
)

// ==========================================
// 2. DATA ACCESS OBJECT (DAO)
// ==========================================

@Dao
interface FinanceDao {
    // Incomes
    @Query("SELECT * FROM incomes ORDER BY date DESC")
    fun getAllIncomes(): Flow<List<Income>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: Income)

    @Delete
    suspend fun deleteIncome(income: Income)

    // Expenses
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    // Debts
    @Query("SELECT * FROM debts ORDER BY id DESC")
    fun getAllDebts(): Flow<List<Debt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(debt: Debt)

    @Delete
    suspend fun deleteDebt(debt: Debt)

    // Savings
    @Query("SELECT * FROM savings ORDER BY date DESC")
    fun getAllSavings(): Flow<List<Savings>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavings(savings: Savings)

    @Delete
    suspend fun deleteSavings(savings: Savings)

    // Goals
    @Query("SELECT * FROM goals ORDER BY id DESC")
    fun getAllGoals(): Flow<List<Goal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    // Budgets
    @Query("SELECT * FROM budgets")
    fun getAllBudgets(): Flow<List<Budget>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Delete
    suspend fun deleteBudget(budget: Budget)

    @Query("DELETE FROM incomes")
    suspend fun clearIncomes()

    @Query("DELETE FROM expenses")
    suspend fun clearExpenses()

    @Query("DELETE FROM debts")
    suspend fun clearDebts()

    @Query("DELETE FROM savings")
    suspend fun clearSavings()

    @Query("DELETE FROM goals")
    suspend fun clearGoals()

    @Query("DELETE FROM budgets")
    suspend fun clearBudgets()

    @Transaction
    suspend fun clearAllData() {
        clearIncomes()
        clearExpenses()
        clearDebts()
        clearSavings()
        clearGoals()
        clearBudgets()
    }
}

// ==========================================
// 3. ROOM DATABASE CLASS
// ==========================================

@Database(
    entities = [
        Income::class,
        Expense::class,
        Debt::class,
        Savings::class,
        Goal::class,
        Budget::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FinanceDatabase : RoomDatabase() {
    abstract val dao: FinanceDao

    companion object {
        @Volatile
        private var INSTANCE: FinanceDatabase? = null

        fun getDatabase(context: Context): FinanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDatabase::class.java,
                    "finance_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// ==========================================
// 4. REPOSITORY PATTERN IMPLEMENTATION
// ==========================================

class FinanceRepository(private val dao: FinanceDao) {
    val incomes: Flow<List<Income>> = dao.getAllIncomes()
    val expenses: Flow<List<Expense>> = dao.getAllExpenses()
    val debts: Flow<List<Debt>> = dao.getAllDebts()
    val savings: Flow<List<Savings>> = dao.getAllSavings()
    val goals: Flow<List<Goal>> = dao.getAllGoals()
    val budgets: Flow<List<Budget>> = dao.getAllBudgets()

    suspend fun insertIncome(income: Income) = dao.insertIncome(income)
    suspend fun deleteIncome(income: Income) = dao.deleteIncome(income)

    suspend fun insertExpense(expense: Expense) = dao.insertExpense(expense)
    suspend fun deleteExpense(expense: Expense) = dao.deleteExpense(expense)

    suspend fun insertDebt(debt: Debt) = dao.insertDebt(debt)
    suspend fun deleteDebt(debt: Debt) = dao.deleteDebt(debt)

    suspend fun insertSavings(savings: Savings) = dao.insertSavings(savings)
    suspend fun deleteSavings(savings: Savings) = dao.deleteSavings(savings)

    suspend fun insertGoal(goal: Goal) = dao.insertGoal(goal)
    suspend fun deleteGoal(goal: Goal) = dao.deleteGoal(goal)

    suspend fun insertBudget(budget: Budget) = dao.insertBudget(budget)
    suspend fun deleteBudget(budget: Budget) = dao.deleteBudget(budget)

    suspend fun clearAllData() = dao.clearAllData()
}
