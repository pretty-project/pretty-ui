







; "/media/storage/:filename"
; helyett
; "/media/storage/:filename/:file-alias"
;
; "/media/storage/b3eaeb03-380e-4065-a5b7-44453a52de2b?original-filename.ext"
; helyett
; "/media/storage/b3eaeb03-380e-4065-a5b7-44453a52de2b/original-filename.ext"






(ns app-extensions.media.file-browser
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :as map :refer [dissoc-in]]
              [mid-fruits.time      :as time]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]
              [x.app-media.api      :as media]
              [x.app-sync.api       :as sync]
              [app-extensions.media.context-menu      :as context-menu]
              [app-extensions.media.directory-actions :as directory-actions]
              [app-extensions.media.engine            :as engine]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A fájl-tallózó a [:file-browser/load!] esemény meghívásával indítható.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
;  :by-datem :by-name, :by-size
(def DEFAULT-ORDER-BY :by-name)

; @constant (boolean)
;
; @example
;  (def ATTACH-FILE-ALIAS? false)
;  => "/media/storage/a725d5b9-a864-421b-88d6-4efafe12ba12.png"
;
; @example
;  (def ATTACH-FILE-ALIAS? true)
;  => "/media/storage/a725d5b9-a864-421b-88d6-4efafe12ba12.png?file-alias=my-file.png"
(def ATTACH-FILE-ALIAS? true)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-props->done-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:browser-mode (keyword)}
  ;
  ; @return (keyword)
  [{:keys [browser-mode]}]
  (case browser-mode
        :select-file       :select
        :select-files      :select
        :add-files         :select
        :copy-to-directory :paste!
        :move-to-directory :paste!))

(defn- view-props->done-button-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:any-file-selected? (boolean)
  ;   :browser-mode (keyword)
  ;   :synchronizing? (boolean)}
  ;
  ; @return (boolean)
  [{:keys [any-file-selected? browser-mode synchronizing?]}]
  (and (not synchronizing?)
       (or (and (or (= browser-mode :select-file)
                    (= browser-mode :select-files)
                    (= browser-mode :add-files))
                (boolean any-file-selected?))
           (= browser-mode :copy-to-directory)
           (= browser-mode :move-to-directory))))

(defn- view-props->done-button-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;
  ; @return (boolean)
  [view-props]
  (not (view-props->done-button-enabled? view-props)))

(defn- view-props->directory-actions-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:browser-mode (keyword)}
  ;
  ; @return (boolean)
  [{:keys [browser-mode]}]
  (or (= browser-mode :select-file)
      (= browser-mode :select-files)
      (= browser-mode :add-files)))

(defn- view-props->file-selectable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:browser-mode (keyword)}
  ;
  ; @return (boolean)
  [{:keys [browser-mode]}]
  (or (= browser-mode :select-file)
      (= browser-mode :select-files)
      (= browser-mode :add-files)))

(defn- view-props->multiple-file-selection?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:browser-mode (keyword)}
  ;
  ; @return (boolean)
  [{:keys [browser-mode]}]
  (or (= browser-mode :select-files)
      (= browser-mode :add-files)))

(defn- view-props->subdirectory-context-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:browser-mode (keyword)}
  ;
  ; @return (component)
  [{:keys [browser-mode]}]
  (cond (or (= browser-mode :select-file)
            (= browser-mode :select-files)
            (= browser-mode :add-files))
        #'context-menu/file-selector-subdirectory-context-menu
        (or (= browser-mode :copy-to-directory)
            (= browser-mode :move-to-directory))
        #'context-menu/directory-selector-subdirectory-context-menu))

(defn- view-props->file-context-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:browser-mode (keyword)}
  ;
  ; @return (component)
  [{:keys [browser-mode]}]
  (cond (or (= browser-mode :select-file)
            (= browser-mode :select-files)
            (= browser-mode :add-files))
        #'context-menu/file-selector-file-context-menu
        (or (= browser-mode :copy-to-directory)
            (= browser-mode :move-to-directory))
        #'context-menu/directory-selector-file-context-menu))

(defn settings-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of keywords) xyz
  ;
  ; @return (meta-item-path vector)
  [& xyz]
  (vector/concat-items [::primary :meta-items] xyz))

(defn- subdirectory-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:disabled-directories (keywords in vector)(opt)}
  ; @param (keyword) subdirectory-id
  ; @param (map) subdirectory-props
  ;
  ; @return (metamorphic-event)
  [_ {:keys [disabled-directories]} subdirectory-id _]
  (vector/contains-item? disabled-directories subdirectory-id))

(defn- file-browser-subdirectory-stickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) subdirectory-id
  ; @param (map) subdirectory-props
  ;
  ; @return (maps in vector)
  [component-id view-props subdirectory-id subdirectory-props]
  (if-not (subdirectory-disabled? component-id view-props subdirectory-id subdirectory-props)
          (let [element-id (engine/item-id->element-id subdirectory-id :file-browser)]
               [{:icon     :more_vert
                 :on-click [:elements/render-context-surface! element-id]
                 :tooltip  :more-options}])))

(defn- file-browser-file-stickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;
  ; @return (maps in vector)
  [_ _ file-id _]
  (let [element-id (engine/item-id->element-id file-id :file-browser)]
       [{:icon     :more_vert
         :on-click [:elements/render-context-surface! element-id]
         :tooltip  :more-options}]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-rendered-directory-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [namespace (a/get-namespace ::this)]
       (r engine/get-rendered-directory-id db namespace)))

(defn- get-order-by
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (get-in db (settings-item-path :order-by)
             (param DEFAULT-ORDER-BY)))

(defn- get-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (item-path vector)
  [db _]
  (get-in db (settings-item-path :value-path)))

(defn- multiple-file-selection-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [browser-mode (get-in db (settings-item-path :browse-mode))]
       (or (= browser-mode :select-files)
           (= browser-mode :add-files))))

(defn- get-selected-file
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keywords in vector)
  [db _]
  ; Egy fájlt kiválasztó módban használva és több fájlt kiválasztó módban
  ; használva ugyanott tárolja a kiválasztott fájl(oka)t.
  (get-in db (settings-item-path :selected-files)))

(defn- get-selected-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keywords in vector)
  [db _]
  (get-in db (settings-item-path :selected-files)))

(defn- get-selected-file-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (let [directory-id (r get-rendered-directory-id db)
        element-id   (r get-selected-file         db)
        file-id      (engine/element-id->item-id  element-id)
        filename     (r engine/get-file-prop      db directory-id file-id :file/filename)
        file-alias   (r engine/get-file-prop      db directory-id file-id :file/alias)
        file-uri     (media/filename->media-storage-uri filename)]
       (if (boolean ATTACH-FILE-ALIAS?)
           (str file-uri "?file-alias=" file-alias)
           (return file-uri))))

(defn- get-selected-file-uris
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (strings in vector)
  [db _]
  (let [directory-id (r get-rendered-directory-id db)
        element-ids  (r get-selected-files        db)]
       (reduce (fn [selected-file-uris element-id]
                   (let [file-id    (engine/element-id->item-id element-id)
                         filename   (r engine/get-file-prop     db directory-id file-id :file/filename)
                         file-alias (r engine/get-file-prop     db directory-id file-id :file/alias)
                         file-uri   (media/filename->media-storage-uri filename)]
                        (if (boolean ATTACH-FILE-ALIAS?)
                            (vector/conj-item selected-file-uris (str   file-uri "?file-alias=" file-alias))
                            (vector/conj-item selected-file-uris (param file-uri)))))
               (param [])
               (param element-ids))))

(defn- any-file-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [selected-files (r get-selected-files db)]
       (or (keyword selected-files)
           (vector/nonempty? selected-files))))

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:any-file-selected? (boolean)
  ;   :directory-alias (string)
  ;   :directory-path (maps in vector)
  ;   :synchronizing? (boolean)}
  [db _]
  (let [namespace             (a/get-namespace ::this)
        query-id              (engine/namespace->query-id namespace)
        rendered-directory-id (r get-rendered-directory-id db)]
       (merge (get-in db (settings-item-path))
              {:any-file-selected? (r any-file-selected?         db)
               :directory-alias    (r engine/get-directory-alias db rendered-directory-id)
               :directory-path     (r engine/get-directory-path  db rendered-directory-id)
               :synchronizing?     (r sync/listening-to-request? db query-id)})))

(a/reg-sub :file-browser/get-header-props get-header-props)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:directory-render-files? (boolean)
  ;   :directory-render-subdirectories? (boolean)
  ;   :directory-empty? (boolean)
  ;   :filtered-files (map)
  ;   :filtered-subdirectories (map)
  ;   :filter-phrase (string)
  ;   :order-by (keyword)}
  [db _]
  (let [rendered-directory-id   (r get-rendered-directory-id          db)
        filter-phrase           (r elements/get-field-value           db ::filter-items-field)
        filtered-files          (r engine/get-filtered-files          db rendered-directory-id filter-phrase)
        filtered-subdirectories (r engine/get-filtered-subdirectories db rendered-directory-id filter-phrase)]
       (merge (get-in db (settings-item-path))
              {:directory-render-files?          (map/nonempty? filtered-files)
               :directory-render-subdirectories? (map/nonempty? filtered-subdirectories)
               :filtered-files                   (param filtered-files)
               :filtered-subdirectories          (param filtered-subdirectories)
               :directory-empty?                 (r engine/directory-empty?  db rendered-directory-id)
               :filter-phrase                    (r elements/get-field-value db ::filter-items-field)
               :order-by                         (r get-order-by             db)})))

(a/reg-sub :file-browser/get-view-props get-view-props)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- browser-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) browser-props
  ;
  ; @return (map)
  ;  {:browser-mode (keyword)
  ;   :directory-id (keyword)}
  [browser-props]
  (merge {:browser-mode :select-file
          :directory-id engine/ROOT-DIRECTORY-ID}
         (param browser-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- discard-selected-files!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db (settings-item-path :selected-files)))

(defn- set-current-directory-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @return (map)
  [db [_ directory-id]]
  (assoc-in db (settings-item-path :current-directory-id)
               (param directory-id)))

(defn- handle-directory-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (let [directory-id (r get-rendered-directory-id db)]
       (if-let [value-path (r get-value-path db)]
               (-> db (assoc-in value-path directory-id)
                      (assoc-in (settings-item-path :selected-directory)
                                (param directory-id)))
               (assoc-in db (settings-item-path :selected-directory)
                            (param directory-id)))))

(defn- handle-file-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (if-let [value-path (r get-value-path db)]
          (let [selected-file-uri (r get-selected-file-uri db)]
               (assoc-in db value-path selected-file-uri))
          (return db)))

(defn- handle-multiple-file-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (if-let [value-path (r get-value-path db)]
          (let [selected-file-uris (r get-selected-file-uris db)]
               (assoc-in db value-path selected-file-uris))
          (return db)))

(defn- handle-multiple-file-add!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (if-let [value-path (r get-value-path db)]
          (let [selected-file-uris (r get-selected-file-uris db)]
               (-> db (update-in value-path vector/concat-items selected-file-uris)
                      (update-in value-path vector/remove-duplicates)))
          (return db)))

(defn- handle-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (let [browser-mode (get-in db (settings-item-path :browser-mode))]
       (case browser-mode
             :select-file       (r handle-file-selection!          db)
             :select-files      (r handle-multiple-file-selection! db)
             :add-files         (r handle-multiple-file-add!       db)
             :copy-to-directory (r handle-directory-selection!     db)
             :move-to-directory (r handle-directory-selection!     db))))

(a/reg-event-db :file-browser/handle-selection! handle-selection!)



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-browser/->directory-data-downloaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  (fn [{:keys [db]} [_ directory-id]]
      (let [namespace (a/get-namespace ::this)]
           {:db (r engine/set-rendered-directory-id! db namespace directory-id)})))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-browser/render-order-by-select!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:elements/render-select-options! ::order-by-select-options
                                    {:default-value DEFAULT-ORDER-BY
                                     :options-label :order-by
                                     :options [{:label :by-date :value :by-date}
                                               {:label :by-name :value :by-name}
                                               {:label :by-size :value :by-size}]
                                     :value-path (settings-item-path :order-by)}])

(a/reg-event-fx
  :file-browser/download-directory-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  (fn [_ [_ directory-id]]
      (let [namespace        (a/get-namespace ::this)
            on-success-event [:file-browser/->directory-data-downloaded directory-id]]
           [:media/download-directory-data! (engine/namespace->query-id namespace)
                                            {:directory-id directory-id
                                             :on-success   on-success-event}])))

(a/reg-event-fx
  :file-browser/go-home!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db         (r discard-selected-files! db)
       :dispatch-n [[:elements/empty-field! ::filter-items-field]
                    [:file-browser/download-directory-data! engine/ROOT-DIRECTORY-ID]]}))

(a/reg-event-fx
  :file-browser/go-up!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [namespace           (a/get-namespace ::this)
            parent-directory-id (r engine/get-parent-directory-id db namespace)]
           {:db         (r discard-selected-files! db)
            :dispatch-n [[:elements/empty-field! ::filter-items-field]
                         [:file-browser/download-directory-data! parent-directory-id]]})))

(a/reg-event-fx
  :file-browser/go-to!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  (fn [{:keys [db]} [_ directory-id]]
      {:db         (r discard-selected-files! db)
       :dispatch-n [[:elements/empty-field! ::filter-items-field]
                    [:file-browser/download-directory-data! directory-id]]}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-browser/->browse-done
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [on-done-event (get-in db (settings-item-path :on-done))]
           {:db (r handle-selection! db)
            :dispatch on-done-event})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-browser-done-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:any-file-selected? (boolean)}
  ;
  ; @return (component)
  [popup-id view-props]
  [elements/button ::done-button
                   (merge {:keypress {:key-code 13}
                           :on-click {:dispatch-n [[:ui/close-popup! popup-id]
                                                   [:file-browser/->browse-done]]}
                           :preset   :primary-button
                           :label    (view-props->done-button-label view-props)}
                          (if (view-props->done-button-disabled? view-props)
                              {:color     :muted
                               :disabled? true}))])

(defn- file-browser-cancel-button
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

(defn- file-browser-home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ view-props]
  [elements/button ::home-button
                   {:color     :default
                    :disabled? (engine/view-props->root-level? view-props)
                    :icon      :home
                    :layout    :icon-button
                    :on-click  [:file-browser/go-home!]
                    :tooltip   :my-storage
                    :variant   :transparent
                    :width     :fit}])

(defn- file-browser-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ view-props]
  [elements/button ::up-button
                   {:disabled? (engine/view-props->root-level? view-props)
                    :icon      :chevron_left
                    :on-click  [:file-browser/go-up!]
                    :layout    :icon-button
                    :tooltip   :back!
                    :variant   :transparent
                    :width     :fit}])

(defn file-browser-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:directory-alias (string)}
  ;
  ; @return (component)
  [_ {:keys [directory-alias]}]
  [elements/text ::label
                 {:content directory-alias :horizontal-align :left :font-weight :bold}])

(defn file-browser-order-by-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [directory-empty?]}]
  [elements/button ::order-by-select-button
                   {:color     :default
                    :disabled? directory-empty?
                    :icon      :sort
                    :label     :order-by
                    :layout    :icon-button
                    :variant   :transparent
                    :on-click  [:file-browser/render-order-by-select!]}])

(defn- file-browser-directory-action-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [popup-id view-props]
  (if (view-props->directory-actions-enabled? view-props)
      (let [namespace (a/get-namespace ::this)]
           [directory-actions/multi-action-button popup-id {:namespace namespace}])))

(defn- file-browser-filter-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)
  ;   :synchronizing? (boolean)}
  ;
  ; @return (component)
  [popup-id {:keys [directory-empty? synchronizing?]}]
  (let [field-disabled? (or directory-empty? synchronizing?)]
       [elements/text-field ::filter-items-field
                            {:auto-focus? true
                             :disabled?   field-disabled?
                             :emptiable?  true
                             :min-width   :l
                             :placeholder :filter-items!}]))

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [popup-id view-props]
  [:<> [elements/polarity {:start-content [file-browser-cancel-button popup-id view-props]
                           :end-content   [file-browser-done-button   popup-id view-props]}]
       [elements/horizontal-line {:color :highlight}]
       [elements/polarity {:start-content [:<> [file-browser-home-button popup-id view-props]
                                               [file-browser-up-button   popup-id view-props]
                                               [file-browser-label       popup-id view-props]]
                           :end-content   [:<> [file-browser-directory-action-select popup-id view-props]
                                               [file-browser-order-by-select         popup-id view-props]]}]
       [elements/polarity {:middle-content [file-browser-filter-items-field popup-id view-props]}]])

(defn- file-browser-subdirectory
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) subdirectory-id
  ; @param (map) subdirectory-props
  ;  {:directory/alias (metamorphic-value)
  ;   :directory/content-size (B)
  ;   :directory/item-count (integer)}
  ;
  ; @return (component)
  [component-id view-props subdirectory-id {:directory/keys [alias content-size item-count]
                                            :as subdirectory-props}]
  (let [element-id           (engine/item-id->element-id subdirectory-id :file-browser)
        stickers             (file-browser-subdirectory-stickers component-id view-props subdirectory-id subdirectory-props)
        disabled?            (subdirectory-disabled?             component-id view-props subdirectory-id subdirectory-props)
        on-click-event       [:file-browser/go-to!              subdirectory-id]
        on-right-click-event [:elements/render-context-surface! element-id]]
       [elements/directory element-id
                           {:content-size    content-size
                            :disabled?       disabled?
                            :item-count      item-count
                            :label           alias
                            :on-click        on-click-event
                            :on-right-click  on-right-click-event
                            :stickers        stickers
                            :context-surface
                            {:content       (view-props->subdirectory-context-menu view-props)
                             :content-props subdirectory-props}}]))

(defn- file-browser-subdirectory-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:filtered-subdirectories (map)}
  ;
  ; @return (component)
  [component-id {:keys [filtered-subdirectories] :as view-props}]
  (reduce (fn [directory-list subdirectory-id]
              (let [subdirectory-props (get filtered-subdirectories subdirectory-id)]
                   (vector/conj-item directory-list ^{:key subdirectory-id}
                                     [file-browser-subdirectory component-id    view-props
                                                                subdirectory-id subdirectory-props])))
          [:<>]
          (engine/view-props->ordered-subdirectories view-props)))

(defn- file-browser-subdirectories
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-render-files? (boolean)
  ;   :directory-render-subdirectories? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-render-files? directory-render-subdirectories?] :as view-props}]
  (if (boolean directory-render-subdirectories?)
      [:<> [elements/row ::subdirectories
                         {:content [file-browser-subdirectory-list component-id view-props]
                          :wrap-items? true}]
           (if (boolean directory-render-files?)
               [:<> [elements/separator {:orientation :horizontal :size :s}]
                    [elements/horizontal-line {:color :highlight}]
                    [elements/separator {:orientation :horizontal :size :s}]])]))

(defn- file-browser-file
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;  {:file/alias (metamorphic-value)
  ;   :file/filesize (string)}
  ;
  ; @return (component)
  [component-id {:keys [] :as view-props}
   file-id      {:file/keys [alias filesize modified-at selected?] :as file-props}]
  (let [element-id           (engine/item-id->element-id file-id :file-browser)
        stickers             (file-browser-file-stickers component-id view-props file-id file-props)
        timestamp            (time/timestamp-string->date-and-time modified-at :yyyymmdd :hhmm)
        on-right-click-event [:elements/render-context-surface! element-id]
        thumbnail-uri        (engine/file-props->thumbnail-uri file-props)]
       [elements/file element-id
                      {:filesize       filesize
                       :label          alias
                       :multiple-selection? (view-props->multiple-file-selection? view-props)
                       :on-right-click on-right-click-event
                       :selectable?    (view-props->file-selectable? view-props)
                       :stickers       stickers
                       :value-path     (settings-item-path :selected-files)
                       :thumbnail-uri  thumbnail-uri
                       :timestamp      timestamp
                       :context-surface
                       {:content       (view-props->file-context-menu view-props)
                        :content-props file-props}}]))

(defn- file-browser-file-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:filtered-files (map)}
  ;
  ; @return (component)
  [component-id {:keys [filtered-files] :as view-props}]
  (reduce (fn [file-list file-id]
              (let [file-props (get filtered-files file-id)]
                   (vector/conj-item file-list ^{:key file-id}
                                     [file-browser-file component-id view-props file-id file-props])))
          [:<>]
          (engine/view-props->ordered-files view-props)))

(defn- file-browser-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-render-files? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-render-files?] :as view-props}]
  (if (boolean directory-render-files?)
      [elements/row ::files
                    {:content [file-browser-file-list component-id view-props]
                     :wrap-items? true}]))

(defn- file-browser-file-drop-area
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  (if (view-props->directory-actions-enabled? view-props)
     ;[elements/file-drop-area ::file-drop-area
     ;                         {:font-size :xs}]
      [elements/label {:content :empty-directory :color :highlight}]
      [elements/label {:content :empty-directory :color :highlight}]))

(defn- file-browser-directory-empty
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-empty?] :as view-props}]
  (if (boolean directory-empty?)
      [elements/polarity ::directory-empty
                         {:middle-content [file-browser-file-drop-area component-id view-props]}]))

(defn- file-browser-no-filtered-items-match
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)
  ;   :directory-render-files? (boolean)
  ;   :directory-render-subdirectories? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [directory-empty? directory-render-files? directory-render-subdirectories?]}]
  (if (and (not directory-render-files?)
           (not directory-render-subdirectories?)
           (not directory-empty?))
      [elements/polarity ::no-filtered-items-match
                         {:middle-content [elements/label {:content :no-items-found :color :highlight}]}]))

(defn- file-browser
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [:<> [elements/separator {:orientation :horizontal :size :s}]
       [file-browser-subdirectories          component-id view-props]
       [file-browser-files                   component-id view-props]
       [file-browser-directory-empty         component-id view-props]
       [file-browser-no-filtered-items-match component-id view-props]
       [elements/separator {:orientation :horizontal :size :s}]])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (component)
  [component-id]
  [elements/column {:stretch-orientation :horizontal
                    :content          #'file-browser
                    :horizontal-align :left
                    :subscriber       [:file-browser/get-view-props]}])

(defn- listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (component)
  [_]
  (let [namespace (a/get-namespace ::this)
        query-id  (engine/namespace->query-id namespace)]
       [components/listener {:content         #'view
                             :pending-content :use-default!
                             :request-id      query-id}]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-browser/initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  ;  {:directory-id (keyword)}
  (fn [{:keys [db]} [_ _ {:keys [directory-id] :as browser-props}]]
      (let [latest-directory-id (r get-rendered-directory-id db)
            directory-id        (or latest-directory-id directory-id)]
           {:db (r db/set-item! db (settings-item-path)
                                   (param browser-props))
            :dispatch [:file-browser/download-directory-data! directory-id]})))

(a/reg-event-fx
  :file-browser/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) browser-props
  (fn [_ [_ browser-id browser-props]]
      [:ui/add-popup! ::view
                      {:body   {:content #'listener}  ; #'body és #'header
                       :header {:content #'header :subscriber [:file-browser/get-header-props]}
                       :horizontal-align :center}]))

(a/reg-event-fx
  :file-browser/load!
  ; @param (keyword)(opt) browser-id
  ; @param (map) browser-props
  ;  {:allowed-extensions (strings in vector)(opt)
  ;   :directory-id (keyword)(opt)
  ;    Default: engine/ROOT-DIRECTORY-ID
  ;   :browser-mode (keyword)(opt)
  ;    :copy-to-directory, :move-to-directory, :select-file, :select-files, :add-files
  ;    Default: select-file
  ;    A {:browser-mode :add-files} módban használva a kiválasztás nem felülírja,
  ;    hanem hozzáadja az eddig kiválasztott fájlokhoz az újonnan kiválasztott fájlokat.
  ;   :disabled-directories (keywords in vector)(opt)
  ;   :on-done (metamorphic-event)(opt)
  ;   :value-path (item-path vector)(opt)}
  ;
  ; @usage
  ;  [:file-browser/load! {...}]
  ;
  ; @usage
  ;  [:file-browser/load! :my-browser {...}]
  ;
  ; @usage
  ;  [:file-browser/load! {:allowed-extension ["htm" "html" "xml"]
  ;                        :browser-mode :select-files}]
  ;
  ; @usage
  ;  [:file-browser/load! {:browser-mode :move-to-directory
  ;                        :disabled-directories [:my-directory :your-directory]}]
  (fn [_ event-vector]
      (let [browser-id    (a/event-vector->second-id   event-vector)
            browser-props (a/event-vector->first-props event-vector)
            browser-props (a/prot browser-props browser-props-prototype)]
           {:dispatch-n [[:file-browser/initialize! browser-id browser-props]
                         [:file-browser/render!     browser-id browser-props]]})))
