
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.file-uploader.views
    (:require [extensions.storage.file-uploader.helpers :as file-uploader.helpers]
              [extensions.storage.media-browser.helpers :as media-browser.helpers]
              [layouts.popup-a.api                      :as popup-a]
              [mid-fruits.css                           :as css]
              [mid-fruits.io                            :as io]
              [mid-fruits.format                        :as format]
              [mid-fruits.math                          :as math]
              [mid-fruits.string                        :as string]
              [plugins.item-browser.api                 :as item-browser]
              [x.app-core.api                           :as a]
              [x.app-elements.api                       :as elements]
              [x.app-media.api                          :as media]))



;; -- Temporary components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id uploader-props]
  [:input#storage--file-selector {:multiple 1 :type "file"
                                  :accept     (file-uploader.helpers/uploader-props->allowed-extensions-list uploader-props)
                                  :on-change #(a/dispatch [:storage.file-uploader/files-selected-to-upload uploader-id])}])



;; -- Dialog components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn abort-progress-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [request-id         (file-uploader.helpers/request-id uploader-id)
        files-uploaded?   @(a/subscribe [:sync/request-successed? request-id])
        request-aborted?  @(a/subscribe [:sync/request-aborted?   request-id])
        request-failured? @(a/subscribe [:sync/request-failured?  request-id])]
       (if-not (or files-uploaded? request-aborted? request-failured?)
               [elements/icon-button {:height   :l
                                      :on-click [:sync/abort-request! request-id]
                                      :preset   :close}])))

(defn progress-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  ; Az upload-progress-diagram komponens önálló feliratkozással rendelkezik, hogy a feltöltési folyamat
  ; sokszoros változása ne kényszerítse a többi komponenst újra renderelődésre!
  (let [request-id         (file-uploader.helpers/request-id uploader-id)
        uploader-progress @(a/subscribe [:storage.file-uploader/get-uploader-progress uploader-id])
        request-aborted?  @(a/subscribe [:sync/request-aborted?   request-id])
        request-failured? @(a/subscribe [:sync/request-failured?  request-id])
        line-color (cond request-aborted? :warning request-failured? :warning :default :primary)]
       [elements/line-diagram {:indent   {:vertical :xs :bottom :xxs}
                               :sections [{:color line-color :value        uploader-progress}
                                          {:color :highlight :value (- 100 uploader-progress)}]}]))

(defn progress-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [request-id         (file-uploader.helpers/request-id uploader-id)
        files-uploaded?   @(a/subscribe [:sync/request-successed? request-id])
        request-aborted?  @(a/subscribe [:sync/request-aborted?   request-id])
        request-failured? @(a/subscribe [:sync/request-failured?  request-id])
        file-count        @(a/subscribe [:storage.file-uploader/get-uploading-file-count uploader-id])
        progress-label {:content :uploading-n-files-in-progress... :replacements [file-count]}
        label (cond files-uploaded? :files-uploaded request-aborted? :aborted request-failured? :file-upload-failure :default progress-label)]
       [elements/label {:color     :default
                        :content   label
                        :font-size :xs
                        :indent    {:left :xs :horizontal :xxs}}]))

(defn progress-state
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [request-id     (file-uploader.helpers/request-id uploader-id)
        request-sent? @(a/subscribe [:sync/request-sent? request-id])]
       (if request-sent? [:<> [elements/row {:content [:<> [progress-label        uploader-id]
                                                           [abort-progress-button uploader-id]]
                                             :horizontal-align :space-between
                                             :indent {:top :xs}}]
                              [:div {:style {:width "100%"}}
                                    [progress-diagram uploader-id]]])))

(defn progress-list
  ; WARNING!
  [dialog-id]
  (let [uploader-ids @(a/subscribe [:storage.file-uploader/get-uploader-ids])]
       (reduce #(conj %1 ^{:key %2} [progress-state %2])
                [:<>] uploader-ids)))

(defn progress-notification-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [dialog-id]
  [:<> [progress-list dialog-id]
       [elements/horizontal-separator {:size :m}]])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cancel-upload-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [elements/button ::cancel-upload-button
                   {:font-size   :xs
                    :hover-color :highlight
                    :indent      {:horizontal :xxs :left :xxs}
                    :keypress    {:key-code 27}
                    :on-click    [:storage.file-uploader/cancel-uploader! uploader-id]
                    :preset      :cancel}])

(defn upload-files-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [all-files-cancelled?     @(a/subscribe [:storage.file-uploader/all-files-cancelled?     uploader-id])
        max-upload-size-reached? @(a/subscribe [:storage.file-uploader/max-upload-size-reached? uploader-id])
        capacity-limit-exceeded? @(a/subscribe [:storage.file-uploader/capacity-limit-exceeded? uploader-id])]
       [elements/button ::upload-files-button
                        {:disabled?   (or all-files-cancelled? max-upload-size-reached? capacity-limit-exceeded?)
                         :font-size   :xs
                         :hover-color :highlight
                         :keypress    {:key-code 13}
                         :indent      {:horizontal :xxs :right :xxs}
                         :on-click    [:storage.file-uploader/start-progress! uploader-id]
                         :preset      :upload}]))

(defn available-capacity-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  ; XXX#0506
  ; - Az available-capacity-label felirat elements/text elem használatával van megjelenítve,
  ;   így kis méretű képernyőkön a szöveg képes megtörni (az elements/label elemben nem törik meg a szöveg).
  ;
  ; - A {:horizontal-align :center} beállítás használatával kis méretű képernyőkön a szöveg a középre
  ;   igazítva törik meg.
  ;
  ; - Az {:indent {:vertical :xs}} beállítás használatával kis méretű képernyőkön a szöveg nem
  ;   ér hozzá a képernyő széléhez.
  (let [capacity-limit-exceeded? @(a/subscribe [:storage.file-uploader/capacity-limit-exceeded? uploader-id])
        free-capacity            @(a/subscribe [:storage.capacity-handler/get-free-capacity])
        free-capacity             (-> free-capacity io/B->MB format/decimals)]
       [elements/text ::available-capacity-label
                      {:color            (if capacity-limit-exceeded? :warning :muted)
                       :content          {:content :available-capacity-in-storage-is :replacements [free-capacity]}
                       :font-size        :xs
                       :font-weight      :bold
                       :horizontal-align :center
                       :indent           {:vertical :xs}}]))

(defn uploading-size-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  ; XXX#0506
  (let [files-size               @(a/subscribe [:storage.file-uploader/get-files-size           uploader-id])
        max-upload-size-reached? @(a/subscribe [:storage.file-uploader/max-upload-size-reached? uploader-id])
        max-upload-size          @(a/subscribe [:storage.capacity-handler/get-max-upload-size])
        files-size      (-> files-size      io/B->MB format/decimals)
        max-upload-size (-> max-upload-size io/B->MB format/decimals)]
       [elements/text ::uploading-size-label
                      {:color            (if max-upload-size-reached? :warning :muted)
                       :content          {:content :uploading-size-is :replacements [files-size max-upload-size]}
                       :font-size        :xs
                       :font-weight      :bold
                       :horizontal-align :center
                       :indent           {:vertical :xs}}]))

(defn file-upload-summary
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [elements/column {:content [:<> [available-capacity-label uploader-id]
                                  [uploading-size-label     uploader-id]
                                  [elements/horizontal-separator {:size :s}]]
                    :horizontal-align :center}])

(defn header-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [elements/horizontal-polarity ::file-uploader-action-buttons
                                {:start-content [cancel-upload-button uploader-id]
                                 :end-content   [upload-files-button  uploader-id]}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [:<> [header-buttons      uploader-id]
       [file-upload-summary uploader-id]])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id file-dex]
  (let [file-cancelled? @(a/subscribe [:storage.file-uploader/get-file-prop uploader-id file-dex :cancelled?])
        filename        @(a/subscribe [:storage.file-uploader/get-file-prop uploader-id file-dex :filename])
        filesize        @(a/subscribe [:storage.file-uploader/get-file-prop uploader-id file-dex :filesize])
        object-url      @(a/subscribe [:storage.file-uploader/get-file-prop uploader-id file-dex :object-url])
        filesize         (-> filesize io/B->MB format/decimals (str " MB"))]
       [:div {:style {:align-items "center" :border-bottom "1px solid #f0f0f0" :display "flex"}}
             (if (io/filename->image? filename)
                 [elements/thumbnail {:border-radius :s :height :s :indent {:horizontal :xxs :vertical :xs}
                                      :uri object-url :width :l}]
                 [elements/icon {:icon :insert_drive_file :indent {:horizontal :m :vertical :xl}}])
             [:div {:style {:flex-grow 1}}
                   [elements/label {:content filename                :style {:color "#333" :line-height "18px"}}]
                   [elements/label {:content filesize :font-size :xs :style {:color "#888" :line-height "18px"}}]]
             (if file-cancelled? [elements/icon {:icon :radio_button_unchecked :indent {:right :xs} :size :s}]
                                 [elements/icon {:icon :highlight_off          :indent {:right :xs} :size :s}])]))

(defn file-item
  [uploader-id file-dex]
  (let [file-cancelled? @(a/subscribe [:storage.file-uploader/get-file-prop uploader-id file-dex :cancelled?])]
       [elements/toggle {:content     [file-item-structure uploader-id file-dex]
                         :hover-color :highlight
                         :on-click    [:storage.file-uploader/toggle-file-upload! uploader-id file-dex]
                         :style       (if file-cancelled? {:opacity ".5"})}]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  (let [file-count @(a/subscribe [:storage.file-uploader/get-selected-file-count uploader-id])]
       (letfn [(f [file-list file-dex]
                  (conj file-list ^{:key (str uploader-id file-dex)}
                                   [file-item uploader-id file-dex]))]
              (reduce f [:<>] (range file-count)))))

 

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id]
  [popup-a/layout :storage.file-uploader/view
                  {:body      [body uploader-id]
                   :header    [header uploader-id]
                   :min-width :s}])
