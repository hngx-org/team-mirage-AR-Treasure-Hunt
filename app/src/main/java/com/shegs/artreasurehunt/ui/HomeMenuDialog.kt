package com.shegs.artreasurehunt.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.ui.common.MenuDialogButton

@Composable
fun HomeMenuDialog(
    modifier: Modifier = Modifier,
    onShowDialog: () -> Unit,
    onProfileClick: () -> Unit,
    onLeaderBoardClick: () -> Unit,
    onDataRulesClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {

    Dialog(
        onDismissRequest = onShowDialog,
        content = {
            ElevatedCard(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(8.dp),
                content = {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        content = {

                            IconButton(
                                modifier = modifier.align(Alignment.End),
                                onClick = onShowDialog,
                                content = {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "")
                                }
                            )

                            MenuDialogButton(
                                onButtonClick = onProfileClick,
                                stringId = R.string.profile,
                                menuIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.pirate_hat),
                                        contentDescription = "",
                                        modifier = modifier.size(40.dp)
                                    )
                                }
                            )

                            MenuDialogButton(
                                onButtonClick = onLeaderBoardClick,
                                stringId = R.string.leaderboard,
                                menuIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.leaderboard),
                                        contentDescription = "",
                                        modifier = modifier.size(40.dp)
                                    )
                                }
                            )

                            MenuDialogButton(
                                onButtonClick = onSettingsClick,
                                stringId = R.string.settings,
                                menuIcon = {
                                    Image(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(
                                            color = Color(
                                                0XFFFEDB41
                                            )
                                        ),
                                        modifier = modifier.size(40.dp)
                                    )
                                }
                            )

                            MenuDialogButton(
                                onButtonClick = onDataRulesClick,
                                stringId = R.string.data_rules,
                                menuIcon = {
                                    Image(
                                        imageVector = Icons.Default.LibraryBooks,
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(
                                            color = Color(
                                                0XFFFEDB41
                                            )
                                        ),
                                        modifier = modifier.size(40.dp)
                                    )
                                }
                            )

                        }
                    )
                }
            )
        }
    )
}