/*
 * Copyright (c) 2019. DNA Software. All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.airbnb.mvrx.conductor

import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.airbnb.mvrx.MvRxView
import com.airbnb.mvrx.MvRxViewModelStore
import com.bluelinelabs.conductor.Controller

abstract class BaseMvRxController(args: Bundle? = null) : BaseMvvmController(args), MvRxView {

    override val mvrxViewModelStore by lazy { MvRxViewModelStore(viewModelStore) }

    init {
        addLifecycleListener(object : Controller.LifecycleListener() {
            override fun postAttach(controller: Controller, view: View) {
                super.postAttach(controller, view)

                // This ensures that invalidate() is called for static screens that don't
                // subscribe to a ViewModel.
                postInvalidate()
            }
        })
    }

    @CallSuper
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        mvrxViewModelStore.restoreViewModels(this, savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvrxViewModelStore.saveViewModels(outState)
    }
}