
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.file-uploader.views
    (:require [extensions.storage.file-uploader.helpers :as file-uploader.helpers]
              [mid-fruits.css                           :as css]
              [mid-fruits.io                            :as io]
              [mid-fruits.format                        :as format]
              [mid-fruits.math                          :as math]
              [mid-fruits.string                        :as string]
              [x.app-core.api                           :as a]
              [x.app-elements.api                       :as elements]
              [x.app-media.api                          :as media]))



;; -- Temporary components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id uploader-props]
  [:input#storage--file-selector {:multiple 1 :type "file"
                                  :accept (file-uploader.helpers/uploader-props->allowed-extensions-list uploader-props)
                                  :on-change #(a/dispatch [:storage.file-uploader/files-selected-to-upload uploader-id])}])



;; -- Dialog components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- abort-progress-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [request-id         (file-uploader.helpers/request-id uploader-id)
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
  (let [request-id         (file-uploader.helpers/request-id uploader-id)
        uploader-progress @(a/subscribe [:storage.file-uploader/get-uploader-progress uploader-id])
        request-aborted?  @(a/subscribe [:sync/request-aborted?   request-id])
        request-failured? @(a/subscribe [:sync/request-failured?  request-id])
        line-color (cond request-aborted? :muted request-failured? :warning :default :primary)]
       [elements/line-diagram {:indent :both :sections [{:color line-color :value        uploader-progress}
                                                        {:color :highlight :value (- 100 uploader-progress)}]}]))

(defn- progress-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [request-id         (file-uploader.helpers/request-id uploader-id)
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
  (let [request-id     (file-uploader.helpers/request-id uploader-id)
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



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-upload-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
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
