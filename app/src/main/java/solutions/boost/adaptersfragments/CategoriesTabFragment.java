package solutions.boost.adaptersfragments;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import solutions.boost.tvprogramm.ChannelsForCategoryActivity;
import solutions.boost.tvprogramm.R;

/**
 * Created on 16.01.2017.
 * Tab Fragment for list of channels
 */
public class CategoriesTabFragment extends BasicTabFragment
{
    public void setAdapterAndList()
    {
        Cursor cursor = getDataBaseInstance().getCursorCategoriesInTable();
        ListView list = getListViewInstance();

        if(cursor != null)
        {
            CategoryAdapter adapter = new CategoryAdapter
                    (getContext(), R.layout.category_item_layout, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

            list.setAdapter(adapter);

            //cursor.close();  //view is empty if cursor is closed ((((
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                TextView idcontainer = (TextView) view.findViewById(R.id.category_id_container);
                String id_s = idcontainer.getText().toString();

                Intent i = new Intent(getContext(), ChannelsForCategoryActivity.class);
                i.putExtra("KEY_ID", id_s); //with this key we retrieve channels for this category

                startActivity(i);
            }
        });
    }

}