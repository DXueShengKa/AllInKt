@file:JsModule("@ant-design/icons")

package ant.icons

import react.CSSProperties
import react.FC
import react.Props
import web.cssom.Color
import web.cssom.Height

external interface IconsProps : Props {
    var fill: Color
    var height: Height
    var style: CSSProperties
}

//region ---------------方向性图标--------------------

@JsName("StepBackwardOutlined")
external val OutlinedStepBackward : FC<IconsProps>
@JsName("StepForwardOutlined")
external val OutlinedStepForward : FC<IconsProps>
@JsName("FastBackwardOutlined")
external val OutlinedFastBackward : FC<IconsProps>
@JsName("FastForwardOutlined")
external val OutlinedFastForward : FC<IconsProps>
@JsName("ShrinkOutlined")
external val OutlinedShrink : FC<IconsProps>
@JsName("ArrowsAltOutlined")
external val OutlinedArrowsAlt : FC<IconsProps>
@JsName("DownOutlined")
external val OutlinedDown : FC<IconsProps>
@JsName("UpOutlined")
external val OutlinedUp : FC<IconsProps>
@JsName("LeftOutlined")
external val OutlinedLeft : FC<IconsProps>
@JsName("RightOutlined")
external val OutlinedRight : FC<IconsProps>
@JsName("CaretUpOutlined")
external val OutlinedCaretUp : FC<IconsProps>
@JsName("CaretDownOutlined")
external val OutlinedCaretDown : FC<IconsProps>
@JsName("CaretLeftOutlined")
external val OutlinedCaretLeft : FC<IconsProps>
@JsName("CaretRightOutlined")
external val OutlinedCaretRight : FC<IconsProps>
@JsName("UpCircleOutlined")
external val OutlinedUpCircle : FC<IconsProps>
@JsName("DownCircleOutlined")
external val OutlinedDownCircle : FC<IconsProps>
@JsName("LeftCircleOutlined")
external val OutlinedLeftCircle : FC<IconsProps>
@JsName("RightCircleOutlined")
external val OutlinedRightCircle : FC<IconsProps>
@JsName("DoubleRightOutlined")
external val OutlinedDoubleRight : FC<IconsProps>
@JsName("DoubleLeftOutlined")
external val OutlinedDoubleLeft : FC<IconsProps>
@JsName("VerticalLeftOutlined")
external val OutlinedVerticalLeft : FC<IconsProps>
@JsName("VerticalRightOutlined")
external val OutlinedVerticalRight : FC<IconsProps>
@JsName("VerticalAlignTopOutlined")
external val OutlinedVerticalAlignTop : FC<IconsProps>
@JsName("VerticalAlignMiddleOutlined")
external val OutlinedVerticalAlignMiddle : FC<IconsProps>
@JsName("VerticalAlignBottomOutlined")
external val OutlinedVerticalAlignBottom : FC<IconsProps>
@JsName("ForwardOutlined")
external val OutlinedForward : FC<IconsProps>
@JsName("BackwardOutlined")
external val OutlinedBackward : FC<IconsProps>
@JsName("RollbackOutlined")
external val OutlinedRollback : FC<IconsProps>
@JsName("EnterOutlined")
external val OutlinedEnter : FC<IconsProps>
@JsName("RetweetOutlined")
external val OutlinedRetweet : FC<IconsProps>
@JsName("SwapOutlined")
external val OutlinedSwap : FC<IconsProps>
@JsName("SwapLeftOutlined")
external val OutlinedSwapLeft : FC<IconsProps>
@JsName("SwapRightOutlined")
external val OutlinedSwapRight : FC<IconsProps>
@JsName("ArrowUpOutlined")
external val OutlinedArrowUp : FC<IconsProps>
@JsName("ArrowDownOutlined")
external val OutlinedArrowDown : FC<IconsProps>
@JsName("ArrowLeftOutlined")
external val OutlinedArrowLeft : FC<IconsProps>
@JsName("ArrowRightOutlined")
external val OutlinedArrowRight : FC<IconsProps>
@JsName("PlayCircleOutlined")
external val OutlinedPlayCircle : FC<IconsProps>
@JsName("UpSquareOutlined")
external val OutlinedUpSquare : FC<IconsProps>
@JsName("DownSquareOutlined")
external val OutlinedDownSquare : FC<IconsProps>
@JsName("LeftSquareOutlined")
external val OutlinedLeftSquare : FC<IconsProps>
@JsName("RightSquareOutlined")
external val OutlinedRightSquare : FC<IconsProps>
@JsName("LoginOutlined")
external val OutlinedLogin : FC<IconsProps>
@JsName("LogoutOutlined")
external val OutlinedLogout : FC<IconsProps>
@JsName("MenuFoldOutlined")
external val OutlinedMenuFold : FC<IconsProps>
@JsName("MenuUnfoldOutlined")
external val OutlinedMenuUnfold : FC<IconsProps>
@JsName("BorderBottomOutlined")
external val OutlinedBorderBottom : FC<IconsProps>
@JsName("BorderHorizontalOutlined")
external val OutlinedBorderHorizontal : FC<IconsProps>
@JsName("BorderInnerOutlined")
external val OutlinedBorderInner : FC<IconsProps>
@JsName("BorderOuterOutlined")
external val OutlinedBorderOuter : FC<IconsProps>
@JsName("BorderLeftOutlined")
external val OutlinedBorderLeft : FC<IconsProps>
@JsName("BorderRightOutlined")
external val OutlinedBorderRight : FC<IconsProps>
@JsName("BorderTopOutlined")
external val OutlinedBorderTop : FC<IconsProps>
@JsName("BorderVerticleOutlined")
external val OutlinedBorderVerticle : FC<IconsProps>
@JsName("PicCenterOutlined")
external val OutlinedPicCenter : FC<IconsProps>
@JsName("PicLeftOutlined")
external val OutlinedPicLeft : FC<IconsProps>
@JsName("PicRightOutlined")
external val OutlinedPicRight : FC<IconsProps>
@JsName("RadiusBottomleftOutlined")
external val OutlinedRadiusBottomleft : FC<IconsProps>
@JsName("RadiusBottomrightOutlined")
external val OutlinedRadiusBottomright : FC<IconsProps>
@JsName("RadiusUpleftOutlined")
external val OutlinedRadiusUpleft : FC<IconsProps>
@JsName("RadiusUprightOutlined")
external val OutlinedRadiusUpright : FC<IconsProps>
@JsName("FullscreenOutlined")
external val OutlinedFullscreen : FC<IconsProps>
@JsName("FullscreenExitOutlined")
external val OutlinedFullscreenExit : FC<IconsProps>
//endregion


//region 提示建议性图标
@JsName("QuestionOutlined")
external val OutlinedQuestion:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedQuestionCircle:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedPlus:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedPlusCircle:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedPause:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedPauseCircle:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedMinus:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedMinusCircle:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedPlusSquare:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedMinusSquare:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedInfo:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedInfoCircle:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedExclamation:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedExclamationCircle:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedClose:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedCloseCircle:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedCloseSquare:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedCheck:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedCheckCircle:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedCheckSquare:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedClockCircle:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedWarning:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedIssuesClose:FC<IconsProps>
@JsName("StopOutlined")
external val OutlinedStop:FC<IconsProps>
//endregion



//region -------------网站通用图标-----------
@JsName("AccountBookOutlined")
external val OutlinedAccountBook:FC<IconsProps>
@JsName("AimOutlined")
external val OutlinedAim:FC<IconsProps>
@JsName("AlertOutlined")
external val OutlinedAlert:FC<IconsProps>
@JsName("ApartmentOutlined")
external val OutlinedApartment:FC<IconsProps>
@JsName("ApiOutlined")
external val OutlinedApi:FC<IconsProps>
@JsName("AppstoreAddOutlined")
external val OutlinedAppstoreAdd:FC<IconsProps>
@JsName("AppstoreOutlined")
external val OutlinedAppstore:FC<IconsProps>
@JsName("AudioOutlined")
external val OutlinedAudio:FC<IconsProps>
@JsName("AudioMutedOutlined")
external val OutlinedAudioMuted:FC<IconsProps>
@JsName("AuditOutlined")
external val OutlinedAudit:FC<IconsProps>
@JsName("BankOutlined")
external val OutlinedBank:FC<IconsProps>
@JsName("BarcodeOutlined")
external val OutlinedBarcode:FC<IconsProps>
@JsName("BarsOutlined")
external val OutlinedBars:FC<IconsProps>
@JsName("BellOutlined")
external val OutlinedBell:FC<IconsProps>
@JsName("BlockOutlined")
external val OutlinedBlock:FC<IconsProps>
@JsName("BookOutlined")
external val OutlinedBook:FC<IconsProps>
@JsName("BorderOutlined")
external val OutlinedBorder:FC<IconsProps>
@JsName("BorderlessTableOutlined")
external val OutlinedBorderlessTable:FC<IconsProps>
@JsName("BranchesOutlined")
external val OutlinedBranches:FC<IconsProps>
@JsName("BugOutlined")
external val OutlinedBug:FC<IconsProps>
@JsName("BuildOutlined")
external val OutlinedBuild:FC<IconsProps>
@JsName("BulbOutlined")
external val OutlinedBulb:FC<IconsProps>
@JsName("CalculatorOutlined")
external val OutlinedCalculator:FC<IconsProps>
@JsName("CalendarOutlined")
external val OutlinedCalendar:FC<IconsProps>
@JsName("CameraOutlined")
external val OutlinedCamera:FC<IconsProps>
@JsName("CarOutlined")
external val OutlinedCar:FC<IconsProps>
@JsName("CarryOutOutlined")
external val OutlinedCarryOut:FC<IconsProps>
@JsName("CiCircleOutlined")
external val OutlinedCiCircle:FC<IconsProps>
@JsName("CiOutlined")
external val OutlinedCi:FC<IconsProps>
@JsName("ClearOutlined")
external val OutlinedClear:FC<IconsProps>
@JsName("CloudDownloadOutlined")
external val OutlinedCloudDownload:FC<IconsProps>
@JsName("CloudOutlined")
external val OutlinedCloud:FC<IconsProps>
@JsName("CloudServerOutlined")
external val OutlinedCloudServer:FC<IconsProps>
@JsName("CloudSyncOutlined")
external val OutlinedCloudSync:FC<IconsProps>
@JsName("CloudUploadOutlined")
external val OutlinedCloudUpload:FC<IconsProps>
@JsName("ClusterOutlined")
external val OutlinedCluster:FC<IconsProps>
@JsName("CodeOutlined")
external val OutlinedCode:FC<IconsProps>
@JsName("CoffeeOutlined")
external val OutlinedCoffee:FC<IconsProps>
@JsName("CommentOutlined")
external val OutlinedComment:FC<IconsProps>
@JsName("CompassOutlined")
external val OutlinedCompass:FC<IconsProps>
@JsName("CompressOutlined")
external val OutlinedCompress:FC<IconsProps>
@JsName("ConsoleSqlOutlined")
external val OutlinedConsoleSql:FC<IconsProps>
@JsName("ContactsOutlined")
external val OutlinedContacts:FC<IconsProps>
@JsName("ContainerOutlined")
external val OutlinedContainer:FC<IconsProps>
@JsName("ControlOutlined")
external val OutlinedControl:FC<IconsProps>
@JsName("CopyrightOutlined")
external val OutlinedCopyright:FC<IconsProps>
@JsName("CreditCardOutlined")
external val OutlinedCreditCard:FC<IconsProps>
@JsName("CrownOutlined")
external val OutlinedCrown:FC<IconsProps>
@JsName("CustomerServiceOutlined")
external val OutlinedCustomerService:FC<IconsProps>
@JsName("DashboardOutlined")
external val OutlinedDashboard:FC<IconsProps>
@JsName("DatabaseOutlined")
external val OutlinedDatabase:FC<IconsProps>
@JsName("DeleteColumnOutlined")
external val OutlinedDeleteColumn:FC<IconsProps>
@JsName("DeleteRowOutlined")
external val OutlinedDeleteRow:FC<IconsProps>
@JsName("DeliveredProcedureOutlined")
external val OutlinedDeliveredProcedure:FC<IconsProps>
@JsName("DeploymentUnitOutlined")
external val OutlinedDeploymentUnit:FC<IconsProps>
@JsName("DesktopOutlined")
external val OutlinedDesktop:FC<IconsProps>
@JsName("DisconnectOutlined")
external val OutlinedDisconnect:FC<IconsProps>
@JsName("DislikeOutlined")
external val OutlinedDislike:FC<IconsProps>
@JsName("DollarOutlined")
external val OutlinedDollar:FC<IconsProps>
@JsName("DownloadOutlined")
external val OutlinedDownload:FC<IconsProps>
@JsName("EllipsisOutlined")
external val OutlinedEllipsis:FC<IconsProps>
@JsName("EnvironmentOutlined")
external val OutlinedEnvironment:FC<IconsProps>
@JsName("EuroCircleOutlined")
external val OutlinedEuroCircle:FC<IconsProps>
@JsName("EuroOutlined")
external val OutlinedEuro:FC<IconsProps>
@JsName("ExceptionOutlined")
external val OutlinedException:FC<IconsProps>
@JsName("ExpandAltOutlined")
external val OutlinedExpandAlt:FC<IconsProps>
@JsName("ExpandOutlined")
external val OutlinedExpand:FC<IconsProps>
@JsName("ExperimentOutlined")
external val OutlinedExperiment:FC<IconsProps>
@JsName("ExportOutlined")
external val OutlinedExport:FC<IconsProps>
@JsName("EyeOutlined")
external val OutlinedEye:FC<IconsProps>
@JsName("EyeInvisibleOutlined")
external val OutlinedEyeInvisible:FC<IconsProps>
@JsName("FieldBinaryOutlined")
external val OutlinedFieldBinary:FC<IconsProps>
@JsName("FieldNumberOutlined")
external val OutlinedFieldNumber:FC<IconsProps>
@JsName("FieldStringOutlined")
external val OutlinedFieldString:FC<IconsProps>
@JsName("FieldTimeOutlined")
external val OutlinedFieldTime:FC<IconsProps>
@JsName("FileAddOutlined")
external val OutlinedFileAdd:FC<IconsProps>
@JsName("FileDoneOutlined")
external val OutlinedFileDone:FC<IconsProps>
@JsName("FileExcelOutlined")
external val OutlinedFileExcel:FC<IconsProps>
@JsName("FileExclamationOutlined")
external val OutlinedFileExclamation:FC<IconsProps>
@JsName("FileOutlined")
external val OutlinedFile:FC<IconsProps>
@JsName("FileGifOutlined")
external val OutlinedFileGif:FC<IconsProps>
@JsName("FileImageOutlined")
external val OutlinedFileImage:FC<IconsProps>
@JsName("FileJpgOutlined")
external val OutlinedFileJpg:FC<IconsProps>
@JsName("FileMarkdownOutlined")
external val OutlinedFileMarkdown:FC<IconsProps>
@JsName("FilePdfOutlined")
external val OutlinedFilePdf:FC<IconsProps>
@JsName("FilePptOutlined")
external val OutlinedFilePpt:FC<IconsProps>
@JsName("FileProtectOutlined")
external val OutlinedFileProtect:FC<IconsProps>
@JsName("FileSearchOutlined")
external val OutlinedFileSearch:FC<IconsProps>
@JsName("FileSyncOutlined")
external val OutlinedFileSync:FC<IconsProps>
@JsName("FileTextOutlined")
external val OutlinedFileText:FC<IconsProps>
@JsName("FileUnknownOutlined")
external val OutlinedFileUnknown:FC<IconsProps>
@JsName("FileWordOutlined")
external val OutlinedFileWord:FC<IconsProps>
@JsName("FileZipOutlined")
external val OutlinedFileZip:FC<IconsProps>
@JsName("FilterOutlined")
external val OutlinedFilter:FC<IconsProps>
@JsName("FireOutlined")
external val OutlinedFire:FC<IconsProps>
@JsName("FlagOutlined")
external val OutlinedFlag:FC<IconsProps>
@JsName("FolderAddOutlined")
external val OutlinedFolderAdd:FC<IconsProps>
@JsName("FolderOutlined")
external val OutlinedFolder:FC<IconsProps>
@JsName("FolderOpenOutlined")
external val OutlinedFolderOpen:FC<IconsProps>
@JsName("FolderViewOutlined")
external val OutlinedFolderView:FC<IconsProps>
@JsName("ForkOutlined")
external val OutlinedFork:FC<IconsProps>
@JsName("FormatPainterOutlined")
external val OutlinedFormatPainter:FC<IconsProps>
@JsName("FrownOutlined")
external val OutlinedFrown:FC<IconsProps>
@JsName("FunctionOutlined")
external val OutlinedFunction:FC<IconsProps>
@JsName("FundProjectionScreenOutlined")
external val OutlinedFundProjectionScreen:FC<IconsProps>
@JsName("FundViewOutlined")
external val OutlinedFundView:FC<IconsProps>
@JsName("FunnelPlotOutlined")
external val OutlinedFunnelPlot:FC<IconsProps>
@JsName("GatewayOutlined")
external val OutlinedGateway:FC<IconsProps>
@JsName("GifOutlined")
external val OutlinedGif:FC<IconsProps>
@JsName("GiftOutlined")
external val OutlinedGift:FC<IconsProps>
@JsName("GlobalOutlined")
external val OutlinedGlobal:FC<IconsProps>
@JsName("GoldOutlined")
external val OutlinedGold:FC<IconsProps>
@JsName("GroupOutlined")
external val OutlinedGroup:FC<IconsProps>
@JsName("HddOutlined")
external val OutlinedHdd:FC<IconsProps>
@JsName("HeartOutlined")
external val OutlinedHeart:FC<IconsProps>
@JsName("HistoryOutlined")
external val OutlinedHistory:FC<IconsProps>
@JsName("HolderOutlined")
external val OutlinedHolder:FC<IconsProps>
@JsName("HomeOutlined")
external val OutlinedHome:FC<IconsProps>
@JsName("HourglassOutlined")
external val OutlinedHourglass:FC<IconsProps>
@JsName("IdcardOutlined")
external val OutlinedIdcard:FC<IconsProps>
@JsName("ImportOutlined")
external val OutlinedImport:FC<IconsProps>
@JsName("InboxOutlined")
external val OutlinedInbox:FC<IconsProps>
@JsName("InsertRowAboveOutlined")
external val OutlinedInsertRowAbove:FC<IconsProps>
@JsName("InsertRowBelowOutlined")
external val OutlinedInsertRowBelow:FC<IconsProps>
@JsName("InsertRowLeftOutlined")
external val OutlinedInsertRowLeft:FC<IconsProps>
@JsName("InsertRowRightOutlined")
external val OutlinedInsertRowRight:FC<IconsProps>
@JsName("InsuranceOutlined")
external val OutlinedInsurance:FC<IconsProps>
@JsName("InteractionOutlined")
external val OutlinedInteraction:FC<IconsProps>
@JsName("KeyOutlined")
external val OutlinedKey:FC<IconsProps>
@JsName("LaptopOutlined")
external val OutlinedLaptop:FC<IconsProps>
@JsName("LayoutOutlined")
external val OutlinedLayout:FC<IconsProps>
@JsName("LikeOutlined")
external val OutlinedLike:FC<IconsProps>
@JsName("LineOutlined")
external val OutlinedLine:FC<IconsProps>
@JsName("LinkOutlined")
external val OutlinedLink:FC<IconsProps>
@JsName("Loading3QuartersOutlined")
external val OutlinedLoading3Quarters:FC<IconsProps>
@JsName("LoadingOutlined")
external val OutlinedLoading:FC<IconsProps>
@JsName("LockOutlined")
external val OutlinedLock:FC<IconsProps>
@JsName("MacCommandOutlined")
external val OutlinedMacCommand:FC<IconsProps>
@JsName("MailOutlined")
external val OutlinedMail:FC<IconsProps>
@JsName("ManOutlined")
external val OutlinedMan:FC<IconsProps>
@JsName("MedicineBoxOutlined")
external val OutlinedMedicineBox:FC<IconsProps>
@JsName("MehOutlined")
external val OutlinedMeh:FC<IconsProps>
@JsName("MenuOutlined")
external val OutlinedMenu:FC<IconsProps>
@JsName("MergeCellsOutlined")
external val OutlinedMergeCells:FC<IconsProps>
@JsName("MergeOutlined")
external val OutlinedMerge:FC<IconsProps>
@JsName("MessageOutlined")
external val OutlinedMessage:FC<IconsProps>
@JsName("MobileOutlined")
external val OutlinedMobile:FC<IconsProps>
@JsName("MoneyCollectOutlined")
external val OutlinedMoneyCollect:FC<IconsProps>
@JsName("MonitorOutlined")
external val OutlinedMonitor:FC<IconsProps>
@JsName("MoonOutlined")
external val OutlinedMoon:FC<IconsProps>
@JsName("MoreOutlined")
external val OutlinedMore:FC<IconsProps>
@JsName("MutedOutlined")
external val OutlinedMuted:FC<IconsProps>
@JsName("NodeCollapseOutlined")
external val OutlinedNodeCollapse:FC<IconsProps>
@JsName("NodeExpandOutlined")
external val OutlinedNodeExpand:FC<IconsProps>
@JsName("NodeIndexOutlined")
external val OutlinedNodeIndex:FC<IconsProps>
@JsName("NotificationOutlined")
external val OutlinedNotification:FC<IconsProps>
@JsName("NumberOutlined")
external val OutlinedNumber:FC<IconsProps>
@JsName("OneToOneOutlined")
external val OutlinedOneToOne:FC<IconsProps>
@JsName("PaperClipOutlined")
external val OutlinedPaperClip:FC<IconsProps>
@JsName("PartitionOutlined")
external val OutlinedPartition:FC<IconsProps>
@JsName("PayCircleOutlined")
external val OutlinedPayCircle:FC<IconsProps>
@JsName("PercentageOutlined")
external val OutlinedPercentage:FC<IconsProps>
@JsName("PhoneOutlined")
external val OutlinedPhone:FC<IconsProps>
@JsName("PictureOutlined")
external val OutlinedPicture:FC<IconsProps>
@JsName("PlaySquareOutlined")
external val OutlinedPlaySquare:FC<IconsProps>
@JsName("PoundCircleOutlined")
external val OutlinedPoundCircle:FC<IconsProps>
@JsName("PoundOutlined")
external val OutlinedPound:FC<IconsProps>
@JsName("PoweroffOutlined")
external val OutlinedPoweroff:FC<IconsProps>
@JsName("PrinterOutlined")
external val OutlinedPrinter:FC<IconsProps>
@JsName("ProductOutlined")
external val OutlinedProduct:FC<IconsProps>
@JsName("ProfileOutlined")
external val OutlinedProfile:FC<IconsProps>
@JsName("ProjectOutlined")
external val OutlinedProject:FC<IconsProps>
@JsName("PropertySafetyOutlined")
external val OutlinedPropertySafety:FC<IconsProps>
@JsName("PullRequestOutlined")
external val OutlinedPullRequest:FC<IconsProps>
@JsName("PushpinOutlined")
external val OutlinedPushpin:FC<IconsProps>
@JsName("QrcodeOutlined")
external val OutlinedQrcode:FC<IconsProps>
@JsName("ReadOutlined")
external val OutlinedRead:FC<IconsProps>
@JsName("ReconciliationOutlined")
external val OutlinedReconciliation:FC<IconsProps>
@JsName("RedEnvelopeOutlined")
external val OutlinedRedEnvelope:FC<IconsProps>
@JsName("ReloadOutlined")
external val OutlinedReload:FC<IconsProps>
@JsName("RestOutlined")
external val OutlinedRest:FC<IconsProps>
@JsName("RobotOutlined")
external val OutlinedRobot:FC<IconsProps>
@JsName("RocketOutlined")
external val OutlinedRocket:FC<IconsProps>
@JsName("RotateLeftOutlined")
external val OutlinedRotateLeft:FC<IconsProps>
@JsName("RotateRightOutlined")
external val OutlinedRotateRight:FC<IconsProps>
@JsName("SafetyCertificateOutlined")
external val OutlinedSafetyCertificate:FC<IconsProps>
@JsName("SafetyOutlined")
external val OutlinedSafety:FC<IconsProps>
@JsName("SaveOutlined")
external val OutlinedSave:FC<IconsProps>
@JsName("ScanOutlined")
external val OutlinedScan:FC<IconsProps>
@JsName("ScheduleOutlined")
external val OutlinedSchedule:FC<IconsProps>
@JsName("SearchOutlined")
external val OutlinedSearch:FC<IconsProps>
@JsName("SecurityScanOutlined")
external val OutlinedSecurityScan:FC<IconsProps>
@JsName("SelectOutlined")
external val OutlinedSelect:FC<IconsProps>
@JsName("SendOutlined")
external val OutlinedSend:FC<IconsProps>
@JsName("SettingOutlined")
external val OutlinedSetting:FC<IconsProps>
@JsName("ShakeOutlined")
external val OutlinedShake:FC<IconsProps>
@JsName("ShareAltOutlined")
external val OutlinedShareAlt:FC<IconsProps>
@JsName("ShopOutlined")
external val OutlinedShop:FC<IconsProps>
@JsName("ShoppingCartOutlined")
external val OutlinedShoppingCart:FC<IconsProps>
@JsName("ShoppingOutlined")
external val OutlinedShopping:FC<IconsProps>
@JsName("SignatureOutlined")
external val OutlinedSignature:FC<IconsProps>
@JsName("SisternodeOutlined")
external val OutlinedSisternode:FC<IconsProps>
@JsName("SkinOutlined")
external val OutlinedSkin:FC<IconsProps>
@JsName("SmileOutlined")
external val OutlinedSmile:FC<IconsProps>
@JsName("SolutionOutlined")
external val OutlinedSolution:FC<IconsProps>
@JsName("SoundOutlined")
external val OutlinedSound:FC<IconsProps>
@JsName("SplitCellsOutlined")
external val OutlinedSplitCells:FC<IconsProps>
@JsName("StarOutlined")
external val OutlinedStar:FC<IconsProps>
@JsName("SubnodeOutlined")
external val OutlinedSubnode:FC<IconsProps>
@JsName("SunOutlined")
external val OutlinedSun:FC<IconsProps>
@JsName("SwitcherOutlined")
external val OutlinedSwitcher:FC<IconsProps>
@JsName("SyncOutlined")
external val OutlinedSync:FC<IconsProps>
@JsName("TableOutlined")
external val OutlinedTable:FC<IconsProps>
@JsName("TabletOutlined")
external val OutlinedTablet:FC<IconsProps>
@JsName("TagOutlined")
external val OutlinedTag:FC<IconsProps>
@JsName("TagsOutlined")
external val OutlinedTags:FC<IconsProps>
@JsName("TeamOutlined")
external val OutlinedTeam:FC<IconsProps>
@JsName("ThunderboltOutlined")
external val OutlinedThunderbolt:FC<IconsProps>
@JsName("ToTopOutlined")
external val OutlinedToTop:FC<IconsProps>
@JsName("ToolOutlined")
external val OutlinedTool:FC<IconsProps>
@JsName("TrademarkCircleOutlined")
external val OutlinedTrademarkCircle:FC<IconsProps>
@JsName("TrademarkOutlined")
external val OutlinedTrademark:FC<IconsProps>
@JsName("TransactionOutlined")
external val OutlinedTransaction:FC<IconsProps>
@JsName("TranslationOutlined")
external val OutlinedTranslation:FC<IconsProps>
@JsName("TrophyOutlined")
external val OutlinedTrophy:FC<IconsProps>
@JsName("TruckOutlined")
external val OutlinedTruck:FC<IconsProps>
@JsName("UngroupOutlined")
external val OutlinedUngroup:FC<IconsProps>
@JsName("UnlockOutlined")
external val OutlinedUnlock:FC<IconsProps>
@JsName("UploadOutlined")
external val OutlinedUpload:FC<IconsProps>
@JsName("UsbOutlined")
external val OutlinedUsb:FC<IconsProps>
@JsName("UserAddOutlined")
external val OutlinedUserAdd:FC<IconsProps>
@JsName("UserDeleteOutlined")
external val OutlinedUserDelete:FC<IconsProps>
@JsName("UserOutlined")
external val OutlinedUser:FC<IconsProps>
@JsName("UserSwitchOutlined")
external val OutlinedUserSwitch:FC<IconsProps>
@JsName("UsergroupAddOutlined")
external val OutlinedUsergroupAdd:FC<IconsProps>
@JsName("UsergroupDeleteOutlined")
external val OutlinedUsergroupDelete:FC<IconsProps>
@JsName("VerifiedOutlined")
external val OutlinedVerified:FC<IconsProps>
@JsName("VideoCameraAddOutlined")
external val OutlinedVideoCameraAdd:FC<IconsProps>
@JsName("VideoCameraOutlined")
external val OutlinedVideoCamera:FC<IconsProps>
@JsName("WalletOutlined")
external val OutlinedWallet:FC<IconsProps>
@JsName("WifiOutlined")
external val OutlinedWifi:FC<IconsProps>
@JsName("WomanOutlined")
external val OutlinedWoman:FC<IconsProps>
//endregion


