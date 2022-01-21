
(ns app-extensions.storage.file-uploader.dialogs
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.io        :as io]
              [mid-fruits.format    :as format]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.storage.file-uploader.engine :as engine]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- abort-upload-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [files-uploaded?]}]
  (if-not false [elements/button {:tooltip :abort! :preset :close-icon-button :indent :both
                                  :on-click [:storage/abort-file-uploading!]}]))

(defn- upload-progress-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [file-count files-uploaded? request-progress] :as x}]
  (let [sections      [{:color :primary :value request-progress} {:color :highlight :value (- 100 request-progress)}]
        progress-label {:content :uploading-n-files-in-progress... :replacements [file-count]}
        label          (if files-uploaded? :files-uploaded progress-label)]
       [:div {:style {:width "100%"}}
             [elements/line-diagram {:sections sections :indent :both}]])); :label label}]]))
                                     ;:min-height :s}]]))
             ;(str x)]))

(defn- upload-progress-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [file-count files-size files-uploaded? uploaded-size] :as x}]
  (let [progress-label {:content :uploading-n-files-in-progress... :replacements [file-count]}
        label          (if files-uploaded? :files-uploaded progress-label)]
       [elements/label {:content label :font-size :xs :color :default :layout :fit :indent :left
                        :min-height :s}]))

(defn- upload-progress-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id {:keys [request-sent?] :as uploader-props}]
  (if request-sent? [:<> [elements/row {:content [:<> [upload-progress-label   uploader-id uploader-props]
                                                      [abort-upload-button   uploader-id uploader-props]]
                                        :horizontal-align :space-between}]
                         [upload-progress-diagram uploader-id uploader-props]]))
    ;[:div])) ;[:div (str uploader-props)]
;                         [elements/horizontal-separator {:size :l}]]))

;                         [elements/horizontal-separator {:size :xs}]]))
;                         [upload-progress-diagram uploader-id uploader-props]]))
;                         [elements/button {:preset :close-icon-button}]]))

(defn- upload-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [components/subscriber uploader-id
                         {:render-f #'upload-progress-structure
                          :subscriber [:storage/get-file-uploader-props uploader-id]}])

(defn- upload-progress-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [dialog-id {:keys [uploader-ids]}]
  (reduce #(conj %1 [upload-progress %2])
           [:<>] uploader-ids))

(defn- upload-progress-notification-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [dialog-id upload-props]
  [:<>
       ;[elements/horizontal-separator {:size :xxs}]
       [upload-progress-list dialog-id upload-props]])
       ;[elements/horizontal-separator {:size :m}]])

(defn- upload-progress-notification
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [dialog-id]
  [components/subscriber dialog-id
                         {:render-f   #'upload-progress-notification-structure
                          :subscriber [:storage/get-file-upload-props]}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/render-file-uploader-progress-notification!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      ; Az egy időben történő feltöltési folyamatok értékei összevonva jelennek meg egy folyamatjelzőn
      [:ui/blow-bubble! :storage/file-uploader-progress-notification
                        {:body #'upload-progress-notification
                         :autopop? false :user-close? false}]))
