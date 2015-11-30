# Encore Bundle

Easier: Download the APK here: https://dl.dropboxusercontent.com/u/55001004/app-debug.apk

Either add the APK to local storage on a native Android device or download Genymotion: https://www.genymotion.com/
With a Genymotion Android emulator, the APK can simply be dragged and dropped on top of an active emulator.

More complicated: Otherwise, download the repository and open the encore_android folder with Android Studio. You will be prompted to install some plugins; install these and then try building. If there is an error stating that there is no class named "FindOrCreateLobby.java", rename the file under the "src" folder called "findorcreatelobby.java" with the former's capitalization. The resulting build will work on a native Android phone connected by USB to the computer or on an emulator (which is really slow for Android Studio).
