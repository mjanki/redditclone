package org.umbrellahq.baseapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import org.umbrellahq.baseapp.R
import org.umbrellahq.baseapp.adapters.TasksRecyclerViewAdapter
import org.umbrellahq.baseapp.mappers.TaskViewViewModelMapper
import org.umbrellahq.baseapp.models.TaskViewEntity
import org.umbrellahq.viewmodel.models.TaskViewModelEntity
import org.umbrellahq.viewmodel.viewmodels.TaskViewModel

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var taskVM: TaskViewModel

    private val taskViewViewModelMapper = TaskViewViewModelMapper()

    private val tasksRecyclerViewAdapter = TasksRecyclerViewAdapter(arrayOf())
    private val linearLayoutManager = LinearLayoutManager(activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskVM = ViewModelProviders.of(this).get(TaskViewModel::class.java)
        taskVM.init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTasksRecyclerView()

        setupTasksObservers()
        setupTasksClickListeners()
    }

    private fun setupTasksRecyclerView() {
        rvTasks.apply {
            layoutManager = linearLayoutManager
            adapter = tasksRecyclerViewAdapter
        }

        srlTasksRefresh.setOnRefreshListener {
            taskVM.retrieveTasks()
        }
    }

    private fun setupTasksObservers() {
        taskVM.allTasks.observe(viewLifecycleOwner, Observer<List<TaskViewModelEntity>> {
            tasksRecyclerViewAdapter.tasks = it.map { taskViewModelEntity ->
                taskViewViewModelMapper.upstream(taskViewModelEntity)
            }.toTypedArray()

            if (!taskVM.insertingTask) {
                tasksRecyclerViewAdapter.notifyDataSetChanged()
                return@Observer
            }

            tasksRecyclerViewAdapter.notifyItemInserted(0)
            linearLayoutManager.scrollToPosition(0)

            taskVM.insertingTask = false
        })

        taskVM.getIsRetrievingTasks().observe(viewLifecycleOwner, Observer {
            srlTasksRefresh.isRefreshing = it
        })
    }

    private fun setupTasksClickListeners() {
        fabAddTask.setOnClickListener {
            taskVM.insertingTask = true
            taskVM.insertTask(
                    taskViewViewModelMapper.downstream(
                            TaskViewEntity(
                                    name = "New Task ${taskVM.allTasks.value?.size}"
                            )
                    )
            )
        }
    }
}