package com.giiisp.giiisp.dto;

import java.util.List;

public class PaperMainVO {

    /**
     * id : asdfas4545d4f5s
     * title : 标题
     * authors : 作者,作者
     * subject : subject
     * useravatar : 用户头像
     * userorgeng : 单位编码
     * type : 1论文2综述
     * vlist : [{"id":"asdfas4545d4f5s","isfollow":"是否收藏 ","isdownload":"是否下载 ","isaddplay":"是否加入播放 ","version":"版本 ","cnfinish":"中文录音是否完成 ","enfinish":"英文录音是否完成 ","cnduration":"中文录音时长 ","enduration":"英文录音时长 ","cnsize":"中文录音文件大小 ","ensize":"英文录音文件大小 "}]
     */

    private String id;
    private String title;
    private String authors;
    private String subject;
    private String useravatar;
    private String userorgeng;
    private String usernmae;
    private String type;
    private String code;
    private String myLanguage = "1";//默认是中文
    private List<VlistBean> vlist;

    public String getMyLanguage() {
        return myLanguage;
    }

    public void setMyLanguage(String myLanguage) {
        this.myLanguage = myLanguage;
    }

    public String getUsername() {
        return usernmae;
    }

    public void setUsername(String username) {
        this.usernmae = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUseravatar() {
        return useravatar;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }

    public String getUserorgeng() {
        return userorgeng;
    }

    public void setUserorgeng(String userorgeng) {
        this.userorgeng = userorgeng;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<VlistBean> getVlist() {
        return vlist;
    }

    public void setVlist(List<VlistBean> vlist) {
        this.vlist = vlist;
    }

    public static class VlistBean {
        /**
         * id : asdfas4545d4f5s
         * isfollow : 是否收藏
         * isdownload : 是否下载
         * isaddplay : 是否加入播放
         * version : 版本
         * cnfinish : 中文录音是否完成
         * enfinish : 英文录音是否完成
         * cnduration : 中文录音时长
         * enduration : 英文录音时长
         * cnsize : 中文录音文件大小
         * ensize : 英文录音文件大小
         */

        private String id;
        private String isfollow;
        private String isdownload;
        private String isaddplay;
        private int version;
        private String cnfinish;
        private String enfinish;
        private String cnduration;
        private String enduration;
        private String cnsize;
        private String ensize;
        private boolean isEnglish;

        public boolean isEnglish() {
            return isEnglish;
        }

        public void setEnglish(boolean english) {
            isEnglish = english;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsfollow() {
            return isfollow;
        }

        public void setIsfollow(String isfollow) {
            this.isfollow = isfollow;
        }

        public String getIsdownload() {
            return isdownload;
        }

        public void setIsdownload(String isdownload) {
            this.isdownload = isdownload;
        }

        public String getIsaddplay() {
            return isaddplay;
        }

        public void setIsaddplay(String isaddplay) {
            this.isaddplay = isaddplay;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getCnfinish() {
            return cnfinish;
        }

        public void setCnfinish(String cnfinish) {
            this.cnfinish = cnfinish;
        }

        public String getEnfinish() {
            return enfinish;
        }

        public void setEnfinish(String enfinish) {
            this.enfinish = enfinish;
        }

        public String getCnduration() {
            return cnduration;
        }

        public void setCnduration(String cnduration) {
            this.cnduration = cnduration;
        }

        public String getEnduration() {
            return enduration;
        }

        public void setEnduration(String enduration) {
            this.enduration = enduration;
        }

        public String getCnsize() {
            return cnsize;
        }

        public void setCnsize(String cnsize) {
            this.cnsize = cnsize;
        }

        public String getEnsize() {
            return ensize;
        }

        public void setEnsize(String ensize) {
            this.ensize = ensize;
        }
    }
}
