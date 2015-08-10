package com.warfarin_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.util.Log;

import com.warfarin_app.db.DbUtil;


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
    private Patient patient;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
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

        etName = (EditText) this.getView().findViewById(R.id.patient_etName);
        rgGender = (RadioGroup) this.getView().findViewById(R.id.patient_rgGender);
        rbMale = (RadioButton) this.getView().findViewById(R.id.patient_rbMale);
        rbFemale = (RadioButton) this.getView().findViewById(R.id.patient_rbFemale);
        etBirthday = (EditText) this.getView().findViewById(R.id.patient_etBirthday);
        etDoctor = (EditText) this.getView().findViewById(R.id.patient_etDoctor);
        rgIsWarfarin = (RadioGroup) this.getView().findViewById(R.id.patient_rgIsWarfarin);
        rbIsMarfarinYes = (RadioButton) this.getView().findViewById(R.id.patient_rbWarfarinYes);

        loadPatient();
    }

    public void onClick(View v)
    {
        if (v == btOK)
        {
            updatePatientFromUI();
            savePatient();
        }
    }

    public void savePatient()
    {
        DbUtil.savePatient(patient);
    }

    public void loadPatient()
    {
        patient = new Patient();
        patient.setName("No Name");
        patient.setGender(true);
        patient.setBirthday("1999/01/02");
        patient.setDoctor("Black Jack");
        patient.setIsWarfarin(true);

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

    }

    private void updatePatientFromUI()
    {
        patient.setName(etName.getText().toString());
        patient.setBirthday(etBirthday.getText().toString());
        patient.setDoctor(etDoctor.getText().toString());
        patient.setGender(rbMale.isChecked());
        patient.setIsWarfarin(rbIsMarfarinYes.isChecked());
    }

}
