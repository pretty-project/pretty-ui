
(ns app-extensions.storage.file-uploader.views
    (:require [app-fruits.dom       :as dom]
              [mid-fruits.candy     :refer [param return]]
              [mid-fruits.css       :as css]
              [mid-fruits.io        :as io]
              [mid-fruits.format    :as format]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.math      :as math]
              [mid-fruits.string    :as string]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-media.api      :as media]
              [x.app-tools.api      :as tools]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn uploader-props->allowed-extensions-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [allowed-extensions]}]
  (let [allowed-extensions (or allowed-extensions (media/allowed-extensions))]
       (str "." (string/join allowed-extensions ", ."))))



;; -- Temporary components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id uploader-props]
  [:input#storage--file-selector {:multiple 1 :type "file"
                                  :accept     (uploader-props->allowed-extensions-list uploader-props)
                                  :on-change #(a/dispatch [:storage/->files-selected-to-upload uploader-id])}])



;; -- Dialog components -------------------------------------------------------
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



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-upload-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id _]
  [elements/button ::cancel-upload-button
                   {:on-click [:storage/abort-file-uploading! uploader-id]
                    :preset :cancel-button :indent :both :keypress {:key-code 27}}])

(defn- upload-files-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id {:keys [all-files-aborted? max-upload-size-reached? storage-capacity-limit-exceeded?]}]
  [elements/button ::upload-files-button
                   {:disabled? (or all-files-aborted? max-upload-size-reached? storage-capacity-limit-exceeded?)
                    :on-click [:storage/upload-files! uploader-id]
                    :preset :upload-button :indent :both :keypress {:key-code 13}}])

(defn- available-capacity-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [storage-capacity-limit-exceeded? storage-free-capacity]}]
  (let [storage-free-capacity (-> storage-free-capacity io/B->MB math/round)]
       [elements/text {:content {:content :available-capacity-in-storage-is :replacements [storage-free-capacity]}
                       :font-size :xs :font-weight :bold :layout :fit
                       :color (if storage-capacity-limit-exceeded? :warning :muted)}]))

(defn- uploading-size-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [files-size max-upload-size-reached? max-upload-size]}]
  (let [files-size      (-> files-size io/B->MB      format/decimals)
        max-upload-size (-> max-upload-size io/B->MB math/round)]
       [elements/text {:content {:content :uploading-size-is :replacements [files-size max-upload-size]}
                       :font-size :xs :font-weight :bold :layout :fit
                       :color (if max-upload-size-reached? :warning :muted)}]))

(defn- file-upload-summary
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id header-props]
  [elements/column {:content [:<> [available-capacity-label uploader-id header-props]
                                  [uploading-size-label     uploader-id header-props]
                                  [elements/horizontal-separator {:size :s}]]
                    :horizontal-align :center}])

(defn- action-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id header-props]
  [elements/horizontal-polarity ::file-uploader-action-buttons
                                {:start-content [cancel-upload-button uploader-id header-props]
                                 :end-content   [upload-files-button  uploader-id header-props]}])

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id header-props]
  [:<> [action-buttons      uploader-id header-props]
       [file-upload-summary uploader-id header-props]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [components/subscriber uploader-id
                         {:render-f   #'header-structure
                          :subscriber [:storage/get-file-uploader-header-props uploader-id]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-files-to-upload-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [all-files-aborted?]}]
  (if all-files-aborted? [elements/label ::no-files-to-upload-label
                                         {:content :no-files-selected :color :muted}]))

(defn- file-item-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ _ {:keys [filename object-url]}]
  [:div.storage--media-item--preview {:style {:background-image (css/url object-url)}}])

(defn- file-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id body-props file-dex {:keys [filename] :as file-props}]
  [:div.storage--media-item--header [elements/icon {:icon :insert_drive_file}]
                                    (if (io/filename->image? filename)
                                        [file-item-preview uploader-id body-props file-dex file-props])])

(defn- file-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ _ {:keys [filename filesize]}]
  [:div.storage--media-item--details
    [elements/label {:content (str filename)
                     :layout :fit :selectable? true  :color :default}]
    [elements/label {:content (-> filesize io/B->MB format/decimals (str " MB"))
                     :layout :fit :selectable? false :color :muted}]])

(defn- file-item-actions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id _ file-dex _]
  [elements/button {:preset :default-icon-button :icon :highlight_off
                    :on-click [:storage/abort-file-upload! uploader-id file-dex]}])

(defn- file-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id body-props file-dex {:keys [aborted?] :as file-props}]
  (if-not aborted? [elements/row {:content [:<> [file-item-actions uploader-id body-props file-dex file-props]
                                                [file-item-header  uploader-id body-props file-dex file-props]
                                                [file-item-details uploader-id body-props file-dex file-props]]}]))

(defn- file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id body-props file-dex]
  ; - A file-item komponens minden példánya feliratkozik az adott fájl tulajdonságira a Re-Frame
  ;   adatbázisban, így az egyes komponensek nem paraméterként kapják a file-list listából az adatot.
  ; - Ha egy fájl eltávolításra kerül a felsorolásból és megváltoznak a lista adatai, akkor
  ;   az változásban nem értintett fájlok nem renderelődnek újra
  (let [file-props (a/subscribe [:storage/get-file-uploader-file-props uploader-id file-dex])]
       (fn [] [file-item-structure uploader-id body-props file-dex @file-props])))

(defn- file-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id {:keys [file-count] :as body-props}]
  (letfn [(f [file-list file-dex]
             (conj file-list ^{:key (str uploader-id file-dex)}
                              [file-item uploader-id body-props file-dex]))]
         (reduce f [:<>] (range 0 file-count))))

(defn- body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id body-props]
  [:<> [file-list                uploader-id body-props]
       [no-files-to-upload-label uploader-id body-props]])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [components/subscriber uploader-id
                         {:render-f   #'body-structure
                          :subscriber [:storage/get-file-uploader-body-props uploader-id]}])



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- open-file-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [[uploader-id uploader-props]]
  (tools/append-temporary-component! [file-selector uploader-id uploader-props]
                                    #(-> "storage--file-selector" dom/get-element-by-id .click)))

(a/reg-fx :storage/open-file-selector! open-file-selector!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/render-file-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      [:ui/add-popup! (keyword/add-namespace :storage uploader-id)
                      {:body   [body   uploader-id]
                       :header [header uploader-id]}]))

(a/reg-event-fx
  :storage/render-file-uploading-progress-notification!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      [:ui/blow-bubble! (keyword/add-namespace :storage uploader-id)
                        {:body        [upload-progress-notification-body uploader-id]
                         :autopop?    false
                         :user-close? false}]))
