import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.level_upmovil.ui.screens.*
import com.example.level_upmovil.viewmodel.MainViewModel
import com.example.level_upmovil.model.Producto
import org.junit.Rule
import org.junit.Test

class AllUiTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun productCard_muestraNombreYPrecio() {
        val producto = Producto(
            id = 1,
            nombre = "Mouse Gamer",
            precio = 9990,
            imageResId = "",
            descripcion = "Descripción",
            categoria = "Periféricos"
        )

        composeTestRule.setContent {
            ProductCard(
                producto = producto,
                navController = TestNavHostController(ApplicationProvider.getApplicationContext()),
                viewModel = MainViewModel()
            )
        }

        composeTestRule.onNodeWithText("Mouse Gamer").assertIsDisplayed()
        composeTestRule.onNodeWithText("$ 9.990").assertIsDisplayed()
    }

    @Test
    fun homeScreen_muestraTituloInicio() {
        composeTestRule.setContent {
            HomeScreen(
                navController = TestNavHostController(ApplicationProvider.getApplicationContext()),
                viewModel = MainViewModel()
            )
        }

        composeTestRule.onNodeWithText("Inicio").assertIsDisplayed()
    }

    @Test
    fun welcomeBanner_muestraTextoBienvenida() {
        composeTestRule.setContent {
            WelcomeBanner(onNavigateToProfile = {})
        }

        composeTestRule.onNodeWithText("BIENVENIDO A LEVEL UP").assertIsDisplayed()
    }
}
