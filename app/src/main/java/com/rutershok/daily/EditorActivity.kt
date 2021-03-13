package com.rutershok.daily

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rutershok.daily.adapters.editor.EditorPagerAdapter
import com.rutershok.daily.database.Storage
import com.rutershok.daily.model.Quote
import com.rutershok.daily.utils.*
import com.rutershok.daily.utils.Dialog.ShareBottomSheet
import com.rutershok.daily.utils.Premium.isPremium

class EditorActivity : AppCompatActivity() {
    private var mImageBackground: ImageView? = null
    private var mTextQuote: TextView? = null
    private var mImageLogo: ImageView? = null
    private var mQuote: Quote? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        title = getString(R.string.editor)
        mQuote = intent.getSerializableExtra(Constant.QUOTE) as Quote?
        if (null != supportActionBar) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        (findViewById<View>(R.id.pager_editor) as ViewPager).adapter = EditorPagerAdapter(
            supportFragmentManager, this
        )


        initText()
        initImage()
        Ad.showBanner(this)
    }

    //Used inside onTouchListener in textView
    private var mX = 0f
    private var mY = 0f
    private var mLastAction = 0
    private fun initText() {
        mTextQuote = findViewById(R.id.text_quote)
        (mTextQuote as TextView).setTextColor(Storage.getTextColor(this))
        //Get text from other apps
        val extraText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (mQuote != null) {
            val quote = mQuote!!.quote
            if (quote != null) {
                val builder = StringBuilder(quote)
                val author = mQuote!!.author
                if (author != null) {
                    builder.append("\n- ").append(author)
                }
                mTextQuote!!.text = builder
            }
        } else if (extraText != null) {
            mTextQuote!!.text = extraText
        }
        mTextQuote!!.setOnTouchListener({ v: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if (mLastAction == MotionEvent.ACTION_DOWN) {
                        Dialog.showEditQuote(v.context, mTextQuote)
                        mLastAction = event.action
                    }
                    v.performClick()
                }
                MotionEvent.ACTION_DOWN -> {
                    mX = v.x - event.rawX
                    mY = v.y - event.rawY
                    mLastAction = event.action
                }
                MotionEvent.ACTION_MOVE -> {
                    v.animate().x(event.rawX + mX).y(event.rawY + mY).setDuration(0).start()
                    mLastAction = event.action
                }
                else -> v.performClick()
            }
            true
        })
        mTextQuote!!.setOnLongClickListener({ v: View ->
            Dialog.showEditQuote(v.context, mTextQuote)
            false
        })
    }

    private fun initImage() {
        mImageBackground = findViewById(R.id.image_background)
        if (mQuote != null) {
            if (Setting.saveData(this)) {
                Glide.with(this).load(ColorDrawable(Storage.getBackgroundColor(this)))
                    .into(mImageBackground as ImageView)
            } else {
                Glide.with(this).load(mQuote!!.background).into(mImageBackground as ImageView)
            }
        }
        mImageLogo = findViewById(R.id.image_logo)
        (mImageLogo as ImageView).setOnClickListener {
            Dialog.showPremiumIsNeeded(
                this
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> Dialog.showExit(this)
            R.id.action_edit_quote -> Dialog.showEditQuote(this, mTextQuote)
            R.id.action_save -> ImageUtil.saveImage(
                this,
                ImageUtil.getScaledBitmap(findViewById(R.id.relative_quote_editor))
            )
            R.id.action_share -> ShareBottomSheet(
                this,
                mQuote,
                findViewById(R.id.relative_quote_editor)
            ).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_editor, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (null != data && null != data.data) {
            Glide.with(this).load(data.data)
                .apply(RequestOptions().centerCrop())
                .skipMemoryCache(true)
                .into(mImageBackground!!)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPremium(this)) {
            mImageLogo!!.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        Dialog.showExit(this)
    }
}