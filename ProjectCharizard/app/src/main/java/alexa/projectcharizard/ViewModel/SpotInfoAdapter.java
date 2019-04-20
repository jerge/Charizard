package alexa.projectcharizard.ViewModel;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import alexa.projectcharizard.Model.EasySpot;
import alexa.projectcharizard.R;



public class SpotInfoAdapter extends ArrayAdapter<EasySpot> {
    private Activity context;
    private List<EasySpot> spotList;

    public SpotInfoAdapter(Activity context, List<EasySpot> spotList){
        super(context, R.layout.list_view, spotList);
        this.context = context;
        this.spotList = spotList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.list_view, null, true);

        TextView spotName = (TextView) listView.findViewById(R.id.list_textView1);
        TextView spotLatitude = (TextView) listView.findViewById(R.id.list_textView2);
        TextView spotLongitude = (TextView) listView.findViewById(R.id.list_textView3);
        TextView spotDescription = (TextView) listView.findViewById(R.id.list_textView4);
        TextView spotCategory = (TextView) listView.findViewById(R.id.list_textView5);

        EasySpot spot = spotList.get(position);

        spotName.setText(spot.getName());
        spotLatitude.setText(spot.getLatitude());
        spotLongitude.setText(spot.getLongitude());
        spotDescription.setText(spot.getDescription());
        spotCategory.setText(spot.getCategory());

        return listView;
    }
}
