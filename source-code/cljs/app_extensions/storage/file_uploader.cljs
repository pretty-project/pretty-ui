
(ns app-extensions.storage.file-uploader
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.css     :as css]
              [mid-fruits.io      :as io]
              [mid-fruits.format  :as format]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [x.app-media.api    :as media]
              [x.app-tools.api    :as tools]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn uploader-props->allowed-extensions-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) uploader-props
  ;  {:allowed-extensions (strings in vector)(opt)}
  ;
  ; @example
  ;  (uploader-props->allowed-extensions-list {:allowed-extensions ["gif" "png"]})
  ;  => ".gif, .png"
  ;
  ; @example
  ;  (uploader-props->allowed-extensions-list {})
  ;  => ".mp4, .xml, ..."
  ;
  ; @return (string)
  [{:keys [allowed-extensions]}]
  (let [allowed-extensions (or allowed-extensions (media/allowed-extensions))]
       (str "." (string/join allowed-extensions ", ."))))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- all-files-aborted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (boolean)
  [db [_ uploader-id]]
  (let [non-aborted-files (get-in db [:storage :file-uploader/data-items])]
       (-> non-aborted-files vector/nonempty? not)))

(defn- storage-capacity-limit-exceeded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (boolean)
  [db [_ uploader-id]]
  (let [;storage-free-capacity (r engine/get-storage-free-capacity db)
        upload-size           (get-in db [:storage :file-uploader/meta-items :files-size])]))
       ;(>= upload-size storage-free-capacity)))

(defn- get-file-uploader-file-props
  [db [_ uploader-id file-dex]]
  (get-in db [:storage :file-uploader/data-items file-dex]))

(a/reg-sub :storage/get-file-uploader-file-props get-file-uploader-file-props)

(defn- get-file-uploader-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (map)
  [db [_ uploader-id]]
  {:all-files-aborted?               (r all-files-aborted?               db)
   ;:max-upload-size-reached?         (r max-upload-size-reached?         db)
   :storage-capacity-limit-exceeded? (r storage-capacity-limit-exceeded? db)})
   ;:max-upload-size                  (r a/get-storage-detail db :max-upload-size)})
   ;:storage-free-capacity            (r engine/get-storage-free-capacity db)})

(a/reg-sub :storage/get-file-uploader-header-props get-file-uploader-header-props)

(defn- get-file-uploader-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (map)
  [db [_ uploader-id]]
  {:all-files-aborted? (r all-files-aborted? db uploader-id)
   :file-count         (count (get-in db [:storage :file-uploader/data-items]))})

(a/reg-sub :storage/get-file-uploader-body-props get-file-uploader-body-props)



;; -- Temporary components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) uploader-props
  ;  {:allowed-extensions (strings in vector)(opt)}
  ;
  ; @return (hiccup)
  [uploader-id uploader-props]
  [:input#storage-file-selector {:on-change #(a/dispatch [:storage/->files-selected-to-upload uploader-id])
                                 :accept     (uploader-props->allowed-extensions-list         uploader-props)
                                 :multiple   (param 1)
                                 :type       (param "file")}])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- cancel-upload-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::cancel-upload-button
                   {:preset :cancel-button
                    :indent :both}])

(defn- upload-files-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::upload-files-button
                   {:preset :upload-button
                    :indent :both}])

(defn- available-capacity-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys []}])

(defn- file-upload-summary
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [popup-id header-props]
  [elements/column {:content [:<> [available-capacity-label popup-id header-props]]
                                  ;[elements/horizontal-separator {:size :s}]]
                                  ;[file-uploader-uploading-size popup-id view-props]]
                    :horizontal-align :center}])

(defn- action-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [popup-id header-props]
  [elements/horizontal-polarity ::file-uploader-action-buttons
                                {:start-content [cancel-upload-button popup-id header-props]
                                 :end-content   [upload-files-button  popup-id header-props]}])

(defn abc
  [_ _]
  [:div "abc"])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [popup-id header-props]
  (let [x (a/subscribe [:db/get-item [:a]])]
       (fn [] [abc nil nil])))
  ;[:<> [action-buttons      popup-id header-props]
  ;     [file-upload-summary popup-id header-props]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-files-to-upload-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;  {}
  ;
  ; @return (component)
  [_ {:keys [all-files-aborted?]}]
  (if all-files-aborted? [elements/label ::no-files-to-upload-label
                                         {:content :no-files-selected :color :muted}]))

(defn- file-item-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ; @param (integer) file-dex
  ; @param (map) file-props
  ;  {}
  ;
  ; @return (component)
  [_ _ _ {:keys [filename object-url]}]
  (if (io/filename->image? filename)
      [:div.storage--file-uploader--file-preview {:style {:background-image (css/url object-url)}}]
      [:div.storage--file-uploader--file-preview [elements/icon {:icon :insert_drive_file}]]))

(defn- file-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ; @param (integer) file-dex
  ; @param (map) file-props
  ;  {}
  ;
  ; @return (component)
  [_ _ _ {:keys [filename filesize]}]
  [:div.storage--file-uploader--file-details

    [elements/label {:content (str filename)
                     :layout :fit :selectable? true  :color :default}]
    [elements/label {:content (-> filesize io/B->MB format/decimals (str " MB"))
                     :layout :fit :selectable? false :color :muted}]])

(defn- file-item-actions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ file-dex _]
  ;[:div.storage--file-uploader--file-actions]
  [elements/button {:preset :default-icon-button :icon :highlight_off :on-click [:storage/abort-file-upload! file-dex]}])

(defn- file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ; @param (integer) file-dex
  ; @param (map) file-props
  ;
  ; @return (component)
  [popup-id body-props file-dex]
  ;[:div.storage--file-uploader--file-item
  (let [file-props (a/subscribe [:storage/get-file-uploader-file-props nil file-dex])]
       (fn []
           (if-not (get @file-props :aborted?)
             [elements/row {:content [:<> [file-item-actions popup-id body-props file-dex @file-props]
                                          [file-item-preview popup-id body-props file-dex @file-props]
                                          [file-item-details popup-id body-props file-dex @file-props]]
                            :style {:margin "6px 0"}}]))))

(defn- file-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;  {}
  ;
  ; @return (component)
  [popup-id {:keys [file-count] :as body-props}]
  (letfn [(f [file-list file-dex]
             (conj file-list ^{:key (str popup-id file-dex)}
                              [file-item popup-id body-props file-dex]))]
         (reduce f [:<>] (range 0 file-count))))

(defn- body-structure
  [])
  ;[:<> [file-list                popup-id body-props]
  ;     [no-files-to-upload-label popup-id body-props]])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;
  ; @return (component)
  [uploader-id]
  [components/subscriber :storage/file-uploader-body
                         {:render-f #'body-structure}])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- abort-file-upload!
  [db [_ uploader-id]])

(a/reg-event-db :storage/abort-file-upload!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ file-dex]]
      {:db (assoc-in db [:storage :file-uploader/data-items file-dex :aborted?] true)}))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- open-file-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) uploader-props
  [[uploader-id uploader-props]]
  (tools/append-temporary-component! [file-selector uploader-id uploader-props]
                                     #(-> "storage-file-selector" dom/get-element-by-id .click)))

(a/reg-fx :storage/open-file-selector! open-file-selector!)



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/->files-selected-to-upload
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A file-selector input fájltallózójának on-change eseménye indítja el
  ; a feltöltés inicializálását.
  ;
  ; @param (keyword) uploader-id
  (fn [{:keys [db]} [event-id uploader-id]]
      (let [file-selector      (dom/get-element-by-id "storage-file-selector")
            any-file-selected? (dom/file-selector->any-file-selected? file-selector)
            files-data         (dom/file-selector->files-data         file-selector)
            files-meta         (dom/file-selector->files-meta         file-selector)]
            ; 1. Eltárolja a kiválasztott fájlok számát, méretét és egyéb adatait.
           {:db (-> db (assoc-in [:storage :file-uploader/data-items] files-data)
                       (assoc-in [:storage :file-uploader/meta-items] files-meta))
            ; 2. Ha van kiválasztva fájl a fájltallózóval, akkor megnyitja a fájlfeltöltő ablakot.
            :dispatch-if [any-file-selected? [:storage/render-file-uploader! uploader-id]]})))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/render-file-uploader!
  (fn [_ [_ uploader-id]]
      [:ui/add-popup! :storage/file-uploader
                      {:body   [body   uploader-id]
                       :header [header uploader-id]}]))

(a/reg-event-fx
  :storage/load-file-uploader!
  ; @param (keyword)(opt) uploader-id
  ; @param (map) uploader-props
  ;  {:allowed-extensions (strings in vector)(opt)
  ;   :destination-directory-id (keyword)
  ;   :namespace (keyword)}
  ;
  ; @usage
  ;  [:storage/load-file-uploader! {...}]
  ;
  ; @usage
  ;  [:storage/load-file-uploader! :my-uploader {...}]
  ;
  ; @usage
  ;  [:storage/load-file-uploader! {:allowed-extensions ["htm" "html" "xml"]
  ;                                 :destination-directory-id :home}]
  (fn [{:keys [db]} event-vector]
      (let [uploader-id    (a/event-vector->second-id   event-vector)
            uploader-props (a/event-vector->first-props event-vector)]
           {;:db (r store-uploader-props! db uploader-id uploader-props)
            :storage/open-file-selector! [uploader-id uploader-props]
            :dispatch [:sync/send-query! :storage/synchronize-file-uploader! ; Silent / no progress-bar
                                         {:query [:debug `(:storage/download-capacity-details ~{})]}]})))
