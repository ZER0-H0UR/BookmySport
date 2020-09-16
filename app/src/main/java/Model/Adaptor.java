package Model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.bookmysport.DetailsActivity;
import com.ahmed.bookmysport.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import Objects.Venue;

public class Adaptor extends RecyclerView.Adapter<Adaptor.myViewHolder> implements Filterable {
    private Context context;
    private List<Venue> venueList;
    private List<Venue> venueListAll;

    public Adaptor(Context context, List<Venue> list) {
        this.context = context;
        this.venueList = list;
        this.venueListAll= new ArrayList<>(venueList);
    }

    @NonNull
    @Override
    public Adaptor.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_row,parent,false);
        return new myViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptor.myViewHolder holder, int position) {
        Venue venue= venueList.get(position);
        holder.backgroudImage.setImageResource(venueList.get(position).getBackground());
        holder.title.setText(venueList.get(position).getVenueName());
        holder.description.setText(venueList.get(position).getVenueDescription());
        holder.location.setText(venueList.get(position).getLocation());

        /*Getting the date of booking dynamically from time of the system
        java.text.DateFormat dateFormat= java.text.DateFormat.getDateInstance();
        String bookingDate= dateFormat.format(new Date(Long.valueOf(venue.getBookingDateFromUser())));*/
    }

    @Override
    public int getItemCount() {
        return venueList.size();
    }

    //This is the filter method to filter the recycler view according the the search field input by the user
    @Override
    public Filter getFilter() {
        return filter;
    }

    //Here is all the logic for filtering goes inside this method
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Venue> filteredList = new ArrayList<>();
            //check if the CharSequence is empty
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(venueListAll);
            } else {
                String filterPattern=constraint.toString().toLowerCase().trim();
                for (Venue item: venueListAll ){
                    if (item.getVenueName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }else if (item.getLocation().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }else if(item.getVenueDescription().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return  results;
        }

        //run on ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            venueList.clear();
            venueList.addAll((Collection<? extends Venue>) results.values);
            notifyDataSetChanged();
        }
    };

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //widgets set as public as we want them to be seen when the adaptor is attached to recyclerview
        public ImageView backgroudImage,profileImage;
        public TextView title,description,location;
        public Button book;

        public myViewHolder(@NonNull View view, Context ctx) {
            super(view);
            context=ctx;
            //declare widgets to be viewed
            backgroudImage=(ImageView) view.findViewById(R.id.venueFeildImageID);
            profileImage=(ImageView) view.findViewById(R.id.profileImageID);
            title=(TextView) view.findViewById(R.id.venueTitleID);
            description=(TextView) view.findViewById(R.id.venueDescriptionID);
            location=(TextView) view.findViewById(R.id.locationTextViewID);
            book=(Button) view.findViewById(R.id.bookButtonID);
            book.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            // click to book and go to venue description and book venue
            int position=getAdapterPosition();
            Venue venue= venueList.get(position);

            //Here as soon as the button is click the activity will go to a new activity so that user can book his venue and shows all details "According to the item selected"
            Intent intent=new Intent(context, DetailsActivity.class);
            intent.putExtra("background",venueList.get(position).getBackground());
            intent.putExtra("venueName",venueList.get(position).getVenueName());
            intent.putExtra("sportType",venueList.get(position).getVenueDescription());
            intent.putExtra("location",venueList.get(position).getLocation());
            intent.putExtra("sporticon",venueList.get(position).getSportTypeLogo());
            intent.putExtra("bathroom",venueList.get(position).isHasBathroom());
            intent.putExtra("carpark",venueList.get(position).isHasCarPark());
            intent.putExtra("firstaid",venueList.get(position).isHasFirstAid());
            intent.putExtra("lockers",venueList.get(position).isHasLocker());
            intent.putExtra("resturant",venueList.get(position).isHasResturant());
            intent.putExtra("wifi",venueList.get(position).isHasWifi());
            context.startActivity(intent);
        }
    }
}
