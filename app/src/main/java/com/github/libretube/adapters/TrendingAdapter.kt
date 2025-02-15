package com.github.libretube.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.github.libretube.databinding.TrendingRowBinding
import com.github.libretube.dialogs.VideoOptionsDialog
import com.github.libretube.extensions.setFormattedDuration
import com.github.libretube.obj.StreamItem
import com.github.libretube.util.ConnectionHelper
import com.github.libretube.util.NavigationHelper
import com.github.libretube.util.formatShort
import com.github.libretube.util.setWatchProgressLength
import com.github.libretube.util.toID

class TrendingAdapter(
    private val streamItems: List<StreamItem>,
    private val childFragmentManager: FragmentManager,
    private val showAllAtOne: Boolean = true
) : RecyclerView.Adapter<SubscriptionViewHolder>() {

    var index = 10

    override fun getItemCount(): Int {
        return if (showAllAtOne) streamItems.size
        else if (index >= streamItems.size) streamItems.size - 1
        else index
    }

    fun updateItems() {
        index += 10
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TrendingRowBinding.inflate(layoutInflater, parent, false)
        return SubscriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        val trending = streamItems[position]
        holder.binding.apply {
            textViewTitle.text = trending.title
            textViewChannel.text =
                trending.uploaderName + " • " +
                trending.views.formatShort() + " • " +
                DateUtils.getRelativeTimeSpanString(trending.uploaded!!)
            thumbnailDuration.setFormattedDuration(trending.duration!!)
            channelImage.setOnClickListener {
                NavigationHelper.navigateChannel(root.context, trending.uploaderUrl)
            }
            ConnectionHelper.loadImage(trending.thumbnail, thumbnail)
            ConnectionHelper.loadImage(trending.uploaderAvatar, channelImage)
            root.setOnClickListener {
                NavigationHelper.navigateVideo(root.context, trending.url)
            }
            val videoId = trending.url!!.toID()
            root.setOnLongClickListener {
                VideoOptionsDialog(videoId)
                    .show(childFragmentManager, VideoOptionsDialog::class.java.name)
                true
            }
            watchProgress.setWatchProgressLength(videoId, trending.duration!!)
        }
    }
}

class SubscriptionViewHolder(val binding: TrendingRowBinding) :
    RecyclerView.ViewHolder(binding.root)
