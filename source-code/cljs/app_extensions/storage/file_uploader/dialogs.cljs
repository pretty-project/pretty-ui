
(ns app-extensions.storage.file-uploader.dialogs
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.io        :as io]
              [mid-fruits.format    :as format]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.math      :as math]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- abort-upload-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id {:keys [files-uploaded?]}]
  (if-not files-uploaded? [elements/button {:label :abort! :preset :warning-button :indent :both
                                            :on-click [:storage/abort-file-uploading! uploader-id]}]))

(defn- upload-progress-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [file-count request-progress files-uploaded? request-failured?]}]
  (let [sections      [{:color :primary :value request-progress} {:color :highlight :value (- 100 request-progress)}]
        progress-label {:content :uploading-n-files-in-progress... :replacements [file-count]}
        label (cond request-failured? :file-upload-failure files-uploaded? :files-uploaded :else progress-label)]
       [:div {:style {:width "100%"}}
             [elements/line-diagram {:layout :row :font-size :xs :sections sections :indent :both
                                     :label label}]]))

(defn- upload-progress-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [total-size uploaded-size]}]
  (let [total-size    (io/B->MB            total-size)
        ;uploaded-size (math/percent-result total-size request-progress)
        uploaded-size (io/B->MB uploaded-size)
        total-size    (format/decimals     total-size)
        uploaded-size (format/decimals     uploaded-size)]
       [elements/label {:content (str  uploaded-size " MB / " total-size " MB")
                        :font-size :xs :color :muted :indent :both}]))

    ; Akkor is hozzászámolja a cuccokat, ha még nincs elidnitva

(defn- upload-progress-notification
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id progress-props]
  [:<> [elements/horizontal-separator {:size :s}]
       [upload-progress-diagram uploader-id progress-props]
       [elements/row {:horizontal-align :space-between
                      :content [:<> [upload-progress-label uploader-id progress-props]
                                    [abort-upload-button   uploader-id progress-props]]}]])
  ;[:div (str progress-props)])

(defn- upload-progress-notification-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [components/subscriber {:render-f   #'upload-progress-notification
                          :subscriber [:storage/get-progress-props uploader-id]}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/render-file-uploading-progress-notification!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      [:ui/blow-bubble! (keyword/add-namespace :storage uploader-id)
                        {:body        [upload-progress-notification-body uploader-id]
                         :autopop?    false
                         :user-close? false}]))
