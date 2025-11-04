/*
 * Copyright 2019-2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package com.example.bookapp.core.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService

fun View.hideKeyboard() {
    val imm = context?.getSystemService<InputMethodManager>()
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard(andRequestFocus: Boolean = false) {
    if (andRequestFocus) {
        requestFocus()
    }
    val imm = context?.getSystemService<InputMethodManager>()
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.setHorizontalPadding(padding: Int) {
    setPadding(
            padding,
            paddingTop,
            padding,
            paddingBottom
    )
}

fun View.expandWidth(duration: Long = 300) {
    // Đo width thật của view (wrap_content)
    this.measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    val targetWidth = this.measuredWidth

    // set width = 0 trước nhưng vẫn giữ INVISIBLE
    this.layoutParams.width = 0
    this.visibility = View.INVISIBLE

    val animator = ValueAnimator.ofInt(0, targetWidth)
    animator.addUpdateListener { valueAnimator ->
        val value = valueAnimator.animatedValue as Int
        this.layoutParams.width = value
        this.requestLayout()
    }
    animator.duration = duration
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            this@expandWidth.visibility = View.VISIBLE  // chỉ hiển thị khi animation bắt đầu
        }
    })

    animator.start()
}

fun View.collapseWidth(duration: Long = 300) {
    val initialWidth = this.width
    if (initialWidth == 0) return

    val animator = ValueAnimator.ofInt(initialWidth, 0)
    animator.addUpdateListener { valueAnimator ->
        val value = valueAnimator.animatedValue as Int
        this.layoutParams.width = value
        this.requestLayout()
    }
    animator.duration = duration

    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@collapseWidth.visibility = View.GONE
            // reset width để lần expand sau có wrap_content chính xác
            this@collapseWidth.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    })

    animator.start()
}

fun View.expandHeight(duration: Long = 300) {
    this.measure(
        View.MeasureSpec.makeMeasureSpec(this.width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    val targetHeight = this.measuredHeight

    this.layoutParams.height = 0
    this.visibility = View.INVISIBLE

    val animator = ValueAnimator.ofInt(0, targetHeight)
    animator.addUpdateListener { valueAnimator ->
        val value = valueAnimator.animatedValue as Int
        this.layoutParams.height = value
        this.requestLayout()
    }
    animator.duration = duration
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            this@expandHeight.visibility = View.VISIBLE
        }
    })

    animator.start()
}

fun View.collapseHeight(duration: Long = 300) {
    val initialHeight = this.height
    if (initialHeight == 0) return

    val animator = ValueAnimator.ofInt(initialHeight, 0)
    animator.addUpdateListener { valueAnimator ->
        val value = valueAnimator.animatedValue as Int
        this.layoutParams.height = value
        this.requestLayout()
    }
    animator.duration = duration
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@collapseHeight.visibility = View.GONE
            // reset để lần expand sau đo wrap_content chính xác
            this@collapseHeight.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    })

    animator.start()
}
