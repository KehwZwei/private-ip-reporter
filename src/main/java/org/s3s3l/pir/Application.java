package org.s3s3l.pir;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;

import org.s3s3l.yggdrasil.utils.common.CliHelper;
import org.s3s3l.yggdrasil.utils.file.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
    private static final String INIT_SCRIPT = "init.sh";
    private static final String REPORT_SCRIPT = "report.sh";

    public static void main(String[] args) throws IOException {
        CliHelper cliHelper = new CliHelper(Executors.newFixedThreadPool(1));
        String baseDir = "./workspace";
        long interval = 1_800_000L;
        if (args.length >= 1) {
            baseDir = args[0];
        }

        if (args.length >= 2) {
            interval = Long.parseLong(args[1]);
        }

        File baseDirFile = new File(baseDir);

        if (!baseDirFile.exists() || !baseDirFile.isDirectory()) {
            log.error("不可用的工作目录：{}", baseDir);
            return;
        }

        log.info("工作目录：{}", baseDir);

        log.info(cliHelper
                .exec(String.format("mv %s %s", FileUtils.getFirstExistFile(INIT_SCRIPT)
                        .getAbsolutePath(), baseDir))
                .getResponse());
        log.info(cliHelper
                .exec(String.format("mv %s %s", FileUtils.getFirstExistFile(REPORT_SCRIPT).getAbsolutePath(), baseDir))
                .getResponse());
        String initScript = String.join("/", baseDir, INIT_SCRIPT);
        String reportScript = String.join("/", baseDir, REPORT_SCRIPT);
        log.info(cliHelper.exec("chmod +x " + initScript).getResponse());
        log.info(cliHelper.exec("chmod +x " + reportScript).getResponse());

        log.info(cliHelper.exec(String.join(" ", initScript, baseDir)).getResponse());

        while (true) {
            try {
                String ip = getIp();

                log.info(cliHelper.exec(String.join(" ", reportScript, baseDir, "test", ip)).getResponse());
                Thread.sleep(interval);
            } catch (Exception e) {
                log.error("ip 上报失败", e);
            }
        }
    }

    private static String getIp() throws IOException {
        URL url = new URL("http://v4.ipv6-test.com/api/myip.php");
        String ip = FileUtils.readToEnd(url.openStream());
        return ip;
    }
}
