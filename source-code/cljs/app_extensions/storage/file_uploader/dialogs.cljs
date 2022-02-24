
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.file-uploader.dialogs
    (:require [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [app-extensions.storage.file-uploader.engine :as file-uploader.engine]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- abort-progress-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [request-id         (file-uploader.engine/request-id uploader-id)
        files-uploaded?   @(a/subscribe [:sync/request-successed? request-id])
        request-aborted?  @(a/subscribe [:sync/request-aborted?   request-id])
        request-failured? @(a/subscribe [:sync/request-failured?  request-id])]
       (if-not (or files-uploaded? request-aborted? request-failured?)
               [elements/icon-button {:tooltip :abort! :preset :close :height :l
                                      :on-click [:sync/abort-request! request-id]}])))

(defn- progress-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  ; Az upload-progress-diagram komponens önálló feliratkozással rendelkezik, hogy a feltöltési folyamat
  ; sokszoros változása ne kényszerítse a többi komponenst újra renderelődésre!
  (let [request-id         (file-uploader.engine/request-id uploader-id)
        uploader-progress @(a/subscribe [:storage.file-uploader/get-uploader-progress uploader-id])
        request-aborted?  @(a/subscribe [:sync/request-aborted?   request-id])
        request-failured? @(a/subscribe [:sync/request-failured?  request-id])
        line-color (cond request-aborted? :muted request-failured? :warning :default :primary)]
       [elements/line-diagram {:indent :both :sections [{:color line-color :value        uploader-progress}
                                                        {:color :highlight :value (- 100 uploader-progress)}]}]))

(defn- progress-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [request-id         (file-uploader.engine/request-id uploader-id)
        files-uploaded?   @(a/subscribe [:sync/request-successed? request-id])
        request-aborted?  @(a/subscribe [:sync/request-aborted?   request-id])
        request-failured? @(a/subscribe [:sync/request-failured?  request-id])
        file-count        @(a/subscribe [:storage.file-uploader/get-file-count uploader-id])
        progress-label {:content :uploading-n-files-in-progress... :replacements [file-count]}
        label (cond files-uploaded? :files-uploaded request-aborted? :aborted request-failured? :file-upload-failure :default progress-label)]
       [elements/label {:content label :font-size :xs :color :default :layout :fit :indent :left :min-height :l}]))

(defn- progress-state
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [request-id     (file-uploader.engine/request-id uploader-id)
        request-sent? @(a/subscribe [:sync/request-sent? request-id])]
       (if request-sent? [:<> [elements/horizontal-separator {:size :m}]
                              [elements/row {:content [:<> [progress-label        uploader-id]
                                                           [abort-progress-button uploader-id]]
                                             :horizontal-align :space-between}]
                              [:div {:style {:width "100%"}}
                                    [progress-diagram uploader-id]]
                              [elements/horizontal-separator {:size :xs}]])))

(defn- progress-list
  ; WARNING!
  [dialog-id]
  (let [uploader-ids @(a/subscribe [:storage.file-uploader/get-uploader-ids])]
       (reduce #(conj %1 ^{:key %2} [progress-state %2])
                [:<>] uploader-ids)))

(defn- progress-notification-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [dialog-id]
  [:<> [progress-list dialog-id]
       [elements/horizontal-separator {:size :m}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.file-uploader/render-progress-notification!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/blow-bubble! :storage.file-uploader/progress-notification
                    {:body #'progress-notification-body
                     :autopop? false :user-close? false}])
