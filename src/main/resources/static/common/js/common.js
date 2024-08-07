const commonLib = {
    /**
     * ajax 요청 공통 기능
     * @param url
     * @param method
     * @param data
     * @param headers
     * @param responseType : 응답 데이터 타입(text - text, 나머지 json)
     */
    ajaxLoad(url, method = "GET", data, headers, responseType) {
        if (!url) {
            return;
        }
        const csrfToken = document.querySelector("meta[name='csrf_token']")?.content?.trim();
        const csrfHeader = document.querySelector("meta[name='csrf_header']")?.content?.trim();
        let rootUrl = document.querySelector("meta[name='rootUrl']")?.content?.trim() ?? '';
        rootUrl = rootUrl === '/' ? '' : rootUrl;

        url = location.protocol + "//" + location.host + rootUrl + url;
        // url = rootUrl + url;

        method = method.toUpperCase();
        if (method === 'GET') {
            data = null;
        }
        if (data && !(data instanceof FormData) && typeof data !== 'string' && data instanceof Object) {//data가 object 일 때
            data = JSON.stringify(data);
        }
        if (csrfToken && csrfHeader) {
            headers = headers ?? {};// 있으면 그대로 없으면 빈 값
            headers[csrfHeader] = csrfToken;
        }
        const options = {
            method
        };
        if (data) options.body = data;
        if (headers) options.headers = headers;
        return new Promise((resolve, reject) => {
            fetch(url, options)
                .then(res => responseType === 'text' ? res.text() : res.json())//res.json() -json / res.text() - 텍스트
                .then(data => resolve(data))
                .catch(err => reject(err));
        })

    }
};