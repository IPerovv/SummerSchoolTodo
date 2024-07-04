package com.example.todoapplication.after_reg.features.todo.detailed

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.todoapplication.R
import com.example.todoapplication.after_reg.domain.model.ImportanceLevel
import com.example.todoapplication.core.ui.AppTheme
import com.example.todoapplication.core.ui.ExtendedTheme
import java.util.Calendar
import java.util.TimeZone

@Composable
fun DetailedTodoItemScreen(
    viewModel: DetailedTodoItemViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    val importance by viewModel.selectedImportance.collectAsState()
    val deadline by viewModel.deadline.collectAsState()
    val todoBody by viewModel.todoBody.collectAsState()

    AppTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .background(ExtendedTheme.colors.backPrimary),
            backgroundColor = ExtendedTheme.colors.backPrimary,
            topBar = {
                TodoTaskTopBar(
                    onNavigateUp = onBack,
                    onAction = onSave
                )

            },
            content = { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding())
                ) {
                    item {
                        TextSection(todoBody = todoBody, viewModel = viewModel)

                        ChoiceImportantTask(viewModel = viewModel, importance)

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 1.dp,
                            color = ExtendedTheme.colors.colorGrayLight
                        )

                        ChoiceDateTask(viewModel = viewModel, deadline = deadline)

                        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))

                        DeleteSection(onDelete)

                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }
        )
    }
}

@Composable
private fun TodoTaskTopBar(
    onNavigateUp: () -> Unit = {},
    onAction: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 8.dp, end = 8.dp, top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onNavigateUp() }
                ),
            contentAlignment = Alignment.Center,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = "CLOSE",
                    colorFilter = ColorFilter.tint(ExtendedTheme.colors.labelPrimary),
                )
            }
        )

        androidx.compose.material3.Text(
            text = stringResource(id = R.string.save).uppercase(),
            style = ExtendedTheme.typography.titleSmall,
            color = ExtendedTheme.colors.colorBlue,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onAction() }
                )
                .wrapContentSize(align = Alignment.Center)
                .padding(end = 7.dp, top = 7.dp),
        )
    }
}


@Composable
private fun TextSection(todoBody: String, viewModel: DetailedTodoItemViewModel) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 12.dp)
            .wrapContentSize(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = ExtendedTheme.colors.backSecondary,
            contentColor = ExtendedTheme.colors.labelTertiary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        OutlinedTextField(
            value = todoBody,
            onValueChange = { newInputText: String ->
                viewModel.updateTodoBody(newInputText)
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.edit_text_hint),
                    color = ExtendedTheme.colors.labelSecondary,
                    style = ExtendedTheme.typography.body
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 120.dp),
        )
    }
}

@Composable
private fun ChoiceDateTask(
    viewModel: DetailedTodoItemViewModel,
    deadline: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Box(content = {
            Column {
                androidx.compose.material3.Text(
                    text = stringResource(id = R.string.make_up_to),
                    color = ExtendedTheme.colors.labelPrimary,
                    style = ExtendedTheme.typography.body
                )

                Text(
                    text = deadline ?: " ",
                    color = ExtendedTheme.colors.colorBlue,
                    style = ExtendedTheme.typography.subhead
                )
            }
        })

        var checkedState by rememberSaveable {
            mutableStateOf(deadline != null)
        }

        val context = LocalContext.current

        androidx.compose.material3.Switch(
            checked = checkedState,
            onCheckedChange = { newState ->
                if (newState) {
                    showDataPicker(viewModel, context)
                } else {
                    viewModel.setDate(null)
                }

                checkedState = newState
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = ExtendedTheme.colors.colorBlue,
                uncheckedThumbColor = ExtendedTheme.colors.colorBlue,
                checkedBorderColor = Color.Transparent,
                uncheckedBorderColor = Color.Transparent,
                checkedTrackColor = Color.Cyan,
                uncheckedTrackColor = ExtendedTheme.colors.colorGrayLight
            ),
        )
    }
}

private fun showDataPicker(
    viewModel: DetailedTodoItemViewModel,
    context: Context,
) {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            viewModel.setNewDate(year, month, dayOfMonth)
        }, year, month, dayOfMonth
    ).show()
}

@Composable
private fun ChoiceImportantTask(
    viewModel: DetailedTodoItemViewModel,
    importance: String
) {
    var expanded by remember { mutableStateOf(false) }

    val items = listOf(
        stringResource(id = R.string.importance_low),
        stringResource(id = R.string.importance_basic),
        "!! ${stringResource(id = R.string.importance_high)}"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { expanded = true }
            ),

        content = {
            Column {
                androidx.compose.material3.Text(
                    text = stringResource(id = R.string.importance_title),
                    color = ExtendedTheme.colors.labelPrimary,
                    style = ExtendedTheme.typography.body
                )

                androidx.compose.material3.Text(
                    text = unwrapImportance(importance = importance),
                    color = ExtendedTheme.colors.labelSecondary,
                    style = ExtendedTheme.typography.subhead
                )

                androidx.compose.material3.DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .size(width = 164.dp, height = 165.dp)
                        .background(ExtendedTheme.colors.backSecondary),
                    content = {
                        items.forEachIndexed { index, item ->
                            androidx.compose.material3.DropdownMenuItem(
                                text = {
                                    androidx.compose.material3.Text(
                                        text = item,
                                        style = TextStyle(
                                            color = if (index == 2) Color.Red else ExtendedTheme.colors.labelSecondary
                                        )
                                    )
                                },
                                onClick = {
                                    viewModel.setImportance(
                                        when (index) {
                                            0 -> ImportanceLevel.LOW
                                            1 -> ImportanceLevel.BASIC
                                            2 -> ImportanceLevel.IMPORTANT
                                            else -> ImportanceLevel.BASIC
                                        }
                                    )
                                    expanded = false
                                }
                            )
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun unwrapImportance(importance: String): String {
    return when (importance) {
        "important" -> ("!!" + stringResource(id = R.string.importance_high))
        "basic" -> stringResource(id = R.string.importance_basic)
        "low" -> stringResource(id = R.string.importance_low)
        else -> stringResource(id = R.string.importance_basic)
    }
}

@Composable
fun DeleteSection(onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDelete() }
            .padding(16.dp)
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.Red
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text("Удалить", color = Color.Red)
    }
}





