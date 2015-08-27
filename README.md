Error:Execution failed for task ':gr:dexDebug'.
> com.android.ide.common.internal.LoggedErrorException: Failed to run command:
	D:\softs\android-sdk\build-tools\22.0.1\dx.bat --dex --no-optimize --output D:\android-projects\GinoRossi\gr\build\intermediates\dex\debug --input-list=D:\android-projects\GinoRossi\gr\build\intermediates\tmp\dex\debug\inputList.txt
Error Code:
	2
Output:
	UNEXPECTED TOP-LEVEL EXCEPTION:
	com.android.dex.DexIndexOverflowException: method ID not in [0, 0xffff]: 65536
		at com.android.dx.merge.DexMerger$6.updateIndex(DexMerger.java:502)
		at com.android.dx.merge.DexMerger$IdMerger.mergeSorted(DexMerger.java:277)
		at com.android.dx.merge.DexMerger.mergeMethodIds(DexMerger.java:491)
		at com.android.dx.merge.DexMerger.mergeDexes(DexMerger.java:168)
		at com.android.dx.merge.DexMerger.merge(DexMerger.java:189)
		at com.android.dx.command.dexer.Main.mergeLibraryDexBuffers(Main.java:454)
		at com.android.dx.command.dexer.Main.runMonoDex(Main.java:303)
		at com.android.dx.command.dexer.Main.run(Main.java:246)
		at com.android.dx.command.dexer.Main.main(Main.java:215)
		at com.android.dx.command.Main.main(Main.java:106)
    Error:Execution failed for task ':gr:packageAllDebugClassesForMultiDex'.
Error:Execution failed for task ':gr:packageAllDebugClassesForMultiDex'.
> java.util.zip.ZipException: duplicate entry: com/google/android/gms/internal/e.class