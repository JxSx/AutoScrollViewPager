# AutoScrollViewPager
实现ViewPager的循环滚动
 * 提供给外界如下方法：
 * 1.setOnPositionChange.设置监听，提供给外界当前页面求模运算后的索引值，注意不是position值
 * 2.setGoToLeft 向左滚动（默认方向，可以不设置）
 * 3.setGoToRight 向右滚动
 * 4.setInterval 设置滚动一次时间间隔，默认3000毫秒
 * 5.stopAutoScroll 停止自动滚动
 * 6.startAutoScroll 开始自动滚动
 * 7.setOnItemClicklistener 每个Item的点击事件
