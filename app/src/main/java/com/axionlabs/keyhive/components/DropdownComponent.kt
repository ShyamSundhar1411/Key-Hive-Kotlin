package com.axionlabs.keyhive.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.axionlabs.keyhive.model.DropDownItem

@Composable
fun DropDownComponent(
    showDropdown: MutableState<Boolean>,
    items:  List<DropDownItem>,
    dropdownWidth: Dp = 160.dp
    ){
    val expanded = remember {
        mutableStateOf(true)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .padding(top = 45.dp, end = 20.dp)

    ) {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false

            },
            modifier = Modifier
                .width(dropdownWidth)
                .background(Color.White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (item.isEnabled) Color.Black else Color.Gray
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = item.label,
                                color = if (item.isEnabled) Color.Black else Color.Gray
                            )
                        }
                    },
                    onClick = {
                        if (item.isEnabled) {
                            expanded.value = false
                            showDropdown.value = false
                            item.onClick()
                        }
                    }
                )
            }

        }
    }
}