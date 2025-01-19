use std::env;
use std::fs;
use std::path::Path;

fn main() {
    let dir_path = match env::args().nth(1) {
        Some(path) => path,
        None => {
            eprintln!("使用方法: ./backup_management <ディレクトリ名>");
            return;
        }
    };
    let path = Path::new(&dir_path);

    if !path.is_dir() {
        eprintln!("指定されたパスはディレクトリではありません: {}", dir_path);
        return;
    }

    let current_exe = env::current_exe().expect("実行ファイルのパスを取得できません");

    let oldest_file = fs::read_dir(path)
        .expect("ディレクトリを読み取れません")
        .filter_map(|entry| entry.ok())
        .filter(|entry| {
            let path = entry.path();
            path.is_file() && path != current_exe
        })
        .min_by_key(|entry| {
            entry
                .metadata()
                .expect("メタデータを取得できません")
                .modified()
                .expect("更新日時を取得できません")
        });

    match oldest_file {
        Some(file) => {
            let file_path = file.path();
            match fs::remove_file(&file_path) {
                Ok(_) => println!(
                    "最も古いファイルを削除しました: {}",
                    file_path.display()
                ),
                Err(e) => eprintln!("ファイルの削除に失敗しました: {}", e),
            }
        }
        None => println!("削除可能なファイルが見つかりませんでした"),
    }
}
