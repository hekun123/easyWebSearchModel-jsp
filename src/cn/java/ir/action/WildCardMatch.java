package cn.java.ir.action;

public class WildCardMatch {
	private static String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
	public static boolean isWildCard(String str) {
		 for(int i = 0 ; i < regEx.length(); i ++) {
//			 System.out.println(regEx.charAt(i));
			 if(str.indexOf(regEx.charAt(i))==-1){ //等于-1表示这个字符串中没有o这个字符
				}else{
					return true;
				}
		 }
		 return false;
	 }
}
