package com.shegs.artreasurehunt.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun AnnotatedText(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            ) {
                append("$title:  ")
            }

            withStyle(
                style = SpanStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontSize = 16.sp
                )
            ) {
                append(description)
            }
        })

}