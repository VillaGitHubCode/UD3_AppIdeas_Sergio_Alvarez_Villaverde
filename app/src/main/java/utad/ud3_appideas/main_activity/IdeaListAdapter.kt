package utad.ud3_appideas.main_activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import utad.ud3_appideas.databinding.ItemIdeaListBinding
import utad.ud3_appideas.room.models.Idea

class IdeaListAdapter(
    val goToDetail: (ideaId: Int)->Unit,
    val deleteIdea: (idea: Idea) -> Unit
): ListAdapter<Idea, IdeaListAdapter.IdeaListAdapterViewHolder> (IdeaItemCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaListAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemIdeaListBinding = ItemIdeaListBinding.inflate(inflater, parent, false)
        return IdeaListAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IdeaListAdapterViewHolder, position: Int) {
        val idea = getItem(position)

        holder.binding.tvIdeaName.text = idea.name
        holder.binding.tvDescriptionIdea.text = idea.description
        holder.binding.tvPriorityIdea.text = idea.priority
        holder.binding.tvProgressIdea.text = idea.progress

        holder.binding.root.setOnClickListener { goToDetail(idea.id) }
        holder.binding.btnDeleteItem.setOnClickListener { deleteIdea(idea) }
    }

    inner class IdeaListAdapterViewHolder(val binding: ItemIdeaListBinding): RecyclerView.ViewHolder(binding.root)

}

object IdeaItemCallBack : DiffUtil.ItemCallback<Idea>(){
    override fun areItemsTheSame(oldItem: Idea, newItem: Idea): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Idea, newItem: Idea): Boolean {
        return oldItem == newItem
    }

}
