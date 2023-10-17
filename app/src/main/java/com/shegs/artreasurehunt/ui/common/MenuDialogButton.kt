package com.shegs.artreasurehunt.ui.common

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shegs.artreasurehunt.R

@Composable
fun MenuDialogButton(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    stringId: Int,
    menuIcon: @Composable () -> Unit,
) {

    Button(
        onClick = onButtonClick,
        modifier = modifier.padding(8.dp).fillMaxWidth().bounceClick(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFC75119),
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                content = {
                    Text(
                        text = stringResource(id = stringId),
                        fontSize = 16.sp
                    )
                    menuIcon()
                }
            )

        }
    )
}