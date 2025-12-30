package com.odc.aws_learning.app.constante;

public class Enumeration {
    public enum COURSE_STATUT {
        Learning("Learning"),
        Valide("Valide");
        private final String desc;

        COURSE_STATUT(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }


}
