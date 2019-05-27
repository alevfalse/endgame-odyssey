package com.enhanced.endgameodyssey;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * This is MovieAdapter class which extends the RecyclerView.Adapter.
 * We pass the MovieHolder so that the RecyclerView knows that it is the ViewHolder we want to use.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private List<Movie> movies = new ArrayList<>();
    private OnItemClickListener listener;

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
        Movie movie = movies.get(position);

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

    // Here we have to return how many itemViews we want to display in our RecyclerView.
    // We want to display as many items as there are in our movies ArrayList.
    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    // In the MainActivity, we observe the LiveData and on the onChange callback, we pass a
    // List of Movies and this is the method used to get this List of movies into the RecyclerView.
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged(); // We notify the Adapter that it has to draw the layout
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

                    // Make sure that the member variable listener is set and that the adapter returns
                    // a valid position before calling the listener's onItemClick method.
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        Movie movie = movies.get(position);

                        // Check if the movie is the current movie to be watched before letting the user view it.
                        if (movie.isCurrent()) {
                            // Execute the listener's onItemClick method which was implemented in the MainActivity
                            listener.onItemClick(movies.get(position));

                        } else if (!movie.isWatched()){
                            Toast.makeText(itemView.getContext(), "You haven't unlocked that movie yet.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    // Whatever implements this interface also has to implement the onItemClick method to make sure
    // that whenever we have an OnTimeClickListener for our MovieAdapter, the listener also has the onItemClick() method.
    // We implement this interface in the MainActivity allowing us to call this method on the MainActivity and pass the movie to it.
    // This is set to interface so that the implementor has the power to perform whatever actions they want whenever an item is clicked.
    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    // We can use the OnItemClickListener instance provided by whatever implemented its interface
    // to set this MovieAdapter's member variable listener and call the onItemClick method on it
    // which was implemented as well in the MainActivity as well.
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
