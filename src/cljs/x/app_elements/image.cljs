
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.17
; Description:
; Version: v0.3.6
; Compatibility: x4.3.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.image
    (:require [app-fruits.dom            :as dom]
              [app-fruits.react          :as react]
              [mid-fruits.candy          :refer [param]]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (base64)
;  Transparent, 1x1px
(def ERROR-IMAGE
     "data:image/png;base64,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bS9leGlmLzEuMC8iCiAgICB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iCiAgIHhtcE1NOkRvY3VtZW50SUQ9ImdpbXA6ZG9jaWQ6Z2ltcDphYzhjZGM4Zi0wNjM5LTRmOGYtYWI5NS1iNjllMmEzOGQzODEiCiAgIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6ODBkN2EzYTctMGU4YS00ODliLWE1OWMtZGU2NzA5ZTAzYTM2IgogICB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6Mzc4MWI5MDgtOTlhOS00YTIyLWE2N2ItN2RmMzlkN2IwZmVlIgogICBHSU1QOkFQST0iMi4wIgogICBHSU1QOlBsYXRmb3JtPSJNYWMgT1MiCiAgIEdJTVA6VGltZVN0YW1wPSIxNjI5NDgyODc1MjY0NDM0IgogICBHSU1QOlZlcnNpb249IjIuMTAuMTQiCiAgIGRjOkZvcm1hdD0iaW1hZ2UvcG5nIgogICBleGlmOlBpeGVsWERpbWVuc2lvbj0iNTQ4IgogICBleGlmOlBpeGVsWURpbWVuc2lvbj0iNDIwIgogICB4bXA6Q3JlYXRvclRvb2w9IkdJTVAgMi4xMCI+CiAgIDxpcHRjRXh0OkxvY2F0aW9uQ3JlYXRlZD4KICAgIDxyZGY6QmFnLz4KICAgPC9pcHRjRXh0OkxvY2F0aW9uQ3JlYXRlZD4KICAgPGlwdGNFeHQ6TG9jYXRpb25TaG93bj4KICAgIDxyZGY6QmFnLz4KICAgPC9pcHRjRXh0OkxvY2F0aW9uU2hvd24+CiAgIDxpcHRjRXh0OkFydHdvcmtPck9iamVjdD4KICAgIDxyZGY6QmFnLz4KICAgPC9pcHRjRXh0OkFydHdvcmtPck9iamVjdD4KICAgPGlwdGNFeHQ6UmVnaXN0cnlJZD4KICAgIDxyZGY6QmFnLz4KICAgPC9pcHRjRXh0OlJlZ2lzdHJ5SWQ+CiAgIDx4bXBNTTpIaXN0b3J5PgogICAgPHJkZjpTZXE+CiAgICAgPHJkZjpsaQogICAgICBzdEV2dDphY3Rpb249InNhdmVkIgogICAgICBzdEV2dDpjaGFuZ2VkPSIvIgogICAgICBzdEV2dDppbnN0YW5jZUlEPSJ4bXAuaWlkOjYyM2ZlNDU4LTliY2YtNGI5ZS04OTU1LTY3NWRlOTE5NTAwZiIKICAgICAgc3RFdnQ6c29mdHdhcmVBZ2VudD0iR2ltcCAyLjEwIChNYWMgT1MpIgogICAgICBzdEV2dDp3aGVuPSIyMDIxLTAzLTA2VDIyOjAwOjA4KzAxOjAwIi8+CiAgICAgPHJkZjpsaQogICAgICBzdEV2dDphY3Rpb249InNhdmVkIgogICAgICBzdEV2dDpjaGFuZ2VkPSIvIgogICAgICBzdEV2dDppbnN0YW5jZUlEPSJ4bXAuaWlkOjdkY2UxYjYxLTMyZjAtNGVmNS1iOGQxLWNhNWY0NGJkOTIxOCIKICAgICAgc3RFdnQ6c29mdHdhcmVBZ2VudD0iR2ltcCAyLjEwIChNYWMgT1MpIgogICAgICBzdEV2dDp3aGVuPSIyMDIxLTAzLTA2VDIyOjM0OjM1KzAxOjAwIi8+CiAgICAgPHJkZjpsaQogICAgICBzdEV2dDphY3Rpb249InNhdmVkIgogICAgICBzdEV2dDpjaGFuZ2VkPSIvIgogICAgICBzdEV2dDppbnN0YW5jZUlEPSJ4bXAuaWlkOjEzZTlhZGJjLWE0YzYtNGY0NC04N2ViLTRhNzM5MDcxZmE1YSIKICAgICAgc3RFdnQ6c29mdHdhcmVBZ2VudD0iR2ltcCAyLjEwIChNYWMgT1MpIgogICAgICBzdEV2dDp3aGVuPSIyMDIxLTA4LTIwVDIwOjA3OjU1KzAyOjAwIi8+CiAgICA8L3JkZjpTZXE+CiAgIDwveG1wTU06SGlzdG9yeT4KICAgPHBsdXM6SW1hZ2VTdXBwbGllcj4KICAgIDxyZGY6U2VxLz4KICAgPC9wbHVzOkltYWdlU3VwcGxpZXI+CiAgIDxwbHVzOkltYWdlQ3JlYXRvcj4KICAgIDxyZGY6U2VxLz4KICAgPC9wbHVzOkltYWdlQ3JlYXRvcj4KICAgPHBsdXM6Q29weXJpZ2h0T3duZXI+CiAgICA8cmRmOlNlcS8+CiAgIDwvcGx1czpDb3B5cmlnaHRPd25lcj4KICAgPHBsdXM6TGljZW5zb3I+CiAgICA8cmRmOlNlcS8+CiAgIDwvcGx1czpMaWNlbnNvcj4KICAgPGV4aWY6VXNlckNvbW1lbnQ+CiAgICA8cmRmOkFsdD4KICAgICA8cmRmOmxpIHhtbDpsYW5nPSJ4LWRlZmF1bHQiPlNjcmVlbnNob3Q8L3JkZjpsaT4KICAgIDwvcmRmOkFsdD4KICAgPC9leGlmOlVzZXJDb21tZW50PgogIDwvcmRmOkRlc2NyaXB0aW9uPgogPC9yZGY6UkRGPgo8L3g6eG1wbWV0YT4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgIAo8P3hwYWNrZXQgZW5kPSJ3Ij8+fW9+MgAAAAZiS0dEAAAAAAAA+UO7fwAAAAlwSFlzAAAWJQAAFiUBSVIk8AAAAAd0SU1FB+UIFBIHN40I704AAAANSURBVAjXY/j9+zcDAAjYAvLzPgXWAAAAAElFTkSuQmCC")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- on-error-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) image-id
  ;
  ; @return (function)
  [image-id]
  #(let [image (react/get-reference image-id)]
        (dom/set-element-attribute! image "src" ERROR-IMAGE)))

(defn- image-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  ;  {:onError (function)
  ;   :ref (function)}
  [image-id _]
  {:onError (on-error-function    image-id)
   :ref     (react/set-reference! image-id)})



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- image-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  [_ image-props]
  (merge {}
         (param image-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- image
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (hiccup)
  [image-id image-props]
  (let [image-attributes (image-attributes image-id image-props)]
       [:img.x-image (engine/element-attributes image-id image-props image-attributes)]))

(defn view
  ; @param (keyword)(opt) image-id
  ; @param (map) image-props
  ;  {:alt (string)(opt)
  ;   :class (string or vector)(opt)
  ;   :error-src (string)(opt)
  ;    TODO ...
  ;   :height (string)(opt)
  ;   :lazy-loading? (boolean)(opt)
  ;    Default: false
  ;    TODO ...
  ;   :src (string)(opt)
  ;   :style (map)(opt)
  ;   :width (string)(opt)}
  ;
  ; @usage
  ;  [elements/image {...}]
  ;
  ; @usage
  ;  [elements/image :my-image {...}]
  ;
  ; @return (hiccup)
  ([image-props]
   [view nil image-props])

  ([image-id image-props]
   (let [image-id    (a/id   image-id)]
        ;image-props (a/prot image-id image-props image-props-prototype)
        [image image-id image-props])))
