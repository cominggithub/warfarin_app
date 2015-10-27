package com.warfarin_app.view;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.warfarin_app.R;
import com.warfarin_app.data.Patient;
import com.warfarin_app.db.DbUtil;
import com.warfarin_app.transfer.BTManager;
import com.warfarin_app.transfer.BTUtil;
import com.warfarin_app.util.SystemInfo;


/**
 * Created by Coming on 8/5/15.
 */
public class PatientFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private String value = "";
    private Button btOK;
    private EditText etName;
    private EditText etDoctor;
    private RadioGroup rgGender;
    private RadioGroup rgIsWarfarin;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private RadioButton rbIsMarfarinYes;
    private EditText etBirthday;
    private EditText etBlueDevName;
    private Patient patient;
    private Button btSetBlueDev;

    private MainActivity mainActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity)activity;
        Log.d("app", "onAttach");
//        value = mainActivity.getPersonalProfileData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v;
//        v = inflater.inflate(R.layout.patient, container, false);


        Log.d("app", "onCreateView");
        return inflater.inflate(R.layout.patient, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        TextView txtResult = (TextView) this.getView().findViewById(R.id.textView1);
//        txtResult.setText(value);

        Log.d("app", "onActivityCreated");
        btOK = (Button) this.getView().findViewById(R.id.patient_btPatientOk);
        btOK.setOnClickListener(this);

        btSetBlueDev = (Button) this.getView().findViewById(R.id.patient_btSetBlueDev);
        btSetBlueDev.setOnClickListener(this);


        etName = (EditText) this.getView().findViewById(R.id.patient_etName);
        rgGender = (RadioGroup) this.getView().findViewById(R.id.patient_rgGender);
        rbMale = (RadioButton) this.getView().findViewById(R.id.patient_rbMale);
        rbFemale = (RadioButton) this.getView().findViewById(R.id.patient_rbFemale);
        etBirthday = (EditText) this.getView().findViewById(R.id.patient_etBirthday);
        etDoctor = (EditText) this.getView().findViewById(R.id.patient_etDoctor);
        rgIsWarfarin = (RadioGroup) this.getView().findViewById(R.id.patient_rgIsWarfarin);
        rbIsMarfarinYes = (RadioButton) this.getView().findViewById(R.id.patient_rbWarfarinYes);
        etBlueDevName = (EditText) this.getView().findViewById(R.id.patient_etBlueDevName);

        loadPatient();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        savePatient();
    }

    public void onClick(View v)
    {
        if (v == btOK)
        {
            updatePatientFromUI();
            savePatient();
        }
        else if (v == btSetBlueDev)
        {
            selectBlueDev();
        }
    }

    public void selectBlueDev()
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(new ContextThemeWrapper(mainActivity, R.style.myDialog));
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("選擇藍芽裝置");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                mainActivity,
                android.R.layout.select_dialog_singlechoice);

        if (SystemInfo.isBluetooth) {
            for (BluetoothDevice device : BTUtil.getPairedDevice()) {
                arrayAdapter.add(device.getName() + " " + device.getAddress());
            }
        }
        else
        {
            arrayAdapter.add("BLUETEH 11:22:33:44:55:66:88:00");
            arrayAdapter.add("AA:BB:CC:DD:EE:FF AA:BB:CC:DD:EE:FF");
        }


        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etBlueDevName.setText(
                            BTUtil.getNameFromNameAddress(arrayAdapter.getItem(which)) + " " +
                                    BTUtil.getAddressFromNameAddress(arrayAdapter.getItem(which))


                        );
                        BTManager.setDeviceByAddress(BTUtil.getAddressFromNameAddress(arrayAdapter.getItem(which)));
                        savePatient();
                    }
                });
        builderSingle.show();
    }

    public void savePatient()
    {
        updatePatientFromUI();
        DbUtil.savePatient(patient);

    }

    public void loadPatient()
    {
        patient = new Patient();
        patient.setName("尚未設定");
        patient.setGender(true);
        patient.setBirthday("1999/01/02");
        patient.setDoctor("尚未設定");
        patient.setIsWarfarin(true);
        patient.setBlueDevName("尚未設定");

        DbUtil.loadPatient(patient);
        updatePatientToUI();

    }

    private void updatePatientToUI()
    {
        etName.setText(patient.getName(), TextView.BufferType.EDITABLE);
        etBirthday.setText(patient.getBirthday(), TextView.BufferType.EDITABLE);
        etDoctor.setText(patient.getDoctor(), TextView.BufferType.EDITABLE);

        rbMale.setChecked(patient.getGender());
        rbFemale.setChecked(!rbMale.isChecked());
        rbIsMarfarinYes.setChecked(patient.getIsWarfarin());
        etBlueDevName.setText(patient.getBlueDevName() + " " + patient.getBlueDevAddress());

    }

    private void updatePatientFromUI()
    {
        patient.setName(etName.getText().toString());
        patient.setBirthday(etBirthday.getText().toString());
        patient.setDoctor(etDoctor.getText().toString());
        patient.setGender(rbMale.isChecked());
        patient.setIsWarfarin(rbIsMarfarinYes.isChecked());
        patient.setBlueDevName(BTUtil.getNameFromNameAddress((etBlueDevName.getText().toString())));
        patient.setBlueDevAddress(BTUtil.getAddressFromNameAddress((etBlueDevName.getText().toString())));
    }

}
