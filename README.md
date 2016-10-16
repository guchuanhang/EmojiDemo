# EmojiDemo
Social APP,always support emotion input such as QQ.
Then in my recent development,studied this ,and save it.

For it's effect play refer:https://github.com/guchuanhang/EmojiDemo/blob/master/introduce.gif

It's useage is simple:

1.import JKeyboardPanelSwitch into project;

  compile 'cn.dreamtobe.kpswitch:library:1.6.0'

2.add this project or(released aar) into project;

3.refer to com.hang.emojidemo.MainActivity&&com.hang.emojidemo.Main2Activity
In MainActivity,when you press emotion, it will appending emtion in edittext;
please make sure its' windowSoftInputMode=adjustSize.
In Main2Activity,it shows how to convert text to emtion.

4.beside you need two icons:R.drawable.icon_icon for ready switch to emotion input,
R.drawable.keyboard_icon for ready switch to ordinary text input. You can find that below drawable-xhdpi.

