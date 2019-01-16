package tools;

import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import bean.User;

public class Encryption {
	private static final MessageDigest md;
	private static final Base64 b64Encoder;
	private static final Integer saltLen = 15;

	static {
		try {
			// 调用 getInstance 将返回已初始化过的MessageDigest对象
			md = MessageDigest.getInstance("MD5", "SUN");
			b64Encoder = new Base64();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 将客户输入的密码加盐后加密
	public static void encryptPasswd(User user) {
		String salt = "";
		Random rand = new Random();
		// 可选的字符
		String base = "abcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()_+";

		// 得到盐salt
		for (int i = 0; i < saltLen; i++) {
			// 获取随机字符
			String target = String.valueOf(base.charAt(rand.nextInt(base.length())));
			salt += target;
		}

		try {
			md.reset();
			String passwordSalt = user.getPassword() + salt;
			// 调用update方法来完成 向MessageDigest对象提供要计算的数据。
			md.update(passwordSalt.getBytes("UTF-8"));
			// 调用 digest（）方法来计算摘要（即生成散列码）：
			byte[] bys = md.digest();
			// b64Encoder编码 为可见字符
			byte[] lastPassword = b64Encoder.encode(bys);
			// new String(lastPassword) 根据byte数组创建一个字符串

			// user.setPassword(new String(lastPassword));
			user.setAddedSaltPassword(new String(lastPassword));
			user.setSalt(salt);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 验证客户密码
	public static boolean checkPassword(User user, String dataBasePassword) {
		try {
			md.reset();
			String passwordSalt = user.getPassword() + user.getSalt();
			// 调用update方法来完成 向MessageDigest对象提供要计算的数据。
			md.update(passwordSalt.getBytes("UTF-8"));
			// 调用 digest（）方法来计算摘要（即生成散列码）：
			byte[] bys = md.digest();
			// b64Encoder编码 为可见字符
			byte[] lastPassword = b64Encoder.encode(bys);
			// 将byte数组转换为字符串
			String inputPassword = new String(lastPassword);
			// 比较两个摘要的相等性
			if (dataBasePassword.equals(inputPassword))
				return true;
			else
				return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
