package com.shegs.artreasurehunt.ui.game.map

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.shegs.artreasurehunt.R

@Composable
fun PermissionRationaleDialog(
    modifier: Modifier = Modifier,
    onShowDialog: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onShowDialog() },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onShowDialog()
                },
                content = {
                    Text(text = stringResource(id = R.string.location_rationale_button_grant))
                }
            )
        },
        title = {
            Text(text = stringResource(id = R.string.location_rationale_title))
        },
        text = {
            Text(text = stringResource(id = R.string.location_rationale_description))
        },
        dismissButton = {
            Button(
                onClick = {
                    onShowDialog()
                },
                content = {
                    Text(text = stringResource(id = R.string.location_rationale_button_deny))
                }
            )
        }
    )
}