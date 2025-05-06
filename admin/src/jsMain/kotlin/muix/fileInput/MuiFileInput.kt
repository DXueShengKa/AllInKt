@file:JsModule("mui-file-input")
package muix.fileInput

import mui.material.IconButtonProps
import mui.material.TextFieldProps
import react.FC

external interface MuiFileInputProps<T:Any>: TextFieldProps {
    var hideSizeText: Boolean?
    var clearIconButtonProps: IconButtonProps?
    @JsName("value")
    var valueFile: T?
    var getInputText: ((T?)->String)?
    var getSizeText: ((T?)->String)?
    var onChange: (T?) -> Unit

}

external val MuiFileInput: FC<MuiFileInputProps<*>>
