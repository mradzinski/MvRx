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

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.annotation.CallSuper
import com.bluelinelabs.conductor.Controller
import java.security.AccessController.getContext

abstract class BaseMvvmController(args: Bundle?) : Controller(args), LifecycleOwner, ViewModelStoreOwner {
    private lateinit var viewModelStore: ViewModelStore

    protected val viewLifecycleOwner: ControllerViewLifecycleOwner<*> by lazy { ControllerViewLifecycleOwner(this) }
    protected val controllerLifecycleOwner: ControllerLifecycleOwner<*> by lazy { ControllerLifecycleOwner(this) }

    final override fun getLifecycle(): Lifecycle = viewLifecycleOwner.getLifecycle()

    override fun getViewModelStore(): ViewModelStore {
        try {
            getContext()
        } catch (_: Exception) {
            throw IllegalStateException("Can't access ViewModels from detached controller")
        }

        if (!::viewModelStore.isInitialized) this.viewModelStore = ViewModelStore()

        return this.viewModelStore
    }

    fun viewModelProvider(): ViewModelProvider =
            viewModelProvider(ViewModelProvider.AndroidViewModelFactory(activity!!.application))

    fun viewModelProvider(factory: ViewModelProvider.NewInstanceFactory): ViewModelProvider =
            ViewModelProvider(getViewModelStore(), factory)

    @CallSuper
    override fun onDestroy() {
        if (::viewModelStore.isInitialized) viewModelStore.clear()

        super.onDestroy()
    }
}