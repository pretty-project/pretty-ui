
(ns app-extensions.media.file-uploader
    (:require [app-fruits.dom       :as dom]
              [mid-fruits.candy     :refer [param return]]
              [mid-fruits.eql       :as eql]
              [mid-fruits.io        :as io]
              [mid-fruits.loop      :refer [reduce-indexed]]
              [mid-fruits.math      :as math]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.random    :as random]
              [mid-fruits.string    :as string]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-dictionary.api :as dictionary]
              [x.app-environment.api :as environment]
              [x.app-elements.api   :as elements]
              [x.app-media.api      :as media]
              [x.app-sync.api       :as sync]
              [x.app-ui.api         :as ui]
              [app-extensions.media.context-menu   :as context-menu]
              [app-extensions.media.engine         :as engine]
              [app-extensions.media.popup-geometry :as popup-geometry]))
              ;[x.app-tools.temporary-component
              ; :refer [append-temporary-component! remove-temporary-component!]]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A rendszert futtató szerver is korlátozza a feltöltés méretét.
;
; @description
;  A fájlfeltöltő a [:file-uploader/load! ...] esemény meghívásával indítható.



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

(defn- files-data->non-aborted-files
  ; @param (maps in vector)
  ;  [{:aborted? (boolean)}]
  ;
  ; @example
  ;  (files-data->non-aborted-files [{:filename "My file"}
  ;                                  {:filename "Your file" :aborted? true}
  ;                                  {:filename "Our file"}])
  ;  => [0 2]
  ;
  ; @return (integers in vector)
  [files-data]
  (reduce-indexed (fn [o dex {:keys [aborted?]}]
                      (if aborted? (return o)
                                   (conj   o dex)))
                  [] files-data))

(defn- view-props->disable-upload-button?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:max-upload-size-reached? (boolean)
  ;   :storage-capacity-limit-exceeded? (boolean)}
  ;
  ; @return (boolean)
  [{:keys [all-files-aborted? max-upload-size-reached? storage-capacity-limit-exceeded?]}]
  (boolean (or all-files-aborted? max-upload-size-reached? storage-capacity-limit-exceeded?)))

(defn settings-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of keywords) xyz
  ;
  ; @return (meta-item-path vector)
  [& xyz]
  (vector/concat-items [::primary :meta-items] xyz))

(defn- open-file-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (?)
  []
  (let [file-selector (dom/get-element-by-id "x-file-selector")]
       (.click file-selector)))

(defn- file-stickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ; @param (map) file-props
  ; @param (integer) file-dex
  ;
  ; @return (component)
  [_ _ _ file-dex]
  (let [element-id (engine/file-dex->element-id file-dex :file-uploader)]
       [{:icon     :close
         :on-click [:file-uploader/abort-file! file-dex]
         :tooltip  :remove-file!}]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-non-aborted-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (integers in vector)
  [db _]
  (let [files-data (get-in db (settings-item-path :files-data))]
       (files-data->non-aborted-files files-data)))

(defn- all-files-aborted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [non-aborted-files (r get-non-aborted-files db)]
       (empty? non-aborted-files)))

(defn- max-upload-size-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _])
  ;(let [max-upload-size (r a/get-storage-detail db :max-upload-size)
  ;      upload-size     (get-in db (settings-item-path :files-size))
  ;     (>= upload-size max-upload-size)])

(defn- storage-capacity-limit-exceeded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _])
  ;(let [storage-free-capacity (r engine/get-storage-free-capacity db)
  ;      upload-size           (get-in db (settings-item-path :files-size))
  ;     (>= upload-size storage-free-capacity)])

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (map)
  [db _]
  (merge (get-in db (settings-item-path))
         {:all-files-aborted?               (r all-files-aborted?               db)
          :max-upload-size-reached?         (r max-upload-size-reached?         db)
          :storage-capacity-limit-exceeded? (r storage-capacity-limit-exceeded? db)
          ;:max-upload-size                  (r a/get-storage-detail db :max-upload-size)
          :storage-free-capacity            (r engine/get-storage-free-capacity db)}))

(a/reg-sub :file-uploader/get-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ;
  ; @return (map)
  [db _]
  (merge (get-in db (settings-item-path))
         {:all-files-aborted? (r all-files-aborted?             db)
          :viewport-width     (r environment/get-viewport-width db)}))

(a/reg-sub :file-uploader/get-body-props get-body-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-uploader-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) uploader-props
  ;
  ; @return (map)
  [db [_ _ uploader-props]]
  (assoc-in db (settings-item-path)
               (param uploader-props)))

(defn- abort-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (integer) file-dex
  ;
  ; @return (map)
  [db [_ file-dex]]
  (let [filesize (get-in db (settings-item-path :files-data file-dex :filesize))]
       (-> db (assoc-in  (settings-item-path :files-data file-dex :aborted?) true)
              (update-in (settings-item-path :files-size) - filesize)
              (update-in (settings-item-path :file-count) dec))))

(a/reg-event-db :file-uploader/abort-file! abort-file!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-uploader/upload-files!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [; *
            namespace                (get-in db (settings-item-path :namespace))
            destination-directory-id (get-in db (settings-item-path :destination-directory-id))
            ; EQL query
            directory-entity         (db/item-id->document-entity destination-directory-id :directory)
            mutation-props           {:destination-directory-id (name destination-directory-id)}
            query-action            `(media/upload-files! ~mutation-props)
            query-question           {directory-entity engine/DOWNLOAD-DIRECTORY-DATA-PARAMS}
            query                    (eql/append-to-query engine/ROOT-DIRECTORY-QUERY query-action query-question)
            ; Form data
            file-selector            (dom/get-element-by-id "x-file-selector")
            non-aborted-files        (r get-non-aborted-files db)
            form-data                (dom/file-selector->form-data file-selector non-aborted-files)
            ; Request details
            action-id                (engine/namespace->query-id namespace)
            body                     (dom/merge-to-form-data!    form-data {:query query})]
           [:sync/send-query! action-id
                              {:body         (param body)
                               :idle-timeout (param 1000)
                               :on-failure   [:file-uploader/->upload-failure]
                               :on-success   {:dispatch-n [[:media/handle-request-response! action-id]
                                                           [:file-uploader/->files-uploaded]]}}])))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-uploader-progress-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) context-props
  ;  {:keyword (namespace)}
  ;
  ; @return (component)
  [bubble-id {:keys [namespace]}]
  (let [query-id (engine/namespace->query-id namespace)]))
;       [elements/request-indicator {:diameter   36
;                                    :layout     :row
;                                    :request-id query-id
;                                    :shape      :circle)]))

(defn- file-uploader-pending-bubble
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [component-id context-props]
  [elements/row {:content [:<> [file-uploader-progress-indicator component-id context-props]
                               [elements/vertical-separator {:size :s}]
                               [elements/label {:content :file-uploading-in-progress}]]}])

(defn- file-uploader-progress-bubble
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) context-props
  ;
  ; @return (component)
  [bubble-id {:keys [namespace] :as context-props}]
  (let [query-id (engine/namespace->query-id namespace)]
       [components/listener {:request-id      query-id
                             :pending-content #'file-uploader-pending-bubble
                             :success-content :files-uploaded
                             :failure-content :file-upload-failure
                             :content-props   context-props}]))

(defn- file-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) uploader-props
  ;  {:allowed-extensions (strings in vector)(opt)}
  ;
  ; @return (hiccup)
  [uploader-id uploader-props]
  (let [allowed-extensions-list (uploader-props->allowed-extensions-list uploader-props)]
       [:input#x-file-selector {:accept     (param allowed-extensions-list)
                                :multiple   (param 1)
                                :on-change #(a/dispatch [:file-uploader/initialize! uploader-id])
                                :type       (param "file")}]))

(defn- file-uploader-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [popup-id _]
  [elements/button ::cancel-button
                   {:keypress {:key-code 27}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :cancel-button}])

(defn- file-uploader-upload-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [popup-id view-props]
  [elements/button ::upload-button
                   {:disabled? (view-props->disable-upload-button? view-props)
                    :keypress  {:key-code 13}
                    :label     :upload!
                    :on-click  {:dispatch-n [[:ui/close-popup! popup-id]
                                             [:ui/blow-bubble! ::progress-bubble
                                                               {:autopop?      false
                                                                :color         :muted
                                                                :content       #'file-uploader-progress-bubble
                                                                :content-props view-props
                                                                :user-close?   false}]
                                             [:file-uploader/upload-files!]]}
                    :preset    :primary-button}])

(defn- file-uploader-available-capacity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:storage-capacity-limit-exceeded? (boolean)
  ;   :storage-free-capacity (B)}
  ;
  ; @return (component)
  [_ {:keys [storage-capacity-limit-exceeded? storage-free-capacity]}]
  [elements/text {:content      :available-capacity-in-storage-is
                  :replacements [(math/round (io/B->MB storage-free-capacity))]
                  :color        (if storage-capacity-limit-exceeded? :warning :muted)
                  :font-size    :xs
                  :font-weight  :bold
                  :layout       :fit}])

(defn- file-uploader-uploading-size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:files-size (B)
  ;   :max-upload-size-reached? (boolean)
  ;   :max-upload-size (B)}
  ;
  ; @return (component)
  [_ {:keys [files-size max-upload-size-reached? max-upload-size]}]
  [elements/text {:content      :uploading-size-is
                  :replacements [(io/B->MB files-size)
                                 (math/round (io/B->MB max-upload-size))]
                  :color        (if max-upload-size-reached? :warning :muted)
                  :font-size    :xs
                  :font-weight  :bold
                  :layout       :fit}])

(defn- file-uploader-summary
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [popup-id view-props]
  [elements/column {:content [:<> [file-uploader-available-capacity popup-id view-props]
                                  [elements/horizontal-separator {:size :s}]
                                  [file-uploader-uploading-size popup-id view-props]]
                    :horizontal-align :center}])

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [popup-id view-props]
  [:<> [elements/horizontal-polarity {:start-content [file-uploader-cancel-button popup-id view-props]
                                      :end-content   [file-uploader-upload-button popup-id view-props]}]
       [file-uploader-summary popup-id view-props]
       [elements/horizontal-separator {:size :s}]])

(defn- file-uploader-file
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ; @param (map) file-props
  ; @param (integer) file-dex
  ;
  ; @return (component)
  [popup-id view-props {:keys [filename filesize file-object-url] :as file-props} file-dex]
  (let [element-id           (engine/file-dex->element-id file-dex :file-uploader)
        on-right-click-event [:elements/render-context-surface! element-id]]
       [elements/file element-id
                      {:label          filename
                       :filesize       filesize
                       :on-right-click on-right-click-event
                       :stickers       (file-stickers popup-id view-props file-props file-dex)
                       :thumbnail-uri  (if (io/filename->image? filename)
                                           (param file-object-url))
                       :width          popup-geometry/FILE-PREVIEW-CARD-WIDTH
                       :context-surface {:content       #'context-menu/file-uploader-file-context-menu
                                         :content-props file-props}}]))

(defn- file-uploader-file-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:file-count (integer)
  ;   :files-data (maps in vector)}
  ;
  ; @return (component)
  [popup-id {:keys [file-count files-data] :as view-props}]
  (let [file-selector (dom/get-element-by-id "x-file-selector")]
       (reduce-indexed (fn [file-list file-dex {:keys [aborted?] :as file-props}]
                           (if (boolean aborted?)
                               (return file-list)
                               (let [];file-object-url (dom/file-selector->file-object-url file-selector file-dex)
                                     ;file-props      (assoc file-props :file-object-url file-object-url)]
                                    (conj file-list ^{:key file-dex}
                                          [file-uploader-file popup-id view-props file-props file-dex]))))
                       [:<>]
                       (param files-data))))

(defn- file-uploader-no-files-to-upload
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:all-files-aborted? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [all-files-aborted?]}]
  (if (boolean all-files-aborted?)
      [elements/text {:content     :no-files-selected
                      :color       :muted
                      :font-weight :bold}]))

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) body-props
  ;
  ; @return (hiccup)
  [popup-id body-props]
  [:<> [elements/horizontal-separator {:size :s}]
       [elements/row {:content [file-uploader-file-list popup-id body-props]
                      :style   (popup-geometry/view-props->item-list-container-style body-props)
                      :wrap-items? true}]
       [file-uploader-no-files-to-upload popup-id body-props]
       [elements/horizontal-separator {:size :s}]])



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-uploader/->upload-failure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r ui/set-element-prop! db :bubbles ::progress-bubble :color :warning)
       :dispatch [:ui/pop-bubble! ::progress-bubble {:timeout 5000}]}))

(a/reg-event-fx
  :file-uploader/->files-uploaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r ui/set-element-prop! db :bubbles ::progress-bubble :color :success)
       :dispatch [:ui/pop-bubble! ::progress-bubble {:timeout 5000}]}))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-uploader/initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A file-selector input fájltallózójának on-change eseménye indítja el
  ; a feltöltés inicializálását.
  ;
  ; @param (keyword) uploader-id
  (fn [{:keys [db]} [event-id uploader-id]]
      (let [file-selector      (dom/get-element-by-id "x-file-selector")
            any-file-selected? (dom/file-selector->any-file-selected? file-selector)])))
            ;file-selector-data (dom/file-selector->file-selector-data file-selector)]
            ; 1. Eltárolja a kiválasztott fájlok számát, méretét és egyéb adatait.
           ;{:db (-> db (db/apply! [event-id (settings-item-path)
            ;                       merge file-selector-data)])))
            ; 2. Ha van kiválasztva fájl a fájltallózóval, akkor megnyitja
            ;    a fájlfeltöltő ablakot.
            ;:dispatch-if [any-file-selected? [:file-uploader/render-file-previews! uploader-id]]])))

(a/reg-event-fx
  :file-uploader/render-file-previews!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ [_ uploader-id]]
      [:ui/add-popup! ::view
                      {:autopadding? false
                       ; #'body és #'header
                       :body   {:content #'body   :subscriber [:file-uploader/get-body-props   uploader-id]}
                       :header {:content #'header :subscriber [:file-uploader/get-header-props uploader-id]}
                       :min-width    :xxs}]))


(a/reg-fx
  :file-uploader/open-file-selector!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) uploader-id
  ; @param (map) uploader-props
  (fn [[uploader-id uploader-props]]))
      ;(append-temporary-component! [file-selector uploader-id uploader-props]
      ;                             (param open-file-selector!)]]))

(a/reg-event-fx
  :file-uploader/load!
  ; A fájlfeltöltő a {:namespace ...} tulajdonságként átadott névtér szerint
  ; elnevezett request-et indít a fájlok feltöltésekor.
  ;
  ; @param (keyword)(opt) uploader-id
  ; @param (map) uploader-props
  ;  {:allowed-extensions (strings in vector)(opt)
  ;   :destination-directory-id (keyword)
  ;   :namespace (keyword)}
  ;
  ; @usage
  ;  [:file-uploader/load! {...}]
  ;
  ; @usage
  ;  [:file-uploader/load! :my-uploader {...}]
  ;
  ; @usage
  ;  [:file-uploader/load! {:allowed-extensions ["htm" "html" "xml"]
  ;                         :destination-directory-id :home
  ;                         :namespace :my-namespace}]
  (fn [{:keys [db]} event-vector]
      (let [uploader-id    (a/event-vector->second-id   event-vector)
            uploader-props (a/event-vector->first-props event-vector)]
           {:db (r store-uploader-props! db uploader-id uploader-props)
            :file-uploader/open-file-selector! [uploader-id uploader-props]})))
