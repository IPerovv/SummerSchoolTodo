package com.example.todoapplication.presentation.detailed

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todoapplication.R
import com.example.todoapplication.core.ui.AppTheme
import com.example.todoapplication.core.ui.ExtendedTheme
import com.example.todoapplication.domain.model.ImportanceLevel
import java.util.Calendar
import java.util.TimeZone

@Composable
fun DetailedTodoItemScreen(
    viewModel: DetailedTodoItemViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    isDeleteEnabled: Boolean
) {
    val importance by viewModel.selectedImportance.collectAsStateWithLifecycle()
    val deadline by viewModel.deadline.collectAsStateWithLifecycle()
    val todoBody by viewModel.todoBody.collectAsStateWithLifecycle()

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
                    onAction = onSave,
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

                        DeleteSection(onDelete, isDeleteEnabled)

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
    onAction: () -> Unit = {},
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
                    indication = rememberRipple(),
                    onClick = { onNavigateUp() }
                )
                .semantics {
                    contentDescription = "Назад. Кнопка"
                    Role.Button
                },
            contentAlignment = Alignment.Center,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = "",
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
                    indication = rememberRipple(),
                    onClick = {
                        onAction()
                    }

                )
                .height(40.dp)
                .wrapContentSize(align = Alignment.Center)
                .padding(7.dp)
                .semantics {
                    contentDescription = "Сохранить. Кнопка"
                    Role.Button
                },
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
    var checkedState by remember { mutableStateOf(deadline != null) }

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp)
            .toggleable(
                value = checkedState,
                onValueChange = { newState ->
                    checkedState = newState
                    if (newState) {
                        showDatePicker(viewModel, context)
                    } else {
                        viewModel.clearDeadline()
                    }
                }
            )
            .semantics(mergeDescendants = true) {
                // Provide custom semantics to make the entire row focusable and describe the state
                stateDescription = if (checkedState) "Дата выполнения $deadline" else "Без даты выполнения"
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            androidx.compose.material3.Text(
                text = stringResource(id = R.string.make_up_to),
                color = ExtendedTheme.colors.labelPrimary,
                style = ExtendedTheme.typography.body,
                modifier = Modifier.clearAndSetSemantics { }
            )
            Text(
                text = deadline ?: " ",
                color = ExtendedTheme.colors.colorBlue,
                style = ExtendedTheme.typography.subhead,
                modifier = Modifier.clearAndSetSemantics { }
            )
        }

        androidx.compose.material3.Switch(
            checked = checkedState,
            onCheckedChange = { newState ->
                checkedState = newState
                if (newState) {
                    showDatePicker(viewModel, context)
                } else {
                    viewModel.clearDeadline()
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = ExtendedTheme.colors.colorBlue,
                uncheckedThumbColor = ExtendedTheme.colors.colorBlue,
                checkedBorderColor = Color.Transparent,
                uncheckedBorderColor = Color.Transparent,
                checkedTrackColor = Color.Cyan,
                uncheckedTrackColor = ExtendedTheme.colors.colorGrayLight
            ),
            modifier = Modifier.clearAndSetSemantics {  }
        )
    }
}

private fun showDatePicker(
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
        stringResource(id = R.string.importance_high)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
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
        "important" -> (stringResource(id = R.string.importance_high))
        "basic" -> stringResource(id = R.string.importance_basic)
        "low" -> stringResource(id = R.string.importance_low)
        else -> stringResource(id = R.string.importance_basic)
    }
}

@Composable
fun DeleteSection(onDelete: () -> Unit, isEnabled: Boolean) {
    val color =
        if (isEnabled) Color.Red else Color.Gray
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) { onDelete() }
            .padding(16.dp)
            .semantics(mergeDescendants = true) {
                Role.Button
                contentDescription = "Удалить. Кнопка"
            }
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            tint = color
        )
        Spacer(
            modifier = Modifier
                .width(12.dp)
        )
        Text(
            text = "Удалить",
            color = color,
            modifier = Modifier.clearAndSetSemantics {  }
        )
    }
}





