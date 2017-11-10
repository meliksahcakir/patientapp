package com.physhome.physhome;

/**
 * Created by Meliksah on 11/7/2017.
 */

public class PhyshomePatientDeviceModel {
    private String _id;
    private String mac_address;
    private String device_name;
    private String doctor_id;
    private String patient_id;
    private String che_limb;

    public PhyshomePatientDeviceModel(String _id, String mac_address, String device_name, String doctor_id, String patient_id, String che_limb) {
        this._id = _id;
        this.mac_address = mac_address;
        this.device_name = device_name;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.che_limb = che_limb;
    }

    public PhyshomePatientDeviceModel() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getChe_limb() {
        return che_limb;
    }

    public void setChe_limb(String che_limb) {
        this.che_limb = che_limb;
    }

    @Override
    public String toString() {
        return device_name+" ("+mac_address+")";
    }
}
