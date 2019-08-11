# 屏幕适配
>原因：Android设备碎片化，导致app的的界面元素在不同屏幕尺寸上显示不一致
>目的：让布局，布局组件，资源，用户界面流程，匹配不同屏幕尺寸

---
#### 布局适配
- 避免写死控件尺寸,使用wrap_content,match_parent
- LinearLayout xxx:layout_weight =1
- ReleaveLayout xxx:layout_centerInParent = "true"
- ContraintLayout xxx:layout_constraintLeft_toLeft = "parent"
- Percent-support-lib xxx:layout_widthPercent="30%"
---


### 屏幕适配-自定义View
##### 缩放比例是关键log
>原理：以一个特定宽度尺寸的设备为参考，在View 的**加载过程**，根据当前设备的实际像素换算出目标像素，再作用到控件上。
### 一.修改系统Density

