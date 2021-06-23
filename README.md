# ImageTransmit

手机助手-图片速递Demo中使用的 `图片传输模块`。

## 图片接收端

在调用模块功能前需要进行初始化，`ImageTransmitManager.init(Context)`。  
调用`ImageTransmitManager.getInstance().startRecServer()` 进行图片接收的网络服务的开启。  
设置监听器进行接收到的图片的后续处理
```java
ImageTransmitManager.getInstance().setImageRecListener(new ImageRecListener(){

	@Override
	public void onImageUploadStart() {
       		...
    	}
	    
	@Override
	public void onImageReceive(List<File> images) {
		...
	}
});
```

## 图片发送端

在调用模块功能前需要进行初始化，`ImageTransmitManager.init(Context)`。  
构建 `ImageTransTask` 实例来创建图片发送任务。
```java
ImageTransTask imageTransTask = new ImageTransTask();
imageTransTask.ip = "target_ip";
imageTransTask.files = new ArrayList<>();
imageTransTask.files.add(local_file_path);
imageTransTask.files.add(local_file_path);
imageTransTask.callback = new ImageSendListener() {

	@Override
	public void onSuccess() {
		...
	}

	@Override
	public void onFail() {
		...
	}
};
```
调用 `ImageTransmitManager.getInstance().putTransTask(ImageTransTask)` 进行图片传送。

## 下载

将JitPack仓库加入到根目录的 build.gradle 的 repositories 节点。
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
然后在 module 中添加依赖
```gradle
dependencies {
	implementation 'com.github.wengz:ImageTransmit:{version}'
	...
}
```
添加依赖时请替换下述 {version} 字段为 https://jitpack.io/#wengz/ImageTransmit 上的最新版本号
