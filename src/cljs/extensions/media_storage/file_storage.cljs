
(ns extensions.media-storage.file-storage
    (:require [mid-fruits.candy      :refer [param return]]
              [mid-fruits.io         :as io]
              [mid-fruits.map        :as map]
              [mid-fruits.time       :as time]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-dictionary.api  :as dictionary]
              [x.app-elements.api    :as elements]
              [x.app-media.api       :as media]
              [x.app-router.api      :as router]
              [x.app-sync.api        :as sync]
              [x.app-ui.api          :as ui]
              [extensions.media-storage.context-menu      :as context-menu]
              [extensions.media-storage.directory-actions :as directory-actions]
              [extensions.media-storage.engine            :as engine]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name current-route-directory-id
;  A current-directory-id azonosító a :directory-id route-path-param pillanatnyi
;  értéke, ami a route megváltozásakor még a route esemény megtörténése előtt megváltozik.
;
; @name rendered-directory-id
;
; @name loading-directory-id
;
; @name rendered-file
;
; @name rendered-subdirectory



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A fájl-intéző az [:x.app-router/go-to! "/file-storage"] vagy az
;  [:x.app-router/go-to! "/file-storage/directory-id"] események meghívásával
;  indítható.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
;  :by-date, :by-name, :by-size
(def DEFAULT-ORDER-BY :by-name)

; @constant (string)
(def FILE-STORAGE-HOME-URI "/admin/fajlkezelo")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn settings-item-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of keywords) xyz
  ;
  ; @return (meta-item-path vector)
  [& xyz]
  (vector/concat-items [::primary :meta-items] xyz))

(defn- file-storage-subdirectory-stickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) subdirectory-id
  ; @param (map) subdirectory-props
  ;
  ; @return (maps in vector)
  [_ _ subdirectory-id _]
  (let [element-id (engine/item-id->element-id subdirectory-id :file-storage)]
       [{:icon     :drive_file_rename_outline
         :on-click [:file-storage/edit-rendered-subdirectory-alias! subdirectory-id]
         :tooltip  :rename-directory!}
        {:icon     :delete_outline
         :on-click [:file-storage/render-delete-rendered-subdirectory-dialog! subdirectory-id]
         :tooltip  :delete-directory!}
        {:icon     :more_vert
         :on-click [:x.app-elements/render-context-surface! element-id]
         :tooltip  :more-options}]))

(defn- file-storage-file-stickers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) file-id
  ; @param (map) file-props
  ;
  ; @return (maps in vector)
  [_ _ file-id _]
  (let [element-id (engine/item-id->element-id file-id :file-storage)]
       [{:icon     :drive_file_rename_outline
         :on-click [:file-storage/edit-rendered-file-alias! file-id]
         :tooltip  :rename-file!}
        {:icon     :delete_outline
         :on-click [:file-storage/render-delete-rendered-file-dialog! file-id]
         :tooltip  :delete-file!}
        {:icon     :more_vert
         :on-click [:x.app-elements/render-context-surface! element-id]
         :tooltip  :more-options}]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- path-param->file-storage-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) path-param
  ;
  ; @return (string)
  [path-param]
  (str FILE-STORAGE-HOME-URI "/" path-param))

(defn directory-id->file-storage-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  ;
  ; @example
  ;  (directory-id->file-storage-uri :my-directory)
  ;  => "/file-storage/my-directory"
  ;
  ; @return (string)
  [directory-id]
  (let [directory-id (name directory-id)]
       (path-param->file-storage-uri directory-id)))

(defn- view-props->capacity-indicator-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:storage-total-capacity (B)
  ;   :storage-used-capacity (B)}
  ;
  ; @return (string)
  [{:keys [storage-total-capacity storage-used-capacity]}]
  (let [storage-total-capacity (io/B->GB storage-total-capacity)
        storage-used-capacity  (io/B->GB storage-used-capacity)]
       (str storage-used-capacity " GB / " storage-total-capacity " GB")))



;; -- Current route directory subscriptions -----------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-route-directory-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (if-let [current-directory-id (r router/get-current-route-path-param db :directory-id)]
          (keyword current-directory-id)
          (return  engine/ROOT-DIRECTORY-ID)))



;; -- Rendered directory subscriptions ----------------------------------------
;; ----------------------------------------------------------------------------

(defn get-rendered-directory-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (let [namespace (a/get-namespace ::this)]
       (r engine/get-rendered-directory-id db namespace)))

(defn get-rendered-file-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  ; @param (keyword) prop-id
  ;
  ; @return (string)
  [db [_ file-id prop-id]]
  (let [rendered-directory-id (r get-rendered-directory-id db)]
       (r engine/get-file-prop db rendered-directory-id file-id prop-id)))

(a/reg-sub :file-storage/get-rendered-file-prop get-rendered-file-prop)

(defn get-rendered-subdirectory-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  ; @param (keyword) prop-id
  ;
  ; @return (string)
  [db [_ subdirectory-id prop-id]]
  (let [rendered-directory-id (r get-rendered-directory-id db)]
       (r engine/get-subdirectory-prop db rendered-directory-id subdirectory-id prop-id)))

(a/reg-sub :file-storage/get-rendered-subdirectory-prop get-rendered-subdirectory-prop)



;; -- View subscriptions ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-surface-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db]
  (str (r a/get-app-detail   db :app-title)
       (param " ")
       (r dictionary/look-up db :file-storage)))

(defn- get-order-by
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (keyword)
  [db _]
  (get-in db (settings-item-path :order-by)
             (param DEFAULT-ORDER-BY)))

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:directory-exists? (boolean)
  ;   :directory-alias (string)
  ;   :directory-render-files? (boolean)
  ;   :directory-render-subdirectories? (boolean)
  ;   :directory-empty? (boolean)
  ;   :directory-path (maps in vector)
  ;   :filtered-files (maps in vector)
  ;   :filtered-subdirectories (maps in vector)
  ;   :listening-to-request? (boolean)
  ;   :order-by (keyword)
  ;   :storage-free-capacity (B)
  ;   :storage-total-capacity (B)
  ;   :storage-used-capacity (B)}
  [db _]
  (let [namespace               (a/get-namespace ::this)
        query-id                (engine/namespace->query-id namespace)
        rendered-directory-id   (r get-rendered-directory-id db)
        filter-phrase           (r elements/get-field-value           db ::filter-items-field)
        filtered-files          (r engine/get-filtered-files          db rendered-directory-id filter-phrase)
        filtered-subdirectories (r engine/get-filtered-subdirectories db rendered-directory-id filter-phrase)]
       {:directory-exists?                (r engine/directory-exists?          db rendered-directory-id)
        :directory-alias                  (r engine/get-directory-alias        db rendered-directory-id)
        :directory-empty?                 (r engine/directory-empty?           db rendered-directory-id)
        :directory-path                   (r engine/get-directory-path         db rendered-directory-id)
        :listening-to-request?            (r sync/listening-to-request?        db query-id)
        :order-by                         (r get-order-by                      db)
        :storage-free-capacity            (r engine/get-storage-free-capacity  db)
        :storage-total-capacity           (r engine/get-storage-total-capacity db)
        :storage-used-capacity            (r engine/get-storage-used-capacity  db)
        :directory-render-files?          (map/nonempty? filtered-files)
        :directory-render-subdirectories? (map/nonempty? filtered-subdirectories)
        :filtered-files                   (param filtered-files)
        :filtered-subdirectories          (param filtered-subdirectories)}))

(a/reg-sub :file-storage/get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-storage-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) storage-id
  ; @param (map) storage-props
  ;
  ; @return (map)
  [db [_ storage-id storage-props]]
  (update-in db (settings-item-path) merge storage-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-storage/go-home!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:x.app-elements/empty-field! ::filter-items-field]
                [:x.app-router/go-to!         FILE-STORAGE-HOME-URI]]})

(a/reg-event-fx
  :file-storage/go-up!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [namespace           (a/get-namespace ::this)
            parent-directory-id (r engine/get-parent-directory-id db namespace)
            file-storage-uri    (directory-id->file-storage-uri parent-directory-id)]
           {:dispatch-n [[:x.app-elements/empty-field! ::filter-items-field]
                         [:x.app-router/go-to!         file-storage-uri]]})))

(a/reg-event-fx
  :file-storage/go-to!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  (fn [_ [_ subdirectory-id]]
      (let [file-storage-uri (directory-id->file-storage-uri subdirectory-id)]
           {:dispatch-n [[:x.app-elements/empty-field! ::filter-items-field]
                         [:x.app-router/go-to!         file-storage-uri]]})))

(a/reg-event-fx
  :file-storage/render-order-by-select!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-elements/render-select-options!
    ::order-by-select-options
    {:default-value DEFAULT-ORDER-BY
     :options-label :order-by
     :options [{:label :by-date :value :by-date}
               {:label :by-name :value :by-name}
               {:label :by-size :value :by-size}]
     :value-path (settings-item-path :order-by)}])

(a/reg-event-fx
  :file-storage/set-window-title!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [rendered-directory-id (r get-rendered-directory-id  db)]
           (if (r engine/directory-exists? db rendered-directory-id)
               (let [rendered-directory-alias (r engine/get-directory-alias db rendered-directory-id)]
                    [:x.app-ui/set-title! rendered-directory-alias])))))

(a/reg-event-fx
  :file-storage/edit-rendered-file-alias!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  (fn [{:keys [db]} [_ file-id]]
      (let [namespace             (a/get-namespace ::this)
            rendered-directory-id (r get-rendered-directory-id db)
            file-alias            (r get-rendered-file-prop    db file-id :file/alias)
            action-id             (engine/namespace->query-id namespace)
            action-props          {:source-directory-id rendered-directory-id
                                   :file-id             file-id}]
           [:x.app-tools.editor/edit!
            :media-storage/alias-editor
            {:initial-value file-alias
             :label         :filename
             :on-save       [:media-storage/->file-alias-edited action-id action-props]
             :validator     {:f io/filename-valid?
                             :invalid-message :invalid-filename
                             :pre-validate? true}}])))

(a/reg-event-fx
  :file-storage/edit-rendered-subdirectory-alias!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  (fn [{:keys [db]} [_ subdirectory-id]]
      (let [namespace             (a/get-namespace ::this)
            rendered-directory-id (r get-rendered-directory-id      db)
            subdirectory-alias    (r get-rendered-subdirectory-prop db subdirectory-id :directory/alias)
            action-id             (engine/namespace->query-id namespace)
            action-props          {:source-directory-id rendered-directory-id
                                   :subdirectory-id     subdirectory-id}]
           [:x.app-tools.editor/edit!
            :media-storage/alias-editor
            {:initial-value subdirectory-alias
             :label         :directory-name
             :on-save       [:media-storage/->subdirectory-alias-edited action-id action-props]
             :validator     {:f io/directory-name-valid?
                             :invalid-message :invalid-directory-name
                             :pre-validate? true}}])))

(a/reg-event-fx
  :file-storage/delete-rendered-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) action-props
  ;  {:selected-item (map)
  ;    {:directory/id (string)
  ;     :file/id (string)}}
  (fn [{:keys [db]} [_ action-props]]
      (let [namespace             (a/get-namespace ::this)
            rendered-directory-id (r get-rendered-directory-id db)
            action-id             (engine/namespace->query-id namespace)
            action-props          (assoc action-props :source-directory-id rendered-directory-id)]
           [:media-storage/delete-item! action-id action-props])))

(a/reg-event-fx
  :file-storage/copy-rendered-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) action-props
  ;  {:selected-item (map)
  ;    {:directory/id (string)
  ;     :file/id (string)}}
  (fn [{:keys [db]} [_ action-props]]
      (let [namespace                (a/get-namespace ::this)
            destination-directory-id (get-in db (settings-item-path :destination-directory-id))
            action-id                (engine/namespace->query-id namespace)
            action-props             (assoc action-props :destination-directory-id destination-directory-id)]
           [:media-storage/copy-item! action-id action-props])))

(a/reg-event-fx
  :file-storage/move-rendered-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) action-props
  ;  {:selected-item (map)
  ;    {:directory/id (string)
  ;     :file/id (string)}}
  (fn [{:keys [db]} [_ action-props]]
      (let [namespace                (a/get-namespace ::this)
            destination-directory-id (get-in db (settings-item-path :destination-directory-id))
            source-directory-id      (r get-rendered-directory-id db)
            action-id                (engine/namespace->query-id namespace)
            action-props             (assoc action-props :destination-directory-id destination-directory-id
                                                         :source-directory-id      source-directory-id)]
           [:media-storage/move-item! action-id action-props])))

(a/reg-event-fx
  :file-storage/save-rendered-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  (fn [{:keys [db]} [_ file-id]]
      (let [rendered-directory-id (r get-rendered-directory-id db)
            file-alias            (r engine/get-file-prop db rendered-directory-id file-id :file/alias)
            filename              (r engine/get-file-prop db rendered-directory-id file-id :file/filename)
            uri                   (media/filename->media-storage-uri filename)]
           [:x.app-tools.file-saver/save-file! {:filename file-alias :uri uri}])))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-storage/->directory-data-downloaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) directory-id
  (fn [{:keys [db]} [_ directory-id]]
      (let [namespace (a/get-namespace ::this)]
           {:db (r engine/set-rendered-directory-id! db namespace directory-id)
            :dispatch [:file-storage/set-window-title!]})))



;; -- Control bar components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-storage-filter-items-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)
  ;   :listening-to-request? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [directory-empty? listening-to-request?]}]
  (let [field-disabled? (or directory-empty? listening-to-request?)]
       [elements/text-field ::filter-items-field
                            {:auto-focus? true
                             :disabled?   field-disabled?
                             :emptiable?  true
                             :placeholder :filter-items!}]))

(defn- file-storage-capacity-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:storage-free-capacity (B)
  ;   :storage-used-capacity (B)}
  ;
  ; @return (component)
  [_ {:keys [storage-free-capacity storage-used-capacity] :as view-props}]
  (let [capacity-indicator-label    (view-props->capacity-indicator-label view-props)
        capacity-indicator-sections [{:value storage-used-capacity :color :primary}
                                     {:value storage-free-capacity :color :highlight}]]
       [elements/line-diagram ::capacity-indicator
                              {:color          :muted
                               :font-size      :xxs
                               :label          capacity-indicator-label
                               :label-position :center
                               :sections       capacity-indicator-sections
                               :width          112}]))

(defn- file-storage-bin-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::bin-button
                   {:color    :default
                    :icon     :delete_outline
                    :layout   :icon-button
                    :on-click [:x.app-router/go-to! (path-param->file-storage-uri "bin")]
                    :variant  :transparent}])

(defn- file-storage-directory-actions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-exists? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-exists?]}]
  (let [namespace (a/get-namespace ::this)]
       [:<> [directory-actions/file-upload-button      component-id
                                                       {:disabled? (not   directory-exists?)
                                                        :namespace (param namespace)}]
            [directory-actions/create-directory-button component-id
                                                       {:disabled? (not   directory-exists?)
                                                        :namespace (param namespace)}]]))

(defn file-storage-control-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [elements/polarity ::control-bar
                     {:start-content [:<> [file-storage-directory-actions component-id view-props]
                                         ;[elements/vertical-line {:color :default :layout :row}]
                                         ;[elements/separator     {:orientation :vertical :size :m}]
                                          [file-storage-filter-items-field component-id view-props]]
                      :end-content   [:<> [elements/separator {:orientation :vertical :size :m}]
                                          [file-storage-capacity-indicator component-id view-props]
                                          [elements/separator {:orientation :vertical :size :m}]]}])
                                         ;[file-storage-bin-button component-id view-props]
                                         ;[elements/separator {:orientation :vertical :size :m}]



;; -- Directory label bar components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-storage-home-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ {:keys [directory-exists?] :as view-props}]
  [elements/button ::home-button
                   {:color     :default
                    :disabled? (or (engine/view-props->root-level? view-props)
                                   (boolean directory-exists?))
                    :icon      :home
                    :layout    :icon-button
                    :tooltip   :my-storage
                    :variant   :transparent
                    :width     :fit}])

(defn file-storage-up-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [_ view-props]
  [elements/button ::up-button
                   {:disabled? (engine/view-props->root-level? view-props)
                    :icon      :chevron_left
                    :layout    :icon-button
                    :on-click  [:file-storage/go-up!]
                    :tooltip   :back!
                    :variant   :transparent
                    :width     :fit}])

(defn file-storage-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-alias (string)}
  ;
  ; @return (component)
  [_ {:keys [directory-alias]}]
  [elements/label ::label
                  {:content directory-alias :horizontal-align :left}])

(defn file-storage-order-by-select
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)}
  ;
  ; @return (component)
  [_ {:keys [directory-empty? listening-to-request?]}]
  [elements/button ::order-by-select-button
                   {:color     :default
                    :disabled? (or directory-empty? listening-to-request?)
                    :icon      :sort
                    :label     :order-by
                    :layout    :icon-button
                    :variant   :transparent
                    :on-click  [:file-storage/render-order-by-select!]}])

(defn file-storage-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [:<> [elements/polarity ::label-bar
                          {:start-content [:<> [file-storage-home-button     component-id view-props]
                                               [file-storage-up-button       component-id view-props]
                                               [elements/separator {:orientation :vertical :size :s}]
                                               [file-storage-label           component-id view-props]]
                           :end-content   [:<> [file-storage-order-by-select component-id view-props]]}]
       [elements/separator {:orientation :horizontal :size :s}]])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-storage-subdirectory
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ; @param (keyword) subdirectory-id
  ; @param (map) subdirectory-props
  ;  {:directory/alias (metamorphic-value)
  ;   :directory/content-size (B)
  ;   :directory/items-count (integer)}
  ;
  ; @return (component)
  [component-id view-props subdirectory-id {:directory/keys [alias content-size items-count]
                                            :as subdirectory-props}]
  (let [element-id           (engine/item-id->element-id subdirectory-id :file-storage)
        stickers             (file-storage-subdirectory-stickers component-id view-props subdirectory-id subdirectory-props)
        on-click-event       [:file-storage/go-to! subdirectory-id]
        on-right-click-event [:x.app-elements/render-context-surface! element-id]]
       [elements/directory element-id
                           {:content-size   content-size
                            :items-count    items-count
                            :label          alias
                            :on-click       on-click-event
                            :on-right-click on-right-click-event
                            :stickers       stickers
                            :context-surface {:content      #'context-menu/file-storage-subdirectory-context-menu
                                              :content-props subdirectory-props}}]))

(defn- file-storage-subdirectory-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:filtered-subdirectories (maps in vector)}
  ;
  ; @return (component)
  [component-id {:keys [filtered-subdirectories] :as view-props}]
  (reduce (fn [directory-list subdirectory-id]
              (let [subdirectory-props (get filtered-subdirectories subdirectory-id)]
                   (vector/conj-item directory-list ^{:key subdirectory-id}
                                     [file-storage-subdirectory component-id
                                      view-props subdirectory-id subdirectory-props])))
          [:<>]
          (engine/view-props->ordered-subdirectories view-props)))

(defn- file-storage-subdirectories
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
                         {:content [file-storage-subdirectory-list component-id view-props]
                          :wrap-items? true}]
           (if directory-render-files?
               [:<> [elements/separator {:orientation :horizontal :size :s}]
                    [elements/horizontal-line {:color :highlight}]
                    [elements/separator {:orientation :horizontal :size :s}]])]))

(defn- file-storage-file
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
  [component-id view-props file-id {:file/keys [alias filesize modified-at] :as file-props}]
  (let [element-id           (engine/item-id->element-id file-id :file-storage)
        stickers             (file-storage-file-stickers component-id view-props file-id file-props)
        timestamp            (time/timestamp->date-and-time modified-at :yyyymmdd :hhmm)
        on-right-click-event [:x.app-elements/render-context-surface! element-id]
        thumbnail-uri        (engine/file-props->thumbnail-uri file-props)]
       [elements/file element-id
                      {:filesize       filesize
                       :label          alias
                       :on-right-click on-right-click-event
                       :stickers       stickers
                       :timestamp      timestamp
                       :thumbnail-uri  thumbnail-uri
                       :context-surface {:content       #'context-menu/file-storage-file-context-menu
                                         :content-props file-props}}]))

(defn- file-storage-file-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:filtered-files (maps in vector)}
  ;
  ; @return (component)
  [component-id {:keys [filtered-files] :as view-props}]
  (reduce (fn [file-list file-id]
              (let [file-props (get filtered-files file-id)]
                   (vector/conj-item file-list ^{:key file-id}
                                     [file-storage-file component-id view-props file-id file-props])))
          [:<>]
          (engine/view-props->ordered-files view-props)))

(defn- file-storage-files
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
                    {:content [file-storage-file-list component-id view-props]
                     :wrap-items? true}]))

(defn- file-storage-file-drop-area
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
 ;[elements/file-drop-area ::file-drop-area
 ;                         {:font-size :xs}]
  [elements/label {:content :empty-directory :color :highlight}])

(defn- file-storage-directory-empty
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-empty? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-empty? directory-exists?] :as view-props}]
  (if (and directory-empty? directory-exists?)
      [:<> [elements/separator {:orientation :horizontal :size :s}]
           [elements/polarity  ::directory-empty
                               {:middle-content [file-storage-file-drop-area component-id view-props]}]
           [elements/separator {:orientation :horizontal :size :s}]]))

(defn- file-storage-directory-not-exists
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;  {:directory-exists? (boolean)}
  ;
  ; @return (component)
  [component-id {:keys [directory-exists?] :as view-props}]
  (if (not directory-exists?)
      [:<> [elements/separator {:orientation :horizontal :size :s}]
           [elements/polarity  ::directory-not-exists
                               {:middle-content [elements/label {:content :directory-does-not-exists}]}]
           [elements/separator {:orientation :horizontal :size :s}]]))

(defn- file-storage-no-filtered-items-match
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

(defn- file-storage
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [:<> [file-storage-label-bar               component-id view-props]
       [file-storage-subdirectories          component-id view-props]
       [file-storage-files                   component-id view-props]
       [file-storage-directory-empty         component-id view-props]
       [file-storage-directory-not-exists    component-id view-props]
       [file-storage-no-filtered-items-match component-id view-props]
       [elements/separator {:orientation :horizontal :size :xs}]])

(defn ghost-file-storage-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [elements/polarity ::ghost-label-bar
                     {:end-content [file-storage-order-by-select component-id view-props]}])

(defn- ghost-file-storage
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [component-id view-props]
  [:<> [ghost-file-storage-label-bar component-id view-props]
       [elements/polarity  {:middle-content [ui/loading-animation-d]}]
       [elements/separator {:orientation :horizontal :size :s}]])

(defn- ghost-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (component)
  [_]
  [elements/box {:content    #'ghost-file-storage
                 :min-width  :xxl
                 :subscriber [:file-storage/get-view-props]}])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ;
  ; @return (component)
  [_]
  [elements/box {:content          #'file-storage
                 :horizontal-align :left
                 :min-width        :xxl
                 :subscriber       [:file-storage/get-view-props]}])

(defn- listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (component)
  [_]
  (let [namespace (a/get-namespace ::this)
        query-id  (engine/namespace->query-id namespace)]
       [components/listener {:content         #'view
                             :pending-content #'ghost-view
                             :request-id      query-id}]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :file-storage/initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) storage-id
  (fn [{:keys [db]} [_ storage-id]]
      (let [directory-id     (r get-current-route-directory-id db)
            namespace        (a/get-namespace ::this)
            on-success-event [:file-storage/->directory-data-downloaded directory-id]]
           [:media-storage/download-directory-data!
             (engine/namespace->query-id namespace)
             {:directory-id directory-id
              :on-success   on-success-event}])))

(a/reg-event-fx
  :file-storage/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) storage-id
  ; @param (map) storage-props
  ;
  ; @usage
  ;  [:file-storage/load! {...}]
  ;
  ; @usage
  ;  [:file-storage/load! :my-storage {...}]
  (fn [{:keys [db]} event-vector]
      (let [storage-id    (a/event-vector->second-id   event-vector)
            storage-props (a/event-vector->first-props event-vector)]

           ;:db (r store-storage-props! db storage-id storage-props)
           {:dispatch-n [[:file-storage/initialize! storage-id]
                         [:file-storage/render!     storage-id]]})))

(a/reg-event-fx
  :file-storage/render-rendered-subdirectory-properties!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) subdirectory-id
  (fn [{:keys [db]} [_ subdirectory-id]]
      (let [rendered-directory-id (r get-rendered-directory-id db)]
           [:media-storage/render-subdirectory-properties! rendered-directory-id subdirectory-id])))

(a/reg-event-fx
  :file-storage/render-rendered-file-properties!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) file-id
  (fn [{:keys [db]} [_ file-id]]
      (let [rendered-directory-id (r get-rendered-directory-id db)]
           [:media-storage/render-file-properties! rendered-directory-id file-id])))

(a/reg-event-fx
  :file-storage/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) storage-id
  (fn [{:keys [db]} [_ storage-id]]
      [:x.app-ui/set-surface!
        :file-storage/view
        {:content     #'listener
         :label-bar   {:content       #'ui/go-home-surface-label-bar
                       :content-props {:label (r get-surface-label db)}}
         :control-bar {:content       #'file-storage-control-bar
                       :subscriber    [:file-storage/get-view-props]}}]))

(a/reg-event-fx
  :file-storage/on-app-init
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:x.app-router/add-route!
                 ::default-route
                 {:restricted?    true
                  :route-event    [:file-storage/load!]
                  :route-template FILE-STORAGE-HOME-URI}]
                [:x.app-router/add-route!
                 ::extended-route
                 {:restricted?    true
                  :route-event    [:file-storage/load!]
                  :route-template (path-param->file-storage-uri ":directory-id")}]]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:file-storage/on-app-init]})
