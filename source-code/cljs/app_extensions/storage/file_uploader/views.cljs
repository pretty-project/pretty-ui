
(ns app-extensions.storage.file-uploader.views
    (:require [app-fruits.dom       :as dom]
              [mid-fruits.candy     :refer [param return]]
              [mid-fruits.css       :as css]
              [mid-fruits.io        :as io]
              [mid-fruits.format    :as format]
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
                                  :on-change #(a/dispatch [:storage.file-uploader/files-selected-to-upload uploader-id])}])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-upload-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id ]
  [elements/button ::cancel-upload-button
                   {:on-click [:storage.file-uploader/cancel-uploader! uploader-id]
                    :preset :cancel-button :indent :both :keypress {:key-code 27}}])

(defn- upload-files-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [all-files-cancelled?     @(a/subscribe [:storage.file-uploader/all-files-cancelled?     uploader-id])
        max-upload-size-reached? @(a/subscribe [:storage.file-uploader/max-upload-size-reached? uploader-id])
        capacity-limit-exceeded? @(a/subscribe [:storage.file-uploader/capacity-limit-exceeded? uploader-id])]
       [elements/button ::upload-files-button
                        {:disabled? (or all-files-cancelled? max-upload-size-reached? capacity-limit-exceeded?)
                         :on-click [:storage.file-uploader/start-progress! uploader-id]
                         :preset :upload-button :indent :both :keypress {:key-code 13}}]))

(defn- available-capacity-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [capacity-limit-exceeded? @(a/subscribe [:storage.file-uploader/capacity-limit-exceeded? uploader-id])
        free-capacity            @(a/subscribe [:storage.capacity-handler/get-free-capacity])
        free-capacity             (-> free-capacity io/B->MB math/round)]
       [elements/text {:content {:content :available-capacity-in-storage-is :replacements [free-capacity]}
                       :font-size :xs :font-weight :bold :layout :fit
                       :color (if capacity-limit-exceeded? :warning :muted)}]))

(defn- uploading-size-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [files-size               @(a/subscribe [:storage.file-uploader/get-files-size           uploader-id])
        max-upload-size-reached? @(a/subscribe [:storage.file-uploader/max-upload-size-reached? uploader-id])
        max-upload-size          @(a/subscribe [:storage.capacity-handler/get-max-upload-size])
        files-size      (-> files-size io/B->MB      format/decimals)
        max-upload-size (-> max-upload-size io/B->MB math/round)]
       [elements/text {:content {:content :uploading-size-is :replacements [files-size max-upload-size]}
                       :font-size :xs :font-weight :bold :layout :fit
                       :color (if max-upload-size-reached? :warning :muted)}]))

(defn- file-upload-summary
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [elements/column {:content [:<> [available-capacity-label uploader-id]
                                  [uploading-size-label     uploader-id]
                                  [elements/horizontal-separator {:size :s}]]
                    :horizontal-align :center}])

(defn- action-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [elements/horizontal-polarity ::file-uploader-action-buttons
                                {:start-content [cancel-upload-button uploader-id]
                                 :end-content   [upload-files-button  uploader-id]}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [:<> [action-buttons      uploader-id]
       [file-upload-summary uploader-id]])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-files-to-upload-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (if-let [all-files-cancelled? @(a/subscribe [:storage.file-uploader/all-files-cancelled? uploader-id])]
          [elements/label ::no-files-to-upload-label
                          {:content :no-files-selected :color :muted}]))

(defn- file-item-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id file-dex]
  (let [object-url @(a/subscribe [:storage.file-uploader/get-file-prop uploader-id file-dex :object-url])]
       [:div.storage--media-item--preview {:style {:background-image (css/url object-url)}}]))

(defn- file-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id file-dex]
  (let [filename @(a/subscribe [:storage.file-uploader/get-file-prop uploader-id file-dex :filename])]
       [:div.storage--media-item--header [elements/icon {:icon :insert_drive_file}]
                                         (if (io/filename->image? filename)
                                             [file-item-preview uploader-id file-dex])]))

(defn- file-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id file-dex]
  (let [filename @(a/subscribe [:storage.file-uploader/get-file-prop uploader-id file-dex :filename])
        filesize @(a/subscribe [:storage.file-uploader/get-file-prop uploader-id file-dex :filesize])]
       [:div.storage--media-item--details
         [elements/label {:min-height :s :selectable? true  :color :default :content filename}]
         [elements/label {:min-height :s :selectable? false :color :muted
                          :content (-> filesize io/B->MB format/decimals (str " MB"))}]]))

(defn- file-item-actions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id file-dex]
  [elements/button {:preset :default-icon-button :icon :highlight_off
                    :on-click [:storage.file-uploader/cancel-file-upload! uploader-id file-dex]}])

(defn- file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id file-dex]
  (let [file-cancelled? @(a/subscribe [:storage.file-uploader/get-file-prop uploader-id file-dex :cancelled?])]
       (if-not file-cancelled? [elements/row {:content [:<> [file-item-actions uploader-id file-dex]
                                                            [file-item-header  uploader-id file-dex]
                                                            [file-item-details uploader-id file-dex]]}])))

(defn- file-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  ; A file-list komponens a feltöltésre kijelölt fájlok számának kezdő értékére iratkozik fel,
  ; így ha egy fájl feltöltése visszavonsára kerül, akkor sem változik meg a file-list komponens
  ; body-props paramétere, ami miatt újra renderelődne a lista.
  (let [file-count @(a/subscribe [:storage.file-uploader/get-file-count uploader-id])]
       (letfn [(f [file-list file-dex]
                  (conj file-list ^{:key (str uploader-id file-dex)}
                                   [file-item uploader-id file-dex]))]
              (reduce f [:<>] (range file-count)))))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [:<> [file-list                uploader-id]
       [no-files-to-upload-label uploader-id]])



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- open-file-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id uploader-props]
  (tools/append-temporary-component! [file-selector uploader-id uploader-props]
                                    #(-> "storage--file-selector" dom/get-element-by-id .click)))

(a/reg-fx :storage.file-uploader/open-file-selector! open-file-selector!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.file-uploader/render-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      [:ui/add-popup! :storage.file-uploader/view
                      {:body   [body   uploader-id]
                       :header [header uploader-id]}]))
