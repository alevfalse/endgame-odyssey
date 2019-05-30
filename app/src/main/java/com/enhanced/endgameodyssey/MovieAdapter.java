package com.enhanced.endgameodyssey;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This is MovieAdapter class which extends the ListAdapter.
 * We pass the Movie class to set the type of data it will hold and MovieHolder
 * so knows that it knows it is the ViewHolder we want to use.
 */
public class MovieAdapter extends ListAdapter<Movie, MovieAdapter.MovieHolder> {
    private OnItemClickListener clickListener;

    public MovieAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        // Check if the data source contains the same items.
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }

        // Check if the items' individual properties are the same.
        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.isWatched() == newItem.isWatched() &&
                   oldItem.isCurrent() == newItem.isCurrent();
        }
    };

    // This is where we create and return the MovieHolder that will the hold the items in our RecyclerView.
    // We then set the layout that we want to use for the items in our RecyclerView.
    // The parameter 'parent' is the RecyclerView and since it is on the MainActivity,
    // we can get the context from it using getContext().
    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We create a view using the layout movie_item that we have created.
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);

        return new MovieHolder(itemView);
    }

    // This is where get the data from the Java object movies into the views of MovieHolder.
    // We set the text of the views of the MovieHolder based on its position in the RecyclerView.
    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {

        // getItem() is a ListAdapter method
        Movie movie = getItem(position);

        holder.textViewTitle.setText(movie.getTitle());
        holder.textViewDescription.setText(movie.getDescription());
        holder.textViewPosition.setText(String.valueOf(movie.getTimelinePosition()));

        if (movie.isWatched()) {
            System.out.println(movie.getId() + " " + movie.isWatched() +
                    ": Setting " + movie.getTitle() + " background color to lime green.");
            holder.itemView.setBackgroundColor(Color.rgb(50,205,50)); // lime green
        } else {

            if (movie.isCurrent()) {
                System.out.println(movie.getId() + " " + movie.isWatched() +
                        ": Setting " + movie.getTitle() + " background color to grey.");
                holder.itemView.setBackgroundColor(Color.rgb(255,255,255)); // white
            } else {
                System.out.println(movie.getId() + " " + movie.isWatched() +
                        ": Setting " + movie.getTitle() + " background color to grey.");
                holder.itemView.setBackgroundColor(Color.rgb(105,105,105)); // grey
            }

        }
    }

    public Movie getMovieAt(int position) {
        return getItem(position);
    }

    // This class will hold the views in the RecyclerView which in this case are the CardViews of movie_item.
    // A ViewHolder describes an item view and metadata about its place within the RecyclerView.
    class MovieHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPosition;

        // We pass the view which is the CardView that contains the TextViews to the constructor
        // and set the views accordingly. This constructor is called whenever the MovieAdapter's
        // onCreateViewHolder method is executed.
        public MovieHolder(@NonNull final View itemView) {
            super(itemView);

            // Set each TextView's layout
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPosition = itemView.findViewById(R.id.text_view_position);

            // Set an anonymous View.OnClickListener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    // Make sure that the member variable clickListener is set and that the adapter returns
                    // a valid position before calling the clickListener's onItemClick method.
                    if (clickListener != null && position != RecyclerView.NO_POSITION) {

                        // Execute the clickListener's onItemClick method which was implemented in the MainActivity
                        clickListener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    // Whatever implements this interface also has to implement the onItemClick method to make sure
    // that whenever we have an OnTimeClickListener for our MovieAdapter, the clickListener also has the onItemClick() method.
    // We implement this interface in the MainActivity allowing us to call this method on the MainActivity and pass the movie to it.
    // This is set to interface so that the implementor has the power to perform whatever actions they want whenever an item is clicked.
    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    // We can use the OnItemClickListener instance provided by whatever implemented its interface
    // to set this MovieAdapter's member variable clickListener and call the onItemClick method on it
    // which was implemented as well in the MainActivity as well.
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
