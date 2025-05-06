package muix.fileInput

import react.ChildrenBuilder
import react.ReactDsl
import web.file.File

fun ChildrenBuilder.MuiFileInputSingle(block: @ReactDsl MuiFileInputProps<File>.() -> Unit) {
    MuiFileInput {
        block(this.unsafeCast<MuiFileInputProps<File>>())
    }
}

fun ChildrenBuilder.MuiFileInputMultiple(block: @ReactDsl MuiFileInputProps<Array<File>>.() -> Unit) {
    MuiFileInput {
        asDynamic()["multiple"] = true
        block(this.unsafeCast<MuiFileInputProps<Array<File>>>())
    }
}
