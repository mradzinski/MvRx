package com.airbnb.mvrx

import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Make your base Fragment class extend this to get MvRx functionality.
 *
 * This is necessary for the view model delegates and persistence to work correctly.
 */
abstract class BaseMvRxFragment : Fragment(), MvRxView {

    override val mvrxViewModelStore by lazy { MvRxViewModelStore(viewModelStore) }
    private var onCreateCalled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        mvrxViewModelStore.restoreViewModels(this, savedInstanceState)
        super.onCreate(savedInstanceState)
        onCreateCalled = true
    }

    override val lifecycleOwner: LifecycleOwner
        get() = if (onCreateCalled) this.viewLifecycleOwner else this

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvrxViewModelStore.saveViewModels(outState)
    }

    override fun onStart() {
        super.onStart()
        // This ensures that invalidate() is called for static screens that don't
        // subscribe to a ViewModel.
        postInvalidate()
    }
}