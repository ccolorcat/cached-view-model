package cc.colorcat.viewmodel.sample

import androidx.appcompat.app.AppCompatActivity

/**
 * Author: ccolorcat
 * Date: 2023-05-14
 * GitHub: https://github.com/ccolorcat
 */
class AActivity : BaseActivity() {
    override val nextActivity: Class<out AppCompatActivity>?
        get() = BActivity::class.java
}