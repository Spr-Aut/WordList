package com.example.wordlist.bean;

import java.util.ArrayList;

public class BookBean {
    private int code;
    String msg;
    ArrayList<BookDataBean> datas;

    public class BookDataBean{
        String title;
        String word_num;
        String course_num;
        ArrayList<BookDataChildListBean> child_list;

        public class BookDataChildListBean{
            String title;
            String class_id;
            String word_num;
            String course_num;

            public BookDataChildListBean() {
            }

            public BookDataChildListBean(String title, String class_id, String word_num, String course_num) {
                this.title = title;
                this.class_id = class_id;
                this.word_num = word_num;
                this.course_num = course_num;
            }

            /**
             * 获取
             * @return title
             */
            public String getTitle() {
                return title;
            }

            /**
             * 设置
             * @param title
             */
            public void setTitle(String title) {
                this.title = title;
            }

            /**
             * 获取
             * @return class_id
             */
            public String getClass_id() {
                return class_id;
            }

            /**
             * 设置
             * @param class_id
             */
            public void setClass_id(String class_id) {
                this.class_id = class_id;
            }

            /**
             * 获取
             * @return word_num
             */
            public String getWord_num() {
                return word_num;
            }

            /**
             * 设置
             * @param word_num
             */
            public void setWord_num(String word_num) {
                this.word_num = word_num;
            }

            /**
             * 获取
             * @return course_num
             */
            public String getCourse_num() {
                return course_num;
            }

            /**
             * 设置
             * @param course_num
             */
            public void setCourse_num(String course_num) {
                this.course_num = course_num;
            }

            public String toString() {
                return "BookDataChildListBean{title = " + title + ", class_id = " + class_id + ", word_num = " + word_num + ", course_num = " + course_num + "}";
            }
        }

        public BookDataBean() {
        }

        public BookDataBean(String title, String word_num, String course_num, ArrayList<BookDataChildListBean> child_list) {
            this.title = title;
            this.word_num = word_num;
            this.course_num = course_num;
            this.child_list = child_list;
        }

        /**
         * 获取
         * @return title
         */
        public String getTitle() {
            return title;
        }

        /**
         * 设置
         * @param title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * 获取
         * @return word_num
         */
        public String getWord_num() {
            return word_num;
        }

        /**
         * 设置
         * @param word_num
         */
        public void setWord_num(String word_num) {
            this.word_num = word_num;
        }

        /**
         * 获取
         * @return course_num
         */
        public String getCourse_num() {
            return course_num;
        }

        /**
         * 设置
         * @param course_num
         */
        public void setCourse_num(String course_num) {
            this.course_num = course_num;
        }

        /**
         * 获取
         * @return child_list
         */
        public ArrayList<BookDataChildListBean> getChild_list() {
            return child_list;
        }

        /**
         * 设置
         * @param child_list
         */
        public void setChild_list(ArrayList<BookDataChildListBean> child_list) {
            this.child_list = child_list;
        }

        public String toString() {
            return "BookDataBean{title = " + title + ", word_num = " + word_num + ", course_num = " + course_num + ", child_list = " + child_list + "}";
        }


    }

    public BookBean() {
    }

    public BookBean(int code, String msg, ArrayList<BookDataBean> datas) {
        this.code = code;
        this.msg = msg;
        this.datas = datas;
    }

    /**
     * 获取
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置
     * @param code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取
     * @return datas
     */
    public ArrayList<BookDataBean> getDatas() {
        return datas;
    }

    /**
     * 设置
     * @param datas
     */
    public void setDatas(ArrayList<BookDataBean> datas) {
        this.datas = datas;
    }

    public String toString() {
        return "BookBean{code = " + code + ", msg = " + msg + ", datas = " + datas + "}";
    }



}
