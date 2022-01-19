
(ns app-extensions.storage.file-uploader
    (:require [app-fruits.dom       :as dom]
              [mid-fruits.candy     :refer [param return]]
              [mid-fruits.css       :as css]
              [mid-fruits.io        :as io]
              [mid-fruits.format    :as format]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.map       :refer [dissoc-in]]
              [mid-fruits.math      :as math]
              [mid-fruits.string    :as string]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-media.api      :as media]
              [x.app-tools.api      :as tools]
              [app-extensions.storage.capacity-handler :as capacity-handler]
              [app-extensions.storage.engine           :as engine]))



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



;; -- File upload subscriptions -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-non-aborted-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (vector)
  [db [_ uploader-id]]
  (letfn [(f [o file-dex {:keys [aborted?]}]
             (if-not aborted? (conj   o file-dex)
                              (return o)))]
         (reduce-kv f [] (get-in db [:storage :file-uploader/data-items]))))

(defn- get-upload-files-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (vector)
  [db [_ uploader-id]]
  (let [destination-id (get-in db [:storage :file-uploader/meta-items :destination-id])]
       [:debug `(storage/upload-files! ~{:destination-id destination-id})]))

(defn- get-form-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (FormData object)
  [db [_ uploader-id]]
  (let [non-aborted-files (r get-non-aborted-files db uploader-id)
        file-selector     (dom/get-element-by-id "storage-file-selector")]
       (dom/file-selector->form-data file-selector non-aborted-files)))



;; -- View subscriptions ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- all-files-aborted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (boolean)
  [db [_ _]]
  (letfn [(f [{:keys [aborted?]}] (not aborted?))]
         (not (some f (get-in db [:storage :file-uploader/data-items])))))

(defn- storage-capacity-limit-exceeded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (boolean)
  [db [_ _]]
  (let [storage-free-capacity (r capacity-handler/get-storage-free-capacity db)
        upload-size           (get-in db [:storage :file-uploader/meta-items :files-size])]
       (>= upload-size storage-free-capacity)))

(defn- max-upload-size-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (boolean)
  [db [_ _]]
  (let [max-upload-size (r capacity-handler/get-max-upload-size db)
        upload-size     (get-in db [:storage :file-uploader/meta-items :files-size])]
       (> upload-size max-upload-size)))

(defn- get-file-uploader-file-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (integer) file-dex
  ;
  ; @return (map)
  ;  {}
  [db [_ _ file-dex]]
  (get-in db [:storage :file-uploader/data-items file-dex]))

(a/reg-sub :storage/get-file-uploader-file-props get-file-uploader-file-props)

(defn- get-file-uploader-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (map)
  ;  {}
  [db [_ uploader-id]]
  {:all-files-aborted?               (r all-files-aborted?               db uploader-id)
   :files-size                       (get-in db [:storage :file-uploader/meta-items :files-size])
   :max-upload-size-reached?         (r max-upload-size-reached?         db uploader-id)
   :storage-capacity-limit-exceeded? (r storage-capacity-limit-exceeded? db uploader-id)
   :max-upload-size                  (r capacity-handler/get-max-upload-size       db)
   :storage-free-capacity            (r capacity-handler/get-storage-free-capacity db)})

(a/reg-sub :storage/get-file-uploader-header-props get-file-uploader-header-props)

(defn- get-file-uploader-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (map)
  ;  {}
  [db [_ uploader-id]]
  {:all-files-aborted? (r all-files-aborted? db uploader-id)
   :file-count         (count (get-in db [:storage :file-uploader/data-items]))})

(a/reg-sub :storage/get-file-uploader-body-props get-file-uploader-body-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- abort-file-upload!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (integer) file-dex
  ;
  ; @return (map)
  [db [_ _ file-dex]]
  (let [filesize (get-in db [:storage :file-uploader/data-items file-dex :filesize])]
       (-> db (assoc-in  [:storage :file-uploader/data-items file-dex :aborted?] true)
              (update-in [:storage :file-uploader/meta-items :files-size] - filesize)
              (update-in [:storage :file-uploader/meta-items :file-count] dec))))

(a/reg-event-db :storage/abort-file-upload! abort-file-upload!)

(defn- store-uploader-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) uploader-props
  ;
  ; @return (map)
  [db [_ _ uploader-props]]
  (assoc-in db [:storage :file-uploader/meta-items] uploader-props))



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
  ; @param (keyword) uploader-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [uploader-id _]
  [elements/button ::cancel-upload-button
                   {:on-click [:storage/abort-file-uploading! uploader-id]
                    :preset :cancel-button :indent :both :keypress {:key-code 27}}])

(defn- upload-files-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) header-props
  ;  {:all-files-aborted? (boolean)
  ;   :max-upload-size-reached? (boolean)
  ;   :storage-capacity-limit-exceeded? (boolean)}
  ;
  ; @return (component)
  [uploader-id {:keys [all-files-aborted? max-upload-size-reached? storage-capacity-limit-exceeded?]}]
  [elements/button ::upload-files-button
                   {:disabled? (or all-files-aborted? max-upload-size-reached? storage-capacity-limit-exceeded?)
                    :on-click [:storage/upload-files! uploader-id]
                    :preset :upload-button :indent :both :keypress {:key-code 13}}])

(defn- available-capacity-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) header-props
  ;  {:storage-capacity-limit-exceeded? (boolean)
  ;   :storage-free-capacity (B)}
  ;
  ; @return (component)
  [_ {:keys [storage-capacity-limit-exceeded? storage-free-capacity]}]
  [elements/text {:content :available-capacity-in-storage-is :font-size :xs :font-weight :bold :layout :fit
                  :replacements [(-> storage-free-capacity io/B->MB math/round)]
                  :color        (if storage-capacity-limit-exceeded? :warning :muted)}])

(defn- uploading-size-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) header-props
  ;  {:files-size (B)
  ;   :max-upload-size-reached? (boolean)
  ;   :max-upload-size (B)}
  ;
  ; @return (component)
  [_ {:keys [files-size max-upload-size-reached? max-upload-size]}]
  [elements/text {:content :uploading-size-is :font-size :xs :font-weight :bold :layout :fit
                  :replacements [(-> files-size io/B->MB      format/decimals)
                                 (-> max-upload-size io/B->MB math/round)]
                  :color        (if max-upload-size-reached? :warning :muted)}])

(defn- file-upload-summary
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [uploader-id header-props]
  [elements/column {:content [:<> [available-capacity-label uploader-id header-props]
                                  [uploading-size-label     uploader-id header-props]
                                  [elements/horizontal-separator {:size :s}]]
                    :horizontal-align :center}])

(defn- action-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [uploader-id header-props]
  [elements/horizontal-polarity ::file-uploader-action-buttons
                                {:start-content [cancel-upload-button uploader-id header-props]
                                 :end-content   [upload-files-button  uploader-id header-props]}])

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) header-props
  ;
  ; @return (component)
  [uploader-id header-props]
  [:<> [action-buttons      uploader-id header-props]
       [file-upload-summary uploader-id header-props]])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (component)
  [uploader-id]
  [components/subscriber uploader-id
                         {:render-f   #'header-structure
                          :subscriber [:storage/get-file-uploader-header-props uploader-id]}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-files-to-upload-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) body-props
  ;  {:all-files-aborted? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [all-files-aborted?]}]
  (if all-files-aborted? [elements/label ::no-files-to-upload-label
                                         {:content :no-files-selected :color :muted}]))

(defn- file-item-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) body-props
  ; @param (integer) file-dex
  ; @param (map) file-props
  ;  {:filename (string)
  ;   :object-url (string)}
  ;
  ; @return (component)
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
  ;
  ; @param (keyword) uploader-id
  ; @param (map) body-props
  ; @param (integer) file-dex
  ; @param (map) file-props
  ;  {:filename (string)
  ;   :filesize (B)}
  ;
  ; @return (component)
  [_ _ _ {:keys [filename filesize]}]
  [:div.storage--media-item--details
    [elements/label {:content (str filename)
                     :layout :fit :selectable? true  :color :default}]
    [elements/label {:content (-> filesize io/B->MB format/decimals (str " MB"))
                     :layout :fit :selectable? false :color :muted}]])

(defn- file-item-actions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) body-props
  ; @param (integer) file-dex
  ; @param (map) file-props
  ;
  ; @return (component)
  [uploader-id _ file-dex _]
  [elements/button {:preset :default-icon-button :icon :highlight_off
                    :on-click [:storage/abort-file-upload! uploader-id file-dex]}])

(defn- file-item-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) body-props
  ; @param (integer) file-dex
  ; @param (map) file-props
  ;  {:aborted? (boolean)}
  ;
  ; @return (component)
  [uploader-id body-props file-dex {:keys [aborted?] :as file-props}]
  (if-not aborted? [elements/row {:content [:<> [file-item-actions uploader-id body-props file-dex file-props]
                                                [file-item-header  uploader-id body-props file-dex file-props]
                                                [file-item-details uploader-id body-props file-dex file-props]]}]))

(defn- file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) body-props
  ; @param (integer) file-dex
  ; @param (map) file-props
  ;
  ; @return (component)
  [uploader-id body-props file-dex]
  ; - A file-item komponens minden példánya feliratkozik az adott fájl tulajdonságira a Re-Frame
  ;   adatbázisban, így az egyes komponensek nem paraméterként kapják a file-list listából az adatot.
  ; - Ha egy fájl eltávolításra kerül a felsorolásból, akkor nem renderelődik újra az egész lista.
  (let [file-props (a/subscribe [:storage/get-file-uploader-file-props uploader-id file-dex])]
       (fn [] [file-item-structure uploader-id body-props file-dex @file-props])))

(defn- file-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) body-props
  ;  {:file-count (integer)}
  ;
  ; @return (component)
  [uploader-id {:keys [file-count] :as body-props}]
  (letfn [(f [file-list file-dex]
             (conj file-list ^{:key (str uploader-id file-dex)}
                              [file-item uploader-id body-props file-dex]))]
         (reduce f [:<>] (range 0 file-count))))

(defn- body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) body-props
  ;
  ; @return (component)
  [uploader-id body-props]
  [:<> [file-list                uploader-id body-props]
       [no-files-to-upload-label uploader-id body-props]])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (component)
  [uploader-id]
  [components/subscriber uploader-id
                         {:render-f   #'body-structure
                          :subscriber [:storage/get-file-uploader-body-props uploader-id]}])



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/upload-files!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  (fn [{:keys [db]} [_ uploader-id]]
      (let [query     (r get-upload-files-query db uploader-id)
            form-data (r get-form-data          db uploader-id)]
           [:sync/send-query! (keyword/add-namespace uploader-id :upload-files!)
                              {:body (dom/merge-to-form-data! form-data {:query query})}])))
                              ;:idle-timeout 1000
                               ;:on-failure [:xxx]
                               ;:on-success [:xxx]}])))



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
  ; A file-selector input fájltallózójának on-change eseménye indítja el a feltöltés inicializálását.
  ;
  ; @param (keyword) uploader-id
  (fn [{:keys [db]} [_ uploader-id]]
      (let [file-selector      (dom/get-element-by-id "storage-file-selector")
            any-file-selected? (dom/file-selector->any-file-selected? file-selector)
            files-data         (dom/file-selector->files-data         file-selector)
            files-meta         (dom/file-selector->files-meta         file-selector)]
            ; 1. Eltárolja a kiválasztott fájlok számát, méretét és egyéb adatait.
           {:db (-> db (assoc-in  [:storage :file-uploader/data-items]       files-data)
                       (update-in [:storage :file-uploader/meta-items] merge files-meta))
            ; 2. Ha van kiválasztva fájl a fájltallózóval, akkor megnyitja a fájlfeltöltő ablakot.
            :dispatch-if [any-file-selected? [:storage/render-file-uploader! uploader-id]]})))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage/abort-file-uploading!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  (fn [{:keys [db]} [_ uploader-id]]
      {;:db (-> db (dissoc-in [:storage :file-uploader/data-items])
       ;           (dissoc-in [:storage :file-uploader/meta-items])
       :dispatch [:ui/close-popup! :storage/file-uploader]}))

(a/reg-event-fx
  :storage/render-file-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  (fn [_ [_ uploader-id]]
      [:ui/add-popup! :storage/file-uploader
                      {:body   [body   uploader-id]
                       :header [header uploader-id]}]))

(a/reg-event-fx
  :storage/load-file-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) uploader-id
  ; @param (map) uploader-props
  ;  {:allowed-extensions (strings in vector)(opt)
  ;   :destination-id (string)}
  ;
  ; @usage
  ;  [:storage/load-file-uploader! {...}]
  ;
  ; @usage
  ;  [:storage/load-file-uploader! :my-uploader {...}]
  ;
  ; @usage
  ;  [:storage/load-file-uploader! {:allowed-extensions ["htm" "html" "xml"]
  ;                                 :destination-id "..."}]
  (fn [{:keys [db]} event-vector]
      ; Az uploader-id egyedi azonosító alkalmazása lehetővé teszi, hogy a különböző névterek
      ; által indított fájlfeltöltők ...
      (let [uploader-id    (a/event-vector->second-id   event-vector)
            uploader-props (a/event-vector->first-props event-vector)]
           {:db (r store-uploader-props! db uploader-id uploader-props)
            :storage/open-file-selector! [uploader-id uploader-props]
            :dispatch [:sync/send-query! :storage/synchronize-file-uploader! ; Silent-mode / no progress-bar
                                         {:query [:debug `(:storage/download-capacity-details ~{})]}]})))
