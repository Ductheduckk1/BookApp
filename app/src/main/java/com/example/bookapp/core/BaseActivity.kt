package com.example.bookapp.core

import android.annotation.SuppressLint
import com.example.bookapp.R
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.MavericksView
import com.example.bookapp.core.utils.ToolbarConfig
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity(), MavericksView {
    protected lateinit var views: VB

    var toolbar: ToolbarConfig? = null


    protected fun View.debouncedClicks(onClicked: () -> Unit) {
        clicks()
            .onEach { onClicked() }
            .launchIn(lifecycleScope)
    }

    private var savedInstanceState: Bundle? = null

    private val restorables = ArrayList<Restorable>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        restorables.forEach { it.onSaveInstanceState(outState) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        restorables.forEach { it.onRestoreInstanceState(savedInstanceState) }
        super.onRestoreInstanceState(savedInstanceState)
    }

    @Suppress("DEPRECATION")
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupMenu()

        doBeforeSetContentView()

        views = getBinding()
        setContentView(views.root)

        this.savedInstanceState = savedInstanceState
        setupInsets()
        initUiAndData()

        val titleRes = getTitleRes()
        if (titleRes != -1) {
            supportActionBar?.setTitle(titleRes) ?: run {
                setTitle(titleRes)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setupInsets() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                view.setBackgroundColor(resources.getColor(R.color.black))

                // Adjust padding to avoid overlap
                view.setPadding(0, statusBarInsets.top, 0, 0)
                insets
            }
        } else {
            // For Android 14 and below
            window.statusBarColor = resources.getColor(R.color.black)
        }
    }

    private fun setupMenu() {
        // Always add a MenuProvider to handle the back action from the Toolbar
        val vectorMenuProvider = this as? BaseMenuProvider
        addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    vectorMenuProvider?.let {
                        menuInflater.inflate(it.getMenuRes(), menu)
                        it.handlePostCreateMenu(menu)
                    }
                }

                override fun onPrepareMenu(menu: Menu) {
                    vectorMenuProvider?.handlePrepareMenu(menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return vectorMenuProvider?.handleMenuItemSelected(menuItem)?: false||
                            handleMenuItemHome(menuItem)
                }
            },
            this,
            Lifecycle.State.RESUMED
        )
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    private fun handleMenuItemHome(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed(true)
                true
            }
            else -> false
        }
    }

    @SuppressLint("MissingSuperCall", "GestureBackNavigation")
    @Suppress("OVERRIDE_DEPRECATION")
    override fun onBackPressed() {
        onBackPressed(false)
    }

    private fun onBackPressed(fromToolbar: Boolean) {
        val handled = recursivelyDispatchOnBackPressed(supportFragmentManager, fromToolbar)
        if (!handled) {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }

    private fun recursivelyDispatchOnBackPressed(fm: FragmentManager, fromToolbar: Boolean): Boolean {
        val reverseOrder = fm.fragments.filterIsInstance<BaseFragment<*>>().reversed()
        for (f in reverseOrder) {
            val handledByChildFragments = recursivelyDispatchOnBackPressed(f.childFragmentManager, fromToolbar)
            if (handledByChildFragments) {
                return true
            }
            if (f is OnBackPressed && f.onBackPressed(fromToolbar)) {
                return true
            }
        }
        return false
    }

    abstract fun getBinding(): VB

    open fun doBeforeSetContentView() = Unit

    open fun initUiAndData() = Unit

    @StringRes
    open fun getTitleRes() = -1

    open fun getCoordinatorLayout(): CoordinatorLayout? = null

    final override fun invalidate() = Unit

    fun setupToolbar(toolbar: MaterialToolbar) = ToolbarConfig(this, toolbar).also {
        this.toolbar = it.setup()
    }

    protected fun isFirstCreation() = savedInstanceState == null
}

interface OnBackPressed {

    /**
     * Returns true, if the on back pressed event has been handled by this Fragment.
     * Otherwise return false
     * @param toolbarButton true if this is the back button from the toolbar
     */
    fun onBackPressed(toolbarButton: Boolean): Boolean
}