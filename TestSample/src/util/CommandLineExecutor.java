package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLineExecutor {
	public static String execute(String cmd) {
		String result = null;
        Process process = null;
        Runtime runtime = Runtime.getRuntime();
        StringBuffer successOutput = new StringBuffer(); // ���� ��Ʈ�� ����
        StringBuffer errorOutput = new StringBuffer(); // ���� ��Ʈ�� ����
        BufferedReader successBufferReader = null; // ���� ����
        BufferedReader errorBufferReader = null; // ���� ����
        String msg = null; // �޽���
	 
        List<String> cmdList = new ArrayList<String>();
	 
        if (System.getProperty("os.name").indexOf("Windows") > -1) {
            cmdList.add("cmd");
            cmdList.add("/c");
        } else {
            cmdList.add("/bin/sh");
            cmdList.add("-c");
        }
        // ��ɾ� ����
        cmdList.add(cmd);
        String[] array = cmdList.toArray(new String[cmdList.size()]);
 
        try {
 
            // ��ɾ� ����
            process = runtime.exec(array);
 
            // shell ������ ���� �������� ���
            successBufferReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "EUC-KR"));
 
            while ((msg = successBufferReader.readLine()) != null) {
                successOutput.append(msg + System.getProperty("line.separator"));
            }
 
            // shell ����� ������ �߻����� ���
            errorBufferReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "EUC-KR"));
            while ((msg = errorBufferReader.readLine()) != null) {
                errorOutput.append(msg + System.getProperty("line.separator"));
            }
 
            // ���μ����� ������ ���������� ���
            process.waitFor();
 
            // shell ������ ���� ����Ǿ��� ���
            if (process.exitValue() == 0) {
                System.out.println("����");
                System.out.println(successOutput.toString());
            } else {
                // shell ������ ������ ����Ǿ��� ���
                System.out.println("������ ����");
                System.out.println(successOutput.toString());
            }
 
            // shell ����� ������ �߻�
            if (successOutput.length() > 0) {
                // shell ������ ������ ����Ǿ��� ���
                System.out.println(successOutput.toString());
                result = successOutput.toString();
            }else if (errorOutput.length() > 0) {
            	System.out.println("error-" + errorOutput.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                process.destroy();
                if (successBufferReader != null) successBufferReader.close();
                if (errorBufferReader != null) errorBufferReader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }
	public static void main(String[] args) {
		CommandLineExecutor.execute("dir bin");
	}
}
