/*
 * Copyright 2019-2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package com.example.bookapp.core

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.MavericksView
import com.example.bookapp.R
import com.example.bookapp.core.extensions.toMvRxBundle
import com.example.bookapp.core.utils.ToolbarConfig
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.view.clicks
import kotlin.collections.forEach
import kotlin.toString

abstract class BaseFragment<VB : ViewBinding> : Fragment(), MavericksView {
    /* ==========================================================================================
     * Activity
     * ========================================================================================== */

    protected val vectorBaseActivity: BaseActivity<*> by lazy {
        activity as BaseActivity<*>
    }

    private var progress: AlertDialog? = null

    /* ==========================================================================================
     * View model
     * ========================================================================================== */

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val activityViewModelProvider
        get() = ViewModelProvider(requireActivity(), viewModelFactory)

    protected val fragmentViewModelProvider
        get() = ViewModelProvider(this, viewModelFactory)

    /* ==========================================================================================
     * Views
     * ========================================================================================== */

    private var _binding: VB? = null

    // This property is only valid between onCreateView and onDestroyView.
    val views: VB
        get() = _binding!!

    /* ==========================================================================================
     * Life cycle
     * ========================================================================================== */

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = getBinding(inflater, container)
        return views.root
    }

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    @CallSuper
    override fun onResume() {
        super.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    protected fun setupInsets() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            ViewCompat.setOnApplyWindowInsetsListener(views.root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(0, systemBars.top, 0, 0)
                insets
            }
        }
    }

    private fun setupMenu() {
        if (this !is BaseMenuProvider) return
        if (getMenuRes() == -1) return
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
                object : MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menuInflater.inflate(getMenuRes(), menu)
                        handlePostCreateMenu(menu)
                    }

                    override fun onPrepareMenu(menu: Menu) {
                        handlePrepareMenu(menu)
                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                        return handleMenuItemSelected(menuItem)
                    }
                },
                viewLifecycleOwner,
                Lifecycle.State.RESUMED
        )
    }

    open fun showFailure(throwable: Throwable) {
        displayErrorDialog(throwable)
    }

    @CallSuper
    override fun onDestroyView() {
        _binding = null
        dismissLoadingDialog()
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
    }

    /* ==========================================================================================
     * Restorable
     * ========================================================================================== */

    private val restorables = kotlin.collections.ArrayList<Restorable>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        restorables.forEach { it.onSaveInstanceState(outState) }
        restorables.clear()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        restorables.forEach { it.onRestoreInstanceState(savedInstanceState) }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun invalidate() {
        // no-ops by default
    }

    protected fun setArguments(args: Parcelable? = null) {
        arguments = args.toMvRxBundle()
    }

    @MainThread
    protected fun <T : Restorable> T.register(): T {
        restorables.add(this)
        return this
    }

    fun dismissLoadingDialog() {
        progress?.dismiss()
    }

    /* ==========================================================================================
     * Toolbar
     * ========================================================================================== */

    /**
     * Sets toolbar as actionBar for current activity.
     *
     * @return Instance of [ToolbarConfig] with set of helper methods to configure toolbar
     * */
    protected fun setupToolbar(toolbar: MaterialToolbar): ToolbarConfig {
        return vectorBaseActivity.setupToolbar(toolbar)
    }

    /* ==========================================================================================
     * ViewEvents
     * ========================================================================================== */

//    protected fun <T : BaseViewEvents> BaseViewModel<*, *, T>.observeViewEvents(
//            observer: (T) -> Unit,
//    ) {
//        val tag = this@BaseFragment::class.simpleName.toString()
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                viewEvents
//                        .stream(tag)
//                        .collect {
//                            dismissLoadingDialog()
//                            observer(it)
//                        }
//            }
//        }
//    }

    /* ==========================================================================================
     * Views
     * ========================================================================================== */

    protected fun View.debouncedClicks(onClicked: () -> Unit) {
        clicks()
                .onEach { onClicked() }
                .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /* ==========================================================================================
     * MENU MANAGEMENT
     * ========================================================================================== */

    // This should be provided by the framework
    protected fun invalidateOptionsMenu() = requireActivity().invalidateOptionsMenu()

    /* ==========================================================================================
     * Common Dialogs
     * ========================================================================================== */

    protected fun displayErrorDialog(throwable: Throwable) {
        MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.dialog_title_error)
                .setMessage(throwable.message)
                .setPositiveButton("OK", null)
                .show()
    }

    /* ==========================================================================================
     * Accessibility - a11y
     * ========================================================================================== */

    private var hasBeenAccessibilityFocused = false
}
