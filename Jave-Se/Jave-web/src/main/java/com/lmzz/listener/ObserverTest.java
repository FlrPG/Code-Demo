package com.lmzz.listener;

public class ObserverTest {
	public static void main(String[] args) {
		// 被监听对象
		Thief thief = new Thief();

		// 监听器，直接new一个接口的匿名类对象
		ThiefListener thiefListener = new ThiefListener() {
			public void shot(Event event) {
                // 我这里并没有用到event，实际上可以从event取出事件源
				System.out.println("啪啪啪！！！！");
			}
		};

		// 注册监听
		thief.registerListener(thiefListener);

		// 特定行为，触发监听器：内部调用listener.shot()
		thief.steal();
	}
}