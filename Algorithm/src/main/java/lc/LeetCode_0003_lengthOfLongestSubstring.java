package lc;

import java.util.HashSet;

/**
 * @author zml
 */
public class LeetCode_0003_lengthOfLongestSubstring {
    public static int lengthOfLongestSubstring(String s) {
        if (s.isEmpty()) {
            return 0;
        }
        int maxLen = 1;
        int l = 0, r = 0;
        HashSet<Character> set = new HashSet<>();
        char[] charArray = s.toCharArray();
        while (r < s.length()) {
            char c = charArray[r];
            if (set.contains(c)) {
                set.remove(charArray[l]);
                l++;
            }else{
                set.add(c);
                maxLen = Math.max(maxLen, r - l + 1);
                r++;
            }
        }
        return maxLen;
    }

    public static int lengthOfLongestSubstring2(String s) {
        if (s.isEmpty()) {
            return 0;
        }
        int maxLen = 1;
        int l = 0, r = 0;
        HashSet<Character> set = new HashSet<>();
        char[] charArray = s.toCharArray();
        while (r < s.length()) {
            char c = charArray[r];
            if (set.contains(c)) {
                set.remove(charArray[l]);
                l++;
            }else{
                set.add(c);
                maxLen = Math.max(maxLen, r - l + 1);
                r++;
            }
        }
        return maxLen;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("1ab1cba"));
    }

}
/*
  a. 循环计算以每个下标结尾无重复子串长度，最后取max; (1.上次a出现的位置；2. i-1位置结尾往左推了多远)
    ⅰ. 先过滤无效参数
    ⅱ. 定义map记录（map<String,int> 也可以 map[a] = int）上次字符出现的位置；int pre记录上一index往前推的最长子串长度，map全部置为-1
    ⅲ. 定义 maxlen ，pre，cur，取max (1.上次a出现的位置；2. i-1位置结尾往左推了多远),cur = i - pre,再更新len最大
 */

















/*
//给定一个字符串 s ，请你找出其中不含有重复字符的 最长 子串 的长度。
//
//
//
// 示例 1:
//
//
//输入: s = "abcabcbb"
//输出: 3
//解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
//
//
// 示例 2:
//
//
//输入: s = "bbbbb"
//输出: 1
//解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
//
//
// 示例 3:
//
//
//输入: s = "pwwkew"
//输出: 3
//解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
//     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
//
//
//
//
// 提示：
//
//
// 0 <= s.length <= 5 * 10⁴
// s 由英文字母、数字、符号和空格组成
//
//
// Related Topics 哈希表 字符串 滑动窗口 👍 10276 👎 0

 */
