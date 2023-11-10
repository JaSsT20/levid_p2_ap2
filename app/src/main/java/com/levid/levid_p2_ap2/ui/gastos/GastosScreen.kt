@file:OptIn(ExperimentalMaterial3Api::class)

package com.levid.levid_p2_ap2.ui.gastos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.levid.levid_p2_ap2.data.remote.dtos.GastoDto

@Composable
fun GastosScreen(
    viewModel: GastosViewModel = hiltViewModel()
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        Form(viewModel)
        GastosList(state.value.gastos, viewModel)
    }
}

@Composable
fun Form(viewModel: GastosViewModel) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        SuplidorTextField(viewModel = viewModel)
        FechaTextField(viewModel = viewModel)
        ConceptoTextField(viewModel = viewModel)
        NcfTextField(viewModel = viewModel)
        ItbisTextField(viewModel = viewModel)
        MontoTextField(viewModel = viewModel)
        SaveButton(viewModel = viewModel)
    }
}

@Composable
fun SaveButton(viewModel: GastosViewModel) {
    OutlinedButton(onClick = { viewModel.guardar()}) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add icon")
        Spacer(modifier = Modifier.padding(horizontal = 3.dp))
        Text(text = "Guardar")
    }
}


@Composable
fun SuplidorTextField(viewModel: GastosViewModel){
    OutlinedTextField(
        value = viewModel.idSuplidor.toString(),
        onValueChange = { viewModel.idSuplidor = it.toIntOrNull() ?: 0},
        label = { Text(text = "Suplidor")}
    )
}

@Composable
fun FechaTextField(viewModel: GastosViewModel){
    OutlinedTextField(
        value = viewModel.fecha,
        onValueChange = { viewModel.fecha = it},
        label = { Text(text = "Fecha")}
    )
}

@Composable
fun ConceptoTextField(viewModel: GastosViewModel){
    OutlinedTextField(
        value = viewModel.concepto,
        onValueChange = { viewModel.concepto = it},
        label = { Text(text = "Concepto")}
    )
}

@Composable
fun NcfTextField(viewModel: GastosViewModel){
    OutlinedTextField(
        value = viewModel.ncf,
        onValueChange = { viewModel.ncf = it},
        label = { Text(text = "NCF")}
    )
}

@Composable
fun ItbisTextField(viewModel: GastosViewModel){
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        value = viewModel.itbis.toString(),
        onValueChange = { viewModel.itbis = it.toIntOrNull() ?: 0},
        label = { Text(text = "Itbis")}
    )
}

@Composable
fun MontoTextField(viewModel: GastosViewModel){
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        value = viewModel.monto.toString(),
        onValueChange = { viewModel.monto = it.toIntOrNull() ?: 0},
        label = { Text(text = "Monto")}
    )
}
@Composable
fun GastosList(gastos: List<GastoDto>, viewModel: GastosViewModel) {
    LazyColumn{
        items(gastos){ gasto ->
            ItemGasto(gasto = gasto, viewModel = viewModel)
        }
    }
}

@Composable
fun ItemGasto(gasto: GastoDto, viewModel: GastosViewModel){
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        //Top row
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(text = "ID: ${gasto.id}")
            }
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = gasto.fecha)
            }
        }
        //Middle row
        Row(
            modifier = Modifier.padding(8.dp)
        ){
            Text(
                text = "${gasto.suplidor}",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 8.dp)
        ){
            Text(text = gasto.concepto, overflow = TextOverflow.Ellipsis, maxLines = 2)
        }
        Row {
            Column(
                modifier = Modifier.weight(3f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(text = "NCF: ${gasto.ncf}")
                }
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(text = "Itbis: ${gasto.itbis}")
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "$${gasto.monto}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Divider(thickness = 2.dp)
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = { viewModel.modificarGasto(gasto) }
                ) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit icon")
                    Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                    Text(text = "Modificar")
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = { viewModel.delete(gasto.id) },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete icon",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                    Text(text = "Eliminar", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}