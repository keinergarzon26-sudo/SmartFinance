package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.viewmodel.FinanceViewModel
import java.text.SimpleDateFormat
import java.util.*

// ==========================================
// DYNAMIC PALETTE & TEMPLATES (SPANISH)
// ==========================================
data class ThemePalette(
    val nombre: String,
    val primaryText: Color,
    val secondaryText: Color,
    val tertiaryText: Color,
    val background: Color,
    val surface: Color,
    val borderOutline: Color,
    val primaryColor: Color,
    val accentMain: Color,
    val accentBackground: Color,
    val alertRed: Color,
    val alertRedBg: Color,
    val alertGreenBg: Color,
    val warningGold: Color,
    val shadowColor: Color
)

data class AppTemplate(
    val nombre: String,
    val cardShape: androidx.compose.ui.graphics.Shape,
    val hasBorder: Boolean,
    val spacing: androidx.compose.ui.unit.Dp,
    val contentPadding: androidx.compose.ui.unit.Dp,
    val compactFont: Boolean
)

val LocalPalette = staticCompositionLocalOf {
    ThemePalette(
        nombre = "Obsidiana Elegante",
        primaryText = Color(0xFFE6E1E5),
        secondaryText = Color(0xFFCAC4D0),
        tertiaryText = Color(0xFF938F99),
        background = Color(0xFF1C1B1F),
        surface = Color(0xFF2B2930),
        borderOutline = Color(0xFF49454F),
        primaryColor = Color(0xFFB2F0AD),
        accentMain = Color(0xFFD0BCFF),
        accentBackground = Color(0xFF381E72),
        alertRed = Color(0xFFF2B8B5),
        alertRedBg = Color(0xFF4A1E1E),
        alertGreenBg = Color(0xFF2E312E),
        warningGold = Color(0xFFE2CFC4),
        shadowColor = Color.Black.copy(alpha = 0.4f)
    )
}

val LocalTemplate = staticCompositionLocalOf {
    AppTemplate(
        nombre = "Moderna con Bordes",
        cardShape = RoundedCornerShape(16.dp),
        hasBorder = true,
        spacing = 16.dp,
        contentPadding = 20.dp,
        compactFont = false
    )
}

fun getThemePalette(index: Int): ThemePalette {
    return when (index) {
        0 -> ThemePalette(
            nombre = "Obsidiana Elegante",
            primaryText = Color(0xFFE6E1E5),
            secondaryText = Color(0xFFCAC4D0),
            tertiaryText = Color(0xFF938F99),
            background = Color(0xFF1C1B1F),
            surface = Color(0xFF2B2930),
            borderOutline = Color(0xFF49454F),
            primaryColor = Color(0xFFB2F0AD),
            accentMain = Color(0xFFD0BCFF),
            accentBackground = Color(0xFF381E72),
            alertRed = Color(0xFFF2B8B5),
            alertRedBg = Color(0xFF4A1E1E),
            alertGreenBg = Color(0xFF2E312E),
            warningGold = Color(0xFFE2CFC4),
            shadowColor = Color.Black.copy(alpha = 0.4f)
        )
        1 -> ThemePalette(
            nombre = "Esmeralda Calmante",
            primaryText = Color(0xFFE0F2E9),
            secondaryText = Color(0xFFA3CFC0),
            tertiaryText = Color(0xFF719588),
            background = Color(0xFF0F1A15),
            surface = Color(0xFF182821),
            borderOutline = Color(0xFF2C4339),
            primaryColor = Color(0xFF81C784),
            accentMain = Color(0xFF5ED9A8),
            accentBackground = Color(0xFF134832),
            alertRed = Color(0xFFFF8A80),
            alertRedBg = Color(0xFF3D1616),
            alertGreenBg = Color(0xFF1A3326),
            warningGold = Color(0xFFFFD54F),
            shadowColor = Color.Black.copy(alpha = 0.5f)
        )
        2 -> ThemePalette(
            nombre = "Mar Azul Cobalto",
            primaryText = Color(0xFFE1F5FE),
            secondaryText = Color(0xFF90CAF9),
            tertiaryText = Color(0xFF5C6F84),
            background = Color(0xFF08121E),
            surface = Color(0xFF101B2E),
            borderOutline = Color(0xFF1E2F49),
            primaryColor = Color(0xFF80DEEA),
            accentMain = Color(0xFF4FC3F7),
            accentBackground = Color(0xFF1A3B70),
            alertRed = Color(0xFFFFB4AB),
            alertRedBg = Color(0xFF410002),
            alertGreenBg = Color(0xFF0D3325),
            warningGold = Color(0xFFFFCC80),
            shadowColor = Color.Black.copy(alpha = 0.5f)
        )
        else -> ThemePalette(
            nombre = "Oro Rosa & Cobre",
            primaryText = Color(0xFFFBE9E7),
            secondaryText = Color(0xFFEDC9AF),
            tertiaryText = Color(0xFFA8897E),
            background = Color(0xFF1C1310),
            surface = Color(0xFF2C1E1A),
            borderOutline = Color(0xFF4A342B),
            primaryColor = Color(0xFFFFCC80),
            accentMain = Color(0xFFFFAB91),
            accentBackground = Color(0xFF5D2D20),
            alertRed = Color(0xFFFF8A80),
            alertRedBg = Color(0xFF441818),
            alertGreenBg = Color(0xFF263A20),
            warningGold = Color(0xFFFFF59D),
            shadowColor = Color.Black.copy(alpha = 0.4f)
        )
    }
}

fun getAppTemplate(index: Int): AppTemplate {
    return when (index) {
        0 -> AppTemplate(
            nombre = "Moderna con Bordes",
            cardShape = RoundedCornerShape(16.dp),
            hasBorder = true,
            spacing = 16.dp,
            contentPadding = 20.dp,
            compactFont = false
        )
        1 -> AppTemplate(
            nombre = "Plana Minimalista",
            cardShape = RoundedCornerShape(12.dp),
            hasBorder = false,
            spacing = 16.dp,
            contentPadding = 20.dp,
            compactFont = false
        )
        else -> AppTemplate(
            nombre = "Compacto y Denso",
            cardShape = RoundedCornerShape(8.dp),
            hasBorder = true,
            spacing = 8.dp,
            contentPadding = 12.dp,
            compactFont = true
        )
    }
}

// Global dynamic properties resolved using Composable CompositionLocal getters
val EmeraldPrimary: Color @Composable get() = LocalPalette.current.primaryColor
val EmeraldDark: Color @Composable get() = LocalPalette.current.accentMain
val EmeraldLight: Color @Composable get() = LocalPalette.current.accentBackground
val CrimsonAlert: Color @Composable get() = LocalPalette.current.alertRed
val GoldWarning: Color @Composable get() = LocalPalette.current.warningGold
val SapphireBlue: Color @Composable get() = Color(0xFF8AB4F8)
val SoftGrayBackground: Color @Composable get() = LocalPalette.current.background
val CardBorderColor: Color @Composable get() = LocalPalette.current.borderOutline

val ElegantSurface: Color @Composable get() = LocalPalette.current.surface
val ElegantTextPrimary: Color @Composable get() = LocalPalette.current.primaryText
val ElegantTextSecondary: Color @Composable get() = LocalPalette.current.secondaryText
val ElegantTextTertiary: Color @Composable get() = LocalPalette.current.tertiaryText
val ElegantBorderOutline: Color @Composable get() = LocalPalette.current.borderOutline
val ElegantRedBg: Color @Composable get() = LocalPalette.current.alertRedBg
val ElegantGreenBg: Color @Composable get() = LocalPalette.current.alertGreenBg

val CardShape: androidx.compose.ui.graphics.Shape @Composable get() = LocalTemplate.current.cardShape
val ElementSpacing: androidx.compose.ui.unit.Dp @Composable get() = LocalTemplate.current.spacing
val ElementPadding: androidx.compose.ui.unit.Dp @Composable get() = LocalTemplate.current.contentPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceAppScreen(viewModel: FinanceViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val themeIndex by viewModel.themeIndex.collectAsStateWithLifecycle()
    val templateIndex by viewModel.templateIndex.collectAsStateWithLifecycle()

    val currentPalette = getThemePalette(themeIndex)
    val currentTemplate = getAppTemplate(templateIndex)

    val incomes by viewModel.incomes.collectAsStateWithLifecycle()
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val debts by viewModel.debts.collectAsStateWithLifecycle()
    val savings by viewModel.savings.collectAsStateWithLifecycle()
    val goals by viewModel.goals.collectAsStateWithLifecycle()
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()

    // Dialog Toggle States
    var showAddIncome by remember { mutableStateOf(false) }
    var showAddExpense by remember { mutableStateOf(false) }
    var showAddDebt by remember { mutableStateOf(false) }
    var showAddSavings by remember { mutableStateOf(false) }
    var showAddGoal by remember { mutableStateOf(false) }
    var showAddBudget by remember { mutableStateOf(false) }
    var showResetConfirmation by remember { mutableStateOf(false) }
    var showDesignSettings by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalPalette provides currentPalette,
        LocalTemplate provides currentTemplate
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = SoftGrayBackground,
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = "Logo de Finanzas",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "SmartFinance",
                                fontWeight = FontWeight.Bold,
                                color = ElegantTextPrimary,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ElegantSurface,
                        titleContentColor = ElegantTextPrimary
                    ),
                    actions = {
                        IconButton(
                            onClick = {
                                showDesignSettings = true
                            },
                            modifier = Modifier.testTag("action_design_settings_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Configurar Diseño",
                                tint = EmeraldDark
                            )
                        }
                        IconButton(
                            onClick = {
                                showResetConfirmation = true
                            },
                            modifier = Modifier.testTag("action_reset_all_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Resetear todo",
                                tint = CrimsonAlert
                            )
                        }
                    IconButton(
                        onClick = {
                            // Quick button to register mock sample dataset
                        },
                        modifier = Modifier.testTag("app_info_btn")
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "App Information", tint = ElegantTextSecondary)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = ElegantSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = { Icon(if (currentTab == 0) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldDark,
                        selectedTextColor = EmeraldDark,
                        indicatorColor = EmeraldLight,
                        unselectedIconColor = ElegantTextTertiary,
                        unselectedTextColor = ElegantTextTertiary
                    ),
                    modifier = Modifier.testTag("nav_tab_dashboard")
                )
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = { Icon(if (currentTab == 1) Icons.Filled.List else Icons.Outlined.List, contentDescription = "Registros") },
                    label = { Text("Registros", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldDark,
                        selectedTextColor = EmeraldDark,
                        indicatorColor = EmeraldLight,
                        unselectedIconColor = ElegantTextTertiary,
                        unselectedTextColor = ElegantTextTertiary
                    ),
                    modifier = Modifier.testTag("nav_tab_trackers")
                )
                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = { Icon(if (currentTab == 2) Icons.Filled.PieChart else Icons.Outlined.PieChart, contentDescription = "Presupuestos & Metas") },
                    label = { Text("Presupuestos", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldDark,
                        selectedTextColor = EmeraldDark,
                        indicatorColor = EmeraldLight,
                        unselectedIconColor = ElegantTextTertiary,
                        unselectedTextColor = ElegantTextTertiary
                    ),
                    modifier = Modifier.testTag("nav_tab_budgets")
                )
                NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { viewModel.selectTab(3) },
                    icon = { Icon(if (currentTab == 3) Icons.Filled.Insights else Icons.Outlined.Insights, contentDescription = "Indicadores") },
                    label = { Text("Indicadores", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldDark,
                        selectedTextColor = EmeraldDark,
                        indicatorColor = EmeraldLight,
                        unselectedIconColor = ElegantTextTertiary,
                        unselectedTextColor = ElegantTextTertiary
                    ),
                    modifier = Modifier.testTag("nav_tab_indicators")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    fadeIn(spring()) togetherWith fadeOut(spring())
                },
                label = "ScreenTransition"
            ) { targetTab ->
                when (targetTab) {
                    0 -> DashboardView(
                        incomes = incomes,
                        expenses = expenses,
                        debts = debts,
                        savings = savings
                    )
                    1 -> TrackersView(
                        incomes = incomes,
                        expenses = expenses,
                        debts = debts,
                        savings = savings,
                        onDeleteIncome = { viewModel.deleteIncome(it) },
                        onDeleteExpense = { viewModel.deleteExpense(it) },
                        onDeleteDebt = { viewModel.deleteDebt(it) },
                        onDeleteSavings = { viewModel.deleteSavings(it) },
                        onAddIncomeClick = { showAddIncome = true },
                        onAddExpenseClick = { showAddExpense = true },
                        onAddDebtClick = { showAddDebt = true },
                        onAddSavingsClick = { showAddSavings = true }
                    )
                    2 -> BudgetsGoalsView(
                        expenses = expenses,
                        budgets = budgets,
                        goals = goals,
                        onAddBudgetClick = { showAddBudget = true },
                        onAddGoalClick = { showAddGoal = true },
                        onDeleteBudget = { viewModel.deleteBudget(it) },
                        onDeleteGoal = { viewModel.deleteGoal(it) }
                    )
                    3 -> IndicatorsView(
                        incomes = incomes,
                        expenses = expenses,
                        debts = debts,
                        savings = savings
                    )
                }
            }
        }
    }

    // ==========================================
    // ACTION DIALOGS
    // ==========================================

    if (showAddIncome) {
        AddIncomeDialog(
            onDismiss = { showAddIncome = false },
            onSave = { source, amount, cat ->
                viewModel.addIncome(source, amount, cat)
                showAddIncome = false
            }
        )
    }

    if (showAddExpense) {
        AddExpenseDialog(
            onDismiss = { showAddExpense = false },
            onSave = { title, amount, cat ->
                viewModel.addExpense(title, amount, cat)
                showAddExpense = false
            }
        )
    }

    if (showAddDebt) {
        AddDebtDialog(
            onDismiss = { showAddDebt = false },
            onSave = { creditor, original, current, rate, due, minPay ->
                viewModel.addDebt(creditor, original, current, rate, due, minPay)
                showAddDebt = false
            }
        )
    }

    if (showAddSavings) {
        AddSavingsDialog(
            onDismiss = { showAddSavings = false },
            onSave = { desc, amount ->
                viewModel.addSavings(desc, amount)
                showAddSavings = false
            }
        )
    }

    if (showAddGoal) {
        AddGoalDialog(
            onDismiss = { showAddGoal = false },
            onSave = { name, target, current, date ->
                viewModel.addGoal(name, target, current, date)
                showAddGoal = false
            }
        )
    }

    if (showAddBudget) {
        AddBudgetDialog(
            onDismiss = { showAddBudget = false },
            onSave = { category, amount ->
                viewModel.addBudget(category, amount)
                showAddBudget = false
            }
        )
    }

    if (showDesignSettings) {
        DesignSettingsDialog(
            onDismiss = { showDesignSettings = false },
            themeIndex = themeIndex,
            onThemeSelected = { viewModel.setThemeIndex(it) },
            templateIndex = templateIndex,
            onTemplateSelected = { viewModel.setTemplateIndex(it) }
        )
    }

    if (showResetConfirmation) {
        AlertDialog(
            onDismissRequest = { showResetConfirmation = false },
            title = {
                Text(
                    text = "Confirmar Reinicio",
                    fontWeight = FontWeight.Bold,
                    color = CrimsonAlert
                )
            },
            text = {
                Text(
                    text = "¿Estás totalmente seguro de que deseas eliminar TODOS los datos financieros de la aplicación? Esto borrará tus ingresos, gastos, ahorros, deudas, metas y presupuestos actuales de forma permanente.",
                    color = ElegantTextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllData()
                        showResetConfirmation = false
                    },
                    modifier = Modifier.testTag("confirm_reset_btn")
                ) {
                    Text("Borrar Todo", color = CrimsonAlert, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showResetConfirmation = false },
                    modifier = Modifier.testTag("cancel_reset_btn")
                ) {
                    Text("Cancelar", color = ElegantTextSecondary)
                }
            },
            containerColor = ElegantSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }
    }
}

// ==========================================
// VIEW 1: DYNAMIC FINANCIAL DASHBOARD
// ==========================================
@Composable
fun DashboardView(
    incomes: List<Income>,
    expenses: List<Expense>,
    debts: List<Debt>,
    savings: List<Savings>
) {
    val totalIncomes = incomes.sumOf { it.amount }
    val totalExpenses = expenses.sumOf { it.amount }
    val totalDebts = debts.sumOf { it.currentAmount }
    val totalSavings = savings.sumOf { it.amount }

    // Net Worth = Assets (CashBalance + Savings) - Liabilities (Debts)
    // CashBalance = TotalIncomes - TotalExpenses
    val netCashFlowBalance = totalIncomes - totalExpenses
    val totalAssets = netCashFlowBalance + totalSavings
    val totalLiabilities = totalDebts
    val calculatedNetWorth = totalAssets - totalLiabilities

    // Savings to Income %
    val savingsToIncomePercent = if (totalIncomes > 0) {
        (totalSavings / totalIncomes) * 100
    } else {
        0.0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(ElementPadding),
        verticalArrangement = Arrangement.spacedBy(ElementSpacing)
    ) {
        // Welcoming card
        Text(
            text = "Panel Financiero",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = ElegantTextPrimary
        )

        // HERO CARD: Automatic Net Worth Calculation
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("net_worth_hero_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ElegantSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(EmeraldLight, ElegantSurface)
                        )
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = "PATRIMONIO NETO ESTIMADO",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$${String.format(Locale.US, "%,.2f", calculatedNetWorth)}",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Activos (Efectivo y Ahorros)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$${String.format(Locale.US, "%,.1f", totalAssets)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Pasivos (Deuda Pendiente)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$${String.format(Locale.US, "%,.1f", totalLiabilities)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Mini panels for cashflows
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ElegantSurface),
                shape = RoundedCornerShape(14.dp),
                border = BoxBorder()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EmeraldLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.TrendingUp, contentDescription = "Ingresos", tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Ingresos", style = MaterialTheme.typography.bodySmall, color = ElegantTextSecondary)
                        Text("$${String.format(Locale.US, "%,.0f", totalIncomes)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)
                    }
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ElegantSurface),
                shape = RoundedCornerShape(14.dp),
                border = BoxBorder()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ElegantRedBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.TrendingDown, contentDescription = "Gastos", tint = CrimsonAlert, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Gastos", style = MaterialTheme.typography.bodySmall, color = ElegantTextSecondary)
                        Text("$${String.format(Locale.US, "%,.0f", totalExpenses)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)
                    }
                }
            }
        }

        // Savings to Income Ratio alert card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ElegantSurface),
            shape = RoundedCornerShape(14.dp),
            border = BoxBorder()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(0.7f)) {
                    Text(
                        text = "AHORRO S/ INGRESOS",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElegantTextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${String.format(Locale.US, "%.1f", savingsToIncomePercent)}%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (savingsToIncomePercent >= 20.0) EmeraldPrimary else GoldWarning
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (savingsToIncomePercent >= 20.0) "¡Excelente! Cumple con la regla del 20% de ahorro." else "Sugerencia: intenta reservar algo más para alcanzar el 20%.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ElegantTextSecondary
                    )
                }
                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(0.3f)) {
                    CircularProgressIndicator(
                        progress = { (savingsToIncomePercent / 100.0).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier.size(60.dp),
                        color = if (savingsToIncomePercent >= 20.0) EmeraldPrimary else GoldWarning,
                        strokeWidth = 6.dp,
                        trackColor = ElegantBorderOutline
                    )
                    Icon(
                        imageVector = if (savingsToIncomePercent >= 20.0) Icons.Default.CheckCircle else Icons.Default.OfflineBolt,
                        contentDescription = "Ratio icon",
                        tint = if (savingsToIncomePercent >= 20.0) EmeraldPrimary else GoldWarning,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // GRAPH PANEL: Income vs Expenses & Capital Distribution
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ElegantSurface),
            shape = RoundedCornerShape(16.dp),
            border = BoxBorder()
        ) {
            var selectedChartTab by remember { mutableStateOf(0) } // 0: Distribution, 1: Comparisons

            Column(modifier = Modifier.padding(16.dp)) {
                // Secondary Tab Header
                TabRow(
                    selectedTabIndex = selectedChartTab,
                    containerColor = Color.Transparent,
                    contentColor = EmeraldPrimary,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            color = EmeraldPrimary,
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedChartTab])
                        )
                    }
                ) {
                    Tab(
                        selected = selectedChartTab == 0,
                        onClick = { selectedChartTab = 0 },
                        text = { Text("Distribución de Capital", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedChartTab == 1,
                        onClick = { selectedChartTab = 1 },
                        text = { Text("Flujo de Caja", fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (selectedChartTab == 0) {
                    // DOUGHNUT PIE CHART
                    Text(
                        text = "DISTRIBUCIÓN DEL CAPITAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElegantTextSecondary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val chartItems = mutableListOf<Pair<String, Double>>()
                    if (netCashFlowBalance > 0) chartItems.add("Efectivo" to netCashFlowBalance)
                    if (totalSavings > 0) chartItems.add("Ahorros" to totalSavings)
                    if (totalDebts > 0) chartItems.add("Deudas" to totalDebts)

                    if (chartItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se han registrado transacciones aún.", style = MaterialTheme.typography.bodyMedium, color = ElegantTextSecondary)
                        }
                    } else {
                        val pieColors = listOf(EmeraldPrimary, SapphireBlue, CrimsonAlert)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            DonutChart(
                                items = chartItems,
                                colors = pieColors,
                                modifier = Modifier.size(130.dp)
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                chartItems.forEachIndexed { index, pair ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(pieColors[index % pieColors.size])
                                        )
                                        Column {
                                            Text(pair.first, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)
                                            Text(
                                                text = "$${String.format(Locale.US, "%,.0f", pair.second)}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = ElegantTextSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // COMPARISON BAR CHART (Income vs Expenses)
                    Column {
                        Text(
                            text = "COMPARACIÓN MENSUAL DE CAJA",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantTextSecondary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        ComparisonBarChart(
                            incomeVal = totalIncomes,
                            expenseVal = totalExpenses,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                        )
                    }
                }
            }
        }
    }
}

// Custom Doughnut Chart Composable
@Composable
fun DonutChart(
    items: List<Pair<String, Double>>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val total = items.sumOf { it.second }.toFloat()

    Canvas(modifier = modifier) {
        if (total > 0f) {
            val diameter = size.minDimension
            val radius = diameter / 2
            val rect = Rect(center - Offset(radius, radius), Size(diameter, diameter))
            var startAngle = -90f

            items.forEachIndexed { index, item ->
                val sweepAngle = ((item.second / total) * 360f).toFloat()
                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 32f, cap = StrokeCap.Round),
                    size = rect.size,
                    topLeft = rect.topLeft
                )
                startAngle += sweepAngle
            }
        } else {
            drawCircle(color = Color.LightGray, style = Stroke(width = 24f))
        }
    }
}

// Side-by-Side Rounded Comparison Bar Chart
@Composable
fun ComparisonBarChart(
    incomeVal: Double,
    expenseVal: Double,
    modifier: Modifier = Modifier
) {
    val maxVal = maxOf(incomeVal, expenseVal, 1.0)

    val incomeRatio = (incomeVal / maxVal).toFloat()
    val expenseRatio = (expenseVal / maxVal).toFloat()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // Income Bar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(75.dp)
        ) {
            Text(
                text = "$${String.format(Locale.US, "%,.0f", incomeVal)}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxHeight(fraction = incomeRatio.coerceIn(0.05f, 1f))
                    .width(42.dp)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(EmeraldPrimary, EmeraldDark)
                        )
                    )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text("Inflow", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.Gray)
        }

        // Expense Bar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(75.dp)
        ) {
            Text(
                text = "$${String.format(Locale.US, "%,.0f", expenseVal)}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = CrimsonAlert
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxHeight(fraction = expenseRatio.coerceIn(0.05f, 1f))
                    .width(42.dp)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFFCA5A5), CrimsonAlert)
                        )
                    )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text("Gastos", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ElegantTextSecondary)
        }
    }
}

// ==========================================
// VIEW 2: MULTI-TAB REGISTER TRACKERS
// ==========================================
@Composable
fun TrackersView(
    incomes: List<Income>,
    expenses: List<Expense>,
    debts: List<Debt>,
    savings: List<Savings>,
    onDeleteIncome: (Income) -> Unit,
    onDeleteExpense: (Expense) -> Unit,
    onDeleteDebt: (Debt) -> Unit,
    onDeleteSavings: (Savings) -> Unit,
    onAddIncomeClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    onAddDebtClick: () -> Unit,
    onAddSavingsClick: () -> Unit
) {
    var trackerTab by remember { mutableStateOf(0) } // 0: Income, 1: Expenses, 2: Debts, 3: Savings

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(ElementPadding),
        verticalArrangement = Arrangement.spacedBy(ElementSpacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Registros Financieros",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = ElegantTextPrimary
            )

            Button(
                onClick = {
                    when (trackerTab) {
                        0 -> onAddIncomeClick()
                        1 -> onAddExpenseClick()
                        2 -> onAddDebtClick()
                        3 -> onAddSavingsClick()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("action_add_transaction_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar elemento", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = when (trackerTab) {
                        0 -> "Ingreso"
                        1 -> "Gasto"
                        2 -> "Deuda"
                        3 -> "Ahorro"
                        else -> "Agregar"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Sub tracker view selective tabs
        ScrollableTabRow(
            selectedTabIndex = trackerTab,
            containerColor = Color.Transparent,
            contentColor = EmeraldPrimary,
            edgePadding = 0.dp,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    color = EmeraldDark,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[trackerTab])
                )
            }
        ) {
            Tab(selected = trackerTab == 0, onClick = { trackerTab = 0 }) {
                Text("Ingresos (${incomes.size})", modifier = Modifier.padding(vertical = 12.dp), fontWeight = FontWeight.Bold, color = if (trackerTab == 0) EmeraldDark else ElegantTextSecondary)
            }
            Tab(selected = trackerTab == 1, onClick = { trackerTab = 1 }) {
                Text("Gastos (${expenses.size})", modifier = Modifier.padding(vertical = 12.dp), fontWeight = FontWeight.Bold, color = if (trackerTab == 1) EmeraldDark else ElegantTextSecondary)
            }
            Tab(selected = trackerTab == 2, onClick = { trackerTab = 2 }) {
                Text("Deudas (${debts.size})", modifier = Modifier.padding(vertical = 12.dp), fontWeight = FontWeight.Bold, color = if (trackerTab == 2) EmeraldDark else ElegantTextSecondary)
            }
            Tab(selected = trackerTab == 3, onClick = { trackerTab = 3 }) {
                Text("Ahorros (${savings.size})", modifier = Modifier.padding(vertical = 12.dp), fontWeight = FontWeight.Bold, color = if (trackerTab == 3) EmeraldDark else ElegantTextSecondary)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (trackerTab) {
                0 -> {
                    if (incomes.isEmpty()) {
                        EmptyListPlaceholder("Sin ingresos registrados aún", "Toca el botón superior para agregar tu salario o proyectos independientes.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(incomes) { item ->
                                ItemCard(
                                    title = item.source,
                                    subtitle = item.category,
                                    amount = item.amount,
                                    dateVal = item.date,
                                    isPositive = true,
                                    onDeleteClick = { onDeleteIncome(item) },
                                    tag = "income_card_${item.id}"
                                )
                            }
                        }
                    }
                }
                1 -> {
                    if (expenses.isEmpty()) {
                        EmptyListPlaceholder("Sin gastos registrados", "Registra tus gastos como comida, transporte o alquiler.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(expenses) { item ->
                                ItemCard(
                                    title = item.title,
                                    subtitle = item.category,
                                    amount = item.amount,
                                    dateVal = item.date,
                                    isPositive = false,
                                    onDeleteClick = { onDeleteExpense(item) },
                                    tag = "expense_card_${item.id}"
                                )
                            }
                        }
                    }
                }
                2 -> {
                    if (debts.isEmpty()) {
                        EmptyListPlaceholder("Sin deudas registradas", "¡Excelente! O agrega tus préstamos o saldos de tarjetas de crédito.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(debts) { item ->
                                DebtListItem(
                                    debt = item,
                                    onDeleteClick = { onDeleteDebt(item) }
                                )
                            }
                        }
                    }
                }
                3 -> {
                    if (savings.isEmpty()) {
                        EmptyListPlaceholder("Sin depósitos de ahorro", "Comienza a ahorrar para tu fondo de emergencia o metas de viaje.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(savings) { item ->
                                ItemCard(
                                    title = item.description,
                                    subtitle = "Reserva de Ahorro",
                                    amount = item.amount,
                                    dateVal = item.date,
                                    isPositive = true,
                                    onDeleteClick = { onDeleteSavings(item) },
                                    tag = "savings_card_${item.id}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Reusable list item card representing transactions
@Composable
fun ItemCard(
    title: String,
    subtitle: String,
    amount: Double,
    dateVal: Long,
    isPositive: Boolean,
    onDeleteClick: () -> Unit,
    tag: String = ""
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag),
        colors = CardDefaults.cardColors(containerColor = ElegantSurface),
        shape = RoundedCornerShape(12.dp),
        border = BoxBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.8f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isPositive) EmeraldLight else ElegantRedBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPositive) Icons.Default.AddCard else Icons.Default.Receipt,
                        contentDescription = "Wallet Category",
                        tint = if (isPositive) EmeraldPrimary else CrimsonAlert,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = ElegantTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = ElegantTextSecondary)
                        Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(ElegantTextSecondary))
                        Text(formatLongDate(dateVal), style = MaterialTheme.typography.bodySmall, color = ElegantTextSecondary)
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(0.25f, fill = false)
            ) {
                Text(
                    text = "${if (isPositive) "+" else "-"}$${String.format(Locale.US, "%,.0f", amount)}",
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isPositive) EmeraldPrimary else CrimsonAlert,
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = onDeleteClick, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete entry", tint = ElegantTextSecondary, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

// Special list item layout for active Outstanding Debt accounts
@Composable
fun DebtListItem(
    debt: Debt,
    onDeleteClick: () -> Unit
) {
    val progressRatio = if (debt.originalAmount > 0) {
        (debt.currentAmount / debt.originalAmount).toFloat()
    } else {
        0f
    }
    val paidPercent = ((1f - progressRatio) * 100f).coerceIn(0f, 100f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ElegantSurface),
        shape = RoundedCornerShape(12.dp),
        border = BoxBorder()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3B331F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.AccountBalance, contentDescription = "Debt Owed", tint = GoldWarning, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(debt.creditor, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)
                        Text("Rate: ${debt.interestRate}% Int. Rate", style = MaterialTheme.typography.bodySmall, color = ElegantTextSecondary)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "$${String.format(Locale.US, "%,.0f", debt.currentAmount)}",
                            fontWeight = FontWeight.Bold,
                            color = ElegantTextPrimary
                        )
                        Text(
                            text = "Owed of $${String.format(Locale.US, "%,.0f", debt.originalAmount)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ElegantTextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete debt", tint = ElegantTextTertiary, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pagado: ${String.format(Locale.US, "%.1f", paidPercent)}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Pago Mín: $${String.format(Locale.US, "%.0f", debt.monthlyPayment)}/mes (Vence: ${debt.dueDate})",
                    style = MaterialTheme.typography.bodySmall,
                    color = ElegantTextSecondary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { (1f - progressRatio).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = EmeraldPrimary,
                trackColor = ElegantBorderOutline
            )
        }
    }
}

// Empty state placeholder helper
@Composable
fun EmptyListPlaceholder(message: String, helper: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Vacío",
                modifier = Modifier.size(46.dp),
                tint = ElegantTextTertiary
            )
            Text(message, fontWeight = FontWeight.Bold, color = ElegantTextSecondary, style = MaterialTheme.typography.bodyLarge)
            Text(helper, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall, color = ElegantTextTertiary, modifier = Modifier.padding(horizontal = 24.dp))
        }
    }
}

// ==========================================
// VIEW 3: MONTHLY BUDGET SHEET & FINANCIAL GOALS
// ==========================================
@Composable
fun BudgetsGoalsView(
    expenses: List<Expense>,
    budgets: List<Budget>,
    goals: List<Goal>,
    onAddBudgetClick: () -> Unit,
    onAddGoalClick: () -> Unit,
    onDeleteBudget: (Budget) -> Unit,
    onDeleteGoal: (Goal) -> Unit
) {
    var sectionTab by remember { mutableStateOf(0) } // 0: Budgets, 1: Goals

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(ElementPadding),
        verticalArrangement = Arrangement.spacedBy(ElementSpacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Presupuestos & Metas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = ElegantTextPrimary
            )

            Button(
                onClick = { if (sectionTab == 0) onAddBudgetClick() else onAddGoalClick() },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("action_add_budget_goal_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (sectionTab == 0) "Límite" else "Nueva Meta",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        TabRow(
            selectedTabIndex = sectionTab,
            containerColor = Color.Transparent,
            contentColor = EmeraldPrimary,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    color = EmeraldDark,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[sectionTab])
                )
            }
        ) {
            Tab(selected = sectionTab == 0, onClick = { sectionTab = 0 }) {
                Text("Presupuesto Mensual", modifier = Modifier.padding(vertical = 12.dp), fontWeight = FontWeight.Bold, color = if (sectionTab == 0) EmeraldDark else ElegantTextSecondary)
            }
            Tab(selected = sectionTab == 1, onClick = { sectionTab = 1 }) {
                Text("Metas Financieras", modifier = Modifier.padding(vertical = 12.dp), fontWeight = FontWeight.Bold, color = if (sectionTab == 1) EmeraldDark else ElegantTextSecondary)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (sectionTab == 0) {
                // ==========================
                // MONTHLY BUDGET SHEET
                // ==========================
                if (budgets.isEmpty()) {
                    EmptyListPlaceholder("Sin presupuestos activos establecidos", "Crea límites de gasto para cada categoría para mantener tu salud financiera.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(budgets) { budget ->
                            // Calculate current total actual spend for this category
                            val isMatchedExpenseSum = expenses
                                .filter { it.category.lowercase() == budget.category.lowercase() }
                                .sumOf { it.amount }

                            val fillPercent = if (budget.limitAmount > 0) {
                                (isMatchedExpenseSum / budget.limitAmount).toFloat()
                            } else {
                                0f
                            }
                            val isOverBudget = isMatchedExpenseSum > budget.limitAmount

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = ElegantSurface),
                                shape = RoundedCornerShape(12.dp),
                                border = BoxBorder()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Text(budget.category, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)
                                                if (isOverBudget) {
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(ElegantRedBg)
                                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                                    ) {
                                                        Text("EXCEDE EL LÍMITE", style = MaterialTheme.typography.labelSmall, color = CrimsonAlert, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                            Text(
                                                text = "Gastado: $${String.format(Locale.US, "%,.0f", isMatchedExpenseSum)} de $${String.format(Locale.US, "%,.0f", budget.limitAmount)}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = ElegantTextSecondary
                                            )
                                        }

                                        IconButton(onClick = { onDeleteBudget(budget) }, modifier = Modifier.size(28.dp)) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar presupuesto", tint = ElegantTextSecondary, modifier = Modifier.size(18.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    LinearProgressIndicator(
                                        progress = { fillPercent.coerceIn(0f, 1f) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(CircleShape),
                                        color = if (isOverBudget) CrimsonAlert else EmeraldPrimary,
                                        trackColor = ElegantBorderOutline
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "${String.format(Locale.US, "%.0f", fillPercent * 100)}% Consumido",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isOverBudget) CrimsonAlert else ElegantTextSecondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (isOverBudget) "Exceso: $${String.format(Locale.US, "%,.0f", isMatchedExpenseSum - budget.limitAmount)}" else "Restante: $${String.format(Locale.US, "%,.0f", budget.limitAmount - isMatchedExpenseSum)}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isOverBudget) CrimsonAlert else EmeraldDark
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // ==========================
                // FINANCIAL GOALS Tracker
                // ==========================
                if (goals.isEmpty()) {
                    EmptyListPlaceholder("Sin metas financieras declaradas", "Establece tus objetivos de ahorro para vacaciones, tecnología o reservas.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(goals) { goal ->
                            val achievementPercent = if (goal.targetAmount > 0) {
                                (goal.currentAmount / goal.targetAmount) * 100
                            } else {
                                0.0
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = ElegantSurface),
                                shape = RoundedCornerShape(12.dp),
                                border = BoxBorder()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(ElegantGreenBg),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Filled.Flag, contentDescription = "Meta de Logro", tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(goal.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)
                                                Text("Fecha Límite: ${goal.targetDate}", style = MaterialTheme.typography.bodySmall, color = ElegantTextSecondary)
                                            }
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Column(horizontalAlignment = Alignment.End) {
                                                Text("$${String.format(Locale.US, "%,.0f", goal.currentAmount)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.ExtraBold, color = ElegantTextPrimary)
                                                Text("Meta: $${String.format(Locale.US, "%,.0f", goal.targetAmount)}", style = MaterialTheme.typography.bodySmall, color = ElegantTextSecondary)
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            IconButton(onClick = { onDeleteGoal(goal) }, modifier = Modifier.size(28.dp)) {
                                                Icon(Icons.Default.Delete, contentDescription = "Eliminar meta", tint = ElegantTextSecondary, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(
                                            text = "Porcentaje de logro: ${String.format(Locale.US, "%.1f", achievementPercent)}%",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldPrimary
                                        )
                                        Text(
                                            text = if (achievementPercent >= 100.0) "¡COMPLETADO!" else "Falta: $${String.format(Locale.US, "%,.0f", goal.targetAmount - goal.currentAmount)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (achievementPercent >= 100.0) EmeraldPrimary else ElegantTextSecondary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LinearProgressIndicator(
                                        progress = { (achievementPercent / 100.0).toFloat().coerceIn(0f, 1f) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(CircleShape),
                                        color = EmeraldPrimary,
                                        trackColor = ElegantBorderOutline
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// VIEW 4: FINANCIAL HEALTH INDICATORS SHEET
// ==========================================
@Composable
fun IndicatorsView(
    incomes: List<Income>,
    expenses: List<Expense>,
    debts: List<Debt>,
    savings: List<Savings>
) {
    val totalIncomes = incomes.sumOf { it.amount }
    val totalExpenses = expenses.sumOf { it.amount }
    val totalDebts = debts.sumOf { it.currentAmount }
    val totalSavings = savings.sumOf { it.amount }

    val monthlyDebtPayment = debts.sumOf { it.monthlyPayment }

    // Ratios Calculations safely
    val savingsRate = if (totalIncomes > 0) (totalSavings / totalIncomes) * 100 else 0.0
    val dtiRatio = if (totalIncomes > 0) (monthlyDebtPayment / totalIncomes) * 100 else 0.0
    val burnRate = if (totalIncomes > 0) (totalExpenses / totalIncomes) * 100 else 0.0

    // Emergency Fund multiplier
    val averageMonthlyExpense = if (expenses.isNotEmpty()) {
        totalExpenses // For dummy model, let's take cumulative registered outflow as monthly index
    } else {
        1000.0
    }
    val emergencyMonthsCovered = if (averageMonthlyExpense > 0) totalSavings / averageMonthlyExpense else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(ElementPadding),
        verticalArrangement = Arrangement.spacedBy(ElementSpacing)
    ) {
        Text(
            text = "Indicadores Financieros",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = ElegantTextPrimary
        )

        Text(
            text = "Métricas automáticas que evalúan tus patrones financieros frente a directrices de salud económica.",
            style = MaterialTheme.typography.bodyMedium,
            color = ElegantTextSecondary
        )

        // Indicator 1: Savings Rate
        IndicatorCard(
            title = "Indicador de Tasa de Ahorro",
            metricValue = "${String.format(Locale.US, "%.1f", savingsRate)}%",
            status = when {
                savingsRate >= 20.0 -> IndicatorStatus.GREEN
                savingsRate >= 10.0 -> IndicatorStatus.YELLOW
                else -> IndicatorStatus.RED
            },
            healthyBenchmark = "Meta Ideal: > 20.0%",
            description = "La tasa de ahorro mide la proporción de tus ingresos mensuales que destinas al ahorro. Mantener un 20% o más asegura una rápida construcción de patrimonio.",
            icon = Icons.Default.Savings
        )

        // Indicator 2: Debt-to-Income
        IndicatorCard(
            title = "Relación Deuda-Ingreso (DTI)",
            metricValue = "${String.format(Locale.US, "%.1f", dtiRatio)}%",
            status = when {
                dtiRatio == 0.0 -> IndicatorStatus.GREEN
                dtiRatio <= 36.0 -> IndicatorStatus.GREEN
                dtiRatio <= 43.0 -> IndicatorStatus.YELLOW
                else -> IndicatorStatus.RED
            },
            healthyBenchmark = "Límite Saludable: < 36.0%",
            description = "Calcula qué porcentaje de tus ingresos se destina al pago de deudas o tarjetas de crédito. Los niveles superiores al 43% se consideran altamente críticos.",
            icon = Icons.Default.Percent
        )

        // Indicator 3: Emergency Fund Progress
        IndicatorCard(
            title = "Cobertura del Fondo de Emergencias",
            metricValue = "${String.format(Locale.US, "%.1f", emergencyMonthsCovered)} meses",
            status = when {
                emergencyMonthsCovered >= 6.0 -> IndicatorStatus.GREEN
                emergencyMonthsCovered >= 3.0 -> IndicatorStatus.YELLOW
                else -> IndicatorStatus.RED
            },
            healthyBenchmark = "Meta de Estabilidad: 3 - 6 meses",
            description = "Calcula cuántos meses podrías cubrir tus gastos actuales únicamente con tus ahorros disponibles en caso de pérdida de ingresos.",
            icon = Icons.Default.Shield
        )

        // Indicator 4: Burn Rate Indicator
        IndicatorCard(
            title = "Tasa de Consumo de Capital",
            metricValue = "${String.format(Locale.US, "%.1f", burnRate)}%",
            status = when {
                burnRate <= 80.0 -> IndicatorStatus.GREEN
                burnRate <= 100.0 -> IndicatorStatus.YELLOW
                else -> IndicatorStatus.RED
            },
            healthyBenchmark = "Límite Seguro: < 80.0%",
            description = "Representa la proporción de tus ingresos que se gasta mensualmente. Superar el 100% significa que estás gastando por encima de tus posibilidades de ingresos primarios.",
            icon = Icons.Default.PriceChange
        )
    }
}

enum class IndicatorStatus { GREEN, YELLOW, RED }

@Composable
fun IndicatorCard(
    title: String,
    metricValue: String,
    status: IndicatorStatus,
    healthyBenchmark: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ElegantSurface),
        shape = RoundedCornerShape(14.dp),
        border = BoxBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                when (status) {
                                    IndicatorStatus.GREEN -> EmeraldLight
                                    IndicatorStatus.YELLOW -> Color(0xFF3B331F)
                                    IndicatorStatus.RED -> ElegantRedBg
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "Icono de indicador",
                            tint = when (status) {
                                IndicatorStatus.GREEN -> EmeraldPrimary
                                IndicatorStatus.YELLOW -> GoldWarning
                                IndicatorStatus.RED -> CrimsonAlert
                             },
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = ElegantTextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when (status) {
                                IndicatorStatus.GREEN -> EmeraldLight
                                IndicatorStatus.YELLOW -> Color(0xFF3B331F)
                                IndicatorStatus.RED -> ElegantRedBg
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (status) {
                            IndicatorStatus.GREEN -> "SALUDABLE"
                            IndicatorStatus.YELLOW -> "ATENCIÓN"
                            IndicatorStatus.RED -> "CRÍTICO"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (status) {
                            IndicatorStatus.GREEN -> EmeraldPrimary
                            IndicatorStatus.YELLOW -> GoldWarning
                            IndicatorStatus.RED -> CrimsonAlert
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = metricValue,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = when (status) {
                        IndicatorStatus.GREEN -> EmeraldPrimary
                        IndicatorStatus.YELLOW -> GoldWarning
                        IndicatorStatus.RED -> CrimsonAlert
                    }
                )

                Text(
                    text = healthyBenchmark,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = ElegantTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = ElegantBorderOutline)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = ElegantTextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}

// ==========================================
// FORM DIALOGS DECLARATIONS
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeDialog(onDismiss: () -> Unit, onSave: (String, Double, String) -> Unit) {
    var source by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Salario") }

    val categoriesList = listOf("Salario", "Trabajo Independiente", "Inversiones", "Reembolsos", "Otros")

    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ElegantSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            border = BoxBorder()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Registrar Nuevo Ingreso", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)

                OutlinedTextField(
                    value = source,
                    onValueChange = { source = it },
                    label = { Text("Origen del Ingreso") },
                    placeholder = { Text("p. ej. Consultoría de Diseño") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("form_income_source_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = EmeraldDark,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = EmeraldDark,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Monto ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    placeholder = { Text("0.00") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("form_income_amount_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = EmeraldDark,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = EmeraldDark,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                Text("Categoría", style = MaterialTheme.typography.labelMedium, color = ElegantTextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categoriesList.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, style = MaterialTheme.typography.labelMedium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldLight,
                                selectedLabelColor = EmeraldDark,
                                containerColor = ElegantSurface,
                                labelColor = ElegantTextSecondary
                            )
                        )
                    }
                }

                if (isError) {
                    Text("Por favor ingresa un origen y monto válidos para continuar.", color = CrimsonAlert, style = MaterialTheme.typography.bodySmall)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = ElegantTextSecondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            val amount = amountStr.toDoubleOrNull()
                            if (source.isNotBlank() && amount != null && amount > 0) {
                                onSave(source, amount, category)
                            } else {
                                isError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark, contentColor = SoftGrayBackground),
                        modifier = Modifier.testTag("form_income_save_btn")
                    ) {
                        Text("Registrar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(onDismiss: () -> Unit, onSave: (String, Double, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Comida") }

    val categoriesList = listOf("Vivienda", "Comida", "Transporte", "Servicios", "Entretenimiento", "Compras", "Otros")

    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ElegantSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            border = BoxBorder()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Registrar Gasto", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Concepto o Detalle") },
                    placeholder = { Text("p. ej. Compras Semanales") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("form_expense_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = CrimsonAlert,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = CrimsonAlert,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Monto Pagado ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    placeholder = { Text("0.00") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("form_expense_amount_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = CrimsonAlert,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = CrimsonAlert,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                Text("Indicador de Categoría", style = MaterialTheme.typography.labelMedium, color = ElegantTextSecondary)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categoriesList.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, style = MaterialTheme.typography.labelMedium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElegantRedBg,
                                selectedLabelColor = CrimsonAlert,
                                containerColor = ElegantSurface,
                                labelColor = ElegantTextSecondary
                            )
                        )
                    }
                }

                if (isError) {
                    Text("Por favor ingresa un concepto y valor de transacción válidos.", color = CrimsonAlert, style = MaterialTheme.typography.bodySmall)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = ElegantTextSecondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            val amount = amountStr.toDoubleOrNull()
                            if (title.isNotBlank() && amount != null && amount > 0) {
                                onSave(title, amount, category)
                            } else {
                                isError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark, contentColor = SoftGrayBackground),
                        modifier = Modifier.testTag("form_expense_save_btn")
                    ) {
                        Text("Registrar Gasto", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddDebtDialog(
    onDismiss: () -> Unit,
    onSave: (String, Double, Double, Double, String, Double) -> Unit
) {
    var creditor by remember { mutableStateOf("") }
    var originalStr by remember { mutableStateOf("") }
    var currentStr by remember { mutableStateOf("") }
    var rateStr by remember { mutableStateOf("") }
    var dueStr by remember { mutableStateOf("") }
    var payStr by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ElegantSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            border = BoxBorder()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Registrar Cuenta de Deuda", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)

                OutlinedTextField(
                    value = creditor,
                    onValueChange = { creditor = it },
                    label = { Text("Nombre del Acreedor") },
                    placeholder = { Text("p. ej. Préstamo de Estudiante / Banco") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = GoldWarning,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = GoldWarning,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = originalStr,
                        onValueChange = { originalStr = it },
                        label = { Text("Monto Original ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ElegantTextPrimary,
                            unfocusedTextColor = ElegantTextPrimary,
                            focusedLabelColor = GoldWarning,
                            unfocusedLabelColor = ElegantTextSecondary,
                            focusedBorderColor = GoldWarning,
                            unfocusedBorderColor = ElegantBorderOutline
                        )
                    )
                    OutlinedTextField(
                        value = currentStr,
                        onValueChange = { currentStr = it },
                        label = { Text("Monto Actual ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ElegantTextPrimary,
                            unfocusedTextColor = ElegantTextPrimary,
                            focusedLabelColor = GoldWarning,
                            unfocusedLabelColor = ElegantTextSecondary,
                            focusedBorderColor = GoldWarning,
                            unfocusedBorderColor = ElegantBorderOutline
                        )
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = rateStr,
                        onValueChange = { rateStr = it },
                        label = { Text("Tasa de Interés (%)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ElegantTextPrimary,
                            unfocusedTextColor = ElegantTextPrimary,
                            focusedLabelColor = GoldWarning,
                            unfocusedLabelColor = ElegantTextSecondary,
                            focusedBorderColor = GoldWarning,
                            unfocusedBorderColor = ElegantBorderOutline
                        )
                    )
                    OutlinedTextField(
                        value = payStr,
                        onValueChange = { payStr = it },
                        label = { Text("Pago Mínimo") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ElegantTextPrimary,
                            unfocusedTextColor = ElegantTextPrimary,
                            focusedLabelColor = GoldWarning,
                            unfocusedLabelColor = ElegantTextSecondary,
                            focusedBorderColor = GoldWarning,
                            unfocusedBorderColor = ElegantBorderOutline
                        )
                    )
                }

                OutlinedTextField(
                    value = dueStr,
                    onValueChange = { dueStr = it },
                    label = { Text("Fecha de Vencimiento Mensual") },
                    placeholder = { Text("p. ej. El 15 de cada mes") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = GoldWarning,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = GoldWarning,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                if (isError) {
                    Text("Asegúrate de que los campos numéricos sean válidos y el acreedor esté definido.", color = CrimsonAlert, style = MaterialTheme.typography.bodySmall)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = ElegantTextSecondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            val orig = originalStr.toDoubleOrNull()
                            val curr = currentStr.toDoubleOrNull()
                            val rate = rateStr.toDoubleOrNull() ?: 0.0
                            val pay = payStr.toDoubleOrNull() ?: 0.0
                            if (creditor.isNotBlank() && orig != null && curr != null && orig >= curr) {
                                onSave(creditor, orig, curr, rate, dueStr.ifBlank { "Mensual" }, pay)
                            } else {
                                isError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark, contentColor = SoftGrayBackground)
                    ) {
                        Text("Agregar Deuda", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddSavingsDialog(onDismiss: () -> Unit, onSave: (String, Double) -> Unit) {
    var desc by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ElegantSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            border = BoxBorder()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Depósito de Reserva de Ahorro", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Descripción del Depósito") },
                    placeholder = { Text("p. ej. Aporte para Vacaciones") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = EmeraldDark,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = EmeraldDark,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Monto del Depósito ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    placeholder = { Text("0.00") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = EmeraldDark,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = EmeraldDark,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                if (isError) {
                    Text("Por favor ingresa una descripción y monto válidos.", color = CrimsonAlert, style = MaterialTheme.typography.bodySmall)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = ElegantTextSecondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            val amount = amountStr.toDoubleOrNull()
                            if (desc.isNotBlank() && amount != null && amount > 0) {
                                onSave(desc, amount)
                            } else {
                                isError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark, contentColor = SoftGrayBackground)
                    ) {
                        Text("Confirmar Depósito", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AddGoalDialog(
    onDismiss: () -> Unit,
    onSave: (String, Double, Double, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var targetStr by remember { mutableStateOf("") }
    var currentStr by remember { mutableStateOf("") }
    var termStr by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ElegantSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            border = BoxBorder()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Establecer Meta Financiera", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Título de la Meta") },
                    placeholder = { Text("p. ej. Comprar Computadora") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = EmeraldDark,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = EmeraldDark,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = targetStr,
                        onValueChange = { targetStr = it },
                        label = { Text("Monto Objetivo ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ElegantTextPrimary,
                            unfocusedTextColor = ElegantTextPrimary,
                            focusedLabelColor = EmeraldDark,
                            unfocusedLabelColor = ElegantTextSecondary,
                            focusedBorderColor = EmeraldDark,
                            unfocusedBorderColor = ElegantBorderOutline
                        )
                    )
                    OutlinedTextField(
                        value = currentStr,
                        onValueChange = { currentStr = it },
                        label = { Text("Monto Guardado Actualmente ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = ElegantTextPrimary,
                            unfocusedTextColor = ElegantTextPrimary,
                            focusedLabelColor = EmeraldDark,
                            unfocusedLabelColor = ElegantTextSecondary,
                            focusedBorderColor = EmeraldDark,
                            unfocusedBorderColor = ElegantBorderOutline
                        )
                    )
                }

                OutlinedTextField(
                    value = termStr,
                    onValueChange = { termStr = it },
                    label = { Text("Horizonte de Tiempo (Fecha)") },
                    placeholder = { Text("p. ej. 31 de Dic de 2026") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = EmeraldDark,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = EmeraldDark,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                if (isError) {
                    Text("Por favor ingresa propiedades válidas para guardar tu meta.", color = CrimsonAlert, style = MaterialTheme.typography.bodySmall)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = ElegantTextSecondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            val trg = targetStr.toDoubleOrNull()
                            val cur = currentStr.toDoubleOrNull() ?: 0.0
                            if (name.isNotBlank() && trg != null && trg > 0) {
                                onSave(name, trg, cur, termStr.ifBlank { "Sin definir" })
                            } else {
                                isError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark, contentColor = SoftGrayBackground)
                    ) {
                        Text("Activar Meta", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetDialog(onDismiss: () -> Unit, onSave: (String, Double) -> Unit) {
    var category by remember { mutableStateOf("Comida") }
    var limitStr by remember { mutableStateOf("") }

    val categoriesList = listOf("Vivienda", "Comida", "Transporte", "Servicios", "Entretenimiento", "Compras", "Otros")
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ElegantSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            border = BoxBorder()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Establecer Límite de Gasto", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ElegantTextPrimary)

                Text("Categoría del Presupuesto", style = MaterialTheme.typography.labelMedium, color = ElegantTextSecondary)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categoriesList.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, style = MaterialTheme.typography.labelMedium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldLight,
                                selectedLabelColor = EmeraldDark,
                                containerColor = ElegantSurface,
                                labelColor = ElegantTextSecondary
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = limitStr,
                    onValueChange = { limitStr = it },
                    label = { Text("Límite Máximo Mensual ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    placeholder = { Text("p. ej. 500.00") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ElegantTextPrimary,
                        unfocusedTextColor = ElegantTextPrimary,
                        focusedLabelColor = EmeraldDark,
                        unfocusedLabelColor = ElegantTextSecondary,
                        focusedBorderColor = EmeraldDark,
                        unfocusedBorderColor = ElegantBorderOutline
                    )
                )

                if (isError) {
                    Text("Por favor define un valor de límite positivo.", color = CrimsonAlert, style = MaterialTheme.typography.bodySmall)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = ElegantTextSecondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            val limit = limitStr.toDoubleOrNull()
                            if (limit != null && limit > 0) {
                                onSave(category, limit)
                            } else {
                                isError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark, contentColor = SoftGrayBackground)
                    ) {
                        Text("Aplicar Presupuesto", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// UTILITY HELPERS
// ==========================================
fun formatLongDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale("es", "ES"))
    return formatter.format(date)
}

@Composable
fun BoxBorder(): androidx.compose.foundation.BorderStroke? {
    val template = LocalTemplate.current
    return if (template.hasBorder) {
        androidx.compose.foundation.BorderStroke(1.dp, CardBorderColor)
    } else {
        null
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DesignSettingsDialog(
    onDismiss: () -> Unit,
    themeIndex: Int,
    onThemeSelected: (Int) -> Unit,
    templateIndex: Int,
    onTemplateSelected: (Int) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = CardShape,
            colors = CardDefaults.cardColors(containerColor = ElegantSurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            border = BoxBorder()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Icono de Diseño",
                        tint = EmeraldDark,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Aspecto y Diseño",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ElegantTextPrimary
                    )
                }

                Text(
                    text = "Personaliza la experiencia visual eligiendo entre varias paletas de colores y plantillas estructurales para SmartFinance.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ElegantTextSecondary
                )

                HorizontalDivider(color = CardBorderColor, modifier = Modifier.padding(vertical = 4.dp))

                Text(
                    text = "Paleta de Colores",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ElegantTextPrimary
                )

                val themes = listOf(
                    Triple("Obsidiana", "Fondo oscuro clásico con detalles violetas", 0),
                    Triple("Esmeralda", "Verde menta relajante para foco sutil", 1),
                    Triple("Azul Mar", "Cobalto marino refrescante y profundo", 2),
                    Triple("Oro Rosa", "Tonalidades cobre cálidas y sofisticadas", 3)
                )

                themes.forEach { (title, desc, idx) ->
                    val isSelected = themeIndex == idx
                    val p = getThemePalette(idx)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onThemeSelected(idx) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) p.accentBackground.copy(alpha = 0.3f) else ElegantSurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, p.accentMain) else androidx.compose.foundation.BorderStroke(1.dp, CardBorderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Mini Color Previews
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(p.surface)
                                    .padding(4.dp)
                            ) {
                                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(p.accentMain))
                                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(p.primaryColor))
                                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(p.alertRed))
                            }
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = p.nombre,
                                    color = if (isSelected) p.accentMain else ElegantTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = desc,
                                    color = ElegantTextSecondary,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            RadioButton(
                                selected = isSelected,
                                onClick = { onThemeSelected(idx) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = p.accentMain,
                                    unselectedColor = ElegantTextTertiary
                                )
                            )
                        }
                    }
                }

                HorizontalDivider(color = CardBorderColor, modifier = Modifier.padding(vertical = 4.dp))

                Text(
                    text = "Plantilla Estructural",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ElegantTextPrimary
                )

                val templates = listOf(
                    Triple("Moderna con Bordes", "Tarjetas amplias delineadas con bordes nítidos", 0),
                    Triple("Plana Minimalista", "Contenedores sin bordes integrados sutilmente", 1),
                    Triple("Compacto y Denso", "Espaciados reducidos para mayor visibilidad", 2)
                )

                templates.forEach { (name, desc, idx) ->
                    val isSelected = templateIndex == idx
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTemplateSelected(idx) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) EmeraldLight.copy(alpha = 0.3f) else ElegantSurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, EmeraldDark) else androidx.compose.foundation.BorderStroke(1.dp, CardBorderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = when(idx) {
                                    0 -> Icons.Default.Square
                                    1 -> Icons.Default.Circle
                                    else -> Icons.Default.GridView
                                },
                                contentDescription = null,
                                tint = if (isSelected) EmeraldDark else ElegantTextTertiary,
                                modifier = Modifier.size(24.dp)
                            )
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = name,
                                    color = if (isSelected) EmeraldDark else ElegantTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = desc,
                                    color = ElegantTextSecondary,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            RadioButton(
                                selected = isSelected,
                                onClick = { onTemplateSelected(idx) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = EmeraldDark,
                                    unselectedColor = ElegantTextTertiary
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldDark,
                        contentColor = SoftGrayBackground
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Listo",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
