package lc;

/**
 * @author zml
 */
public class LeetCode_0002_addTwoNumbers {
    class ListNode {
        int val;
        ListNode next;

        public ListNode() {
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }


    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode res = new ListNode();
        ListNode head = res;
        int remainder = (l1.val + l2.val) / 10;
        head.val = (l1.val + l2.val) % 10;
        while (l1.next != null || l2.next != null) {
            int sum = remainder;
            if (l1.next != null) {
                l1 = l1.next;
                sum += l1.val;
            }
            if (l2.next != null) {
                l2 = l2.next;
                sum += l2.val;
            }
            head.next = new ListNode(sum % 10, null);
            head = head.next;
            remainder = sum / 10;
        }
        if (remainder > 0) {
            head.next = new ListNode(remainder, null);
        }
        return res;
    }

}

















/*
//给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
//
// 请你将两个数相加，并以相同形式返回一个表示和的链表。
//
// 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
// 示例 1：
//输入：l1 = [2,4,3], l2 = [5,6,4]
//输出：[7,0,8]
//解释：342 + 465 = 807.
//
// 示例 2：
//输入：l1 = [0], l2 = [0]
//输出：[0]
//
// 示例 3：
//输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
//输出：[8,9,9,9,0,0,0,1]
 */
