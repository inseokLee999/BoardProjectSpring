/**
 * 프로필 이미지 업로드 후속 처리
 * @param files
 */
function fileUploadCallback(files) {
    if (files.length === 0) {
        return;
    }
    const file = files[0];
    let html = document.getElementById("image-file-tpl").innerHTML;//목록 파일일때는 const 사용해서 치환?
    html = html.replace(/\[seq\]/g, file.seq)
        .replace(/\[fileUrl\]/g, file.fileUrl);

    const domParser = new DOMParser();
    const dom = domParser.parseFromString(html, 'text/html');
    const box = dom.querySelector('.image-file-box');

    const targetEl = document.querySelector('.profile-image');
    targetEl.innerHTML = "";
    targetEl.append(box);

    const removeEl = box.querySelector('.remove');
    removeEl.addEventListener("click", function () {
        if (!confirm('참말로 삭제 하겠습니까?')) {
            return;
        }
        const seq = this.dataset.seq;
        fileManager.delete(seq);//삭제

    });
}

/**
 * 파일 삭제 후 후속 처리
 * @param file
 */
function fileDeleteCallback(file) {
    const targetEl = document.querySelector(".profile-image");
    if (targetEl) targetEl.innerHTML = "";
}