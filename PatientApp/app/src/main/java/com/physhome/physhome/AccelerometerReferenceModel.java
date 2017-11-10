package com.physhome.physhome;
/**
 * Created by Meliksah on 8/2/2017.
 */

public class AccelerometerReferenceModel {
    private int cp_num_arm;
    private int cp_num_chest;
    private RecordedValues recorded_arm;
    private RecordedValues recorded_chest;
    private CheckPointValues cp_arm;
    private CheckPointValues cp_chest;
    private PeakValues peak_arm;
    private PeakValues peak_chest;

    public AccelerometerReferenceModel() {
    }

    public class RecordedValues{
        private int[] X;
        private int[] Y;
        private int[] Z;

        public int[] getX() {
            return X;
        }

        public void setX(int[] x) {
            X = x;
        }

        public int[] getY() {
            return Y;
        }

        public void setY(int[] y) {
            Y = y;
        }

        public int[] getZ() {
            return Z;
        }

        public void setZ(int[] z) {
            Z = z;
        }
    }
    public class CheckPointValues{
        private int[] X;
        private int[] Y;
        private int[] Z;
        private int[] Inc;

        public int[] getX() {
            return X;
        }

        public void setX(int[] x) {
            X = x;
        }

        public int[] getY() {
            return Y;
        }

        public void setY(int[] y) {
            Y = y;
        }

        public int[] getZ() {
            return Z;
        }

        public void setZ(int[] z) {
            Z = z;
        }

        public int[] getInc() {
            return Inc;
        }

        public void setInc(int[] inc) {
            Inc = inc;
        }
    }
    public class PeakValues {
        private int X;
        private int Y;
        private int Z;

        public int getX() {
            return X;
        }

        public void setX(int x) {
            X = x;
        }

        public int getY() {
            return Y;
        }

        public void setY(int y) {
            Y = y;
        }

        public int getZ() {
            return Z;
        }

        public void setZ(int z) {
            Z = z;
        }
    }

    public int getCp_num_arm() {
        return cp_num_arm;
    }

    public void setCp_num_arm(int cp_num_arm) {
        this.cp_num_arm = cp_num_arm;
    }

    public int getCp_num_chest() {
        return cp_num_chest;
    }

    public void setCp_num_chest(int cp_num_chest) {
        this.cp_num_chest = cp_num_chest;
    }

    public RecordedValues getRecorded_arm() {
        return recorded_arm;
    }

    public void setRecorded_arm(RecordedValues recorded_arm) {
        this.recorded_arm = recorded_arm;
    }

    public RecordedValues getRecorded_chest() {
        return recorded_chest;
    }

    public void setRecorded_chest(RecordedValues recorded_chest) {
        this.recorded_chest = recorded_chest;
    }

    public CheckPointValues getCp_arm() {
        return cp_arm;
    }

    public void setCp_arm(CheckPointValues cp_arm) {
        this.cp_arm = cp_arm;
    }

    public CheckPointValues getCp_chest() {
        return cp_chest;
    }

    public void setCp_chest(CheckPointValues cp_chest) {
        this.cp_chest = cp_chest;
    }

    public PeakValues getPeak_arm() {
        return peak_arm;
    }

    public void setPeak_arm(PeakValues peak_arm) {
        this.peak_arm = peak_arm;
    }

    public PeakValues getPeak_chest() {
        return peak_chest;
    }

    public void setPeak_chest(PeakValues peak_chest) {
        this.peak_chest = peak_chest;
    }
}

