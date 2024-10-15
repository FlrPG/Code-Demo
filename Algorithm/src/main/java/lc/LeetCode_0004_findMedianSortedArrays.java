package lc;

/**
 * @author zml
 *
 * A + B fina k
 * 1. 先合并两个数组，然后排序   O(m+n)
 * 2. 找出中间的数，如果是奇数个，则直接返回   O(1)
 * 3. 如果是偶数个，则找出两个中间的数，然后求平均值   O(1)
 * 4. 时间复杂度为 O(m+n)
 * 5. 空间复杂度为 O(m+n)
 * 6. 注意边界条件
 *
 */
public class LeetCode_0004_findMedianSortedArrays {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length < 1 && nums2.length < 1) {
            return 0;
        }
        int total = nums1.length + nums2.length;
        if (total % 2 == 1) {
            return findKth(nums1, nums2, total / 2 + 1);
        } else {
            return (findKth(nums1, nums2, total / 2) + findKth(nums1, nums2, total / 2 + 1)) / 2.0;
        }
    }

    private int findKth(int[] nums1, int[] nums2, int k) {
        int m = nums1.length;
        int n = nums2.length;
        if (m > n) {
            return findKth(nums2, nums1, k);
        }
        if (m == 0) {
            return nums2[k - 1];
        }
        if (k == 1) {
            return Math.min(nums1[0], nums2[0]);
        }
        int iMin = Math.max(0, k - n);
        int iMax = Math.min(m, k);
        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;   // 防止溢出
            int j = k - i;
            if (i < m && nums2[j - 1] > nums1[i]) {
                iMin = i + 1;
            } else if (i > 0 && nums1[i - 1] > nums2[j]) {
                iMax = i - 1;
            } else {
                if (i == 0) {
                    return nums2[j - 1];
                }
                if (j == 0) {
                    return nums1[i - 1];
                }
                return Math.max(nums1[i - 1], nums2[j - 1]);
            }
        }
         return 0;
    }
}

/*
//给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。
//
// 算法的时间复杂度应该为 O(log (m+n)) 。
//
//
//
// 示例 1：
//
//
//输入：nums1 = [1,3], nums2 = [2]
//输出：2.00000
//解释：合并数组 = [1,2,3] ，中位数 2
//
//
// 示例 2：
//
//
//输入：nums1 = [1,2], nums2 = [3,4]
//输出：2.50000
//解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5
//
//
//
//
//
//
// 提示：
//
//
// nums1.length == m
// nums2.length == n
// 0 <= m <= 1000
// 0 <= n <= 1000
// 1 <= m + n <= 2000
// -10⁶ <= nums1[i], nums2[i] <= 10⁶
//
//
// Related Topics 数组 二分查找 分治 👍 7203 👎 0
 */