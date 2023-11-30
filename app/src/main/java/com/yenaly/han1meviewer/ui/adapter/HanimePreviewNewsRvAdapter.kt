package com.yenaly.han1meviewer.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.DataBindingHolder
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.itxca.spannablex.spannable
import com.lxj.xpopup.XPopup
import com.yenaly.han1meviewer.R
import com.yenaly.han1meviewer.VIDEO_CODE
import com.yenaly.han1meviewer.databinding.ItemHanimePreviewNews2Binding
import com.yenaly.han1meviewer.logic.model.HanimePreviewModel
import com.yenaly.han1meviewer.ui.activity.PreviewActivity
import com.yenaly.han1meviewer.ui.activity.VideoActivity
import com.yenaly.han1meviewer.ui.popup.CoilImageLoader
import com.yenaly.han1meviewer.util.notNull
import com.yenaly.yenaly_libs.utils.dp
import com.yenaly.yenaly_libs.utils.startActivity

/**
 * @project Han1meViewer
 * @author Yenaly Liew
 * @time 2023/11/26 026 16:48
 */
class HanimePreviewNewsRvAdapter :
    BaseQuickAdapter<HanimePreviewModel.PreviewInfo, HanimePreviewNewsRvAdapter.ViewHolder>() {

    init {
        isStateViewEnable = true
    }

    private val imageLoader = CoilImageLoader()

    inner class ViewHolder(view: View) : DataBindingHolder<ItemHanimePreviewNews2Binding>(view)

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        item: HanimePreviewModel.PreviewInfo?,
    ) {
        item.notNull()
        holder.binding.ivCoverBig.load(item.coverUrl) {
            crossfade(true)
        }
        holder.binding.tvTitle.text = spannable {
            item.title.quote(Color.RED, stripeWidth = 4.dp, gapWidth = 4.dp)
        }
        holder.binding.tvIntroduction.text = item.introduction
        holder.binding.tvBrand.text = item.brand
        holder.binding.tvReleaseDate.text = item.releaseDate
        holder.binding.tvVideoTitle.text = item.videoTitle

        holder.binding.tags.setTags(item.tags)

        holder.binding.rvPreview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = PreviewPicRvAdapter(item)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            ItemHanimePreviewNews2Binding.inflate(
                LayoutInflater.from(context), parent, false
            ).root
        ).also { viewHolder ->
            viewHolder.binding.tags.lifecycle = (context as? PreviewActivity)?.lifecycle
            viewHolder.binding.tags.isCollapsedEnabled = false
            viewHolder.itemView.apply {
                setOnClickListener {
                    val position = viewHolder.bindingAdapterPosition
                    val item = getItem(position).notNull()
                    if (context is PreviewActivity) {
                        context.startActivity<VideoActivity>(VIDEO_CODE to item.videoCode)
                    }
                }
            }
        }
    }

    private inner class PreviewPicRvAdapter(private val item: HanimePreviewModel.PreviewInfo) :
        BaseQuickAdapter<String, QuickViewHolder>(item.relatedPicsUrl) {
        override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: String?) {
            holder.getView<ImageView>(R.id.iv_preview_news_pic).load(item) {
                crossfade(true)
            }
        }

        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup,
            viewType: Int,
        ): QuickViewHolder {
            return QuickViewHolder(
                R.layout.item_hanime_preview_news_pic, parent
            ).also { viewHolder ->
                viewHolder.itemView.setOnClickListener {
                    val position = viewHolder.bindingAdapterPosition
                    XPopup.Builder(context).asImageViewer(
                        it as? ImageView, position, item.relatedPicsUrl, { popupView, pos ->
                            popupView.updateSrcView(recyclerView.getChildAt(pos) as? ImageView)
                        }, imageLoader
                    ).show()
                }
            }
        }
    }
}