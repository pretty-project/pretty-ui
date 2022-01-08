
(ns app-extensions.storage.file-uploader
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.io      :as io]
              [mid-fruits.candy   :refer [param return]]
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

(defn- get-file-uploader-body-props
  ; @param (keyword) uploader-id
  ;
  ; @return (map)
  [db [_ uploader-id]]
  (let [non-aborted-files (get-in db [:storage :file-uploader/data-items])]
       {:all-files-aborted? (-> non-aborted-files vector/nonempty? not)
        :file-list          (param non-aborted-files)}))

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



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-files-to-upload-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [all-files-aborted?]}]
  (if all-files-aborted? [elements/label ::no-files-to-upload-label
                                         {:content :no-files-selected :color :muted}]))

(defn- file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ dex {:keys [filename filesize]}]
  [:div.storage--file-uploader--file-item
    [:div.storage--file-uploader--file-item--file-details
      [:div.storage--file-uploader--file-item--filename (str filename)]
      [:div.storage--file-uploader--file-item--filesize (-> filesize io/B->MB format/decimals (str " MB"))]]
    [:div.storage--file-uploader--file-item--file-actions
      [elements/button {:preset :default-icon-button :icon :close}]]])

(defn- file-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id {:keys [file-list] :as body-props}]
  (let [file-selector (dom/get-element-by-id "storage-file-selector")]
       (letfn [(f [file-list file-dex file-props]
                  (let [file-object-url (dom/file-selector->file-object-url file-selector file-dex)
                        file-props      (assoc file-props :file-object-url file-object-url)]
                       (conj file-list ^{:key (str body-id file-dex)}
                                        [file-item body-id body-props file-dex file-props])))]
              (reduce-kv f [:<>] file-list))))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [body-id body-props]
  [:<> [file-list                body-id body-props]
       [no-files-to-upload-label body-id body-props]])



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
                      {:body {:content #'body :subscriber [:storage/get-file-uploader-body-props uploader-id]}}]))

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
            :storage/open-file-selector! [uploader-id uploader-props]})))
