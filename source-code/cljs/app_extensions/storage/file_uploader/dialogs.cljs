
(ns app-extensions.storage.file-uploader.dialogs
    (:require [mid-fruits.candy     :refer [param return]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.storage.file-uploader.engine :as engine]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- abort-progress-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id {:keys [files-uploaded? request-aborted?]}]
  (let [request-id (engine/request-id uploader-id)]
       (if-not (or files-uploaded? request-aborted?)
               [elements/icon-button {:tooltip :abort! :preset :close :height :l
                                      :on-click [:sync/abort-request! request-id]}])))

(defn- progress-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id _]
  ; Az upload-progress-diagram komponens önálló feliratkozással rendelkezik, hogy a feltöltési folyamat
  ; változása ne kényszerítse a többi komponenst sokszoros újra renderelődésre!
  (let [uploader-progress (a/subscribe [:storage.file-uploader/get-uploader-progress uploader-id])]
       (fn [] [elements/line-diagram {:indent :both :sections [{:color :primary   :value @uploader-progress}
                                                               {:color :highlight :value (- 100 @uploader-progress)}]}])))

(defn- progress-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [file-count files-size files-uploaded? request-aborted? uploaded-size]}]
  (let [progress-label {:content :uploading-n-files-in-progress... :replacements [file-count]}
        label          (cond files-uploaded? :files-uploaded request-aborted? :aborted :else progress-label)]
       [elements/label {:content label :font-size :xs :color :default :layout :fit :indent :left :min-height :l}]))

(defn- progress-state-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id {:keys [request-sent?] :as uploader-props}]
  (if request-sent? [:<> [elements/horizontal-separator {:size :m}]
                         [elements/row {:content [:<> [progress-label        uploader-id uploader-props]
                                                      [abort-progress-button uploader-id uploader-props]]
                                        :horizontal-align :space-between}]
                         [:div {:style {:width "100%"}}
                               [progress-diagram uploader-id uploader-props]]
                         [elements/horizontal-separator {:size :xs}]]))

(defn- progress-state
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [components/subscriber uploader-id
                         {:render-f #'progress-state-structure
                          :subscriber [:storage.file-uploader/get-uploader-props uploader-id]}])

(defn- progress-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [dialog-id {:keys [uploader-ids]}]
  (reduce #(conj %1 ^{:key %2} [progress-state %2])
           [:<>] uploader-ids))

(defn- progress-notification-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [dialog-id notification-props]
  [:<> [progress-list dialog-id notification-props]
       [elements/horizontal-separator {:size :m}]])

(defn- progress-notification
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [dialog-id]
  [components/subscriber dialog-id
                         {:render-f   #'progress-notification-structure
                          :subscriber [:storage.file-uploader/get-notification-props]}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.file-uploader/render-progress-notification!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/blow-bubble! :storage.file-uploader/progress-notification
                    {:body #'progress-notification
                     :autopop? false :user-close? false}])
