package alexa.projectcharizard.Model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import alexa.projectcharizard.R;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder{
    /*public TextView name, description;

    public MyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.txtShowName);
        description = (TextView) itemView.findViewById(R.id.txtShowDiscription);
    }*/

    public TextView name, latitude, longitude, description, visibility;

    public MyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.txtShowName);
        latitude = (TextView) itemView.findViewById(R.id.txtShowLat);
        longitude = (TextView) itemView.findViewById(R.id.txtShowLong);
        description = (TextView) itemView.findViewById(R.id.txtShowDiscription);
        visibility = (TextView) itemView.findViewById(R.id.txtShowVisibility);

    }


}
