package cn.allin.ota

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.jieli.jl_bt_ota.constant.StateCode
import com.jieli.jl_bt_ota.impl.BluetoothOTAManager
import com.jieli.jl_bt_ota.model.base.BaseError


class OTAManager(context: Context?) : BluetoothOTAManager(context) {
//    private val bleManager: BleManager = BleManager.getInstance() //BLE连接的实现

    init {
        
        //TODO:用户通过自行实现的连接库对象完成传递设备连接状态和接收到的数据
//        bleManager.registerBleEventCallback(object : BleEventCallback() {
//            public override fun onBleConnection(device: BluetoothDevice?, status: Int) {
//                super.onBleConnection(device, status)
//                //TODO: 注意：转变成OTA库的连接状态
//                //注意: 需要正确传入设备连接状态，不要重复传入相同状态， 连接中-已连接-断开 或者 已连接-断开
//                val connectStatus = changeConnectStatus(status)
//                //传递设备的连接状态
//                onBtDeviceConnection(device, connectStatus)
//            }
//
//            public override fun onBleDataNotification(device: BluetoothDevice?, serviceUuid: UUID?, characteristicsUuid: UUID?, data: ByteArray?) {
//                super.onBleDataNotification(device, serviceUuid, characteristicsUuid, data)
//                //传递设备的接收数据
//                onReceiveDeviceData(device, data)
//            }
//
//            public override fun onBleDataBlockChanged(device: BluetoothDevice?, block: Int, status: Int) {
//                super.onBleDataBlockChanged(device, block, status)
//                //传递BLE的MTU改变
//                //注意: 非必要实现，建议客户在设备连接上时进行MTU协商
//                onMtuChanged(getConnectedBluetoothGatt(), block, status)
//            }
//        })
    }

    /**
     * 获取已连接的蓝牙设备
     *
     *
     * 注意：1. 是通讯方式对应的蓝牙设备对象<br></br>
     * 2. 实时反映设备的连接状态，设备已连接时有值，设备断开时返回null
     *
     */
    override fun getConnectedDevice(): BluetoothDevice {
        TODO("用户自行实现")
//        return bleManager.getConnectedBtDevice()
    }

    /**
     * 获取已连接的BluetoothGatt对象
     *
     *
     * 若选择BLE方式OTA，需要实现此方法。反之，SPP方式不需要实现
     *
     */
    override fun getConnectedBluetoothGatt(): BluetoothGatt {
        TODO()
    }

    /**
     * 连接蓝牙设备
     *
     *
     * 注意:1. 目前的回连方式都是回连BLE设备，只需要实现回连设备的BLE
     * 2. 该方法用于设备回连过程，如果客户是双备份OTA或者自行实现回连流程，不需要实现
     *
     * @param device 通讯方式的蓝牙设备
     */
    override fun connectBluetoothDevice(device: BluetoothDevice?) {
        //TODO:用户自行实现连接设备
    }

    /**
     * 断开蓝牙设备的连接
     *
     * @param device 通讯方式的蓝牙设备
     */
    override fun disconnectBluetoothDevice(device: BluetoothDevice?) {
        //TODO:用户自行实现断开设备
    }

    /**
     * 发送数据到蓝牙设备
     *
     *
     * 注意: 1. 需要实现可靠的大数据传输<br></br>
     * 1.1 如果是BLE发送数据，需要根据MTU进行分包，然后队列式发数，确保数据发出<br></br>
     * 1.2 如果是BLE发送数据 而且 协商MTU大于128， 建议发送MTU = 协商MTU - 6， 进行边缘保护
     * 2. 该方法在发送数据时回调，发送的数据是组装好的RCSP命令。一般长度在[10, 525]
     *
     * @param device 已连接的蓝牙设备
     * @param data 数据包
     * @return 操作结果
     */
    override fun sendDataToDevice(device: BluetoothDevice?, data: ByteArray?): Boolean {
        //TODO:用户自行实现发送数据，BLE方式，需要注意MTU分包和队列式发数
//        bleManager.writeDataByBleAsync(device, BleManager.BLE_UUID_SERVICE, BleManager.BLE_UUID_WRITE, data, object : OnWriteDataCallback() {
//            public override fun onBleResult(device: BluetoothDevice?, serviceUUID: UUID?, characteristicUUID: UUID?, result: Boolean, data: ByteArray?) {
//                //返回结果
//            }
//        })
        //也可以阻塞等待结果
        return true
    }

    /**
     * 用户通知OTA库的错误事件
     *
     *
     * 1. 错误码参考 3.5 章节 错误码定义
     *
     */
    override fun errorEventCallback(error: BaseError?) {
    }

    /**
     * 用于释放资源
     */
    override fun release() {
    }

    //连接状态转换
    private fun changeConnectStatus(status: Int): Int {
        var changeStatus = StateCode.CONNECTION_DISCONNECT
        when (status) {
            BluetoothProfile.STATE_DISCONNECTED, BluetoothProfile.STATE_DISCONNECTING -> {
                changeStatus = StateCode.CONNECTION_DISCONNECT
            }

            BluetoothProfile.STATE_CONNECTED -> changeStatus = StateCode.CONNECTION_OK
            BluetoothProfile.STATE_CONNECTING -> changeStatus = StateCode.CONNECTION_CONNECTING
        }
        return changeStatus
    }
}
