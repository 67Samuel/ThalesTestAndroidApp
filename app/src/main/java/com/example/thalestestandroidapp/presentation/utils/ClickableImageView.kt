package com.example.thalestestandroidapp.presentation.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.example.thalestestandroidapp.R


class ForegroundImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatImageView(context, attrs) {
    private var foreground: Drawable? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundImageView)
        val foreground = a.getDrawable(R.styleable.ForegroundImageView_android_foreground)
        foreground?.let {
            setForeground(it)
        }
        a.recycle()
    }

    /**
     * Supply a drawable resource that is to be rendered on top of all of the child
     * views in the frame layout.
     *
     * @param drawableResId The drawable resource to be drawn on top of the children.
     */
    fun setForegroundResource(drawableResId: Int) {
        ResourcesCompat.getDrawable(context.resources, drawableResId, null)?.let {
            setForeground(it)
        }
    }

    /**
     * Supply a Drawable that is to be rendered on top of all of the child
     * views in the frame layout.
     *
     * @param drawable The Drawable to be drawn on top of the children.
     */
    override fun setForeground(drawable: Drawable) {
        if (foreground === drawable) {
            return
        }
        foreground?.let {
            it.callback = null
            unscheduleDrawable(it)
        }

        foreground = drawable

        drawable?.let {
            it.callback = this
            if (it.isStateful) {
                it.setState(drawableState)
            }
        }
        requestLayout()
        invalidate()
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === foreground
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        foreground?.jumpToCurrentState()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        foreground?.let {
            if (it.isStateful) {
                it.setState(drawableState)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        foreground?.let {
            it.setBounds(0, 0, measuredWidth, measuredHeight)
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        foreground?.let {
            it.setBounds(0, 0, w, h)
            invalidate()
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        foreground?.draw(canvas)
    }
}