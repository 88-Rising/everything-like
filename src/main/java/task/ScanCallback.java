package task;

import java.io.File;

public interface ScanCallback {

    //对于文件夹的扫描任务进行回调,处理文件夹
    void callback(File dir);
}
