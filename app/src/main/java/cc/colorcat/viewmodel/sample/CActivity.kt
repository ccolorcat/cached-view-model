package cc.colorcat.viewmodel.sample

import androidx.appcompat.app.AppCompatActivity

/**
 * Author: ccolorcat
 * Date: 2023-05-12
 * GitHub: https://github.com/ccolorcat
 */
class CActivity : BaseActivity() {
    override val nextActivity: Class<out AppCompatActivity>?
        get() = DActivity::class.java
}