ClassicEditor
    .create(document.querySelector('#TestContent'),config)
    .catch(error=>{
        console.error(error);
    });