package com.odc.aws_learning.auth.base.response.utils;

public class Enumeration {

    public enum TYPE_QUESTION {
        text("Paragraphe"),
        number("Nombre"),
        checkbox("Cases à cocher"),
        select("Liste déroulante"),
        date("Date");

        private final String desc;

        TYPE_QUESTION(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum STATE_FORM {

        not_traited("Non traité"),
        accepted("Accepté"),
        reject("Rejété");

        private final String desc;

        STATE_FORM(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum TYPE_ALERT {

        focal_point("POINT FOCAL"),
        app_mobile("APP MOBILE");

        private final String desc;

        TYPE_ALERT(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum AUTHOR_IDENTITY {

        anonymous("Anonyme"),
        Identified("Identifié");

        private final String desc;

        AUTHOR_IDENTITY(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
