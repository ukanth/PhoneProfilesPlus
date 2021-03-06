package sk.henrichg.phoneprofilesplus;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

class BluetoothNamePreferenceAdapter extends BaseAdapter
{
    BluetoothNamePreference preference;
    Context context;

    private LayoutInflater inflater;
    //private Context context;

    BluetoothNamePreferenceAdapter(Context context, BluetoothNamePreference preference)
    {
        this.preference = preference;
        this.context = context;

        // Cache the LayoutInflate to avoid asking for a new one each time.
        inflater = LayoutInflater.from(context);
        //this.context = context; 
    }

    public int getCount() {
        return preference.bluetoothList.size();
    }

    public Object getItem(int position) {
        return preference.bluetoothList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    static class ViewHolder {
        TextView bluetoothName;
        CheckBox checkBox;
        ImageView itemEditMenu;
        int position;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // BluetoothDevice to display
        BluetoothDeviceData bluetoothDevice = preference.bluetoothList.get(position);
        //System.out.println(String.valueOf(position));

        ViewHolder holder;
        
        View vi = convertView;
        if (convertView == null)
        {
            vi = inflater.inflate(R.layout.bluetooth_name_preference_list_item, parent, false);
            holder = new ViewHolder();
            holder.bluetoothName = (TextView)vi.findViewById(R.id.bluetooth_name_pref_dlg_item_label);
            holder.checkBox = (CheckBox) vi.findViewById(R.id.bluetooth_name_pref_dlg_item_checkbox);
            holder.itemEditMenu = (ImageView)  vi.findViewById(R.id.bluetooth_name_pref_dlg_item_edit_menu);
            vi.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)vi.getTag();
        }

        if (bluetoothDevice.getName().equals(EventPreferencesBluetooth.ALL_BLUETOOTH_NAMES_VALUE))
            holder.bluetoothName.setText("[DU] " + context.getString(R.string.bluetooth_name_pref_dlg_all_bt_names_chb));
        else
        if (bluetoothDevice.getName().equals(EventPreferencesBluetooth.CONFIGURED_BLUETOOTH_NAMES_VALUE))
            holder.bluetoothName.setText("[DU] " + context.getString(R.string.bluetooth_name_pref_dlg_configured_bt_names_chb));
        else {
            if (android.os.Build.VERSION.SDK_INT >= 18) {
                String sType;
                if (bluetoothDevice.type == BluetoothDevice.DEVICE_TYPE_CLASSIC)
                    sType = "CL";
                else if (bluetoothDevice.type == BluetoothDevice.DEVICE_TYPE_DUAL)
                    sType = "DU";
                else if (bluetoothDevice.type == BluetoothDevice.DEVICE_TYPE_LE)
                    sType = "LE";
                else
                    sType = "??";

                holder.bluetoothName.setText("[" + sType + "] " + bluetoothDevice.getName());
            } else
                holder.bluetoothName.setText(bluetoothDevice.getName());
        }

        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(preference.isBluetoothNameSelected(bluetoothDevice.getName()));
        holder.checkBox.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                CheckBox chb = (CheckBox) v;

                int index = (Integer)chb.getTag();
                //Log.d("BluetoothNamePreferenceAdapter.getView","index="+index);
                String bluetoothName = preference.bluetoothList.get(index).getName();
                //Log.d("BluetoothNamePreferenceAdapter.getView","bluetoothName="+bluetoothName);

                if (chb.isChecked())
                    preference.addBluetoothName(bluetoothName);
                else
                    preference.removeBluetoothName(bluetoothName);
            }
        });

        if (!bluetoothDevice.custom)
            holder.itemEditMenu.setVisibility(View.GONE);
        else
            holder.itemEditMenu.setVisibility(View.VISIBLE);
        holder.itemEditMenu.setTag(position);
        final ImageView itemEditMenu = holder.itemEditMenu;
        holder.itemEditMenu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                preference.showEditMenu(itemEditMenu);
            }
        });

        return vi;
    }

}
