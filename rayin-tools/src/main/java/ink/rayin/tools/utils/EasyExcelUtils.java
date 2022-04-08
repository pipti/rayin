package ink.rayin.tools.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.SyncReadListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class EasyExcelUtils {

    /**
     * 根据excel输入流，读取excel文件，忽略第一行，返回第一行头名字对应的json
     *
     * @param inputStream exece表格的输入流
     * @return 返回Json数据字符串
     **/
    public static String readWithoutHead(InputStream inputStream) throws IOException {
        SyncReadListener listener1 = new SyncReadListener();
        byte[] b = FileUtil.copyToByteArray(inputStream);
        ExcelReader excelReader1 = EasyExcel.read(new ByteArrayInputStream(b),  listener1).headRowNumber(0).build();
        excelReader1.readAll();
        List<Object> list = listener1.getList();

        if(list.size() == 0){
            return null;
        }
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(gson.toJson(list),JsonArray.class);

        Set<Map.Entry<String, JsonElement>> head = jsonArray.get(0).getAsJsonObject().entrySet();
        list.remove(0);
        String str = gson.toJson(list);
        for(Map.Entry<String, JsonElement> h:head){
            str = str.replace("\"" + h.getKey()+"\":",h.getValue()+":");
        }
        log.debug(str);
        return str;
    }
}
