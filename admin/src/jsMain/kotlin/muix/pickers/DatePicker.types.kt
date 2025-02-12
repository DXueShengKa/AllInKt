package muix.pickers

import mui.system.PropsWithSx
import react.ReactNode

external interface DatePickerProps<TDate, TEnableAccessibleFieldDOMStructure> : PropsWithSx {
    /**
     * CSS media query when `Mobile` mode will be changed to `Desktop`.
     * @default '@media (pointer: fine)'
     * @example '@media (min-width: 720px)' or theme.breakpoints.up("sm")
     */
    var desktopModeMediaQuery: String?

    /**
     * Overridable component slots.
     * @default {}
     */
    var slots: DatePickerSlots<TDate>?

    /**
     * The props used for each component slot.
     * @default {}
     */
    var slotProps: DatePickerSlotProps<TDate, TEnableAccessibleFieldDOMStructure>?

    /**
     * Years rendered per row.
     * @default 4 on desktop, 3 on mobile
     */
    var yearsPerRow: Number? /* 3 | 4 */


    var value: TDate
    var onChange: (TDate) -> Unit
    var name: String
    var maxDate: TDate
    var minDate: TDate
    var label: ReactNode
}

external interface DatePickerSlots<TDate>

external interface DatePickerSlotProps<TDate, TEnableAccessibleFieldDOMStructure> : react.Props