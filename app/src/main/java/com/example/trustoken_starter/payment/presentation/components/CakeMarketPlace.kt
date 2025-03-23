package com.example.trustoken_starter.payment.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trustoken_starter.R

// Define the font family
val poppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

// Define data class for cake items
data class CakeItem(
    val name: String,
    val price: Int,
    val imageRes: Int,
    val isCrazy: Boolean = false
)
@Composable
fun CakeMarketplace() {
    // Sample data
    val cakeItems = listOf(
        CakeItem("Strawberry Cake (3pc)", 300, R.drawable.card_symbol, true),
        CakeItem("Strawberry Cake (3pc)", 300, R.drawable.card_symbol, true),
        CakeItem("Strawberry Cake (3pc)", 300, R.drawable.card_symbol, true),
        CakeItem("Strawberry Cake (3pc)", 300, R.drawable.card_symbol, false),
        CakeItem("Strawberry Cake (3pc)", 300, R.drawable.card_symbol, false),
        CakeItem("Strawberry Cake (3pc)", 300, R.drawable.card_symbol, false)
    )

    val crazyCakes = cakeItems.filter { it.isCrazy }
    val normalCakes = cakeItems.filter { !it.isCrazy }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF0F9FF),
                        Color(0xFFE0F2FE)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Marketplace Header
            Text(
                text = "MARKETPLACE",
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color(0xFF0F172A),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Crazy Cakes Section
            CategorySection(
                title = "Crazy Cakes",
                cakes = crazyCakes,
                gradient = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF8787))
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Normal Cakes Section
            CategorySection(
                title = "Normal Cakes",
                cakes = normalCakes,
                gradient = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF4ECDC4), Color(0xFF74DFD6))
                )
            )
        }
    }
}

@Composable
fun CategorySection(
    title: String,
    cakes: List<CakeItem>,
    gradient: Brush
) {
    Column {
        // Category Title with styled background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(gradient)
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = title,
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cake grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(220.dp) // Limit height for scrolling
        ) {
            items(cakes) { cake ->
                CakeCard(cake = cake)
            }
        }
    }
}

@Composable
fun CakeCard(cake: CakeItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Cake Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                Image(
                    painter = painterResource(id = cake.imageRes),
                    contentDescription = cake.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Price tag
                Surface(
                    color = Color(0xFF1A237E).copy(alpha = 0.85f),
                    shape = RoundedCornerShape(bottomStart = 12.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "${cake.price}",
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Cake Name
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Text(
                    text = cake.name,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CakeMarketplacePreview() {
    MaterialTheme {
        CakeMarketplace()
    }
}