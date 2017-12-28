package com.sftc.web.model.others;

import lombok.Getter;
import net.sf.json.JSONObject;

/**
 * @author ： CatalpaFlat
 * @date ：Create in 16:42 2017/11/29
 */
public class WeightSegment {

    private static final String PACKAGE_SMALL_CONTENTS = "小包裹";
    private static final String PACKAGE_MIDDLE_CONTENTS = "中包裹";
    private static final String PACKAGE_BIG_CONTENTS = "大包裹";
    private static final String PACKAGE_SUPER_BIG_CONTENTS = "超大包裹";
    private static final String PACKAGE_SMALL_ICON = "https://sf.dankal.cn/icn_package_small_copy.png";
    private static final String PACKAGE_MIDDLE_ICON = "https://sf.dankal.cn/icn_package_middle_copy.png";
    private static final String PACKAGE_BIG_ICON = "https://sf.dankal.cn/icn_package_big_copy%20.png";
    private static final String PACKAGE_SUPER_BIG_ICON = "https://sf.dankal.cn/icn_package_super_copy.png";
    private static final String PACKAGE_SMALL_ICON_SELECTED = "https://sf.dankal.cn/icn_package_small_selected.png";
    private static final String PACKAGE_MIDDLE_ICON_SELECTED = "https://sf.dankal.cn/icn_package_middle_selected.png";
    private static final String PACKAGE_BIG_ICON_SELECTED = "https://sf.dankal.cn/icn_package_big_selected.png";
    private static final String PACKAGE_SUPER_BIG_ICON_SELECTED = "https://sf.dankal.cn/icn_package_super_selected.png";
    private static final int LAST_SIZE = 4;
    private static final String JSON_KEY_WEIGHTOBJ = "weightOBJ";
    private static final String JSON_KEY_TYPE = "type";

    /**
     * name : 0~5Kg
     * weight : 3
     * contents : 小包裹
     * package_icon : https://sf.dankal.cn/icn_package_small_copy.png
     * package_icon_selected : https://sf.dankal.cn/icn_package_small_seleted.png
     */
    @Getter
    private String name;
    @Getter
    private String weight;
    @Getter
    private String contents;
    @Getter
    private String package_icon;
    @Getter
    private String package_icon_selected;
    private JSONObject weightOBJ;
    private int type;

    private WeightSegment(JSONObject weightOBJ, int type, int size) {
        this.weightOBJ = weightOBJ;
        this.type = type;
        init(size);
    }

    private void init(int size) {
        this.name = weightOBJ.getString("name");
        this.weight = weightOBJ.getString("weight");
        switch (type) {
            case 0:
                this.contents = PACKAGE_SMALL_CONTENTS;
                this.package_icon = PACKAGE_SMALL_ICON;
                this.package_icon_selected = PACKAGE_SMALL_ICON_SELECTED;
                break;
            case 1:
                this.contents = PACKAGE_MIDDLE_CONTENTS;
                this.package_icon = PACKAGE_MIDDLE_ICON;
                this.package_icon_selected = PACKAGE_MIDDLE_ICON_SELECTED;
                break;
            case 2:
                this.contents = PACKAGE_BIG_CONTENTS;
                this.package_icon = PACKAGE_BIG_ICON;
                this.package_icon_selected = PACKAGE_BIG_ICON_SELECTED;
                break;
            case 3:
                this.contents = PACKAGE_SUPER_BIG_CONTENTS;
                this.package_icon = PACKAGE_SUPER_BIG_ICON;
                this.package_icon_selected = PACKAGE_SUPER_BIG_ICON_SELECTED;
                break;
            default:
                this.contents = PACKAGE_SUPER_BIG_CONTENTS;
                this.package_icon = PACKAGE_SUPER_BIG_ICON;
                this.package_icon_selected = PACKAGE_SUPER_BIG_ICON_SELECTED;
                break;
        }
        if (size > LAST_SIZE) {
            this.contents = null;
        }
    }

    public static JSONObject getWeightSegmentJson(JSONObject weightOBJ, int type, int size) {
        WeightSegment weightSegment = new WeightSegment(weightOBJ, type, size);
        JSONObject jsonResult = JSONObject.fromObject(weightSegment);
        jsonResult.remove(JSON_KEY_WEIGHTOBJ);
        jsonResult.remove(JSON_KEY_TYPE);
        return jsonResult;
    }
}
