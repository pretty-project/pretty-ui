
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
  [uploader-id _]
  [elements/button ::cancel-upload-button
                   {:on-click [:storage.file-uploader/cancel-uploader! uploader-id]
                    :preset :cancel-button :indent :both :keypress {:key-code 27}}])

(defn- upload-files-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id {:keys [all-files-cancelled? max-upload-size-reached? storage-capacity-limit-exceeded?]}]
  [elements/button ::upload-files-button
                   {:disabled? (or all-files-cancelled? max-upload-size-reached? storage-capacity-limit-exceeded?)
                    :on-click [:storage.file-uploader/start-progress! uploader-id]
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
                          :subscriber [:storage.file-uploader/get-header-props uploader-id]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-files-to-upload-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [all-files-cancelled?]}]
  (if all-files-cancelled? [elements/label ::no-files-to-upload-label
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
    [elements/label {:min-height :s :selectable? true  :color :default :content filename}]
    [elements/label {:min-height :s :selectable? false :color :muted
                     :content (-> filesize io/B->MB format/decimals (str " MB"))}]])

(defn- file-item-actions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id _ file-dex _]
  [elements/button {:preset :default-icon-button :icon :highlight_off
                    :on-click [:storage.file-uploader/cancel-file-upload! uploader-id file-dex]}])

(defn- file-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id body-props file-dex {:keys [cancelled?] :as file-props}]
  (if-not cancelled? [elements/row {:content [:<> [file-item-actions uploader-id body-props file-dex file-props]
                                                  [file-item-header  uploader-id body-props file-dex file-props]
                                                  [file-item-details uploader-id body-props file-dex file-props]]}]))

(defn- file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id body-props file-dex]
  ; - A file-item komponens minden példánya feliratkozik az adott fájl tulajdonságira a Re-Frame
  ;   adatbázisban, így az egyes komponensek nem paraméterként kapják a file-list listából az adatot.
  ; - Ha egy fájl felöltése visszavonsára kerül és megváltoznak a lista adatai, akkor a változásban
  ;   nem értintett fájlok nem renderelődnek újra
  (let [file-props (a/subscribe [:storage.file-uploader/get-file-props uploader-id file-dex])]
       (fn [] [file-item-structure uploader-id body-props file-dex @file-props])))

(defn- file-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id {:keys [file-count] :as body-props}]
  ; A file-list komponens a feltöltésre kijelölt fájlok számának kezdő értékére iratkozik fel,
  ; így ha egy fájl feltöltése visszavonsára kerül, akkor sem változik meg a file-list komponens
  ; body-props paramétere, ami miatt újra renderelődne a lista.
  (letfn [(f [file-list file-dex]
             (conj file-list ^{:key (str uploader-id file-dex)}
                              [file-item uploader-id body-props file-dex]))]
         (reduce f [:<>] (range file-count))))

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
                          :subscriber [:storage.file-uploader/get-body-props uploader-id]}])



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- open-file-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [uploader-id uploader-props]
  (tools/append-temporary-component! [file-selector uploader-id uploader-props]
                                    #(-> "storage--file-selector" dom/get-element-by-id .click)))

(a/reg-fx_ :storage.file-uploader/open-file-selector! open-file-selector!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.file-uploader/render-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      [:ui/add-popup! :storage.file-uploader/view
                      {:body   [body   uploader-id]
                       :header [header uploader-id]}]))
