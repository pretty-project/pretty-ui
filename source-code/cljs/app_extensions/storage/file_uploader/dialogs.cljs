
(ns app-extensions.storage.file-uploader.dialogs
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.storage.file-uploader.engine :as engine]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- abort-upload-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id {:keys [files-uploaded? request-aborted?]}]
  (let [request-id (engine/request-id uploader-id)]
       (if-not (or files-uploaded? request-aborted?)
               [elements/icon-button {:tooltip :abort! :preset :close :height :l
                                      :on-click [:sync/abort-request! request-id]}])))

(defn- upload-progress-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id _]
  ; Az upload-progress-diagram komponens önálló feliratkozással rendelkezik, hogy a feltöltési folyamat
  ; változása ne kényszerítse a többi komponenst sokszoros újra renderelődésre
  (let [uploader-progress (a/subscribe [:storage/get-file-uploader-progress uploader-id])]
       (fn [] [elements/line-diagram {:indent :both :sections [{:color :primary   :value @uploader-progress}
                                                               {:color :highlight :value (- 100 @uploader-progress)}]}])))

(defn- upload-progress-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [file-count files-size files-uploaded? request-aborted? uploaded-size]}]
  (let [progress-label {:content :uploading-n-files-in-progress... :replacements [file-count]}
        label          (cond files-uploaded? :files-uploaded request-aborted? :aborted :else progress-label)]
       [elements/label {:content label :font-size :xs :color :default :layout :fit :indent :left :min-height :l}]))

(defn- upload-progress-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id {:keys [request-sent?] :as uploader-props}]
  (if request-sent? [:<> [elements/horizontal-separator {:size :m}]
                         [elements/row {:content [:<> [upload-progress-label   uploader-id uploader-props]
                                                      [abort-upload-button   uploader-id uploader-props]]
                                        :horizontal-align :space-between}]
                         [:div {:style {:width "100%"}}
                               [upload-progress-diagram uploader-id uploader-props]]
                         [elements/horizontal-separator {:size :xs}]]))

(defn- upload-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [components/subscriber uploader-id
                         {:render-f #'upload-progress-structure
                          :subscriber [:storage/get-file-uploader-props uploader-id]}])

(defn- upload-progress-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [dialog-id {:keys [uploader-ids]}]
  (reduce #(conj %1 ^{:key %2} [upload-progress %2])
           [:<>] uploader-ids))

(defn- upload-progress-notification-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [dialog-id upload-props]
  [:<> [upload-progress-list dialog-id upload-props]
       [elements/horizontal-separator {:size :m}]])

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
