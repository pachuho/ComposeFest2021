/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable

import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codelabs.state.databinding.ActivityHelloCodelabBinding
import com.codelabs.state.ui.StateCodelabTheme

class HelloCodelabViewModel: ViewModel() {

    // LiveData holds state which is observed by the UI
    // (state flows down from ViewModel)
    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    // onNameChanged is an event we're defining that the UI can invoke
    // (events flow up from UI)
    fun onNameChanged(newName: String) {
        _name.value = newName
    }
}


class HelloCodelabActivity : AppCompatActivity() {
    private val helloViewModel by viewModels<HelloCodelabViewModel>()

    private lateinit var binding: ActivityHelloCodelabBinding
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* ... */
        binding.textInput.doAfterTextChanged {
            helloViewModel.onNameChanged(it.toString())
        }

        helloViewModel.name.observe(this) { name ->
            binding.helloText.text = "Hello, $name"
        }
    }
}

class TodoActivity : AppCompatActivity() {

    private val todoViewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateCodelabTheme {
                Surface {
                    TodoActivityScreen(todoViewModel)
                }
            }
        }
    }
}

@Composable
private fun TodoActivityScreen(todoViewModel: TodoViewModel) {
    TodoScreen(
        items = todoViewModel.todoItems,
        currentlyEditing = todoViewModel.currentEditItem,
        onAddItem = todoViewModel::addItem,
        onRemoveItem = todoViewModel::removeItem,
        onStartEdit = todoViewModel::onEditItemSelected,
        onEditItemChange = todoViewModel::onEditItemChange,
        onEditDone = todoViewModel::onEditDone
    )
}