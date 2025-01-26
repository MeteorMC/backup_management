package net.hatomg

import java.io.File
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("使用方法: backup_management-all.jar <ディレクトリ名>")
        exitProcess(1)
    }

    val dirPath = args[0]
    val dir = File(dirPath)

    if (!dir.isDirectory) {
        println("指定されたパスはディレクトリではありません: $dirPath")
        exitProcess(1)
    }

    val currentExe = File(Main::class.java.protectionDomain.codeSource.location.toURI()).toPath().toAbsolutePath().normalize().toFile()

    val oldFile = dir.listFiles()
        ?.filter { it.isFile && it.canonicalPath != currentExe.canonicalPath  }
        ?.minByOrNull { it.lastModified() }

    if (oldFile != null) {
        try {
            if (oldFile.delete()) {
                println("古いファイルを削除しました: ${oldFile.absolutePath}")
            } else {
                println("ファイルの削除に失敗しました: ${oldFile.absolutePath}")
            }
        } catch (e: Exception) {
            println("ファイルの削除中にエラーが発生しました: ${e.message}")
        }
    } else {
        println("削除可能なファイルが見つかりませんでした")
    }
}
