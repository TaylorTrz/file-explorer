package com.taylor.tooz.utils.format;


/**
 * *****************************************************************
 * <p/>Transform text into formatted document<p/>
 * -----------------------------------------------------------------
 * <p/>
 * INPUT:
 * abc
 * <p/>
 * OUTPUT:
 * |-------|
 * |  abc  |
 * |-------|
 * <p/>
 * *****************************************************************
 *
 * @author taoruizhe
 * @since 2022/01/16
 */
public class DocFormatUtil {
    private static final String DEFAULT_SPLIT = "    ";
    private static final String DEFAULT_INDENT = "  ";
    private static final String DASH = "-";
    private static final String VERTICAL = "|";
    private static final String LF = "\n";

    private int width = 8;
    private int height = 10;
    /*  cache string for each line */
    private StringBuilder cacheStr;
    /*  all content in an array */
    private String[] content;
    /*  pointer for context index */
    private int ptr = 0;

    public DocBuilder getBuilder() {
        return getBuilder(0, 0);
    }

    public DocBuilder getBuilder(int width, int height) {
        if (width != 0) {
            this.width = width;
        }
        if (height != 0) {
            this.height = height;
        }
        content = new String[this.height];
        return new DocBuilder();
    }

    public class DocBuilder {
        public DocBuilder then(Object o) {
            if (cacheStr == null) {
                cacheStr = new StringBuilder();
            }
            cacheStr.append(DEFAULT_SPLIT).append(o.toString());
            return this;
        }

        public DocBuilder enter() {
            // initialize or resize
            if (content == null) {
                content = new String[height];
            }
            if (++ptr >= height) {
                height = height + (1 << 4);
                String[] nContent = new String[height];
                System.arraycopy(content, 0, nContent, 0, content.length);
                content = nContent;
            }
            // flush cached string
            flush();
            return this;
        }

        private void flush() {
            if (cacheStr == null || cacheStr.length() == 0) {
                return;
            }
            for (int c = 0; c < content.length; c++) {
                if (content[c] == null || content[c].isEmpty()) {
                    // to ensure enough width
                    if (cacheStr.length() >= width - 2) {
                        width = width + (cacheStr.length() - width) + 4;
                    }
                    content[c] = cacheStr.toString();
                    break;
                }
            }
            cacheStr = null;
        }

        private String repeatStr(String s, int count) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < count; i++) {
                buffer.append(s);
            }
            return buffer.toString();
        }

        public String build() {
            flush();
            StringBuilder doc = new StringBuilder()
                    .append(VERTICAL)
                    .append(repeatStr(DASH, width))
                    .append(VERTICAL)
                    .append(LF);
            for (String s : content) {
                if (s == null || s.isEmpty()) {
                    break;
                }
                doc.append(VERTICAL)
                        .append(s)
                        .append(repeatStr(" ", width - s.length()))
                        .append(VERTICAL).append(LF);
            }
            doc.append(VERTICAL)
                    .append(repeatStr(DASH, width))
                    .append(VERTICAL)
                    .append(LF);
            return doc.toString();
        }
    }


    public static void main(String[] args) {
        System.out.println(new DocFormatUtil().
                getBuilder(0, 0)
                .then("abc").then("ufo").enter()
                .then("uvf").then("good").enter()
                .then("loooooooooooooooooooooooooooooooooooooooooooo0000000000000000000000000ooog")
                .build());
    }
}
