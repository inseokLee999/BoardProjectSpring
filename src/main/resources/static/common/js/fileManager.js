/**
 * 파일 업로드, 삭제, 조회 공통 기능
 * */

const fileManager = {
    /**
     * 파일 업로드
     *
     */
    upload(files, gid, location) {
        try {
            if (!files || files.length == 0) {
                throw new Error("파일을 선택 하세요.");
            }
            if (!gid || !gid.trim()) {
                throw new Error("필수 항목 누락입니다(gid).")
            }
            const formData = new FormData();
            formData.append("gid", gid.trim());

            for (const file of files) {
                formData.append("file", file);
            }
            if (location && location.trim()) {
                formData.append("location", location.trim());
            }
            // commonLib.ajaxLoad('/file/upload','POST',formData)
            const {ajaxLoad} = commonLib;
            ajaxLoad('/file/upload', 'POST', formData)
                .then(res => {
                    if (!res.success) {//실패시
                        alert(res.message);
                        return;
                    }
                    //파일 업로드 후 처리는 다양, fileUploadCallback 을 직접 상황에 맞게 정의 처리
                    if (typeof parent.fileUploadCallback === 'function'){
                        parent.fileUploadCallback(res.data);
                    }
                })
                .catch(err => alert(err.message));
        } catch (e) {
            console.error(e);
            alert(e.message);
        }

    },
    /**
     * 파일 삭제
     *
     */
    delete() {

    },
    /**
     * 파일 조회
     *
     */
    search() {

    }
}
window.addEventListener("DOMContentLoaded", function () {
    //파일 업로드 버튼 이벤트 처리 S
    const fileUploads = document.getElementsByClassName("fileUploads");
    const fileEl = document.createElement("input");
    fileEl.type = "file";
    fileEl.multiple = true;

    for (const el of fileUploads) {
        el.addEventListener("click", function () {
            fileEl.value = "";
            delete fileEl.gid
            delete fileEl.location;
            const dataset = this.dataset; //data- 친구들을  받아올 수 있음
            fileEl.gid = dataset.gid;
            if (dataset.location) fileEl.location = dataset.location;
            console.log(fileEl.value)
            fileEl.click();
        });
    }
    //파일 업로드 버튼 이벤트 처리 E
    fileEl.addEventListener("change", function (e) {
        const files = e.target.files;
        fileManager.upload(files, fileEl.gid, fileEl.location);
    });
});